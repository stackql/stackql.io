---
title: shell
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

Command used to launch the interactive StackQL Command Shell.  For more information see [Using the Interactive StackQL Shell](/docs/getting-started/using-stackql#using-the-stackql-interactive-shell).

* * * 

### Syntax

`stackql shell [flags]`

* * *

### Flags

| Flag | Description |
|--|--|
|`-H,--help`|Print help information|
|`-v,--verbose`|Run queries in verbose mode with additional output sent to stdout, if the `-f` option is selected<br/>this additional logging information will be written to the output file along with the query results|
&nbsp;  
&nbsp;  
> see [Global Flags](/docs/command-line-usage/global-flags) for additional options

* * *

### Examples

Launch the StackQL shell, using a service account key file for authentication.
```shell
stackql shell --keyfilepath /tmp/stackql-demo.json
```