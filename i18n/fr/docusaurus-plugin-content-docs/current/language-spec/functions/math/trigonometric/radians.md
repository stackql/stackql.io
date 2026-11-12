---
title: RADIANS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: Convertir un angle de degrés en radians avec SQL dans StackQL.
image: "/img/stackql-featured-image.png"
---
Convertit un angle spécifié de degrés en radians.

Voir aussi :  
[[`SELECT`]](/language-spec/select)

* * * 

## Syntaxe {#syntax}

```sql
SELECT RADIANS(angle_in_degrees) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*angle_in_degrees*__  
Une expression numérique représentant un angle en degrés.

## Valeur(s) renvoyée(s) {#return-values}
Renvoie l'équivalent de l'angle en radians.

* * *

## Exemples {#examples}

### Convertir 180 degrés en radians {#convert-180-degrees-to-radians}

```sql
SELECT RADIANS(180) AS radians_value;
```

Cette requête renvoie π (environ `3.14159`), car la conversion de 180 degrés en radians donne π radians.

### Convertir 90 degrés en radians {#convert-90-degrees-to-radians}

```sql
SELECT RADIANS(90) AS radians_value;
```

Cette requête calcule la conversion de 90 degrés en radians, ce qui donne π/2, soit environ `1.5708`, car 90 degrés correspondent à la moitié de 180 degrés.
