---
title: LOG2
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: Calculer le logarithme en base 2 d'un nombre avec SQL dans StackQL.
image: "/img/stackql-featured-image.png"
---
Calcule le logarithme en base 2 d'un nombre donné et renvoie la puissance à laquelle le nombre 2 doit être élevé pour obtenir cette valeur.

Voir aussi :  
[[`SELECT`]](/language-spec/select) 

* * * 

## Syntaxe {#syntax}

```sql
SELECT LOG2(numeric_expression) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*numeric_expression*__  
Une expression numérique positive dont le logarithme en base 2 doit être calculé.

## Valeur(s) renvoyée(s) {#return-values}
Renvoie le logarithme en base 2 de la valeur numérique spécifiée.

* * *

## Exemples {#examples}

### Calculer le logarithme en base 2 de 16 {#calculate-the-base-2-logarithm-of-16}

```sql
SELECT LOG2(16) AS log_result;
```

Cette requête renvoie `4`, car \(2^4 = 16\).

### Calculer le logarithme en base 2 de 32 {#calculate-the-base-2-logarithm-of-32}

```sql
SELECT LOG2(32) AS log_result;
```

Cette requête calcule le logarithme en base 2 de 32, ce qui donne `5`, car \(2^5 = 32\).
