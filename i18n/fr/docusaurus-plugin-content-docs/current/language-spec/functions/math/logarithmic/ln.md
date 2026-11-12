---
title: LN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: Calculer le logarithme naturel d'un nombre avec SQL dans StackQL.
image: "/img/stackql-featured-image.png"
---
Calcule le logarithme naturel (logarithme en base \(e\)) d'un nombre donné.

Voir aussi :  
[[`SELECT`]](/language-spec/select) 

* * * 

## Syntaxe {#syntax}

```sql
SELECT LN(numeric_expression) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*numeric_expression*__  
Une expression numérique positive dont le logarithme naturel doit être calculé.

## Valeur(s) renvoyée(s) {#return-values}
Renvoie le logarithme naturel de la valeur numérique positive spécifiée.

* * *

## Exemples {#examples}

### Calculer le logarithme naturel de 2.71828 (environ \(e\)) {#calculate-the-natural-logarithm-of-271828-approximately-e}

```sql
SELECT LN(2.71828) AS natural_log;
```

Cette requête renvoie une valeur proche de `1`, car le logarithme naturel de \(e\) (environ 2.71828) vaut 1.

### Calculer le logarithme naturel d'un nombre plus grand {#calculate-the-natural-logarithm-of-a-larger-number}

```sql
SELECT LN(10) AS natural_log;
```

Cette requête calcule le logarithme naturel de 10, ce qui donne la valeur du logarithme de 10 en base \(e\).
