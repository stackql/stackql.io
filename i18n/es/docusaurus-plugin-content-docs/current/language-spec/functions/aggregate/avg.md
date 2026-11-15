---
title: AVG
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Función de agregación AVG en StackQL: el promedio de los valores distintos de NULL de una columna o agrupación."
image: "/img/stackql-featured-image.png"
---
Devuelve el valor promedio de todos los valores distintos de `NULL` de una columna o agrupación de columnas.  

Consulte también:  
[[` SELECT `]](/language-spec/select)

* * * 

## Sintaxis {#syntax}

```sql
SELECT AVG(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## Argumentos {#arguments}

__*columnExpression*__  
Una columna o expresión.

> Los valores de cadena y BLOB que no se pueden representar como valores numéricos se interpretan como `0`.  

> Al menos un valor de *columnExpression* debe ser distinto de `NULL`; de lo contrario, se devuelve `NULL`.

__*groupByColumn*__  
Una o varias columnas sobre las que se realizan operaciones de resumen o de agregación.  La cláusula `GROUP BY` devuelve una fila por cada agrupación de columnas.

## Valor(es) devuelto(s) {#return-values}

Devuelve un valor de punto flotante que representa el promedio de *columnExpression*.

* * *

## Ejemplos {#examples}

### Devolver el promedio de una expresión de columna en todo un recurso {#return-the-average-of-a-column-expression-over-an-entire-resource}

```sql
SELECT avg(json_array_length(disks)) as avg_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Devolver el promedio de una expresión de columna agrupada por otra expresión de columna {#return-the-average-of-a-column-expression-grouped-by-another-column-expression}

```sql
SELECT tags, avg(json_array_length(disks)) as avg_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY json_extract(tags, '$.instanceType');
```

### Usar `AVG` como función de ventana para calcular promedios móviles {#use-avg-as-a-window-function-to-calculate-moving-averages}

```sql
-- Calculate moving average of commit activity
WITH weekly_totals AS (
    SELECT
        week,
        SUM(json_each.value) as commits_this_week
    FROM github.repos.stats_commit_activity, JSON_EACH(days)
    WHERE owner = 'stackql'
      AND repo = 'stackql'
    GROUP BY week
)
SELECT
    week,
    commits_this_week,
    ROUND(AVG(commits_this_week) OVER (ORDER BY week ROWS BETWEEN 3 PRECEDING AND CURRENT ROW), 1) as four_week_moving_avg
FROM weekly_totals
ORDER BY week;
```

Para obtener más información, consulte [https://www.sqlite.org/lang_aggfunc.html#avg](https://www.sqlite.org/lang_aggfunc.html#avg).
