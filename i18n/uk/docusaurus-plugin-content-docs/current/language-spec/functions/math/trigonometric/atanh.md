---
title: ATANH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: Обчислення гіперболічного арктангенса (оберненого гіперболічного тангенса) числа за допомогою SQL у StackQL.
image: "/img/stackql-featured-image.png"
---
Обчислює гіперболічний арктангенс (обернений гіперболічний тангенс) числа, тобто значення, гіперболічний тангенс якого дорівнює вказаному числу.

Див. також:  
[[`SELECT`]](/language-spec/select)

* * * 

## Синтаксис {#syntax}

```sql
SELECT ATANH(value) FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*value*__  
Числовий вираз у діапазоні від -1 до 1 (не включно), для якого потрібно обчислити гіперболічний арктангенс.

## Значення, що повертається {#return-values}
Повертає гіперболічний арктангенс вказаного значення.

* * *

## Приклади {#examples}

### Обчислення гіперболічного арктангенса 0.5 {#calculate-the-hyperbolic-arc-tangent-of-05}

```sql
SELECT ATANH(0.5) AS hyperbolic_arc_tangent_value;
```

Цей запит повертає приблизно `0.5493`, оскільки гіперболічний арктангенс 0.5 становить приблизно 0.5493.

### Обчислення гіперболічного арктангенса -0.5 {#calculate-the-hyperbolic-arc-tangent-of--05}

```sql
SELECT ATANH(-0.5) AS hyperbolic_arc_tangent_value;
```

Цей запит обчислює гіперболічний арктангенс -0.5 і повертає приблизно `-0.5493`, оскільки гіперболічний тангенс цього значення дорівнює -0.5.
