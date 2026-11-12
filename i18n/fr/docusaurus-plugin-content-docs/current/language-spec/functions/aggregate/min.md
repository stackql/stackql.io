---
title: MIN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Fonction d'agrégation MIN dans StackQL : la plus petite valeur d'une colonne ou d'un regroupement de colonnes."
image: "/img/stackql-featured-image.png"
---
Renvoie la valeur minimale à partir d'une colonne d'entrée ou d'un regroupement de colonnes.  

Voir aussi :  
[[` SELECT `]](/language-spec/select) 

* * * 

## Syntaxe {#syntax}

```sql
/* aggregate function */
SELECT MIN(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```
```sql
/* scalar (multi-argument) function */
SELECT MIN(scalarExpression1, scalarExpressionN, ...) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

### Fonction d'agrégation {#aggregate-function}

__*columnExpression*__  
Une colonne ou une expression incluant des opérateurs (`+`, `-`, ...).

> La fonction `MIN` renvoie la plus petite valeur de la colonne donnée selon le type de données de la colonne ; par exemple, une opération `MIN` sur une colonne d'entiers renvoie le plus petit entier de cette colonne ou de ce regroupement de colonnes, tandis qu'une opération `MIN` sur une colonne ou un regroupement de colonnes de type chaîne de caractères renvoie la plus petite valeur ASCII (généralement la première valeur dans l'ordre lexicographique, à quelques différences près).

__*groupByColumn*__  
Une ou plusieurs colonnes sur lesquelles effectuer des opérations de synthèse ou d'agrégation.  La clause `GROUP BY` renvoie une ligne pour chaque regroupement de colonnes.

> La fonction `MIN` ignore les valeurs `NULL`.

> La fonction `MIN` renvoie `NULL` si toutes les valeurs du groupe sont `NULL`.

### Fonction scalaire {#scalar-function}

__*scalarExpression*__  
Une liste de colonnes ou d'expressions (2 ou plus) à partir de laquelle la plus petite valeur est déterminée et renvoyée. 

> La fonction scalaire `MIN` renvoie `NULL` si l'un des arguments est `NULL`. 

> La fonction `MIN` scalaire, ou à plusieurs arguments, parcourt ses arguments de gauche à droite à la recherche d'un argument qui définit une fonction de collation et utilise cette fonction de collation pour toutes les comparaisons de chaînes. 

> Si aucun des arguments de la fonction scalaire `MIN` ne définit de fonction de collation, la fonction de collation `BINARY` est utilisée. 

> Si un seul argument est fourni, la fonction d'agrégation `MIN` est appelée.

## Valeur(s) renvoyée(s) {#return-values}

Renvoie la valeur minimale selon le type de données en entrée (ou le regroupement).

* * *

## Exemples {#examples}

### Renvoyer la valeur minimale d'une colonne {#return-the-minimum-value-for-a-column}

```sql
SELECT name, location, min(timeCreated) 
FROM google.storage.buckets WHERE project = 'stackql';
```

### Renvoyer la valeur minimale pour chaque valeur d'une colonne {#return-the-minimum-value-for-each-value-in-a-column}

```sql
SELECT name, location,
min(round(julianday('now')-julianday(timeCreated))) as age
FROM google.storage.buckets WHERE project = 'stackql'
GROUP BY location;
```

### Renvoyer la valeur minimale d'une liste de valeurs avec la fonction scalaire `min` {#return-the-minimum-value-from-a-list-of-values-using-the-scalar-min-function}

```sql
SELECT min(json_extract(disks, '$[0].diskSizeGb'),json_extract(disks, '$[1].diskSizeGb')) as smallest_disk
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Utiliser `MIN` comme fonction de fenêtrage pour trouver les valeurs minimales au sein des partitions {#use-min-as-a-window-function-to-find-minimum-values-within-partitions}

```sql
-- Find the earliest issue in each state
SELECT
    number,
    title,
    state,
    created_at,
    MIN(created_at) OVER (PARTITION BY state) as earliest_in_state
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY state, created_at;
```

Pour plus d'informations, consultez [https://www.sqlite.org/lang_aggfunc.html#min_agg](https://www.sqlite.org/lang_aggfunc.html#min_agg) ou [https://www.sqlite.org/lang_corefunc.html#min_scalar](https://www.sqlite.org/lang_corefunc.html#min_scalar)
