---
title: DATETIME
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Fungsi DATETIME di StackQL: menghitung nilai datetime dari input dan satu atau beberapa pengubah, dikembalikan sebagai YYYY-MM-DD HH:MM:SS."
image: "/img/stackql-featured-image.png"
---
Mengembalikan nilai datetime hasil perhitungan dari input datetime dan satu atau beberapa pengubah.  

Lihat juga:  
[[` SELECT `]](/language-spec/select) [[ Pengubah tanggal ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## Sintaksis {#syntax}

```sql
SELECT DATETIME(datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*datetime_expression*__  
Nilai datetime.

__*modifier*__  
Nol atau beberapa pengubah yang diterapkan pada *datetime_expression*.  Untuk informasi selengkapnya tentang pengubah, lihat [**Pengubah tanggal**](/language-spec/functions/datetime/date_modifiers)

## Nilai yang dikembalikan {#return-values}

Mengembalikan string yang merepresentasikan datetime dari input *datetime_expression* setelah *modifier* diterapkan.

* * *

## Contoh {#examples}

### Mengonversi UTC ke waktu lokal menggunakan pengubah `localtime` {#convert-utc-to-localtime-using-the-localtime-modifier}

```sql
SELECT name, timeCreated,
datetime(timeCreated, 'localtime') as local_time_created
FROM google.storage.buckets WHERE project = 'stackql';
```

### Mengonversi datetime ke UTC {#convert-datetime-to-utc}

```sql
SELECT name, timeCreated,
datetime('now', 'utc') as now_utc
FROM google.storage.buckets WHERE project = 'stackql';
```

### Menghitung tanggal relatif terhadap tanggal lain {#calculate-a-date-relative-to-another-date}

```sql
SELECT name, timeCreated,
datetime(timeCreated, '+24 hours') as a_day_later
FROM google.storage.buckets WHERE project = 'stackql';
```

### Mendapatkan hari tertentu berikutnya setelah tanggal yang diberikan {#get-the-next-specified-weekday-after-a-given-date}

```sql
SELECT name, timeCreated,
datetime(timeCreated, 'weekday 0') as following_sunday
FROM google.storage.buckets WHERE project = 'stackql';
```

### Mendapatkan awal bulan untuk tanggal yang diberikan {#get-the-start-of-the-month-for-a-given-date}

```sql
SELECT name, timeCreated,
datetime(timeCreated, 'start of month') as start_of_month
FROM google.storage.buckets WHERE project = 'stackql';
```

### Mengonversi stempel waktu Unix ke datetime {#convert-a-unix-timestamp-to-a-datetime}

```sql
SELECT datetime(1576417943, 'unixepoch') as date_from_ts
FROM google.storage.buckets WHERE project = 'stackql';
```

:::tip 

Untuk mengonversi datetime ke stempel waktu epoch Unix, lihat [**STRFTIME**](/language-spec/functions/datetime/strftime).  Untuk menghitung selisih antara dua objek datetime, lihat [**STRFTIME**](/language-spec/functions/datetime/strftime) atau [**JULIANDAY**](/language-spec/functions/datetime/julianday).

:::

Untuk informasi selengkapnya, lihat [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)
