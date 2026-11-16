---
title: DEGREES
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: Mengonversi sudut dari radian ke derajat menggunakan SQL di StackQL.
image: "/img/stackql-featured-image.png"
---
Mengonversi sudut yang ditentukan dari radian ke derajat.

Lihat juga:  
[[`SELECT`]](/language-spec/select)

* * * 

## Sintaksis {#syntax}

```sql
SELECT DEGREES(angle_in_radians) FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*angle_in_radians*__  
Ekspresi numerik yang menyatakan sudut dalam radian.

## Nilai yang dikembalikan {#return-values}
Mengembalikan nilai sudut yang setara dalam derajat.

* * *

## Contoh {#examples}

### Mengonversi π radian ke derajat {#convert-π-radians-to-degrees}

```sql
SELECT DEGREES(3.14159) AS degrees_value;
```

Kueri ini mengembalikan kira-kira `180`, karena konversi π radian (kira-kira `3.14159`) menghasilkan sekitar 180 derajat.

### Mengonversi π/2 radian ke derajat {#convert-π2-radians-to-degrees}

```sql
SELECT DEGREES(1.5708) AS degrees_value;
```

Kueri ini menghitung konversi π/2 radian (kira-kira `1.5708`) ke derajat, dengan hasil sekitar `90`, karena π/2 radian setara dengan sudut 90 derajat.
