---
title: SINH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: Calcular o seno hiperbólico de um número e retornar o resultado com SQL no StackQL.
image: "/img/stackql-featured-image.png"
---
Calcula o seno hiperbólico de um valor especificado.

Veja também:  
[[`SELECT`]](/language-spec/select)

* * * 

## Sintaxe {#syntax}

```sql
SELECT SINH(value) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*value*__  
Uma expressão numérica cujo seno hiperbólico será calculado.

## Valor(es) retornado(s) {#return-values}
Retorna o seno hiperbólico do valor especificado.

* * *

## Exemplos {#examples}

### Calcular o seno hiperbólico de 1 {#calculate-the-hyperbolic-sine-of-1}

```sql
SELECT SINH(1) AS hyperbolic_sine_value;
```

Esta consulta retorna aproximadamente `1.1752`, pois o seno hiperbólico de 1 é cerca de 1.1752.

### Calcular o seno hiperbólico de 0 {#calculate-the-hyperbolic-sine-of-0}

```sql
SELECT SINH(0) AS hyperbolic_sine_value;
```

Esta consulta calcula o seno hiperbólico de 0, com resultado `0`, já que o seno hiperbólico de 0 é exatamente 0.
