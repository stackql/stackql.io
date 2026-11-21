---
title: NTH_VALUE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Função de janela NTH_VALUE no StackQL: o valor de uma expressão para a N-ésima linha do quadro da janela."
image: "/img/stackql-featured-image.png"
---
Retorna o valor da expressão avaliada na N-ésima linha do quadro da janela.

Esta função de janela integrada calcula o quadro da janela de cada linha da mesma forma que uma função de janela de agregação. Ela retorna o valor da expressão avaliada na linha N do quadro da janela. As linhas são numeradas dentro do quadro da janela a partir de 1, na ordem definida pela cláusula `ORDER BY`, se houver, ou em ordem arbitrária caso contrário. Se não houver uma N-ésima linha no quadro da janela, a função retorna `NULL`.

Veja também:
[[` SELECT `]](/language-spec/select) [[` FIRST_VALUE `]](/language-spec/functions/window/first_value) [[` LAST_VALUE `]](/language-spec/functions/window/last_value)

* * *

## Sintaxe {#syntax}

```sql
SELECT NTH_VALUE(expr, N) OVER (
    [PARTITION BY column]
    ORDER BY column
    [ROWS BETWEEN frame_start AND frame_end]
) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*expr*__
A expressão a ser avaliada na N-ésima linha do quadro da janela.

__*N*__
Um inteiro positivo que especifica o número da linha a ser obtida (indexado a partir de 1).

__*PARTITION BY column*__
Opcional. Divide o conjunto de resultados em partições. A função `NTH_VALUE` é aplicada dentro de cada partição separadamente.

__*ORDER BY column*__
Especifica a ordem em que as linhas são processadas.

__*ROWS BETWEEN frame_start AND frame_end*__
Opcional. Define o quadro da janela.

## Valor(es) retornado(s) {#return-values}

Retorna o valor da expressão avaliada na N-ésima linha do quadro da janela, ou `NULL` se a N-ésima linha não existir.

* * *

## Exemplos {#examples}

### Obter os três colaboradores com mais contribuições {#get-the-top-three-contributors}

```sql
-- Get the top 3 contributors by number of contributions
SELECT
    login,
    contributions,
    NTH_VALUE(login, 1) OVER (ORDER BY contributions DESC
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) as top_contributor,
    NTH_VALUE(login, 2) OVER (ORDER BY contributions DESC
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) as second_contributor,
    NTH_VALUE(login, 3) OVER (ORDER BY contributions DESC
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) as third_contributor
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

Para obter mais informações, consulte [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
