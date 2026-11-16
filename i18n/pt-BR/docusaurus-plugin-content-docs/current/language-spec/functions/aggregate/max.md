---
title: MAX
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Função de agregação MAX no StackQL: o maior valor de uma coluna ou de um agrupamento de colunas."
image: "/img/stackql-featured-image.png"
---
Retorna o valor máximo com base em uma coluna de entrada ou em um agrupamento de colunas.  

Veja também:  
[[` SELECT `]](/language-spec/select) 

* * * 

## Sintaxe {#syntax}

```sql
/* aggregate function */
SELECT MAX(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```
```sql
/* scalar (multi-argument) function */
SELECT MAX(scalarExpression1, scalarExpressionN, ...) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

### Função de agregação {#aggregate-function}

__*columnExpression*__  
Uma coluna ou expressão, incluindo operadores (`+`, `-`, ...).

> A função `MAX` retorna o maior valor da coluna indicada com base no tipo de dados da coluna; por exemplo, uma operação `MAX` em uma coluna de inteiros retorna o maior inteiro dessa coluna ou desse agrupamento de colunas, enquanto uma operação `MAX` em uma coluna ou agrupamento de colunas de strings retorna o valor que fica por último na ordenação, comparando as strings com a função de ordenação `BINARY` (byte a byte, de modo que as letras maiúsculas ficam antes das minúsculas).

__*groupByColumn*__  
Uma ou mais colunas usadas para realizar operações de resumo ou de agregação.  A cláusula `GROUP BY` retorna uma linha para cada agrupamento de colunas.

> A função `MAX` ignora valores `NULL`.

> A função `MAX` retorna `NULL` se todos os valores do grupo forem `NULL`.

### Função escalar {#scalar-function}

__*scalarExpression*__  
Uma lista de colunas ou expressões (2 ou mais) da qual o maior valor é determinado e retornado. 

> A função escalar `MAX` retorna `NULL` se algum argumento for `NULL`. 

> A função `MAX` escalar ou de vários argumentos procura em seus argumentos, da esquerda para a direita, um argumento que defina uma função de ordenação e usa essa função de ordenação em todas as comparações de strings. 

> Se nenhum dos argumentos da função escalar `MAX` definir uma função de ordenação, a função de ordenação `BINARY` é usada. 

> Se apenas um argumento for fornecido, a função de agregação `MAX` é invocada.

## Valor(es) retornado(s) {#return-values}

Retorna o valor máximo com base no tipo de dados de entrada (ou no agrupamento).

* * *

## Exemplos {#examples}

### Retornar o valor máximo de uma coluna {#return-the-maximum-value-for-a-column}

```sql
SELECT name, location, max(timeCreated) 
FROM google.storage.buckets WHERE project = 'stackql';
```

### Retornar o valor máximo para cada valor de uma coluna {#return-the-maximum-value-for-each-value-in-a-column}

```sql
SELECT name, location,
max(round(julianday('now')-julianday(timeCreated))) as age
FROM google.storage.buckets WHERE project = 'stackql'
GROUP BY location;
```

### Retornar o valor máximo de uma lista de valores com a função escalar `max` {#return-the-maximum-value-from-a-list-of-values-using-the-scalar-max-function}

```sql
SELECT max(CAST(json_extract(disks, '$[0].diskSizeGb') AS INTEGER), CAST(json_extract(disks, '$[1].diskSizeGb') AS INTEGER)) as largest_disk
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Usar `MAX` como função de janela para encontrar valores máximos dentro de partições {#use-max-as-a-window-function-to-find-maximum-values-within-partitions}

```sql
-- Find the most recent issue in each state
SELECT
    number,
    title,
    state,
    created_at,
    MAX(created_at) OVER (PARTITION BY state) as latest_in_state
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY state, created_at DESC;
```

Para obter mais informações, consulte [https://www.sqlite.org/lang_aggfunc.html#max_agg](https://www.sqlite.org/lang_aggfunc.html#max_agg) ou [https://www.sqlite.org/lang_corefunc.html#max_scalar](https://www.sqlite.org/lang_corefunc.html#max_scalar)
