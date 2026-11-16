---
title: IIF
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Fungsi IIF di StackQL: mengembalikan salah satu dari dua nilai bergantung pada apakah suatu kondisi bernilai benar, ekspresi if-then-else sebaris."
image: "/img/stackql-featured-image.png"
---
Mengembalikan nilai berdasarkan evaluasi ekspresi input.  

Lihat juga:  
[[` SELECT `]](/language-spec/select)

* * * 

## Sintaksis {#syntax}

```sql
SELECT IIF(expression, result_if_true, result_if_false) 
FROM <multipartIdentifier>;
```
> Fungsi `iif()` secara logis setara dengan ekspresi `CASE` berikut: `CASE WHEN expression THEN result_if_true ELSE result_if_false END`

## Argumen {#arguments}

__*expression*__  
Ekspresi boolean yang akan dievaluasi.

:::tip 

Argumen *expression* dapat berupa fungsi bersarang seperti `JSON_EXTRACT`.

:::

__*result_if_true*__  
Hasil yang dikembalikan jika *expression* bernilai `true`.

__*result_if_false*__  
Hasil yang dikembalikan jika *expression* bernilai `false`.

## Nilai yang dikembalikan {#return-values}
Tipe data argumen *result_if_true* atau *result_if_false*.

* * *

## Contoh {#examples}

### Mengategorikan ukuran disk instance compute engine {#categorize-disk-sizes-of-compute-engine-instances}

```sql
SELECT iif(CAST(json_extract(disks, '$[0].diskSizeGb') AS INTEGER) > 10, '> 10GB', '<= 10GB') as disk_size
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Untuk informasi selengkapnya, lihat [https://www.sqlite.org/lang_corefunc.html#iif](https://www.sqlite.org/lang_corefunc.html#iif)
