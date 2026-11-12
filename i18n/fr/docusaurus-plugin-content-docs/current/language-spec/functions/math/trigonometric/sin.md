---
title: SIN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: Calculer le sinus d'un angle, exprimé en radians, avec SQL dans StackQL.
image: "/img/stackql-featured-image.png"
---
Calcule le sinus d'un angle spécifié, exprimé en radians.

Voir aussi :  
[[`SELECT`]](/language-spec/select)

* * * 

## Syntaxe {#syntax}

```sql
SELECT SIN(radian_value) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*radian_value*__  
Une expression numérique qui spécifie l'angle en radians dont le sinus doit être calculé.

## Valeur(s) renvoyée(s) {#return-values}
Renvoie le sinus de l'angle spécifié.

* * *

## Exemples {#examples}

### Calculer le sinus de π/2 radians {#calculate-the-sine-of-π2-radians}

```sql
SELECT SIN(PI() / 2) AS sine_value;
```

Cette requête renvoie `1`, car le sinus de π/2 radians est 1.

### Calculer le sinus de π radians {#calculate-the-sine-of-π-radians}

```sql
SELECT SIN(PI()) AS sine_value;
```

Cette requête calcule le sinus de π radians, ce qui donne environ `0`, car le sinus de π est 0.
