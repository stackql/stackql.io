---
title: JSON_GROUP_ARRAY
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: JSON-Objekte mit SQL in StackQL zu einem JSON-Array aggregieren.
image: "/img/stackql-featured-image.png"
---
Aggregiert Werte zu einem JSON-Array. `JSON_GROUP_ARRAY()` sammelt alle Werte jeder Gruppe in einer angegebenen Spalte und fasst sie zu einem JSON-Array zusammen.

Siehe auch:  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract)

* * * 

## Syntax {#syntax}

```sql
SELECT JSON_GROUP_ARRAY(column_name) FROM <multipartIdentifier>
WHERE condition
GROUP BY column_name;
```

## Argumente {#arguments}

__*column_name*__  
Die Spalte, aus der die Werte zu einem JSON-Array aggregiert werden.

## Rückgabewert(e) {#return-values}
Gibt ein JSON-Array zurück, das alle aggregierten Werte aus der angegebenen Spalte enthält.

* * *

## Beispiele {#examples}

### Ressourcengruppennamen zu einem JSON-Array aggregieren {#aggregate-resource-group-names-into-a-json-array}

```sql
-- Collects all resource group names into a JSON array
SELECT JSON_GROUP_ARRAY(name) as res_groups
FROM azure.resource.resource_groups
WHERE subscription_id = '631d1c6d-2a65-43e7-93c2-688bfe4e1468';
```

Diese Abfrage gibt ein JSON-Array mit Ressourcengruppennamen wie `["stackqlenv1","stackqlenv2","stackqlenv3"]` zurück.

### Ressourcengruppen nach Standort aggregieren {#aggregate-resource-groups-by-location}

```sql
-- Collects resource group names into a JSON array grouped by location
SELECT
  location,
  JSON_GROUP_ARRAY(name) AS resource_groups
FROM
  azure.resource.resource_groups
WHERE
  subscription_id = '631d1c6d-2a65-43e7-93c2-688bfe4e1468'
GROUP BY
  location;
```

Diese Abfrage ordnet Ressourcengruppen anhand ihres Standorts in JSON-Arrays ein und gibt mehrere Arrays zurück, eines pro Standort, die jeweils die Namen der Ressourcengruppen an diesem Standort enthalten.
