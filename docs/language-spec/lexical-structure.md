---
title: Lexical Structure
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

See also:  
[[ StackQL Resource Hierarchy ]](/docs/getting-started/resource-hierarchy) [[ Keywords ]](/docs/language-spec/keywords) [[ Data Types ]](/docs/language-spec/data-types) [[ DESCRIBE ]](/docs/language-spec/describe)


StackQL’s lexical structure and grammar is modelled upon MySQL’s language implementation (an adaptation of the ANSI SQL-92 standard) with some extensions.  StackQL statements are used to query cloud resources (using `SELECT` grammar) or create, modify or delete infrastructure using standard Data Manipulation Language (DML) statements such as `INSERT`, `UPDATE`, `REPLACE` and `DELETE`.  Additionally, non DML operations such as starting an instance, or getting or putting an object into a bucket in AWS S3 for example are implemented as built-in provider procedures (akin to Stored Procedures in a DBMS).  Built-in provider procedures are invoked using the `EXEC` statement.

StackQL’s SQL domain-specific language allows for declarative as well as procedural infrastructure programming.

* * * 

## Syntax
StackQL operations are comprised of one or more semicolon (`;`) terminated statements.  Statements include keywords, identifiers, parameters and values, an example StackQL statement used to create a 10GB disk in GCP would be:

```sql
INSERT INTO google.compute.disks (project, zone, name, sizeGb)
SELECT 'myProject', 'australia-southeast1-a', 'my10GbDisk', 10;
```

In the above example, _`INSERT`_, _`INTO`_ and _`SELECT`_ are keywords, _google.compute.disks_ is an identifier, _project_, _zone_, _name_, _sizeGb_  are parameters, and _'myProject'_, _'australia-southeast1-a'_, _'my10GbDisk'_, _10_ are values.

## Keywords
Keywords are not case sensitive, meaning `INSERT` and `insert` are treated equivalently, however they are often capitalized by convention. A complete list of keywords can be found in the Keywords section.

## Identifiers
Identifiers are symbols that represent objects in a cloud provider resource hierarchy.  Identifiers are made up of multiple parts, more information on identifiers can be found in the StackQL Resource Hierarchy topic.

## Parameters and Values
Parameters are specified by the resource; these can be found by using a `DESCRIBE` statement for a given cloud provider resource.  Values are typed scalar or complex objects which map to parameters.  For more information on parameters and values, see the `DESCRIBE` and Data Types topics.

## Comments
StackQL supports multi-line and in line comments within StackQL statements, examples of comments are shown below:

```shell
/* This
Is a
Multiline
Comment
*/

--This is an inline comment
```