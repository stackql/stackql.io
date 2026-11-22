---
title: TANH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: Вычисление гиперболического тангенса числа и возврат результата с помощью SQL в StackQL.
image: "/img/stackql-featured-image.png"
---
Вычисляет гиперболический тангенс указанного значения.

См. также:  
[[`SELECT`]](/language-spec/select)

* * * 

## Синтаксис {#syntax}

```sql
SELECT TANH(value) FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*value*__  
Числовое выражение, для которого вычисляется гиперболический тангенс.

## Возвращаемое значение {#return-values}
Возвращает гиперболический тангенс указанного значения.

* * *

## Примеры {#examples}

### Вычисление гиперболического тангенса 1 {#calculate-the-hyperbolic-tangent-of-1}

```sql
SELECT TANH(1) AS hyperbolic_tangent_value;
```

Этот запрос возвращает примерно `0.7616`, так как гиперболический тангенс 1 равен примерно 0.7616.

### Вычисление гиперболического тангенса 0 {#calculate-the-hyperbolic-tangent-of-0}

```sql
SELECT TANH(0) AS hyperbolic_tangent_value;
```

Этот запрос вычисляет гиперболический тангенс 0 и возвращает `0`, поскольку гиперболический тангенс 0 равен ровно 0.
