---
title: AVG
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Fonction d'agrégation AVG dans StackQL : la moyenne des valeurs non NULL d'une colonne ou d'un regroupement."
image: "/img/stackql-featured-image.png"
---
Renvoie la valeur moyenne de toutes les valeurs non `NULL` d'une colonne ou d'un regroupement de colonnes.  

Voir aussi :  
[[` SELECT `]](/language-spec/select)

* * * 

## Syntaxe {#syntax}

```sql
SELECT AVG(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## Arguments {#arguments}

__*columnExpression*__  
Une colonne ou une expression.

> Les valeurs de type chaîne de caractères et BLOB qui ne peuvent pas être représentées sous forme de valeurs numériques sont interprétées comme `0`.  

> Au moins une valeur de *columnExpression* doit être non `NULL`, sinon `NULL` est renvoyé.

__*groupByColumn*__  
Une ou plusieurs colonnes sur lesquelles effectuer des opérations de synthèse ou d'agrégation.  La clause `GROUP BY` renvoie une ligne pour chaque regroupement de colonnes.

## Valeur(s) renvoyée(s) {#return-values}

Renvoie une valeur à virgule flottante représentant la moyenne de *columnExpression*.

* * *

## Exemples {#examples}

### Renvoyer la moyenne d'une expression de colonne sur l'ensemble d'une ressource {#return-the-average-of-a-column-expression-over-an-entire-resource}

```sql
SELECT avg(json_array_length(disks)) as avg_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Renvoyer la moyenne d'une expression de colonne regroupée selon une autre expression de colonne {#return-the-average-of-a-column-expression-grouped-by-another-column-expression}

```sql
SELECT tags, avg(json_array_length(disks)) as avg_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY json_extract(tags, '$.instanceType');
```

### Utiliser `AVG` comme fonction de fenêtrage pour calculer des moyennes mobiles {#use-avg-as-a-window-function-to-calculate-moving-averages}

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
    ROUND(AVG(commits_this_week) OVER (ORDER BY week ROWS BETWEEN 3 PRECEDING AND CURRENT ROW), 1) as four_week_moving_avg
FROM weekly_totals
ORDER BY week;
```

Pour plus d'informations, consultez [https://www.sqlite.org/lang_aggfunc.html#avg](https://www.sqlite.org/lang_aggfunc.html#avg).
