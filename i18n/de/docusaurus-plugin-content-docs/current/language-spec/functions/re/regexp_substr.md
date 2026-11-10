---
title: REGEXP_SUBSTR
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "REGEXP_SUBSTR-Funktion in StackQL: extrahiert den Teilstring einer Eingabe, der einem regulären Ausdruck entspricht."
image: "/img/stackql-featured-image.png"
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

Gibt einen Teilstring aus einem Eingabestring zurück, der einem angegebenen Muster entspricht.  

Siehe auch:  
[[` SELECT `]](/language-spec/select) [[ Referenz für reguläre Ausdrücke ]](/language-spec/functions/re/reference) 

* * * 

## Syntax {#syntax}

```sql
SELECT REGEXP_SUBSTR(source, pattern) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*source*__  
Ein String-Literal oder eine String-Spalte, aus der Sie anhand eines durch einen regulären Ausdruck (`pattern`) definierten Musters einen Teilstring extrahieren möchten.  

__*pattern*__  
Das Muster des regulären Ausdrucks, nach dem gesucht wird.  

## Rückgabewert(e) {#return-values}
Gibt einen String zurück.

* * *

## Beispiele {#examples}

### Einen Teilstring anhand eines regulären Ausdrucks extrahieren {#extract-a-substring-based-upon-a-regular-expression}

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

Weitere Informationen finden Sie unter [https://github.com/nalgeon/sqlean/blob/main/docs/re.md](https://github.com/nalgeon/sqlean/blob/main/docs/re.md)
