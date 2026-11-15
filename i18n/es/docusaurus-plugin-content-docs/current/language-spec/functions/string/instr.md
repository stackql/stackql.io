---
title: INSTR
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Función INSTR en StackQL: la posición (a partir de 1) de la primera aparición de una cadena dentro de otra, o 0 si no se encuentra."
image: "/img/stackql-featured-image.png"
---
Devuelve la posición del carácter de la primera aparición de una cadena dentro de otra cadena; devuelve 0 si no se encuentra la cadena buscada.

Consulte también:  
[[` SELECT `]](/language-spec/select)

* * * 

## Sintaxis {#syntax}

```sql
SELECT INSTR(string, searchstring) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*string*__  
La cadena de origen en la que se busca.

__*searchstring*__  
La subcadena que se busca.

> Si *string* y *searchstring* son ambos BLOB, `instr(<string>, <searchstring>)` devuelve el número de bytes anteriores a la primera aparición de *searchstring* más uno, o 0 si *searchstring* no aparece en ninguna parte de *string*. Si ambos argumentos son distintos de `NULL` y no son BLOB, ambos se interpretan como cadenas. 

## Valor(es) devuelto(s) {#return-values}
Devuelve un entero.

> Si alguno de los valores *string* o *searchstring* es `NULL`, el resultado es `NULL`.

* * *

## Ejemplos {#examples}

### Buscar una cadena en un campo {#search-a-field-for-a-string}

```sql
SELECT INSTR(name, 'worker') as charpos
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
/*
returns 0 if the term 'worker' is not present in the 'name' column, otherwise returns the character position (starting from 1) of the first occurence of the term 'worker' in the 'name' column
*/
```

Para obtener más información, consulte [https://www.sqlite.org/lang_corefunc.html#instr](https://www.sqlite.org/lang_corefunc.html#instr)
