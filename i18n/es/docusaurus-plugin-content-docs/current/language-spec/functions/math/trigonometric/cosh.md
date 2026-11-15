---
title: COSH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: Calcular el coseno hiperbólico de un número y devolver el resultado con SQL en StackQL.
image: "/img/stackql-featured-image.png"
---
Calcula el coseno hiperbólico de un valor especificado.

Consulte también:  
[[`SELECT`]](/language-spec/select)

* * * 

## Sintaxis {#syntax}

```sql
SELECT COSH(value) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*value*__  
Una expresión numérica cuyo coseno hiperbólico se va a calcular.

## Valor(es) devuelto(s) {#return-values}
Devuelve el coseno hiperbólico del valor especificado.

* * *

## Ejemplos {#examples}

### Calcular el coseno hiperbólico de 1 {#calculate-the-hyperbolic-cosine-of-1}

```sql
SELECT COSH(1) AS hyperbolic_cosine_value;
```

Esta consulta devuelve aproximadamente `1.5431`, ya que el coseno hiperbólico de 1 es de alrededor de 1.5431.

### Calcular el coseno hiperbólico de 0 {#calculate-the-hyperbolic-cosine-of-0}

```sql
SELECT COSH(0) AS hyperbolic_cosine_value;
```

Esta consulta calcula el coseno hiperbólico de 0, cuyo resultado es `1`, puesto que el coseno hiperbólico de 0 es exactamente 1.
