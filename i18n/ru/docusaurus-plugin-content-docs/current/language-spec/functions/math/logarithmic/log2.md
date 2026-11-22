---
title: LOG2
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: Вычисление логарифма числа по основанию 2 с помощью SQL в StackQL.
image: "/img/stackql-featured-image.png"
---
Вычисляет логарифм заданного числа по основанию 2 и возвращает показатель степени, в которую нужно возвести число 2, чтобы получить это значение.

См. также:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Синтаксис {#syntax}

```sql
SELECT LOG2(numeric_expression) FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*numeric_expression*__  
Положительное числовое выражение, для которого нужно вычислить логарифм по основанию 2.

## Возвращаемое значение {#return-values}
Возвращает логарифм указанного числового значения по основанию 2.

* * *

## Примеры {#examples}

### Вычисление логарифма 16 по основанию 2 {#calculate-the-base-2-logarithm-of-16}

```sql
SELECT LOG2(16) AS log_result;
```

Этот запрос возвращает `4`, так как 2^4 = 16.

### Вычисление логарифма 32 по основанию 2 {#calculate-the-base-2-logarithm-of-32}

```sql
SELECT LOG2(32) AS log_result;
```

Этот запрос вычисляет логарифм 32 по основанию 2 и возвращает `5`, так как 2^5 = 32.
