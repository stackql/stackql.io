---
title: COALESCE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Función COALESCE en StackQL: devolver el primer argumento no NULL de dos o más, normalmente para sustituir un valor ausente por uno predeterminado."
image: "/img/stackql-featured-image.png"
---
Devuelve el primer argumento que no es `NULL` de una serie de dos o más argumentos; se puede usar para sustituir un valor `NULL` por un valor predeterminado.  

> La función `COALESCE` equivale a la función `NVL` de Oracle o a la función `IFNULL` de MySQL.

Consulte también:  
[[` SELECT `]](/language-spec/select) [[` IFNULL `]](/language-spec/functions/general/ifnull) [[` NULLIF `]](/language-spec/functions/general/nullif)

* * * 

## Sintaxis {#syntax}

```sql
SELECT COALESCE(expression1, expression2 [, ..]) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*expression*__  
Dos o más expresiones de cualquier tipo de datos que se resuelven en un valor `NULL` o no `NULL`.

> Si todos los argumentos *expression* son `NULL`, la función `COALESCE` devuelve `NULL`.

## Valor(es) devuelto(s) {#return-values}

Devuelve el primer valor no `NULL` de la serie de argumentos *expression*.

* * *

## Ejemplos {#examples}

### Sustituir un valor `NULL` por un valor predeterminado {#replace-a-null-value-with-a-default-value}

```sql
SELECT name, coalesce(website, cors, 'not hosting shared resources') AS website_or_cors_config
FROM google.storage.buckets where project = 'stackql';
```

Para obtener más información, consulte [https://www.sqlite.org/lang_corefunc.html#coalesce](https://www.sqlite.org/lang_corefunc.html#coalesce)
