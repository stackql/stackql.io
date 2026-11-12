---
title: MOD
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: Calculer le reste de la division d'un nombre par un autre avec SQL dans StackQL.
image: "/img/stackql-featured-image.png"
---
Calcule le reste de la division de *x* par *y* et renvoie le modulo. Cette fonction est similaire à l'opérateur "%", mais elle est compatible avec les arguments non entiers.

Voir aussi :  
[[`SELECT`]](/language-spec/select) 

* * * 

## Syntaxe {#syntax}

```sql
SELECT MOD(x, y) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*x*__  
Le dividende, qui est une expression numérique.

__*y*__  
Le diviseur, également une expression numérique.

## Valeur(s) renvoyée(s) {#return-values}
Renvoie le reste de la division de *x* par *y*.

* * *

## Exemples {#examples}

### Calculer le modulo de 10 divisé par 3 {#calculate-the-modulus-of-10-divided-by-3}

```sql
SELECT MOD(10, 3) AS remainder;
```
