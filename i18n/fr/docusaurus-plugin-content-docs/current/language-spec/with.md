---
title: WITH (CTE)
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "WITH dans StackQL : définir des expressions de table communes (CTE), jeux de résultats nommés temporaires limités à une seule instruction SELECT, INSERT, UPDATE ou DELETE."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Définit une expression de table commune (CTE) à utiliser dans une instruction `SELECT`, `INSERT`, `UPDATE` ou `DELETE` qui suit. Les CTE se comportent comme des jeux de résultats nommés temporaires qui n'existent que pendant la durée de la requête.

Voir aussi :
[[` SELECT `]](/language-spec/select) [[` INSERT `]](/language-spec/insert) [[` UPDATE `]](/language-spec/update) [[` DELETE `]](/language-spec/delete)

* * *

## Syntaxe {#syntax}

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

## Arguments {#arguments}

__*RECURSIVE*__
Mot-clé facultatif qui permet à la CTE de se référencer elle-même, ce qui autorise des requêtes récursives comme le parcours de données hiérarchiques.

__*cte_name*__
Nom attribué à la CTE. Ce nom peut être référencé dans la requête principale comme s'il s'agissait d'une table.

__*column_name*__
Facultatif. Noms des colonnes du jeu de résultats de la CTE. S'ils ne sont pas indiqués, les noms de colonnes sont dérivés de l'instruction SELECT.

__*select_statement*__
Requête qui définit le jeu de résultats de la CTE.

## Valeur(s) renvoyée(s) {#return-values}

Les CTE ne renvoient pas de valeurs directement. Elles définissent des jeux de résultats temporaires qui peuvent être référencés dans la requête principale.

* * *

## Exemples {#examples}

### CTE de base pour organiser des requêtes complexes {#basic-cte-for-organizing-complex-queries}

Une CTE peut simplifier des requêtes complexes en les décomposant en composants logiques nommés.

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

### Combiner des données de plusieurs sources avec `UNION ALL` {#combining-data-from-multiple-sources-with-union-all}

Les CTE sont utiles pour combiner des données de plusieurs sources avant d'effectuer une analyse.

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

### Utiliser des CTE avec des fonctions de fenêtrage pour le classement {#using-ctes-with-window-functions-for-ranking}

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

### Plusieurs CTE dans une seule requête {#multiple-ctes-in-a-single-query}

Vous pouvez définir plusieurs CTE séparées par des virgules.

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

## CTE récursives {#recursive-ctes}

Les CTE récursives permettent à une CTE de se référencer elle-même, ce qui rend possibles des requêtes qui parcourent des structures de données hiérarchiques ou de type graphe.

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

Les CTE récursives sont puissantes pour les données hiérarchiques comme les organigrammes, les systèmes de fichiers ou les arbres de dépendances. Incluez toujours une condition d'arrêt dans le cas récursif pour éviter les boucles infinies.

:::

## Bonnes pratiques {#best-practices}

1. **Utilisez des CTE pour la lisibilité** : décomposez les requêtes complexes en composants logiques nommés.
2. **Combinez-les avec des fonctions de fenêtrage** : les CTE fonctionnent bien avec les fonctions de fenêtrage pour l'analyse avancée.
3. **Enchaînez plusieurs CTE** : référencez des CTE précédentes dans les suivantes pour construire une logique complexe étape par étape.
4. **Tenez compte des performances** : si les CTE améliorent la lisibilité, des CTE très volumineuses peuvent dégrader les performances. Testez avec vos volumes de données réels.

Pour plus d'informations, consultez [https://sqlite.org/lang_with.html](https://sqlite.org/lang_with.html).
