---
title: CEIL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Función CEIL en StackQL: redondear un argumento numérico hacia arriba al entero más cercano."
image: "/img/stackql-featured-image.png"
---
Redondea un argumento numérico hacia arriba al entero más cercano.  

Consulte también:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Sintaxis {#syntax}

```sql
SELECT CEIL(numeric_expression) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*numeric_expression*__  
Una expresión de la categoría de tipos de datos numéricos exactos o numéricos aproximados.

## Valor(es) devuelto(s) {#return-values}
Devuelve el menor entero mayor o igual que la expresión numérica especificada.

* * *

## Ejemplos {#examples}

### Redondear hacia arriba un número de punto flotante al entero (mayor) más cercano {#round-up-a-floating-point-number-to-the-closest-larger-integer}

```sql
SELECT name, timeCreated,
ceil(julianday('now')-julianday(timeCreated)) as days_since_ceiling
FROM google.storage.buckets WHERE project = 'stackql';
```
