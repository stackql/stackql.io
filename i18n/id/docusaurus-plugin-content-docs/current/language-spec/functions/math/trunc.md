---
title: TRUNC
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Fungsi TRUNC di StackQL: bagian bilangan bulat dari suatu bilangan, dengan pembulatan ke arah nol dan membuang bagian pecahan."
image: "/img/stackql-featured-image.png"
---

Mengembalikan bagian bilangan bulat dari suatu bilangan dengan membulatkan ke arah nol, sehingga komponen pecahan apa pun dibuang.

Lihat juga:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Sintaksis {#syntax}

```sql
SELECT TRUNC(numeric_expression) FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*numeric_expression*__  
Ekspresi dari kategori tipe data numerik eksak atau numerik perkiraan.

## Nilai yang dikembalikan {#return-values}
Mengembalikan nilai numerik yang dipotong menjadi komponen bilangan bulatnya.

* * *

## Contoh {#examples}

### Memotong jumlah hari sejak pembuatan bucket Google Cloud Storage menjadi hari penuh {#truncate-the-number-of-days-since-creation-of-google-cloud-storage-buckets-to-whole-days}

```sql
SELECT name, timeCreated,
TRUNC(julianday('now') - julianday(timeCreated)) as days_since
FROM google.storage.buckets WHERE project = 'stackql';
```
