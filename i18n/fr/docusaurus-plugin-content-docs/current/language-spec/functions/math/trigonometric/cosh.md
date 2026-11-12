---
title: COSH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: Calculer le cosinus hyperbolique d'un nombre et renvoyer le résultat avec SQL dans StackQL.
image: "/img/stackql-featured-image.png"
---
Calcule le cosinus hyperbolique d'une valeur spécifiée.

Voir aussi :  
[[`SELECT`]](/language-spec/select)

* * * 

## Syntaxe {#syntax}

```sql
SELECT COSH(value) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*value*__  
Une expression numérique dont le cosinus hyperbolique doit être calculé.

## Valeur(s) renvoyée(s) {#return-values}
Renvoie le cosinus hyperbolique de la valeur spécifiée.

* * *

## Exemples {#examples}

### Calculer le cosinus hyperbolique de 1 {#calculate-the-hyperbolic-cosine-of-1}

```sql
SELECT COSH(1) AS hyperbolic_cosine_value;
```

Cette requête renvoie environ `1.5431`, car le cosinus hyperbolique de 1 vaut environ 1.5431.

### Calculer le cosinus hyperbolique de 0 {#calculate-the-hyperbolic-cosine-of-0}

```sql
SELECT COSH(0) AS hyperbolic_cosine_value;
```

Cette requête calcule le cosinus hyperbolique de 0, ce qui donne `1`, car le cosinus hyperbolique de 0 vaut exactement 1.
