---
title: TAN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: Calculer la tangente d'un angle, exprimé en radians, avec SQL dans StackQL.
image: "/img/stackql-featured-image.png"
---
Calcule la tangente d'un angle spécifié, exprimé en radians.

Voir aussi :  
[[`SELECT`]](/language-spec/select)

* * * 

## Syntaxe {#syntax}

```sql
SELECT TAN(radian_value) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*radian_value*__  
Une expression numérique qui spécifie l'angle en radians dont la tangente doit être calculée.

## Valeur(s) renvoyée(s) {#return-values}
Renvoie la tangente de l'angle spécifié.

* * *

## Exemples {#examples}

### Calculer la tangente de π/4 radians {#calculate-the-tangent-of-π4-radians}

```sql
SELECT TAN(PI() / 4) AS tangent_value;
```

Cette requête renvoie `1`, car la tangente de π/4 radians est 1.

### Calculer la tangente de π radians {#calculate-the-tangent-of-π-radians}

```sql
SELECT TAN(PI()) AS tangent_value;
```

Cette requête calcule la tangente de π radians, ce qui donne environ `0`, car la tangente de π est très proche de 0.
