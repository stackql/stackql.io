---
title: POW
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: Calculer un nombre élevé à la puissance d'un autre avec SQL dans StackQL.
image: "/img/stackql-featured-image.png"
---
Calcule *x* élevé à la puissance *y*.

Voir aussi :  
[[`SELECT`]](/language-spec/select) 

* * * 

## Syntaxe {#syntax}

```sql
SELECT POW(x, y) FROM <multipartIdentifier>;
-- or using the equivalent
SELECT POWER(x, y) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*x*__  
La base, qui est une expression numérique.

__*y*__  
L'exposant auquel la base *x* est élevée, également une expression numérique.

## Valeur(s) renvoyée(s) {#return-values}
Renvoie le résultat de *x* élevé à la puissance *y*.

* * *

## Exemples {#examples}

### Calculer x élevé à la puissance y {#calculate-x-raised-to-the-power-y}


```sql
SELECT POW(2, 3) AS result;
```
