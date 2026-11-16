---
title: Tipe data
hide_title: false
hide_table_of_contents: true
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Tipe data StackQL: tipe yang digunakan untuk properti sumber daya dan parameter dalam kueri dan hasil, serta cara pemetaannya ke skema API provider."
image: "/img/stackql-featured-image.png"
---

Tipe data StackQL digunakan untuk merepresentasikan nilai field input atau output (properti atau parameter) yang digunakan dalam sumber daya cloud. Tipe data StackQL dicantumkan dalam tabel di bawah ini:

| Tipe StackQL | Deskripsi | Contoh |
|--|--|--|
| `string` | urutan karakter Unicode yang diapit tanda kutip tunggal | `'mybucket'` |
| `array` | kumpulan nilai yang terurut, diapit tanda kurung siku `[ ]`, dipisahkan dengan koma | `[0,1,2]` |
| `object` | himpunan tidak terurut berisi nol atau lebih pasangan nama/nilai yang disisipkan di antara `{ }` dan dipisahkan dengan koma | `{"name": "mydb"}` |
| `boolean` | `true` atau `false`, tidak diapit tanda kutip (nilai yang diberi tanda kutip seperti `'true'` diperlakukan sebagai string) | `true` |
| `integer` | bilangan bulat yang dapat bernilai positif, negatif, atau nol | `2` |
| `double` | bilangan floating point presisi ganda, dapat bernilai positif atau negatif | `0.95` |
<br/>
<br/>
> Tipe data StackQL dipetakan ke tipe data yang diekspos dalam API semua penyedia cloud utama.

## Konversi tipe {#type-conversion}

`CAST(expression AS type)` dan `CONVERT(expression, type)` mengonversi nilai ke tipe lain. Selain tipe konversi yang diwarisi dari tata bahasa MySQL (`CHAR`, `DATE`, `DATETIME`, `DECIMAL`, `SIGNED`, `UNSIGNED`, `TIME`, `JSON`), StackQL mulai `v1.0` menerima `TEXT`, `INTEGER`, `INT`, `BIGINT`, `BOOLEAN`, dan `BOOL`. Konversi dilakukan oleh backend SQL, sehingga semantiknya mengikuti SQLite atau PostgreSQL. Konversi umumnya digunakan untuk membandingkan [variabel sesi](/getting-started/session-variables) yang memiliki tipe dengan kolom string:

```sql
SET some_id = 42;

SELECT CAST($some_id AS TEXT) AS id_text,
       CONVERT($some_id, TEXT) || '-east' AS label;
```

Field boolean yang disajikan oleh provider XML seperti AWS dilaporkan sebagai `true` dan `false`; rilis StackQL sebelum `v1.0` melaporkannya sebagai `0` dan `1`.
