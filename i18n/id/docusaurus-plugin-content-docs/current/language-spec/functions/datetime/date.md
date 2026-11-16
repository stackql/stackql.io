---
title: DATE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Fungsi DATE di StackQL: mengembalikan tanggal sebagai string YYYY-MM-DD dari nilai waktu dan pengubah opsional."
image: "/img/stackql-featured-image.png"
---
Mengembalikan string tanggal dalam format YYYY-MM-DD.  

Lihat juga:  
[[` SELECT `]](/language-spec/select) [[ Pengubah tanggal ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## Sintaksis {#syntax}

```sql
SELECT DATE(datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*datetime_expression*__  
Nilai datetime.

__*modifier*__  
Nol atau beberapa pengubah yang diterapkan pada *datetime_expression*.  Untuk informasi selengkapnya tentang pengubah, lihat [**Pengubah tanggal**](/language-spec/functions/datetime/date_modifiers)

## Nilai yang dikembalikan {#return-values}

Mengembalikan string yang merepresentasikan komponen tanggal dari input *datetime_expression* setelah *modifier* diterapkan.

* * *

## Contoh {#examples}

### Mengembalikan komponen tanggal dari nilai datetime {#return-the-date-component-of-a-datetime-value}

```sql
SELECT name, timeCreated,
date(timeCreated) as date_only
FROM google.storage.buckets WHERE project = 'stackql';
```

### Mengembalikan komponen tanggal lokal dari nilai datetime {#return-the-local-date-component-of-a-datetime-value}

```sql
SELECT name, timeCreated,
date(timeCreated, 'localtime') as local_date
FROM google.storage.buckets WHERE project = 'stackql';
```

Untuk informasi selengkapnya, lihat [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)
