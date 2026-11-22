---
title: ACOS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: Вычисление арккосинуса (обратного косинуса) числа в виде угла в радианах с помощью SQL в StackQL.
image: "/img/stackql-featured-image.png"
---
Вычисляет арккосинус (обратный косинус) числа, то есть угол в радианах, косинус которого равен указанному числу.

См. также:  
[[`SELECT`]](/language-spec/select)

* * * 

## Синтаксис {#syntax}

```sql
SELECT ACOS(value) FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*value*__  
Числовое выражение в диапазоне от -1 до 1 включительно, для которого вычисляется арккосинус.

## Возвращаемое значение {#return-values}
Возвращает арккосинус указанного значения в радианах.

* * *

## Примеры {#examples}

### Вычисление арккосинуса 0.5 {#calculate-the-arc-cosine-of-05}

```sql
SELECT ACOS(0.5) AS arc_cosine_value;
```

Этот запрос возвращает примерно `1.047`, так как арккосинус 0.5 равен π/3 радиан (60 градусов), что составляет около 1.047 радиана.

### Вычисление арккосинуса 1 {#calculate-the-arc-cosine-of-1}

```sql
SELECT ACOS(1) AS arc_cosine_value;
```

Этот запрос вычисляет арккосинус 1 и возвращает `0`, поскольку косинус 0 радиан равен 1.
