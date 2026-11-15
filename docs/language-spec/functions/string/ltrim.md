---
title: LTRIM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: "LTRIM function in StackQL: remove spaces, or any of a given set of characters, from the left of a string."
image: "/img/stackql-featured-image.png"
---
Returns a string formed by removing spaces, or any of a given set of characters, from the left side of an input string.  

See also:  
[[` SELECT `]](/language-spec/select) [[` RTRIM `]](/language-spec/functions/string/rtrim) [[` TRIM `]](/language-spec/functions/string/trim) 

* * * 

## Syntax {#syntax}

```sql
SELECT LTRIM(character_expression[, character ]) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*character_expression*__  
A literal string or string column.

__*character*__  
An optional string of characters: every character in it is removed from the beginning of the *character_expression*.  If omitted, spaces are removed (not tabs or newlines).

## Return Value(s) {#return-values}
Returns a string.

* * *

## Examples {#examples}

### Remove leading spaces from a string column {#remove-leading-spaces-from-a-string-column}

```sql
SELECT ltrim(description) AS leading_whitespace_removed
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

For more information, see [https://www.sqlite.org/lang_corefunc.html#ltrim](https://www.sqlite.org/lang_corefunc.html#ltrim)