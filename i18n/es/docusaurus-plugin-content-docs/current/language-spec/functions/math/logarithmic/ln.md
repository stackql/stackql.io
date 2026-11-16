---
title: LN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: Calcular el logaritmo natural de un número con SQL en StackQL.
image: "/img/stackql-featured-image.png"
---
Calcula el logaritmo natural (logaritmo en base e) de un número dado.

Consulte también:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Sintaxis {#syntax}

```sql
SELECT LN(numeric_expression) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*numeric_expression*__  
Una expresión numérica positiva cuyo logaritmo natural se va a calcular.

## Valor(es) devuelto(s) {#return-values}
Devuelve el logaritmo natural del valor numérico positivo especificado.

* * *

## Ejemplos {#examples}

### Calcular el logaritmo natural de 2.71828 (aproximadamente e) {#calculate-the-natural-logarithm-of-271828-approximately-e}

```sql
SELECT LN(2.71828) AS natural_log;
```

Esta consulta devuelve un valor cercano a `1`, puesto que el logaritmo natural de e (aproximadamente 2.71828) es 1.

### Calcular el logaritmo natural de un número mayor {#calculate-the-natural-logarithm-of-a-larger-number}

```sql
SELECT LN(10) AS natural_log;
```

Esta consulta calcula el logaritmo natural de 10 y proporciona el valor logarítmico de 10 en base e.
