---
title: TANH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: Обчислення гіперболічного тангенса числа з поверненням результату за допомогою SQL у StackQL.
image: "/img/stackql-featured-image.png"
---
Обчислює гіперболічний тангенс вказаного значення.

Див. також:  
[[`SELECT`]](/language-spec/select)

* * * 

## Синтаксис {#syntax}

```sql
SELECT TANH(value) FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*value*__  
Числовий вираз, для якого потрібно обчислити гіперболічний тангенс.

## Значення, що повертається {#return-values}
Повертає гіперболічний тангенс вказаного значення.

* * *

## Приклади {#examples}

### Обчислення гіперболічного тангенса 1 {#calculate-the-hyperbolic-tangent-of-1}

```sql
SELECT TANH(1) AS hyperbolic_tangent_value;
```

Цей запит повертає приблизно `0.7616`, оскільки гіперболічний тангенс 1 становить приблизно 0.7616.

### Обчислення гіперболічного тангенса 0 {#calculate-the-hyperbolic-tangent-of-0}

```sql
SELECT TANH(0) AS hyperbolic_tangent_value;
```

Цей запит обчислює гіперболічний тангенс 0 і повертає `0`, оскільки гіперболічний тангенс 0 дорівнює рівно 0.
