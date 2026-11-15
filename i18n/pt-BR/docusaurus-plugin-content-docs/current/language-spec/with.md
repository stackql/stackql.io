---
title: WITH (CTEs)
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "WITH no StackQL: definir expressões de tabela comuns (CTEs), conjuntos de resultados nomeados temporários restritos a uma única instrução SELECT, INSERT, UPDATE ou DELETE."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Define uma expressão de tabela comum (CTE) para uso em uma instrução `SELECT`, `INSERT`, `UPDATE` ou `DELETE` subsequente. As CTEs funcionam como conjuntos de resultados nomeados temporários que existem apenas durante a consulta.

Veja também:
[[` SELECT `]](/language-spec/select) [[` INSERT `]](/language-spec/insert) [[` UPDATE `]](/language-spec/update) [[` DELETE `]](/language-spec/delete)

* * *

## Sintaxe {#syntax}

*withClause::=*

<RailroadDiagram
type="withClause"
/>

*cteDefinition::=*

<RailroadDiagram
type="cteDefinition"
/>

&nbsp;
&nbsp;

```sql
WITH [RECURSIVE] cte_name [(column_name, ...)] AS (
    select_statement
)
[, cte_name [(column_name, ...)] AS (
    select_statement
)]
SELECT ... FROM cte_name ...;
```

## Argumentos {#arguments}

__*RECURSIVE*__
Palavra-chave opcional que permite que a CTE faça referência a si mesma, possibilitando consultas recursivas, como percorrer dados hierárquicos.

__*cte_name*__
O nome atribuído à CTE. Esse nome pode ser referenciado na consulta principal como se fosse uma tabela.

__*column_name*__
Opcional. Nomes das colunas do conjunto de resultados da CTE. Se não forem especificados, os nomes das colunas são derivados da instrução SELECT.

__*select_statement*__
A consulta que define o conjunto de resultados da CTE.

## Valor(es) retornado(s) {#return-values}

As CTEs não retornam valores diretamente. Elas definem conjuntos de resultados temporários que podem ser referenciados na consulta principal.

* * *

## Exemplos {#examples}

### CTE básica para organizar consultas complexas {#basic-cte-for-organizing-complex-queries}

Uma CTE pode simplificar consultas complexas ao dividi-las em componentes lógicos e nomeados.

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

### Combinar dados de várias fontes com `UNION ALL` {#combining-data-from-multiple-sources-with-union-all}

As CTEs são úteis para combinar dados de várias fontes antes de realizar uma análise.

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

### Usar CTEs com funções de janela para classificação {#using-ctes-with-window-functions-for-ranking}

```sql
-- Rank contributors within each repo using CTE and window functions
WITH repo_contributors AS (
    SELECT
        repo,
        login,
        contributions,
        RANK() OVER (PARTITION BY repo ORDER BY contributions DESC) as rank_in_repo,
        SUM(contributions) OVER (PARTITION BY repo) as repo_total_contributions
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

### Várias CTEs em uma única consulta {#multiple-ctes-in-a-single-query}

Você pode definir várias CTEs separadas por vírgulas.

```sql
-- Analyze issues and releases together
WITH open_issues AS (
    SELECT number, title, created_at
    FROM github.issues.issues
    WHERE owner = 'stackql'
      AND repo = 'stackql'
      AND state = 'open'
),
recent_releases AS (
    SELECT tag_name, published_at
    FROM github.repos.releases
    WHERE owner = 'stackql'
      AND repo = 'stackql'
    ORDER BY published_at DESC
    LIMIT 5
)
SELECT
    'Open Issues' as category,
    COUNT(*) as count
FROM open_issues
UNION ALL
SELECT
    'Recent Releases' as category,
    COUNT(*) as count
FROM recent_releases;
```

## CTEs recursivas {#recursive-ctes}

As CTEs recursivas permitem que uma CTE faça referência a si mesma, possibilitando consultas que percorrem estruturas de dados hierárquicas ou em forma de grafo.

```sql
WITH RECURSIVE cte_name (columns) AS (
    -- Base case (anchor member)
    SELECT ...
    UNION ALL
    -- Recursive case (references cte_name)
    SELECT ... FROM cte_name WHERE ...
)
SELECT * FROM cte_name;
```

:::tip

As CTEs recursivas são poderosas para dados hierárquicos, como organogramas, sistemas de arquivos ou árvores de dependências. Sempre inclua uma condição de término no caso recursivo para evitar loops infinitos.

:::

## Práticas recomendadas {#best-practices}

1. **Use CTEs para facilitar a leitura**: divida consultas complexas em componentes lógicos e nomeados.
2. **Combine com funções de janela**: as CTEs funcionam bem com funções de janela para análises avançadas.
3. **Encadeie várias CTEs**: faça referência a CTEs anteriores nas posteriores para construir uma lógica complexa passo a passo.
4. **Considere o desempenho**: embora as CTEs melhorem a legibilidade, CTEs muito grandes podem afetar o desempenho. Teste com os volumes de dados reais.

Para obter mais informações, consulte [https://sqlite.org/lang_with.html](https://sqlite.org/lang_with.html).
