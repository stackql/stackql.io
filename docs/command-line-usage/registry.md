---
title: registry
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: "The stackql registry command lists, pulls and manages providers from the StackQL provider registry from the command line."
image: "/img/stackql-featured-image.png"
---

Command used to interact with the StackQL Provider Registry.  The registry location, and whether provider document signatures are verified, are configured with the [`--registry`](/command-line-usage/global-flags#--registry) global flag.  The `registry` command is not available while [access management](/command-line-usage/access-management) is enabled; use `REGISTRY PULL` in a protected `exec` or `shell` session instead.

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
<br/>
<br/>
> see [Global Flags](/command-line-usage/global-flags) for additional options

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