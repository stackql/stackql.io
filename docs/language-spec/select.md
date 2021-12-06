---
title: SELECT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Query and Deploy Cloud Infrastructure and Resources using SQL
custom_edit_url: null
image: https://storage.googleapis.com/stackql-web-assets/blog/stackql-blog-post-featured-image.png
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Returns an instance or instances of a resource.  

See also:  
[[ StackQL Resource Hierarchy ]](/docs/getting-started/resource-hierarchy)

* * * 

## Syntax

*selectStatement::=*

<RailroadDiagram 
type="select"
/>

&nbsp;  
&nbsp;

```sql
SELECT { * | <fieldList> } FROM <multipartIdentifier>
WHERE <expression> ;
```

* * *

## Examples

### Basic SELECT Statement
Run a basic `SELECT` statement in an authenticated session.

```sql
-- Selecting all resources deployed within a service using a basic SELECT statement
SELECT * FROM google.compute.instances
WHERE project = 'stackql-demo'
AND zone = 'australia-southeast1-a';
```

### Returning specified fields using a SELECT Statement
Run a `SELECT` statement with a column list to return specified fields from a Compute Engine instance.

```sql
-- Selecting specified fields from a resource
USE google;
SELECT id, name 
FROM compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Return a result set ordered by a column in ascending order using the ORDER BY clause
Run a `SELECT` statement to return fields from the Cloud Storage buckets resource, ordering the results in ascending order (oldest first). 

```sql
-- Order a list of Cloud Storage buckets by creation time (in ascending order)
SELECT name, location, timeCreated 
FROM google.storage.buckets WHERE project = 'stackql'
ORDER BY timeCreated;
```

### Return a result set ordered by a column in descending order using the ORDER BY clause
Run a `SELECT` statement to return fields from the Cloud Storage buckets resource, ordering the results in descending order (newest first).

```sql
-- Order a list of Cloud Storage buckets by creation time (in descending order)
SELECT name, location, timeCreated 
FROM google.storage.buckets WHERE project = 'stackql'
ORDER BY timeCreated DESC;
```

### Get the top result from a column using the ORDER BY and LIMIT clauses
Run a `SELECT` statement to return the most recently created Cloud Storage bucket.

```sql
-- Find the most recently created bucket
SELECT name, location, timeCreated 
FROM google.storage.buckets WHERE project = 'stackql'
ORDER BY timeCreated DESC LIMIT 1;
```

### Use the COUNT function with the GROUP BY and HAVING clauses to count resources based upon a grouping and filter
Run a `SELECT` statement with a `COUNT` function, using the `GROUP BY` and `HAVING` clauses to return the number of buckets in every location other than Asia.

```sql
-- Return a count of the number of buckets in every location other than Asia
SELECT name, location, count(*) as num_buckets 
FROM google.storage.buckets WHERE project = 'stackql'
GROUP BY location
HAVING location != 'ASIA';
```

For more information on the `COUNT` function and other aggregate functions supported by StackQL see [Aggregate Functions](/docs/language-spec/functions/aggregate/count).