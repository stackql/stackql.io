---
title: SINH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: Вычисление гиперболического синуса числа и возврат результата с помощью SQL в StackQL.
image: "/img/stackql-featured-image.png"
---
Вычисляет гиперболический синус указанного значения.

См. также:  
[[`SELECT`]](/language-spec/select)

* * * 

## Синтаксис {#syntax}

```sql
SELECT SINH(value) FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*value*__  
Числовое выражение, для которого вычисляется гиперболический синус.

## Возвращаемое значение {#return-values}
Возвращает гиперболический синус указанного значения.

* * *

## Примеры {#examples}

### Вычисление гиперболического синуса 1 {#calculate-the-hyperbolic-sine-of-1}

```sql
SELECT SINH(1) AS hyperbolic_sine_value;
```

Этот запрос возвращает примерно `1.1752`, так как гиперболический синус 1 равен примерно 1.1752.

### Вычисление гиперболического синуса 0 {#calculate-the-hyperbolic-sine-of-0}

```sql
SELECT SINH(0) AS hyperbolic_sine_value;
```

Этот запрос вычисляет гиперболический синус 0 и возвращает `0`, поскольку гиперболический синус 0 равен ровно 0.
