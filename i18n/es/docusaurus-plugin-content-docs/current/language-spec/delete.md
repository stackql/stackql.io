---
title: DELETE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "DELETE en StackQL: eliminar una o varias instancias de un recurso en la nube o de SaaS con una instrucción SQL, tomando los parámetros obligatorios de la cláusula WHERE."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Elimina una o varias instancias de un recurso.

Consulte también:  
[[ Jerarquía de recursos de StackQL ]](/getting-started/resource-hierarchy)

* * * 

## Sintaxis {#syntax}

*deleteStatement::=*

<RailroadDiagram 
type="delete"
/>

&nbsp;  
&nbsp;  

```sql
DELETE FROM <multipartIdentifier>
WHERE <expression> ;
```

* * *

## Ejemplos {#examples}

### Instrucción `DELETE` básica {#basic-delete-statement}
Ejecute una instrucción DELETE básica para eliminar un recurso desplegado en una sesión autenticada.

```sql
-- Delete a Compute Engine resource instance by name
DELETE FROM google.compute.instances 
WHERE instance = 'demo-instance-1' 
AND project = 'stackql-demo' AND zone = 'australia-southeast1-a';
```
