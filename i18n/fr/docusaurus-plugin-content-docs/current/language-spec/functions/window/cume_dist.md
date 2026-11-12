---
title: CUME_DIST
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Fonction de fenêtrage CUME_DIST dans StackQL : la distribution cumulée de la ligne courante, soit son numéro de ligne divisé par le nombre de lignes de sa partition."
image: "/img/stackql-featured-image.png"
---
Renvoie la distribution cumulée d'une valeur au sein d'une partition. Elle est calculée comme `row-number / partition-rows`, où `row-number` est la valeur renvoyée par `ROW_NUMBER()` pour le dernier pair du groupe et `partition-rows` est le nombre de lignes de la partition.

Voir aussi :
[[` SELECT `]](/language-spec/select) [[` PERCENT_RANK `]](/language-spec/functions/window/percent_rank) [[` RANK `]](/language-spec/functions/window/rank)

* * *

## Syntaxe {#syntax}

```sql
SELECT CUME_DIST() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*PARTITION BY column*__
Facultatif. Divise le jeu de résultats en partitions. La fonction `CUME_DIST` est appliquée à chaque partition séparément.

__*ORDER BY column*__
Spécifie l'ordre dans lequel la distribution cumulée est calculée.

## Valeur(s) renvoyée(s) {#return-values}

Renvoie un nombre à virgule flottante compris entre 0.0 et 1.0 représentant la valeur de la distribution cumulée.

* * *

## Exemples {#examples}

### Calculer la distribution cumulée des contributeurs {#calculate-cumulative-distribution-of-contributors}

```sql
-- Calculate percentile ranking of contributors
SELECT
    login,
    contributions,
    PERCENT_RANK() OVER (ORDER BY contributions) as percentile,
    CUME_DIST() OVER (ORDER BY contributions) as cumulative_dist
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY contributions DESC;
```

:::tip

`CUME_DIST` renvoie la proportion de lignes dont la valeur est inférieure ou égale à celle de la ligne courante. Une valeur de 0.75 signifie que 75% des lignes ont une valeur inférieure ou égale à celle de la ligne courante.

:::

Pour plus d'informations, consultez [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
