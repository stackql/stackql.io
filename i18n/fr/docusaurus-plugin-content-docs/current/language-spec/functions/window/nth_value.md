---
title: NTH_VALUE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Fonction de fenêtrage NTH_VALUE dans StackQL : la valeur d'une expression pour la N-ième ligne du cadre de fenêtre."
image: "/img/stackql-featured-image.png"
---
Renvoie la valeur de l'expression évaluée sur la N-ième ligne du cadre de fenêtre.

Cette fonction de fenêtrage intégrée calcule le cadre de fenêtre de chaque ligne de la même manière qu'une fonction de fenêtrage d'agrégation. Elle renvoie la valeur de l'expression évaluée sur la ligne N du cadre de fenêtre. Les lignes sont numérotées au sein du cadre de fenêtre à partir de 1 dans l'ordre défini par la clause `ORDER BY` si elle est présente, ou dans un ordre arbitraire sinon. S'il n'y a pas de N-ième ligne dans la partition, `NULL` est renvoyé.

Voir aussi :
[[` SELECT `]](/language-spec/select) [[` FIRST_VALUE `]](/language-spec/functions/window/first_value) [[` LAST_VALUE `]](/language-spec/functions/window/last_value)

* * *

## Syntaxe {#syntax}

```sql
SELECT NTH_VALUE(expr, N) OVER (
    [PARTITION BY column]
    ORDER BY column
    [ROWS BETWEEN frame_start AND frame_end]
) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*expr*__
L'expression à évaluer sur la N-ième ligne du cadre de fenêtre.

__*N*__
Un entier strictement positif indiquant le numéro de la ligne à récupérer (indexé à partir de 1).

__*PARTITION BY column*__
Facultatif. Divise le jeu de résultats en partitions. La fonction `NTH_VALUE` est appliquée séparément au sein de chaque partition.

__*ORDER BY column*__
Spécifie l'ordre dans lequel les lignes sont traitées.

__*ROWS BETWEEN frame_start AND frame_end*__
Facultatif. Définit le cadre de fenêtre.

## Valeur(s) renvoyée(s) {#return-values}

Renvoie la valeur de l'expression évaluée sur la N-ième ligne du cadre de fenêtre, ou `NULL` si la N-ième ligne n'existe pas.

* * *

## Exemples {#examples}

### Obtenir le deuxième contributeur le plus important {#get-the-second-highest-contributor}

```sql
-- Get the top 3 contributors by name for comparison
SELECT
    login,
    contributions,
    NTH_VALUE(login, 1) OVER (ORDER BY contributions DESC
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) as top_contributor,
    NTH_VALUE(login, 2) OVER (ORDER BY contributions DESC
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) as second_contributor,
    NTH_VALUE(login, 3) OVER (ORDER BY contributions DESC
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) as third_contributor
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

Pour plus d'informations, consultez [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
