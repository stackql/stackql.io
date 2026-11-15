---
title: NTILE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Função de janela NTILE no StackQL: dividir uma partição em N grupos do modo mais uniforme possível e numerar o grupo de cada linha de 1 a N."
image: "/img/stackql-featured-image.png"
---
Divide a partição em N grupos do modo mais uniforme possível e atribui um inteiro entre 1 e N a cada grupo, na ordem definida pela cláusula `ORDER BY` ou em ordem arbitrária caso contrário. Se necessário, os grupos maiores vêm primeiro.

Veja também:
[[` SELECT `]](/language-spec/select) [[` RANK `]](/language-spec/functions/window/rank)

* * *

## Sintaxe {#syntax}

```sql
SELECT NTILE(N) OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*N*__
Um inteiro positivo que especifica o número de grupos (tiles) em que a partição é dividida.

__*PARTITION BY column*__
Opcional. Divide o conjunto de resultados em partições. A função `NTILE` é aplicada a cada partição separadamente.

__*ORDER BY column*__
Especifica a ordem em que as linhas são atribuídas aos grupos.

## Valor(es) retornado(s) {#return-values}

Retorna um inteiro entre 1 e N que indica a qual grupo (tile) a linha atual pertence.

* * *

## Exemplos {#examples}

### Agrupar os colaboradores em quartis por nível de contribuição {#group-contributors-into-quartiles-by-contribution-level}

```sql
-- Group contributors into quartiles by contribution level
SELECT
    login,
    contributions,
    NTILE(4) OVER (ORDER BY contributions DESC) as contribution_quartile,
    CASE NTILE(4) OVER (ORDER BY contributions DESC)
        WHEN 1 THEN 'Top Contributors'
        WHEN 2 THEN 'Active Contributors'
        WHEN 3 THEN 'Moderate Contributors'
        WHEN 4 THEN 'Occasional Contributors'
    END as contributor_tier
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

:::tip

Use `NTILE` para criar segmentos ou níveis nos seus dados. Casos de uso comuns incluem dividir os dados em quartis (N=4), decis (N=10) ou percentis (N=100).

:::

Para obter mais informações, consulte [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
