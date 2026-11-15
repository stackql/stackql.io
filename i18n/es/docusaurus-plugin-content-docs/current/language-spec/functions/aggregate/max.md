---
title: MAX
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Función de agregación MAX en StackQL: el mayor valor de una columna o agrupación de columnas."
image: "/img/stackql-featured-image.png"
---
Devuelve el valor máximo a partir de una columna de entrada o una agrupación de columnas.  

Consulte también:  
[[` SELECT `]](/language-spec/select) 

* * * 

## Sintaxis {#syntax}

```sql
/* aggregate function */
SELECT MAX(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```
```sql
/* scalar (multi-argument) function */
SELECT MAX(scalarExpression1, scalarExpressionN, ...) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

### Función de agregación {#aggregate-function}

__*columnExpression*__  
Una columna o expresión, incluidos operadores (`+`, `-`, ...).

> La función `MAX` devuelve el mayor valor de la columna indicada según el tipo de datos de la columna; por ejemplo, una operación `MAX` sobre una columna de enteros devuelve el mayor entero de esa columna o agrupación de columnas, mientras que una operación `MAX` sobre una columna o agrupación de columnas de cadenas devuelve el mayor valor ASCII (generalmente el primer valor al ordenar en orden lexicográfico, con algunas diferencias).

__*groupByColumn*__  
Una o varias columnas sobre las que se realizan operaciones de resumen o de agregación.  La cláusula `GROUP BY` devuelve una fila por cada agrupación de columnas.

> La función `MAX` ignora los valores `NULL`.

> La función `MAX` devuelve `NULL` si todos los valores del grupo son `NULL`.

### Función escalar {#scalar-function}

__*scalarExpression*__  
Una lista de columnas o expresiones (2 o más) a partir de la cual se determina y se devuelve el mayor valor. 

> La función escalar `MAX` devuelve `NULL` si algún argumento es `NULL`. 

> La función `MAX` escalar o de varios argumentos busca en sus argumentos, de izquierda a derecha, un argumento que defina una función de intercalación y usa esa función de intercalación para todas las comparaciones de cadenas. 

> Si ninguno de los argumentos de la función escalar `MAX` define una función de intercalación, se usa la función de intercalación `BINARY`. 

> Si solo se proporciona un argumento, se invoca la función de agregación `MAX`.

## Valor(es) devuelto(s) {#return-values}

Devuelve el valor máximo según el tipo de datos de entrada (o la agrupación).

* * *

## Ejemplos {#examples}

### Devolver el valor máximo de una columna {#return-the-maximum-value-for-a-column}

```sql
SELECT name, location, max(timeCreated) 
FROM google.storage.buckets WHERE project = 'stackql';
```

### Devolver el valor máximo para cada valor de una columna {#return-the-maximum-value-for-each-value-in-a-column}

```sql
SELECT name, location,
max(round(julianday('now')-julianday(timeCreated))) as age
FROM google.storage.buckets WHERE project = 'stackql'
GROUP BY location;
```

### Devolver el valor máximo de una lista de valores con la función escalar `max` {#return-the-maximum-value-from-a-list-of-values-using-the-scalar-max-function}

```sql
SELECT max(json_extract(disks, '$[0].diskSizeGb'),json_extract(disks, '$[1].diskSizeGb')) as largest_disk
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Usar `MAX` como función de ventana para encontrar los valores máximos dentro de las particiones {#use-max-as-a-window-function-to-find-maximum-values-within-partitions}

```sql
-- Find the most recent issue in each state
SELECT
    number,
    title,
    state,
    created_at,
    MAX(created_at) OVER (PARTITION BY state) as latest_in_state
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY state, created_at DESC;
```

Para obtener más información, consulte [https://www.sqlite.org/lang_aggfunc.html#max_agg](https://www.sqlite.org/lang_aggfunc.html#max_agg) o [https://www.sqlite.org/lang_corefunc.html#max_scalar](https://www.sqlite.org/lang_corefunc.html#max_scalar)
