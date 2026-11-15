---
title: FLOOR
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Función FLOOR en StackQL: redondear un argumento numérico hacia abajo al entero más cercano."
image: "/img/stackql-featured-image.png"
---
Redondea un argumento numérico hacia abajo al entero más cercano.  

Consulte también:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Sintaxis {#syntax}

```sql
SELECT FLOOR(numeric_expression) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*numeric_expression*__  
Una expresión de la categoría de tipos de datos numéricos exactos o numéricos aproximados.

## Valor(es) devuelto(s) {#return-values}
Devuelve el mayor entero menor o igual que la expresión numérica especificada.

* * *

## Ejemplos {#examples}

### Redondear hacia abajo un número de punto flotante al entero (menor) más cercano {#round-down-a-floating-point-number-to-the-closest-smaller-integer}

```sql
SELECT name, timeCreated,
floor(julianday('now')-julianday(timeCreated)) as days_since_floor
FROM google.storage.buckets WHERE project = 'stackql';
```
