---
title: COSH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: Вычисление гиперболического косинуса числа и возврат результата с помощью SQL в StackQL.
image: "/img/stackql-featured-image.png"
---
Вычисляет гиперболический косинус указанного значения.

См. также:  
[[`SELECT`]](/language-spec/select)

* * * 

## Синтаксис {#syntax}

```sql
SELECT COSH(value) FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*value*__  
Числовое выражение, для которого вычисляется гиперболический косинус.

## Возвращаемое значение {#return-values}
Возвращает гиперболический косинус указанного значения.

* * *

## Примеры {#examples}

### Вычисление гиперболического косинуса 1 {#calculate-the-hyperbolic-cosine-of-1}

```sql
SELECT COSH(1) AS hyperbolic_cosine_value;
```

Этот запрос возвращает примерно `1.5431`, так как гиперболический косинус 1 равен примерно 1.5431.

### Вычисление гиперболического косинуса 0 {#calculate-the-hyperbolic-cosine-of-0}

```sql
SELECT COSH(0) AS hyperbolic_cosine_value;
```

Этот запрос вычисляет гиперболический косинус 0 и возвращает `1`, поскольку гиперболический косинус 0 равен ровно 1.
