---
title: LOG10
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: Calculer le logarithme en base 10 d'un nombre avec SQL dans StackQL.
image: "/img/stackql-featured-image.png"
---
Calcule le logarithme en base 10 d'un nombre donné, en déterminant la puissance à laquelle le nombre 10 doit être élevé pour obtenir cette valeur.

Voir aussi :  
[[`SELECT`]](/language-spec/select) 

* * * 

## Syntaxe {#syntax}

```sql
SELECT LOG10(numeric_expression) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*numeric_expression*__  
Une expression numérique positive dont le logarithme en base 10 doit être calculé.

## Valeur(s) renvoyée(s) {#return-values}
Renvoie le logarithme en base 10 de la valeur numérique spécifiée.

* * *

## Exemples {#examples}

### Calculer le logarithme en base 10 de 100 {#calculate-the-base-10-logarithm-of-100}

```sql
SELECT LOG10(100) AS log_result;
```

Cette requête renvoie `2`, car \(10^2 = 100\).

### Calculer le logarithme en base 10 de 1000 {#calculate-the-base-10-logarithm-of-1000}

```sql
SELECT LOG10(1000) AS log_result;
```

Cette requête calcule le logarithme en base 10 de 1000, ce qui donne `3`, car \(10^3 = 1000\).
