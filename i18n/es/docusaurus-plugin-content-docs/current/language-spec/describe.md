---
title: DESCRIBE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "DESCRIBE en StackQL: enumerar los campos de un recurso, o los parámetros y el tipo de valor devuelto de un método concreto, antes de consultarlo o modificarlo."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';
import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

Describe un recurso o un método concreto de un recurso.

Consulte también:
[[ Jerarquía de recursos de StackQL ]](/getting-started/resource-hierarchy)

* * *

## Sintaxis {#syntax}

*describeStatement::=*

<RailroadDiagram
type="describe"
/>

&nbsp;
&nbsp;

```sql
DESCRIBE [ METHOD ] [ EXTENDED ] <multipartIdentifier> ;
DESC [ EXTENDED ] <multipartIdentifier> ;
```

`DESC` se acepta como alias de `DESCRIBE` al describir un recurso; `DESCRIBE METHOD` requiere la palabra clave completa.

## Respuesta {#response}

<Tabs
  defaultValue="describe-resource"
  values={[
    { label: 'DESCRIBE', value: 'describe-resource', },
    { label: 'DESCRIBE METHOD', value: 'describe-method', },
  ]
}>
<TabItem value="describe-resource">

| Columna | Descripción |
| --- | --- |
| `name` | Nombre del campo |
| `type` | Tipo de datos (`string`, `integer`, `boolean`, `number`, `object`, `array`) |
| `description` | Descripción del campo (si se especifica `EXTENDED`) |

</TabItem>
<TabItem value="describe-method">

| Columna | Descripción |
| --- | --- |
| `name` | Nombre del campo |
| `type` | Tipo de datos (`string`, `integer`, `boolean`, `number`, `object`, `array`) |
| `param_type` | `input_required`, `input_optional` u `output` |
| `shape` | Subconjunto de JSON Schema para los campos de tipo objeto y array; vacío para los escalares. Incluye los elementos anidados `properties`, `items`, `required`, `enum`, `default`, `description` y, opcionalmente, booleanos si el provider los proporciona (`readOnly`, `writeOnly`, `deprecated`) |
| `description` | Descripción del campo (si se especifica `EXTENDED`) |

</TabItem>
</Tabs>

* * *

## Ejemplos {#examples}

### Instrucción `DESCRIBE` básica {#basic-describe-statement}
Ejecute una instrucción `DESCRIBE` básica para enumerar los campos de un recurso desde una sesión autenticada.

```sql
-- Show the available fields in a Compute Engine resource
DESCRIBE google.compute.instances;
```

### Instrucción `DESCRIBE` extendida {#extended-describe-statement}
Ejecute una instrucción `DESCRIBE` extendida para enumerar los campos de un recurso y sus descripciones desde una sesión autenticada.

```sql
-- Show the available fields in a Compute Engine resource
DESCRIBE EXTENDED google.compute.instances;
```

### Alias `DESC` {#desc-alias}
`DESC` es la forma abreviada de `DESCRIBE` sobre un recurso, con o sin `EXTENDED`. Se trata como una instrucción de solo lectura en todos los lugares donde `DESCRIBE` lo es, incluida la herramienta `run_select_query` del servidor MCP en los modos `read_only` y `safe`.

```sql
-- Same result as DESCRIBE EXTENDED google.compute.instances
DESC EXTENDED google.compute.instances;
```

### Instrucción `DESCRIBE METHOD` básica {#basic-describe-method-statement}
Inspeccione un único método de un recurso. El resultado incluye los parámetros de entrada y los campos de la respuesta, con la estructura anidada representada en la columna `shape`.

```sql
-- Show the inputs and outputs for the buckets.get method
DESCRIBE METHOD google.storage.buckets.get;
```

### Instrucción `DESCRIBE METHOD` extendida {#extended-describe-method-statement}
Ejecute una instrucción `DESCRIBE METHOD` extendida para incluir las descripciones de cada campo junto con la forma.

```sql
-- Show inputs, outputs, and descriptions for the buckets.insert method
DESCRIBE METHOD EXTENDED google.storage.buckets.insert;
```

### Descubrir la carga útil de un `INSERT` {#discovering-an-insert-payload}
`DESCRIBE METHOD` es la forma canónica de descubrir qué campos acepta un `INSERT` (o cualquier método de mutación). La columna `shape` contiene la estructura anidada que los agentes y las personas pueden usar para construir una carga útil con una sola consulta.

```sql
-- See what google.compute.instances.insert requires
DESCRIBE METHOD EXTENDED google.compute.instances.insert;
```
