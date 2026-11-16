---
title: CHAR
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Fungsi CHAR di StackQL: karakter untuk satu atau lebih kode karakter, untuk menyisipkan baris baru, tanda kutip, tab, dan karakter kontrol lainnya ke dalam string."
image: "/img/stackql-featured-image.png"
---

Fungsi `CHAR()` mengembalikan string yang tersusun dari karakter untuk satu atau lebih kode karakter.

Lihat juga:  
[[`SELECT`]](/language-spec/select)

* * *

:::note
Saat menggunakan StackQL dengan backend PostgreSQL, fungsi yang setara adalah `CHR()`, yang menerima satu kode saja.
:::

## Sintaksis {#syntax}

```sql
SELECT CHAR(ascii_code [, ascii_code, ...]) FROM <multipartIdentifier>;
```

> Setiap argumen menambahkan satu karakter ke hasil, sehingga `CHAR(72, 105)` mengembalikan `Hi`.

## Argumen {#arguments}

**_ascii_code_**  
Satu atau lebih bilangan bulat, masing-masing berupa kode sebuah karakter: kode ASCII, atau titik kode Unicode apa pun.

## Nilai yang dikembalikan {#return-values}

Mengembalikan string dengan satu karakter untuk setiap argumen *ascii_code*.

* * *

## Kode ASCII umum {#common-ascii-codes}

| Karakter      | Deskripsi             | Kode ASCII | Contoh penggunaan               |
|---------------|-----------------------|------------|---------------------------------|
| `\n`          | Baris baru            | 10         | `CHAR(10)`                      |
| `'`           | Tanda kutip tunggal   | 39         | `CHAR(39)`                      |
| `"`           | Tanda kutip ganda     | 34         | `CHAR(34)`                      |
| (spasi)       | Spasi                 | 32         | `CHAR(32)`                      |
| `:`           | Titik dua             | 58         | `CHAR(58)`                      |
| `;`           | Titik koma            | 59         | `CHAR(59)`                      |
| `\t`          | Tab horizontal        | 9          | `CHAR(9)`                       |
| `\r`          | Carriage return       | 13         | `CHAR(13)`                      |
| `\b`          | Backspace             | 8          | `CHAR(8)`                       |
| `\f`          | Form feed             | 12         | `CHAR(12)`                      |

* * *

## Contoh {#examples}

### Menggabungkan string dengan karakter khusus {#concatenate-strings-with-special-characters}

Untuk menggabungkan string dengan karakter baris baru di antaranya:

```sql
SELECT 
'/* ' || displayName || ' */' || CHAR(10) || 'DELETE FROM databricks_account.iam.account_users WHERE account_id = ' || CHAR(39) || account_id || CHAR(39) || ' AND id = ' || CHAR(39) || id || CHAR(39)
FROM
databricks_account.iam.account_users
where account_id = 'abcd1234-1234-5678-90ab-abcdef123456'
AND active = false;
```

**Hasil** (untuk pengguna dengan nama tampilan `Jane Doe` dan ID `1234567890123456`):

```
/* Jane Doe */
DELETE FROM databricks_account.iam.account_users WHERE account_id = 'abcd1234-1234-5678-90ab-abcdef123456' AND id = '1234567890123456'
```

* * *
