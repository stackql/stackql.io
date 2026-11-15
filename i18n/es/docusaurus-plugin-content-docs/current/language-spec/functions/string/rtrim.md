---
title: RTRIM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Función RTRIM en StackQL: eliminar los espacios en blanco, o un conjunto de caracteres dado, del lado derecho de una cadena."
image: "/img/stackql-featured-image.png"
---
Devuelve una cadena formada al eliminar los espacios en blanco o los caracteres indicados que aparecen en el lado derecho de una cadena de entrada.  

Consulte también:  
[[` SELECT `]](/language-spec/select) [[` LTRIM `]](/language-spec/functions/string/ltrim) [[` TRIM `]](/language-spec/functions/string/trim) 

* * * 

## Sintaxis {#syntax}

```sql
SELECT RTRIM(character_expression[, character ]) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*character_expression*__  
Una cadena literal o una columna de cadena.

__*character*__  
Un carácter opcional que se elimina si se encuentra al final de *character_expression*.

## Valor(es) devuelto(s) {#return-values}
Devuelve una cadena.

* * *

## Ejemplos {#examples}

### Eliminar los espacios en blanco finales de una columna de cadena {#remove-trailing-whitespace-from-a-string-column}

```sql
SELECT rtrim(description) AS trailing_whitespace_removed
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Para obtener más información, consulte [https://www.sqlite.org/lang_corefunc.html#rtrim](https://www.sqlite.org/lang_corefunc.html#rtrim)
