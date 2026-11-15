---
title: DENSE_RANK
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Função de janela DENSE_RANK no StackQL: a classificação sem lacunas da linha atual na partição; empates compartilham a classificação e a próxima vem logo depois."
image: "/img/stackql-featured-image.png"
---
Retorna a classificação da linha atual sem lacunas. É o número do grupo de linhas equivalentes da linha atual dentro de sua partição. Linhas com valores iguais nas colunas de ordenação recebem a mesma classificação, e o próximo valor de classificação é sempre incrementado em 1 (sem lacunas).

As linhas são numeradas a partir de 1 na ordem definida pela cláusula `ORDER BY` na definição da janela. Se não houver cláusula `ORDER BY`, todas as linhas são consideradas equivalentes e esta função sempre retorna 1.

Veja também:
[[` SELECT `]](/language-spec/select) [[` ROW_NUMBER `]](/language-spec/functions/window/row_number) [[` RANK `]](/language-spec/functions/window/rank)

* * *

## Sintaxe {#syntax}

```sql
SELECT DENSE_RANK() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*PARTITION BY column*__
Opcional. Divide o conjunto de resultados em partições. A função `DENSE_RANK` é aplicada a cada partição separadamente.

__*ORDER BY column*__
Especifica a ordem em que as classificações são atribuídas. Se não houver cláusula `ORDER BY`, todas as linhas são consideradas equivalentes e recebem a classificação 1.

## Valor(es) retornado(s) {#return-values}

Retorna um inteiro que representa a classificação densa da linha atual dentro da partição, sem lacunas para valores empatados.

* * *

## Exemplos {#examples}

### Classificar os colaboradores por contribuições (sem lacunas) {#rank-contributors-by-contributions-without-gaps}

```sql
-- Rank contributors without gaps for ties
SELECT
    DENSE_RANK() OVER (ORDER BY contributions DESC) as rank,
    login,
    contributions,
    html_url
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo IN ('stackql', 'stackql-deploy', 'stackql-provider-registry')
ORDER BY contributions DESC;
```

### Comparar `DENSE_RANK` com `RANK` e `ROW_NUMBER` {#compare-dense_rank-with-rank-and-row_number}

```sql
-- Compare RANK vs DENSE_RANK vs ROW_NUMBER for contributors
SELECT
    login,
    contributions,
    RANK() OVER (ORDER BY contributions DESC) as rank,
    DENSE_RANK() OVER (ORDER BY contributions DESC) as dense_rank,
    ROW_NUMBER() OVER (ORDER BY contributions DESC) as row_num
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

### Classificar as contribuições agregadas de vários repositórios {#rank-aggregated-contributions-across-multiple-repositories}

```sql
-- Rank total contributions across multiple repos
WITH all_contributors AS (
    SELECT login, contributions, html_url
    FROM github.repos.contributors
    WHERE owner = 'stackql' AND repo = 'stackql'
    UNION ALL
    SELECT login, contributions, html_url
    FROM github.repos.contributors
    WHERE owner = 'stackql' AND repo = 'stackql-deploy'
    UNION ALL
    SELECT login, contributions, html_url
    FROM github.repos.contributors
    WHERE owner = 'stackql' AND repo = 'stackql-provider-registry'
)
SELECT
    DENSE_RANK() OVER (ORDER BY SUM(contributions) DESC) as rank,
    login,
    SUM(contributions) as total_contributions,
    MAX(html_url) as html_url
FROM all_contributors
GROUP BY login
ORDER BY total_contributions DESC;
```

Para obter mais informações, consulte [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
