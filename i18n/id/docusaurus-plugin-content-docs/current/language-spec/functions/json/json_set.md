---
title: JSON_SET
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: Memperbarui atau menyisipkan nilai dalam dokumen JSON menggunakan SQL di StackQL.
image: "/img/stackql-featured-image.png"
---
Memperbarui nilai yang sudah ada atau menyisipkan nilai baru dalam dokumen JSON pada jalur yang ditentukan.

Lihat juga:  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[` JSON_SET `]](/language-spec/functions/json/json_set) [[` JSON_REPLACE `]](/language-spec/functions/json/json_replace)

* * * 

## Sintaksis {#syntax}

```sql
SELECT JSON_SET(json_column, path, value_to_set) FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*json_column*__  
Kolom atau ekspresi JSON yang akan dimodifikasi.

__*path*__  
Ekspresi jalur JSON yang menunjukkan tempat nilai akan ditetapkan. Jika jalur sudah ada, nilainya diperbarui; jika jalur belum ada, pasangan kunci-nilai baru ditambahkan.

__*value_to_set*__  
Nilai yang akan ditetapkan pada jalur yang ditentukan dalam dokumen JSON.

## Nilai yang dikembalikan {#return-values}
Mengembalikan dokumen JSON dengan nilai yang ditetapkan pada jalur yang ditentukan. Jika jalur sudah ada, nilai yang ada diperbarui; jika tidak, pasangan kunci-nilai baru ditambahkan.

:::note Perilaku jika kunci sudah ada

| Fungsi                                                       | Timpa jika sudah ada?        | Buat jika belum ada?      |
|--------------------------------------------------------------|------------------------------|---------------------------|
| [`json_insert()`](/language-spec/functions/json/json_insert) | Tidak                        | Ya                        |
| [`json_replace()`](/language-spec/functions/json/json_replace) | Ya                           | Tidak                     |
| [`json_set()`](/language-spec/functions/json/json_set)       | Ya                           | Ya                        |

:::

* * *

## Contoh {#examples}

### Memperbarui nilai yang sudah ada dalam objek JSON {#update-an-existing-value-in-a-json-object}

```sql
-- Example: Update the 'status' key's value to 'active' in a JSON object
SELECT JSON_SET(json_data, '$.status', 'active') as updated_json
FROM cloud_services
WHERE service_id = 'service123';

-- Expected output:
-- |---------------------------------------------------------|
-- |                    updated_json                         |
-- |---------------------------------------------------------|
-- | {"name":"ServiceOne", "status":"active", "region":"US"} |
-- |---------------------------------------------------------|
```

Kueri ini memperbarui kunci 'status' menjadi 'active' dalam dokumen JSON jika kunci 'status' sudah ada. Jika 'status' tidak ada dalam dokumen, kunci tersebut ditambahkan dengan nilai 'active'.

### Menambahkan pasangan kunci-nilai baru ke objek JSON {#add-a-new-key-value-pair-to-a-json-object}

```sql
-- Example: Add a new key 'lastChecked' with the current date to a JSON object
SELECT JSON_SET(json_data, '$.lastChecked', '2024-04-29') as updated_json
FROM cloud_services
WHERE service_id = 'service123';

/* expected output...
|-----------------------------------------------------------------------|
|                          updated_json                                 |
|-----------------------------------------------------------------------|
| {"name":"ServiceOne", "status":"pending", "lastChecked":"2024-04-29"} |
|-----------------------------------------------------------------------|
*/
```

Kueri ini menambahkan kunci baru 'lastChecked' dengan nilai '2024-04-29' ke dokumen JSON jika 'lastChecked' belum ada. Jika sudah ada, nilai yang ada diperbarui menjadi '2024-04-29'.
