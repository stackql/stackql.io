---
title: REGISTRY
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
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';
//import Gist from 'react-gist';

Changes the context to a specified provider or service.  

See also:  
[[ StackQL Resource Hierarchy ]](/docs/getting-started/resource-hierarchy)

* * * 

## Syntax

*registryStatement::=*

<RailroadDiagram 
type="registry"
/>

&nbsp;  
&nbsp;  

```sql
REGISTRY { LIST [<provider>] | PULL <provider> [<version>]};
```

* * *

## Examples

### List available providers
Retrieves the list of the latest versions of each available provider.  
```sql
REGISTRY LIST;
```
### List all versions for a specific provider
Retrieves the list of all available versions of a specific provider.    
```sql
REGISTRY LIST google;
```

### Install the latest version of a provider
```
REGISTRY PULL okta;
```

### Install a specific version of a provider
```
REGISTRY PULL azure v23.01.00104;
```
