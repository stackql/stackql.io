---
title: LOG
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: Calculer le logarithme d'un nombre dans une base quelconque avec SQL dans StackQL.
image: "/img/stackql-featured-image.png"
---
Calcule le logarithme d'un nombre dans une base spécifiée, une forme plus générale de fonction logarithmique que LN, qui utilise la base \(e\).

Voir aussi :  
[[`SELECT`]](/language-spec/select) 

* * * 

## Syntaxe {#syntax}

```sql
SELECT LOG(base, numeric_expression) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*base*__  
La base du logarithme, une expression numérique positive différente de 1.

__*numeric_expression*__  
Une expression numérique positive dont le logarithme doit être calculé.

## Valeur(s) renvoyée(s) {#return-values}
Renvoie le logarithme de la valeur numérique spécifiée dans la base spécifiée.

* * *

## Exemples {#examples}

### Calculer le logarithme de 1000 en base 10 {#calculate-the-logarithm-of-1000-with-base-10}

```sql
SELECT LOG(10, 1000) AS log_result;
```

Cette requête renvoie `3`, car \(10^3 = 1000\).

### Calculer le logarithme de 16 en base 2 {#calculate-the-logarithm-of-16-with-base-2}

```sql
SELECT LOG(2, 16) AS log_result;
```
Cette requête calcule le logarithme de 16 en base 2, ce qui donne `4`, car \(2^4 = 16\).
