---
title: MIN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Função de agregação MIN no StackQL: o menor valor de uma coluna ou de um agrupamento de colunas."
image: "/img/stackql-featured-image.png"
---
Retorna o valor mínimo com base em uma coluna de entrada ou em um agrupamento de colunas.  

Veja também:  
[[` SELECT `]](/language-spec/select) 

* * * 

## Sintaxe {#syntax}

```sql
/* aggregate function */
SELECT MIN(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```
```sql
/* scalar (multi-argument) function */
SELECT MIN(scalarExpression1, scalarExpressionN, ...) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

### Função de agregação {#aggregate-function}

__*columnExpression*__  
Uma coluna ou expressão, incluindo operadores (`+`, `-`, ...).

> A função `MIN` retorna o menor valor da coluna indicada com base no tipo de dados da coluna; por exemplo, uma operação `MIN` em uma coluna de inteiros retorna o menor inteiro dessa coluna ou desse agrupamento de colunas, enquanto uma operação `MIN` em uma coluna ou agrupamento de colunas de strings retorna o valor que fica primeiro na ordenação, comparando as strings com a função de ordenação `BINARY` (byte a byte, de modo que as letras maiúsculas ficam antes das minúsculas).

__*groupByColumn*__  
Uma ou mais colunas usadas para realizar operações de resumo ou de agregação.  A cláusula `GROUP BY` retorna uma linha para cada agrupamento de colunas.

> A função `MIN` ignora valores `NULL`.

> A função `MIN` retorna `NULL` se todos os valores do grupo forem `NULL`.

### Função escalar {#scalar-function}

__*scalarExpression*__  
Uma lista de colunas ou expressões (2 ou mais) da qual o menor valor é determinado e retornado. 

> A função escalar `MIN` retorna `NULL` se algum argumento for `NULL`. 

> A função `MIN` escalar ou de vários argumentos procura em seus argumentos, da esquerda para a direita, um argumento que defina uma função de ordenação e usa essa função de ordenação em todas as comparações de strings. 

> Se nenhum dos argumentos da função escalar `MIN` definir uma função de ordenação, a função de ordenação `BINARY` é usada. 

> Se apenas um argumento for fornecido, a função de agregação `MIN` é invocada.

## Valor(es) retornado(s) {#return-values}

Retorna o valor mínimo com base no tipo de dados de entrada (ou no agrupamento).

* * *

## Exemplos {#examples}

### Retornar o valor mínimo de uma coluna {#return-the-minimum-value-for-a-column}

```sql
SELECT name, location, min(timeCreated) 
FROM google.storage.buckets WHERE project = 'stackql';
```

### Retornar o valor mínimo para cada valor de uma coluna {#return-the-minimum-value-for-each-value-in-a-column}

```sql
SELECT name, location,
min(round(julianday('now')-julianday(timeCreated))) as age
FROM google.storage.buckets WHERE project = 'stackql'
GROUP BY location;
```

### Retornar o valor mínimo de uma lista de valores com a função escalar `min` {#return-the-minimum-value-from-a-list-of-values-using-the-scalar-min-function}

```sql
SELECT min(json_extract(disks, '$[0].diskSizeGb'),json_extract(disks, '$[1].diskSizeGb')) as smallest_disk
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Usar `MIN` como função de janela para encontrar valores mínimos dentro de partições {#use-min-as-a-window-function-to-find-minimum-values-within-partitions}

```sql
-- Find the earliest issue in each state
SELECT
    number,
    title,
    state,
    created_at,
    MIN(created_at) OVER (PARTITION BY state) as earliest_in_state
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY state, created_at;
```

Para obter mais informações, consulte [https://www.sqlite.org/lang_aggfunc.html#min_agg](https://www.sqlite.org/lang_aggfunc.html#min_agg) ou [https://www.sqlite.org/lang_corefunc.html#min_scalar](https://www.sqlite.org/lang_corefunc.html#min_scalar)
