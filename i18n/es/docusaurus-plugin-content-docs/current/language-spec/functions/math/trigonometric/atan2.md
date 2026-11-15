---
title: ATAN2
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: Calcular la arcotangente de dos números (y/x) y devolver un ángulo en radianes con SQL en StackQL.
image: "/img/stackql-featured-image.png"
---
Calcula la arcotangente de dos números, en concreto el ángulo \( \theta \) en radianes cuya tangente es el cociente de dos números especificados (y dividido entre x), y trata correctamente los signos de las entradas para determinar el cuadrante correcto.

Consulte también:  
[[`SELECT`]](/language-spec/select)

* * * 

## Sintaxis {#syntax}

```sql
SELECT ATAN2(y, x) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*y*__  
La coordenada y (numerador) del punto.

__*x*__  
La coordenada x (denominador) del punto.

## Valor(es) devuelto(s) {#return-values}
Devuelve la arcotangente de y/x en radianes, teniendo en cuenta el cuadrante correcto del ángulo.

* * *

## Ejemplos {#examples}

### Calcular la arcotangente del punto (1,1) {#calculate-the-arc-tangent-of-the-point-11}

```sql
SELECT ATAN2(1, 1) AS arc_tangent_value;
```

Esta consulta devuelve π/4 o aproximadamente `0.785`, ya que la arcotangente del punto (1,1) corresponde a un ángulo de π/4 radianes en el primer cuadrante.

### Calcular la arcotangente del punto (-10, 10) {#calculate-the-arc-tangent-of-the-point--10-10}

```sql
SELECT ATAN2(10, -10) AS arc_tangent_value;
```

Esta consulta calcula el ángulo del punto (-10, 10), con y = 10 y x = -10, cuyo resultado es 3π/4 o aproximadamente `2.356`: la tangente de este ángulo es -1 y el punto está en el segundo cuadrante.
