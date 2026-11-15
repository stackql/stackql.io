---
title: ACOS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: Calcular el arcocoseno (coseno inverso) de un número y devolver un ángulo en radianes con SQL en StackQL.
image: "/img/stackql-featured-image.png"
---
Calcula el arcocoseno (coseno inverso) de un número, que es el ángulo en radianes cuyo coseno es el número especificado.

Consulte también:  
[[`SELECT`]](/language-spec/select)

* * * 

## Sintaxis {#syntax}

```sql
SELECT ACOS(value) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*value*__  
Una expresión numérica en el intervalo de -1 a 1, ambos inclusive, cuyo arcocoseno se va a calcular.

## Valor(es) devuelto(s) {#return-values}
Devuelve el arcocoseno del valor especificado en radianes.

* * *

## Ejemplos {#examples}

### Calcular el arcocoseno de 0.5 {#calculate-the-arc-cosine-of-05}

```sql
SELECT ACOS(0.5) AS arc_cosine_value;
```

Esta consulta devuelve aproximadamente `1.047`, ya que el arcocoseno de 0.5 radianes es π/3, que equivale a unos 1.047 radianes.

### Calcular el arcocoseno de 1 {#calculate-the-arc-cosine-of-1}

```sql
SELECT ACOS(1) AS arc_cosine_value;
```

Esta consulta calcula el arcocoseno de 1, cuyo resultado es `0`, puesto que el coseno de 0 radianes es 1.
