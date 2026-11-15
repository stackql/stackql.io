---
title: Funções de janela
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Funções de janela no StackQL: classificações, totais acumulados, deslocamentos e distribuições em linhas relacionadas, com PARTITION BY, ORDER BY e cláusulas de quadro."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

As funções de janela realizam cálculos em um conjunto de linhas relacionadas à linha atual. Ao contrário das funções de agregação, que retornam um único resultado para um grupo de linhas, as funções de janela retornam um valor para cada linha, considerando uma "janela" de linhas relacionadas.

Veja também:
[[` SELECT `]](/language-spec/select) [[` WITH (CTEs) `]](/language-spec/with) [[ Referência de funções de janela ]](/language-spec/functions/window/row_number)

* * *

## Sintaxe {#syntax}

As funções de janela usam a cláusula `OVER` para definir a especificação da janela:

```sql
SELECT <windowFunction> OVER ( <windowSpec> ) FROM <multipartIdentifier>;
```

*windowFunctionCall::=*

<RailroadDiagram 
type="windowFunctionCall"
/>

### Função de janela {#window-function}

As funções de janela são funções especializadas que realizam cálculos em um conjunto de linhas definido pela especificação da janela. Elas podem ser funções de janela dedicadas (como `ROW_NUMBER` ou `LAG`) ou funções de agregação (como `SUM` ou `COUNT`) usadas com uma cláusula `OVER`.

```sql
{ <aggregateFunction> | ROW_NUMBER() | RANK() | DENSE_RANK() | PERCENT_RANK() 
  | CUME_DIST() | NTILE(<int>) | LAG(<expr> [,<offset> [,<default>]]) 
  | LEAD(<expr> [,<offset> [,<default>]]) | FIRST_VALUE(<expr>) 
  | LAST_VALUE(<expr>) | NTH_VALUE(<expr>, <int>) }
```  

*windowFunction::=*

<RailroadDiagram 
type="windowFunction"
/>

### Especificação da janela {#window-specification}

A especificação da janela define como as linhas são particionadas, ordenadas e enquadradas para o cálculo da função de janela. `PARTITION BY` divide as linhas em grupos, `ORDER BY` determina a sequência dentro de cada partição e a cláusula de quadro opcional especifica quais linhas, em relação à linha atual, são incluídas no cálculo.

```sql
[ <windowName> ]
[ PARTITION BY <fieldList> ]
[ ORDER BY <fieldList> [ ASC | DESC ] ]
[ { ROWS | RANGE | GROUPS } 
    { UNBOUNDED PRECEDING | <int> PRECEDING | CURRENT ROW }
  | { ROWS | RANGE | GROUPS } BETWEEN 
    { UNBOUNDED PRECEDING | <int> PRECEDING | CURRENT ROW | <int> FOLLOWING }
    AND
    { <int> PRECEDING | CURRENT ROW | <int> FOLLOWING | UNBOUNDED FOLLOWING } ]
```

*windowSpec::=*

<RailroadDiagram 
type="windowSpec"
/>

## Funções de janela disponíveis {#available-window-functions}

| Categoria | Funções |
|----------|-----------|
| **Classificação** | [__`ROW_NUMBER()`__](/language-spec/functions/window/row_number), [__`RANK()`__](/language-spec/functions/window/rank), [__`DENSE_RANK()`__](/language-spec/functions/window/dense_rank), [__`NTILE()`__](/language-spec/functions/window/ntile) |
| **Deslocamento** | [__`LAG()`__](/language-spec/functions/window/lag), [__`LEAD()`__](/language-spec/functions/window/lead), [__`FIRST_VALUE()`__](/language-spec/functions/window/first_value), [__`LAST_VALUE()`__](/language-spec/functions/window/last_value), [__`NTH_VALUE()`__](/language-spec/functions/window/nth_value) |
| **Distribuição** | [__`PERCENT_RANK()`__](/language-spec/functions/window/percent_rank), [__`CUME_DIST()`__](/language-spec/functions/window/cume_dist) |
| **Agregação** | [__`SUM()`__](/language-spec/functions/aggregate/sum), [__`COUNT()`__](/language-spec/functions/aggregate/count), [__`AVG()`__](/language-spec/functions/aggregate/avg), [__`MIN()`__](/language-spec/functions/aggregate/min), [__`MAX()`__](/language-spec/functions/aggregate/max) com a cláusula __`OVER`__ |

* * *

## Exemplos {#examples}

### Classificação com `ROW_NUMBER`, `RANK` e `DENSE_RANK` {#ranking-with-row_number-rank-and-dense_rank}

Use funções de classificação para atribuir classificações às linhas com base nos valores das colunas.

```sql
-- Rank contributors by contribution count
SELECT
    login,
    contributions,
    ROW_NUMBER() OVER (ORDER BY contributions DESC) as row_num,
    RANK() OVER (ORDER BY contributions DESC) as rank,
    DENSE_RANK() OVER (ORDER BY contributions DESC) as dense_rank
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

### Totais acumulados e porcentagens {#running-totals-and-percentages}

Funções de agregação como `SUM` e `COUNT` podem ser usadas como funções de janela para calcular totais acumulados e porcentagens.

```sql
-- Running total and percentage of contributions
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

### Comparar linhas com `LAG` e `LEAD` {#comparing-rows-with-lag-and-lead}

Use `LAG` e `LEAD` para acessar valores de linhas anteriores ou posteriores.

```sql
-- Compare each release to previous release
SELECT
    tag_name,
    name,
    published_at,
    LAG(tag_name, 1) OVER (ORDER BY published_at) as previous_release,
    LEAD(tag_name, 1) OVER (ORDER BY published_at) as next_release,
    julianday(published_at) - julianday(LAG(published_at, 1) OVER (ORDER BY published_at)) as days_since_last_release
FROM github.repos.releases
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY published_at;
```

### Criar grupos com `NTILE` {#creating-groups-with-ntile}

Use `NTILE` para dividir as linhas em um número especificado de grupos.

```sql
-- Group contributors into quartiles
SELECT
    login,
    contributions,
    NTILE(4) OVER (ORDER BY contributions DESC) as quartile,
    CASE NTILE(4) OVER (ORDER BY contributions DESC)
        WHEN 1 THEN 'Top Contributors'
        WHEN 2 THEN 'Active Contributors'
        WHEN 3 THEN 'Moderate Contributors'
        WHEN 4 THEN 'Occasional Contributors'
    END as tier
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

### Usar `PARTITION BY` para análise agrupada {#using-partition-by-for-grouped-analysis}

Use `PARTITION BY` para realizar cálculos de janela dentro de grupos.

```sql
-- Track cumulative issues by state
SELECT
    number,
    title,
    state,
    created_at,
    ROW_NUMBER() OVER (ORDER BY created_at) as issue_sequence,
    COUNT(*) OVER (ORDER BY created_at) as cumulative_issues,
    COUNT(*) OVER (PARTITION BY state ORDER BY created_at) as cumulative_by_state
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY created_at;
```

### Usar quadros de janela {#using-window-frames}

Especifique uma cláusula de quadro para controlar quais linhas são incluídas no cálculo da janela.

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
    ROUND(AVG(commits_this_week) OVER (ORDER BY week ROWS BETWEEN 3 PRECEDING AND CURRENT ROW), 1) as four_week_moving_avg,
    SUM(commits_this_week) OVER (ORDER BY week) as cumulative_commits
FROM weekly_totals
ORDER BY week;
```

Para obter a documentação detalhada de cada função de janela, consulte a [Referência de funções de janela](/language-spec/functions/window/row_number).

Para obter mais informações, consulte [https://sqlite.org/windowfunctions.html](https://sqlite.org/windowfunctions.html).
