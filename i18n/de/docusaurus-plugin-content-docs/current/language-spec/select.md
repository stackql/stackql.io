---
title: SELECT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "SELECT in StackQL: Cloud- und SaaS-Ressourcen mit SQL abfragen, mit WHERE-Filtern, die auf API-Parameter abgebildet werden, Joins, Gruppierung, Sortierung und JSON-Feldzugriff."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Gibt eine oder mehrere Instanzen einer Ressource zurück.  

Siehe auch:
[[ StackQL-Ressourcenhierarchie ]](/getting-started/resource-hierarchy) [[` WITH (CTEs) `]](/language-spec/with) [[` Fensterfunktionen `]](/language-spec/windowing_functions)

* * * 

## Syntax {#syntax}

*selectStatement::=*

<RailroadDiagram 
type="select"
/>

&nbsp;  
&nbsp;

```sql
[ WITH [ RECURSIVE ] <cteName> [ ( <columnList> ) ] AS ( <selectStatement> ) [, ...] ]
SELECT { * | <fieldList> | <windowFunctionCall> [ AS <alias> ] }
FROM { <multipartIdentifier> | <joinStatement(s)> }
[ WHERE <expression> ]
[ GROUP BY <fieldList> ]
[ HAVING <expression> ]
[ WINDOW <windowName> AS ( <windowSpec> ) [, ...] ]
[ ORDER BY <fieldList> [ ASC | DESC ] ]
[ LIMIT <integer> ]
[ UNION <selectStatement> ];
```

Ein `JOIN` ohne `ON`-Klausel wird als `CROSS JOIN` behandelt, sodass die Join-Bedingung stattdessen in der `WHERE`-Klausel angegeben werden kann.  StackQL-Releases vor `v0.12.732` können einen `JOIN` ohne Bedingung nicht planen.

* * *

## Beispiele {#examples}

### Einfache `SELECT`-Anweisung {#basic-select-statement}
Führen Sie eine einfache `SELECT`-Anweisung in einer authentifizierten Sitzung aus.

```sql
-- Selecting all resources deployed within a service using a basic SELECT statement
SELECT * FROM google.compute.instances
WHERE project = 'stackql-demo'
AND zone = 'australia-southeast1-a';
```

### Bestimmte Felder mit einer `SELECT`-Anweisung zurückgeben {#returning-specified-fields-using-a-select-statement}
Führen Sie eine `SELECT`-Anweisung mit einer Spaltenliste aus, um bestimmte Felder einer Compute-Engine-Instanz zurückzugeben.

```sql
-- Selecting specified fields from a resource
SELECT id, name 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Eine aufsteigend nach einer Spalte sortierte Ergebnismenge mit der `ORDER BY`-Klausel zurückgeben {#return-a-result-set-ordered-by-a-column-in-ascending-order-using-the-order-by-clause}
Führen Sie eine `SELECT`-Anweisung aus, um Felder der Cloud-Storage-Buckets-Ressource zurückzugeben und die Ergebnisse aufsteigend (älteste zuerst) zu sortieren. 

```sql
-- Order a list of Cloud Storage buckets by creation time (in ascending order)
SELECT name, location, timeCreated 
FROM google.storage.buckets WHERE project = 'stackql'
ORDER BY timeCreated;
```

### Eine absteigend nach einer Spalte sortierte Ergebnismenge mit der `ORDER BY`-Klausel zurückgeben {#return-a-result-set-ordered-by-a-column-in-descending-order-using-the-order-by-clause}
Führen Sie eine `SELECT`-Anweisung aus, um Felder der Cloud-Storage-Buckets-Ressource zurückzugeben und die Ergebnisse absteigend (neueste zuerst) zu sortieren.

```sql
-- Order a list of Cloud Storage buckets by creation time (in descending order)
SELECT name, location, timeCreated 
FROM google.storage.buckets WHERE project = 'stackql'
ORDER BY timeCreated DESC;
```

### Das oberste Ergebnis einer Spalte mit den Klauseln `ORDER BY` und `LIMIT` abrufen {#get-the-top-result-from-a-column-using-the-order-by-and-limit-clauses}
Führen Sie eine `SELECT`-Anweisung aus, um den zuletzt erstellten Cloud-Storage-Bucket zurückzugeben.

```sql
-- Find the most recently created bucket
SELECT name, location, timeCreated 
FROM google.storage.buckets WHERE project = 'stackql'
ORDER BY timeCreated DESC LIMIT 1;
```

### Die Funktion `COUNT` mit den Klauseln `GROUP BY` und `HAVING` verwenden, um Ressourcen anhand einer Gruppierung und eines Filters zu zählen {#use-the-count-function-with-the-group-by-and-having-clauses-to-count-resources-based-upon-a-grouping-and-filter}
Führen Sie eine `SELECT`-Anweisung mit der Funktion `COUNT` aus und verwenden Sie die Klauseln `GROUP BY` und `HAVING`, um die Anzahl der Buckets an jedem Standort außer Asien zurückzugeben.

```sql
-- Return a count of the number of buckets in every location other than Asia
SELECT name, location, count(*) as num_buckets 
FROM google.storage.buckets WHERE project = 'stackql'
GROUP BY location
HAVING location != 'ASIA';
```

Weitere Informationen zur Funktion `COUNT` und zu anderen von StackQL unterstützten Aggregatfunktionen finden Sie unter [Aggregatfunktionen](/language-spec/functions/aggregate/count).

### Liste der On-Demand-VMs und der zugehörigen Netzwerkschnittstelle aus einem Azure VM Scaleset abrufen {#get-list-of-on-demand-vms-and-corresponding-network-interface-from-azure-vm-scaleset}
Führen Sie eine `SELECT`-Anweisung mit den Funktionen [`SPLIT_PART`](/language-spec/functions/string/split_part) und [`JSON_EXTRACT`](/language-spec/functions/json/json_extract) aus, um die Liste der OD-VMs und ihrer NICs abzurufen. Beachten Sie, dass subscription_id und resource_group_name ohne Qualifizierer auf alle drei Ressourcen angewendet werden.

```sql
SELECT  c.name AS vm_name,
        SPLIT_PART(JSON_EXTRACT(c.properties,'$.networkProfile.networkInterfaces[0].id'), '/', -1) AS nic_name
FROM
  azure.compute.virtual_machine_scale_sets a
  INNER JOIN  azure.compute.virtual_machine_scale_set_vms b
    ON a.name = b.virtualMachineScaleSetName
  INNER JOIN  azure.compute.virtual_machines c
    ON b.name = c.name
WHERE subscription_id = '0123456789'
AND resource_group_name = 'vmss-flex'
AND JSON_EXTRACT(c.properties,'$.priority') is null;
```

Weitere Informationen zu Fensterfunktionen finden Sie unter [Fensterfunktionen](/language-spec/windowing_functions).

### Common Table Expressions (CTEs) mit `WITH` verwenden {#using-common-table-expressions-ctes-with-with}

CTEs ermöglichen es Ihnen, temporäre benannte Ergebnismengen zu definieren, auf die in der Hauptabfrage verwiesen werden kann.

```sql
-- Combine and rank contributors across multiple repositories
WITH all_contributors AS (
    SELECT login, contributions, html_url
    FROM github.repos.contributors
    WHERE owner = 'stackql' AND repo = 'stackql'
    UNION ALL
    SELECT login, contributions, html_url
    FROM github.repos.contributors
    WHERE owner = 'stackql' AND repo = 'stackql-deploy'
    UNION ALL
    SELECT login, contributions, html_url
    FROM github.repos.contributors
    WHERE owner = 'stackql' AND repo = 'stackql-provider-registry'
)
SELECT
    DENSE_RANK() OVER (ORDER BY SUM(contributions) DESC) as rank,
    login,
    SUM(contributions) as total_contributions,
    MAX(html_url) as html_url
FROM all_contributors
GROUP BY login
ORDER BY total_contributions DESC;
```

Weitere Informationen zu CTEs finden Sie unter [WITH (CTEs)](/language-spec/with).
