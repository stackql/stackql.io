---
title: SUM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Função de agregação SUM no StackQL: o total dos valores não NULL de uma coluna ou agrupamento, retornado como inteiro quando todas as entradas são inteiros."
image: "/img/stackql-featured-image.png"
---
Retorna a soma de todos os valores não `NULL` de uma coluna ou de um agrupamento de colunas.  

Veja também:  
[[` SELECT `]](/language-spec/select) [[` TOTAL `]](/language-spec/functions/aggregate/total)

* * * 

:::tip

Use a função [**TOTAL**](/language-spec/functions/aggregate/total) para somar números de ponto flutuante ou valores longos.

:::

## Sintaxe {#syntax}

```sql
SELECT SUM(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## Argumentos {#arguments}

__*columnExpression*__  
Uma coluna ou expressão.

> Se todos os valores de *columnExpression* forem `NULL`, `SUM` retorna `NULL`.  

> `SUM` gera uma exceção "integer overflow" se ocorrer um estouro de inteiro em qualquer ponto do cálculo.  A função [`TOTAL`](/language-spec/functions/aggregate/total) nunca gera um estouro de inteiro.  

__*groupByColumn*__  
Uma ou mais colunas usadas para realizar operações de resumo ou de agregação.  A cláusula `GROUP BY` retorna uma linha para cada agrupamento de colunas.

## Valor(es) retornado(s) {#return-values}

Retorna um inteiro que representa a soma de *columnExpression* se todas as entradas não `NULL` forem inteiros. Se alguma entrada não for um inteiro ou for `NULL`, retorna um valor de ponto flutuante que pode ser uma aproximação da soma real.

* * *

## Exemplos {#examples}

### Retornar a soma de uma expressão de coluna em todo um recurso {#return-the-sum-of-a-column-expression-over-an-entire-resource}

```sql
SELECT sum(json_array_length(disks)) as sum_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Retornar a soma de uma expressão de coluna agrupada por outra expressão de coluna {#return-the-sum-of-a-column-expression-grouped-by-another-column-expression}

```sql
SELECT tags, sum(json_array_length(disks)) as sum_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY json_extract(tags, '$.instanceType');
```

### Usar `SUM` como função de janela para calcular totais acumulados {#use-sum-as-a-window-function-to-calculate-running-totals}

```sql
-- Calculate running total and percentage of contributions
SELECT
    login,
    contributions,
    SUM(contributions) OVER (ORDER BY contributions DESC) as running_total,
    SUM(contributions) OVER () as total_contributions,
    ROUND(100.0 * contributions / SUM(contributions) OVER (), 2) as pct_of_total
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

Para obter mais informações, consulte [https://www.sqlite.org/lang_aggfunc.html#sumunc](https://www.sqlite.org/lang_aggfunc.html#sumunc).
