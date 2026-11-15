---
title: UPDATE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "UPDATE en StackQL: cambiar los campos especificados de una instancia de recurso, equivalente a un PATCH de HTTP, con el destino identificado por la cláusula WHERE."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Actualiza los campos especificados de un recurso (sinónimo de una operación `patch`).

Consulte también:  
[[ Jerarquía de recursos de StackQL ]](/getting-started/resource-hierarchy) [[ `REPLACE` ]](/language-spec/replace)

* * * 

## Sintaxis {#syntax}

*updateStatement::=*

<RailroadDiagram 
type="update"
/>

&nbsp;  
&nbsp;

```sql
UPDATE [ /*+ AWAIT  */ ] <multipartIdentifier>
SET field_name = value [, field_name = value ...]
[ WHERE condition ];
```

* * *

## Ejemplos {#examples}

### Instrucción `UPDATE` básica {#basic-update-statement}
Ejecute una instrucción `UPDATE` para establecer las etiquetas de un bucket de Google Cloud Storage.

```sql
-- Set the labels on a Google Cloud Storage bucket
UPDATE google.storage.buckets
SET data__labels = '{"env": "prod", "team": "platform"}'
WHERE bucket = 'stackql-demo-bucket';
```

### Instrucción `UPDATE` (con bloqueo) {#update-statement-blocking}
Ejecute una instrucción `UPDATE` para modificar una regla de firewall de Compute Engine. Se trata de una invocación con bloqueo (síncrona).

```sql
-- Synchronous update of a google firewall 
UPDATE /*+ AWAIT */ google.compute.firewalls
SET
 data__network = 'https://www.googleapis.com/compute/v1/projects/stackql-demo/global/networks/my-vpc',
 data__direction = 'INGRESS',
 data__sourceRanges = '["0.0.0.0/0"]',
 data__allowed = '[{"IPProtocol": "tcp", "ports": ["22"]}, {"IPProtocol": "tcp", "ports": ["6443"]},{"IPProtocol": "icmp"}]'
WHERE firewall = 'test-fw'
AND project = 'stackql-demo';
```

### Instrucción `UPDATE` con funciones JSON {#update-statement-with-json-functions}
Ejecute una instrucción `UPDATE` cuyo valor se construye con funciones JSON. Una función que no lee ninguna columna se evalúa antes de enviar la solicitud. Un valor de `SET` no puede leer la fila que se actualiza, por lo que, para cambiar parte de un valor existente, léalo primero con un `SELECT` y establezca el valor modificado.

```sql
-- Set the protocols and ports a firewall rule allows
UPDATE google.compute.firewalls
SET data__allowed = json_array(
  json_object('IPProtocol', 'tcp', 'ports', json_array('22', '443')),
  json_object('IPProtocol', 'icmp')
)
WHERE firewall = 'test-fw'
AND project = 'stackql-demo';
```
