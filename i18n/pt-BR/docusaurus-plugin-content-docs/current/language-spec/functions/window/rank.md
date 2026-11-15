---
title: RANK
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Função de janela RANK no StackQL: a classificação com lacunas da linha atual na partição; empates compartilham a classificação e as seguintes são puladas."
image: "/img/stackql-featured-image.png"
---
Retorna a classificação da linha atual com lacunas. É o `ROW_NUMBER` da primeira linha equivalente de cada grupo. Linhas com valores iguais nas colunas de ordenação recebem a mesma classificação, e o próximo valor de classificação é incrementado pelo número de linhas empatadas (deixando lacunas).

Se não houver cláusula `ORDER BY`, todas as linhas são consideradas equivalentes e esta função sempre retorna 1.

Veja também:
[[` SELECT `]](/language-spec/select) [[` ROW_NUMBER `]](/language-spec/functions/window/row_number) [[` DENSE_RANK `]](/language-spec/functions/window/dense_rank)

* * *

## Sintaxe {#syntax}

```sql
SELECT RANK() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*PARTITION BY column*__
Opcional. Divide o conjunto de resultados em partições. A função `RANK` é aplicada a cada partição separadamente.

__*ORDER BY column*__
Especifica a ordem em que as classificações são atribuídas. Se não houver cláusula `ORDER BY`, todas as linhas são consideradas equivalentes e recebem a classificação 1.

## Valor(es) retornado(s) {#return-values}

Retorna um inteiro que representa a classificação da linha atual dentro da partição, com lacunas para valores empatados.

* * *

## Exemplos {#examples}

### Classificar os colaboradores por contribuições (com lacunas) {#rank-contributors-by-contributions-with-gaps}

```sql
-- Rank contributors with gaps for ties
SELECT
    login,
    contributions,
    RANK() OVER (ORDER BY contributions DESC) as rank
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

### Comparar `RANK` com `DENSE_RANK` e `ROW_NUMBER` {#compare-rank-with-dense_rank-and-row_number}

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

### Classificar os colaboradores dentro de cada repositório com `PARTITION BY` {#rank-contributors-within-each-repository-using-partition-by}

```sql
-- Rank contributors within each repo
WITH repo_contributors AS (
    SELECT
        repo,
        login,
        contributions,
        RANK() OVER (PARTITION BY repo ORDER BY contributions DESC) as rank_in_repo
    FROM (
        SELECT 'stackql' as repo, login, contributions
        FROM github.repos.contributors
        WHERE owner = 'stackql' AND repo = 'stackql'
        UNION ALL
        SELECT 'stackql-provider-registry' as repo, login, contributions
        FROM github.repos.contributors
        WHERE owner = 'stackql' AND repo = 'stackql-provider-registry'
    ) t
)
SELECT
    repo,
    login,
    contributions,
    rank_in_repo
FROM repo_contributors
ORDER BY repo, rank_in_repo;
```

Para obter mais informações, consulte [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
