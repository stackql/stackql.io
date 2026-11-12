---
title: REGISTRY
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "REGISTRY dans StackQL : lister les providers et les versions du registre de providers et télécharger la dernière version ou une version spécifique dans votre installation."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';
//import Gist from 'react-gist';

Liste les providers disponibles dans le registre de providers configuré, ou télécharge un provider dans l'installation locale.  

Voir aussi :  
[[ Hiérarchie des ressources StackQL ]](/getting-started/resource-hierarchy) [[` --registry `]](/command-line-usage/global-flags#--registry)

> À partir de `v1.0`, il est possible d'exiger que les documents de provider portent une signature ML-DSA-65 valide au moment de leur lecture. La vérification est désactivée par défaut et s'active avec la clé `verifyConfig` de l'option [`--registry`](/command-line-usage/global-flags#--registry).

* * * 

## Syntaxe {#syntax}

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

## Exemples {#examples}

### Lister les providers disponibles {#list-available-providers}
Récupère la liste des dernières versions de chaque provider disponible.  
```sql
REGISTRY LIST;
```
### Lister toutes les versions d'un provider donné {#list-all-versions-for-a-specific-provider}
Récupère la liste de toutes les versions disponibles d'un provider donné.    
```sql
REGISTRY LIST google;
```

### Installer la dernière version d'un provider {#install-the-latest-version-of-a-provider}
```
REGISTRY PULL okta;
```

### Installer une version spécifique d'un provider {#install-a-specific-version-of-a-provider}
```
REGISTRY PULL azure v23.01.00104;
```
