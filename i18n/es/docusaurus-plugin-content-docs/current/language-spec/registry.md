---
title: REGISTRY
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "REGISTRY en StackQL: enumerar los providers y las versiones del registro de providers y descargar la versión más reciente o una versión concreta en su instalación."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Enumera los providers disponibles en el registro de providers configurado o descarga un provider en la instalación local.  

Consulte también:  
[[ Jerarquía de recursos de StackQL ]](/getting-started/resource-hierarchy) [[` --registry `]](/command-line-usage/global-flags#--registry)

> A partir de `v1.0`, se puede exigir que los documentos de provider lleven una firma ML-DSA-65 válida cuando se leen.  La verificación está deshabilitada de forma predeterminada y se habilita con la clave `verifyConfig` de la opción [`--registry`](/command-line-usage/global-flags#--registry).

* * * 

## Sintaxis {#syntax}

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

## Ejemplos {#examples}

### Enumerar los providers disponibles {#list-available-providers}
Recupera la lista de las versiones más recientes de cada provider disponible.  
```sql
REGISTRY LIST;
```
### Enumerar todas las versiones de un provider concreto {#list-all-versions-for-a-specific-provider}
Recupera la lista de todas las versiones disponibles de un provider concreto.    
```sql
REGISTRY LIST google;
```

### Instalar la versión más reciente de un provider {#install-the-latest-version-of-a-provider}
```sql
REGISTRY PULL okta;
```

### Instalar una versión concreta de un provider {#install-a-specific-version-of-a-provider}
```sql
REGISTRY PULL azure v24.10.00267;
```
