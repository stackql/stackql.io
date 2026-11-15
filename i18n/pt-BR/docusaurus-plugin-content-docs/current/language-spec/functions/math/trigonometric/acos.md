---
title: ACOS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: Calcular o arco cosseno (cosseno inverso) de um número e retornar um ângulo em radianos com SQL no StackQL.
image: "/img/stackql-featured-image.png"
---
Calcula o arco cosseno (cosseno inverso) de um número, que é o ângulo em radianos cujo cosseno é o número especificado.

Veja também:  
[[`SELECT`]](/language-spec/select)

* * * 

## Sintaxe {#syntax}

```sql
SELECT ACOS(value) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*value*__  
Uma expressão numérica no intervalo de -1 a 1, inclusive, cujo arco cosseno será calculado.

## Valor(es) retornado(s) {#return-values}
Retorna o arco cosseno do valor especificado em radianos.

* * *

## Exemplos {#examples}

### Calcular o arco cosseno de 0.5 {#calculate-the-arc-cosine-of-05}

```sql
SELECT ACOS(0.5) AS arc_cosine_value;
```

Esta consulta retorna aproximadamente `1.047`, pois o arco cosseno de 0.5 é π/3 radianos (60 graus), que equivale a cerca de 1.047 radianos.

### Calcular o arco cosseno de 1 {#calculate-the-arc-cosine-of-1}

```sql
SELECT ACOS(1) AS arc_cosine_value;
```

Esta consulta calcula o arco cosseno de 1, o que resulta em `0`, pois o cosseno de 0 radianos é 1.
