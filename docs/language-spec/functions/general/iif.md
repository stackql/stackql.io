---
title: IIF
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
Returns a value based upon the evaluation of an input expression.  

See also:  
[[` SELECT `]](/docs/language-spec/select)

* * * 

## Syntax

```sql
SELECT IIF(expression, result_if_true, result_if_false) 
FROM <multipartIdentifier>;
```
> The `iif()` function is logically equivalent to the `CASE` expression `CASE WHEN expression THEN result_if_true ELSE result_if_false END`

## Arguments

__*expression*__  
A boolean expression to be evaluated.

:::tip 

The *expression* argument can be a nested function such as `JSON_EXTRACT`.

:::

__*result_if_true*__  
The result to be returned if *expression* evaluates to `true`.

__*result_if_false*__  
The result to be returned if *expression* evaluates to `false`.

## Return Value(s)
The datatype of the *result_if_true* or *result_if_false* arguments.

* * *

## Examples

### Catgorize disk sizes of compute engine instances

```sql
SELECT iif(json_extract(disks, '$[0].diskSizeGb') > 9, "> 10GB", "<= 10GB") as disk_size
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

For more information, see [https://www.sqlite.org/lang_corefunc.html#iif](https://www.sqlite.org/lang_corefunc.html#iif)