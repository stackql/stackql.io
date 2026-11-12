---
title: ATANH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: Calculer l'arc tangente hyperbolique (réciproque de la tangente hyperbolique) d'un nombre et renvoyer un angle en radians avec SQL dans StackQL.
image: "/img/stackql-featured-image.png"
---
Calcule l'arc tangente hyperbolique (réciproque de la tangente hyperbolique) d'un nombre, c'est-à-dire la valeur \( \theta \) en radians dont la tangente hyperbolique est le nombre spécifié.

Voir aussi :  
[[`SELECT`]](/language-spec/select)

* * * 

## Syntaxe {#syntax}

```sql
SELECT ATANH(value) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*value*__  
Une expression numérique comprise entre -1 et 1 (bornes exclues), dont l'arc tangente hyperbolique doit être calculé.

## Valeur(s) renvoyée(s) {#return-values}
Renvoie l'arc tangente hyperbolique de la valeur spécifiée, en radians.

* * *

## Exemples {#examples}

### Calculer l'arc tangente hyperbolique de 0.5 {#calculate-the-hyperbolic-arc-tangent-of-05}

```sql
SELECT ATANH(0.5) AS hyperbolic_arc_tangent_value;
```

Cette requête renvoie environ `0.5493`, car l'arc tangente hyperbolique de 0.5 vaut environ 0.5493 radian.

### Calculer l'arc tangente hyperbolique de -0.5 {#calculate-the-hyperbolic-arc-tangent-of--05}

```sql
SELECT ATANH(-0.5) AS hyperbolic_arc_tangent_value;
```

Cette requête calcule l'arc tangente hyperbolique de -0.5, ce qui donne environ `-0.5493`, car il s'agit de la valeur en radians dont la tangente hyperbolique est -0.5.
