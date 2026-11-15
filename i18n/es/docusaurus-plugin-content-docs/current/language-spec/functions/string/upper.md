---
title: UPPER
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Función UPPER en StackQL: convertir una cadena a mayúsculas."
image: "/img/stackql-featured-image.png"
---
Devuelve una cadena de entrada en mayúsculas.  

Consulte también:  
[[` SELECT `]](/language-spec/select) [[` LOWER `]](/language-spec/functions/string/lower) [[ Tipos de datos ]](/language-spec/data-types)

* * * 

## Sintaxis {#syntax}

```sql
SELECT UPPER(character_expression) FROM <multipartIdentifier>;
```
> La función `upper()` solo funciona con caracteres ASCII.

## Argumentos {#arguments}

__*character_expression*__  
Una cadena ASCII.

## Valor(es) devuelto(s) {#return-values}
Devuelve un valor de cadena.

* * *

## Ejemplos {#examples}

### Devolver una cadena en mayúsculas {#return-a-string-in-uppercase}

```sql
SELECT UPPER(name), location FROM google.storage.buckets WHERE project = 'stackql-demo';
```

Para obtener más información, consulte [https://www.sqlite.org/lang_corefunc.html#upper](https://www.sqlite.org/lang_corefunc.html#upper)
