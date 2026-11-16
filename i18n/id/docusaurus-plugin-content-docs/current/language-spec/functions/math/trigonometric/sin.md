---
title: SIN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: Menghitung sinus sudut yang diberikan dalam radian menggunakan SQL di StackQL.
image: "/img/stackql-featured-image.png"
---
Menghitung sinus dari sudut yang ditentukan dalam radian.

Lihat juga:  
[[`SELECT`]](/language-spec/select)

* * * 

## Sintaksis {#syntax}

```sql
SELECT SIN(radian_value) FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*radian_value*__  
Ekspresi numerik yang menentukan sudut dalam radian yang sinusnya akan dihitung.

## Nilai yang dikembalikan {#return-values}
Mengembalikan sinus dari sudut yang ditentukan.

* * *

## Contoh {#examples}

### Menghitung sinus π/2 radian {#calculate-the-sine-of-π2-radians}

```sql
SELECT SIN(PI() / 2) AS sine_value;
```

Kueri ini mengembalikan `1`, karena sinus π/2 radian adalah 1.

### Menghitung sinus π radian {#calculate-the-sine-of-π-radians}

```sql
SELECT SIN(PI()) AS sine_value;
```

Kueri ini menghitung sinus π radian, dengan hasil kira-kira `0`, karena sinus π adalah 0.
