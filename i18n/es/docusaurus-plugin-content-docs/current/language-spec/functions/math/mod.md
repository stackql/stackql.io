---
title: MOD
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: Calcular el resto de la división de un número entre otro con SQL en StackQL.
image: "/img/stackql-featured-image.png"
---
Calcula el resto de dividir *x* entre *y* y devuelve el módulo. Es similar a usar el operador '%', pero es compatible con argumentos no enteros.

Consulte también:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Sintaxis {#syntax}

```sql
SELECT MOD(x, y) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*x*__  
El dividendo, que es una expresión numérica.

__*y*__  
El divisor, también una expresión numérica.

## Valor(es) devuelto(s) {#return-values}
Devuelve el resto de dividir *x* entre *y*.

* * *

## Ejemplos {#examples}

### Calcular el módulo de 10 dividido entre 3 {#calculate-the-modulus-of-10-divided-by-3}

```sql
SELECT MOD(10, 3) AS remainder;
```
