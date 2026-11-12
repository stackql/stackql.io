---
title: Fonctions de fenêtrage
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Fonctions de fenêtrage dans StackQL : rangs, totaux cumulés, décalages et distributions sur des lignes liées, avec PARTITION BY, ORDER BY et les clauses de cadre."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Les fonctions de fenêtrage effectuent des calculs sur un ensemble de lignes liées à la ligne courante. Contrairement aux fonctions d'agrégation, qui renvoient un seul résultat pour un groupe de lignes, les fonctions de fenêtrage renvoient une valeur pour chaque ligne en tenant compte d'une "fenêtre" de lignes liées.

Voir aussi :
[[` SELECT `]](/language-spec/select) [[` WITH (CTE) `]](/language-spec/with) [[ Référence des fonctions de fenêtrage ]](/language-spec/functions/window/row_number)

* * *

## Syntaxe {#syntax}

Les fonctions de fenêtrage utilisent la clause `OVER` pour définir la spécification de fenêtre :

```sql
SELECT <windowFunction> OVER ( <windowSpec> ) FROM <multipartIdentifier>;
```

*windowFunctionCall::=*

<RailroadDiagram 
type="windowFunctionCall"
/>

### Fonction de fenêtrage {#window-function}

Les fonctions de fenêtrage sont des fonctions spécialisées qui effectuent des calculs sur un ensemble de lignes défini par la spécification de fenêtre. Il peut s'agir de fonctions de fenêtrage dédiées (comme `ROW_NUMBER` ou `LAG`) ou de fonctions d'agrégation (comme `SUM` ou `COUNT`) utilisées avec une clause `OVER`.

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

### Spécification de fenêtre {#window-specification}

La spécification de fenêtre définit comment les lignes sont partitionnées, ordonnées et cadrées pour le calcul de la fonction de fenêtrage. `PARTITION BY` répartit les lignes en groupes, `ORDER BY` détermine l'ordre au sein de chaque partition, et la clause de cadre, facultative, précise quelles lignes, par rapport à la ligne courante, sont incluses dans le calcul.

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

## Fonctions de fenêtrage disponibles {#available-window-functions}

| Catégorie | Fonctions |
|----------|-----------|
| **Classement** | [__`ROW_NUMBER()`__](/language-spec/functions/window/row_number), [__`RANK()`__](/language-spec/functions/window/rank), [__`DENSE_RANK()`__](/language-spec/functions/window/dense_rank), [__`NTILE()`__](/language-spec/functions/window/ntile) |
| **Décalage** | [__`LAG()`__](/language-spec/functions/window/lag), [__`LEAD()`__](/language-spec/functions/window/lead), [__`FIRST_VALUE()`__](/language-spec/functions/window/first_value), [__`LAST_VALUE()`__](/language-spec/functions/window/last_value), [__`NTH_VALUE()`__](/language-spec/functions/window/nth_value) |
| **Distribution** | [__`PERCENT_RANK()`__](/language-spec/functions/window/percent_rank), [__`CUME_DIST()`__](/language-spec/functions/window/cume_dist) |
| **Agrégation** | [__`SUM()`__](/language-spec/functions/aggregate/sum), [__`COUNT()`__](/language-spec/functions/aggregate/count), [__`AVG()`__](/language-spec/functions/aggregate/avg), [__`MIN()`__](/language-spec/functions/aggregate/min), [__`MAX()`__](/language-spec/functions/aggregate/max) avec la clause __`OVER`__ |

* * *

## Exemples {#examples}

### Classer les lignes avec `ROW_NUMBER`, `RANK` et `DENSE_RANK` {#ranking-with-row_number-rank-and-dense_rank}

Utilisez les fonctions de classement pour attribuer un rang aux lignes en fonction des valeurs de colonnes.

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

### Totaux cumulés et pourcentages {#running-totals-and-percentages}

Les fonctions d'agrégation comme `SUM` et `COUNT` peuvent être utilisées comme fonctions de fenêtrage pour calculer des totaux cumulés et des pourcentages.

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

### Comparer des lignes avec `LAG` et `LEAD` {#comparing-rows-with-lag-and-lead}

Utilisez `LAG` et `LEAD` pour accéder aux valeurs des lignes précédentes ou suivantes.

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

### Créer des groupes avec `NTILE` {#creating-groups-with-ntile}

Utilisez `NTILE` pour répartir les lignes en un nombre donné de groupes.

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

### Utiliser `PARTITION BY` pour une analyse par groupe {#using-partition-by-for-grouped-analysis}

Utilisez `PARTITION BY` pour effectuer des calculs de fenêtrage au sein de groupes.

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

### Utiliser des cadres de fenêtre {#using-window-frames}

Indiquez une clause de cadre pour contrôler quelles lignes sont incluses dans le calcul de fenêtrage.

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

Pour une documentation détaillée de chaque fonction de fenêtrage, consultez la [Référence des fonctions de fenêtrage](/language-spec/functions/window/row_number).

Pour plus d'informations, consultez [https://sqlite.org/windowfunctions.html](https://sqlite.org/windowfunctions.html).
