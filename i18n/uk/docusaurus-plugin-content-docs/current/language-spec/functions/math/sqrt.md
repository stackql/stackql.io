---
title: SQRT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: Обчислення квадратного кореня з числа за допомогою SQL у StackQL.
image: "/img/stackql-featured-image.png"
---
Обчислює квадратний корінь із невід'ємного числового виразу.

Див. також:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Синтаксис {#syntax}

```sql
SELECT SQRT(numeric_expression) FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*numeric_expression*__  
Невід'ємний числовий вираз, для якого потрібно обчислити квадратний корінь.

## Значення, що повертається {#return-values}
Повертає квадратний корінь із вказаного невід'ємного числового значення.

* * *

## Приклади {#examples}

### Обчислення квадратного кореня з числа {#calculate-the-square-root-of-a-number}

```sql
SELECT SQRT(9) AS square_root;
```
