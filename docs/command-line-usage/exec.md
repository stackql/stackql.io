---
title: exec
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Query and Deploy Cloud Infrastructure and Resources using SQL
image: https://storage.googleapis.com/stackql-web-assets/blog/stackql-blog-post-featured-image.png
---

Command used to run a StackQL query, either as a statement or as a batch of statements from an StackQL script (IQL File).  

* * * 

### Syntax

`stackql exec [query] [flags]`

* * *

### Arguments

| Argument | Description | Example |
|--|--|--|
|`query`|StackQL query to be performed<br/>or command to be executed | `"SELECT * FROM google.compute.instances`<br/>`WHERE project = 'myproject' AND zone = 'us-east1-a'"`<br/>_Note that the query must be enclosed in single or double quotes_ |    


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

Output query results to the console in table format:
```shell
stackql exec "SHOW services IN google"
```

Output query results to the console in json format:
```shell
stackql exec "SHOW services IN google" -o json
```

Output query results to a file in json format:
```shell
stackql exec "SHOW services IN google" -o json -f services.json
```

Execute query from an input file (IQL File) outputting the results to a file in json format:
```shell
stackql exec -i getservices.iql -o json -f services.json
```