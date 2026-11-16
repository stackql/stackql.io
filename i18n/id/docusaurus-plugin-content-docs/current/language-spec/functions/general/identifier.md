---
title: IDENTIFIER
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - variabel sesi
  - pengidentifikasi
description: "Fungsi IDENTIFIER di StackQL: mengubah variabel sesi berisi nama kolom menjadi referensi kolom agar variabel dapat menamai kolom di proyeksi dan ORDER BY."
image: "/img/stackql-featured-image.png"
---
Mengubah nilai string, biasanya variabel sesi, menjadi nama kolom.  Gunakan fungsi ini jika `$variable` polos di posisi tersebut akan dibaca sebagai nilai skalar.  Tersedia mulai StackQL `v1.0`.

Lihat juga:  
[[` SET `]](/language-spec/set) [[ Variabel sesi ]](/getting-started/session-variables)

* * * 

## Sintaksis {#syntax}

```sql
SELECT IDENTIFIER($variable) FROM <multipartIdentifier> ORDER BY IDENTIFIER($variable);
```

## Argumen {#arguments}

__*variable*__  
Variabel sesi, atau literal string, yang berisi nama kolom.  Nama yang dikualifikasi seperti `t.name` diterima.  Fungsi ini menerima tepat satu argumen, dan argumen tersebut harus berupa satu nilai string; penggabungan seperti `'t.' || $column_name`, argumen kedua, atau nilai numerik ditolak dengan `IDENTIFIER requires one string value`.  Nilai kosong, atau nilai yang berisi pernyataan, ditolak dengan `invalid identifier value`, dan variabel yang tidak terdefinisi gagal sebagai tidak terdefinisi.  Semua pemeriksaan ini dilakukan sebelum permintaan apa pun dikirim ke provider.

## Nilai yang dikembalikan {#return-values}

Dalam pernyataan yang telah diurai, fungsi ini diganti dengan referensi ke kolom yang dinamai, sehingga fungsi ini mengembalikan nilai kolom tersebut.  Nama dikonversi menjadi pengidentifikasi dan tidak pernah disisipkan ke dalam teks SQL.  Referensi kolom yang dihasilkan dapat digunakan di mana pun kolom dapat digunakan: daftar select, klausa `WHERE`, `GROUP BY`, dan `ORDER BY`, argumen fungsi, serta di dalam CTE dan subkueri.

* * *

## Contoh {#examples}

### Memproyeksikan dan mengurutkan menurut kolom yang namanya ada dalam variabel {#project-and-order-by-a-column-named-in-a-variable}

Kolom yang dikembalikan dan kolom yang menjadi dasar pengurutan sama-sama diambil dari `column_name`, dan kolom output diberi nama baru dari `output_name`.  `IDENTIFIER` diperlukan di daftar select dan di `ORDER BY` karena `$column_name` polos di posisi tersebut akan menjadi string `'name'`, bukan kolom tersebut.  Alias menggunakan bentuk dengan tanda kutip ganda sebagai gantinya.  Setelah variabel diganti dengan nilainya, pernyataan tersebut sama dengan `SELECT name AS bucket FROM aws.s3.buckets WHERE region = 'ap-southeast-2' ORDER BY name`.

```sql
SET column_name = 'name';
SET output_name = 'bucket';

SELECT IDENTIFIER($column_name) AS "$output_name"
FROM aws.s3.buckets
WHERE region = 'ap-southeast-2'
ORDER BY IDENTIFIER($column_name);
```

### Memfilter menurut kolom yang namanya ada dalam variabel {#filter-on-a-column-named-in-a-variable}

Referensi yang dihasilkan berfungsi dalam predikat maupun proyeksi.  Di sini, field yang difilter dan nilai yang dicocokkan sama-sama berasal dari variabel: `IDENTIFIER($filter_column)` menjadi kolom `status`, sedangkan `$filter_value` diteruskan sebagai literal string `'RUNNING'`.  Kedua bentuk ini sengaja dibuat berbeda, karena bentuk pertama menamai kolom dan bentuk kedua memberikan nilai.

```sql
SET filter_column = 'status';
SET filter_value = 'RUNNING';

SELECT name, zone
FROM google.compute.instances
WHERE project = 'stackql-demo'
AND zone = 'australia-southeast1-a'
AND IDENTIFIER($filter_column) = $filter_value;
```

### Mengualifikasi kolom dengan alias tabel {#qualify-the-column-with-a-table-alias}

Nilai yang mengandung titik menghasilkan referensi kolom yang dikualifikasi.  Tabel diberi alias `w`, dengan alias itu sendiri diambil dari variabel, dan `IDENTIFIER($column_name)` menjadi `w.name`.  Bentuk ini yang perlu digunakan jika nama kolom yang sama ada di kedua sisi join dan referensi tersebut akan ambigu jika tidak dikualifikasi.

```sql
SET table_alias = 'w';
SET column_name = 'w.name';

SELECT IDENTIFIER($column_name) AS bucket
FROM aws.s3.buckets AS "$table_alias"
WHERE w.region = 'ap-southeast-2';
```

### Nilai yang ditolak {#rejected-values}

Karena nilai tersebut menjadi nama, bukan teks kueri, apa pun yang bukan nama yang dapat digunakan akan ditolak sebelum pernyataan direncanakan, dan tidak ada permintaan yang sampai ke provider.  Dua pernyataan pertama di bawah gagal dengan `invalid identifier value` dan pernyataan ketiga dengan `IDENTIFIER requires one string value`.

```sql
/*
SET column_name = '';
SELECT IDENTIFIER($column_name) FROM aws.s3.buckets;
-- FAILS with invalid identifier value

SET column_name = 'name; DROP TABLE imaginary';
SELECT IDENTIFIER($column_name) FROM aws.s3.buckets;
-- FAILS with invalid identifier value

SET column_name = 17;
SELECT IDENTIFIER($column_name) FROM aws.s3.buckets;
-- FAILS with IDENTIFIER requires one string value
*/
```

> `$variable` di klausa `FROM` menjadi relasi tanpa `IDENTIFIER`, dan `"$variable"` dengan tanda kutip ganda digunakan untuk alias, nama kolom DDL, daftar kolom `INSERT`, dan target `UPDATE`.
