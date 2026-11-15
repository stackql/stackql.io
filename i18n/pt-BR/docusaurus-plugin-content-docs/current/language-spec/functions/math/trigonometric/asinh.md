---
title: ASINH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: Calcular o arco seno hiperbólico (seno hiperbólico inverso) de um número e retornar um ângulo em radianos com SQL no StackQL.
image: "/img/stackql-featured-image.png"
---
Calcula o arco seno hiperbólico (seno hiperbólico inverso) de um número, que é o valor \( \theta \) em radianos cujo seno hiperbólico é o número especificado.

Veja também:  
[[`SELECT`]](/language-spec/select)

* * * 

## Sintaxe {#syntax}

```sql
SELECT ASINH(value) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*value*__  
Uma expressão numérica cujo arco seno hiperbólico será calculado.

## Valor(es) retornado(s) {#return-values}
Retorna o arco seno hiperbólico do valor especificado em radianos.

* * *

## Exemplos {#examples}

### Calcular o arco seno hiperbólico de 0.5 {#calculate-the-hyperbolic-arc-sine-of-05}

```sql
SELECT ASINH(0.5) AS hyperbolic_arc_sine_value;
```

Esta consulta retorna aproximadamente `0.4812`, pois o arco seno hiperbólico de 0.5 é cerca de 0.4812 radianos.

### Calcular o arco seno hiperbólico de -1 {#calculate-the-hyperbolic-arc-sine-of--1}

```sql
SELECT ASINH(-1) AS hyperbolic_arc_sine_value;
```

Esta consulta calcula o arco seno hiperbólico de -1, o que resulta em aproximadamente `-0.8814`, pois esse é o valor em radianos cujo seno hiperbólico é -1.
