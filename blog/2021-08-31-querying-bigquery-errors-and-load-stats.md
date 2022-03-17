---
slug: querying-bigquery-errors-and-load-stats
title: Querying Big Query Errors and Load Stats
authors:	
  - jeffreyaven
image: "/img/blog/infraql-bq-errors-and-stats.png"
description: This article demonstrates some queries you can run using StackQL to bring back live statistics from Big Query as well as detail regarding encountered during the loading of data into Big Query.
keywords: [stackql, bigquery, gcp, load errors, load statistics, troubleshooting]
tags: [stackql, bigquery, gcp, load errors, load statistics, troubleshooting]
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

<head>
<meta name="author" content="Jeffrey Aven" />
</head>

Big Query provides a wealth of metrics and statistics for jobs run against it which could be queries, load jobs or export jobs.  This article demonstrates some queries you can run using StackQL to bring back live statistics from load operations into Big Query as well as detail regarding errors encountered during the loading of data into Big Query.  

## Loading Data into Big Query from GCS using StackQL

In a previous blog, we demonstrated how to [__create a Big Query dataset__](/blog/analyze-gcs-usage-logs-in-bigquery#step-1--create-a-big-query-dataset) and how to [__create a Big Query table__](/blog/analyze-gcs-usage-logs-in-bigquery#step-2--create-usage-table) using StackQL [__`INSERT`__](/docs/language-spec/insert) statements.  Having created a target dataset and table in Big Query, we can invoke a load job using StackQL by performing an `INSERT` into the `google.bigquery.jobs` resource.  

The data for this operation is shown in the __Data__ tab which is supplied in Jsonnet format.  

<Tabs
  defaultValue="iql"
  values={[
    { label: 'StackQL', value: 'iql', },
    { label: 'Data', value: 'data', },
  ]
}>
<TabItem value="iql">

```jsx
INSERT INTO google.bigquery.jobs(
  projectId,
  data__configuration
)
SELECT
  'stackql',
  '{{ .configuration }}'
;
```

</TabItem>
<TabItem value="data">

```jsx
{
  local project_id = 'stackql',
  local dataset_id = 'stackql_downloads',
  local table_name = 'usage',
  local bucket_name = 'stackql-download-logs',

  configuration: {
    load: {
      destinationTable: {
        projectId: project_id,
        datasetId: dataset_id,
        tableId: table_name,
      },
      sourceUris: [
	    "gs://%s/%s_%s_*" % [bucket_name, dataset_id, table_name],
      ],
      skipLeadingRows: 1,
      maxBadRecords: 0,
      projectionFields: [],
    }
  }
}
```

</TabItem>
</Tabs>

## Query for Big Query Errors

The [Big Query Job Object](https://cloud.google.com/bigquery/docs/reference/rest/v2/Job) can be queried using an StackQL  [__`SELECT`__](/docs/language-spec/select) statement.  

To see the available fields with their data types and descriptions, you can run the following StackQL [__`DESCRIBE`__](/docs/language-spec/describe) statement:

```jsx
DESCRIBE EXTENDED google.bigquery.jobs;
```

As you can see from running the above command or looking at the API documentation, there is a `state` field which is an enum showing the state of the job, since we are only concerned with completed jobs we will filter on jobs with a `state` of `DONE`.  The `errorResult` field is an object but its presence alone indicates that an error has occurred so we will add another filter to only show results where `errorResult` is not `null`.  

A simple query to start off with is to count the number of errors, this will be for all job types (`load`, `extract` and `query`):  

<Tabs
  defaultValue="iql"
  values={[
    { label: 'StackQL', value: 'iql', },
    { label: 'Results', value: 'results', },
  ]
}>
<TabItem value="iql">

```jsx
SELECT COUNT(*) as num_errors 
FROM google.bigquery.jobs
WHERE projectId = 'stackql'
AND state = 'DONE'
AND errorResult IS NOT null;
```

</TabItem>
<TabItem value="results">

```
|------------|
| num_errors |
|------------|
|          2 |
|------------|
```

</TabItem>
</Tabs>

To get a little more information about Big Query errors we can run a detailed query, extracting fields from the `errorResult` object using the [__`JSON_EXTRACT`__](/docs/language-spec/functions/json/json_extract) built in function.  This function is exceptionally useful as many of the fields returned from Google APIs are complex objects.  

<Tabs
  defaultValue="iql"
  values={[
    { label: 'StackQL', value: 'iql', },
    { label: 'Results', value: 'results', },
  ]
}>
<TabItem value="iql">

```jsx
SELECT id, JSON_EXTRACT(errorResult, '$.reason') AS errorReason
FROM google.bigquery.jobs
WHERE projectId = 'stackql'
AND state = 'DONE'
AND errorResult IS NOT null;
```

</TabItem>
<TabItem value="results">

```
|-----------------------------------------|--------------|
|                   id                    | errorReason  |
|-----------------------------------------|--------------|
| stackql:US.bquxjob_3016f574_17ba908f99f | invalidQuery |
|-----------------------------------------|--------------|
| stackql:US.job_zqyy5XkNSGFlAcP7MfyfO5KA | accessDenied |
|-----------------------------------------|--------------|
```

</TabItem>
</Tabs>

## Get Big Query Load Specific Errors

The previous queries returned all errors for all Big Query job types.  If we want to narrow our query to just Big Query `load` operations we can use the [Big Query JobStatistics](https://cloud.google.com/bigquery/docs/reference/rest/v2/Job#JobStatistics) object, which includes fields for each job type.  

To refine results to only `load` operations add the following expression to the `WHERE` clause:  

```jsx
AND JSON_EXTRACT(statistics, '$.load') IS NOT null;
```

Date values returned in job responses are in Unix timestamp format, to format them in a human readable format we can use the [__`DATETIME`__](/docs/language-spec/functions/datetime/datetime-fn) built in function.  Here is a more advanced example:  

<Tabs
  defaultValue="iql"
  values={[
    { label: 'StackQL', value: 'iql', },
    { label: 'Results', value: 'results', },
  ]
}>
<TabItem value="iql">

```jsx
SELECT id,
JSON_EXTRACT(errorResult, '$.message') AS errorMessage,
JSON_EXTRACT(errorResult, '$.reason') AS errorReason,
DATETIME(SUBSTR(JSON_EXTRACT(statistics, '$.creationTime'), 1, 10), 'unixepoch') AS creationTime,
DATETIME(SUBSTR(JSON_EXTRACT(statistics, '$.startTime'), 1, 10), 'unixepoch') AS startTime,
DATETIME(SUBSTR(JSON_EXTRACT(statistics, '$.endTime'), 1, 10), 'unixepoch') AS endTime
FROM google.bigquery.jobs
WHERE projectId = 'stackql'
AND state = 'DONE'
AND errorResult IS NOT null
AND JSON_EXTRACT(statistics, '$.load') IS NOT null;
```

</TabItem>
<TabItem value="results">

```
|---------------------------------------------|-----------------------------------------------------------|--------------|---------------------|---------------------|---------------------|
|                     id                      |                       errorMessage                        | errorReason  |    creationTime     |      startTime      |       endTime       |
|---------------------------------------------|-----------------------------------------------------------|--------------|---------------------|---------------------|---------------------|
| stackql:US.job_zqyy5XkNSGFlAcP7MfyfO5KAo7oA | Access Denied: File                                       | accessDenied | 2021-08-13 01:22:35 | 2021-08-13 01:22:36 | 2021-08-13 01:22:36 |
|                                             | gs://stackql-download-logs/stackql_downloads_usage_2021_: |              |                     |                     |                     |
|                                             | Access Denied                                             |              |                     |                     |                     |
|---------------------------------------------|-----------------------------------------------------------|--------------|---------------------|---------------------|---------------------|
| stackql:US.job_UGJCZTQNUBq7OKF62HRT5Ic0T7bx | Access Denied: File                                       | accessDenied | 2021-08-13 00:08:45 | 2021-08-13 00:08:45 | 2021-08-13 00:08:45 |
|                                             | gs://stackql-download-logs/stackql_downloads_usage_2021:  |              |                     |                     |                     |
|                                             | Access Denied                                             |              |                     |                     |                     |
|---------------------------------------------|-----------------------------------------------------------|--------------|---------------------|---------------------|---------------------|
| stackql:US.job_18YCXQmstlwhfj4Pzc3SA8-DOgYP | Access Denied: File                                       | accessDenied | 2021-08-12 23:59:27 | 2021-08-12 23:59:27 | 2021-08-12 23:59:27 |
|                                             | gs://stackql-download-logs/stackql_downloads_usage_2021:  |              |                     |                     |                     |
|                                             | Access Denied                                             |              |                     |                     |                     |
|---------------------------------------------|-----------------------------------------------------------|--------------|---------------------|---------------------|---------------------|
```

</TabItem>
</Tabs>

## Get Big Query Load Statistics

Now if you want to query for statistics for Big Query `load` operations which were successful, we can refine the query using the following conditions:  

```jsx
WHERE project = 'myproject'
AND state = 'DONE'
AND errorResult IS null
AND JSON_EXTRACT(statistics, '$.load') IS NOT null;
```

The JobStatistics object for a Big Query `load` job can be found here: [https://cloud.google.com/bigquery/docs/reference/rest/v2/Job#JobStatistics3](https://cloud.google.com/bigquery/docs/reference/rest/v2/Job#JobStatistics3).  Let's run an StackQL query to return all of the statistics for `load` jobs run in a given GCP project.  

<Tabs
  defaultValue="iql"
  values={[
    { label: 'StackQL', value: 'iql', },
    { label: 'Results', value: 'results', },
  ]
}>
<TabItem value="iql">

```jsx
SELECT id,
DATETIME(SUBSTR(JSON_EXTRACT(statistics, '$.creationTime'), 1, 10), 'unixepoch') AS creationTime,
DATETIME(SUBSTR(JSON_EXTRACT(statistics, '$.startTime'), 1, 10), 'unixepoch') AS startTime,
DATETIME(SUBSTR(JSON_EXTRACT(statistics, '$.endTime'), 1, 10), 'unixepoch') AS endTime,
JSON_EXTRACT(statistics, '$.load.inputFiles') AS inputFiles,
JSON_EXTRACT(statistics, '$.load.inputFileBytes') AS inputFileBytes,
JSON_EXTRACT(statistics, '$.load.outputRows') AS outputRows,
JSON_EXTRACT(statistics, '$.load.outputBytes') AS outputBytes,
JSON_EXTRACT(statistics, '$.load.badRecords') AS badRecords
FROM google.bigquery.jobs
WHERE projectId = 'stackql'
AND state = 'DONE'
AND errorResult IS null
AND json_extract(statistics, '$.load') IS NOT null;
```

</TabItem>
<TabItem value="results">

```
|-------------------------------------------------------|---------------------|---------------------|---------------------|------------|----------------|------------|-------------|------------|
|                          id                           |    creationTime     |      startTime      |       endTime       | inputFiles | inputFileBytes | outputRows | outputBytes | badRecords |
|-------------------------------------------------------|---------------------|---------------------|---------------------|------------|----------------|------------|-------------|------------|
| stackql:US.job_2wqdOf0hQmEbfoUtT_H2Vk6bhHb4           | 2021-09-03 01:21:01 | 2021-09-03 01:21:01 | 2021-09-03 01:21:05 |         34 |          24833 |         37 |       16913 |          0 |
|-------------------------------------------------------|---------------------|---------------------|---------------------|------------|----------------|------------|-------------|------------|
| stackql:US.job_XS1X7uaKznqkd0xKwB3M4yxBu1GP           | 2021-08-19 04:29:29 | 2021-08-19 04:29:29 | 2021-08-19 04:29:30 |          1 |            650 |          1 |         424 |          0 |
|-------------------------------------------------------|---------------------|---------------------|---------------------|------------|----------------|------------|-------------|------------|
| stackql:US.bqjob_r35be078db112298a_0000017b5ca605f3_1 | 2021-08-19 04:23:13 | 2021-08-19 04:23:13 | 2021-08-19 04:23:16 |         21 |          16136 |         24 |       11223 |          0 |
|-------------------------------------------------------|---------------------|---------------------|---------------------|------------|----------------|------------|-------------|------------|
| stackql:US.job_n4WpeG26fYQGN3q2s4oBS5ECZtrO           | 2021-08-19 04:18:32 | 2021-08-19 04:18:32 | 2021-08-19 04:18:36 |         21 |          16136 |         24 |       11223 |          0 |
|-------------------------------------------------------|---------------------|---------------------|---------------------|------------|----------------|------------|-------------|------------|
| stackql:US.job_jcYDO5Uqk_Kq56LyB5gir8t3dOcs           | 2021-08-19 04:09:30 | 2021-08-19 04:09:30 | 2021-08-19 04:09:33 |         21 |          16136 |         24 |       11223 |          0 |
|-------------------------------------------------------|---------------------|---------------------|---------------------|------------|----------------|------------|-------------|------------|
| stackql:US.bqjob_r563627d461394a48_0000017b18e560af_1 | 2021-08-06 00:38:12 | 2021-08-06 00:38:13 | 2021-08-06 00:38:14 |          7 |           5280 |          8 |        3632 |          0 |
|-------------------------------------------------------|---------------------|---------------------|---------------------|------------|----------------|------------|-------------|------------|
| stackql:US.bqjob_r53c339dfc533e536_0000017b18e3ae1d_1 | 2021-08-06 00:36:21 | 2021-08-06 00:36:22 | 2021-08-06 00:36:23 |          7 |           5280 |          8 |        3632 |          0 |
|-------------------------------------------------------|---------------------|---------------------|---------------------|------------|----------------|------------|-------------|------------|
| stackql:US.bqjob_r4e070967e1b17aad_0000017b18e12350_1 | 2021-08-06 00:33:35 | 2021-08-06 00:33:35 | 2021-08-06 00:33:37 |          7 |           5280 |          8 |        3632 |          0 |
|-------------------------------------------------------|---------------------|---------------------|---------------------|------------|----------------|------------|-------------|------------|
| stackql:US.bqjob_r7da027bed5c2df33_0000017b1307b5f2_1 | 2021-08-04 21:18:00 | 2021-08-04 21:18:00 | 2021-08-04 21:18:02 |          2 |            125 |          2 |          54 |          0 |
|-------------------------------------------------------|---------------------|---------------------|---------------------|------------|----------------|------------|-------------|------------|
| stackql:US.bqjob_r33090c6d975e8b83_0000017b13078053_1 | 2021-08-04 21:17:47 | 2021-08-04 21:17:47 | 2021-08-04 21:17:49 |          2 |           1075 |          2 |         616 |          0 |
|-------------------------------------------------------|---------------------|---------------------|---------------------|------------|----------------|------------|-------------|------------|
| stackql:US.bqjob_r13fcb919db480ae_0000017b0de8b157_1  | 2021-08-03 21:26:00 | 2021-08-03 21:26:00 | 2021-08-03 21:26:02 |          2 |           1075 |          2 |         616 |          0 |
|-------------------------------------------------------|---------------------|---------------------|---------------------|------------|----------------|------------|-------------|------------|
| stackql:US.bqjob_r5172493bba418297_0000017b0de7ab60_1 | 2021-08-03 21:24:54 | 2021-08-03 21:24:54 | 2021-08-03 21:24:56 |          1 |             62 |          1 |          27 |          0 |
|-------------------------------------------------------|---------------------|---------------------|---------------------|------------|----------------|------------|-------------|------------|
| stackql:US.bqjob_r61e992eab4f511bc_0000017b099e0bcf_1 | 2021-08-03 01:26:00 | 2021-08-03 01:26:00 | 2021-08-03 01:26:02 |          2 |           1075 |          2 |         616 |          0 |
|-------------------------------------------------------|---------------------|---------------------|---------------------|------------|----------------|------------|-------------|------------|
```

</TabItem>
</Tabs>

In future posts we will show similar examples using StackQL to query for errors and statistics for `extract` and `query` jobs in Big Query, see you then!
