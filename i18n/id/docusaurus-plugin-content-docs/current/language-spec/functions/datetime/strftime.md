---
title: STRFTIME
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Fungsi STRFTIME di StackQL: memformat nilai datetime dengan string format, menggunakan substitusi yang sama dengan fungsi strftime di C."
image: "/img/stackql-featured-image.png"
---
Mengembalikan nilai datetime yang diformat berdasarkan format yang ditentukan.  

Lihat juga:  
[[` SELECT `]](/language-spec/select) [[ Pengubah tanggal ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## Sintaksis {#syntax}

```sql
SELECT STRFTIME(format, datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*format*__  
Format yang diinginkan untuk nilai datetime yang ditentukan oleh nilai *datetime_expression*.  Lihat tabel format di bawah.

__*datetime_expression*__  
Nilai tanggal atau waktu yang akan diformat.

__*modifier*__  
Nol atau beberapa pengubah yang diterapkan pada *datetime_expression*.  Untuk informasi selengkapnya tentang pengubah, lihat [**Pengubah tanggal**](/language-spec/functions/datetime/date_modifiers)

### Format {#formats}

| Format | Deskripsi                                |
|--------|------------------------------------------|
| %d     | hari dalam bulan: 01-31                  |
| %f     | pecahan detik: SS.SSS                    |
| %H     | jam: 00-24                               |
| %j     | hari dalam tahun: 001-366                |
| %J     | nomor hari Julian                        |
| %m     | bulan: 01-12                             |
| %M     | menit: 00-59                             |
| %s     | detik sejak 1970-01-01                   |
| %S     | detik: 00-59                             |
| %w     | hari dalam seminggu 0-6 dengan Minggu==0 |
| %W     | minggu dalam tahun: 00-53                |
| %Y     | tahun: 0000-9999                         |
| %%     | meng-escape simbol persen: %             |

## Nilai yang dikembalikan {#return-values}

Mengembalikan string terformat yang merepresentasikan format datetime yang diinginkan.

* * *

## Contoh {#examples}

### Mengembalikan tanggal dalam format AS {#return-a-date-in-us-format}

```sql
SELECT name, strftime('%m/%d/%Y', creationTimestamp) as created_at
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Mengonversi datetime ke stempel waktu epoch {#convert-a-datetime-to-an-epoch-timestamp}

```sql
SELECT name, strftime('%s', creationTimestamp) as created_at
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Menghitung jumlah hari di antara dua tanggal {#calculate-the-days-between-two-dates}

```sql
SELECT name, (strftime('%s','now')-strftime('%s', creationTimestamp))/86400 as days_since_creation
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Untuk informasi selengkapnya, lihat [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)
