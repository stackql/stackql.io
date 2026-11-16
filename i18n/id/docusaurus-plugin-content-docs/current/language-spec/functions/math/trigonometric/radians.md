---
title: RADIANS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: Mengonversi sudut dari derajat ke radian menggunakan SQL di StackQL.
image: "/img/stackql-featured-image.png"
---
Mengonversi sudut yang ditentukan dari derajat ke radian.

Lihat juga:  
[[`SELECT`]](/language-spec/select)

* * * 

## Sintaksis {#syntax}

```sql
SELECT RADIANS(angle_in_degrees) FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*angle_in_degrees*__  
Ekspresi numerik yang menyatakan sudut dalam derajat.

## Nilai yang dikembalikan {#return-values}
Mengembalikan nilai sudut yang setara dalam radian.

* * *

## Contoh {#examples}

### Mengonversi 180 derajat ke radian {#convert-180-degrees-to-radians}

```sql
SELECT RADIANS(180) AS radians_value;
```

Kueri ini mengembalikan π (kira-kira `3.14159`), karena konversi 180 derajat ke radian menghasilkan π radian.

### Mengonversi 90 derajat ke radian {#convert-90-degrees-to-radians}

```sql
SELECT RADIANS(90) AS radians_value;
```

Kueri ini menghitung konversi 90 derajat ke radian, dengan hasil π/2 atau kira-kira `1.5708`, karena 90 derajat adalah setengah dari 180 derajat.
