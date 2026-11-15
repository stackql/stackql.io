---
title: SQRT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: Calcular a raiz quadrada de um número com SQL no StackQL.
image: "/img/stackql-featured-image.png"
---
Calcula a raiz quadrada de uma expressão numérica não negativa.

Veja também:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Sintaxe {#syntax}

```sql
SELECT SQRT(numeric_expression) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*numeric_expression*__  
Uma expressão numérica não negativa cuja raiz quadrada será calculada.

## Valor(es) retornado(s) {#return-values}
Retorna a raiz quadrada do valor numérico não negativo especificado.

* * *

## Exemplos {#examples}

### Calcular a raiz quadrada de um número {#calculate-the-square-root-of-a-number}

```sql
SELECT SQRT(9) AS square_root;
```
