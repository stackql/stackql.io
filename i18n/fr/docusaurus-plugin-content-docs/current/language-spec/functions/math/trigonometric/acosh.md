---
title: ACOSH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: Calculer l'arc cosinus hyperbolique (réciproque du cosinus hyperbolique) d'un nombre et renvoyer un angle en radians avec SQL dans StackQL.
image: "/img/stackql-featured-image.png"
---
Calcule l'arc cosinus hyperbolique (réciproque du cosinus hyperbolique) d'un nombre, c'est-à-dire la valeur non négative \( \theta \) en radians dont le cosinus hyperbolique est le nombre spécifié.

Voir aussi :  
[[`SELECT`]](/language-spec/select)

* * * 

## Syntaxe {#syntax}

```sql
SELECT ACOSH(value) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*value*__  
Une expression numérique supérieure ou égale à 1, dont l'arc cosinus hyperbolique doit être calculé.

## Valeur(s) renvoyée(s) {#return-values}
Renvoie l'arc cosinus hyperbolique de la valeur spécifiée, en radians.

* * *

## Exemples {#examples}

### Calculer l'arc cosinus hyperbolique de 1 {#calculate-the-hyperbolic-arc-cosine-of-1}

```sql
SELECT ACOSH(1) AS hyperbolic_arc_cosine_value;
```

Cette requête renvoie `0`, car l'arc cosinus hyperbolique de 1 vaut 0 radian.

### Calculer l'arc cosinus hyperbolique de 10 {#calculate-the-hyperbolic-arc-cosine-of-10}

```sql
SELECT ACOSH(10) AS hyperbolic_arc_cosine_value;
```

Cette requête calcule l'arc cosinus hyperbolique de 10, ce qui donne environ `2.9932`, car il s'agit de la valeur en radians dont le cosinus hyperbolique est 10.
