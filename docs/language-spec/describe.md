---
title: DESCRIBE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Query and Deploy Cloud Infrastructure and Resources using SQL
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';
import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

Describes a resource or a specific method on a resource.

See also:
[[ StackQL Resource Hierarchy ]](/docs/getting-started/resource-hierarchy)

* * *

## Syntax

*describeStatement::=*

<RailroadDiagram
type="describe"
/>

&nbsp;
&nbsp;

```sql
DESCRIBE [ METHOD ] [ EXTENDED ] <multipartIdentifier> ;
```

## Response

<Tabs
  defaultValue="describe-resource"
  values={[
    { label: 'DESCRIBE', value: 'describe-resource', },
    { label: 'DESCRIBE METHOD', value: 'describe-method', },
  ]
}>
<TabItem value="describe-resource">

| Column | Description |
| --- | --- |
| `name` | Field name |
| `type` | Datatype (`string`, `integer`, `boolean`, `number`, `object`, `array`) |
| `description` | Field description (if `EXTENDED` is supplied) |

</TabItem>
<TabItem value="describe-method">

| Column | Description |
| --- | --- |
| `name` | Field name |
| `type` | Datatype (`string`, `integer`, `boolean`, `number`, `object`, `array`) |
| `param_type` | `input_required`, `input_optional`, or `output` |
| `shape` | JSON Schema subset for object and array fields; empty for scalars. Includes nested `properties`, `items`, `required`, `enum`, `default`, `description`, and optionally includes booleans if made available by the provider (`readOnly`, `writeOnly`, `deprecated`) |
| `description` | Field description (if `EXTENDED` is supplied) |

</TabItem>
</Tabs>

* * *

## Examples

### Basic `DESCRIBE` Statement
Run a basic DESCRIBE statement to list the fields in a resource from an authenticated session.

```sql
-- Show the available fields in a Compute Engine resource
DESCRIBE google.compute.instances;
```

### Extended `DESCRIBE` Statement
Run an extended DESCRIBE statement to list the fields in a resource and their descriptions from an authenticated session.

```sql
-- Show the available fields in a Compute Engine resource
DESCRIBE EXTENDED google.compute.instances;
```

### Basic `DESCRIBE METHOD` Statement
Introspect a single method on a resource. The result includes input parameters and response fields, with nested structure rendered in the `shape` column.

```sql
-- Show the inputs and outputs for the buckets.get method
DESCRIBE METHOD google.storage.buckets.get;
```

### Extended `DESCRIBE METHOD` Statement
Run an extended DESCRIBE METHOD statement to include per-field descriptions alongside the shape.

```sql
-- Show inputs, outputs, and descriptions for the buckets.insert method
DESCRIBE METHOD EXTENDED google.storage.buckets.insert;
```

### Discovering an `INSERT` Payload
`DESCRIBE METHOD` is the canonical way to discover what fields an `INSERT` (or any mutating method) accepts. The `shape` column carries nested structure agents and humans can use to construct a payload in one query.

```sql
-- See what google.compute.instances.insert requires
DESCRIBE METHOD EXTENDED google.compute.instances.insert;
```
