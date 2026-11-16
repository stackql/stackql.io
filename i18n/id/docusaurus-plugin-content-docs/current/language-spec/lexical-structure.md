---
title: Struktur leksikal
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Struktur leksikal StackQL: cara parser memecah pernyataan, pengidentifikasi, tanda kutip, literal, komentar, dan spasi kosong menjadi token leksikal."
image: "/img/stackql-featured-image.png"
---

Lihat juga:  
[[ Hierarki sumber daya StackQL ]](/getting-started/resource-hierarchy) [[ Kata kunci ]](/language-spec/keywords) [[ Tipe data ]](/language-spec/data-types) [[ DESCRIBE ]](/language-spec/describe)


Struktur leksikal dan tata bahasa StackQL dimodelkan berdasarkan implementasi bahasa MySQL (adaptasi dari standar ANSI SQL-92) dengan beberapa ekstensi.  Pernyataan StackQL digunakan untuk mengkueri sumber daya cloud (menggunakan tata bahasa `SELECT`) atau untuk membuat, mengubah, atau menghapus infrastruktur menggunakan pernyataan bahasa manipulasi data (DML) standar seperti `INSERT`, `UPDATE`, `REPLACE`, dan `DELETE`.  Selain itu, operasi non-DML, misalnya memulai instance, atau mengambil objek dari bucket di AWS S3 atau menaruh objek ke dalamnya, diimplementasikan sebagai prosedur bawaan provider (mirip dengan prosedur tersimpan di DBMS).  Prosedur bawaan provider dipanggil menggunakan pernyataan `EXEC`.

Bahasa SQL khusus domain dari StackQL memungkinkan pemrograman infrastruktur secara deklaratif maupun prosedural.

* * * 

## Sintaksis {#syntax}
Operasi StackQL terdiri atas satu atau beberapa pernyataan yang diakhiri titik koma (`;`).  Pernyataan mencakup kata kunci, pengidentifikasi, parameter, dan nilai; contoh pernyataan StackQL untuk membuat disk 10GB di GCP adalah sebagai berikut:

```sql
INSERT INTO google.compute.disks (project, zone, name, sizeGb)
SELECT 'myProject', 'australia-southeast1-a', 'my10GbDisk', 10;
```

Pada contoh di atas, _`INSERT`_, _`INTO`_, dan _`SELECT`_ adalah kata kunci, _google.compute.disks_ adalah pengidentifikasi, _project_, _zone_, _name_, _sizeGb_  adalah parameter, dan _'myProject'_, _'australia-southeast1-a'_, _'my10GbDisk'_, _10_ adalah nilai.

## Kata kunci {#keywords}
Kata kunci tidak peka huruf besar-kecil, artinya `INSERT` dan `insert` diperlakukan sama, tetapi kata kunci sering ditulis dengan huruf kapital sesuai konvensi. Daftar lengkap kata kunci dapat ditemukan di bagian Kata kunci.

## Pengidentifikasi {#identifiers}
Pengidentifikasi adalah simbol yang merepresentasikan objek dalam hierarki sumber daya penyedia cloud.  Pengidentifikasi terdiri atas beberapa bagian; informasi selengkapnya tentang pengidentifikasi dapat ditemukan di topik Hierarki sumber daya StackQL.

## Parameter dan nilai {#parameters-and-values}
Parameter ditentukan oleh sumber daya; parameter ini dapat ditemukan dengan menggunakan pernyataan `DESCRIBE` untuk sumber daya penyedia cloud tertentu.  Nilai adalah objek skalar atau kompleks bertipe yang dipetakan ke parameter.  Untuk informasi selengkapnya tentang parameter dan nilai, lihat topik `DESCRIBE` dan Tipe data.

## Variabel {#variables}
Pengidentifikasi berawalan tanda dolar seperti `$region` mendereferensi variabel sesi yang ditetapkan dengan [`SET`](/language-spec/set); pengidentifikasi tersebut diganti dengan nilai bertipe dari variabel itu sebelum pernyataan direncanakan.  `'$region'` yang diapit tanda kutip tunggal adalah literal string.  Dalam klausa `FROM`, `$variable` dievaluasi menjadi nama relasi, dan `IDENTIFIER($variable)` dievaluasi menjadi nama kolom di posisi ekspresi.  Jalur berawalan tanda dolar yang diapit tanda kutip ganda, seperti `"$.auth.aws.password"`, adalah target konfigurasi runtime yang diterima oleh `SET` dan `UNSET`.  Variabel sesi tersedia mulai StackQL `v1.0`; lihat [Variabel sesi](/getting-started/session-variables).

## Operator {#operators}
`||` adalah operator penggabungan string dan memiliki prioritas lebih tinggi daripada operator perbandingan.  `OR` adalah disjungsi boolean.  Rilis StackQL sebelum `v1.0` mengenali `||` sebagai token `OR`.

## Komentar {#comments}
StackQL mendukung komentar multibaris dan komentar sebaris di dalam pernyataan StackQL; contoh komentar ditunjukkan di bawah ini:

```sql
/* This
Is a
Multiline
Comment
*/

--This is an inline comment
```
