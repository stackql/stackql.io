---
title: REGEXP_SUBSTR
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Fonction REGEXP_SUBSTR dans StackQL : extraire la sous-chaîne d'une entrée qui correspond à une expression régulière."
image: "/img/stackql-featured-image.png"
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

Renvoie la sous-chaîne d'une chaîne de caractères d'entrée qui correspond à un motif spécifié.  

Voir aussi :  
[[` SELECT `]](/language-spec/select) [[ Référence des expressions régulières ]](/language-spec/functions/re/reference) 

* * * 

## Syntaxe {#syntax}

```sql
SELECT REGEXP_SUBSTR(source, pattern) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*source*__  
Une chaîne littérale ou une colonne de type chaîne dont vous souhaitez extraire une sous-chaîne d'après un motif défini par une expression régulière (`pattern`).  

__*pattern*__  
Le motif d'expression régulière à rechercher.  

## Valeur(s) renvoyée(s) {#return-values}
Renvoie une chaîne.

* * *

## Exemples {#examples}

### Extraire une sous-chaîne d'après une expression régulière {#extract-a-substring-based-upon-a-regular-expression}

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
json_extract(properties, '$.storageProfile.imageReference.offer') as os,
regexp_substr(
  json_extract(properties, '$.storageProfile.imageReference.exactVersion'), 
  '[0-9][0-9]\.[0-9][0-9]') as os_version 
from azure.compute.virtual_machines 
WHERE subscription_id = '273769f6-545f-45b2-8ab8-2f14ec5768dc';
```

</TabItem>
<TabItem value="results">

```
|------|---------------|------------|
| name |      os       | os_version |
|------|---------------|------------|
| vm0  | UbuntuServer  |      16.04 |
|------|---------------|------------|
| vm1  | UbuntuServer  |      16.04 |
|------|---------------|------------|
| vm2  | UbuntuServer  |      20.04 |
|------|---------------|------------|
```

</TabItem>
</Tabs>

Pour plus d'informations, consultez [https://github.com/nalgeon/sqlean/blob/main/docs/re.md](https://github.com/nalgeon/sqlean/blob/main/docs/re.md)
