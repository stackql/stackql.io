---
title: TIME
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Fungsi TIME di StackQL: mengembalikan waktu sebagai string HH:MM:SS dari nilai waktu dan pengubah opsional."
image: "/img/stackql-featured-image.png"
---
Mengembalikan string waktu dalam format HH:MM:SS.  

Lihat juga:  
[[` SELECT `]](/language-spec/select) [[ Pengubah tanggal ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## Sintaksis {#syntax}

```sql
SELECT TIME(datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*datetime_expression*__  
Nilai datetime.

__*modifier*__  
Nol atau beberapa pengubah yang diterapkan pada *datetime_expression*.  Untuk informasi selengkapnya tentang pengubah, lihat [**Pengubah tanggal**](/language-spec/functions/datetime/date_modifiers)

## Nilai yang dikembalikan {#return-values}

Mengembalikan string yang merepresentasikan komponen waktu dari input *datetime_expression* setelah *modifier* diterapkan.

* * *

## Contoh {#examples}

### Mengembalikan komponen waktu dari nilai datetime {#return-the-time-component-of-a-datetime-value}

```sql
SELECT name, timeCreated,
time(timeCreated) as time_only
FROM google.storage.buckets WHERE project = 'stackql';
```

### Mengembalikan komponen waktu lokal dari nilai datetime {#return-the-local-time-component-of-a-datetime-value}

```sql
SELECT name, timeCreated,
time(timeCreated, 'localtime') as local_time
FROM google.storage.buckets WHERE project = 'stackql';
```

Untuk informasi selengkapnya, lihat [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)
