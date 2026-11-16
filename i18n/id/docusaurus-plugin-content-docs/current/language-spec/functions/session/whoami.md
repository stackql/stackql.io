---
title: WHOAMI
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - manajemen akses
  - whoami
  - prinsipal
description: "Fungsi WHOAMI di StackQL: mengembalikan ID prinsipal pemanggil yang terautentikasi saat manajemen akses diaktifkan."
image: "/img/stackql-featured-image.png"
---
Mengembalikan ID prinsipal pemanggil yang terautentikasi.  Memerlukan sesi yang dimulai dengan [manajemen akses](/command-line-usage/access-management) yang diaktifkan.  Tersedia mulai StackQL `v1.0`.

Lihat juga:  
[[` GROUPS `]](/language-spec/functions/session/groups) [[ Manajemen akses ]](/command-line-usage/access-management)

* * * 

## Sintaksis {#syntax}

```sql
SELECT WHOAMI() [ AS <alias> ];
```

## Argumen {#arguments}

Tidak ada.

## Nilai yang dikembalikan {#return-values}

Mengembalikan string ID prinsipal yang ditetapkan untuk pernyataan saat ini.  Fungsi ini gagal dalam sesi yang dimulai tanpa `--access.config`.

* * *

## Contoh {#examples}

### Memeriksa identitas yang ditetapkan {#inspect-the-established-identity}

Kedua fungsi tidak menerima argumen dan mendeskripsikan pemanggil yang diautentikasi oleh server untuk pernyataan ini.  `whoami()` mengembalikan ID prinsipal dan `groups()` mengembalikan grup direktori pemanggil, sehingga menjalankan keduanya bersama-sama merupakan cara cepat untuk memeriksa bahwa token atau sertifikat dipetakan ke prinsipal yang diharapkan sebelum hal lain dijalankan.

```sql
SELECT whoami() AS principal, groups() AS memberships;
```
