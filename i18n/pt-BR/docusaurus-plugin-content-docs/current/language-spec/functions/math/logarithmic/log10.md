---
title: LOG10
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: Calcular o logaritmo na base 10 de um número com SQL no StackQL.
image: "/img/stackql-featured-image.png"
---
Calcula o logaritmo na base 10 de um determinado número, determinando a potência à qual o número 10 deve ser elevado para obter o valor.

Veja também:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Sintaxe {#syntax}

```sql
SELECT LOG10(numeric_expression) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*numeric_expression*__  
Uma expressão numérica positiva cujo logaritmo na base 10 será calculado.

## Valor(es) retornado(s) {#return-values}
Retorna o logaritmo na base 10 do valor numérico especificado.

* * *

## Exemplos {#examples}

### Calcular o logaritmo na base 10 de 100 {#calculate-the-base-10-logarithm-of-100}

```sql
SELECT LOG10(100) AS log_result;
```

Esta consulta retorna `2`, pois 10^2 = 100.

### Calcular o logaritmo na base 10 de 1000 {#calculate-the-base-10-logarithm-of-1000}

```sql
SELECT LOG10(1000) AS log_result;
```

Esta consulta calcula o logaritmo na base 10 de 1000, o que resulta em `3`, pois 10^3 = 1000.
