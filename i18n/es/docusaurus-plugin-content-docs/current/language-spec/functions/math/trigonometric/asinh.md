---
title: ASINH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: Calcular el arcoseno hiperbólico (seno hiperbólico inverso) de un número y devolver un ángulo en radianes con SQL en StackQL.
image: "/img/stackql-featured-image.png"
---
Calcula el arcoseno hiperbólico (seno hiperbólico inverso) de un número, que es el valor \( \theta \) en radianes cuyo seno hiperbólico es el número especificado.

Consulte también:  
[[`SELECT`]](/language-spec/select)

* * * 

## Sintaxis {#syntax}

```sql
SELECT ASINH(value) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*value*__  
Una expresión numérica cuyo arcoseno hiperbólico se va a calcular.

## Valor(es) devuelto(s) {#return-values}
Devuelve el arcoseno hiperbólico del valor especificado en radianes.

* * *

## Ejemplos {#examples}

### Calcular el arcoseno hiperbólico de 0.5 {#calculate-the-hyperbolic-arc-sine-of-05}

```sql
SELECT ASINH(0.5) AS hyperbolic_arc_sine_value;
```

Esta consulta devuelve aproximadamente `0.4812`, ya que el arcoseno hiperbólico de 0.5 es de unos 0.4812 radianes.

### Calcular el arcoseno hiperbólico de -1 {#calculate-the-hyperbolic-arc-sine-of--1}

```sql
SELECT ASINH(-1) AS hyperbolic_arc_sine_value;
```

Esta consulta calcula el arcoseno hiperbólico de -1, cuyo resultado es aproximadamente `-0.8814`, ya que este es el valor en radianes cuyo seno hiperbólico es -1.
