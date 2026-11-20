---
title: JSON_INSERT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: Menyisipkan nilai baru ke dalam dokumen JSON tanpa menimpa data yang sudah ada menggunakan SQL di StackQL.
image: "/img/stackql-featured-image.png"
---
Menyisipkan nilai baru ke dalam dokumen JSON pada jalur yang ditentukan tanpa menimpa nilai yang sudah ada.

Lihat juga:  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[` JSON_SET `]](/language-spec/functions/json/json_set) [[` JSON_REPLACE `]](/language-spec/functions/json/json_replace)

* * * 

## Sintaksis {#syntax}

```sql
SELECT JSON_INSERT(json_column, path, new_value) FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*json_column*__  
Kolom atau ekspresi JSON yang akan diubah.

__*path*__  
Ekspresi jalur JSON tempat nilai baru akan disisipkan. Jika jalur tersebut sudah ada, nilai asli pada jalur itu tidak diganti.

__*new_value*__  
Nilai yang akan disisipkan pada jalur yang ditentukan di dalam dokumen JSON.

## Nilai yang dikembalikan {#return-values}
Mengembalikan dokumen JSON dengan nilai baru yang telah disisipkan jika jalur yang ditentukan belum ada; jika sudah ada, dokumen JSON dikembalikan tanpa perubahan.

:::note Perilaku jika kunci sudah ada

| Fungsi                                                       | Timpa jika sudah ada? | Buat jika belum ada? |
|--------------------------------------------------------------|-----------------------|----------------------|
| [`json_insert()`](/language-spec/functions/json/json_insert) | Tidak                 | Ya                   |
| [`json_replace()`](/language-spec/functions/json/json_replace) | Ya                    | Tidak                |
| [`json_set()`](/language-spec/functions/json/json_set)       | Ya                    | Ya                   |

:::

* * *

## Contoh {#examples}

### Menyisipkan pasangan kunci-nilai baru ke dalam objek JSON {#insert-a-new-key-value-pair-into-a-json-object}

```sql
-- Example: Insert a new 'region' key with the value 'Europe' into a JSON object
SELECT JSON_INSERT('{"name":"ResourceOne","type":"Storage"}', '$.region', 'Europe') as updated_object;

/* expected output...
|-----------------------------------------------------------|
|                      updated_object                       |
|-----------------------------------------------------------|
| {"name":"ResourceOne","type":"Storage","region":"Europe"} |
|-----------------------------------------------------------|
*/
```

Kueri ini menyisipkan kunci baru bernama 'region' dengan nilai 'Europe' ke dalam dokumen JSON jika kunci 'region' belum ada. Jika 'region' sudah menjadi kunci di dalam dokumen, dokumen asli dikembalikan tanpa perubahan.
