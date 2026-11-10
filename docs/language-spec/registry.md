---
title: REGISTRY
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: "REGISTRY in StackQL: list the providers and versions in the provider registry and pull the latest or a specific version into your installation."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';
//import Gist from 'react-gist';

Lists the providers available in the configured provider registry, or pulls a provider into the local installation.  

See also:  
[[ StackQL Resource Hierarchy ]](/getting-started/resource-hierarchy) [[` --registry `]](/command-line-usage/global-flags#--registry)

> From `v1.0`, provider documents can be required to carry a valid ML-DSA-65 signature when they are read.  Verification is off by default and is enabled with the `verifyConfig` key of the [`--registry`](/command-line-usage/global-flags#--registry) flag.

* * * 

## Syntax {#syntax}

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

## Examples {#examples}

### List available providers {#list-available-providers}
Retrieves the list of the latest versions of each available provider.  
```sql
REGISTRY LIST;
```
### List all versions for a specific provider {#list-all-versions-for-a-specific-provider}
Retrieves the list of all available versions of a specific provider.    
```sql
REGISTRY LIST google;
```

### Install the latest version of a provider {#install-the-latest-version-of-a-provider}
```
REGISTRY PULL okta;
```

### Install a specific version of a provider {#install-a-specific-version-of-a-provider}
```
REGISTRY PULL azure v23.01.00104;
```
