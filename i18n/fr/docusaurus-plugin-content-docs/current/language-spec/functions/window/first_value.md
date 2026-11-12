---
title: FIRST_VALUE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Fonction de fenêtrage FIRST_VALUE dans StackQL : la valeur d'une expression pour la première ligne du cadre de fenêtre."
image: "/img/stackql-featured-image.png"
---
Renvoie la valeur de l'expression évaluée sur la première ligne du cadre de fenêtre.

Cette fonction de fenêtrage intégrée calcule le cadre de fenêtre de chaque ligne de la même manière qu'une fonction de fenêtrage d'agrégation. Elle renvoie, pour chaque ligne, la valeur de l'expression évaluée sur la première ligne du cadre de fenêtre.

Voir aussi :
[[` SELECT `]](/language-spec/select) [[` LAST_VALUE `]](/language-spec/functions/window/last_value) [[` NTH_VALUE `]](/language-spec/functions/window/nth_value)

* * *

## Syntaxe {#syntax}

```sql
SELECT FIRST_VALUE(expr) OVER (
    [PARTITION BY column]
    ORDER BY column
    [ROWS BETWEEN frame_start AND frame_end]
) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*expr*__
L'expression à évaluer sur la première ligne du cadre de fenêtre.

__*PARTITION BY column*__
Facultatif. Divise le jeu de résultats en partitions. La fonction `FIRST_VALUE` est appliquée séparément au sein de chaque partition.

__*ORDER BY column*__
Spécifie l'ordre dans lequel les lignes sont traitées.

__*ROWS BETWEEN frame_start AND frame_end*__
Facultatif. Définit le cadre de fenêtre. Les options courantes sont notamment :
- `ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW` (valeur par défaut)
- `ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING`

## Valeur(s) renvoyée(s) {#return-values}

Renvoie la valeur de l'expression évaluée sur la première ligne du cadre de fenêtre.

* * *

## Exemples {#examples}

### Trouver la première version à titre de contexte {#find-the-first-release-for-context}

```sql
-- Find first and latest release for context
SELECT
    tag_name,
    name,
    published_at,
    FIRST_VALUE(tag_name) OVER (
        ORDER BY published_at
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING
    ) as first_release,
    LAST_VALUE(tag_name) OVER (
        ORDER BY published_at
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING
    ) as latest_release
FROM github.repos.releases
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

:::tip

Par défaut, le cadre de fenêtre s'étend du début de la partition à la ligne courante. Pour inclure toutes les lignes de la partition, utilisez `ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING`.

:::

Pour plus d'informations, consultez [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
