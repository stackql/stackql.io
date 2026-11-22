---
title: LEAD
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Função de janela LEAD no StackQL: avaliar uma expressão em uma linha seguinte da partição, com deslocamento e valor padrão opcionais."
image: "/img/stackql-featured-image.png"
---
Retorna o resultado da avaliação de uma expressão em uma linha posterior da partição.

A primeira forma de `LEAD()` retorna o resultado da avaliação da expressão na próxima linha da partição. Ou, se não houver próxima linha (porque a linha atual é a última), retorna `NULL`.

Se o argumento offset for informado, ele deve ser um inteiro não negativo. O valor retornado é o resultado da avaliação da expressão na linha que fica *offset* linhas depois da linha atual dentro da partição. Se offset for 0, a expressão é avaliada na linha atual. Se não houver uma linha *offset* linhas depois da linha atual, a função retorna `NULL`.

Se um valor padrão também for informado, ele é retornado no lugar de `NULL` caso a linha identificada por offset não exista.

Veja também:
[[` SELECT `]](/language-spec/select) [[` LAG `]](/language-spec/functions/window/lag)

* * *

## Sintaxe {#syntax}

```sql
SELECT LEAD(expr) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;

SELECT LEAD(expr, offset) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;

SELECT LEAD(expr, offset, default) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*expr*__
A expressão a ser avaliada na linha posterior.

__*offset*__
Opcional. Um inteiro não negativo que especifica quantas linhas avançar. Valor padrão: 1.

__*default*__
Opcional. O valor a ser retornado se a linha indicada por offset não existir. Valor padrão: `NULL`.

__*PARTITION BY column*__
Opcional. Divide o conjunto de resultados em partições. A função `LEAD` é aplicada dentro de cada partição separadamente.

__*ORDER BY column*__
Especifica a ordem em que as linhas são processadas.

## Valor(es) retornado(s) {#return-values}

Retorna o valor da expressão avaliada na linha posterior especificada, ou `NULL` (ou o valor padrão) se essa linha não existir.

* * *

## Exemplos {#examples}

### Olhar adiante para a próxima versão {#look-ahead-to-the-next-release}

```sql
-- Compare each release to the previous and next release
SELECT
    tag_name,
    name,
    published_at,
    LAG(tag_name, 1) OVER (ORDER BY published_at) as previous_release,
    LEAD(tag_name, 1) OVER (ORDER BY published_at) as next_release
FROM github.repos.releases
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY published_at;
```

Para obter mais informações, consulte [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
