---
title: Architecture
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

See also:  
[[ Using StackQL ]](/docs/getting-started/using-stackql) [[ Using Variables ]](/docs/getting-started/variables) [[ Templating ]](/docs/getting-started/templating)

### Overview

The following diagram describes the internals of the StackQL at a high level.

[![StackQL Component Diagram](/img/stackql-component-diagram.svg)](/img/stackql-component-diagram.png)

### Driver
The StackQL Driver is invoked by either a command being run in the [StackQL Interactive Command Shell](/docs/command-line-usage/shell) or a command or commands being submitted using the [`stackql exec`](/docs/command-line-usage/exec) command. The Driver is responsible for the orchestration of StackQL commands. 

### QueryPreProcessor
The QueryPreProcessor is responsible for the preprossing of variables (if provided), using either Json or [Jsonnet](https://jsonnet.org/).  For more information see [[ Templating ]](/docs/getting-started/templating).

### QueryParser
The QueryParser parses the IQL statement and ensures the validity of the syntax provided as well as attribute references and mandatory attributes. 

### ProviderInterface
The ProviderInterface consumes signed extended API specs from the StackQL Provider Registry.

> StackQL provider interface documents are used to enumerate and define all available resources and their associated methods and attributes for a given provider.  This information - collected at execution time - is cached internally for the current session.

### QueryPlanner
The QueryPlanner determines which components of the query will be executed remotely through the relevant Cloud Provider API, and which components will be executed locally (such as aggregate operations).

### RemoteExecutor
The RemoteExecutor is responsible for interacting with the relevant Cloud Provider's API, this includes the handling of asynchronous operations and paginated responses.

### LocalExecutor
The LocalExector is the local embedded SQL engine responsible for operations on provider resource data, such as scalar functions and aggegation operations.