---
title: REGISTRY
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "REGISTRY in StackQL: listet die Provider und Versionen der Provider-Registry auf und lädt die neueste oder eine bestimmte Version in Ihre Installation."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';
//import Gist from 'react-gist';

Listet die in der konfigurierten Provider-Registry verfügbaren Provider auf oder lädt einen Provider in die lokale Installation.  

Siehe auch:  
[[ StackQL-Ressourcenhierarchie ]](/getting-started/resource-hierarchy) [[` --registry `]](/command-line-usage/global-flags#--registry)

> Ab `v1.0` kann verlangt werden, dass Provider-Dokumente beim Lesen eine gültige ML-DSA-65-Signatur tragen.  Die Überprüfung ist standardmäßig deaktiviert und wird mit dem Schlüssel `verifyConfig` des Flags [`--registry`](/command-line-usage/global-flags#--registry) aktiviert.

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

## Beispiele {#examples}

### Verfügbare Provider auflisten {#list-available-providers}
Ruft die Liste der neuesten Versionen jedes verfügbaren Providers ab.  
```sql
REGISTRY LIST;
```
### Alle Versionen eines bestimmten Providers auflisten {#list-all-versions-for-a-specific-provider}
Ruft die Liste aller verfügbaren Versionen eines bestimmten Providers ab.    
```sql
REGISTRY LIST google;
```

### Die neueste Version eines Providers installieren {#install-the-latest-version-of-a-provider}
```
REGISTRY PULL okta;
```

### Eine bestimmte Version eines Providers installieren {#install-a-specific-version-of-a-provider}
```
REGISTRY PULL azure v23.01.00104;
```
