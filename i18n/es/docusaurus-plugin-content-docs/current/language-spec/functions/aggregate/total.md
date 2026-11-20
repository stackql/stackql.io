---
title: TOTAL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Función de agregación TOTAL en StackQL: el total de los valores distintos de NULL de una columna o agrupación, devuelto siempre como número de punto flotante."
image: "/img/stackql-featured-image.png"
---
Devuelve la suma de todos los valores distintos de `NULL` de una columna o agrupación de columnas.  

Consulte también:  
[[` SELECT `]](/language-spec/select) [[` SUM `]](/language-spec/functions/aggregate/sum)

* * * 

## Sintaxis {#syntax}

```sql
SELECT TOTAL(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## Argumentos {#arguments}

__*columnExpression*__  
Una columna o expresión.

> Si todos los valores de *columnExpression* son `NULL`, `TOTAL` devuelve `0.0`.  

__*groupByColumn*__  
Una o varias columnas sobre las que se realizan operaciones de resumen o de agregación.  La cláusula `GROUP BY` devuelve una fila por cada agrupación de columnas.

## Valor(es) devuelto(s) {#return-values}

Devuelve un valor de punto flotante que representa la suma de *columnExpression*.

* * *

## Ejemplos {#examples}

### Devolver la suma de una expresión de columna en todo un recurso {#return-the-sum-of-a-column-expression-over-an-entire-resource}

```sql
SELECT total(json_array_length(disks)) as total_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Devolver la suma de una expresión de columna agrupada por otra expresión de columna {#return-the-sum-of-a-column-expression-grouped-by-another-column-expression}

```sql
SELECT status, total(json_array_length(disks)) as total_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY status;
```

Para obtener más información, consulte [https://www.sqlite.org/lang_aggfunc.html#sumunc](https://www.sqlite.org/lang_aggfunc.html#sumunc).
