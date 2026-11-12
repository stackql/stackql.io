---
title: COUNT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Fonction d'agrégation COUNT dans StackQL : le nombre de lignes d'une ressource, ou de valeurs non NULL d'une colonne au sein d'un regroupement."
image: "/img/stackql-featured-image.png"
---
Renvoie le nombre total d'enregistrements d'une ressource ou le nombre d'occurrences non `NULL` d'une colonne dans un regroupement.  

Voir aussi :  
[[` SELECT `]](/language-spec/select)

* * * 

## Syntaxe {#syntax}

```sql
SELECT COUNT( * | columnExpression ) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## Arguments {#arguments}

__*columnExpression*__  ou __*__  
Une colonne ou une expression.  Si __*__ est spécifié, tous les enregistrements de la ressource ou du regroupement sont comptés.  

> Au moins une valeur de *columnExpression* doit être non `NULL`, sinon `NULL` est renvoyé.

__*groupByColumn*__  
Une ou plusieurs colonnes sur lesquelles effectuer des opérations de synthèse ou d'agrégation.  La clause `GROUP BY` renvoie une ligne pour chaque regroupement de colonnes.

## Valeur(s) renvoyée(s) {#return-values}

Renvoie un entier représentant le décompte.

* * *

## Exemples {#examples}

### Renvoyer le nombre d'enregistrements sur l'ensemble d'une ressource {#return-the-average-of-a-column-expression-over-an-entire-resource}

```sql
SELECT count(*) as num_instances
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Renvoyer le nombre d'enregistrements regroupés selon une expression de colonne {#return-the-average-of-a-column-expression-grouped-by-another-column-expression}

```sql
SELECT tags, count(*) as num_instances
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY json_extract(tags, '$.instanceType');
```

### Utiliser `COUNT` comme fonction de fenêtrage pour calculer des décomptes cumulés {#use-count-as-a-window-function-to-calculate-cumulative-counts}

```sql
-- Track cumulative issue counts over time
SELECT
    number,
    title,
    state,
    created_at,
    COUNT(*) OVER (ORDER BY created_at) as cumulative_issues,
    COUNT(*) OVER (PARTITION BY state ORDER BY created_at) as cumulative_by_state
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY created_at;
```

Pour plus d'informations, consultez [https://www.sqlite.org/lang_aggfunc.html#count](https://www.sqlite.org/lang_aggfunc.html#count).
