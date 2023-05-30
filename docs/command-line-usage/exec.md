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
|<span class="nowrap">`-o`</span><br/><span class="nowrap">`--output`</span>|Output mode, valid values include: `table`(default) `json`, `csv` and `text`, example: `--output json` |
|<span class="nowrap">`-i`</span><br/><span class="nowrap">`--infile`</span>|Run query from the specified file if required, if specified any query provided as an argument will be ignored, example: `--infile myquery.iql` |
|<span class="nowrap">`-q`</span><br/><span class="nowrap">`--iqldata`</span>|Source data for the command using a `json` or `jsonnet` file, example: `--iqldata vars.jsonnet`|
|<span class="nowrap">`--dryrun`</span>|Dry run flag; preprocessor only will run and output will returned |
|<span class="nowrap">`--var`</span>|External variables (can be environment vars or secrets) sourced by jsonnet files specified using the `--iqldata` flag, example `--var region=${AWS_REGION}`|
|<span class="nowrap">`-f`</span><br/><span class="nowrap">`--outfile`</span>|File to output results to if required (default behavior is to output results to the console only), example `--outfile myoutput.csv`|
|<span class="nowrap">`-H`</span><br/><span class="nowrap">`--help`</span>|Print help information|
|<span class="nowrap">`-v`</span><br/><span class="nowrap">`--verbose`</span>|Run queries in verbose mode with additional output sent to stdout, if the `-f` option is selected this additional logging information will be written to the output file along with the query results|
&nbsp;  
&nbsp;  
> see [Global Flags](/docs/command-line-usage/global-flags) for additional options


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