---
title: ROW_NUMBER
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Fonction de fenêtrage ROW_NUMBER dans StackQL : numéroter les lignes d'une partition à partir de 1 dans l'ordre donné par la clause ORDER BY de la fenêtre."
image: "/img/stackql-featured-image.png"
---
Renvoie le numéro de la ligne au sein de la partition courante. Les lignes sont numérotées à partir de 1 dans l'ordre défini par la clause `ORDER BY` de la définition de la fenêtre, ou dans un ordre arbitraire sinon.

Voir aussi :
[[` SELECT `]](/language-spec/select) [[` RANK `]](/language-spec/functions/window/rank) [[` DENSE_RANK `]](/language-spec/functions/window/dense_rank)

* * *

## Syntaxe {#syntax}

```sql
SELECT ROW_NUMBER() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*PARTITION BY column*__
Facultatif. Divise le jeu de résultats en partitions. La fonction `ROW_NUMBER` est appliquée à chaque partition séparément et réinitialise le numéro de ligne pour chaque partition.

__*ORDER BY column*__
Spécifie l'ordre dans lequel les numéros de ligne sont attribués. Obligatoire pour obtenir des résultats pertinents.

## Valeur(s) renvoyée(s) {#return-values}

Renvoie un entier représentant le numéro de ligne séquentiel au sein de la partition, à partir de 1.

* * *

## Exemples {#examples}

### Classer les contributeurs au sein d'un dépôt {#rank-contributors-within-a-repository}

```sql
-- Rank contributors within a repository
SELECT
    login,
    contributions,
    ROW_NUMBER() OVER (ORDER BY contributions DESC) as contribution_rank
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

### Suivre la séquence des issues dans le temps {#track-issue-sequence-over-time}

```sql
-- Track issue growth over time
SELECT
    number,
    title,
    state,
    created_at,
    ROW_NUMBER() OVER (ORDER BY created_at) as issue_sequence
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY created_at;
```

### Comparer `ROW_NUMBER` avec `RANK` et `DENSE_RANK` {#compare-row_number-with-rank-and-dense_rank}

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

Pour plus d'informations, consultez [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
