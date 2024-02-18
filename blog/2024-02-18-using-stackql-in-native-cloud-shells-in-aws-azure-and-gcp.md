---
slug: using-stackql-in-native-cloud-shells-in-aws-azure-and-gcp
title: Using StackQL in Native Cloud Shells in AWS, Azure and GCP
hide_table_of_contents: false
authors:	
  - jeffreyaven
image: "/img/blog/stackql-multicloud.png"
description: Run StackQL direct in AWS, Azure and GCP Cloud Shells.
keywords: [azure, aws, google, stackql, stackql provider registry, multicloud, asset management, cloud security]
tags: [azure, aws, google, stackql, stackql provider registry, multicloud, asset management, cloud security]
---

> [__StackQL__](https://github.com/stackql/stackql) allows you to query and interact with your cloud and SaaS assets using a simple SQL framework.  Use cases include CSPM, asset inventory and analysis, finops and more, as well as our IaC and ops (lifecycle management).  

The three major cloud providers all offer a built-in Linux shell for executing commands using their respective CLIs; in some cases, they come with tools like `terraform` pre-installed. They are pre-authorized with your credentials in the cloud console for the user you authenticated with.  

Now you can easily use `stackql` - a unified analytics and IaC dev tool - in all major cloud providers' built-in shells, using cloud shell scripts packaged with the `stackql` Linux binary (available from v0.5.587 onwards).  

StackQL is particularly useful for asynchronously querying across regions in AWS, projects in Google, or resource groups in Azure, which is challenging to do via the CLIs.  For example:  

```sql
SELECT region, COUNT(*) as num_functions
FROM aws.lambda.functions
WHERE region IN (
	'us-east-1','us-east-2','us-west-1','us-west-2',
	'ap-south-1','ap-northeast-3','ap-northeast-2',
	'ap-southeast-1','ap-southeast-2','ap-northeast-1',
	'ca-central-1','eu-central-1','eu-west-1',
	'eu-west-2','eu-west-3','eu-north-1','sa-east-1')
GROUP BY region;
```

Additionally, you could authenticate to another provider from one cloud shell simultaneously and run multi-cloud inventory commands. For example:  

```sql
SELECT 
  name, 
  SPLIT_PART(machineType, '/', -1) as instance_type,
  'google' as provider
  FROM google.compute.instances 
  WHERE project IN ('myproject1','myproject2')
UNION
SELECT
  instanceId as name,
  instanceType as instance_type,
  'aws' as provider
  FROM aws.ec2.instances 
  WHERE region IN (
	'us-east-1','us-east-2','us-west-1','us-west-2',
	'ap-south-1','ap-northeast-3','ap-northeast-2',
	'ap-southeast-1','ap-southeast-2','ap-northeast-1',
	'ca-central-1','eu-central-1','eu-west-1',
	'eu-west-2','eu-west-3','eu-north-1','sa-east-1');
```

## Getting Started

To get started with StackQL in your preferred cloud shell environment, download the StackQL package using the following command:  

```shell
curl -L https://bit.ly/stackql-zip -O \
&& unzip stackql-zip
```

This command downloads the StackQL package, unzips it, and sets the appropriate permissions. From there, you can use our tailored scripts for AWS, Google Cloud, or Azure to integrate StackQL seamlessly into your cloud shell environment.  

## Using StackQL in the AWS Cloud Shell

Run the `stackql-aws-cloud-shell.sh` as follows to use the StackQL command shell within the AWS cloud shell:  

```shell
sh stackql-aws-cloud-shell.sh
```

An example is shown here:  

![aws-cloud-shell-example](/img/blog/aws-cloud-shell-example.png)

You can also run `stackql exec` commands using the stackql-aws-cloud-shell.sh script; for instance, this command will write a CSV file for the results of a query that could be downloaded from the Cloud Shell.  

```shell
sh stackql-aws-cloud-shell.sh exec \
--output csv --outfile instances.csv \
"SELECT region, instanceType FROM aws.ec2.instances WHERE region IN ('us-east-1')"
```

Additionally, you can supply an IAM role using the `--role-arn` argument to assume another role for your query or mutation operation, an example is shown here:  

```shell
sh stackql-aws-cloud-shell.sh \
--role-arn arn:aws:iam::824532806693:role/SecurityReviewerRole exec \
--infile query.iql \
--output csv --outfile output.csv
```

## Using StackQL in the Azure Cloud Shell

Run the `stackql-azure-cloud-shell.sh` as follows to open a StackQL command shell from the Azure Cloud Shell:  

```shell
sh stackql-azure-cloud-shell.sh
```

An example is shown here:  

![azure-cloud-shell-example](/img/blog/azure-cloud-shell-example.png)

Similar to the AWS script, you can also invoke `stackql exec` as well, an example is shown here:  

```shell
sh stackql-azure-cloud-shell.sh exec \
--output csv --outfile instances_by_location.csv \
"SELECT location, COUNT(*) as num_instances FROM azure.compute.virtual_machines WHERE resourceGroupName =  'stackql-ops-cicd-dev-01' AND subscriptionId = '631d1c6d-2a65-43e7-93c2-688bfe4e1468' GROUP BY location"
```

## Using StackQL in the Google Cloud Shell

Run the `stackql-google-cloud-shell.sh` as shown below to launch a StackQL command shell from within the google cloud shell:  

```shell
sh stackql-google-cloud-shell.sh
```

An example is shown here:  

![google-cloud-shell-example](/img/blog/google-cloud-shell-example.png)

As with the other two providers, you can run `exec` commands following the example below:  

```shell
sh stackql-google-cloud-shell.sh exec \
--output csv --outfile instances.csv \
"SELECT name, status FROM google.compute.instances WHERE project = 'stackql-demo'"
```

Please give us your feedback!  Star us at [__github.com/stackql__](https://github.com/stackql/stackql).