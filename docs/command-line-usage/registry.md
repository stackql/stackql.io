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
image: https://storage.googleapis.com/stackql-web-assets/blog/stackql-blog-post-featured-image.png
---

Command used to interact with the StackQL Provider Registry.  

* * * 

### Syntax

`stackql registry {subcommand} [{provider} [{version}]]`

* * *

### Arguments

| Argument | Description | Example |
|--|--|--|
|<span class="nowrap">`subcommand`</span>|Registry operation to be performed (either `list` or `pull`) | `list` or `pull` |    
|<span class="nowrap">`provider`</span>|Provider to be installed using `pull` subcommand| `okta` |
|<span class="nowrap">`version`</span>|Optional provider version to be installed using `pull` subcommand *(StackQL Provider Document Version)* | `v23.03.00121` |


### Flags

| Flag | Description |
|--|--|
|<span class="nowrap">`-H, --help`</span>|Print help information|
&nbsp;  
&nbsp;  
> see [Global Flags](/docs/command-line-usage/global-flags) for additional options

* * *

### Examples

List available providers in the public registry:
```shell
stackql registry list
```

Pull the latest version of a provider from the public registry:
```shell
stackql registry pull okta
```