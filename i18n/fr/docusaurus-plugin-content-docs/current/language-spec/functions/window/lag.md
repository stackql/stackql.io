---
title: LAG
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Fonction de fenêtrage LAG dans StackQL : évaluer une expression sur une ligne précédente de la partition, avec un décalage et une valeur par défaut facultatifs."
image: "/img/stackql-featured-image.png"
---
Renvoie le résultat de l'évaluation d'une expression sur une ligne précédente de la partition.

La première forme de `LAG()` renvoie le résultat de l'évaluation de l'expression sur la ligne précédente de la partition. S'il n'y a pas de ligne précédente (parce que la ligne courante est la première), `NULL` est renvoyé.

Si l'argument offset est fourni, il doit être un entier positif ou nul. La valeur renvoyée est le résultat de l'évaluation de l'expression sur la ligne située *offset* lignes avant la ligne courante au sein de la partition. Si offset vaut 0, l'expression est évaluée sur la ligne courante. S'il n'y a pas de ligne située *offset* lignes avant la ligne courante, `NULL` est renvoyé.

Si une valeur par défaut est également fournie, elle est renvoyée à la place de `NULL` lorsque la ligne désignée par offset n'existe pas.

Voir aussi :
[[` SELECT `]](/language-spec/select) [[` LEAD `]](/language-spec/functions/window/lead)

* * *

## Syntaxe {#syntax}

```sql
SELECT LAG(expr) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;

SELECT LAG(expr, offset) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;

SELECT LAG(expr, offset, default) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*expr*__
L'expression à évaluer sur la ligne précédente.

__*offset*__
Facultatif. Un entier positif ou nul indiquant de combien de lignes remonter. Valeur par défaut : 1.

__*default*__
Facultatif. La valeur à renvoyer si la ligne désignée par offset n'existe pas. Valeur par défaut : `NULL`.

__*PARTITION BY column*__
Facultatif. Divise le jeu de résultats en partitions. La fonction `LAG` est appliquée séparément au sein de chaque partition.

__*ORDER BY column*__
Spécifie l'ordre dans lequel les lignes sont traitées.

## Valeur(s) renvoyée(s) {#return-values}

Renvoie la valeur de l'expression évaluée sur la ligne précédente indiquée, ou `NULL` (ou la valeur par défaut) si cette ligne n'existe pas.

* * *

## Exemples {#examples}

### Comparer chaque version à la version précédente {#compare-each-release-to-the-previous-release}

```sql
-- Compare each release to previous and next release dates
SELECT
    tag_name,
    name,
    published_at,
    LAG(tag_name, 1) OVER (ORDER BY published_at) as previous_release,
    LAG(published_at, 1) OVER (ORDER BY published_at) as previous_release_date,
    julianday(published_at) - julianday(LAG(published_at, 1) OVER (ORDER BY published_at)) as days_since_last_release
FROM github.repos.releases
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY published_at;
```

### Analyser les écarts entre les créations d'issues {#analyze-gaps-between-issue-creation}

```sql
-- Analyze gaps between issue creation
SELECT
    number,
    title,
    created_at,
    LAG(created_at, 1) OVER (ORDER BY created_at) as prev_issue_date,
    julianday(created_at) - julianday(LAG(created_at, 1) OVER (ORDER BY created_at)) as days_since_prev_issue
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY created_at;
```

Pour plus d'informations, consultez [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
