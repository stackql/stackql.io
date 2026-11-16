---
title: ATANH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: Calcular la arcotangente hiperbólica (tangente hiperbólica inversa) de un número y devolver un ángulo en radianes con SQL en StackQL.
image: "/img/stackql-featured-image.png"
---
Calcula la arcotangente hiperbólica (tangente hiperbólica inversa) de un número, que es el valor en radianes cuya tangente hiperbólica es el número especificado.

Consulte también:  
[[`SELECT`]](/language-spec/select)

* * * 

## Sintaxis {#syntax}

```sql
SELECT ATANH(value) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*value*__  
Una expresión numérica en el intervalo de -1 a 1 (ambos excluidos), cuya arcotangente hiperbólica se va a calcular.

## Valor(es) devuelto(s) {#return-values}
Devuelve la arcotangente hiperbólica del valor especificado en radianes.

* * *

## Ejemplos {#examples}

### Calcular la arcotangente hiperbólica de 0.5 {#calculate-the-hyperbolic-arc-tangent-of-05}

```sql
SELECT ATANH(0.5) AS hyperbolic_arc_tangent_value;
```

Esta consulta devuelve aproximadamente `0.5493`, ya que la arcotangente hiperbólica de 0.5 es de unos 0.5493 radianes.

### Calcular la arcotangente hiperbólica de -0.5 {#calculate-the-hyperbolic-arc-tangent-of--05}

```sql
SELECT ATANH(-0.5) AS hyperbolic_arc_tangent_value;
```

Esta consulta calcula la arcotangente hiperbólica de -0.5, cuyo resultado es aproximadamente `-0.5493`, ya que este es el valor en radianes cuya tangente hiperbólica es -0.5.
