---
title: PERCENT_RANK
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Fonction de fenêtrage PERCENT_RANK dans StackQL : le rang relatif de la ligne courante dans sa partition, sous forme de valeur comprise entre 0.0 et 1.0."
image: "/img/stackql-featured-image.png"
---
Renvoie une valeur comprise entre 0.0 et 1.0 représentant le rang relatif de la ligne courante au sein de sa partition.

Malgré son nom, cette fonction renvoie une valeur égale à `(rank - 1) / (partition-rows - 1)`, où `rank` est la valeur renvoyée par la fonction de fenêtrage intégrée `RANK()` et `partition-rows` est le nombre total de lignes de la partition. Si la partition ne contient qu'une seule ligne, cette fonction renvoie 0.0.

Voir aussi :
[[` SELECT `]](/language-spec/select) [[` RANK `]](/language-spec/functions/window/rank) [[` CUME_DIST `]](/language-spec/functions/window/cume_dist)

* * *

## Syntaxe {#syntax}

```sql
SELECT PERCENT_RANK() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*PARTITION BY column*__
Facultatif. Divise le jeu de résultats en partitions. La fonction `PERCENT_RANK` est appliquée à chaque partition séparément.

__*ORDER BY column*__
Spécifie l'ordre dans lequel les rangs centiles sont calculés.

## Valeur(s) renvoyée(s) {#return-values}

Renvoie un nombre à virgule flottante compris entre 0.0 et 1.0 représentant le rang centile.

* * *

## Exemples {#examples}

### Calculer le rang centile des contributeurs {#calculate-percentile-ranking-of-contributors}

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

Une valeur `PERCENT_RANK` de 0.0 indique la ligne la moins bien classée, tandis que 1.0 indique la ligne la mieux classée. Utilisez cette fonction pour déterminer où se situe une valeur donnée au sein d'une distribution.

:::

Pour plus d'informations, consultez [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
