---
title: LN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: Вычисление натурального логарифма числа с помощью SQL в StackQL.
image: "/img/stackql-featured-image.png"
---
Вычисляет натуральный логарифм (логарифм по основанию e) заданного числа.

См. также:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Синтаксис {#syntax}

```sql
SELECT LN(numeric_expression) FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*numeric_expression*__  
Положительное числовое выражение, для которого нужно вычислить натуральный логарифм.

## Возвращаемое значение {#return-values}
Возвращает натуральный логарифм указанного положительного числового значения.

* * *

## Примеры {#examples}

### Вычисление натурального логарифма 2.71828 (приблизительно e) {#calculate-the-natural-logarithm-of-271828-approximately-e}

```sql
SELECT LN(2.71828) AS natural_log;
```

Этот запрос возвращает значение, близкое к `1`, так как натуральный логарифм e (приблизительно 2.71828) равен 1.

### Вычисление натурального логарифма большего числа {#calculate-the-natural-logarithm-of-a-larger-number}

```sql
SELECT LN(10) AS natural_log;
```

Этот запрос вычисляет натуральный логарифм 10, то есть логарифм числа 10 по основанию e.
