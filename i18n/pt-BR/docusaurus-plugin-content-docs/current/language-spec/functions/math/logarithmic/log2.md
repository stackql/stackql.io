---
title: LOG2
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: Calcular o logaritmo na base 2 de um número com SQL no StackQL.
image: "/img/stackql-featured-image.png"
---
Calcula o logaritmo na base 2 de um determinado número, retornando a potência à qual o número 2 deve ser elevado para obter o valor.

Veja também:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Sintaxe {#syntax}

```sql
SELECT LOG2(numeric_expression) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*numeric_expression*__  
Uma expressão numérica positiva cujo logaritmo na base 2 será calculado.

## Valor(es) retornado(s) {#return-values}
Retorna o logaritmo na base 2 do valor numérico especificado.

* * *

## Exemplos {#examples}

### Calcular o logaritmo na base 2 de 16 {#calculate-the-base-2-logarithm-of-16}

```sql
SELECT LOG2(16) AS log_result;
```

Esta consulta retorna `4`, pois \(2^4 = 16\).

### Calcular o logaritmo na base 2 de 32 {#calculate-the-base-2-logarithm-of-32}

```sql
SELECT LOG2(32) AS log_result;
```

Esta consulta calcula o logaritmo na base 2 de 32, o que resulta em `5`, pois \(2^5 = 32\).
