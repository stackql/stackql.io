---
title: UPDATE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "UPDATE in StackQL: bestimmte Felder einer Ressourceninstanz ändern, entsprechend einem HTTP PATCH, wobei das Ziel über die WHERE-Klausel bestimmt wird."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Aktualisiert bestimmte Felder einer Ressource (gleichbedeutend mit einer `patch`-Operation).

Siehe auch:  
[[ StackQL-Ressourcenhierarchie ]](/getting-started/resource-hierarchy) [[ `REPLACE` ]](/language-spec/replace)

* * * 

## Syntax {#syntax}

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

## Beispiele {#examples}

### Einfache `UPDATE`-Anweisung {#basic-update-statement}
Führen Sie eine `UPDATE`-Anweisung aus, um die Labels eines Google-Cloud-Storage-Buckets festzulegen.

```sql
-- Set the labels on a Google Cloud Storage bucket
UPDATE google.storage.buckets
SET data__labels = '{"env": "prod", "team": "platform"}'
WHERE bucket = 'stackql-demo-bucket';
```

### `UPDATE`-Anweisung (blockierend) {#update-statement-blocking}
Führen Sie eine `UPDATE`-Anweisung aus, um eine Firewallregel in Compute Engine zu ändern. Dies ist ein blockierender (synchroner) Aufruf.

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

### `UPDATE`-Anweisung mit JSON-Funktionen {#update-statement-with-json-functions}
Führen Sie eine `UPDATE`-Anweisung aus, deren Wert mit JSON-Funktionen gebildet wird. Eine Funktion, die keine Spalten liest, wird ausgewertet, bevor die Anfrage gesendet wird. Ein `SET`-Wert kann die zu aktualisierende Zeile nicht lesen. Um einen Teil eines vorhandenen Werts zu ändern, lesen Sie ihn daher zuerst mit einem `SELECT` und setzen dann den geänderten Wert.

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
