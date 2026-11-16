---
title: REGEXP_LIKE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Fungsi REGEXP_LIKE di StackQL: menguji apakah string cocok dengan ekspresi reguler dan mengembalikan true atau false, untuk filter pola dalam klausa WHERE."
image: "/img/stackql-featured-image.png"
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

Memeriksa apakah string sumber cocok dengan pola dan mengembalikan `true` atau `false`.  

Lihat juga:  
[[` SELECT `]](/language-spec/select) [[ Referensi ekspresi reguler ]](/language-spec/functions/re/reference) 

* * * 

## Sintaksis {#syntax}

```sql
SELECT REGEXP_LIKE(source, pattern) FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*source*__  
String literal atau kolom string yang ingin Anda uji untuk mengetahui keberadaan pola yang ditentukan oleh ekspresi reguler (`pattern`).  

__*pattern*__  
Pola ekspresi reguler yang dicari.  

## Nilai yang dikembalikan {#return-values}
Mengembalikan `0` jika `false` dan `1` jika `true`.  

* * *

## Contoh {#examples}

### Menentukan apakah string berisi pola {#determine-if-a-string-contains-a-pattern}

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

Untuk informasi selengkapnya, lihat [https://github.com/nalgeon/sqlean/blob/main/docs/re.md](https://github.com/nalgeon/sqlean/blob/main/docs/re.md)
