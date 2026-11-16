---
title: JSON_EQUAL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: Membandingkan dua string JSON untuk kesetaraan menggunakan SQL.
image: "/img/stackql-featured-image.png"
---
Membandingkan dua string JSON dan mengembalikan 1 jika keduanya setara, atau 0 jika tidak.

Lihat juga:  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[` JSON_ARRAY_LENGTH `]](/language-spec/functions/json/json_array_length) [[ Tipe data ]](/language-spec/data-types)

* * * 

:::tip 

Fungsi `json_equal` merupakan fungsi ekstensi kustom StackQL. Fungsi ini mendukung perbandingan objek dan array JSON, serta memperlakukan objek dengan kunci dalam urutan yang berbeda sebagai setara sesuai spesifikasi JSON. Fungsi `json_equal` tersedia mulai StackQL versi `0.5.707` dan hanya didukung dengan backend SQL default StackQL yang tersemat dan berjalan di memori.

:::

## Sintaksis {#syntax}

```sql
SELECT JSON_EQUAL(json1, json2);
```

## Argumen {#arguments}

__*json1*__  
String JSON yang merepresentasikan objek atau array.

__*json2*__  
String JSON lain untuk dibandingkan dengan argumen pertama.

> Kedua argumen harus berupa string JSON yang valid.

## Nilai yang dikembalikan {#return-values}
Nilai bilangan bulat: 1 jika string JSON setara, 0 jika tidak.

* * *

## Contoh {#examples}

### Membandingkan objek JSON {#compare-json-objects}

```sql
SELECT 
public_access_block_configuration as current_state,
'{"BlockPublicAcls": true, "BlockPublicPolicy": true, "IgnorePublicAcls": true, "RestrictPublicBuckets": true}' as desired_state,
JSON_EQUAL(public_access_block_configuration, '{"BlockPublicAcls": true, "BlockPublicPolicy": true, "IgnorePublicAcls": true, "RestrictPublicBuckets": true}') as comp
FROM awscc.s3.buckets 
WHERE region = 'ap-southeast-2' 
AND Identifier = 'my-bucket';
/* returns...
|--------------------------------------------------------------------------------------------------------|--------------------------------|------|
|                                             current_state                                              |         desired_state          | comp |
|--------------------------------------------------------------------------------------------------------|--------------------------------|------|
| {"RestrictPublicBuckets":true,"BlockPublicPolicy":true,"BlockPublicAcls":true,"IgnorePublicAcls":true} | {"BlockPublicAcls": true,      |    1 |
|                                                                                                        | "BlockPublicPolicy": true,     |      |
|                                                                                                        | "IgnorePublicAcls": true,      |      |
|                                                                                                        | "RestrictPublicBuckets": true} |      |
|--------------------------------------------------------------------------------------------------------|--------------------------------|------|
*/
```

### Membandingkan array JSON {#compare-json-arrays}

```sql
SELECT 
  json_equal('[1, 2, 3]', '[1, 2, 3]') as array_comp_1,
  json_equal('[1, 2, 3]', '[3, 2, 1]') as array_comp_2
; 
/* returns...
|--------------|--------------|
| array_comp_1 | array_comp_2 |
|--------------|--------------|
|            1 |            0 |
|--------------|--------------|
*/
```

Untuk informasi selengkapnya, lihat [json_equal](https://github.com/stackql/sqlite-ext-functions/blob/main/docs/json_equal.md).
