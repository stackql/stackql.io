---
title: Variabel sesi
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - variabel sesi
  - set
  - unset
  - identifier
description: "Variabel sesi di StackQL: menetapkan nilai skalar bertipe dengan SET, mendereferensinya dengan $name dalam predikat dan ekspresi, serta sebagai nama relasi dan kolom dengan IDENTIFIER."
image: "/img/stackql-featured-image.png"
---

Lihat juga:  
[[` SET `]](/language-spec/set) [[` UNSET `]](/language-spec/unset) [[ Sesi provider ]](/getting-started/provider-sessions) [[ Menggunakan variabel ]](/getting-started/variables)

Variabel sesi adalah nilai skalar yang disimpan oleh sesi saat ini dan dirujuk dari pernyataan berikutnya.  Variabel sesi tersedia mulai StackQL `v1.0`, dan berfungsi di shell interaktif, dalam skrip `exec`, melalui protokol komunikasi PostgreSQL, dan melalui server MCP.

Variabel sesi berbeda dari variabel template yang dijelaskan di [Menggunakan variabel](/getting-started/variables).  Variabel template diberikan ke perintah `stackql exec` melalui flag `--iqldata` dan `--var` dan dirender ke dalam teks skrip sebelum skrip tersebut diurai.  Variabel sesi di-resolve di dalam pernyataan yang telah diurai, mempertahankan tipenya, dan dapat diubah di antara pernyataan.

## Menetapkan dan menghapus variabel {#setting-and-removing-variables}

Pernyataan [`SET`](/language-spec/set) menetapkan string, angka, boolean, atau `null`.  Pernyataan [`UNSET`](/language-spec/unset) menghapus variabel; `SET name = DEFAULT` setara dengannya.  Nilai `null` yang eksplisit merupakan nilai tersimpan, bukan pengaturan ulang.

```sql
SET region = 'ap-southeast-2';
SET minimum_count = 3;
SET enabled = true;
SET optional_value = null;
UNSET region;
```

Array dan objek bukan nilai skalar.  Untuk menyimpan dokumen JSON, simpan sebagai string:

```sql
SET "providers.saved" = '{"provider":"aws","tenancy":"production"}';
```

## Mendereferensi variabel {#dereferencing-a-variable}

Gunakan `$name`, tanpa tanda kutip, di posisi ekspresi mana pun.  String di-escape sebagai literal SQL; angka, boolean, dan `null` mempertahankan tipenya.  Bentuk `'$name'` yang diapit tanda kutip tunggal adalah teks literal `$name`, bukan dereferensi.  Referensi ke variabel yang tidak ada akan gagal sebelum permintaan apa pun ke provider dibuat.

Nilai yang telah di-resolve dapat digunakan dalam ekspresi SQL biasa, termasuk `CAST`, `CONVERT`, penggabungan dengan `||`, dan pemanggilan fungsi:

```sql
SET some_id = 42;
SET suffix = 'East';

SELECT CAST($some_id AS TEXT) AS id_text,
       CONVERT($some_id, TEXT) || '-' || lower($suffix) AS label,
       md5(lower(CONVERT($some_id, TEXT) || '-' || $suffix)) AS digest,
       '$some_id' AS literal_text;
```

Penetapan hanya menerima literal skalar atau referensi langsung; terapkan konversi dan ekspresi lain di tempat variabel digunakan, bukan dalam pernyataan `SET`.

## Variabel dalam predikat {#variables-in-predicates}

Variabel dalam klausa `WHERE` di-resolve sebelum pernyataan direncanakan, sehingga variabel tersebut diteruskan ke API provider persis seperti literal.  Karena itu, parameter wajib dapat diberikan dari variabel:

```sql
SET project = 'stackql-demo';
SET zone = 'australia-southeast1-a';

SELECT name, status
FROM google.compute.instances
WHERE project = $project AND zone = $zone;
```

Nilai bertipe dibandingkan sesuai tipenya.  Klausa `WHERE active = $enabled` dengan `SET enabled = false` memfilter berdasarkan boolean, dan `WHERE id = CAST($some_id AS TEXT)` adalah bentuk yang digunakan jika kolom provider berupa string dan variabelnya berupa angka.

## Variabel sebagai nama objek {#variables-as-object-names}

Variabel dapat menamai relasi dan kolom, selain nilai.  Nama dikonversi menjadi pengidentifikasi dalam pernyataan yang telah diurai dan tidak pernah disisipkan ke dalam teks SQL, sehingga nilai yang berisi pernyataan, kosong, atau bukan string akan ditolak sebelum provider diakses.

| Posisi | Bentuk |
|--|--|
| Klausa `FROM` | `FROM $relation_name`, atau per komponen, `FROM $provider.$service.$resource` |
| Kolom dalam ekspresi | `IDENTIFIER($column_name)`, lihat [`IDENTIFIER`](/language-spec/functions/general/identifier) |
| Alias kolom atau tabel | `AS "$alias_name"` |
| `ORDER BY` | `ORDER BY IDENTIFIER($column_name)` |
| `LIMIT` | `LIMIT $row_count` |
| Nama kolom DDL, daftar kolom `INSERT`, target `UPDATE` | bentuk `"$name"` yang diapit tanda kutip ganda |
<br/>

```sql
SET relation_name = 'aws.s3.buckets';
SET column_name = 'name';
SET output_name = 'bucket';
SET row_count = 10;

SELECT IDENTIFIER($column_name) AS "$output_name"
FROM $relation_name
WHERE region = 'ap-southeast-2'
ORDER BY IDENTIFIER($column_name)
LIMIT $row_count;
```

Join, CTE, dan subkueri me-resolve variabel relasinya dengan cara yang sama.  Nilai relasi bertitik memiliki paling banyak empat komponen.  Variabel bukan fragmen SQL sembarang: kata kunci, daftar kolom yang dipisahkan koma, atau seluruh klausa `WHERE` tidak dapat disubstitusikan.

## Cakupan dan isolasi {#scope-and-isolation}

Variabel dimiliki oleh sesi yang menetapkannya.  Koneksi terpisah ke server `stackql srv` memiliki variabel terpisah, begitu pula pemanggilan `exec` yang terpisah.  Setiap permintaan MCP adalah sesi tersendiri, sehingga pernyataan `SET` dan kueri yang menggunakannya harus dikirim dalam batch pernyataan yang sama.  Sesi baru dimulai tanpa variabel.

Sesi adalah satu-satunya cakupan.  Pernyataan `SET SESSION` dan `UNSET SESSION` diterima dan memiliki arti yang persis sama dengan `SET` dan `UNSET`; kata kunci tersebut ada untuk skrip yang harus terbaca sama seperti di database yang membedakan pengaturan sesi dan pengaturan global.  Kata kunci `GLOBAL` ditolak karena tidak ada status yang dibagikan di antara sesi.

Pernyataan yang disiapkan me-resolve variabel saat dieksekusi, bukan saat disiapkan, dan rencana yang berisi nilai sesi terikat tidak digunakan ulang di antara eksekusi.

Jika [manajemen akses](/command-line-usage/access-management) diaktifkan, membaca variabel memerlukan `session/variables:read`, sedangkan menetapkan atau mengatur ulang variabel memerlukan `session/config:write`.  Me-resolve variabel menjadi nama objek tidak memberikan izin apa pun atas objek tersebut; relasi yang telah di-resolve diotorisasi sebelum eksekusi seperti biasa.
