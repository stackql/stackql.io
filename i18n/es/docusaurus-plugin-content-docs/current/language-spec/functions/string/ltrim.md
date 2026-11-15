---
title: LTRIM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Función LTRIM en StackQL: eliminar los espacios en blanco, o un conjunto de caracteres dado, del lado izquierdo de una cadena."
image: "/img/stackql-featured-image.png"
---
Devuelve una cadena formada al eliminar los espacios en blanco o los caracteres indicados que aparecen en el lado izquierdo de una cadena de entrada.  

Consulte también:  
[[` SELECT `]](/language-spec/select) [[` RTRIM `]](/language-spec/functions/string/rtrim) [[` TRIM `]](/language-spec/functions/string/trim) 

* * * 

## Sintaxis {#syntax}

```sql
SELECT LTRIM(character_expression[, character ]) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*character_expression*__  
Una cadena literal o una columna de cadena.

__*character*__  
Un carácter opcional que se elimina si se encuentra al principio de *character_expression*.

## Valor(es) devuelto(s) {#return-values}
Devuelve una cadena.

* * *

## Ejemplos {#examples}

### Eliminar los espacios en blanco iniciales de una columna de cadena {#remove-leading-whitespace-from-a-string-column}

```sql
SELECT ltrim(description) AS leading_whitespace_removed
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Para obtener más información, consulte [https://www.sqlite.org/lang_corefunc.html#ltrim](https://www.sqlite.org/lang_corefunc.html#ltrim)
