---
title: SUM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Función de agregación SUM en StackQL: el total de los valores distintos de NULL de una columna o agrupación, devuelto como entero cuando todas las entradas son enteros."
image: "/img/stackql-featured-image.png"
---
Devuelve la suma de todos los valores distintos de `NULL` de una columna o agrupación de columnas.  

Consulte también:  
[[` SELECT `]](/language-spec/select) [[` TOTAL `]](/language-spec/functions/aggregate/total)

* * * 

:::tip

Use la función [**TOTAL**](/language-spec/functions/aggregate/total) para sumar números de punto flotante o valores largos.

:::

## Sintaxis {#syntax}

```sql
SELECT SUM(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## Argumentos {#arguments}

__*columnExpression*__  
Una columna o expresión.

> Si todos los valores de *columnExpression* son `NULL`, `SUM` devuelve `NULL`.  

> `SUM` genera una excepción "integer overflow" si se produce un desbordamiento de enteros en algún momento del cálculo.  La función [`TOTAL`](/language-spec/functions/aggregate/total) nunca genera un desbordamiento de enteros.  

__*groupByColumn*__  
Una o varias columnas sobre las que se realizan operaciones de resumen o de agregación.  La cláusula `GROUP BY` devuelve una fila por cada agrupación de columnas.

## Valor(es) devuelto(s) {#return-values}

Devuelve un entero que representa la suma de *columnExpression* si todas las entradas distintas de `NULL` son enteros. Si alguna entrada no es un entero o es `NULL`, se devuelve un valor de punto flotante que puede ser una aproximación de la suma real.

* * *

## Ejemplos {#examples}

### Devolver la suma de una expresión de columna en todo un recurso {#return-the-sum-of-a-column-expression-over-an-entire-resource}

```sql
SELECT sum(json_array_length(disks)) as sum_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Devolver la suma de una expresión de columna agrupada por otra expresión de columna {#return-the-sum-of-a-column-expression-grouped-by-another-column-expression}

```sql
SELECT status, sum(json_array_length(disks)) as sum_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY status;
```

### Usar `SUM` como función de ventana para calcular totales acumulados {#use-sum-as-a-window-function-to-calculate-running-totals}

```sql
-- Calculate running total and percentage of contributions
SELECT
    login,
    contributions,
    SUM(contributions) OVER (ORDER BY contributions DESC) as running_total,
    SUM(contributions) OVER () as total_contributions,
    ROUND(100.0 * contributions / SUM(contributions) OVER (), 2) as pct_of_total
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

Para obtener más información, consulte [https://www.sqlite.org/lang_aggfunc.html#sumunc](https://www.sqlite.org/lang_aggfunc.html#sumunc).
