---
title: COSH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: Обчислення гіперболічного косинуса числа з поверненням результату за допомогою SQL у StackQL.
image: "/img/stackql-featured-image.png"
---
Обчислює гіперболічний косинус вказаного значення.

Див. також:  
[[`SELECT`]](/language-spec/select)

* * * 

## Синтаксис {#syntax}

```sql
SELECT COSH(value) FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*value*__  
Числовий вираз, для якого потрібно обчислити гіперболічний косинус.

## Значення, що повертається {#return-values}
Повертає гіперболічний косинус вказаного значення.

* * *

## Приклади {#examples}

### Обчислення гіперболічного косинуса 1 {#calculate-the-hyperbolic-cosine-of-1}

```sql
SELECT COSH(1) AS hyperbolic_cosine_value;
```

Цей запит повертає приблизно `1.5431`, оскільки гіперболічний косинус 1 становить приблизно 1.5431.

### Обчислення гіперболічного косинуса 0 {#calculate-the-hyperbolic-cosine-of-0}

```sql
SELECT COSH(0) AS hyperbolic_cosine_value;
```

Цей запит обчислює гіперболічний косинус 0 і повертає `1`, оскільки гіперболічний косинус 0 дорівнює рівно 1.
