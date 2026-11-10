---
title: DESCRIBE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "DESCRIBE in StackQL: listet die Felder einer Ressource oder die Parameter und den Rückgabetyp einer bestimmten Methode auf, bevor Sie sie abfragen oder ändern."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';
import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

Beschreibt eine Ressource oder eine bestimmte Methode einer Ressource.

Siehe auch:
[[ StackQL-Ressourcenhierarchie ]](/getting-started/resource-hierarchy)

* * *

## Syntax {#syntax}

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

`DESC` wird beim Beschreiben einer Ressource als Alias für `DESCRIBE` akzeptiert; `DESCRIBE METHOD` erfordert das vollständige Schlüsselwort.

## Antwort {#response}

<Tabs
  defaultValue="describe-resource"
  values={[
    { label: 'DESCRIBE', value: 'describe-resource', },
    { label: 'DESCRIBE METHOD', value: 'describe-method', },
  ]
}>
<TabItem value="describe-resource">

| Spalte | Beschreibung |
| --- | --- |
| `name` | Feldname |
| `type` | Datentyp (`string`, `integer`, `boolean`, `number`, `object`, `array`) |
| `description` | Feldbeschreibung (wenn `EXTENDED` angegeben ist) |

</TabItem>
<TabItem value="describe-method">

| Spalte | Beschreibung |
| --- | --- |
| `name` | Feldname |
| `type` | Datentyp (`string`, `integer`, `boolean`, `number`, `object`, `array`) |
| `param_type` | `input_required`, `input_optional` oder `output` |
| `shape` | JSON-Schema-Teilmenge für Objekt- und Array-Felder; leer bei Skalaren. Enthält verschachtelte `properties`, `items`, `required`, `enum`, `default`, `description` und optional boolesche Angaben, sofern der Provider sie bereitstellt (`readOnly`, `writeOnly`, `deprecated`) |
| `description` | Feldbeschreibung (wenn `EXTENDED` angegeben ist) |

</TabItem>
</Tabs>

* * *

## Beispiele {#examples}

### Einfache `DESCRIBE`-Anweisung {#basic-describe-statement}
Führen Sie eine einfache `DESCRIBE`-Anweisung aus, um die Felder einer Ressource aus einer authentifizierten Sitzung aufzulisten.

```sql
-- Show the available fields in a Compute Engine resource
DESCRIBE google.compute.instances;
```

### Erweiterte `DESCRIBE`-Anweisung {#extended-describe-statement}
Führen Sie eine erweiterte `DESCRIBE`-Anweisung aus, um die Felder einer Ressource und ihre Beschreibungen aus einer authentifizierten Sitzung aufzulisten.

```sql
-- Show the available fields in a Compute Engine resource
DESCRIBE EXTENDED google.compute.instances;
```

### `DESC`-Alias {#desc-alias}
`DESC` ist die Kurzform von `DESCRIBE` für eine Ressource, mit oder ohne `EXTENDED`.  Sie wird überall dort als reine Leseanweisung behandelt, wo `DESCRIBE` es wird, einschließlich des Tools `run_select_query` des MCP-Servers in den Modi `read_only` und `safe`.

```sql
-- Same result as DESCRIBE EXTENDED google.compute.instances
DESC EXTENDED google.compute.instances;
```

### Einfache `DESCRIBE METHOD`-Anweisung {#basic-describe-method-statement}
Inspizieren Sie eine einzelne Methode einer Ressource. Das Ergebnis enthält Eingabeparameter und Antwortfelder; die verschachtelte Struktur wird in der Spalte `shape` dargestellt.

```sql
-- Show the inputs and outputs for the buckets.get method
DESCRIBE METHOD google.storage.buckets.get;
```

### Erweiterte `DESCRIBE METHOD`-Anweisung {#extended-describe-method-statement}
Führen Sie eine erweiterte `DESCRIBE METHOD`-Anweisung aus, um zusätzlich zur Struktur die Beschreibungen der einzelnen Felder einzuschließen.

```sql
-- Show inputs, outputs, and descriptions for the buckets.insert method
DESCRIBE METHOD EXTENDED google.storage.buckets.insert;
```

### Eine `INSERT`-Payload ermitteln {#discovering-an-insert-payload}
`DESCRIBE METHOD` ist der kanonische Weg, um zu ermitteln, welche Felder ein `INSERT` (oder jede andere verändernde Methode) akzeptiert. Die Spalte `shape` enthält die verschachtelte Struktur, mit der Agenten und Menschen eine Payload in einer einzigen Abfrage erstellen können.

```sql
-- See what google.compute.instances.insert requires
DESCRIBE METHOD EXTENDED google.compute.instances.insert;
```
