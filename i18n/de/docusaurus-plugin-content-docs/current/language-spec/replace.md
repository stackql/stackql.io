---
title: REPLACE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "REPLACE in StackQL: ersetzt alle Felder einer Ressourceninstanz, entsprechend einem HTTP PUT, im Gegensatz zur partiellen Aktualisierung durch UPDATE."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Aktualisiert alle Felder einer Ressource (gleichbedeutend mit einer `put`-Operation).

Siehe auch:  
[[ StackQL-Ressourcenhierarchie ]](/getting-started/resource-hierarchy) [[ `UPDATE` ]](/language-spec/update)

* * * 

## Syntax {#syntax}

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

## Beispiele {#examples}

### Einfache `REPLACE`-Anweisung {#basic-replace-statement}
Führen Sie eine `REPLACE`-Anweisung aus, um alle Topics eines GitHub-Repositorys zu ersetzen.

```sql
-- Replace all of the topics on a GitHub repository
REPLACE github.repos.topics
SET data__names = '["sql", "cloud-infrastructure"]'
WHERE owner = 'my-org'
AND repo = 'my-repo';
```

### `REPLACE`-Anweisung (blockierend) {#replace-statement-blocking}
Führen Sie eine `REPLACE`-Anweisung aus, um eine Firewallregel in Compute Engine zu ersetzen. Dies ist ein blockierender (synchroner) Aufruf.

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

### `REPLACE`-Anweisung mit JSON-Funktionen {#replace-statement-with-json-functions}
Führen Sie eine `REPLACE`-Anweisung aus, deren Wert mit JSON-Funktionen gebildet wird. Eine Funktion, die keine Spalten liest, wird ausgewertet, bevor die Anfrage gesendet wird. Ein `SET`-Wert kann die zu ersetzende Zeile nicht lesen. Um einen Teil eines vorhandenen Werts zu ändern, lesen Sie ihn daher zuerst mit einem `SELECT` und setzen dann den geänderten Wert.

```sql
-- Replace the topics on a GitHub repository with a list built by json_array
REPLACE github.repos.topics
SET data__names = json_array('sql', 'cloud-infrastructure', 'infrastructure-as-code')
WHERE owner = 'my-org'
AND repo = 'my-repo';
```
