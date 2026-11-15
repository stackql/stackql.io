---
title: FIRST_VALUE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Função de janela FIRST_VALUE no StackQL: o valor de uma expressão para a primeira linha do quadro da janela."
image: "/img/stackql-featured-image.png"
---
Retorna o valor da expressão avaliada na primeira linha do quadro da janela.

Esta função de janela integrada calcula o quadro da janela de cada linha da mesma forma que uma função de janela de agregação. Para cada linha, ela retorna o valor da expressão avaliada na primeira linha do quadro da janela.

Veja também:
[[` SELECT `]](/language-spec/select) [[` LAST_VALUE `]](/language-spec/functions/window/last_value) [[` NTH_VALUE `]](/language-spec/functions/window/nth_value)

* * *

## Sintaxe {#syntax}

```sql
SELECT FIRST_VALUE(expr) OVER (
    [PARTITION BY column]
    ORDER BY column
    [ROWS BETWEEN frame_start AND frame_end]
) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*expr*__
A expressão a ser avaliada na primeira linha do quadro da janela.

__*PARTITION BY column*__
Opcional. Divide o conjunto de resultados em partições. A função `FIRST_VALUE` é aplicada dentro de cada partição separadamente.

__*ORDER BY column*__
Especifica a ordem em que as linhas são processadas.

__*ROWS BETWEEN frame_start AND frame_end*__
Opcional. Define o quadro da janela. As opções comuns incluem:
- `ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW`
- `ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING`

Sem uma cláusula de quadro, o quadro é `RANGE BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW`: ele termina na última linha empatada com a linha atual nas colunas de `ORDER BY`, e não na linha atual.

## Valor(es) retornado(s) {#return-values}

Retorna o valor da expressão avaliada na primeira linha do quadro da janela.

* * *

## Exemplos {#examples}

### Encontrar a primeira versão para contextualizar {#find-the-first-release-for-context}

```sql
-- Find first and latest release for context
SELECT
    tag_name,
    name,
    published_at,
    FIRST_VALUE(tag_name) OVER (
        ORDER BY published_at
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING
    ) as first_release,
    LAST_VALUE(tag_name) OVER (
        ORDER BY published_at
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING
    ) as latest_release
FROM github.repos.releases
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

:::tip

Por padrão, o quadro da janela se estende do início da partição até a linha atual. Para incluir todas as linhas da partição, use `ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING`.

:::

Para obter mais informações, consulte [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
