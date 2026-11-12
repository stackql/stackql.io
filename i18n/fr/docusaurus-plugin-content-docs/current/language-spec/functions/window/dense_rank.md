---
title: DENSE_RANK
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Fonction de fenêtrage DENSE_RANK dans StackQL : le rang sans trous de la ligne courante dans sa partition, où les lignes à égalité partagent un rang et le suivant vient aussitôt."
image: "/img/stackql-featured-image.png"
---
Renvoie le rang de la ligne courante, sans trous. Il s'agit du numéro du groupe de pairs de la ligne courante au sein de sa partition. Les lignes ayant des valeurs égales pour les colonnes de tri reçoivent le même rang, et la valeur de rang suivante est toujours incrémentée de 1 (sans trous).

Les lignes sont numérotées à partir de 1 dans l'ordre défini par la clause `ORDER BY` de la définition de la fenêtre. En l'absence de clause `ORDER BY`, toutes les lignes sont considérées comme des pairs et cette fonction renvoie toujours 1.

Voir aussi :
[[` SELECT `]](/language-spec/select) [[` ROW_NUMBER `]](/language-spec/functions/window/row_number) [[` RANK `]](/language-spec/functions/window/rank)

* * *

## Syntaxe {#syntax}

```sql
SELECT DENSE_RANK() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*PARTITION BY column*__
Facultatif. Divise le jeu de résultats en partitions. La fonction `DENSE_RANK` est appliquée à chaque partition séparément.

__*ORDER BY column*__
Spécifie l'ordre dans lequel les rangs sont attribués. En l'absence de clause `ORDER BY`, toutes les lignes sont considérées comme des pairs et reçoivent le rang 1.

## Valeur(s) renvoyée(s) {#return-values}

Renvoie un entier représentant le rang dense de la ligne courante au sein de la partition, sans trous pour les valeurs à égalité.

* * *

## Exemples {#examples}

### Classer les contributeurs par contributions (sans trous) {#rank-contributors-by-contributions-without-gaps}

```sql
-- Rank contributors without gaps for ties
SELECT
    DENSE_RANK() OVER (ORDER BY contributions DESC) as rank,
    login,
    contributions,
    html_url
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo IN ('stackql', 'stackql-deploy', 'stackql-provider-registry')
ORDER BY contributions DESC;
```

### Comparer `DENSE_RANK` avec `RANK` et `ROW_NUMBER` {#compare-dense_rank-with-rank-and-row_number}

```sql
-- Compare RANK vs DENSE_RANK vs ROW_NUMBER for contributors
SELECT
    login,
    contributions,
    RANK() OVER (ORDER BY contributions DESC) as rank,
    DENSE_RANK() OVER (ORDER BY contributions DESC) as dense_rank,
    ROW_NUMBER() OVER (ORDER BY contributions DESC) as row_num
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

### Classer les contributions agrégées sur plusieurs dépôts {#rank-aggregated-contributions-across-multiple-repositories}

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

Pour plus d'informations, consultez [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
