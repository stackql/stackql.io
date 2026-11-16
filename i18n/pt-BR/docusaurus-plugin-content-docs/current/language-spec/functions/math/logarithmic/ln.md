---
title: LN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: Calcular o logaritmo natural de um número com SQL no StackQL.
image: "/img/stackql-featured-image.png"
---
Calcula o logaritmo natural (logaritmo na base e) de um determinado número.

Veja também:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Sintaxe {#syntax}

```sql
SELECT LN(numeric_expression) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*numeric_expression*__  
Uma expressão numérica positiva cujo logaritmo natural será calculado.

## Valor(es) retornado(s) {#return-values}
Retorna o logaritmo natural do valor numérico positivo especificado.

* * *

## Exemplos {#examples}

### Calcular o logaritmo natural de 2.71828 (aproximadamente e) {#calculate-the-natural-logarithm-of-271828-approximately-e}

```sql
SELECT LN(2.71828) AS natural_log;
```

Esta consulta retorna um valor próximo de `1`, pois o logaritmo natural de e (aproximadamente 2.71828) é 1.

### Calcular o logaritmo natural de um número maior {#calculate-the-natural-logarithm-of-a-larger-number}

```sql
SELECT LN(10) AS natural_log;
```

Esta consulta calcula o logaritmo natural de 10, fornecendo o valor logarítmico de 10 na base e.
