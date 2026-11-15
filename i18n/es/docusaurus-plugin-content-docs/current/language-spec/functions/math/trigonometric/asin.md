---
title: ASIN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: Calcular el arcoseno (seno inverso) de un número y devolver un ángulo en radianes con SQL en StackQL.
image: "/img/stackql-featured-image.png"
---
Calcula el arcoseno (seno inverso) de un número, que es el ángulo en radianes cuyo seno es el número especificado.

Consulte también:  
[[`SELECT`]](/language-spec/select)

* * * 

## Sintaxis {#syntax}

```sql
SELECT ASIN(value) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*value*__  
Una expresión numérica en el intervalo de -1 a 1, ambos inclusive, cuyo arcoseno se va a calcular.

## Valor(es) devuelto(s) {#return-values}
Devuelve el arcoseno del valor especificado en radianes.

* * *

## Ejemplos {#examples}

### Calcular el arcoseno de 0.5 {#calculate-the-arc-sine-of-05}

```sql
SELECT ASIN(0.5) AS arc_sine_value;
```

Esta consulta devuelve π/6 o aproximadamente `0.5236`, ya que el arcoseno de 0.5 corresponde a un ángulo de π/6 radianes.

### Calcular el arcoseno de -1 {#calculate-the-arc-sine-of--1}

```sql
SELECT ASIN(-1) AS arc_sine_value;
```

Esta consulta calcula el arcoseno de -1, cuyo resultado es -π/2 o aproximadamente `-1.5708`, ya que el seno de -π/2 es -1.
