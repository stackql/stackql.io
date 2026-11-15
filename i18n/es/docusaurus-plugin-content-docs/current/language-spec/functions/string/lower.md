---
title: LOWER
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Función LOWER en StackQL: convertir una cadena a minúsculas."
image: "/img/stackql-featured-image.png"
---
Devuelve una cadena de entrada en minúsculas.  

Consulte también:  
[[` SELECT `]](/language-spec/select) [[` UPPER `]](/language-spec/functions/string/upper) [[ Tipos de datos ]](/language-spec/data-types)

* * * 

## Sintaxis {#syntax}

```sql
SELECT LOWER(character_expression) FROM <multipartIdentifier>;
```
> La función `lower()` solo funciona con caracteres ASCII.

## Argumentos {#arguments}

__*character_expression*__  
Una cadena ASCII.

## Valor(es) devuelto(s) {#return-values}
Devuelve un valor de cadena.

* * *

## Ejemplos {#examples}

### Devolver una cadena en minúsculas {#return-a-string-in-lowercase}

```sql
SELECT name, LOWER(location) FROM google.storage.buckets WHERE project = 'stackql-demo';
```

Para obtener más información, consulte [https://www.sqlite.org/lang_corefunc.html#lower](https://www.sqlite.org/lang_corefunc.html#lower)
