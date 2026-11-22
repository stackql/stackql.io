---
title: SINH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: Обчислення гіперболічного синуса числа з поверненням результату за допомогою SQL у StackQL.
image: "/img/stackql-featured-image.png"
---
Обчислює гіперболічний синус вказаного значення.

Див. також:  
[[`SELECT`]](/language-spec/select)

* * * 

## Синтаксис {#syntax}

```sql
SELECT SINH(value) FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*value*__  
Числовий вираз, для якого потрібно обчислити гіперболічний синус.

## Значення, що повертається {#return-values}
Повертає гіперболічний синус вказаного значення.

* * *

## Приклади {#examples}

### Обчислення гіперболічного синуса 1 {#calculate-the-hyperbolic-sine-of-1}

```sql
SELECT SINH(1) AS hyperbolic_sine_value;
```

Цей запит повертає приблизно `1.1752`, оскільки гіперболічний синус 1 становить приблизно 1.1752.

### Обчислення гіперболічного синуса 0 {#calculate-the-hyperbolic-sine-of-0}

```sql
SELECT SINH(0) AS hyperbolic_sine_value;
```

Цей запит обчислює гіперболічний синус 0 і повертає `0`, оскільки гіперболічний синус 0 дорівнює рівно 0.
