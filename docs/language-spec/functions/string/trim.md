---
title: TRIM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: "TRIM function in StackQL: remove spaces, or any of a given set of characters, from both ends of a string."
image: "/img/stackql-featured-image.png"
---
Returns a string formed by removing spaces, or any of a given set of characters, from both ends of an input string.  

See also:  
[[` SELECT `]](/language-spec/select) [[` LTRIM `]](/language-spec/functions/string/ltrim) [[` RTRIM `]](/language-spec/functions/string/rtrim) 

* * * 

## Syntax {#syntax}

```sql
SELECT TRIM(character_expression[, character ]) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*character_expression*__  
A literal string or string column.

__*character*__  
An optional string of characters: every character in it is removed from the beginning and end of the *character_expression*.  If omitted, spaces are removed (not tabs or newlines).

## Return Value(s) {#return-values}
Returns a string.

* * *

## Examples {#examples}

### Remove leading and trailing spaces from a string column {#remove-leading-and-trailing-spaces-from-a-string-column}

```sql
SELECT trim(description) AS leading_and_trailing_whitespace_removed
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

For more information, see [https://www.sqlite.org/lang_corefunc.html#trim](https://www.sqlite.org/lang_corefunc.html#trim)