---
title: JSON_GROUP_OBJECT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Aggregate key-value pairs into a JSON object using SQL in StackQL.
image: "/img/stackql-featured-image.png"
---
Aggregates key-value pairs into a JSON object, grouping by a specified column. `JSON_GROUP_OBJECT()` collects all key-value pairs for each group in specified columns and combines them into a single JSON object for each group.

See also:  
[[`SELECT`]](/docs/language-spec/select)

* * * 

## Syntax

```sql
SELECT JSON_GROUP_OBJECT(key_column, value_column) FROM <multipartIdentifier>
WHERE condition
GROUP BY grouping_column;
```

## Arguments

__*key_column*__  
The column that provides the keys for the JSON objects.

__*value_column*__  
The column that provides the values for the JSON objects.

__*grouping_column*__  
The column by which the results are grouped.

## Return Value(s)
Returns a JSON object containing all the aggregated key-value pairs from the specified columns, grouped according to the `grouping_column`.

* * *

## Examples

### Aggregate Repository Sizes by Programming Language

```sql
-- Example: Group repository names with their corresponding sizes by programming language
SELECT
  language, 
  JSON_GROUP_OBJECT(name, size) AS language_index
FROM
  github.repos.repos
WHERE
  org = 'stackql'
  AND language IS NOT NULL
GROUP BY language;
```

This query returns a JSON object for each programming language, where each object contains repository names and their corresponding sizes as key-value pairs. Here's a sample of the expected output:

```plaintext
|-------------|-----------------------------------------------------------------------------------|
| language    | language_index                                                                    |
|-------------|-----------------------------------------------------------------------------------|
| C           | {"go-sqlite3":35667,"data-encryption-spike":50746}                                |
|-------------|-----------------------------------------------------------------------------------|
| C++         | {"expt-any-sdk":2399,"isqlang":9}                                                 |
|-------------|-----------------------------------------------------------------------------------|
| Dockerfile  | {"stackql-compliance-checks-devel":8}                                             |
|-------------|-----------------------------------------------------------------------------------|
| Go          | {"stackql-provider-registry":115711, ...}                                         |
|-------------|-----------------------------------------------------------------------------------|
```

This output is useful for visualizing the distribution of repository sizes across different programming languages within an organization, providing clear and organized data grouped by language.
