---
title: ASIN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: Вычисление арксинуса (обратного синуса) числа в виде угла в радианах с помощью SQL в StackQL.
image: "/img/stackql-featured-image.png"
---
Вычисляет арксинус (обратный синус) числа, то есть угол в радианах, синус которого равен указанному числу.

См. также:  
[[`SELECT`]](/language-spec/select)

* * * 

## Синтаксис {#syntax}

```sql
SELECT ASIN(value) FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*value*__  
Числовое выражение в диапазоне от -1 до 1 включительно, для которого вычисляется арксинус.

## Возвращаемое значение {#return-values}
Возвращает арксинус указанного значения в радианах.

* * *

## Примеры {#examples}

### Вычисление арксинуса 0.5 {#calculate-the-arc-sine-of-05}

```sql
SELECT ASIN(0.5) AS arc_sine_value;
```

Этот запрос возвращает π/6, то есть примерно `0.5236`, так как арксинус 0.5 соответствует углу π/6 радиан.

### Вычисление арксинуса -1 {#calculate-the-arc-sine-of--1}

```sql
SELECT ASIN(-1) AS arc_sine_value;
```

Этот запрос вычисляет арксинус -1 и возвращает -π/2, то есть примерно `-1.5708`, так как синус -π/2 равен -1.
