---
title: TAN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: Menghitung tangen sudut yang diberikan dalam radian menggunakan SQL di StackQL.
image: "/img/stackql-featured-image.png"
---
Menghitung tangen dari sudut yang ditentukan dalam radian.

Lihat juga:  
[[`SELECT`]](/language-spec/select)

* * * 

## Sintaksis {#syntax}

```sql
SELECT TAN(radian_value) FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*radian_value*__  
Ekspresi numerik yang menentukan sudut dalam radian yang tangennya akan dihitung.

## Nilai yang dikembalikan {#return-values}
Mengembalikan tangen dari sudut yang ditentukan.

* * *

## Contoh {#examples}

### Menghitung tangen π/4 radian {#calculate-the-tangent-of-π4-radians}

```sql
SELECT TAN(PI() / 4) AS tangent_value;
```

Kueri ini mengembalikan `1`, karena tangen π/4 radian adalah 1.

### Menghitung tangen π radian {#calculate-the-tangent-of-π-radians}

```sql
SELECT TAN(PI()) AS tangent_value;
```

Kueri ini menghitung tangen π radian, dengan hasil kira-kira `0`, karena tangen π sangat mendekati 0.
