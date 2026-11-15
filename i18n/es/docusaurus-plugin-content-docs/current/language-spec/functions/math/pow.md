---
title: POW
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: Calcular la potencia de un número elevado a otro con SQL en StackQL.
image: "/img/stackql-featured-image.png"
---
Calcula *x* elevado a la potencia *y*.

Consulte también:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Sintaxis {#syntax}

```sql
SELECT POW(x, y) FROM <multipartIdentifier>;
-- or using the equivalent
SELECT POWER(x, y) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*x*__  
El número base, que es una expresión numérica.

__*y*__  
El exponente al que se eleva el número base *x*, también una expresión numérica.

## Valor(es) devuelto(s) {#return-values}
Devuelve el resultado de *x* elevado a la potencia *y*.

* * *

## Ejemplos {#examples}

### Calcular x elevado a la potencia y {#calculate-x-raised-to-the-power-y}


```sql
SELECT POW(2, 3) AS result;
```
