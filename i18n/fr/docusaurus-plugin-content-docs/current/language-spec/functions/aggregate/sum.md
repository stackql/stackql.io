---
title: SUM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Fonction d'agrégation SUM dans StackQL : le total des valeurs non NULL d'une colonne ou d'un regroupement, renvoyé sous forme d'entier si toutes les entrées sont entières."
image: "/img/stackql-featured-image.png"
---
Renvoie la somme de toutes les valeurs non `NULL` d'une colonne ou d'un regroupement de colonnes.  

Voir aussi :  
[[` SELECT `]](/language-spec/select) [[` TOTAL `]](/language-spec/functions/aggregate/total)

* * * 

:::tip

Utilisez la fonction [**TOTAL**](/language-spec/functions/aggregate/total) pour additionner des nombres à virgule flottante ou de grandes valeurs.

:::

## Syntaxe {#syntax}

```sql
SELECT SUM(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## Arguments {#arguments}

__*columnExpression*__  
Une colonne ou une expression.

> Si toutes les valeurs de *columnExpression* sont `NULL`, `SUM` renvoie `NULL`.  

> `SUM` lève une exception "integer overflow" si un dépassement d'entier se produit à un moment quelconque du calcul.  La fonction [`TOTAL`](/language-spec/functions/aggregate/total) ne lève jamais de dépassement d'entier.  

__*groupByColumn*__  
Une ou plusieurs colonnes sur lesquelles effectuer des opérations de synthèse ou d'agrégation.  La clause `GROUP BY` renvoie une ligne pour chaque regroupement de colonnes.

## Valeur(s) renvoyée(s) {#return-values}

Renvoie un entier représentant la somme de *columnExpression* si toutes les entrées non `NULL` sont des entiers. Si certaines entrées ne sont pas des entiers ou sont `NULL`, une valeur à virgule flottante, qui peut être une approximation de la somme réelle, est renvoyée.

* * *

## Exemples {#examples}

### Renvoyer la somme d'une expression de colonne sur l'ensemble d'une ressource {#return-the-sum-of-a-column-expression-over-an-entire-resource}

```sql
SELECT sum(json_array_length(disks)) as sum_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Renvoyer la somme d'une expression de colonne regroupée selon une autre expression de colonne {#return-the-sum-of-a-column-expression-grouped-by-another-column-expression}

```sql
SELECT tags, sum(json_array_length(disks)) as sum_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY json_extract(tags, '$.instanceType');
```

### Utiliser `SUM` comme fonction de fenêtrage pour calculer des totaux cumulés {#use-sum-as-a-window-function-to-calculate-running-totals}

```sql
-- Calculate running total and percentage of contributions
SELECT
    login,
    contributions,
    SUM(contributions) OVER (ORDER BY contributions DESC) as running_total,
    SUM(contributions) OVER () as total_contributions,
    ROUND(100.0 * contributions / SUM(contributions) OVER (), 2) as pct_of_total
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

Pour plus d'informations, consultez [https://www.sqlite.org/lang_aggfunc.html#sumunc](https://www.sqlite.org/lang_aggfunc.html#sumunc).
