---
title: JSON_OBJECT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Fungsi JSON_OBJECT di StackQL: membuat objek JSON dari argumen kunci dan nilai, misalnya untuk menyusun isi permintaan bagi INSERT dan UPDATE."
image: "/img/stackql-featured-image.png"
---
Mengembalikan objek yang disusun dari argumen yang diberikan.  

Lihat juga:  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[ Tipe data ]](/language-spec/data-types)

* * * 

:::tip 

Gunakan pernyataan [**DESCRIBE**](/language-spec/describe) untuk menemukan tipe data `array` atau `object` yang dapat digunakan dengan fungsi JSON StackQL.

:::

:::note 

Jika Anda menggunakan stackql dengan backend SQL `postgres`, gunakan `jsonb_build_object` sebagai pengganti `json_object`: StackQL meneruskan `json_object` ke PostgreSQL tanpa perubahan, dan fungsi PostgreSQL dengan nama tersebut menerima kunci dan nilainya sebagai array teks.

:::

## Sintaksis {#syntax}

```sql
SELECT JSON_OBJECT(key1, value1, key2, value2, ...) 
FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*key1, key2, ...*__  
Label untuk objek JSON.

__*value1, value2, ...*__  
Nilai yang sesuai dengan label di dalam objek JSON.

## Nilai yang dikembalikan {#return-values}
String yang merepresentasikan objek JSON yang berformat benar.

* * *

## Contoh {#examples}

### Membuat array objek JSON dari suatu kolom {#build-an-array-of-json-objects-from-a-column}

```sql
SELECT JSON_GROUP_ARRAY(json_object('instance', selfLink)) as instance_links
FROM google.compute.instances 
WHERE project = 'stackql-k8s-the-hard-way-demo'
AND zone = 'australia-southeast1-a'
AND name like '%-dev-controller-%';
/* returns a data structure required for a targetPool in GCP */
```

Untuk informasi selengkapnya, lihat [https://sqlite.org/json1.html#jobj](https://sqlite.org/json1.html#jobj)
