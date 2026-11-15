---
title: IFNULL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Função IFNULL no StackQL: retornar o primeiro de exatamente dois argumentos que não é NULL, uma forma de COALESCE com dois argumentos."
image: "/img/stackql-featured-image.png"
---
Aceita exatamente dois argumentos e retorna o primeiro argumento não `NULL`; pode ser usada para substituir um valor `NULL` por um valor padrão.  

> A função `IFNULL` equivale à função [`COALESCE`](/language-spec/functions/general/coalesce) com apenas dois argumentos; a função [`COALESCE`](/language-spec/functions/general/coalesce) pode ter mais de dois argumentos.

Veja também:  
[[` SELECT `]](/language-spec/select) [[` COALESCE `]](/language-spec/functions/general/coalesce) [[` NULLIF `]](/language-spec/functions/general/nullif)

* * * 

## Sintaxe {#syntax}

```sql
SELECT IFNULL(expression1, expression2) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*expression*__  
Duas expressões de qualquer tipo de dados que resultam em um valor `NULL` ou não `NULL`.

> Se os dois argumentos *expression* forem `NULL`, a função `IFNULL` retorna `NULL`.


## Valor(es) retornado(s) {#return-values}

Retorna o primeiro valor não `NULL` dos dois argumentos *expression*. 

* * *

## Exemplos {#examples}

### Substituir um valor `NULL` por um valor padrão {#replace-a-null-value-with-a-default-value}

```sql
SELECT name, ifnull(website, 'Not hosting a website') AS website 
FROM google.storage.buckets where project = 'stackql';
```

Para obter mais informações, consulte [https://www.sqlite.org/lang_corefunc.html#ifnull](https://www.sqlite.org/lang_corefunc.html#ifnull)
