---
title: Pengubah tanggal
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Pengubah tanggal dan waktu di StackQL: menggeser hasil DATE, TIME, DATETIME, JULIANDAY, dan STRFTIME per hari, bulan, atau tahun, ke awal periode, hari tertentu, dan lainnya."
image: "/img/stackql-featured-image.png"
---

## Ringkasan {#overview}

Dalam fungsi tanggal dan waktu StackQL, nilai tanggal atau waktu dapat diikuti oleh nol atau beberapa pengubah yang mengubah tanggal, waktu, atau keduanya. Setiap pengubah merupakan transformasi yang diterapkan pada nilai waktu di sebelah kirinya. Pengubah diterapkan secara berurutan dari kiri ke kanan. Pengubah yang tersedia adalah sebagai berikut:

| Pengubah             | Deskripsi                                                                       |
|----------------------|---------------------------------------------------------------------------------|
| NNN day(s)           | Menambahkan ±NNN hari ke tanggal dan waktu                                      |
| NNN hour(s)          | Menambahkan ±NNN jam ke tanggal dan waktu                                       |
| NNN minute(s)        | Menambahkan ±NNN menit ke tanggal dan waktu                                     |
| NNN[.NNNN] second(s) | Menambahkan ±NNN detik ke atau dari tanggal dan waktu                           |
| NNN month(s)         | Menambahkan ±NNN bulan ke tanggal dan waktu                                     |
| NNN year(s)          | Menambahkan ±NNN tahun ke tanggal dan waktu                                     |
| start of month       | Mundur ke awal bulan                                                            |
| start of year        | Mundur ke awal tahun                                                            |
| start of day         | Mundur ke awal hari                                                             |
| weekday N            | Memajukan tanggal ke tanggal berikutnya yang nomor hari dalam minggunya adalah N; tanggal yang sudah jatuh pada hari N dikembalikan tanpa perubahan |
| localtime            | Mengembalikan waktu lokal                                                       |
| utc                  | Mengembalikan waktu UTC                                                         |
| unixepoch            | Waktu Unix                                                                      |

## Contoh {#examples}

### Mengembalikan tanggal dan waktu saat ini menggunakan nilai waktu `now` {#return-the-current-date-and-time-using-the-now-time-value}

```sql
SELECT name, date('now') as date, time('now') as time
FROM google.storage.buckets WHERE project = 'stackql';
```

### Mengonversi UTC ke waktu lokal menggunakan pengubah `localtime` {#convert-utc-to-localtime-using-the-localtime-modifier}

```sql
SELECT name, timeCreated,
datetime(timeCreated, 'localtime') as local_time_created
FROM google.storage.buckets WHERE project = 'stackql';
```

### Mengonversi waktu lokal ke UTC {#convert-datetime-to-utc}

Pengubah `utc` memperlakukan inputnya sebagai waktu lokal di mesin yang menjalankan StackQL dan mengonversinya ke UTC.  Nilai yang sudah dalam UTC, seperti `'now'` atau stempel waktu dengan sufiks `Z`, dikembalikan tanpa perubahan.

```sql
SELECT datetime('2026-11-20 17:59:24', 'utc') as utc_time;
```

Di mesin dengan zona waktu UTC+11, kueri ini mengembalikan `2026-11-20 06:59:24`.

### Menghitung tanggal relatif terhadap tanggal lain {#calculate-a-date-relative-to-another-date}

```sql
SELECT name, timeCreated,
datetime(timeCreated, '+24 hours') as a_day_later
FROM google.storage.buckets WHERE project = 'stackql';
```

### Mendapatkan hari tertentu pada atau setelah tanggal yang diberikan {#get-the-next-specified-weekday-after-a-given-date}

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
