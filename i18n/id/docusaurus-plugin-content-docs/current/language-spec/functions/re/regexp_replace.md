---
title: REGEXP_REPLACE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Fungsi REGEXP_REPLACE di StackQL: mengganti bagian string yang cocok dengan ekspresi reguler dengan string pengganti."
image: "/img/stackql-featured-image.png"
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

Mengganti substring yang cocok dengan string pengganti.  

Lihat juga:  
[[` SELECT `]](/language-spec/select) [[ Referensi ekspresi reguler ]](/language-spec/functions/re/reference) 

* * * 

## Sintaksis {#syntax}

```sql
SELECT REGEXP_REPLACE(source, pattern, replacement) FROM <multipartIdentifier>;
```
## Argumen {#arguments}

__*source*__  
String literal atau kolom string tempat kecocokan ekspresi reguler `pattern` diganti dengan `replacement`.  

__*pattern*__  
Pola ekspresi reguler yang dicari.  

__*replacement*__  
String pengganti.

> Mendukung referensi balik ke grup tangkapan `\1` hingga `\9` dalam string pengganti.  

## Nilai yang dikembalikan {#return-values}
Mengembalikan string.

* * *

## Contoh {#examples}

### Mengganti pola ekspresi reguler dalam kolom {#replace-a-regular-expression-pattern-in-a-column}

<Tabs
  defaultValue="iql"
  values={[
    { label: 'StackQL', value: 'iql', },
    { label: 'Hasil', value: 'results', },
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

Untuk informasi selengkapnya, lihat [https://github.com/nalgeon/sqlean/blob/main/docs/re.md](https://github.com/nalgeon/sqlean/blob/main/docs/re.md)
