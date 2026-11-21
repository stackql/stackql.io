---
title: COS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: Menghitung kosinus suatu sudut yang dinyatakan dalam radian menggunakan SQL di StackQL.
image: "/img/stackql-featured-image.png"
---
Menghitung kosinus sudut tertentu yang dinyatakan dalam radian.

Lihat juga:  
[[`SELECT`]](/language-spec/select)

* * * 

## Sintaksis {#syntax}

```sql
SELECT COS(radian_value) FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*radian_value*__  
Ekspresi numerik yang menentukan sudut dalam radian yang kosinusnya akan dihitung.

## Nilai yang dikembalikan {#return-values}
Mengembalikan kosinus sudut yang ditentukan.

* * *

## Contoh {#examples}

### Menghitung kosinus π/3 radian {#calculate-the-cosine-of-π3-radians}

```sql
SELECT COS(PI() / 3) AS cosine_value;
```

Kueri ini mengembalikan `0.5000000000000001`, yaitu perkiraan floating point dari kosinus π/3 radian, yang nilainya 0.5.

### Menghitung kosinus π radian {#calculate-the-cosine-of-π-radians}

```sql
SELECT COS(PI()) AS cosine_value;
```

Kueri ini menghitung kosinus π radian, yang menghasilkan `-1`, karena kosinus π adalah -1.
