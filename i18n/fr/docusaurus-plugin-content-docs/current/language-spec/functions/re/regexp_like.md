---
title: REGEXP_LIKE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Fonction REGEXP_LIKE dans StackQL : tester si une chaîne correspond à une expression régulière, en renvoyant true ou false, pour filtrer par motif dans les clauses WHERE."
image: "/img/stackql-featured-image.png"
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

Vérifie si la chaîne de caractères source correspond au motif et renvoie `true` ou `false`.  

Voir aussi :  
[[` SELECT `]](/language-spec/select) [[ Référence des expressions régulières ]](/language-spec/functions/re/reference) 

* * * 

## Syntaxe {#syntax}

```sql
SELECT REGEXP_LIKE(source, pattern) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*source*__  
Une chaîne littérale ou une colonne de type chaîne dans laquelle vous souhaitez tester la présence d'un motif défini par une expression régulière (`pattern`).  

__*pattern*__  
Le motif d'expression régulière à rechercher.  

## Valeur(s) renvoyée(s) {#return-values}
Renvoie `0` si `false` et `1` si `true`.  

* * *

## Exemples {#examples}

### Déterminer si une chaîne contient un motif {#determine-if-a-string-contains-a-pattern}

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
regexp_like(name, 'vm[0-9]+') as name_starts_with_vm 
from azure.compute.virtual_machines 
WHERE subscription_id = '273769f6-545f-45b2-8ab8-2f14ec5768dc';
```

</TabItem>
<TabItem value="results">

```
|------|---------------------|
| name | name_starts_with_vm |
|------|---------------------|
| vm0  |                   1 |
|------|---------------------|
| vm1  |                   1 |
|------|---------------------|
| test |                   0 |
|------|---------------------|
```

</TabItem>
</Tabs>

Pour plus d'informations, consultez [https://github.com/nalgeon/sqlean/blob/main/docs/re.md](https://github.com/nalgeon/sqlean/blob/main/docs/re.md)
