---
title: ASINH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: Вычисление гиперболического арксинуса (обратного гиперболического синуса) числа с помощью SQL в StackQL.
image: "/img/stackql-featured-image.png"
---
Вычисляет гиперболический арксинус (обратный гиперболический синус) числа, то есть значение, гиперболический синус которого равен указанному числу.

См. также:  
[[`SELECT`]](/language-spec/select)

* * * 

## Синтаксис {#syntax}

```sql
SELECT ASINH(value) FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*value*__  
Числовое выражение, для которого вычисляется гиперболический арксинус.

## Возвращаемое значение {#return-values}
Возвращает гиперболический арксинус указанного значения.

* * *

## Примеры {#examples}

### Вычисление гиперболического арксинуса 0.5 {#calculate-the-hyperbolic-arc-sine-of-05}

```sql
SELECT ASINH(0.5) AS hyperbolic_arc_sine_value;
```

Этот запрос возвращает примерно `0.4812`, так как гиперболический арксинус 0.5 равен примерно 0.4812.

### Вычисление гиперболического арксинуса -1 {#calculate-the-hyperbolic-arc-sine-of--1}

```sql
SELECT ASINH(-1) AS hyperbolic_arc_sine_value;
```

Этот запрос вычисляет гиперболический арксинус -1 и возвращает примерно `-0.8814`, так как это значение, гиперболический синус которого равен -1.
