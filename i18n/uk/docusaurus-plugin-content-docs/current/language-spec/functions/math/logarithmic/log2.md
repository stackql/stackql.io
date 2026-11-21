---
title: LOG2
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: Обчислення логарифма числа за основою 2 за допомогою SQL у StackQL.
image: "/img/stackql-featured-image.png"
---
Обчислює логарифм вказаного числа за основою 2, тобто повертає степінь, до якого потрібно піднести число 2, щоб отримати це значення.

Див. також:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Синтаксис {#syntax}

```sql
SELECT LOG2(numeric_expression) FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*numeric_expression*__  
Додатний числовий вираз, для якого потрібно обчислити логарифм за основою 2.

## Значення, що повертається {#return-values}
Повертає логарифм указаного числового значення за основою 2.

* * *

## Приклади {#examples}

### Обчислення логарифма 16 за основою 2 {#calculate-the-base-2-logarithm-of-16}

```sql
SELECT LOG2(16) AS log_result;
```

Цей запит повертає `4`, оскільки 2^4 = 16.

### Обчислення логарифма 32 за основою 2 {#calculate-the-base-2-logarithm-of-32}

```sql
SELECT LOG2(32) AS log_result;
```

Цей запит обчислює логарифм 32 за основою 2 і повертає `5`, оскільки 2^5 = 32.
