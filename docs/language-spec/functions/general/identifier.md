---
title: IDENTIFIER
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - session variables
  - identifier
description: "IDENTIFIER function in StackQL: resolve a session variable holding a column name into a column reference, so variables can name columns in projections and ORDER BY."
image: "/img/stackql-featured-image.png"
---
Resolves a string value, normally a session variable, into a column name.  Use it where a bare `$variable` would otherwise be read as a scalar value.  Available in StackQL `v1.0` and later.

See also:  
[[` SET `]](/language-spec/set) [[ Session Variables ]](/getting-started/session-variables)

* * * 

## Syntax {#syntax}

```sql
SELECT IDENTIFIER($variable) FROM <multipartIdentifier> ORDER BY IDENTIFIER($variable);
```

## Arguments {#arguments}

__*variable*__  
A session variable, or a string literal, holding a column name.  A qualified name such as `t.name` is accepted.  Exactly one argument is accepted and it must be a single string value; a concatenation such as `'t.' || $column_name`, a second argument, or a numeric value is rejected with `IDENTIFIER requires one string value`.  An empty value, or a value containing a statement, is rejected with `invalid identifier value`, and an undefined variable fails as undefined.  All of these checks happen before any provider request is made.

## Return Value(s) {#return-values}

The function is replaced in the parsed statement by a reference to the named column, so it returns that column's value.  Names are converted to identifiers and never spliced into SQL text.  The resulting column reference can be used anywhere a column can: the select list, `WHERE`, `GROUP BY` and `ORDER BY` clauses, function arguments, and inside CTEs and subqueries.

* * *

## Examples {#examples}

### Project and order by a column named in a variable {#project-and-order-by-a-column-named-in-a-variable}

The column to return and the column to sort on are both taken from `column_name`, and the output column is renamed from `output_name`.  `IDENTIFIER` is needed in the select list and in `ORDER BY` because a bare `$column_name` in those positions would be the string `'name'`, not the column.  The alias uses the double-quoted form instead.  Once the variables are resolved, the statement is the same as `SELECT name AS bucket FROM aws.s3.buckets WHERE region = 'ap-southeast-2' ORDER BY name`.

```sql
SET column_name = 'name';
SET output_name = 'bucket';

SELECT IDENTIFIER($column_name) AS "$output_name"
FROM aws.s3.buckets
WHERE region = 'ap-southeast-2'
ORDER BY IDENTIFIER($column_name);
```

### Filter on a column named in a variable {#filter-on-a-column-named-in-a-variable}

The resolved reference works in a predicate as well as a projection.  Here the field to filter on and the value to match both come from variables: `IDENTIFIER($filter_column)` becomes the column `status`, while `$filter_value` is passed as the string literal `'RUNNING'`.  The two forms are deliberately different, because the first names a column and the second supplies a value.

```sql
SET filter_column = 'status';
SET filter_value = 'RUNNING';

SELECT name, zone
FROM google.compute.instances
WHERE project = 'stackql-demo'
AND zone = 'australia-southeast1-a'
AND IDENTIFIER($filter_column) = $filter_value;
```

### Qualify the column with a table alias {#qualify-the-column-with-a-table-alias}

A dotted value resolves to a qualified column reference.  The table is aliased as `w`, with the alias itself taken from a variable, and `IDENTIFIER($column_name)` becomes `w.name`.  This is the form to use when the same column name exists on both sides of a join and the reference would otherwise be ambiguous.

```sql
SET table_alias = 'w';
SET column_name = 'w.name';

SELECT IDENTIFIER($column_name) AS bucket
FROM aws.s3.buckets AS "$table_alias"
WHERE w.region = 'ap-southeast-2';
```

### Rejected values {#rejected-values}

Because the value becomes a name rather than query text, anything that is not a usable name is refused before the statement is planned, and no request reaches the provider.  The first two statements below fail with `invalid identifier value` and the third with `IDENTIFIER requires one string value`.

```sql
/*
SET column_name = '';
SELECT IDENTIFIER($column_name) FROM aws.s3.buckets;
-- FAILS with invalid identifier value

SET column_name = 'name; DROP TABLE imaginary';
SELECT IDENTIFIER($column_name) FROM aws.s3.buckets;
-- FAILS with invalid identifier value

SET column_name = 17;
SELECT IDENTIFIER($column_name) FROM aws.s3.buckets;
-- FAILS with IDENTIFIER requires one string value
*/
```

> A `$variable` in a `FROM` clause resolves to a relation without `IDENTIFIER`, and a double-quoted `"$variable"` is used for aliases, DDL column names, `INSERT` column lists and `UPDATE` targets.
