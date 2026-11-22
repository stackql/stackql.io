---
title: SQRT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: Вычисление квадратного корня из числа с помощью SQL в StackQL.
image: "/img/stackql-featured-image.png"
---
Вычисляет квадратный корень из неотрицательного числового выражения.

См. также:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Синтаксис {#syntax}

```sql
SELECT SQRT(numeric_expression) FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*numeric_expression*__  
Неотрицательное числовое выражение, из которого вычисляется квадратный корень.

## Возвращаемое значение {#return-values}
Возвращает квадратный корень из указанного неотрицательного числового значения.

* * *

## Примеры {#examples}

### Вычисление квадратного корня из числа {#calculate-the-square-root-of-a-number}

```sql
SELECT SQRT(9) AS square_root;
```
