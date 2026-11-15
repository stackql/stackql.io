---
title: IFNULL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Función IFNULL en StackQL: devolver el primero de exactamente dos argumentos que no sea NULL, una forma de COALESCE con dos argumentos."
image: "/img/stackql-featured-image.png"
---
Acepta exactamente dos argumentos y devuelve el primer argumento no `NULL`; se puede usar para sustituir un valor `NULL` por un valor predeterminado.  

> La función `IFNULL` equivale a la función [`COALESCE`](/language-spec/functions/general/coalesce) con solo dos argumentos; la función [`COALESCE`](/language-spec/functions/general/coalesce) puede tener más de dos argumentos.

Consulte también:  
[[` SELECT `]](/language-spec/select) [[` COALESCE `]](/language-spec/functions/general/coalesce) [[` NULLIF `]](/language-spec/functions/general/nullif)

* * * 

## Sintaxis {#syntax}

```sql
SELECT IFNULL(expression1, expression2) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*expression*__  
Dos expresiones de cualquier tipo de datos que se resuelven en un valor `NULL` o no `NULL`.

> Si ambos argumentos *expression* son `NULL`, la función `IFNULL` devuelve `NULL`.


## Valor(es) devuelto(s) {#return-values}

Devuelve el primer valor no `NULL` de los dos argumentos *expression*. 

* * *

## Ejemplos {#examples}

### Sustituir un valor `NULL` por un valor predeterminado {#replace-a-null-value-with-a-default-value}

```sql
SELECT name, ifnull(website, 'Not hosting a website') AS website 
FROM google.storage.buckets where project = 'stackql';
```

Para obtener más información, consulte [https://www.sqlite.org/lang_corefunc.html#ifnull](https://www.sqlite.org/lang_corefunc.html#ifnull)
