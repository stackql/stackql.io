---
title: LOG10
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: Calcular el logaritmo en base 10 de un número con SQL en StackQL.
image: "/img/stackql-featured-image.png"
---
Calcula el logaritmo en base 10 de un número dado y determina la potencia a la que se debe elevar el número 10 para obtener el valor.

Consulte también:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Sintaxis {#syntax}

```sql
SELECT LOG10(numeric_expression) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*numeric_expression*__  
Una expresión numérica positiva cuyo logaritmo en base 10 se va a calcular.

## Valor(es) devuelto(s) {#return-values}
Devuelve el logaritmo en base 10 del valor numérico especificado.

* * *

## Ejemplos {#examples}

### Calcular el logaritmo en base 10 de 100 {#calculate-the-base-10-logarithm-of-100}

```sql
SELECT LOG10(100) AS log_result;
```

Esta consulta devuelve `2`, ya que 10^2 = 100.

### Calcular el logaritmo en base 10 de 1000 {#calculate-the-base-10-logarithm-of-1000}

```sql
SELECT LOG10(1000) AS log_result;
```

Esta consulta calcula el logaritmo en base 10 de 1000, cuyo resultado es `3`, puesto que 10^3 = 1000.
