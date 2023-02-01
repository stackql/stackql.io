---
slug: cross-cloud-queries-with-stackql
title: Cross Cloud Queries with StackQL
hide_table_of_contents: false
authors:	
  - kieranrimmer
image: "/img/blog/cross-cloud-queries-with-stackql.png"
description: This is a how-to article demonstrating the use of stackql to query across AWS and Google cloud environments and combine data in real-time.
keywords: [stackql, aws, google, multicloud, observability, cloud security, analysis, analytics]
tags: [stackql, aws, google, multicloud, observability, cloud security, analysis, analytics]
---

This exercise will show you how to run a real-time query across your AWS and Google cloud environments.  You may do this for inventory analysis, security analysis, or any other reason you can think of.  We will use `stackql` to query the state of your cloud resources across your AWS and Google environments.  You can also use `stackql` to provision, de-provision or manage resources across different cloud and SaaS providers.   

The steps we will take are:  

1. Prepare your environment for `stackql` usage.
2. Use `stackql` to provision some resources in cloud.  **optional**
3. Use `stackql` to query resources present in the cloud.
4. Use `stackql` to tear down resources created in step (2), if any.  **Important**: you must destroy any resources created through this exercise, or you will incur ongoing charges.

<iframe width="560" height="315" src="https://www.youtube.com/embed/4ukV1xQ-EMU" title="YouTube video player" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" allowfullscreen></iframe>

## Preparation

For this exercise, credentials with privileges against google and aws are required. It is outside the scope of this document to go into great detail on the various topics and options relevant to this. Instead, the below steps provide both: (i) reference to vendor documentation and (ii) suggestions for workarounds to get yourself going. 

### for old hands

All the materials required for this exercise are:

1. A current `stackql` executable.
2. A Google Service Account Key JSON file, where the corresponding Service Account possesses permissions sufficient to create, interrogate and delete `compute` block storage.
1. AWS credentials stored in the traditional `AWS_ACCESS_KEY_ID` and `AWS_SECRET_ACCESS_KEY` environment variables, where the corresponding Service Account possesses permissions sufficient to create, interrogate and delete `ec2` block storage.

### step by step

First, please do the following:

1. Download and install `stackql` [__from our website__](https://stackql.io/downloads).
2. For google:
    - (i) Create and download a Google Service Account Key as per [__Google documentation__](https://cloud.google.com/iam/docs/creating-managing-service-account-keys). Remember the location of your key file.
    - (ii) You will need to grant the Service Account at least read, list, create, and delete privileges. For more information about google `iam` and Service Accounts in particular, please consult the [__documentation__](https://cloud.google.com/iam/docs/service-accounts). For this exercise, grant your service account the [__`roles/compute.storageAdmin`__](https://cloud.google.com/iam/docs/understanding-roles#compute.storageAdmin) role would be adequate.
3. For AWS:
    - (i) Create and download AWS user credentials as per [__AWS documentation__](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_credentials_access-keys.html#Using_CreateAccessKey). We will require long-lived credentials. In keeping with vendor advice, we strongly recommend against using root user credentials. We have created a dedicated CICD user for this exercise.
    - (ii) Set up the AWS CLI environment variables as per the [__documentation__](https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-envvars.html).
    - (iii) The user will need create / read / delete privileges against `ec2` volumes. This can be done though the [__AWS IAM console__](https://docs.aws.amazon.com/IAM/latest/UserGuide/id.html) in various ways. For example, one can use groups and permission policies. Adding your user to a group with `AmazonEC2FullAccess` will certainly work, although lesser privileges may be adequate.

Then, create some shell variables:

```bash
# you will need to edit the file path as appropriate

GOOGLE_DOWNLOADED_KEY_FILE_PATH="/path/to/your/downloaded/key.json"

AWS_AUTH_FRAGMENT='{ "type": "aws_signing_v4", "credentialsenvvar": "AWS_SECRET_ACCESS_KEY", "keyIDenvvar": "AWS_ACCESS_KEY_ID" }'

GOOGLE_AUTH_FRAGMENT='{ "credentialsfilepath": "'"${GOOGLE_DOWNLOADED_KEY_FILE_PATH}"'", "type": "service_account" }'

export STACKQL_AUTH_CTX='{ "aws":  '"${AWS_AUTH_FRAGMENT}"', "google": '"${GOOGLE_AUTH_FRAGMENT}"'  }'
```

<details>
  <summary>Setting up Provider Auth in PowerShell</summary>

```powershell
$GOOGLE_DOWNLOADED_KEY_FILE_PATH = "C:\path\to\your\downloaded\key.json"

$AWS_AUTH_FRAGMENT = '{ "type": "aws_signing_v4", "credentialsenvvar": "AWS_SECRET_ACCESS_KEY", "keyIDenvvar": "AWS_ACCESS_KEY_ID" }'

$GOOGLE_AUTH_FRAGMENT = '{ "credentialsfilepath": "' + $GOOGLE_DOWNLOADED_KEY_FILE_PATH + '", "type": "service_account" }'

$env:STACKQL_AUTH_CTX = '{ "aws": ' + $AWS_AUTH_FRAGMENT + ', "google": ' + $GOOGLE_AUTH_FRAGMENT + ' }'
```

</details>

## Start a `stackql shell` session

To start an interactive shell session, in the same shell you setup your envrioment variables, run:

```bash
stackql --auth="${STACKQL_AUTH_CTX}" shell
```

You can exit at any time with `ctrl + C`.

## Setup and meta queries to get started

StackQL providers are installed from the StackQL Provider Registry using the [__`REGISTRY`__](https://stackql.io/docs/language-spec/registry) command.  StackQL supports *meta queries* such as [__`SHOW`__](https://stackql.io/docs/language-spec/show) and [__`DESCRIBE`__](https://stackql.io/docs/language-spec/describe) which can be used to explore the available services, resources, fields, and operations available in a given cloud or SaaS provider.    

```sql
-- see available providers
registry pull list;

-- pull the required providers
registry pull google;

registry pull aws;

-- some the installed providers
show providers;

-- some meta queries
show services in google;

show resources in google.compute;

describe google.compute.disks;
```

## Create block storage (optional)

You will need to replace the items in `<ANGLE_BRACKETS>`.

```sql
-- create a google volume, await and verify creation completes successfully
insert /*+ AWAIT */ into google.compute.disks(
  project, 
  zone, 
  data__name, 
  data__sizeGb
) 
select 
  '<YOUR_GCP_PROJECT>', 
  'australia-southeast1-a', 
  'my-stackql-demo-disk-01', 
  '10' ;

-- create an aws volume, operation despatched on a BEST EFFORT basis
insert into aws.ec2.volumes(
  AvailabilityZone, 
  Size, 
  region) 
select 
  'ap-southeast-2a', 
  10, 
  'ap-southeast-2';
```

## Interrogate cloud block storage

```sql

-- query one resource from google
select 
 name, 
 split_part(split_part(type, '/', 11), '-', 2) as type, 
 status, 
 sizeGb as size 
from google.compute.disks 
 where project = '<YOUR_GCP_PROJECT>' 
 and zone = 'australia-southeast1-a';

-- query the equivalent from aws
select 
 volumeId as name, 
 volumeType as type, 
 status, 
 size 
from aws.ec2.volumes 
 where region = 'ap-southeast-2';

-- union the equivalent resources across clouds
select 
 'google' as vendor, 
 name, 
 split_part(split_part(type, '/', 11), '-', 2) as type, 
 status, 
 sizeGb as size 
from google.compute.disks 
 where project = '<YOUR_GCP_PROJECT>' 
 and zone = 'australia-southeast1-a'
union
select 
 'aws' as vendor, 
 volumeId as name, 
 volumeType as type, 
 status, 
 size 
from aws.ec2.volumes 
 where region = 'ap-southeast-2';

-- create a view for convenience
create view dual_cloud_block_storage as
select 
 'google' as vendor, 
 name, 
 split_part(split_part(type, '/', 11), '-', 2) as type, 
 status, 
 sizeGb as size 
from google.compute.disks 
 where project = '<YOUR_GCP_PROJECT>' 
 and zone = 'australia-southeast1-a'
union
select 
 'aws' as vendor, 
 volumeId as name, 
 volumeType as type, 
 status, 
 size 
from aws.ec2.volumes 
 where region = 'ap-southeast-2';

-- select from the newly created view, with ordering
select * from dual_cloud_block_storage order by name desc;
```

## Delete block storage (if required)

This will only work if the disks are deletable. For example, `aws.ec2.volumes` must have `status` = `available`; you can check this with the view we created above.

```sql
/* delete a google volume, await and verify creation completes successfully.
One at a time only... */
delete /*+ AWAIT */ from google.compute.disks
 where project = '<YOUR_GCP_PROJECT>' 
 and zone = 'australia-southeast1-a' 
 and disk = 'my-stackql-demo-disk-01';

-- delete an aws volume, operation despatched on a BEST EFFORT basis
delete from aws.ec2.volumes 
 where VolumeId = 'vol-049ee07b31aff451a'  
 and region = 'ap-southeast-2';
```

## Verify the cleanup was successful

```sql
select * from dual_cloud_block_storage order by name desc;
```

That's it for the scripted demo!

## Get involved

:::info We Need Your Help!

if you find bugs, want features, have tech questions then go to [__github.com/stackql/stackql/issues__](https://github.com/stackql/stackql/issues) and raise the appropriate issue 🙏

:::

