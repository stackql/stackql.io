---
title: REPLACE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Función REPLACE en StackQL: sustituir cada aparición de una subcadena dentro de una cadena por otra cadena."
image: "/img/stackql-featured-image.png"
---
Devuelve una cadena formada al sustituir cada aparición de una cadena por otra cadena.

Consulte también:  
[[` SELECT `]](/language-spec/select) 

* * * 

## Sintaxis {#syntax}

```sql
SELECT REPLACE(character_expression, pattern, replacement) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*character_expression*__  
Una cadena literal o una columna de cadena en la que desea realizar el reemplazo.

__*pattern*__  
La subcadena que se busca en la cadena original.

> Si *pattern* es una cadena vacía o no se encuentra en *character_expression*, se devuelve *character_expression* sin cambios. 

__*replacement*__  
La cadena de reemplazo.

> Si *replacement* no es inicialmente una cadena, se convierte en una cadena UTF-8 antes del procesamiento.

> Para las comparaciones se usa la secuencia de intercalación `BINARY`.

## Valor(es) devuelto(s) {#return-values}
Devuelve una cadena.

* * *

## Ejemplos {#examples}

### Reemplazar un patrón de cadena en una columna {#replace-a-string-pattern-in-a-column}

```sql
SELECT replace(name, 'prd', 'production') AS replacement_string
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Para obtener más información, consulte [https://www.sqlite.org/lang_corefunc.html#replace](https://www.sqlite.org/lang_corefunc.html#replace)
