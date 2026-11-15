---
title: CUME_DIST
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Função de janela CUME_DIST no StackQL: a distribuição acumulada da linha atual em sua partição, como o número da linha dividido pelas linhas da partição."
image: "/img/stackql-featured-image.png"
---
Retorna a distribuição acumulada de um valor dentro de uma partição. Ela é calculada como `row-number / partition-rows`, em que `row-number` é o valor retornado por `ROW_NUMBER()` para a última linha equivalente do grupo e `partition-rows` é o número de linhas da partição.

Veja também:
[[` SELECT `]](/language-spec/select) [[` PERCENT_RANK `]](/language-spec/functions/window/percent_rank) [[` RANK `]](/language-spec/functions/window/rank)

* * *

## Sintaxe {#syntax}

```sql
SELECT CUME_DIST() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*PARTITION BY column*__
Opcional. Divide o conjunto de resultados em partições. A função `CUME_DIST` é aplicada a cada partição separadamente.

__*ORDER BY column*__
Especifica a ordem em que a distribuição acumulada é calculada.

## Valor(es) retornado(s) {#return-values}

Retorna um número de ponto flutuante entre 0.0 e 1.0 que representa o valor da distribuição acumulada.

* * *

## Exemplos {#examples}

### Calcular a distribuição acumulada dos colaboradores {#calculate-cumulative-distribution-of-contributors}

```sql
-- Calculate percentile ranking of contributors
SELECT
    login,
    contributions,
    PERCENT_RANK() OVER (ORDER BY contributions) as percentile,
    CUME_DIST() OVER (ORDER BY contributions) as cumulative_dist
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY contributions DESC;
```

:::tip

`CUME_DIST` retorna a proporção de linhas com valores menores ou iguais ao valor da linha atual. Um valor de 0.75 significa que 75% das linhas têm valores menores ou iguais ao da linha atual.

:::

Para obter mais informações, consulte [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
