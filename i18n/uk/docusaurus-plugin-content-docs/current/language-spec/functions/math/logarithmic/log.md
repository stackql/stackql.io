---
title: LOG
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: Обчислення логарифма числа за будь-якою основою за допомогою SQL у StackQL.
image: "/img/stackql-featured-image.png"
---
Обчислює логарифм числа за вказаною основою; це загальніша форма логарифмічної функції порівняно з LN, яка використовує основу e.

Див. також:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Синтаксис {#syntax}

```sql
SELECT LOG(base, numeric_expression) FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*base*__  
Основа логарифма: додатний числовий вираз, відмінний від 1.

__*numeric_expression*__  
Додатний числовий вираз, для якого потрібно обчислити логарифм.

## Значення, що повертається {#return-values}
Повертає логарифм указаного числового значення за вказаною основою.

* * *

## Приклади {#examples}

### Обчислення логарифма 1000 за основою 10 {#calculate-the-logarithm-of-1000-with-base-10}

```sql
SELECT LOG(10, 1000) AS log_result;
```

Цей запит повертає `3`, оскільки 10^3 = 1000.

### Обчислення логарифма 16 за основою 2 {#calculate-the-logarithm-of-16-with-base-2}

```sql
SELECT LOG(2, 16) AS log_result;
```
Цей запит обчислює логарифм 16 за основою 2 і повертає `4`, оскільки 2^4 = 16.
