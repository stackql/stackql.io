---
title: COS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: Calcular el coseno de un ángulo, expresado en radianes, con SQL en StackQL.
image: "/img/stackql-featured-image.png"
---
Calcula el coseno de un ángulo especificado, expresado en radianes.

Consulte también:  
[[`SELECT`]](/language-spec/select)

* * * 

## Sintaxis {#syntax}

```sql
SELECT COS(radian_value) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*radian_value*__  
Una expresión numérica que especifica el ángulo en radianes cuyo coseno se va a calcular.

## Valor(es) devuelto(s) {#return-values}
Devuelve el coseno del ángulo especificado.

* * *

## Ejemplos {#examples}

### Calcular el coseno de π/3 radianes {#calculate-the-cosine-of-π3-radians}

```sql
SELECT COS(PI() / 3) AS cosine_value;
```

Esta consulta devuelve `0.5000000000000001`, la aproximación de punto flotante del coseno de π/3 radianes, que es 0.5.

### Calcular el coseno de π radianes {#calculate-the-cosine-of-π-radians}

```sql
SELECT COS(PI()) AS cosine_value;
```

Esta consulta calcula el coseno de π radianes, cuyo resultado es `-1`, puesto que el coseno de π es -1.
