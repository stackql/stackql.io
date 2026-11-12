---
title: ASIN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: Calculer l'arc sinus (réciproque du sinus) d'un nombre et renvoyer un angle en radians avec SQL dans StackQL.
image: "/img/stackql-featured-image.png"
---
Calcule l'arc sinus (réciproque du sinus) d'un nombre, c'est-à-dire l'angle en radians dont le sinus est le nombre spécifié.

Voir aussi :  
[[`SELECT`]](/language-spec/select)

* * * 

## Syntaxe {#syntax}

```sql
SELECT ASIN(value) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*value*__  
Une expression numérique comprise entre -1 et 1 inclus, dont l'arc sinus doit être calculé.

## Valeur(s) renvoyée(s) {#return-values}
Renvoie l'arc sinus de la valeur spécifiée, en radians.

* * *

## Exemples {#examples}

### Calculer l'arc sinus de 0.5 {#calculate-the-arc-sine-of-05}

```sql
SELECT ASIN(0.5) AS arc_sine_value;
```

Cette requête renvoie π/6, soit environ `0.5236`, car l'arc sinus de 0.5 correspond à un angle de π/6 radians.

### Calculer l'arc sinus de -1 {#calculate-the-arc-sine-of--1}

```sql
SELECT ASIN(-1) AS arc_sine_value;
```

Cette requête calcule l'arc sinus de -1, ce qui donne -π/2, soit environ `-1.5708`, car le sinus de -π/2 est -1.
