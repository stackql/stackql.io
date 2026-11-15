---
title: ATAN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: Calcular la arcotangente (tangente inversa) de un número y devolver un ángulo en radianes con SQL en StackQL.
image: "/img/stackql-featured-image.png"
---
Calcula la arcotangente (tangente inversa) de un número, que es el ángulo en radianes cuya tangente es el número especificado.

Consulte también:  
[[`SELECT`]](/language-spec/select)

* * * 

## Sintaxis {#syntax}

```sql
SELECT ATAN(value) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*value*__  
Una expresión numérica cuya arcotangente se va a calcular.

## Valor(es) devuelto(s) {#return-values}
Devuelve la arcotangente del valor especificado en radianes.

* * *

## Ejemplos {#examples}

### Calcular la arcotangente de 1 {#calculate-the-arc-tangent-of-1}

```sql
SELECT ATAN(1) AS arc_tangent_value;
```

Esta consulta devuelve π/4 o aproximadamente `0.785`, ya que la arcotangente de 1 es π/4 radianes.

### Calcular la arcotangente de 0 {#calculate-the-arc-tangent-of-0}

```sql
SELECT ATAN(0) AS arc_tangent_value;
```

Esta consulta calcula la arcotangente de 0, cuyo resultado es `0`, puesto que la tangente de 0 radianes es 0.
