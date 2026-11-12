---
title: LEAD
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Fonction de fenêtrage LEAD dans StackQL : évaluer une expression sur une ligne suivante de la partition, avec un décalage et une valeur par défaut facultatifs."
image: "/img/stackql-featured-image.png"
---
Renvoie le résultat de l'évaluation d'une expression sur une ligne suivante de la partition.

La première forme de `LEAD()` renvoie le résultat de l'évaluation de l'expression sur la ligne suivante de la partition. S'il n'y a pas de ligne suivante (parce que la ligne courante est la dernière), `NULL` est renvoyé.

Si l'argument offset est fourni, il doit être un entier positif ou nul. La valeur renvoyée est le résultat de l'évaluation de l'expression sur la ligne située *offset* lignes après la ligne courante au sein de la partition. Si offset vaut 0, l'expression est évaluée sur la ligne courante. S'il n'y a pas de ligne située *offset* lignes après la ligne courante, `NULL` est renvoyé.

Si une valeur par défaut est également fournie, elle est renvoyée à la place de `NULL` lorsque la ligne désignée par offset n'existe pas.

Voir aussi :
[[` SELECT `]](/language-spec/select) [[` LAG `]](/language-spec/functions/window/lag)

* * *

## Syntaxe {#syntax}

```sql
SELECT LEAD(expr) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;

SELECT LEAD(expr, offset) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;

SELECT LEAD(expr, offset, default) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*expr*__
L'expression à évaluer sur la ligne suivante.

__*offset*__
Facultatif. Un entier positif ou nul indiquant de combien de lignes avancer. Valeur par défaut : 1.

__*default*__
Facultatif. La valeur à renvoyer si la ligne désignée par offset n'existe pas. Valeur par défaut : `NULL`.

__*PARTITION BY column*__
Facultatif. Divise le jeu de résultats en partitions. La fonction `LEAD` est appliquée séparément au sein de chaque partition.

__*ORDER BY column*__
Spécifie l'ordre dans lequel les lignes sont traitées.

## Valeur(s) renvoyée(s) {#return-values}

Renvoie la valeur de l'expression évaluée sur la ligne suivante indiquée, ou `NULL` (ou la valeur par défaut) si cette ligne n'existe pas.

* * *

## Exemples {#examples}

### Consulter la version suivante {#look-ahead-to-the-next-release}

```sql
-- Compare each release to previous and next release dates
SELECT
    tag_name,
    name,
    published_at,
    LAG(tag_name, 1) OVER (ORDER BY published_at) as previous_release,
    LEAD(tag_name, 1) OVER (ORDER BY published_at) as next_release
FROM github.repos.releases
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY published_at;
```

Pour plus d'informations, consultez [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
