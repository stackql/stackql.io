---
title: JSON_REMOVE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: Menghapus elemen dari dokumen JSON dengan menentukan jalur menggunakan SQL di StackQL.
image: "/img/stackql-featured-image.png"
---
Menghapus elemen tertentu dari dokumen JSON menggunakan ekspresi jalur. Jika jalur yang ditentukan tidak ada di dalam dokumen, jalur tersebut diabaikan. Pemrosesan jalur secara berurutan dapat memengaruhi struktur untuk penghapusan berikutnya.

Lihat juga:  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract)

* * * 

## Sintaksis {#syntax}

```sql
SELECT JSON_REMOVE(json_document, path1, path2, ...) FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*json_document*__  
Dokumen JSON yang elemennya akan dihapus.

__*path1, path2, ...*__  
Satu atau beberapa ekspresi jalur yang menunjuk ke elemen di dalam dokumen JSON yang akan dihapus.

## Nilai yang dikembalikan {#return-values}
Mengembalikan salinan dokumen JSON tanpa elemen yang ditentukan. Jika tidak ada jalur yang diberikan, fungsi mengembalikan JSON input yang diformat ulang tanpa spasi kosong berlebih.

* * *

## Contoh {#examples}

### Menghapus elemen dari array JSON {#remove-an-element-from-a-json-array}

```sql
-- Example: Remove the third element from a JSON array
SELECT JSON_REMOVE('[0,1,2,3,4]', '$[2]') AS modified_json;
```

Output: `'[0,1,3,4]'`

### Menghapus beberapa elemen dari array JSON {#remove-multiple-elements-from-a-json-array}

```sql
-- Example: Remove the first and third elements from a JSON array
SELECT JSON_REMOVE('[0,1,2,3,4]', '$[2]', '$[0]') AS modified_json;
```

Output: `'[1,3,4]'`

### Menghapus elemen terakhir dan pertama menggunakan indeks negatif {#remove-last-element-using-negative-index}

```sql
-- Example: Remove the last and first elements from a JSON array
SELECT JSON_REMOVE('[0,1,2,3,4]', '$[#-1]', '$[0]') AS modified_json;
```

Output: `'[1,2,3]'`

### Menghapus jalur yang tidak ada {#remove-non-existent-path}

```sql
-- Example: Attempt to remove a non-existent key from a JSON object
SELECT JSON_REMOVE('{"x":25,"y":42}', '$.z') AS modified_json;
```

Output: `'{"x":25,"y":42}'`

### Menghapus kunci yang ada dari objek JSON {#remove-existing-key-from-json-object}

```sql
-- Example: Remove the 'y' key from a JSON object
SELECT JSON_REMOVE('{"x":25,"y":42}', '$.y') AS modified_json;
```

Output: `'{"x":25}'`

### Kasus tepi: penghapusan dengan jalur root {#edge-case-removal-with-root-path}

```sql
-- Example: Attempt to remove using the root path
SELECT JSON_REMOVE('{"x":25,"y":42}', '$') AS modified_json;
```

Output: `NULL`

Contoh ini menunjukkan cara fungsi `json_remove()` digunakan untuk memanipulasi data JSON secara dinamis, mencakup berbagai skenario, mulai dari penghapusan sederhana hingga penanganan kasus tepi dalam modifikasi struktur data.
