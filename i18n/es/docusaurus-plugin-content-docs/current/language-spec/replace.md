---
title: REPLACE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "REPLACE en StackQL: reemplazar todos los campos de una instancia de recurso, equivalente a un PUT de HTTP, a diferencia de la actualización parcial que realiza UPDATE."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Actualiza todos los campos de un recurso (sinónimo de una operación `put`).

Consulte también:  
[[ Jerarquía de recursos de StackQL ]](/getting-started/resource-hierarchy) [[ `UPDATE` ]](/language-spec/update)

* * * 

## Sintaxis {#syntax}

*replaceStatement::=*

<RailroadDiagram 
type="replace"
/>

&nbsp;  
&nbsp;

```sql
REPLACE [ /*+ AWAIT  */ ] <multipartIdentifier>
SET field_name = value [, field_name = value ...]
[ WHERE condition ];
```

* * *

## Ejemplos {#examples}

### Instrucción `REPLACE` básica {#basic-replace-statement}
Ejecute una instrucción `REPLACE` para reemplazar todos los temas de un repositorio de GitHub.

```sql
-- Replace all of the topics on a GitHub repository
REPLACE github.repos.topics
SET data__names = '["sql", "cloud-infrastructure"]'
WHERE owner = 'my-org'
AND repo = 'my-repo';
```

### Instrucción `REPLACE` (con bloqueo) {#replace-statement-blocking}
Ejecute una instrucción `REPLACE` para reemplazar una regla de firewall de Compute Engine. Se trata de una invocación con bloqueo (síncrona).

```sql
-- Synchronous update of a google firewall 
REPLACE /*+ AWAIT */ google.compute.firewalls
SET
 data__name = 'test-fw',
 data__network = 'https://www.googleapis.com/compute/v1/projects/stackql-demo/global/networks/my-vpc',
 data__direction = 'INGRESS',
 data__sourceRanges = '["0.0.0.0/0"]',
 data__allowed = '[{"IPProtocol": "tcp", "ports": ["22"]}, {"IPProtocol": "tcp", "ports": ["6443"]},{"IPProtocol": "icmp"}]'
WHERE firewall = 'test-fw'
AND project = 'stackql-demo';
```

### Instrucción `REPLACE` con funciones JSON {#replace-statement-with-json-functions}
Ejecute una instrucción `REPLACE` cuyo valor se construye con funciones JSON. Una función que no lee ninguna columna se evalúa antes de enviar la solicitud. Un valor de `SET` no puede leer la fila que se reemplaza, por lo que, para cambiar parte de un valor existente, léalo primero con un `SELECT` y establezca el valor modificado.

```sql
-- Replace the topics on a GitHub repository with a list built by json_array
REPLACE github.repos.topics
SET data__names = json_array('sql', 'cloud-infrastructure', 'infrastructure-as-code')
WHERE owner = 'my-org'
AND repo = 'my-repo';
```
