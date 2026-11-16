---
title: PI
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: Mengambil konstanta matematika π (pi) menggunakan SQL di StackQL.
image: "/img/stackql-featured-image.png"
---
Mengembalikan konstanta matematika π (pi), yaitu rasio keliling lingkaran terhadap diameternya.

Lihat juga:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Sintaksis {#syntax}

```sql
SELECT PI() AS pi_value;
```

## Argumen {#arguments}

Fungsi ini tidak memerlukan argumen apa pun.

## Nilai yang dikembalikan {#return-values}
Mengembalikan konstanta π, kira-kira sama dengan 3.14159.

* * *

## Contoh {#examples}

### Mengambil nilai π {#retrieve-the-value-of-π}

```sql
SELECT PI() AS pi_value;
```

Kueri ini menghasilkan nilai π, yaitu kira-kira `3.14159`, yang berguna dalam berbagai perhitungan matematika yang melibatkan lingkaran dan fungsi geometris lainnya.
