---
title: ACOSH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: Вычисление гиперболического арккосинуса (обратного гиперболического косинуса) числа с помощью SQL в StackQL.
image: "/img/stackql-featured-image.png"
---
Вычисляет гиперболический арккосинус (обратный гиперболический косинус) числа, то есть неотрицательное значение, гиперболический косинус которого равен указанному числу.

См. также:  
[[`SELECT`]](/language-spec/select)

* * * 

## Синтаксис {#syntax}

```sql
SELECT ACOSH(value) FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*value*__  
Числовое выражение, большее или равное 1, для которого вычисляется гиперболический арккосинус.

## Возвращаемое значение {#return-values}
Возвращает гиперболический арккосинус указанного значения.

* * *

## Примеры {#examples}

### Вычисление гиперболического арккосинуса 1 {#calculate-the-hyperbolic-arc-cosine-of-1}

```sql
SELECT ACOSH(1) AS hyperbolic_arc_cosine_value;
```

Этот запрос возвращает `0`, так как гиперболический арккосинус 1 равен 0.

### Вычисление гиперболического арккосинуса 10 {#calculate-the-hyperbolic-arc-cosine-of-10}

```sql
SELECT ACOSH(10) AS hyperbolic_arc_cosine_value;
```

Этот запрос вычисляет гиперболический арккосинус 10 и возвращает примерно `2.9932`, так как это значение, гиперболический косинус которого равен 10.
