---
title: LOG10
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: Обчислення логарифма числа за основою 10 за допомогою SQL у StackQL.
image: "/img/stackql-featured-image.png"
---
Обчислює логарифм вказаного числа за основою 10, тобто визначає степінь, до якого потрібно піднести число 10, щоб отримати це значення.

Див. також:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Синтаксис {#syntax}

```sql
SELECT LOG10(numeric_expression) FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*numeric_expression*__  
Додатний числовий вираз, для якого потрібно обчислити логарифм за основою 10.

## Значення, що повертається {#return-values}
Повертає логарифм указаного числового значення за основою 10.

* * *

## Приклади {#examples}

### Обчислення логарифма 100 за основою 10 {#calculate-the-base-10-logarithm-of-100}

```sql
SELECT LOG10(100) AS log_result;
```

Цей запит повертає `2`, оскільки 10^2 = 100.

### Обчислення логарифма 1000 за основою 10 {#calculate-the-base-10-logarithm-of-1000}

```sql
SELECT LOG10(1000) AS log_result;
```

Цей запит обчислює логарифм 1000 за основою 10 і повертає `3`, оскільки 10^3 = 1000.
