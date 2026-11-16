---
title: ACOSH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: Calcular el arcocoseno hiperbólico (coseno hiperbólico inverso) de un número y devolver un ángulo en radianes con SQL en StackQL.
image: "/img/stackql-featured-image.png"
---
Calcula el arcocoseno hiperbólico (coseno hiperbólico inverso) de un número, que es el valor no negativo en radianes cuyo coseno hiperbólico es el número especificado.

Consulte también:  
[[`SELECT`]](/language-spec/select)

* * * 

## Sintaxis {#syntax}

```sql
SELECT ACOSH(value) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*value*__  
Una expresión numérica mayor o igual que 1, cuyo arcocoseno hiperbólico se va a calcular.

## Valor(es) devuelto(s) {#return-values}
Devuelve el arcocoseno hiperbólico del valor especificado en radianes.

* * *

## Ejemplos {#examples}

### Calcular el arcocoseno hiperbólico de 1 {#calculate-the-hyperbolic-arc-cosine-of-1}

```sql
SELECT ACOSH(1) AS hyperbolic_arc_cosine_value;
```

Esta consulta devuelve `0`, ya que el arcocoseno hiperbólico de 1 es 0 radianes.

### Calcular el arcocoseno hiperbólico de 10 {#calculate-the-hyperbolic-arc-cosine-of-10}

```sql
SELECT ACOSH(10) AS hyperbolic_arc_cosine_value;
```

Esta consulta calcula el arcocoseno hiperbólico de 10, cuyo resultado es aproximadamente `2.9932`, ya que este es el valor en radianes cuyo coseno hiperbólico es 10.
