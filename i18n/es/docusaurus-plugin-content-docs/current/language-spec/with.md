---
title: WITH (CTE)
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "WITH en StackQL: definir expresiones de tabla comunes (CTE), conjuntos de resultados temporales con nombre limitados a una sola instrucción SELECT, INSERT, UPDATE o DELETE."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Define una expresión de tabla común (CTE) para usarla en una instrucción `SELECT`, `INSERT`, `UPDATE` o `DELETE` posterior. Las CTE actúan como conjuntos de resultados temporales con nombre que solo existen mientras dura la consulta.

Consulte también:
[[` SELECT `]](/language-spec/select) [[` INSERT `]](/language-spec/insert) [[` UPDATE `]](/language-spec/update) [[` DELETE `]](/language-spec/delete)

* * *

## Sintaxis {#syntax}

*withClause::=*

<RailroadDiagram
type="withClause"
/>

*cteDefinition::=*

<RailroadDiagram
type="cteDefinition"
/>

&nbsp;
&nbsp;

```sql
WITH [RECURSIVE] cte_name [(column_name, ...)] AS (
    select_statement
)
[, cte_name [(column_name, ...)] AS (
    select_statement
)]
SELECT ... FROM cte_name ...;
```

## Argumentos {#arguments}

__*RECURSIVE*__
Palabra clave opcional que permite que la CTE haga referencia a sí misma, lo que posibilita consultas recursivas como el recorrido de datos jerárquicos.

__*cte_name*__
El nombre asignado a la CTE. Se puede hacer referencia a este nombre en la consulta principal como si fuera una tabla.

__*column_name*__
Opcional. Nombres de columna para el conjunto de resultados de la CTE. Si no se especifican, los nombres de columna se derivan de la instrucción SELECT.

__*select_statement*__
La consulta que define el conjunto de resultados de la CTE.

## Valor(es) devuelto(s) {#return-values}

Las CTE no devuelven valores directamente. Definen conjuntos de resultados temporales a los que se puede hacer referencia en la consulta principal.

* * *

## Ejemplos {#examples}

### CTE básica para organizar consultas complejas {#basic-cte-for-organizing-complex-queries}

Una CTE puede simplificar consultas complejas al dividirlas en componentes lógicos con nombre.

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

### Combinar datos de varios orígenes con `UNION ALL` {#combining-data-from-multiple-sources-with-union-all}

Las CTE resultan útiles para combinar datos de varios orígenes antes de realizar un análisis.

```sql
-- Rank total contributions across multiple repos
WITH all_contributors AS (
    SELECT login, contributions, html_url
    FROM github.repos.contributors
    WHERE owner = 'stackql' AND repo = 'stackql'
    UNION ALL
    SELECT login, contributions, html_url
    FROM github.repos.contributors
    WHERE owner = 'stackql' AND repo = 'stackql-deploy'
    UNION ALL
    SELECT login, contributions, html_url
    FROM github.repos.contributors
    WHERE owner = 'stackql' AND repo = 'stackql-provider-registry'
)
SELECT
    DENSE_RANK() OVER (ORDER BY SUM(contributions) DESC) as rank,
    login,
    SUM(contributions) as total_contributions,
    MAX(html_url) as html_url
FROM all_contributors
GROUP BY login
ORDER BY total_contributions DESC;
```

### Usar CTE con funciones de ventana para clasificar {#using-ctes-with-window-functions-for-ranking}

```sql
-- Rank contributors within each repo using CTE and window functions
WITH repo_contributors AS (
    SELECT
        repo,
        login,
        contributions,
        RANK() OVER (PARTITION BY repo ORDER BY contributions DESC) as rank_in_repo,
        SUM(contributions) OVER (PARTITION BY repo) as repo_total_contributions
    FROM (
        SELECT 'stackql' as repo, login, contributions
        FROM github.repos.contributors
        WHERE owner = 'stackql' AND repo = 'stackql'
        UNION ALL
        SELECT 'stackql-provider-registry' as repo, login, contributions
        FROM github.repos.contributors
        WHERE owner = 'stackql' AND repo = 'stackql-provider-registry'
    ) t
)
SELECT
    repo,
    login,
    contributions,
    rank_in_repo
FROM repo_contributors
ORDER BY repo, rank_in_repo;
```

### Varias CTE en una sola consulta {#multiple-ctes-in-a-single-query}

Puede definir varias CTE separadas por comas.

```sql
-- Analyze issues and releases together
WITH open_issues AS (
    SELECT number, title, created_at
    FROM github.issues.issues
    WHERE owner = 'stackql'
      AND repo = 'stackql'
      AND state = 'open'
),
recent_releases AS (
    SELECT tag_name, published_at
    FROM github.repos.releases
    WHERE owner = 'stackql'
      AND repo = 'stackql'
    ORDER BY published_at DESC
    LIMIT 5
)
SELECT
    'Open Issues' as category,
    COUNT(*) as count
FROM open_issues
UNION ALL
SELECT
    'Recent Releases' as category,
    COUNT(*) as count
FROM recent_releases;
```

## CTE recursivas {#recursive-ctes}

Las CTE recursivas permiten que una CTE haga referencia a sí misma, lo que posibilita consultas que recorren estructuras de datos jerárquicas o de tipo grafo.

```sql
WITH RECURSIVE cte_name (columns) AS (
    -- Base case (anchor member)
    SELECT ...
    UNION ALL
    -- Recursive case (references cte_name)
    SELECT ... FROM cte_name WHERE ...
)
SELECT * FROM cte_name;
```

:::tip

Las CTE recursivas son potentes para datos jerárquicos como organigramas, sistemas de archivos o árboles de dependencias. Incluya siempre una condición de terminación en el caso recursivo para evitar bucles infinitos.

:::

## Prácticas recomendadas {#best-practices}

1. **Use CTE para mejorar la legibilidad**: divida las consultas complejas en componentes lógicos con nombre.
2. **Combínelas con funciones de ventana**: las CTE funcionan bien con las funciones de ventana para análisis avanzados.
3. **Encadene varias CTE**: haga referencia a CTE anteriores en las posteriores para construir lógica compleja paso a paso.
4. **Tenga en cuenta el rendimiento**: aunque las CTE mejoran la legibilidad, las CTE muy grandes pueden afectar al rendimiento. Haga pruebas con sus volúmenes de datos reales.

Para obtener más información, consulte [https://sqlite.org/lang_with.html](https://sqlite.org/lang_with.html).
