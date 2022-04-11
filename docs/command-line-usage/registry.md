---
title: registry
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

Command used to interact with the StackQL Provider Registry.  

* * * 

### Syntax

`stackql registry {subcommand} [{provider} {version}]`

* * *

### Arguments

| Argument | Description | Example |
|--|--|--|
|`subcommand`|Registry operation to be performed<br/>(either `list` or `pull`) | `list` or `pull` |    
|`provider`|Provider to be installed using `pull` subcommand| `okta` |
|`version`|Provider version to be installed using `pull` subcommand<br />*(StackQL Provider Document Version)* | `v1` |


### Flags

| Flag | Description |
|--|--|
|`-o, --output <outputtype>`|Output mode, valid values include: `table`(default) `json`, `csv` and `text` |
|`-i, --infile <filename>`|Run query from the specified file if required, if specified any query provided<br/> as an argument will be ignored |
|`-q, --iqldata <filename>`|Source data for the command using a `json` or `jsonnet` file |
|`--dryrun`|Dry run flag; preprocessor only will run and output will returned |
|`-i, --infile <filename>`|Run query from the specified file if required, if specified any query provided<br/> as an argument will be ignored |
|`-f, --outfile <filename>`|File to output results to if required (default behavior is to output results to the<br/> console only)|
|`-H, --help`|Print help information|
|`-v, --verbose`|Run queries in verbose mode with additional output sent to stdout,<br/> if the `-f` option is selected this additional logging information will be written<br/>to the output file along with the query results|

see [Global Flags](/docs/command-line-usage/global-flags) for additional options

* * *

### Examples

List available providers in the public registry:
```shell
stackql registry list
```

Pull a provider from the public registry:
```shell
stackql registry pull okta v1
```