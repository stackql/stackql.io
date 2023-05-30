---
title: Global Flags
hide_title: false
hide_table_of_contents: true
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Query and Deploy Cloud Infrastructure and Resources using SQL
image: https://storage.googleapis.com/stackql-web-assets/blog/stackql-blog-post-featured-image.png
---

Global flags specify runtime program behavior for the StackQL application, these flags are set at the command line when the StackQL application is launched (in either the interactive/shell mode or non-interactive exec mode) or are sourced from the StackQL initialization ([`.stackqlrc`](/docs/getting-started/using-stackql#the-stackqlrc-initialization-file)) file.  Flags include:  

| Flag | Data Type | Description | Default |
| -- | -- | -- | -- |
| <span class="nowrap">`--acid`</span> | string | JSON / YAML string representing ACID config | `{}` |
| <span class="nowrap">`--apirequesttimeout`</span> | integer | API request timeout in seconds, 0 for no timeout | `45` |
| <span class="nowrap">`--approot`</span> | string | Application config and cache root path | `{cwd}/.stackql` |
| <span class="nowrap">`--approotfilemode`</span> | uint32 | Application config and cache file mode | `493` |
| <span class="nowrap">`--auth`</span> | string | `auth` context keyvals in json form |  |
| <span class="nowrap">`--cachekeycount`</span> | integer | Cache initial key count | `100` |
| <span class="nowrap">`--colorscheme`</span> | string | Color scheme used in the shell must be one of {`dark`, `light`, `null`}; applies to Linux and Mac only | `null` |
| <span class="nowrap">`--configfile`</span> | string | Config file full path | `{cwd}/.stackqlrc` |
| <span class="nowrap">`--cpuprofile`</span> | string | `cpuprofile` file, none if empty | `null` |
| <span class="nowrap">`--cpuprofile`</span> | string | `cpuprofile` file, none if empty | `null` |
| <span class="nowrap">`--dataflow.dependency.max`</span> | int | Maximum dataflow dependency depth for a given query | `1` |
| <span class="nowrap">`--dbInternal`</span> | string | JSON / YAML string to configure DBMS housekeeping query handling | `{}` |
| <span class="nowrap">`-d`, `--delimiter`</span> | string | Delimiter for csv output. Single char only.  Ignored for all non-csv output | `,` |
| <span class="nowrap">`--dryrun`</span> | flag | `dryrun` flag; run preprocessor only, templated output will be returned | `false` |
| <span class="nowrap">`--execution.concurrency.limit`</span> | int | Concurrency limit for query execution | `1` |
| <span class="nowrap">`--gc`</span> | string | JSON / YAML string representing GC config | `{}` |
| <span class="nowrap">`-H`, `--hideheaders`</span> | flag | Disables column headers, valid only with `--output csv` or `--output text` | `false` |
| <span class="nowrap">`--http.log.enabled`</span> | flag | Display http request info in terminal | `false` |
| <span class="nowrap">`--http.proxy.host`</span> | string | Proxy host (leave blank for no proxy) | `null` |
| <span class="nowrap">`--http.proxy.password`</span> | string | Proxy password | `null` |
| <span class="nowrap">`--http.proxy.port`</span> | integer | Proxy port, any number <=0 will result in the default port for a given scheme (e.g. `http -> 80`) | `-1` |
| <span class="nowrap">`--http.proxy.scheme`</span> | string | Proxy scheme, {`http` or `https`} | `http` |
| <span class="nowrap">`--http.proxy.user`</span> | string | Proxy user | `null` |
| <span class="nowrap">`--http.response.maxResults`</span> | integer | Maximum results per http request, any number <=0 results in no limitation | `-1` |
| <span class="nowrap">`--http.response.pageLimit`</span> | integer | Maximum pages of results that will be returned per resource, any number <=0 results in no limitation | `20` |
| <span class="nowrap">`--indirect.depth.max`</span> | integer | Maximum depth for indirect queries: views and subqueries | `5` |
| <span class="nowrap">`-i`, `--infile`</span> | string | Input file (IQL file) from which queries are read | `{stdin}` |
| <span class="nowrap">`-q`, `--iqldata`</span> | string | Context (data) file for templating (`json` or `jsonnet` file)| |
| <span class="nowrap">`--loglevel`</span> | string | Log level, must be one of {`info`, `warn`, `debug`, `fatal`} | `fatal` |
| <span class="nowrap">`--metadatattl`</span> | integer | TTL for cached metadata documents, in seconds | `3600` |
| <span class="nowrap">`--namespaces`</span> | string | JSON / YAML string representing namespaces for caching, views etc | `{}` |
| <span class="nowrap">`--offline`</span> | flag | Work offline, using cached data | `false` |
| <span class="nowrap">`-f`, `--outfile`</span> | string | Output file into which results are written | `{stdout}` |
| <span class="nowrap">`-o`, `--output`</span> | string | Output format, must be one of {`json`, `table`, `csv`, `text`} | `table` |
| <span class="nowrap">`--provider`</span> | string | Default cloud provider | `google` |
| <span class="nowrap">`--querycachesize`</span> | integer | Size in number of entries of LRU cache for query plans | `10000` |
| <span class="nowrap">`--registry`</span> | string | openapi registry context keyvals in json form |  |
| <span class="nowrap">`--sqlBackend`</span> | string | JSON / YAML string representing SQL Backend System Config | `{}` |
| <span class="nowrap">`--store.txn`</span> | string | JSON / YAML string representing Txn store config | `{}` |
| <span class="nowrap">`--tls.CABundle`</span> | string | Path to CA bundle, if not specified then system defaults used |  |
| <span class="nowrap">`--tls.allowInsecure`</span> | flag | Allow trust of insecure certificates (not recommended) |  |
| <span class="nowrap">`--var`</span> | string | External variables provided as key/value pairs for sourcing into StackQL queries through `jsonnet` data files |  |
| <span class="nowrap">`-v`, `--verbose`</span> | flag | Verbose output | `false` |
| <span class="nowrap">`-h`, `--help`</span> | | Context specific help for stackql | |
| <span class="nowrap">`--version`</span> | | Displays the version of the StackQL program | |
