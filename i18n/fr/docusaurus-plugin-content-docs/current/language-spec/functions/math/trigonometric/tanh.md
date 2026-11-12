---
title: TANH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: Calculer la tangente hyperbolique d'un nombre et renvoyer le résultat avec SQL dans StackQL.
image: "/img/stackql-featured-image.png"
---
Calcule la tangente hyperbolique d'une valeur spécifiée.

Voir aussi :  
[[`SELECT`]](/language-spec/select)

* * * 

## Syntaxe {#syntax}

```sql
SELECT TANH(value) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*value*__  
Une expression numérique dont la tangente hyperbolique doit être calculée.

## Valeur(s) renvoyée(s) {#return-values}
Renvoie la tangente hyperbolique de la valeur spécifiée.

* * *

## Exemples {#examples}

### Calculer la tangente hyperbolique de 1 {#calculate-the-hyperbolic-tangent-of-1}

```sql
SELECT TANH(1) AS hyperbolic_tangent_value;
```

Cette requête renvoie environ `0.7616`, car la tangente hyperbolique de 1 vaut environ 0.7616.

### Calculer la tangente hyperbolique de 0 {#calculate-the-hyperbolic-tangent-of-0}

```sql
SELECT TANH(0) AS hyperbolic_tangent_value;
```

Cette requête calcule la tangente hyperbolique de 0, ce qui donne `0`, car la tangente hyperbolique de 0 vaut exactement 0.
