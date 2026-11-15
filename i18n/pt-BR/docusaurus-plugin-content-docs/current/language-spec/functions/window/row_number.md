---
title: ROW_NUMBER
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Função de janela ROW_NUMBER no StackQL: numerar as linhas de uma partição a partir de 1, na ordem dada pela cláusula ORDER BY da janela."
image: "/img/stackql-featured-image.png"
---
Retorna o número da linha dentro da partição atual. As linhas são numeradas a partir de 1 na ordem definida pela cláusula `ORDER BY` na definição da janela, ou em ordem arbitrária caso contrário.

Veja também:
[[` SELECT `]](/language-spec/select) [[` RANK `]](/language-spec/functions/window/rank) [[` DENSE_RANK `]](/language-spec/functions/window/dense_rank)

* * *

## Sintaxe {#syntax}

```sql
SELECT ROW_NUMBER() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*PARTITION BY column*__
Opcional. Divide o conjunto de resultados em partições. A função `ROW_NUMBER` é aplicada a cada partição separadamente e reinicia o número da linha em cada partição.

__*ORDER BY column*__
Especifica a ordem em que os números de linha são atribuídos. Necessário para obter resultados significativos.

## Valor(es) retornado(s) {#return-values}

Retorna um inteiro que representa o número sequencial da linha dentro da partição, começando em 1.

* * *

## Exemplos {#examples}

### Classificar os colaboradores dentro de um repositório {#rank-contributors-within-a-repository}

```sql
-- Rank contributors within a repository
SELECT
    login,
    contributions,
    ROW_NUMBER() OVER (ORDER BY contributions DESC) as contribution_rank
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

### Acompanhar a sequência de issues ao longo do tempo {#track-issue-sequence-over-time}

```sql
-- Track issue growth over time
SELECT
    number,
    title,
    state,
    created_at,
    ROW_NUMBER() OVER (ORDER BY created_at) as issue_sequence
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY created_at;
```

### Comparar `ROW_NUMBER` com `RANK` e `DENSE_RANK` {#compare-row_number-with-rank-and-dense_rank}

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

Para obter mais informações, consulte [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
