---
title: NTILE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Оконная функция NTILE в StackQL: разбиение секции на N групп как можно равномернее и нумерация группы каждой строки от 1 до N."
image: "/img/stackql-featured-image.png"
---
Разбивает секцию на N групп как можно равномернее и присваивает каждой группе целое число от 1 до N в порядке, заданном предложением `ORDER BY`, или в произвольном порядке в противном случае. При необходимости более крупные группы идут первыми.

См. также:
[[` SELECT `]](/language-spec/select) [[` RANK `]](/language-spec/functions/window/rank)

* * *

## Синтаксис {#syntax}

```sql
SELECT NTILE(N) OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*N*__
Положительное целое число, задающее количество групп (tiles), на которые разбивается секция.

__*PARTITION BY column*__
Необязательный. Разделяет результирующий набор на секции. Функция `NTILE` применяется к каждой секции отдельно.

__*ORDER BY column*__
Задает порядок, в котором строки распределяются по группам.

## Возвращаемое значение {#return-values}

Возвращает целое число от 1 до N, указывающее, к какой группе (tile) относится текущая строка.

* * *

## Примеры {#examples}

### Распределение участников по квартилям в зависимости от уровня вклада {#group-contributors-into-quartiles-by-contribution-level}

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

Используйте функцию `NTILE`, чтобы разбить данные на сегменты или уровни. Типичные сценарии использования - разбиение данных на квартили (N=4), децили (N=10) или процентили (N=100).

:::

Подробнее см. [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
