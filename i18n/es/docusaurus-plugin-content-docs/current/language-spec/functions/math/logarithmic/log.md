---
title: LOG
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: Calcular el logaritmo de un número en cualquier base con SQL en StackQL.
image: "/img/stackql-featured-image.png"
---
Calcula el logaritmo de un número en una base especificada, una forma más general de función logarítmica en comparación con LN, que usa la base \(e\).

Consulte también:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Sintaxis {#syntax}

```sql
SELECT LOG(base, numeric_expression) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*base*__  
La base del logaritmo, una expresión numérica positiva distinta de 1.

__*numeric_expression*__  
Una expresión numérica positiva cuyo logaritmo se va a calcular.

## Valor(es) devuelto(s) {#return-values}
Devuelve el logaritmo del valor numérico especificado en la base especificada.

* * *

## Ejemplos {#examples}

### Calcular el logaritmo de 1000 en base 10 {#calculate-the-logarithm-of-1000-with-base-10}

```sql
SELECT LOG(10, 1000) AS log_result;
```

Esta consulta devuelve `3`, ya que \(10^3 = 1000\).

### Calcular el logaritmo de 16 en base 2 {#calculate-the-logarithm-of-16-with-base-2}

```sql
SELECT LOG(2, 16) AS log_result;
```
Esta consulta calcula el logaritmo de 16 en base 2, cuyo resultado es `4`, puesto que \(2^4 = 16\).
