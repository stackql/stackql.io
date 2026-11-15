---
title: POW
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: Calcular a potência de um número elevado a outro com SQL no StackQL.
image: "/img/stackql-featured-image.png"
---
Calcula *x* elevado à potência *y*.

Veja também:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Sintaxe {#syntax}

```sql
SELECT POW(x, y) FROM <multipartIdentifier>;
-- or using the equivalent
SELECT POWER(x, y) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*x*__  
O número base, que é uma expressão numérica.

__*y*__  
O expoente ao qual o número base *x* é elevado, também uma expressão numérica.

## Valor(es) retornado(s) {#return-values}
Retorna o resultado de *x* elevado à potência *y*.

* * *

## Exemplos {#examples}

### Calcular x elevado à potência y {#calculate-x-raised-to-the-power-y}


```sql
SELECT POW(2, 3) AS result;
```
