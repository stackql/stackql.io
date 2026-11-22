---
title: LOG
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: Вычисление логарифма числа по любому основанию с помощью SQL в StackQL.
image: "/img/stackql-featured-image.png"
---
Вычисляет логарифм числа по указанному основанию; это более общая форма логарифмической функции по сравнению с LN, которая использует основание e.

См. также:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Синтаксис {#syntax}

```sql
SELECT LOG(base, numeric_expression) FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*base*__  
Основание логарифма: положительное числовое выражение, отличное от 1.

__*numeric_expression*__  
Положительное числовое выражение, для которого нужно вычислить логарифм.

## Возвращаемое значение {#return-values}
Возвращает логарифм указанного числового значения по указанному основанию.

* * *

## Примеры {#examples}

### Вычисление логарифма 1000 по основанию 10 {#calculate-the-logarithm-of-1000-with-base-10}

```sql
SELECT LOG(10, 1000) AS log_result;
```

Этот запрос возвращает `3`, так как 10^3 = 1000.

### Вычисление логарифма 16 по основанию 2 {#calculate-the-logarithm-of-16-with-base-2}

```sql
SELECT LOG(2, 16) AS log_result;
```
Этот запрос вычисляет логарифм 16 по основанию 2 и возвращает `4`, так как 2^4 = 16.
