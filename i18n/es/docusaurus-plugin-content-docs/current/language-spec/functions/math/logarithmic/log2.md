---
title: LOG2
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: Calcular el logaritmo en base 2 de un número con SQL en StackQL.
image: "/img/stackql-featured-image.png"
---
Calcula el logaritmo en base 2 de un número dado y devuelve la potencia a la que se debe elevar el número 2 para obtener el valor.

Consulte también:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Sintaxis {#syntax}

```sql
SELECT LOG2(numeric_expression) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*numeric_expression*__  
Una expresión numérica positiva cuyo logaritmo en base 2 se va a calcular.

## Valor(es) devuelto(s) {#return-values}
Devuelve el logaritmo en base 2 del valor numérico especificado.

* * *

## Ejemplos {#examples}

### Calcular el logaritmo en base 2 de 16 {#calculate-the-base-2-logarithm-of-16}

```sql
SELECT LOG2(16) AS log_result;
```

Esta consulta devuelve `4`, ya que \(2^4 = 16\).

### Calcular el logaritmo en base 2 de 32 {#calculate-the-base-2-logarithm-of-32}

```sql
SELECT LOG2(32) AS log_result;
```

Esta consulta calcula el logaritmo en base 2 de 32, cuyo resultado es `5`, puesto que \(2^5 = 32\).
