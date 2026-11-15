---
title: LAG
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Função de janela LAG no StackQL: avaliar uma expressão em uma linha anterior da partição, com deslocamento e valor padrão opcionais."
image: "/img/stackql-featured-image.png"
---
Retorna o resultado da avaliação de uma expressão em uma linha anterior da partição.

A primeira forma de `LAG()` retorna o resultado da avaliação da expressão na linha anterior da partição. Ou, se não houver linha anterior (porque a linha atual é a primeira), retorna `NULL`.

Se o argumento offset for informado, ele deve ser um inteiro não negativo. O valor retornado é o resultado da avaliação da expressão na linha que fica *offset* linhas antes da linha atual dentro da partição. Se offset for 0, a expressão é avaliada na linha atual. Se não houver uma linha *offset* linhas antes da linha atual, a função retorna `NULL`.

Se um valor padrão também for informado, ele é retornado no lugar de `NULL` caso a linha identificada por offset não exista.

Veja também:
[[` SELECT `]](/language-spec/select) [[` LEAD `]](/language-spec/functions/window/lead)

* * *

## Sintaxe {#syntax}

```sql
SELECT LAG(expr) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;

SELECT LAG(expr, offset) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;

SELECT LAG(expr, offset, default) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*expr*__
A expressão a ser avaliada na linha anterior.

__*offset*__
Opcional. Um inteiro não negativo que especifica quantas linhas voltar. Valor padrão: 1.

__*default*__
Opcional. O valor a ser retornado se a linha indicada por offset não existir. Valor padrão: `NULL`.

__*PARTITION BY column*__
Opcional. Divide o conjunto de resultados em partições. A função `LAG` é aplicada dentro de cada partição separadamente.

__*ORDER BY column*__
Especifica a ordem em que as linhas são processadas.

## Valor(es) retornado(s) {#return-values}

Retorna o valor da expressão avaliada na linha anterior especificada, ou `NULL` (ou o valor padrão) se essa linha não existir.

* * *

## Exemplos {#examples}

### Comparar cada versão com a versão anterior {#compare-each-release-to-the-previous-release}

```sql
-- Compare each release to previous and next release dates
SELECT
    tag_name,
    name,
    published_at,
    LAG(tag_name, 1) OVER (ORDER BY published_at) as previous_release,
    LAG(published_at, 1) OVER (ORDER BY published_at) as previous_release_date,
    julianday(published_at) - julianday(LAG(published_at, 1) OVER (ORDER BY published_at)) as days_since_last_release
FROM github.repos.releases
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY published_at;
```

### Analisar os intervalos entre a criação de issues {#analyze-gaps-between-issue-creation}

```sql
-- Analyze gaps between issue creation
SELECT
    number,
    title,
    created_at,
    LAG(created_at, 1) OVER (ORDER BY created_at) as prev_issue_date,
    julianday(created_at) - julianday(LAG(created_at, 1) OVER (ORDER BY created_at)) as days_since_prev_issue
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY created_at;
```

Para obter mais informações, consulte [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
