---
title: ASIN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: Calcular o arco seno (seno inverso) de um número e retornar um ângulo em radianos com SQL no StackQL.
image: "/img/stackql-featured-image.png"
---
Calcula o arco seno (seno inverso) de um número, que é o ângulo em radianos cujo seno é o número especificado.

Veja também:  
[[`SELECT`]](/language-spec/select)

* * * 

## Sintaxe {#syntax}

```sql
SELECT ASIN(value) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*value*__  
Uma expressão numérica no intervalo de -1 a 1, inclusive, cujo arco seno será calculado.

## Valor(es) retornado(s) {#return-values}
Retorna o arco seno do valor especificado em radianos.

* * *

## Exemplos {#examples}

### Calcular o arco seno de 0.5 {#calculate-the-arc-sine-of-05}

```sql
SELECT ASIN(0.5) AS arc_sine_value;
```

Esta consulta retorna π/6, ou aproximadamente `0.5236`, pois o arco seno de 0.5 corresponde a um ângulo de π/6 radianos.

### Calcular o arco seno de -1 {#calculate-the-arc-sine-of--1}

```sql
SELECT ASIN(-1) AS arc_sine_value;
```

Esta consulta calcula o arco seno de -1, o que resulta em -π/2, ou aproximadamente `-1.5708`, pois o seno de -π/2 é -1.
