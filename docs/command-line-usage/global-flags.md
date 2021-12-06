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
custom_edit_url: null
image: https://storage.googleapis.com/stackql-web-assets/blog/stackql-blog-post-featured-image.png
---

Global flags specify runtime program behavior for the StackQL application, these flags are set at the command line when the StackQL application is launched (in either the interactive/shell mode or non-interactive exec mode) or are sourced from the StackQL initialization ([`.iqlrc`](/docs/getting-started/using-stackql#the-iqlrc-initialization-file)) file.  Flags include:  

| Flag | Data Type | Description | Default |
| -- | -- | -- | -- |
| `--apirequesttimeout` | integer | API request timeout in seconds, 0 for no timeout | `45` |
| `--cachekeycount` | integer | Cache initial key count | `100` |
| `--colorscheme` | string | Color scheme used in the shell<br /> must be one of {`dark`, `light`, `null`};<br /> applies to Linux and Mac only | `null` |
| `--configfile` | string | Config file (.iqlrc) full path; defaults to current directory | `{cwd}` |
| `-d`, `--delimiter` | string | Delimiter for csv output. Single char only.  Ignored for all non-csv output | `,` |
| `--dryrun` | flag | `dryrun` flag; run preprocessor only, templated output will be returned | `false` |
| `-H`, `--hideheaders` | flag | Disables column headers, valid only with `--output csv` or `--output text` | `false` |
| `--http.proxy.host` | string | Proxy host (leave blank for no proxy) | `null` |
| `--http.proxy.password` | string | Proxy password | `null` |
| `--http.proxy.port` | integer | Proxy port, any number <=0 will result in the default port for a given scheme (e.g. `http -> 80`) | `-1` |
| `--http.proxy.scheme` | string | Proxy scheme, {`http` or `https`} | `http` |
| `--http.proxy.user` | string | Proxy user | `null` |
| `--http.response.maxResults` | integer | Maximum results per http request, any number <=0 results in no limitation | `-1` |
| `-i`, `--infile` | string | Input file (IQL file) from which queries are read | `{stdin}` |
| `-q`, `--iqldata` | string | Context (data) file for templating (`json` or `jsonnet` file)| |
| `--keyfilepath` | string | Service account key filename | |
| `--loglevel` | string | Log level, must be one of {`info`, `warn`, `debug`} | `warn` |
| `--metadatattl` | integer | TTL for cached metadata documents, in seconds | `3600` |
| `--offline` | flag | Work offline, using cached data | `false` |
| `-f`, `--outfile` | string | Output file into which results are written | `{stdout}` |
| `-o`, `--output` | string | Output format, must be one of {`json`, `table`, `csv`, `text`} | `table` |
| `--provider` | string | Default cloud provider | `google` |
| `--providerroot` | string | Configuration and meta data cache root path | `.stackql` directory in current directory |
| `--providerrootfilemode` | uint32 | File mode for configuration and cache root path | `511` |
| `--providerrootfilemode` | uint32 | File mode for configuration and cache root path | `511` |
| `--querycachesize` | integer | Size in number of entries of LRU cache for query plans | `10000` |
| `--reinit` | flag | `reinit` flag; will delete local metadata db file at startup and force regeneration of all dependencies | `false` |
| `--usenonpreferredapis` | flag | Flag to enable non-preferred (alpha) APIs | `false` |
| `-v`, `--verbose` | flag | Verbose output | `false` |
| `-h`, `--help` | | Context specific help for stackql | |
| `--version` | | Displays the version of the StackQL program | |