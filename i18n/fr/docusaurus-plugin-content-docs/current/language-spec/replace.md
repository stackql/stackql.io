---
title: REPLACE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "REPLACE dans StackQL : remplacer tous les champs d'une instance de ressource, équivalent à un PUT HTTP, par opposition à la mise à jour partielle effectuée par UPDATE."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Met à jour tous les champs d'une ressource (synonyme d'une opération `put`).

Voir aussi :  
[[ Hiérarchie des ressources StackQL ]](/getting-started/resource-hierarchy) [[ `UPDATE` ]](/language-spec/update)

* * * 

## Syntaxe {#syntax}

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

## Exemples {#examples}

### Instruction `REPLACE` de base {#basic-replace-statement}
Exécutez une instruction `REPLACE` pour remplacer tous les sujets d'un dépôt GitHub.

```sql
-- Replace all of the topics on a GitHub repository
REPLACE github.repos.topics
SET data__names = '["sql", "cloud-infrastructure"]'
WHERE owner = 'my-org'
AND repo = 'my-repo';
```

### Instruction `REPLACE` (bloquante) {#replace-statement-blocking}
Exécutez une instruction `REPLACE` pour remplacer une règle de pare-feu Compute Engine. Il s'agit d'un appel bloquant (synchrone).

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

### Instruction `REPLACE` avec des fonctions JSON {#replace-statement-with-json-functions}
Exécutez une instruction `REPLACE` dont la valeur est construite avec des fonctions JSON. Une fonction qui ne lit aucune colonne est évaluée avant l'envoi de la requête HTTP. Une valeur de `SET` ne peut pas lire la ligne remplacée : pour modifier une partie d'une valeur existante, lisez-la d'abord avec un `SELECT`, puis définissez la valeur modifiée.

```sql
-- Replace the topics on a GitHub repository with a list built by json_array
REPLACE github.repos.topics
SET data__names = json_array('sql', 'cloud-infrastructure', 'infrastructure-as-code')
WHERE owner = 'my-org'
AND repo = 'my-repo';
```
