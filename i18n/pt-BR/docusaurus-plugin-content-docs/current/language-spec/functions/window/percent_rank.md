---
title: PERCENT_RANK
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Função de janela PERCENT_RANK no StackQL: a classificação relativa da linha atual em sua partição, como um valor entre 0.0 e 1.0."
image: "/img/stackql-featured-image.png"
---
Retorna um valor entre 0.0 e 1.0 que representa a classificação relativa da linha atual dentro de sua partição.

Apesar do nome, esta função retorna um valor igual a `(rank - 1) / (partition-rows - 1)`, em que `rank` é o valor retornado pela função de janela integrada `RANK()` e `partition-rows` é o número total de linhas da partição. Se a partição contiver apenas uma linha, esta função retorna 0.0.

Veja também:
[[` SELECT `]](/language-spec/select) [[` RANK `]](/language-spec/functions/window/rank) [[` CUME_DIST `]](/language-spec/functions/window/cume_dist)

* * *

## Sintaxe {#syntax}

```sql
SELECT PERCENT_RANK() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*PARTITION BY column*__
Opcional. Divide o conjunto de resultados em partições. A função `PERCENT_RANK` é aplicada a cada partição separadamente.

__*ORDER BY column*__
Especifica a ordem em que as classificações percentis são calculadas.

## Valor(es) retornado(s) {#return-values}

Retorna um número de ponto flutuante entre 0.0 e 1.0 que representa a classificação percentil.

* * *

## Exemplos {#examples}

### Calcular a classificação percentil dos colaboradores {#calculate-percentile-ranking-of-contributors}

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

O valor 0.0 de `PERCENT_RANK` indica a linha com a menor classificação, enquanto 1.0 indica a linha com a maior classificação. Use esta função para identificar onde um determinado valor se situa dentro de uma distribuição.

:::

Para obter mais informações, consulte [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
