---
title: REGEXP_SUBSTR
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Fungsi REGEXP_SUBSTR di StackQL: mengekstrak substring dari input yang cocok dengan ekspresi reguler."
image: "/img/stackql-featured-image.png"
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

Mengembalikan substring dari string input yang cocok dengan pola yang ditentukan.  

Lihat juga:  
[[` SELECT `]](/language-spec/select) [[ Referensi ekspresi reguler ]](/language-spec/functions/re/reference) 

* * * 

## Sintaksis {#syntax}

```sql
SELECT REGEXP_SUBSTR(source, pattern) FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*source*__  
String literal atau kolom string tempat Anda ingin mengekstrak substring berdasarkan pola yang ditentukan oleh ekspresi reguler (`pattern`).  

__*pattern*__  
Pola ekspresi reguler yang dicari.  

## Nilai yang dikembalikan {#return-values}
Mengembalikan string.

* * *

## Contoh {#examples}

### Mengekstrak substring berdasarkan ekspresi reguler {#extract-a-substring-based-upon-a-regular-expression}

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

Untuk informasi selengkapnya, lihat [https://github.com/nalgeon/sqlean/blob/main/docs/re.md](https://github.com/nalgeon/sqlean/blob/main/docs/re.md)
