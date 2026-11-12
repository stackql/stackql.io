---
title: ATAN2
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: Calculer l'arc tangente de deux nombres (y/x) et renvoyer un angle en radians avec SQL dans StackQL.
image: "/img/stackql-featured-image.png"
---
Calcule l'arc tangente de deux nombres, plus précisément l'angle \( \theta \) en radians dont la tangente est le rapport des deux nombres spécifiés (y divisé par x), en gérant correctement le signe des entrées pour déterminer le bon quadrant.

Voir aussi :  
[[`SELECT`]](/language-spec/select)

* * * 

## Syntaxe {#syntax}

```sql
SELECT ATAN2(y, x) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*y*__  
La coordonnée y (numérateur) du point.

__*x*__  
La coordonnée x (dénominateur) du point.

## Valeur(s) renvoyée(s) {#return-values}
Renvoie l'arc tangente de y/x en radians, en tenant compte du bon quadrant de l'angle.

* * *

## Exemples {#examples}

### Calculer l'arc tangente du point (1,1) {#calculate-the-arc-tangent-of-the-point-11}

```sql
SELECT ATAN2(1, 1) AS arc_tangent_value;
```

Cette requête renvoie π/4, soit environ `0.785`, car l'arc tangente du point (1,1) correspond à un angle de π/4 radians dans le premier quadrant.

### Calculer l'arc tangente du point (10, -10) {#calculate-the-arc-tangent-of-the-point-10--10}

```sql
SELECT ATAN2(10, -10) AS arc_tangent_value;
```

Cette requête calcule l'arc tangente du point (10, -10), ce qui donne 3π/4, soit environ `2.356`, car la tangente de cet angle est -1 dans le deuxième quadrant.
