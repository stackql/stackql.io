---
title: UPDATE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "UPDATE dans StackQL : modifier les champs indiqués d'une instance de ressource, équivalent à un PATCH HTTP, la cible étant identifiée par la clause WHERE."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Met à jour les champs indiqués d'une ressource (synonyme d'une opération `patch`).

Voir aussi :  
[[ Hiérarchie des ressources StackQL ]](/getting-started/resource-hierarchy) [[ `REPLACE` ]](/language-spec/replace)

* * * 

## Syntaxe {#syntax}

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

## Exemples {#examples}

### Instruction `UPDATE` de base {#basic-update-statement}
Exécutez une instruction `UPDATE` pour définir les libellés d'un bucket Google Cloud Storage.

```sql
-- Set the labels on a Google Cloud Storage bucket
UPDATE google.storage.buckets
SET data__labels = '{"env": "prod", "team": "platform"}'
WHERE bucket = 'stackql-demo-bucket';
```

### Instruction `UPDATE` (bloquante) {#update-statement-blocking}
Exécutez une instruction `UPDATE` pour modifier une règle de pare-feu Compute Engine. Il s'agit d'un appel bloquant (synchrone).

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

### Instruction `UPDATE` avec des fonctions JSON {#update-statement-with-json-functions}
Exécutez une instruction `UPDATE` dont la valeur est construite avec des fonctions JSON. Une fonction qui ne lit aucune colonne est évaluée avant l'envoi de la requête HTTP. Une valeur de `SET` ne peut pas lire la ligne mise à jour : pour modifier une partie d'une valeur existante, lisez-la d'abord avec un `SELECT`, puis définissez la valeur modifiée.

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
