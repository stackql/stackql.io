---
title: REGEXP_REPLACE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "REGEXP_REPLACE-Funktion in StackQL: ersetzt die Teile eines Strings, die einem regulären Ausdruck entsprechen, durch einen Ersetzungsstring."
image: "/img/stackql-featured-image.png"
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

Ersetzt einen passenden Teilstring durch den Ersetzungsstring.  

Siehe auch:  
[[` SELECT `]](/language-spec/select) [[ Referenz für reguläre Ausdrücke ]](/language-spec/functions/re/reference) 

* * * 

## Syntax {#syntax}

```sql
SELECT REGEXP_REPLACE(source, pattern, replacement) FROM <multipartIdentifier>;
```
## Argumente {#arguments}

__*source*__  
Ein String-Literal oder eine String-Spalte, aus der Sie anhand eines durch einen regulären Ausdruck (`pattern`) definierten Musters einen Teilstring extrahieren möchten.  

__*pattern*__  
Das Muster des regulären Ausdrucks, nach dem gesucht wird.  

__*replacement*__  
Der Ersetzungsstring.

> Unterstützt Rückverweise auf erfasste Gruppen `\1` bis `\9` im Ersetzungsstring.  

## Rückgabewert(e) {#return-values}
Gibt einen String zurück.

* * *

## Beispiele {#examples}

### Ein Muster eines regulären Ausdrucks in einer Spalte ersetzen {#replace-a-regular-expression-pattern-in-a-column}

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

Weitere Informationen finden Sie unter [https://github.com/nalgeon/sqlean/blob/main/docs/re.md](https://github.com/nalgeon/sqlean/blob/main/docs/re.md)
