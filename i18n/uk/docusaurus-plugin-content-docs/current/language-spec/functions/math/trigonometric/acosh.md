---
title: ACOSH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: Обчислення гіперболічного арккосинуса (оберненого гіперболічного косинуса) числа за допомогою SQL у StackQL.
image: "/img/stackql-featured-image.png"
---
Обчислює гіперболічний арккосинус (обернений гіперболічний косинус) числа, тобто невід'ємне значення, гіперболічний косинус якого дорівнює вказаному числу.

Див. також:  
[[`SELECT`]](/language-spec/select)

* * * 

## Синтаксис {#syntax}

```sql
SELECT ACOSH(value) FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*value*__  
Числовий вираз, більший або рівний 1, для якого потрібно обчислити гіперболічний арккосинус.

## Значення, що повертається {#return-values}
Повертає гіперболічний арккосинус вказаного значення.

* * *

## Приклади {#examples}

### Обчислення гіперболічного арккосинуса 1 {#calculate-the-hyperbolic-arc-cosine-of-1}

```sql
SELECT ACOSH(1) AS hyperbolic_arc_cosine_value;
```

Цей запит повертає `0`, оскільки гіперболічний арккосинус 1 дорівнює 0.

### Обчислення гіперболічного арккосинуса 10 {#calculate-the-hyperbolic-arc-cosine-of-10}

```sql
SELECT ACOSH(10) AS hyperbolic_arc_cosine_value;
```

Цей запит обчислює гіперболічний арккосинус 10 і повертає приблизно `2.9932`, оскільки гіперболічний косинус цього значення дорівнює 10.
