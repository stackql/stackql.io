---
title: Funciones de ventana
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Funciones de ventana en StackQL: rangos, totales acumulados, desplazamientos y distribuciones sobre filas relacionadas, con PARTITION BY, ORDER BY y cláusulas de marco."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Las funciones de ventana realizan cálculos sobre un conjunto de filas relacionadas con la fila actual. A diferencia de las funciones de agregación, que devuelven un único resultado para un grupo de filas, las funciones de ventana devuelven un valor para cada fila teniendo en cuenta una "ventana" de filas relacionadas.

Consulte también:
[[` SELECT `]](/language-spec/select) [[` WITH (CTE) `]](/language-spec/with) [[ Referencia de funciones de ventana ]](/language-spec/functions/window/row_number)

* * *

## Sintaxis {#syntax}

Las funciones de ventana usan la cláusula `OVER` para definir la especificación de ventana:

```sql
SELECT <windowFunction> OVER ( <windowSpec> ) FROM <multipartIdentifier>;
```

*windowFunctionCall::=*

<RailroadDiagram 
type="windowFunctionCall"
/>

### Función de ventana {#window-function}

Las funciones de ventana son funciones especializadas que realizan cálculos sobre un conjunto de filas definido por la especificación de ventana. Pueden ser funciones de ventana dedicadas (como `ROW_NUMBER` o `LAG`) o funciones de agregación (como `SUM` o `COUNT`) usadas con una cláusula `OVER`.

```sql
{ <aggregateFunction> | ROW_NUMBER() | RANK() | DENSE_RANK() | PERCENT_RANK() 
  | CUME_DIST() | NTILE(<int>) | LAG(<expr> [,<offset> [,<default>]]) 
  | LEAD(<expr> [,<offset> [,<default>]]) | FIRST_VALUE(<expr>) 
  | LAST_VALUE(<expr>) | NTH_VALUE(<expr>, <int>) }
```  

*windowFunction::=*

<RailroadDiagram 
type="windowFunction"
/>

### Especificación de ventana {#window-specification}

La especificación de ventana define cómo se particionan, ordenan y enmarcan las filas para el cálculo de la función de ventana. `PARTITION BY` divide las filas en grupos, `ORDER BY` determina la secuencia dentro de cada partición y la cláusula de marco opcional especifica qué filas, en relación con la fila actual, se incluyen en el cálculo.

```sql
[ <windowName> ]
[ PARTITION BY <fieldList> ]
[ ORDER BY <fieldList> [ ASC | DESC ] ]
[ { ROWS | RANGE | GROUPS } 
    { UNBOUNDED PRECEDING | <int> PRECEDING | CURRENT ROW }
  | { ROWS | RANGE | GROUPS } BETWEEN 
    { UNBOUNDED PRECEDING | <int> PRECEDING | CURRENT ROW | <int> FOLLOWING }
    AND
    { <int> PRECEDING | CURRENT ROW | <int> FOLLOWING | UNBOUNDED FOLLOWING } ]
```

*windowSpec::=*

<RailroadDiagram 
type="windowSpec"
/>

## Funciones de ventana disponibles {#available-window-functions}

| Categoría | Funciones |
|----------|-----------|
| **Clasificación** | [__`ROW_NUMBER()`__](/language-spec/functions/window/row_number), [__`RANK()`__](/language-spec/functions/window/rank), [__`DENSE_RANK()`__](/language-spec/functions/window/dense_rank), [__`NTILE()`__](/language-spec/functions/window/ntile) |
| **Desplazamiento** | [__`LAG()`__](/language-spec/functions/window/lag), [__`LEAD()`__](/language-spec/functions/window/lead), [__`FIRST_VALUE()`__](/language-spec/functions/window/first_value), [__`LAST_VALUE()`__](/language-spec/functions/window/last_value), [__`NTH_VALUE()`__](/language-spec/functions/window/nth_value) |
| **Distribución** | [__`PERCENT_RANK()`__](/language-spec/functions/window/percent_rank), [__`CUME_DIST()`__](/language-spec/functions/window/cume_dist) |
| **Agregación** | [__`SUM()`__](/language-spec/functions/aggregate/sum), [__`COUNT()`__](/language-spec/functions/aggregate/count), [__`AVG()`__](/language-spec/functions/aggregate/avg), [__`MIN()`__](/language-spec/functions/aggregate/min), [__`MAX()`__](/language-spec/functions/aggregate/max) con la cláusula __`OVER`__ |

* * *

## Ejemplos {#examples}

### Clasificar con `ROW_NUMBER`, `RANK` y `DENSE_RANK` {#ranking-with-row_number-rank-and-dense_rank}

Use las funciones de clasificación para asignar rangos a las filas según los valores de las columnas.

```sql
-- Rank contributors by contribution count
SELECT
    login,
    contributions,
    ROW_NUMBER() OVER (ORDER BY contributions DESC) as row_num,
    RANK() OVER (ORDER BY contributions DESC) as rank,
    DENSE_RANK() OVER (ORDER BY contributions DESC) as dense_rank
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

### Totales acumulados y porcentajes {#running-totals-and-percentages}

Las funciones de agregación como `SUM` y `COUNT` pueden usarse como funciones de ventana para calcular totales acumulados y porcentajes.

```sql
-- Running total and percentage of contributions
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

### Comparar filas con `LAG` y `LEAD` {#comparing-rows-with-lag-and-lead}

Use `LAG` y `LEAD` para acceder a valores de filas anteriores o posteriores.

```sql
-- Compare each release to previous release
SELECT
    tag_name,
    name,
    published_at,
    LAG(tag_name, 1) OVER (ORDER BY published_at) as previous_release,
    LEAD(tag_name, 1) OVER (ORDER BY published_at) as next_release,
    julianday(published_at) - julianday(LAG(published_at, 1) OVER (ORDER BY published_at)) as days_since_last_release
FROM github.repos.releases
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY published_at;
```

### Crear grupos con `NTILE` {#creating-groups-with-ntile}

Use `NTILE` para dividir las filas en un número especificado de grupos.

```sql
-- Group contributors into quartiles
SELECT
    login,
    contributions,
    NTILE(4) OVER (ORDER BY contributions DESC) as quartile,
    CASE NTILE(4) OVER (ORDER BY contributions DESC)
        WHEN 1 THEN 'Top Contributors'
        WHEN 2 THEN 'Active Contributors'
        WHEN 3 THEN 'Moderate Contributors'
        WHEN 4 THEN 'Occasional Contributors'
    END as tier
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

### Usar `PARTITION BY` para análisis agrupados {#using-partition-by-for-grouped-analysis}

Use `PARTITION BY` para realizar cálculos de ventana dentro de grupos.

```sql
-- Track cumulative issues by state
SELECT
    number,
    title,
    state,
    created_at,
    ROW_NUMBER() OVER (ORDER BY created_at) as issue_sequence,
    COUNT(*) OVER (ORDER BY created_at) as cumulative_issues,
    COUNT(*) OVER (PARTITION BY state ORDER BY created_at) as cumulative_by_state
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY created_at;
```

### Usar marcos de ventana {#using-window-frames}

Especifique una cláusula de marco para controlar qué filas se incluyen en el cálculo de la ventana.

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
    ROUND(AVG(commits_this_week) OVER (ORDER BY week ROWS BETWEEN 3 PRECEDING AND CURRENT ROW), 1) as four_week_moving_avg,
    SUM(commits_this_week) OVER (ORDER BY week) as cumulative_commits
FROM weekly_totals
ORDER BY week;
```

Para obtener documentación detallada de cada función de ventana, consulte la [Referencia de funciones de ventana](/language-spec/functions/window/row_number).

Para obtener más información, consulte [https://sqlite.org/windowfunctions.html](https://sqlite.org/windowfunctions.html).
