---
title: ATAN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: Вычисление арктангенса (обратного тангенса) числа в виде угла в радианах с помощью SQL в StackQL.
image: "/img/stackql-featured-image.png"
---
Вычисляет арктангенс (обратный тангенс) числа, то есть угол в радианах, тангенс которого равен указанному числу.

См. также:  
[[`SELECT`]](/language-spec/select)

* * * 

## Синтаксис {#syntax}

```sql
SELECT ATAN(value) FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*value*__  
Числовое выражение, для которого вычисляется арктангенс.

## Возвращаемое значение {#return-values}
Возвращает арктангенс указанного значения в радианах.

* * *

## Примеры {#examples}

### Вычисление арктангенса 1 {#calculate-the-arc-tangent-of-1}

```sql
SELECT ATAN(1) AS arc_tangent_value;
```

Этот запрос возвращает π/4, то есть примерно `0.785`, так как арктангенс 1 равен π/4 радиан.

### Вычисление арктангенса 0 {#calculate-the-arc-tangent-of-0}

```sql
SELECT ATAN(0) AS arc_tangent_value;
```

Этот запрос вычисляет арктангенс 0 и возвращает `0`, поскольку тангенс 0 радиан равен 0.
