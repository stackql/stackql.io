---
title: MIN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Función de agregación MIN en StackQL: el menor valor de una columna o agrupación de columnas."
image: "/img/stackql-featured-image.png"
---
Devuelve el valor mínimo a partir de una columna de entrada o una agrupación de columnas.  

Consulte también:  
[[` SELECT `]](/language-spec/select) 

* * * 

## Sintaxis {#syntax}

```sql
/* aggregate function */
SELECT MIN(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```
```sql
/* scalar (multi-argument) function */
SELECT MIN(scalarExpression1, scalarExpressionN, ...) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

### Función de agregación {#aggregate-function}

__*columnExpression*__  
Una columna o expresión, incluidos operadores (`+`, `-`, ...).

> La función `MIN` devuelve el menor valor de la columna indicada según el tipo de datos de la columna; por ejemplo, una operación `MIN` sobre una columna de enteros devuelve el menor entero de esa columna o agrupación de columnas, mientras que una operación `MIN` sobre una columna o agrupación de columnas de cadenas devuelve el valor que queda en primer lugar al ordenar, comparando las cadenas con la función de intercalación `BINARY` (byte a byte, por lo que las letras mayúsculas se ordenan antes que las minúsculas).

__*groupByColumn*__  
Una o varias columnas sobre las que se realizan operaciones de resumen o de agregación.  La cláusula `GROUP BY` devuelve una fila por cada agrupación de columnas.

> La función `MIN` ignora los valores `NULL`.

> La función `MIN` devuelve `NULL` si todos los valores del grupo son `NULL`.

### Función escalar {#scalar-function}

__*scalarExpression*__  
Una lista de columnas o expresiones (2 o más) a partir de la cual se determina y se devuelve el menor valor. 

> La función escalar `MIN` devuelve `NULL` si algún argumento es `NULL`. 

> La función `MIN` escalar o de varios argumentos busca en sus argumentos, de izquierda a derecha, un argumento que defina una función de intercalación y usa esa función de intercalación para todas las comparaciones de cadenas. 

> Si ninguno de los argumentos de la función escalar `MIN` define una función de intercalación, se usa la función de intercalación `BINARY`. 

> Si solo se proporciona un argumento, se invoca la función de agregación `MIN`.

## Valor(es) devuelto(s) {#return-values}

Devuelve el valor mínimo según el tipo de datos de entrada (o la agrupación).

* * *

## Ejemplos {#examples}

### Devolver el valor mínimo de una columna {#return-the-minimum-value-for-a-column}

```sql
SELECT name, location, min(timeCreated) 
FROM google.storage.buckets WHERE project = 'stackql';
```

### Devolver el valor mínimo para cada valor de una columna {#return-the-minimum-value-for-each-value-in-a-column}

```sql
SELECT name, location,
min(round(julianday('now')-julianday(timeCreated))) as age
FROM google.storage.buckets WHERE project = 'stackql'
GROUP BY location;
```

### Devolver el valor mínimo de una lista de valores con la función escalar `min` {#return-the-minimum-value-from-a-list-of-values-using-the-scalar-min-function}

```sql
SELECT min(json_extract(disks, '$[0].diskSizeGb'),json_extract(disks, '$[1].diskSizeGb')) as smallest_disk
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Usar `MIN` como función de ventana para encontrar los valores mínimos dentro de las particiones {#use-min-as-a-window-function-to-find-minimum-values-within-partitions}

```sql
-- Find the earliest issue in each state
SELECT
    number,
    title,
    state,
    created_at,
    MIN(created_at) OVER (PARTITION BY state) as earliest_in_state
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY state, created_at;
```

Para obtener más información, consulte [https://www.sqlite.org/lang_aggfunc.html#min_agg](https://www.sqlite.org/lang_aggfunc.html#min_agg) o [https://www.sqlite.org/lang_corefunc.html#min_scalar](https://www.sqlite.org/lang_corefunc.html#min_scalar)
