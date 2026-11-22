---
title: Operator penggabungan
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Operator penggabungan || di StackQL: menggabungkan dua atau lebih nilai string menjadi satu string."
image: "/img/stackql-featured-image.png"
---
Mengembalikan string hasil penggabungan, atau penyambungan, dua atau lebih nilai string.  

Lihat juga:  
[[` SELECT `]](/language-spec/select) 

* * * 

## Sintaksis {#syntax}

```sql
SELECT character_expression1 || character_expression2 FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*character_expression1*__, __*character_expression2*__  
String literal atau kolom string.

## Nilai yang dikembalikan {#return-values}

Mengembalikan string.

:::note

Mulai StackQL `v1.0`, parser memperlakukan `||` sebagai operator penggabungan yang mengikat lebih kuat daripada operator perbandingan, sehingga `WHERE id = prefix || suffix` membandingkan `id` dengan string hasil penggabungan.  Rilis sebelumnya memperlakukan token `||` sebagai `OR` boolean.  Gunakan `OR` untuk disjungsi boolean.  [Variabel sesi](/getting-started/session-variables) juga dapat digabungkan: `$prefix || '-' || $suffix`.

:::

* * *

## Contoh {#examples}

### Menggabungkan dua field {#concatenate-two-fields-together}

```sql
SELECT name || ' (' || location || ')' as bucket_location
FROM google.storage.buckets 
WHERE project = 'stackql-demo';
```

Untuk informasi selengkapnya, lihat [https://sqlite.org/lang_expr.html](https://sqlite.org/lang_expr.html)
