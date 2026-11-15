---
title: TANH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: Calcular la tangente hiperbólica de un número y devolver el resultado con SQL en StackQL.
image: "/img/stackql-featured-image.png"
---
Calcula la tangente hiperbólica de un valor especificado.

Consulte también:  
[[`SELECT`]](/language-spec/select)

* * * 

## Sintaxis {#syntax}

```sql
SELECT TANH(value) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*value*__  
Una expresión numérica cuya tangente hiperbólica se va a calcular.

## Valor(es) devuelto(s) {#return-values}
Devuelve la tangente hiperbólica del valor especificado.

* * *

## Ejemplos {#examples}

### Calcular la tangente hiperbólica de 1 {#calculate-the-hyperbolic-tangent-of-1}

```sql
SELECT TANH(1) AS hyperbolic_tangent_value;
```

Esta consulta devuelve aproximadamente `0.7616`, ya que la tangente hiperbólica de 1 es de alrededor de 0.7616.

### Calcular la tangente hiperbólica de 0 {#calculate-the-hyperbolic-tangent-of-0}

```sql
SELECT TANH(0) AS hyperbolic_tangent_value;
```

Esta consulta calcula la tangente hiperbólica de 0, cuyo resultado es `0`, puesto que la tangente hiperbólica de 0 es exactamente 0.
