---
title: GROUP_CONCAT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Función de agregación GROUP_CONCAT en StackQL: concatenar los valores distintos de NULL de una columna o agrupación en una cadena con un separador opcional."
image: "/img/stackql-featured-image.png"
---
Devuelve una cadena que es la concatenación de todos los valores distintos de `NULL` de una columna de entrada o agrupación.  

> El orden de los elementos concatenados es arbitrario.

Consulte también:  
[[` SELECT `]](/language-spec/select)

* * * 

## Sintaxis {#syntax}

```sql
SELECT GROUP_CONCAT(columnExpression [, separator]) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## Argumentos {#arguments}

__*columnExpression*__  
Una columna o expresión.  Si se especifica __*__, se cuentan todos los registros del recurso o de la agrupación.  

> Al menos un valor de *columnExpression* debe ser distinto de `NULL`; de lo contrario, se devuelve `NULL`.

__*separator*__  
Separador que se usa para el resultado concatenado; si no se especifica *separator*, se usa el valor predeterminado `,`.

__*groupByColumn*__  
Una o varias columnas sobre las que se realizan operaciones de resumen o de agregación.  La cláusula `GROUP BY` devuelve una fila por cada agrupación de columnas.

## Valor(es) devuelto(s) {#return-values}

Devuelve una cadena como lista de valores separados por *separator* o `,`.

* * *

## Ejemplos {#examples}

### Devolver una lista de valores de una columna en todo un recurso {#return-a-list-of-values-for-a-column-over-an-entire-resource}

```sql
SELECT group_concat(name) as instance_list
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Devolver una lista de valores de una expresión de columna agrupada por otra expresión de columna {#return-a-list-of-values-for-a-column-expression-grouped-by-another-column-expression}

```sql
SELECT tags, group_concat(name) as instance_list
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY json_extract(tags, '$.instanceType');
```

Para obtener más información, consulte [https://www.sqlite.org/lang_aggfunc.html#group_concat](https://www.sqlite.org/lang_aggfunc.html#group_concat).
