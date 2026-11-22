---
title: ATANH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: Вычисление гиперболического арктангенса (обратного гиперболического тангенса) числа с помощью SQL в StackQL.
image: "/img/stackql-featured-image.png"
---
Вычисляет гиперболический арктангенс (обратный гиперболический тангенс) числа, то есть значение, гиперболический тангенс которого равен указанному числу.

См. также:  
[[`SELECT`]](/language-spec/select)

* * * 

## Синтаксис {#syntax}

```sql
SELECT ATANH(value) FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*value*__  
Числовое выражение в диапазоне от -1 до 1 (не включая границы), для которого вычисляется гиперболический арктангенс.

## Возвращаемое значение {#return-values}
Возвращает гиперболический арктангенс указанного значения.

* * *

## Примеры {#examples}

### Вычисление гиперболического арктангенса 0.5 {#calculate-the-hyperbolic-arc-tangent-of-05}

```sql
SELECT ATANH(0.5) AS hyperbolic_arc_tangent_value;
```

Этот запрос возвращает примерно `0.5493`, так как гиперболический арктангенс 0.5 равен примерно 0.5493.

### Вычисление гиперболического арктангенса -0.5 {#calculate-the-hyperbolic-arc-tangent-of--05}

```sql
SELECT ATANH(-0.5) AS hyperbolic_arc_tangent_value;
```

Этот запрос вычисляет гиперболический арктангенс -0.5 и возвращает примерно `-0.5493`, так как это значение, гиперболический тангенс которого равен -0.5.
