---
title: CEIL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Fonction CEIL dans StackQL : arrondir un argument numérique à l'entier supérieur le plus proche."
image: "/img/stackql-featured-image.png"
---
Arrondit un argument numérique à l'entier supérieur le plus proche.  

Voir aussi :  
[[`SELECT`]](/language-spec/select) 

* * * 

## Syntaxe {#syntax}

```sql
SELECT CEIL(numeric_expression) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*numeric_expression*__  
Une expression de la catégorie des types de données numériques exacts ou approchés.

## Valeur(s) renvoyée(s) {#return-values}
Renvoie le plus petit entier supérieur ou égal à l'expression numérique spécifiée.

* * *

## Exemples {#examples}

### Arrondir un nombre à virgule flottante à l'entier (supérieur) le plus proche {#round-up-a-floating-point-number-to-the-closest-larger-integer}

```sql
SELECT name, timeCreated,
ceil(julianday('now')-julianday(timeCreated)) as days_since_ceiling
FROM google.storage.buckets WHERE project = 'stackql';
```
