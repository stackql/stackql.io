---
title: Referensi ekspresi reguler
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Referensi ekspresi reguler untuk StackQL: sintaksis, kelas karakter, anchor, kuantifier, dan grup untuk REGEXP_LIKE, REGEXP_REPLACE, dan REGEXP_SUBSTR."
image: "/img/stackql-featured-image.png"
---
Referensi ekspresi reguler yang digunakan di StackQL.  

Lihat juga:  
[[` REGEXP_LIKE `]](/language-spec/functions/re/regexp_like)  [[` REGEXP_SUBSTR `]](/language-spec/functions/re/regexp_substr)  [[` REGEXP_REPLACE `]](/language-spec/functions/re/regexp_replace)

* * * 

## Sintaksis yang didukung {#supported-syntax}

Sintaksis ekspresi reguler berikut didukung di StackQL:    

| Ekspresi | Deskripsi |
|------------|-------------|
| `X*` | nol atau lebih kemunculan `X` |
| `X+` | satu atau lebih kemunculan `X` | 
| `X?` | nol atau satu kemunculan `X` |
| `X{n}` | tepat `n` kemunculan `X` |
| `X{n,}` | `n` atau lebih kemunculan `X` |
| `X{n,m}` | antara `n` dan `m` kemunculan `X` |
| `(X)` | mencocokkan `X` |
| `X`&vert;`Y` | `X` atau `Y` |
| `^X` | `X` yang muncul di awal string |
| `X$` | `X` yang muncul di akhir string |
| `.` | Mencocokkan satu karakter apa pun, termasuk baris baru |
| `\c` | Karakter `c`, dengan `c` salah satu dari `\{}()[]`&vert;`*+?.` |
| `\c` | Urutan escape bahasa C untuk `c` dalam `afnrtv`. Misalnya: `\t` atau `\n` |
| `[abc]` | Satu karakter apa pun dari himpunan `abc` |
| `[^abc]` | Satu karakter apa pun yang tidak ada dalam himpunan `abc` |
| `[a-z]` | Satu karakter apa pun dalam rentang `a-z` |
| `[^a-z]` | Satu karakter apa pun yang tidak ada dalam rentang `a-z` |
| `\d`, `\w`, `\s` | Satu digit, satu karakter kata (`[0-9A-Za-z_]`), satu karakter spasi kosong |
| `\D`, `\W`, `\S` | Karakter apa pun yang bukan digit, bukan karakter kata, bukan karakter spasi kosong |
| `\b` | Batas kata |  

&nbsp;  
&nbsp;  
Pola menggunakan sintaksis RE2, sebagaimana diimplementasikan oleh paket `regexp` Go.  Referensi balik (`\1`) dan asersi lookaround tidak didukung: pola yang menggunakannya ditolak dengan error.

Garis miring terbalik dalam literal string StackQL memulai urutan escape (`'\n'` adalah baris baru), sehingga garis miring terbalik yang merupakan bagian dari ekspresi reguler ditulis dua kali: `'\\d+'` cocok dengan satu digit atau lebih dan `'[0-9]+\\.[0-9]+'` cocok dengan titik literal di antara digit.

Untuk sintaksis lengkapnya, lihat [sintaksis RE2](https://github.com/google/re2/wiki/Syntax).
