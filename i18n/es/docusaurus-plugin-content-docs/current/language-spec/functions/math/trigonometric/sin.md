---
title: SIN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: Calcular el seno de un ángulo, expresado en radianes, con SQL en StackQL.
image: "/img/stackql-featured-image.png"
---
Calcula el seno de un ángulo especificado, expresado en radianes.

Consulte también:  
[[`SELECT`]](/language-spec/select)

* * * 

## Sintaxis {#syntax}

```sql
SELECT SIN(radian_value) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*radian_value*__  
Una expresión numérica que especifica el ángulo en radianes cuyo seno se va a calcular.

## Valor(es) devuelto(s) {#return-values}
Devuelve el seno del ángulo especificado.

* * *

## Ejemplos {#examples}

### Calcular el seno de π/2 radianes {#calculate-the-sine-of-π2-radians}

```sql
SELECT SIN(PI() / 2) AS sine_value;
```

Esta consulta devuelve `1`, ya que el seno de π/2 radianes es 1.

### Calcular el seno de π radianes {#calculate-the-sine-of-π-radians}

```sql
SELECT SIN(PI()) AS sine_value;
```

Esta consulta calcula el seno de π radianes, cuyo resultado es aproximadamente `0`, ya que el seno de π es 0.
