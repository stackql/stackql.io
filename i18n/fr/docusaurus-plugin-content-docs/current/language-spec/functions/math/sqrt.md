---
title: SQRT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: Calculer la racine carrée d'un nombre avec SQL dans StackQL.
image: "/img/stackql-featured-image.png"
---
Calcule la racine carrée d'une expression numérique non négative.

Voir aussi :  
[[`SELECT`]](/language-spec/select) 

* * * 

## Syntaxe {#syntax}

```sql
SELECT SQRT(numeric_expression) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*numeric_expression*__  
Une expression numérique non négative dont la racine carrée doit être calculée.

## Valeur(s) renvoyée(s) {#return-values}
Renvoie la racine carrée de la valeur numérique non négative spécifiée.

* * *

## Exemples {#examples}

### Calculer la racine carrée d'un nombre {#calculate-the-square-root-of-a-number}

```sql
SELECT SQRT(9) AS square_root;
```
