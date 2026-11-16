---
title: ATAN2
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: Menghitung arkus tangen dari dua bilangan (y/x), yang mengembalikan sudut dalam radian, menggunakan SQL di StackQL.
image: "/img/stackql-featured-image.png"
---
Menghitung arkus tangen dari dua bilangan, tepatnya sudut dalam radian yang tangennya merupakan rasio dua bilangan yang ditentukan (y dibagi x), dengan memperhitungkan tanda kedua input secara benar untuk menentukan kuadran yang tepat.

Lihat juga:  
[[`SELECT`]](/language-spec/select)

* * * 

## Sintaksis {#syntax}

```sql
SELECT ATAN2(y, x) FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*y*__  
Koordinat y (pembilang) dari titik.

__*x*__  
Koordinat x (penyebut) dari titik.

## Nilai yang dikembalikan {#return-values}
Mengembalikan arkus tangen y/x dalam radian, dengan memperhitungkan kuadran sudut yang benar.

* * *

## Contoh {#examples}

### Menghitung arkus tangen titik (1,1) {#calculate-the-arc-tangent-of-the-point-11}

```sql
SELECT ATAN2(1, 1) AS arc_tangent_value;
```

Kueri ini mengembalikan π/4 atau kira-kira `0.785`, karena arkus tangen titik (1,1) sesuai dengan sudut π/4 radian di kuadran pertama.

### Menghitung arkus tangen titik (-10, 10) {#calculate-the-arc-tangent-of-the-point--10-10}

```sql
SELECT ATAN2(10, -10) AS arc_tangent_value;
```

Kueri ini menghitung sudut titik (-10, 10), dengan y = 10 dan x = -10, yang menghasilkan 3π/4 atau kira-kira `2.356`: tangen sudut ini adalah -1 dan titik tersebut berada di kuadran kedua.
