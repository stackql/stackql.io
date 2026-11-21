---
title: JSON_TYPE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Fungsi JSON_TYPE di StackQL: tipe (object, array, text, integer, real, true, false, atau null) dari elemen terluar nilai JSON, atau dari jalur di dalamnya."
image: "/img/stackql-featured-image.png"
---
Mengembalikan tipe data elemen terluar dari nilai JSON, atau dari elemen di jalur opsional.  

Lihat juga:  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[ Tipe data ]](/language-spec/data-types)

* * * 

:::tip 

Gunakan pernyataan [**DESCRIBE**](/language-spec/describe) untuk menemukan tipe data `array` atau `object` yang dapat digunakan dengan fungsi JSON StackQL.

:::

:::note 

Jika Anda menggunakan StackQL dengan backend SQL `postgres`, gunakan fungsi `json_typeof` sebagai gantinya.

:::

## Sintaksis {#syntax}

```sql
SELECT JSON_TYPE(json_array_or_object [, json_path]) 
FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*json_array_or_object*__  
Array atau objek JSON.

__*json_path*__  
Opsional. Jalur JSON ke array atau objek yang terdapat di dalam *json_array_or_object*.  Lihat [jsonpath.com](https://jsonpath.com/).

> Jika *json_path* tidak ada di *json_array_or_object*, fungsi mengembalikan `NULL`.

> Fungsi `json_type()` menghasilkan error jika salah satu argumennya tidak berformat benar.

## Nilai yang dikembalikan {#return-values}
String yang merepresentasikan tipe data hasil resolusi.

> Tipe data mencakup `null`, `true`, `false`, `integer`, `real`, `text`, `array`, atau `object`.

* * *

## Contoh {#examples}

### Mengembalikan tipe data field yang bersarang di dalam objek atau array JSON {#return-the-datatype-of-a-field-nested-within-a-json-object-or-array}

```sql
SELECT json_type(disks, '$[0].licenses[0]') as datatype
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
/* returns text */
```

Untuk informasi selengkapnya, lihat [https://www.sqlite.org/json1.html#jtype](https://www.sqlite.org/json1.html#jtype)
