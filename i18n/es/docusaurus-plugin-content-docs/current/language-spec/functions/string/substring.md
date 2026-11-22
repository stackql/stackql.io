---
title: SUBSTRING
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Función SUBSTRING en StackQL (alias SUBSTR): devolver parte de una cadena o de un valor binario según la posición inicial y la longitud."
image: "/img/stackql-featured-image.png"
---
Devuelve parte de una expresión de caracteres o binaria.  `SUBSTR` es un alias de `SUBSTRING`.

Consulte también:  
[[` SELECT `]](/language-spec/select) [[ Tipos de datos ]](/language-spec/data-types)

* * * 

:::info

Para extraer un campo de un objeto JSON, use la función [**JSON_EXTRACT**](/language-spec/functions/json/json_extract).

:::

## Sintaxis {#syntax}

```sql
SELECT SUBSTRING(expression, start[, length]) FROM <multipartIdentifier>;
SELECT SUBSTR(expression, start[, length]) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*expression*__  
Una cadena o un BLOB.

> Si *expression* es una cadena, los índices de caracteres hacen referencia a caracteres UTF-8 reales. Si *expression* es un BLOB, los índices hacen referencia a bytes.

__*start*__  
Un entero que representa el primer carácter o byte de la subsecuencia de la expresión de entrada que se devuelve.  Si *start* es negativo, el primer carácter de la subcadena se encuentra contando desde la derecha en lugar de desde la izquierda.

> El carácter situado más a la izquierda de *expression* es el número 1.

__*length*__  
Un valor entero opcional que representa la longitud de la subsecuencia que se devuelve después del valor *start*.  Si se omite *length*, se devuelven todos los caracteres hasta el final de la cadena o del BLOB especificado por *expression*.

> Si *length* es negativo, se devuelven los caracteres que preceden a *start*, tantos como el valor absoluto de *length*: `SUBSTR('abcdef', 4, -2)` devuelve `bc`.

## Valor(es) devuelto(s) {#return-values}

Devuelve una cadena o un valor BLOB según el tipo de *expression*.

* * *

## Ejemplos {#examples}

### Devolver los primeros *n* caracteres de una columna {#return-the-first-n-characters-from-a-column}

```sql
SELECT SUBSTRING(name, 1, 5) as "first five chars" 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Truncar los primeros *n* caracteres de una columna {#truncate-the-first-n-characters-from-a-column}

```sql
SELECT SUBSTR(name, 5) as "from char 5" 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Devolver los últimos *n* caracteres de una columna {#return-the-last-n-characters-from-a-column}

```sql
SELECT SUBSTRING(name, -5) as "last five chars" 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Para obtener más información, consulte [https://www.sqlite.org/lang_corefunc.html#substr](https://www.sqlite.org/lang_corefunc.html#substr)
