---
slug: enable-google-cloud-storage-logging
title: Enable Logging for Google Cloud Storage Buckets and Analyzing Logs in Big Query (Part I)
author: Jeffrey Aven
author_title: Cloud Consultant
author_url: https://github.com/stackql
hide_table_of_contents: false
author_image_url: https://s.gravatar.com/avatar/f96573d092470c74be233e1dded5376f?s=80
image: https://storage.googleapis.com/stackql-web-assets/blog/stackql-gcs-logging-to-bq.png
description: This post demonstrates how to enable logging for a Google Cloud Storage bucket and analyze usage logs in Big Query using StackQL - a new, SQL based approach to deploying and querying cloud resources.
keywords: [stackql, google cloud, GCP, infracoding, IaC, infrastructure as code, google cloud storage, cloud storage, GCS, logging, bigquery]
tags: [stackql, google cloud, GCP, infracoding, IaC, infrastructure as code, google cloud storage, cloud storage, GCS, logging, bigquery]
---

In a previous article, [__Deploying and Querying GCS Buckets using StackQL__](/blog/deploying-and-querying-gcs-buckets-using-stackql), we walked through some basic creation and query operations on Google Cloud Storage buckets.  In this post we will extend on this by enabling logging on a GCS bucket using StackQL.  This post is based upon this article: [Usage logs & storage logs](https://cloud.google.com/storage/docs/access-logs).  

Assuming we have deployed a bucket which we want to log activities on, follow the steps below:  

## Step 1 : Create a bucket to store the usage logs

One bucket in a project can be used to collect the usage logs from one or more other buckets in the project.  Use the StackQL Command Shell (`stackql shell`) or `stackql exec` to create this logs bucket as shown here:  

```jsx
INSERT INTO google.storage.buckets(
  project,
  data__name,
  data__location,
  data__locationType
)
SELECT
  'stackql',
  'stackql-download-logs',
  'US',
  'multi-region'
;
```

> for more examples of creating Google Cloud Storage buckets using StackQL, see  [Deploying and Querying GCS Buckets using StackQL](/blog/deploying-and-querying-gcs-buckets-using-stackql).  

## Step 2: Set IAM policy for the logs bucket

You will need to create an IAM binding to enable writes to this bucket, do this by using the `setIamPolicy` method as shown here:  

```jsx
EXEC google.storage.buckets.setIamPolicy
@bucket = 'stackql-download-logs'
@@json = '{
  "bindings":[
    {
      "role": "roles/storage.legacyBucketWriter",
      "members":[
        "group:cloud-storage-analytics@google.com"
      ]
    }
  ]
}';
```

> TIP: you should also add role bindings to the `roles/storage.legacyBucketOwner` role for serviceAccount or users who will be running StackQL `SELECT` queries against this logs bucket.  

## Step 3: Enable logging on the target bucket

To enable logging on your target bucket (or buckets) run the following StackQL `EXEC` method:  

```jsx
EXEC google.storage.buckets.patch
@bucket = 'stackql-downloads'
@@json = '{
 "logging": {
  "logBucket": "stackql-download-logs",
  "logObjectPrefix": "stackql_downloads"
 }
}';
```

> TIP: use `SHOW METHODS IN google.storage.buckets;` to see what operations are avaialable such as the `patch` and `setIamPolicy` examples shown in the previous steps.  

## Step 4: Check logging status on target bucket

To see that logging has been enabled run the StackQL query below:  

```jsx
select name, logging
from google.storage.buckets
WHERE project = 'stackql'
and logging is not null;
```

To unpack the `logging` object, you can use the [`JSON_EXTRACT`]](/docs/language-spec/functions/json/json_extract) built in function as shown here:  

```jsx
select name, json_extract(logging, '$.logBucket') as logBucket,
json_extract(logging, '$.logObjectPrefix') as logObjectPrefix
from google.storage.buckets
WHERE project = 'stackql'
and logging is not null;
```

In Part II of this post, we will demonstrate how to create a Big Query dataset, then load and analyze the GCS usage logs you have collected using Big Query, stay tuned!  