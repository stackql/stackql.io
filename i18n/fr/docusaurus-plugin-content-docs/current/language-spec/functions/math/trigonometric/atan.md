---
title: ATAN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: Calculer l'arc tangente (réciproque de la tangente) d'un nombre et renvoyer un angle en radians avec SQL dans StackQL.
image: "/img/stackql-featured-image.png"
---
Calcule l'arc tangente (réciproque de la tangente) d'un nombre, c'est-à-dire l'angle en radians dont la tangente est le nombre spécifié.

Voir aussi :  
[[`SELECT`]](/language-spec/select)

* * * 

## Syntaxe {#syntax}

```sql
SELECT ATAN(value) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*value*__  
Une expression numérique dont l'arc tangente doit être calculé.

## Valeur(s) renvoyée(s) {#return-values}
Renvoie l'arc tangente de la valeur spécifiée, en radians.

* * *

## Exemples {#examples}

### Calculer l'arc tangente de 1 {#calculate-the-arc-tangent-of-1}

```sql
SELECT ATAN(1) AS arc_tangent_value;
```

Cette requête renvoie π/4, soit environ `0.785`, car l'arc tangente de 1 vaut π/4 radians.

### Calculer l'arc tangente de 0 {#calculate-the-arc-tangent-of-0}

```sql
SELECT ATAN(0) AS arc_tangent_value;
```

Cette requête calcule l'arc tangente de 0, ce qui donne `0`, car la tangente de 0 radian est 0.
