---
title: JULIANDAY
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Fungsi JULIANDAY di StackQL: jumlah hari pecahan sejak tengah hari 24 November 4714 SM untuk nilai waktu, berguna untuk aritmetika tanggal."
image: "/img/stackql-featured-image.png"
---
Mengembalikan hari Julian - jumlah hari sejak tengah hari di Greenwich pada 24 November 4714 SM [(kalender Gregorian proleptik)](https://en.wikipedia.org/wiki/Proleptic_Gregorian_calendar).  

Lihat juga:  
[[` SELECT `]](/language-spec/select) [[ Pengubah tanggal ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## Sintaksis {#syntax}

```sql
SELECT JULIANDAY(datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*datetime_expression*__  
Nilai datetime.

__*modifier*__  
Nol atau beberapa pengubah yang diterapkan pada *datetime_expression*.  Untuk informasi selengkapnya tentang pengubah, lihat [**Pengubah tanggal**](/language-spec/functions/datetime/date_modifiers)

## Nilai yang dikembalikan {#return-values}

Mengembalikan [hari Julian](https://en.wikipedia.org/wiki/Julian_day) dari input *datetime_expression* setelah *modifier* diterapkan: jumlah hari, termasuk pecahan untuk waktu dalam hari tersebut, sejak tengah hari di Greenwich pada 24 November 4714 SM dalam [kalender Gregorian proleptik](https://en.wikipedia.org/wiki/Proleptic_Gregorian_calendar).

* * *

## Contoh {#examples}

### Menghitung jumlah hari di antara dua tanggal {#calculate-the-days-between-two-dates}

```sql
SELECT name, timeCreated,
round(julianday('now')-julianday(timeCreated)) as days_since
FROM google.storage.buckets WHERE project = 'stackql';
```

:::tip 

Gunakan fungsi [**ROUND**](/language-spec/functions/math/round) untuk mengonversi bilangan floating point ke bilangan bulat terdekat.

:::

Untuk informasi selengkapnya, lihat [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)
