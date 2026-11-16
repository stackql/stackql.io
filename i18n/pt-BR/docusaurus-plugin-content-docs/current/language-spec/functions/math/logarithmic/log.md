---
title: LOG
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: Calcular o logaritmo de um número em qualquer base com SQL no StackQL.
image: "/img/stackql-featured-image.png"
---
Calcula o logaritmo de um número em uma base especificada, uma forma mais geral de função logarítmica em comparação com LN, que usa a base e.

Veja também:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Sintaxe {#syntax}

```sql
SELECT LOG(base, numeric_expression) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*base*__  
A base do logaritmo, uma expressão numérica positiva diferente de 1.

__*numeric_expression*__  
Uma expressão numérica positiva cujo logaritmo será calculado.

## Valor(es) retornado(s) {#return-values}
Retorna o logaritmo do valor numérico especificado na base especificada.

* * *

## Exemplos {#examples}

### Calcular o logaritmo de 1000 na base 10 {#calculate-the-logarithm-of-1000-with-base-10}

```sql
SELECT LOG(10, 1000) AS log_result;
```

Esta consulta retorna `3`, pois 10^3 = 1000.

### Calcular o logaritmo de 16 na base 2 {#calculate-the-logarithm-of-16-with-base-2}

```sql
SELECT LOG(2, 16) AS log_result;
```
Esta consulta calcula o logaritmo de 16 na base 2, o que resulta em `4`, pois 2^4 = 16.
