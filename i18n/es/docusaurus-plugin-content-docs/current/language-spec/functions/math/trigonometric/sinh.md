---
title: SINH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: Calcular el seno hiperbólico de un número y devolver el resultado con SQL en StackQL.
image: "/img/stackql-featured-image.png"
---
Calcula el seno hiperbólico de un valor especificado.

Consulte también:  
[[`SELECT`]](/language-spec/select)

* * * 

## Sintaxis {#syntax}

```sql
SELECT SINH(value) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*value*__  
Una expresión numérica cuyo seno hiperbólico se va a calcular.

## Valor(es) devuelto(s) {#return-values}
Devuelve el seno hiperbólico del valor especificado.

* * *

## Ejemplos {#examples}

### Calcular el seno hiperbólico de 1 {#calculate-the-hyperbolic-sine-of-1}

```sql
SELECT SINH(1) AS hyperbolic_sine_value;
```

Esta consulta devuelve aproximadamente `1.1752`, ya que el seno hiperbólico de 1 es de alrededor de 1.1752.

### Calcular el seno hiperbólico de 0 {#calculate-the-hyperbolic-sine-of-0}

```sql
SELECT SINH(0) AS hyperbolic_sine_value;
```

Esta consulta calcula el seno hiperbólico de 0, cuyo resultado es `0`, puesto que el seno hiperbólico de 0 es exactamente 0.
