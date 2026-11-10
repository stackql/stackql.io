---
title: Session Variables
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - session variables
  - set
  - unset
  - identifier
description: "Session variables in StackQL: set typed scalar values with SET, dereference them with $name in predicates and expressions, and use them as relation and column names with IDENTIFIER."
image: "/img/stackql-featured-image.png"
---

See also:  
[[` SET `]](/language-spec/set) [[` UNSET `]](/language-spec/unset) [[ Provider Sessions ]](/getting-started/provider-sessions) [[ Using Variables ]](/getting-started/variables)

Session variables are scalar values held by the current session and referenced from later statements.  They are available in StackQL `v1.0` and later, and work in the interactive shell, in `exec` scripts, over the PostgreSQL wire protocol and through the MCP server.

Session variables are distinct from the templating variables described in [Using Variables](/getting-started/variables).  Templating variables are supplied to `stackql exec` through `--iqldata` and `--var` and are rendered into the script text before it is parsed.  Session variables are resolved inside the parsed statement, keep their type, and can be changed between statements.

## Setting and removing variables {#setting-and-removing-variables}

[`SET`](/language-spec/set) assigns a string, number, boolean or `null`.  [`UNSET`](/language-spec/unset) removes a variable; `SET name = DEFAULT` is equivalent.  An explicit `null` is a stored value, not a reset.

```sql
SET region = 'ap-southeast-2';
SET minimum_count = 3;
SET enabled = true;
SET optional_value = null;
UNSET region;
```

Arrays and objects are not scalar values.  To hold a JSON document, store it as a string:

```sql
SET "providers.saved" = '{"provider":"aws","tenancy":"production"}';
```

## Dereferencing a variable {#dereferencing-a-variable}

Use `$name`, without quotes, in any expression position.  Strings are escaped as SQL literals; numbers, booleans and `null` keep their type.  A single-quoted `'$name'` is the literal text `$name`, not a dereference.  A reference to a variable that does not exist fails before any provider request is made.

Resolved values can be used in ordinary SQL expressions, including `CAST`, `CONVERT`, concatenation with `||` and function calls:

```sql
SET some_id = 42;
SET suffix = 'East';

SELECT CAST($some_id AS TEXT) AS id_text,
       CONVERT($some_id, TEXT) || '-' || lower($suffix) AS label,
       md5(lower(CONVERT($some_id, TEXT) || '-' || $suffix)) AS digest,
       '$some_id' AS literal_text;
```

Assignments accept scalar literals or direct references only; apply conversions and other expressions where the variable is used, not in the `SET` statement.

## Variables in predicates {#variables-in-predicates}

A variable in a `WHERE` clause is resolved before the statement is planned, so it is routed to the provider API exactly as a literal would be.  Required parameters can therefore be supplied from variables:

```sql
SET project = 'stackql-demo';
SET zone = 'australia-southeast1-a';

SELECT name, status
FROM google.compute.instances
WHERE project = $project AND zone = $zone;
```

Typed values are compared with their type.  `WHERE active = $enabled` with `SET enabled = false` filters on the boolean, and `WHERE id = CAST($some_id AS TEXT)` is the form to use when the provider column is a string and the variable is a number.

## Variables as object names {#variables-as-object-names}

Variables can name relations and columns as well as values.  Names are converted to identifiers in the parsed statement and are never spliced into SQL text, so a value that contains a statement, is empty, or is not a string is rejected before any provider access.

| Position | Form |
|--|--|
| `FROM` clause | `FROM $relation_name`, or by component, `FROM $provider.$service.$resource` |
| Column in an expression | `IDENTIFIER($column_name)`, see [`IDENTIFIER`](/language-spec/functions/general/identifier) |
| Column or table alias | `AS "$alias_name"` |
| `ORDER BY` | `ORDER BY IDENTIFIER($column_name)` |
| `LIMIT` | `LIMIT $row_count` |
| DDL column names, `INSERT` column lists, `UPDATE` targets | the double-quoted `"$name"` form |
<br/>

```sql
SET relation_name = 'aws.s3.buckets';
SET column_name = 'name';
SET output_name = 'bucket';
SET row_count = 10;

SELECT IDENTIFIER($column_name) AS "$output_name"
FROM $relation_name
WHERE region = 'ap-southeast-2'
ORDER BY IDENTIFIER($column_name)
LIMIT $row_count;
```

Joins, CTEs and subqueries resolve their relation variables in the same way.  Dotted relation values have up to four components.  Variables are not arbitrary SQL fragments: a keyword, a comma-separated column list or a whole `WHERE` clause cannot be substituted.

## Scope and isolation {#scope-and-isolation}

Variables belong to the session that set them.  Separate connections to a `stackql srv` server have separate variables, as do separate `exec` invocations.  Each MCP request is its own session, so a `SET` and the query that uses it must be sent in the same statement batch.  New sessions start with no variables.

The session is the only scope.  `SET SESSION` and `UNSET SESSION` are accepted and mean exactly the same as `SET` and `UNSET`; the keyword is there for scripts that should read the same way as on databases where session and global settings differ.  `GLOBAL` is rejected, because there is no state shared across sessions.

Prepared statements resolve variables when they are executed, not when they are prepared, and a plan that contains bound session values is not reused across executions.

With [access management](/command-line-usage/access-management) enabled, reading a variable requires `session/variables:read` and setting or resetting one requires `session/config:write`.  Resolving a variable to an object name grants no permission on that object; the resolved relation is authorised before execution as usual.
