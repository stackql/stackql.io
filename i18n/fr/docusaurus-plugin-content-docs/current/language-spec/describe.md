---
title: DESCRIBE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "DESCRIBE dans StackQL : lister les champs d'une ressource, ou les paramètres et le type de retour d'une méthode donnée, avant de l'interroger ou de la modifier."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';
import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

Décrit une ressource ou une méthode donnée d'une ressource.

Voir aussi :
[[ Hiérarchie des ressources StackQL ]](/getting-started/resource-hierarchy)

* * *

## Syntaxe {#syntax}

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

`DESC` est accepté comme alias de `DESCRIBE` pour décrire une ressource ; `DESCRIBE METHOD` exige le mot-clé complet.

## Réponse {#response}

<Tabs
  defaultValue="describe-resource"
  values={[
    { label: 'DESCRIBE', value: 'describe-resource', },
    { label: 'DESCRIBE METHOD', value: 'describe-method', },
  ]
}>
<TabItem value="describe-resource">

| Colonne | Description |
| --- | --- |
| `name` | Nom du champ |
| `type` | Type de données (`string`, `integer`, `boolean`, `number`, `object`, `array`) |
| `description` | Description du champ (si `EXTENDED` est fourni) |

</TabItem>
<TabItem value="describe-method">

| Colonne | Description |
| --- | --- |
| `name` | Nom du champ |
| `type` | Type de données (`string`, `integer`, `boolean`, `number`, `object`, `array`) |
| `param_type` | `input_required`, `input_optional` ou `output` |
| `shape` | Sous-ensemble de JSON Schema pour les champs de type objet et tableau ; vide pour les scalaires. Inclut les éléments imbriqués `properties`, `items`, `required`, `enum`, `default`, `description`, et éventuellement des booléens si le provider les met à disposition (`readOnly`, `writeOnly`, `deprecated`) |
| `description` | Description du champ (si `EXTENDED` est fourni) |

</TabItem>
</Tabs>

* * *

## Exemples {#examples}

### Instruction `DESCRIBE` de base {#basic-describe-statement}
Depuis une session authentifiée, exécutez une instruction `DESCRIBE` de base pour lister les champs d'une ressource.

```sql
-- Show the available fields in a Compute Engine resource
DESCRIBE google.compute.instances;
```

### Instruction `DESCRIBE` étendue {#extended-describe-statement}
Depuis une session authentifiée, exécutez une instruction `DESCRIBE` étendue pour lister les champs d'une ressource et leurs descriptions.

```sql
-- Show the available fields in a Compute Engine resource
DESCRIBE EXTENDED google.compute.instances;
```

### Alias `DESC` {#desc-alias}
`DESC` est la forme abrégée de `DESCRIBE` pour une ressource, avec ou sans `EXTENDED`.  Cette forme est traitée comme une instruction en lecture seule partout où `DESCRIBE` l'est, y compris par l'outil `run_select_query` du serveur MCP dans les modes `read_only` et `safe`.

```sql
-- Same result as DESCRIBE EXTENDED google.compute.instances
DESC EXTENDED google.compute.instances;
```

### Instruction `DESCRIBE METHOD` de base {#basic-describe-method-statement}
Inspectez une seule méthode d'une ressource. Le résultat inclut les paramètres d'entrée et les champs de la réponse, la structure imbriquée étant rendue dans la colonne `shape`.

```sql
-- Show the inputs and outputs for the buckets.get method
DESCRIBE METHOD google.storage.buckets.get;
```

### Instruction `DESCRIBE METHOD` étendue {#extended-describe-method-statement}
Exécutez une instruction `DESCRIBE METHOD` étendue pour inclure la description de chaque champ en plus de la forme.

```sql
-- Show inputs, outputs, and descriptions for the buckets.insert method
DESCRIBE METHOD EXTENDED google.storage.buckets.insert;
```

### Découvrir la charge utile d'un `INSERT` {#discovering-an-insert-payload}
`DESCRIBE METHOD` est le moyen de référence pour découvrir les champs qu'un `INSERT` (ou toute méthode de mutation) accepte. La colonne `shape` contient la structure imbriquée dont les agents et les humains peuvent se servir pour construire une charge utile en une seule requête.

```sql
-- See what google.compute.instances.insert requires
DESCRIBE METHOD EXTENDED google.compute.instances.insert;
```
