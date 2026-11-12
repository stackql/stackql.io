---
title: COS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: Calculer le cosinus d'un angle, exprimé en radians, avec SQL dans StackQL.
image: "/img/stackql-featured-image.png"
---
Calcule le cosinus d'un angle spécifié, exprimé en radians.

Voir aussi :  
[[`SELECT`]](/language-spec/select)

* * * 

## Syntaxe {#syntax}

```sql
SELECT COS(radian_value) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*radian_value*__  
Une expression numérique qui spécifie l'angle en radians dont le cosinus doit être calculé.

## Valeur(s) renvoyée(s) {#return-values}
Renvoie le cosinus de l'angle spécifié.

* * *

## Exemples {#examples}

### Calculer le cosinus de π/3 radians {#calculate-the-cosine-of-π3-radians}

```sql
SELECT COS(PI() / 3) AS cosine_value;
```

Cette requête renvoie `0.5`, car le cosinus de π/3 radians est 0.5.

### Calculer le cosinus de π radians {#calculate-the-cosine-of-π-radians}

```sql
SELECT COS(PI()) AS cosine_value;
```

Cette requête calcule le cosinus de π radians, ce qui donne `-1`, car le cosinus de π est -1.
