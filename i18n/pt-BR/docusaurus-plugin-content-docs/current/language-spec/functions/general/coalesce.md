---
title: COALESCE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Função COALESCE no StackQL: retornar o primeiro argumento não NULL entre dois ou mais, normalmente para substituir um valor ausente por um valor padrão."
image: "/img/stackql-featured-image.png"
---
Retorna o primeiro argumento que não é `NULL` em uma série de dois ou mais argumentos; pode ser usada para substituir um valor `NULL` por um valor padrão.  

> A função `COALESCE` equivale à função `NVL` do Oracle ou à função `IFNULL` do MySQL.

Veja também:  
[[` SELECT `]](/language-spec/select) [[` IFNULL `]](/language-spec/functions/general/ifnull) [[` NULLIF `]](/language-spec/functions/general/nullif)

* * * 

## Sintaxe {#syntax}

```sql
SELECT COALESCE(expression1, expression2 [, ..]) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*expression*__  
Duas ou mais expressões de qualquer tipo de dados que resultam em um valor `NULL` ou não `NULL`.

> Se todos os argumentos *expression* forem `NULL`, a função `COALESCE` retorna `NULL`.

## Valor(es) retornado(s) {#return-values}

Retorna o primeiro valor não `NULL` da série de argumentos *expression*.

* * *

## Exemplos {#examples}

### Substituir um valor `NULL` por um valor padrão {#replace-a-null-value-with-a-default-value}

```sql
SELECT name, coalesce(website, cors, 'not hosting shared resources') AS website_or_cors_config
FROM google.storage.buckets where project = 'stackql';
```

Para obter mais informações, consulte [https://www.sqlite.org/lang_corefunc.html#coalesce](https://www.sqlite.org/lang_corefunc.html#coalesce)
