---
title: ASINH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: Обчислення гіперболічного арксинуса (оберненого гіперболічного синуса) числа за допомогою SQL у StackQL.
image: "/img/stackql-featured-image.png"
---
Обчислює гіперболічний арксинус (обернений гіперболічний синус) числа, тобто значення, гіперболічний синус якого дорівнює вказаному числу.

Див. також:  
[[`SELECT`]](/language-spec/select)

* * * 

## Синтаксис {#syntax}

```sql
SELECT ASINH(value) FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*value*__  
Числовий вираз, для якого потрібно обчислити гіперболічний арксинус.

## Значення, що повертається {#return-values}
Повертає гіперболічний арксинус вказаного значення.

* * *

## Приклади {#examples}

### Обчислення гіперболічного арксинуса 0.5 {#calculate-the-hyperbolic-arc-sine-of-05}

```sql
SELECT ASINH(0.5) AS hyperbolic_arc_sine_value;
```

Цей запит повертає приблизно `0.4812`, оскільки гіперболічний арксинус 0.5 становить приблизно 0.4812.

### Обчислення гіперболічного арксинуса -1 {#calculate-the-hyperbolic-arc-sine-of--1}

```sql
SELECT ASINH(-1) AS hyperbolic_arc_sine_value;
```

Цей запит обчислює гіперболічний арксинус -1 і повертає приблизно `-0.8814`, оскільки гіперболічний синус цього значення дорівнює -1.
