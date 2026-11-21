---
title: LENGTH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Función LENGTH en StackQL: el número de caracteres de una cadena o el número de bytes de un BLOB."
image: "/img/stackql-featured-image.png"
---
Devuelve el número de caracteres de una expresión de entrada de tipo cadena anteriores al primer carácter NUL (`char(0)`), o el número de bytes de una expresión BLOB.  

Consulte también:  
[[` SELECT `]](/language-spec/select) 

* * * 

:::info

Para obtener la longitud de un array JSON, use la función [**JSON_ARRAY_LENGTH**](/language-spec/functions/json/json_array_length).

:::

## Sintaxis {#syntax}

```sql
SELECT LENGTH(expression) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*expression*__  
Una cadena o un BLOB.

> Devuelve `NULL` si *expression* es `NULL`.

> Si *expression* es un valor numérico, se devuelve la longitud de una representación en forma de cadena de *expression*.

## Valor(es) devuelto(s) {#return-values}
Devuelve un entero.

* * *

## Ejemplos {#examples}

### Devolver la longitud de una columna de cadena {#return-the-length-of-a-string-column}

```sql
SELECT name, length(name) AS len
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Para obtener más información, consulte [https://www.sqlite.org/lang_corefunc.html#length](https://www.sqlite.org/lang_corefunc.html#length)
