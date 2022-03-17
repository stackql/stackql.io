---
slug: deploying-and-querying-gcs-buckets-using-stackql
title: Deploying and Querying GCS Buckets using StackQL
author: Jeffrey Aven
author_title: Cloud Consultant
author_url: https://github.com/stackql
hide_table_of_contents: false
author_image_url: https://s.gravatar.com/avatar/f96573d092470c74be233e1dded5376f?s=80
image: /img/blog/infraql-gcs-blog.png
description: StackQL is a simple way to automate the deployment, configuration, management, and removal of Google Cloud Storage buckets, as well as to query buckets.
keywords: [stackql, google cloud, GCP, infracoding, IaC, infrastructure as code, google cloud storage, cloud storage, GCS]
tags: [stackql, google cloud, GCP, infracoding, IaC, infrastructure as code, google cloud storage, cloud storage, GCS]
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

StackQL is a simple way to automate the deployment, configuration, management, and removal of Google Cloud Storage buckets, as well as an easy way to query buckets in your environment for asset management reasons or to look for misconfigurations, such as public access, non-conformant encryption configuration and more.  It may be useful to review [__Exploring GCP services and resources using StackQL__](/blog/exploring-gcp-services-and-resources-using-stackql), which walks working through the StackQL resource hierarchy including the [`SHOW`](/docs/language-spec/show) and [`DESCRIBE`](/docs/language-spec/describe) commands.  

## Generate an `INSERT` template for a new bucket  

The [`SHOW INSERT`](/docs/language-spec/show) command in StackQL can be used to generate an [`INSERT`](/docs/language-spec/show) template which can be used to create any resources in GCP.  The easiest way to use this command is via the command line using the text output type and supressing column headers using the `-H` flag, the StackQL interactive shell can be used as well.

<Tabs
  defaultValue="shell"
  values={[
    { label: 'Interactive Shell', value: 'shell', },
    { label: 'Command Line', value: 'cmd', },
  ]
}>
<TabItem value="shell">

```jsx
SHOW INSERT INTO google.storage.buckets;
```
</TabItem>
<TabItem value="cmd">

```bash
stackql exec "SHOW INSERT INTO google.storage.buckets" --output text -H
```
</TabItem>
</Tabs>

To only show the mandatory fields for a resource, you can use the `/*+ REQUIRED */` query hint.  

You could also use the [`--outfile`](/docs/command-line-usage/global-flags) flag to write the template to a new IQL file, or pipe the results to a file using the appropriate shell operator (e.g. `>` or `>>`).

> More information on generating templates can be found at [__Creating Infrastructure Templates__](/docs/getting-started/templating)

## Create a bucket

Now that you have a template, you can curate this to the fields you desire.  Executing an [`INSERT`](/docs/language-spec/insert) command to create a bucket is as easy as:  

```jsx
-- change this for your project and bucket names
INSERT INTO google.storage.buckets(
  project,
  data__name,
  data__location,
  data__locationType,
  data__labels
)
SELECT
  'stackql-demo',
  'stackql-demo-bucket',
  'US',
  'multi-region',
  '[{"env":"demo"}]'
;
```

## Removing buckets

In some cases you may want to retire and remove buckets that are no longer needed, doing so is easy in StackQL using the [`DELETE`](/docs/language-spec/delete) command this can be done individually or in a batch (as we will demonstrate later).

> __NOTE__ that buckets cannot be deleted if they contain any objects, we will show examples of working with objects in future posts  

To delete the bucket you created in the previous step, run the following:    

```jsx
DELETE FROM google.storage.buckets
WHERE bucket = 'stackql-demo-bucket';
```

## Using variables to automate bucket creation

Although the previous example was simple, providing static values for attributes is not very scalable.  In most cases, configuration for resource creation and modification is provided as data associated with an StackQL script or module using `json` or `jsonnet` files.

This can be supplied inline in the IQL script or provided as a separate file, e.g. `vars.jsonnet`.  For more information, see [__Using Variables__](/docs/getting-started/variables).

The example below demonstrates how to create 3 different buckets using configuration data provided using `jsonnet`.

> __TIP__ Use Jsonnet where possible as it is a much more powerful and feature rich configuration and data templating language supporting inheritance, external variables, parametrization, functions and operators, and much more.  For more information visit [https://jsonnet.org/](https://jsonnet.org/).

__Step 1:__  Save the following `jsonnet` configuration to a file named __`cloud_storage_vars.jsonnet`__:   

```jsx
local project = 'stackql-demo'; // update to your project name

{
  project: project,
  location: 'US',
  locationType: 'multi-region',
  buckets: [
    {
      name: project + '-bucket1', 
      label: [{"env":"demo1"}],
    },
    {
      name: project + '-bucket2',  
      label: [{"env":"demo2"}],
    },
    {
      name: project + '-bucket3',  
      label: [{"env":"demo3"}],
    },
  ]
} 
```
__Step 2:__  Save the following StackQL script to a file named __`deploy_buckets.iql`__:  

```jsx
{{range .buckets}}
INSERT INTO google.storage.buckets(
  project,
  data__location,
  data__locationType,
  data__name,
  data__labels
)
SELECT
  '{{ $.project }}',
  '{{ $.location }}',
  '{{ $.locationType }}',
  '{{ .name }}',
  '{{ .label }}'
;
{{end}}
```
__Step 3:__  *(Optional)* Perform a `dryrun` operation to see what the resultant query would be  

With any modification operation which sources data from a `jsonnet` file it is useful to perform a `dryrun` operation first to ensure the variable substitution is as you intended.  This is done by using the [`dryrun`](/docs/command-line-usage/global-flags) flag as shown here:

```bash
stackql exec --iqldata ./cloud_storage_vars.jsonnet --infile ./deploy_buckets.iql --dryrun --output text -H
```

__Step 4:__  Run the query  

If you are satisfied with the results of the dry run, remove the `dryrun` and `output` flags (which are not needed) and run the command, as shown here (note that if you are creating or deploying resources in GCP you will need to supply a valid service account key file or authenticate interactively):

```bash
stackql exec --iqldata ./cloud_storage_vars.jsonnet --infile ./deploy_buckets.iql --keyfilepath /mnt/c/tmp/stackql-demo.json
```
## Querying buckets using `SELECT` statements

This is easy...  

```jsx
SELECT id, name, location, locationType, timeCreated
FROM google.storage.buckets
WHERE project = 'stackql-demo';
/* returns
|----------------------|----------------------|----------|--------------|--------------------------|
|          id          |         name         | location | locationType |       timeCreated        |
|----------------------|----------------------|----------|--------------|--------------------------|
| stackql-demo-bucket1 | stackql-demo-bucket1 | US       | multi-region | 2021-08-10T21:32:25.847Z |
|----------------------|----------------------|----------|--------------|--------------------------|
| stackql-demo-bucket2 | stackql-demo-bucket2 | US       | multi-region | 2021-08-10T21:32:26.877Z |
|----------------------|----------------------|----------|--------------|--------------------------|
| stackql-demo-bucket3 | stackql-demo-bucket3 | US       | multi-region | 2021-08-10T21:32:27.864Z |
|----------------------|----------------------|----------|--------------|--------------------------|
*/
```

To order the result set by `timeCreated` latest first...  

```jsx
SELECT id, name, location, locationType,timeCreated
FROM google.storage.buckets
WHERE project = 'stackql-demo'
ORDER BY timeCreated DESC;
```

Or a simple count:  

```jsx
SELECT COUNT(*) as 'Number of Buckets'
FROM google.storage.buckets
WHERE project = 'stackql-demo'
AND name LIKE '%stackql-demo-bucket%';
/* returns
|-------------------|
| Number of Buckets |
|-------------------|
|                 3 |
|-------------------|
*/
```

## Cleanup

To clean up the environment and remove the test buckets, copy the following code to a file named `bucket_cleanup.iql`.  

```jsx
{{range .buckets}}
DELETE FROM google.storage.buckets
WHERE bucket = '{{.name}}';
{{end}}
```

> Since we are in the danger zone here (deleting multiple persistent data objects) you may want to be sure you haven’t fat-fingered anything and don’t remove anything you didn’t intend to...  To do this, use the `dryrun` option shown earlier to run the script and preprocess the input data only and show the templated command before you run it.  

When you are ready to go you can run this command:  

```jsx
stackql exec --iqldata ./cloud_storage_vars.jsonnet --infile ./bucket_cleanup.iql --keyfilepath /mnt/c/tmp/stackql-demo.json
```

Now you should see a filtered count return `0` resources as shown here:  

```jsx
SELECT COUNT(*) as 'Number of Buckets'
FROM google.storage.buckets
WHERE project = 'stackql-demo'
AND name LIKE '%stackql-demo-bucket%';
/* returns
|-------------------|
| Number of Buckets |
|-------------------|
|                 0 |
|-------------------|
*/
```

Future posts will demonstrate more advanced query and deployment operations with Cloud Storage, such as working with encryption using CMEK in KMS, configuring logging, cors, website access, logging configuration, acls and IAM.  See ya then!