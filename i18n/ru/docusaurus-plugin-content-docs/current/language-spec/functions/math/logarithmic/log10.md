---
title: LOG10
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: Вычисление логарифма числа по основанию 10 с помощью SQL в StackQL.
image: "/img/stackql-featured-image.png"
---
Вычисляет логарифм заданного числа по основанию 10, то есть показатель степени, в которую нужно возвести число 10, чтобы получить это значение.

См. также:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Синтаксис {#syntax}

```sql
SELECT LOG10(numeric_expression) FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*numeric_expression*__  
Положительное числовое выражение, для которого нужно вычислить логарифм по основанию 10.

## Возвращаемое значение {#return-values}
Возвращает логарифм указанного числового значения по основанию 10.

* * *

## Примеры {#examples}

### Вычисление логарифма 100 по основанию 10 {#calculate-the-base-10-logarithm-of-100}

```sql
SELECT LOG10(100) AS log_result;
```

Этот запрос возвращает `2`, так как 10^2 = 100.

### Вычисление логарифма 1000 по основанию 10 {#calculate-the-base-10-logarithm-of-1000}

```sql
SELECT LOG10(1000) AS log_result;
```

Этот запрос вычисляет логарифм 1000 по основанию 10 и возвращает `3`, так как 10^3 = 1000.
