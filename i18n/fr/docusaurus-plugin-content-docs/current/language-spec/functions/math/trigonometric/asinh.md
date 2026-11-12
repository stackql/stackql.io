---
title: ASINH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: Calculer l'arc sinus hyperbolique (réciproque du sinus hyperbolique) d'un nombre et renvoyer un angle en radians avec SQL dans StackQL.
image: "/img/stackql-featured-image.png"
---
Calcule l'arc sinus hyperbolique (réciproque du sinus hyperbolique) d'un nombre, c'est-à-dire la valeur \( \theta \) en radians dont le sinus hyperbolique est le nombre spécifié.

Voir aussi :  
[[`SELECT`]](/language-spec/select)

* * * 

## Syntaxe {#syntax}

```sql
SELECT ASINH(value) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*value*__  
Une expression numérique dont l'arc sinus hyperbolique doit être calculé.

## Valeur(s) renvoyée(s) {#return-values}
Renvoie l'arc sinus hyperbolique de la valeur spécifiée, en radians.

* * *

## Exemples {#examples}

### Calculer l'arc sinus hyperbolique de 0.5 {#calculate-the-hyperbolic-arc-sine-of-05}

```sql
SELECT ASINH(0.5) AS hyperbolic_arc_sine_value;
```

Cette requête renvoie environ `0.4812`, car l'arc sinus hyperbolique de 0.5 vaut environ 0.4812 radian.

### Calculer l'arc sinus hyperbolique de -1 {#calculate-the-hyperbolic-arc-sine-of--1}

```sql
SELECT ASINH(-1) AS hyperbolic_arc_sine_value;
```

Cette requête calcule l'arc sinus hyperbolique de -1, ce qui donne environ `-0.8814`, car il s'agit de la valeur en radians dont le sinus hyperbolique est -1.
