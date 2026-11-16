---
title: GROUPS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - manajemen akses
  - groups
  - direktori prinsipal
description: "Fungsi GROUPS di StackQL: mengembalikan grup direktori pemanggil yang terautentikasi sebagai array JSON saat manajemen akses diaktifkan."
image: "/img/stackql-featured-image.png"
---
Mengembalikan keanggotaan grup direktori pemanggil yang terautentikasi sebagai array JSON.  Memerlukan sesi yang dimulai dengan [manajemen akses](/command-line-usage/access-management) yang diaktifkan.  Tersedia mulai StackQL `v1.0`.

Lihat juga:  
[[` WHOAMI `]](/language-spec/functions/session/whoami) [[ Manajemen akses ]](/command-line-usage/access-management)

* * * 

## Sintaksis {#syntax}

```sql
SELECT GROUPS() [ AS <alias> ];
```

## Argumen {#arguments}

Tidak ada.

## Nilai yang dikembalikan {#return-values}

Mengembalikan array JSON berisi ID grup dari snapshot direktori prinsipal yang diambil untuk pernyataan saat ini, misalnya `["readers"]`.  Keanggotaan diperbarui untuk setiap pernyataan, sehingga pencabutan tercermin pada pernyataan berikutnya.  Fungsi ini gagal dalam sesi yang dimulai tanpa `--access.config`.

* * *

## Contoh {#examples}

### Memeriksa identitas yang ditetapkan {#inspect-the-established-identity}

Kedua fungsi ini bersama-sama menunjukkan ID prinsipal pemanggil dan grup direktorinya saat ini.  Karena keanggotaan dibaca ulang untuk setiap pernyataan, menjalankan kueri ini setelah perubahan direktori memastikan bahwa perubahan tersebut terlihat oleh sesi.

```sql
SELECT whoami() AS principal, groups() AS memberships;
```
