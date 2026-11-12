---
title: TOTAL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Fonction d'agrégation TOTAL dans StackQL : le total des valeurs non NULL d'une colonne ou d'un regroupement, toujours renvoyé sous forme de nombre à virgule flottante."
image: "/img/stackql-featured-image.png"
---
Renvoie la somme de toutes les valeurs non `NULL` d'une colonne ou d'un regroupement de colonnes.  

Voir aussi :  
[[` SELECT `]](/language-spec/select) [[` SUM `]](/language-spec/functions/aggregate/sum)

* * * 

## Syntaxe {#syntax}

```sql
SELECT TOTAL(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## Arguments {#arguments}

__*columnExpression*__  
Une colonne ou une expression.

> Si toutes les valeurs de *columnExpression* sont `NULL`, `TOTAL` renvoie `0.0`.  

__*groupByColumn*__  
Une ou plusieurs colonnes sur lesquelles effectuer des opérations de synthèse ou d'agrégation.  La clause `GROUP BY` renvoie une ligne pour chaque regroupement de colonnes.

## Valeur(s) renvoyée(s) {#return-values}

Renvoie une valeur à virgule flottante représentant la somme de *columnExpression*.

* * *

## Exemples {#examples}

### Renvoyer la somme d'une expression de colonne sur l'ensemble d'une ressource {#return-the-sum-of-a-column-expression-over-an-entire-resource}

```sql
SELECT total(json_array_length(disks)) as total_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Renvoyer la somme d'une expression de colonne regroupée selon une autre expression de colonne {#return-the-sum-of-a-column-expression-grouped-by-another-column-expression}

```sql
SELECT tags, total(json_array_length(disks)) as total_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY json_extract(tags, '$.instanceType');
```

Pour plus d'informations, consultez [https://www.sqlite.org/lang_aggfunc.html#sumunc](https://www.sqlite.org/lang_aggfunc.html#sumunc).
