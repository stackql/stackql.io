---
title: TRIM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Función TRIM en StackQL: eliminar los espacios, o cualquiera de un conjunto de caracteres dado, de ambos extremos de una cadena."
image: "/img/stackql-featured-image.png"
---
Devuelve una cadena formada al eliminar los espacios, o cualquiera de un conjunto de caracteres dado, de ambos extremos de una cadena de entrada.  

Consulte también:  
[[` SELECT `]](/language-spec/select) [[` LTRIM `]](/language-spec/functions/string/ltrim) [[` RTRIM `]](/language-spec/functions/string/rtrim) 

* * * 

## Sintaxis {#syntax}

```sql
SELECT TRIM(character_expression[, character ]) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*character_expression*__  
Una cadena literal o una columna de cadena.

__*character*__  
Una cadena de caracteres opcional: cada uno de sus caracteres se elimina del principio y del final de *character_expression*.  Si se omite, se eliminan los espacios (no las tabulaciones ni los saltos de línea).

## Valor(es) devuelto(s) {#return-values}
Devuelve una cadena.

* * *

## Ejemplos {#examples}

### Eliminar los espacios iniciales y finales de una columna de cadena {#remove-leading-and-trailing-spaces-from-a-string-column}

```sql
SELECT trim(description) AS leading_and_trailing_whitespace_removed
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Para obtener más información, consulte [https://www.sqlite.org/lang_corefunc.html#trim](https://www.sqlite.org/lang_corefunc.html#trim)
