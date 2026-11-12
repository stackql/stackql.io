---
title: ACOS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: Calculer l'arc cosinus (réciproque du cosinus) d'un nombre et renvoyer un angle en radians avec SQL dans StackQL.
image: "/img/stackql-featured-image.png"
---
Calcule l'arc cosinus (réciproque du cosinus) d'un nombre, c'est-à-dire l'angle en radians dont le cosinus est le nombre spécifié.

Voir aussi :  
[[`SELECT`]](/language-spec/select)

* * * 

## Syntaxe {#syntax}

```sql
SELECT ACOS(value) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*value*__  
Une expression numérique comprise entre -1 et 1 inclus, dont l'arc cosinus doit être calculé.

## Valeur(s) renvoyée(s) {#return-values}
Renvoie l'arc cosinus de la valeur spécifiée, en radians.

* * *

## Exemples {#examples}

### Calculer l'arc cosinus de 0.5 {#calculate-the-arc-cosine-of-05}

```sql
SELECT ACOS(0.5) AS arc_cosine_value;
```

Cette requête renvoie environ `1.047`, car l'arc cosinus de 0.5 vaut π/3, soit environ 1.047 radian.

### Calculer l'arc cosinus de 1 {#calculate-the-arc-cosine-of-1}

```sql
SELECT ACOS(1) AS arc_cosine_value;
```

Cette requête calcule l'arc cosinus de 1, ce qui donne `0`, car le cosinus de 0 radian est 1.
