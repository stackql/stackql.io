---
title: LN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: Обчислення натурального логарифма числа за допомогою SQL у StackQL.
image: "/img/stackql-featured-image.png"
---
Обчислює натуральний логарифм (логарифм за основою e) вказаного числа.

Див. також:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Синтаксис {#syntax}

```sql
SELECT LN(numeric_expression) FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*numeric_expression*__  
Додатний числовий вираз, для якого потрібно обчислити натуральний логарифм.

## Значення, що повертається {#return-values}
Повертає натуральний логарифм указаного додатного числового значення.

* * *

## Приклади {#examples}

### Обчислення натурального логарифма 2.71828 (приблизно e) {#calculate-the-natural-logarithm-of-271828-approximately-e}

```sql
SELECT LN(2.71828) AS natural_log;
```

Цей запит повертає значення, близьке до `1`, оскільки натуральний логарифм e (приблизно 2.71828) дорівнює 1.

### Обчислення натурального логарифма більшого числа {#calculate-the-natural-logarithm-of-a-larger-number}

```sql
SELECT LN(10) AS natural_log;
```

Цей запит обчислює натуральний логарифм 10, тобто логарифм числа 10 за основою e.
