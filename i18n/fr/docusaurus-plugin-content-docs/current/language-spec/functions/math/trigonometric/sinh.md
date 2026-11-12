---
title: SINH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: Calculer le sinus hyperbolique d'un nombre et renvoyer le résultat avec SQL dans StackQL.
image: "/img/stackql-featured-image.png"
---
Calcule le sinus hyperbolique d'une valeur spécifiée.

Voir aussi :  
[[`SELECT`]](/language-spec/select)

* * * 

## Syntaxe {#syntax}

```sql
SELECT SINH(value) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*value*__  
Une expression numérique dont le sinus hyperbolique doit être calculé.

## Valeur(s) renvoyée(s) {#return-values}
Renvoie le sinus hyperbolique de la valeur spécifiée.

* * *

## Exemples {#examples}

### Calculer le sinus hyperbolique de 1 {#calculate-the-hyperbolic-sine-of-1}

```sql
SELECT SINH(1) AS hyperbolic_sine_value;
```

Cette requête renvoie environ `1.1752`, car le sinus hyperbolique de 1 vaut environ 1.1752.

### Calculer le sinus hyperbolique de 0 {#calculate-the-hyperbolic-sine-of-0}

```sql
SELECT SINH(0) AS hyperbolic_sine_value;
```

Cette requête calcule le sinus hyperbolique de 0, ce qui donne `0`, car le sinus hyperbolique de 0 vaut exactement 0.
