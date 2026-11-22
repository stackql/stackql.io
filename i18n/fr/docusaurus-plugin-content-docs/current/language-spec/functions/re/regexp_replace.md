---
title: REGEXP_REPLACE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Fonction REGEXP_REPLACE dans StackQL : remplacer les parties d'une chaîne qui correspondent à une expression régulière par une chaîne de remplacement."
image: "/img/stackql-featured-image.png"
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

Remplace une sous-chaîne correspondante par une chaîne de caractères de remplacement.  

Voir aussi :  
[[` SELECT `]](/language-spec/select) [[ Référence des expressions régulières ]](/language-spec/functions/re/reference) 

* * * 

## Syntaxe {#syntax}

```sql
SELECT REGEXP_REPLACE(source, pattern, replacement) FROM <multipartIdentifier>;
```
## Arguments {#arguments}

__*source*__  
Une chaîne littérale ou une colonne de type chaîne dans laquelle les correspondances de l'expression régulière `pattern` sont remplacées par `replacement`.  

__*pattern*__  
Le motif d'expression régulière à rechercher.  

__*replacement*__  
La chaîne de remplacement.

> La chaîne de remplacement est insérée telle quelle : les références aux groupes de capture telles que `$1` ne sont pas développées.  

## Valeur(s) renvoyée(s) {#return-values}
Renvoie une chaîne.

* * *

## Exemples {#examples}

### Remplacer un motif d'expression régulière dans une colonne {#replace-a-regular-expression-pattern-in-a-column}

<Tabs
  defaultValue="iql"
  values={[
    { label: 'StackQL', value: 'iql', },
    { label: 'Résultats', value: 'results', },
  ]
}>
<TabItem value="iql">

```sql
select name,
regexp_replace(json_extract(properties, '$.timeCreated'), 'T.*', '') as date_created
from azure.compute.virtual_machines 
WHERE subscription_id = '273769f6-545f-45b2-8ab8-2f14ec5768dc';
```

</TabItem>
<TabItem value="results">

```
|------|--------------|
| name | date_created |
|------|--------------|
| vm0  | 2022-09-14   |
|------|--------------|
| vm1  | 2022-09-14   |
|------|--------------|
| vm2  | 2022-09-14   |
|------|--------------|
```

</TabItem>
</Tabs>

Pour plus d'informations, consultez [https://github.com/google/re2/wiki/Syntax](https://github.com/google/re2/wiki/Syntax)
