---
title: JSON_PATCH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: Menerapkan JSON MergePatch ke dokumen JSON menggunakan SQL di StackQL, untuk mengubah atau menggabungkan objek JSON.
image: "/img/stackql-featured-image.png"
---
Menerapkan JSON MergePatch (RFC-7396) ke dokumen JSON dan mengubahnya sesuai operasi yang ditentukan. Fungsi ini digunakan untuk menambahkan, mengubah, atau menghapus elemen di dalam objek JSON, tetapi memperlakukan array JSON sebagai unit atomik.

Lihat juga:  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract)

* * * 

## Sintaksis {#syntax}

```sql
SELECT JSON_PATCH(target_json, patch_json) FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*target_json*__  
Dokumen JSON yang akan di-patch.

__*patch_json*__  
Dokumen JSON yang menjelaskan perubahan yang akan diterapkan menurut algoritma MergePatch.

## Nilai yang dikembalikan {#return-values}
Mengembalikan dokumen JSON yang telah diubah setelah patch diterapkan.

* * *

## Contoh {#examples}

### MergePatch sederhana {#simple-mergepatch}

```sql
-- Example: Simple merge of new elements into the target JSON object
SELECT JSON_PATCH('{"a":1,"b":2}', '{"c":3,"d":4}') AS patched_json;
```

Output: `{"a":1,"b":2,"c":3,"d":4}`

### Mengganti elemen dalam array {#replace-element-in-array}

```sql
-- Example: Replacing an array with a single value in the JSON object
SELECT JSON_PATCH('{"a":[1,2],"b":2}', '{"a":9}') AS patched_json;
```

Output: `{"a":9,"b":2}`

### Menghapus elemen menggunakan null {#remove-element-using-null}

```sql
-- Example: Removing an element by setting it to null
SELECT JSON_PATCH('{"a":1,"b":2}', '{"a":9,"b":null,"c":8}') AS patched_json;
```

Output: `{"a":9,"c":8}`

### MergePatch bersarang {#nested-mergepatch}

```sql
-- Example: Nested merge patch updating sub-keys within a nested JSON object
SELECT JSON_PATCH('{"a":{"x":1,"y":2},"b":3}', '{"a":{"y":9},"c":8}') AS patched_json;
```

Output: `{"a":{"x":1,"y":9},"b":3,"c":8}`

Fungsionalitas ini memungkinkan pembaruan yang fleksibel dan dinamis terhadap JSON yang disimpan di database, dan sangat berguna bagi aplikasi yang memerlukan modifikasi struktur data secara kondisional.
