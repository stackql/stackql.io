---
title: DEGREES
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: Convertir un angle de radians en degrés avec SQL dans StackQL.
image: "/img/stackql-featured-image.png"
---
Convertit un angle spécifié de radians en degrés.

Voir aussi :  
[[`SELECT`]](/language-spec/select)

* * * 

## Syntaxe {#syntax}

```sql
SELECT DEGREES(angle_in_radians) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*angle_in_radians*__  
Une expression numérique représentant un angle en radians.

## Valeur(s) renvoyée(s) {#return-values}
Renvoie l'équivalent de l'angle en degrés.

* * *

## Exemples {#examples}

### Convertir π radians en degrés {#convert-π-radians-to-degrees}

```sql
SELECT DEGREES(3.14159) AS degrees_value;
```

Cette requête renvoie environ `180`, car la conversion de π radians (environ `3.14159`) donne environ 180 degrés.

### Convertir π/2 radians en degrés {#convert-π2-radians-to-degrees}

```sql
SELECT DEGREES(1.5708) AS degrees_value;
```

Cette requête calcule la conversion de π/2 radians (environ `1.5708`) en degrés, ce qui donne environ `90`, car π/2 radians équivalent à un angle de 90 degrés.
