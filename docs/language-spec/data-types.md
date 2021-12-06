---
title: Data Types
hide_title: false
hide_table_of_contents: true
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Query and Deploy Cloud Infrastructure and Resources using SQL
custom_edit_url: null
image: https://storage.googleapis.com/stackql-web-assets/blog/stackql-blog-post-featured-image.png
---

StackQL data types are used to represent values for input or output fields (properties or parameters) used within cloud resources.  StackQL data types are listed in the table below:

| StackQL type | Description | Example |
|--|--|--|
| `string` | sequence of Unicode characters enclosed by single quotes | `'mybucket'` |
| `array` | ordered collection of values, surrounded by brackets `[ ]`, separated by a comma | `[0,1,2]` |
| `object` | unordered set of zero or more name/value pairs inserted between `{ }` and separated by a comma | `{"name": "mydb"}` |
| `boolean` | `true` or `false`, boolean values are not surrounded within quotes and will be treated as string values | `true` |
| `integer` | a whole number that can be positive, negative, or zero | `2` |
| `double` | double-precision floating-point number, can be positive or negative | `0.95` |
&nbsp;  
&nbsp;  
> StackQL data types map to the data types exposed in the APIs for all major cloud providers.