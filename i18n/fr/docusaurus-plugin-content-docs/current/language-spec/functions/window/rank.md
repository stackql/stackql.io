---
title: RANK
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Fonction de fenêtrage RANK dans StackQL : le rang avec trous de la ligne courante dans sa partition, où les lignes à égalité partagent un rang et les suivants sont sautés."
image: "/img/stackql-featured-image.png"
---
Renvoie le rang de la ligne courante, avec des trous. Il s'agit du `ROW_NUMBER` du premier pair de chaque groupe. Les lignes ayant des valeurs égales pour les colonnes de tri reçoivent le même rang, et la valeur de rang suivante est incrémentée du nombre de lignes à égalité (ce qui laisse des trous).

En l'absence de clause `ORDER BY`, toutes les lignes sont considérées comme des pairs et cette fonction renvoie toujours 1.

Voir aussi :
[[` SELECT `]](/language-spec/select) [[` ROW_NUMBER `]](/language-spec/functions/window/row_number) [[` DENSE_RANK `]](/language-spec/functions/window/dense_rank)

* * *

## Syntaxe {#syntax}

```sql
SELECT RANK() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*PARTITION BY column*__
Facultatif. Divise le jeu de résultats en partitions. La fonction `RANK` est appliquée à chaque partition séparément.

__*ORDER BY column*__
Spécifie l'ordre dans lequel les rangs sont attribués. En l'absence de clause `ORDER BY`, toutes les lignes sont considérées comme des pairs et reçoivent le rang 1.

## Valeur(s) renvoyée(s) {#return-values}

Renvoie un entier représentant le rang de la ligne courante au sein de la partition, avec des trous pour les valeurs à égalité.

* * *

## Exemples {#examples}

### Classer les contributeurs par contributions (avec des trous) {#rank-contributors-by-contributions-with-gaps}

```sql
-- Rank contributors with gaps for ties
SELECT
    login,
    contributions,
    RANK() OVER (ORDER BY contributions DESC) as rank
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

### Comparer `RANK` avec `DENSE_RANK` et `ROW_NUMBER` {#compare-rank-with-dense_rank-and-row_number}

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

### Classer les contributeurs au sein de chaque dépôt avec `PARTITION BY` {#rank-contributors-within-each-repository-using-partition-by}

```sql
-- Rank contributors within each repo
WITH repo_contributors AS (
    SELECT
        repo,
        login,
        contributions,
        RANK() OVER (PARTITION BY repo ORDER BY contributions DESC) as rank_in_repo
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

Pour plus d'informations, consultez [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
