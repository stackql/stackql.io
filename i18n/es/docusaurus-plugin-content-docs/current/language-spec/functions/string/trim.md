---
title: TRIM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Función TRIM en StackQL: eliminar los espacios en blanco, o un conjunto de caracteres dado, de ambos extremos de una cadena."
image: "/img/stackql-featured-image.png"
---
Devuelve una cadena formada al eliminar los espacios en blanco o los caracteres indicados que aparecen en los lados izquierdo y derecho de una cadena de entrada.  

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
Un carácter opcional que se elimina si se encuentra al principio o al final de *character_expression*.

## Valor(es) devuelto(s) {#return-values}
Devuelve una cadena.

* * *

## Ejemplos {#examples}

### Eliminar los espacios en blanco iniciales y finales de una columna de cadena {#remove-leading-and-trailing-whitespace-from-a-string-column}

```sql
SELECT trim(description) AS leading_and_trailing_whitespace_removed
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Para obtener más información, consulte [https://www.sqlite.org/lang_corefunc.html#trim](https://www.sqlite.org/lang_corefunc.html#trim)
