---
slug: big-query-cost-analysis-using-stackql
title: Big Query Cost Analysis using StackQL
author: Jeffrey Aven
author_title: Cloud Consultant
author_url: https://github.com/stackql
author_image_url: https://s.gravatar.com/avatar/f96573d092470c74be233e1dded5376f?s=80
image: https://storage.googleapis.com/stackql-web-assets/blog/stackql-bq-cost-analysis.png
description: This article demonstrates how to use StackQL to summarize or drill down into detail Big Query billing data for analysis into billing spikes or anomalies.
keywords: [stackql, bigquery, gcp, costs, cost management]
tags: [stackql, bigquery, gcp, costs, cost management]
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

Queries (particularly) repetitive queries that don't take advantage of results caching can lead to extraordinarily high bills.    

StackQL, with it's backend SQL engine, allows you to query Big Query statistics in real time, including identifying queries which are not served from cache and understanding billable charges per query or time slice.  

Here is a simple query to break down a time period into hours and show the total queries, queries served from cache and the total query charges per hour. 

<Tabs
  defaultValue="iql"
  values={[
    { label: 'StackQL', value: 'iql', },
    { label: 'Data', value: 'data', },
    { label: 'Results', value: 'results', },
  ]
}>
<TabItem value="iql">

```jsx
SELECT
 STRFTIME('%H', DATETIME(SUBSTR(JSON_EXTRACT(statistics, '$.startTime'), 1, 10), 'unixepoch')) as hour,
 COUNT(*) as num_queries,
 SUM(JSON_EXTRACT(statistics, '$.query.cacheHit')) as using_cache,
 SUM(JSON_EXTRACT(statistics, '$.query.totalBytesBilled')*{{ .costPerByte }} ) as queryCost
FROM google.bigquery.jobs
 WHERE projectId = '{{ .projectId }}'
  AND allUsers = 'true'
  AND minCreationTime = '{{ .minCreationTime }}'
  AND maxCreationTime = '{{ .maxCreationTime }}'
  AND state = 'DONE'
  AND JSON_EXTRACT(statistics, '$.query') IS NOT null
GROUP BY STRFTIME('%H', datetime(SUBSTR(JSON_EXTRACT(statistics, '$.startTime'), 1, 10), 'unixepoch'));
```
</TabItem>
<TabItem value="data">

```jsx
// variables

local projectId = 'my-project-id';
local costPerTb = 6.5;
local startTimeMs = 1634734801000;

{
	projectId: projectId,
	minCreationTime: std.toString(startTimeMs),
	maxCreationTime: std.toString(startTimeMs+86400000),
	costPerByte: costPerTb*(1/std.pow(1024, 4)),
}
```

</TabItem>
<TabItem value="results">

```jsx
|------|-------------|-------------|------------------------|
| hour | num_queries | using_cache |       queryCost        |
|------|-------------|-------------|------------------------|
|   00 |         182 |           0 |      70.73793411254883 |
|------|-------------|-------------|------------------------|
|   01 |          88 |           0 |      34.20295715332031 |
|------|-------------|-------------|------------------------|
|   02 |           2 |           0 |     0.7773399353027344 |
|------|-------------|-------------|------------------------|
|   03 |         267 |           0 |     103.77488136291504 |
|------|-------------|-------------|------------------------|
|   04 |         216 |           0 |      83.95271301269531 |
|------|-------------|-------------|------------------------|
|   05 |          47 |           0 |     18.267488479614258 |
|------|-------------|-------------|------------------------|
|   06 |         122 |           0 |       47.4177360534668 |
|------|-------------|-------------|------------------------|
|   07 |         195 |           0 |       75.7906436920166 |
|------|-------------|-------------|------------------------|
|   08 |         186 |           0 |       72.2926139831543 |
|------|-------------|-------------|------------------------|
|   09 |          75 |           0 |      29.15024757385254 |
|------|-------------|-------------|------------------------|
|   10 |          62 |           0 |     24.097537994384766 |
|------|-------------|-------------|------------------------|
|   11 |          56 |           0 |     21.765518188476562 |
|------|-------------|-------------|------------------------|
|   12 |          89 |           0 |      34.59162712097168 |
|------|-------------|-------------|------------------------|
|   15 |           3 |           0 | 0.00018596649169921875 |
|------|-------------|-------------|------------------------|
|   22 |           1 |           0 |     0.3886699676513672 |
|------|-------------|-------------|------------------------|
|   23 |          35 |           0 |     13.603448867797852 |
|------|-------------|-------------|------------------------|
```

</TabItem>
</Tabs>

Many more examples to come, including using this data to create visualisations in a Jupyter notebook, stay tuned!  
