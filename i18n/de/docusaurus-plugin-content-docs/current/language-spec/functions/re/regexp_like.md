---
title: REGEXP_LIKE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "REGEXP_LIKE-Funktion in StackQL: prüft, ob ein String einem regulären Ausdruck entspricht, und gibt true oder false zurück, für Musterfilter in WHERE-Klauseln."
image: "/img/stackql-featured-image.png"
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

Prüft, ob der Quellstring dem Muster entspricht, und gibt `true` oder `false` zurück.  

Siehe auch:  
[[` SELECT `]](/language-spec/select) [[ Referenz für reguläre Ausdrücke ]](/language-spec/functions/re/reference) 

* * * 

## Syntax {#syntax}

```sql
SELECT REGEXP_LIKE(source, pattern) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*source*__  
Ein String-Literal oder eine String-Spalte, die Sie auf das Vorhandensein eines durch einen regulären Ausdruck (`pattern`) definierten Musters prüfen möchten.  

__*pattern*__  
Das Muster des regulären Ausdrucks, nach dem gesucht wird.  

## Rückgabewert(e) {#return-values}
Gibt `0` zurück, wenn `false`, und `1`, wenn `true`.  

* * *

## Beispiele {#examples}

### Feststellen, ob ein String ein Muster enthält {#determine-if-a-string-contains-a-pattern}

<Tabs
  defaultValue="iql"
  values={[
    { label: 'StackQL', value: 'iql', },
    { label: 'Ergebnisse', value: 'results', },
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

Weitere Informationen finden Sie unter [https://github.com/nalgeon/sqlean/blob/main/docs/re.md](https://github.com/nalgeon/sqlean/blob/main/docs/re.md)
