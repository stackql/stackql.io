---
title: MD5
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - md5
  - hash
  - digest
description: "Fungsi MD5 di StackQL: mengembalikan hash MD5 heksadesimal huruf kecil dari suatu string, untuk pembuatan sidik jari dan deteksi perubahan, bukan untuk keamanan."
image: "/img/stackql-featured-image.png"
---
Mengembalikan hash MD5 dari suatu string sebagai 32 karakter heksadesimal huruf kecil.  Tersedia mulai StackQL `v1.0`.

Lihat juga:  
[[` SELECT `]](/language-spec/select) [[ Penggabungan ]](/language-spec/functions/string/concatenation)

* * * 

## Sintaksis {#syntax}

```sql
SELECT MD5(expression) FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*expression*__  
String, atau nilai yang dikonversi secara implisit menjadi teks.  Argumen `NULL` mengembalikan `NULL`.

## Nilai yang dikembalikan {#return-values}

Mengembalikan string heksadesimal huruf kecil sepanjang 32 karakter.

> `MD5` disediakan untuk pembuatan sidik jari dan deteksi perubahan.  Fungsi ini bukan primitif untuk kata sandi atau keamanan.  Di backend PostgreSQL, yang digunakan adalah fungsi `md5` milik database itu sendiri.

* * *

## Contoh {#examples}

### Membuat sidik jari dari nilai turunan {#fingerprint-a-derived-value}

Variabel disubstitusikan sebagai nilai bertipe, bilangan bulat dikonversi menjadi teks dan digabungkan dengan sufiks, lalu hash dihitung dari hasil yang telah diubah menjadi huruf kecil.  Input yang sama selalu menghasilkan string heksadesimal 32 karakter yang sama, sehingga nilai tersebut dapat digunakan sebagai kunci yang stabil.

```sql
SET some_id = 42;
SET suffix = 'East';

SELECT md5(lower(CONVERT($some_id, TEXT) || '-' || $suffix)) AS digest;
```

### Mendeteksi perubahan konfigurasi {#detect-configuration-changes}

Menghitung hash dari kolom JSON yang diserialisasi menghasilkan nilai pendek untuk disimpan dan dibandingkan dari satu eksekusi ke eksekusi berikutnya.  Bucket dengan hash yang berbeda dari eksekusi terakhir memiliki konfigurasi IAM yang berubah, tanpa perlu menyimpan atau membandingkan seluruh dokumen.

```sql
SELECT name, md5(json_extract(iamConfiguration, '$')) AS iam_digest
FROM google.storage.buckets
WHERE project = 'stackql-demo';
```
