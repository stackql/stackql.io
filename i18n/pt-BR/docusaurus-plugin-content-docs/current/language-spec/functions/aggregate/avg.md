---
title: AVG
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Função de agregação AVG no StackQL: a média dos valores não NULL de uma coluna ou de um agrupamento."
image: "/img/stackql-featured-image.png"
---
Retorna o valor médio de todos os valores não `NULL` de uma coluna ou de um agrupamento de colunas.

Veja também:  
[[` SELECT `]](/language-spec/select)

* * * 

## Sintaxe {#syntax}

```sql
SELECT AVG(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## Argumentos {#arguments}

__*columnExpression*__  
Uma coluna ou expressão.

> Valores de string e BLOB que não podem ser representados como valores numéricos são interpretados como `0`.

> Pelo menos um valor de *columnExpression* deve ser não `NULL`; caso contrário, a função retorna `NULL`.

__*groupByColumn*__  
Uma ou mais colunas usadas para realizar operações de resumo ou de agregação. A cláusula `GROUP BY` retorna uma linha para cada agrupamento de colunas.

## Valor(es) retornado(s) {#return-values}

Retorna um valor de ponto flutuante que representa a média de *columnExpression*.

* * *

## Exemplos {#examples}

### Retornar a média de uma expressão de coluna em todo um recurso {#return-the-average-of-a-column-expression-over-an-entire-resource}

```sql
SELECT avg(json_array_length(disks)) as avg_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Retornar a média de uma expressão de coluna agrupada por outra expressão de coluna {#return-the-average-of-a-column-expression-grouped-by-another-column-expression}

```sql
SELECT SPLIT_PART(machineType, '/', -1) as machine_type, avg(json_array_length(disks)) as avg_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY SPLIT_PART(machineType, '/', -1);
```

### Usar `AVG` como função de janela para calcular médias móveis {#use-avg-as-a-window-function-to-calculate-moving-averages}

```sql
-- Calculate moving average of commit activity
WITH weekly_totals AS (
    SELECT
        week,
        SUM(json_each.value) as commits_this_week
    FROM github.repos.stats_commit_activity, JSON_EACH(days)
    WHERE owner = 'stackql'
      AND repo = 'stackql'
    GROUP BY week
)
SELECT
    week,
    commits_this_week,
    ROUND(AVG(commits_this_week) OVER (ORDER BY week ROWS BETWEEN 3 PRECEDING AND CURRENT ROW), 1) as four_week_moving_avg
FROM weekly_totals
ORDER BY week;
```

Para obter mais informações, consulte [https://www.sqlite.org/lang_aggfunc.html#avg](https://www.sqlite.org/lang_aggfunc.html#avg).
