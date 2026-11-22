---
title: MOD
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: Обчислення остачі від ділення одного числа на інше за допомогою SQL у StackQL.
image: "/img/stackql-featured-image.png"
---
Обчислює остачу від ділення *x* на *y*, тобто повертає результат ділення за модулем. Це схоже на використання оператора '%', але функція сумісна також із нецілими аргументами.

Див. також:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Синтаксис {#syntax}

```sql
SELECT MOD(x, y) FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*x*__  
Ділене - числовий вираз.

__*y*__  
Дільник - також числовий вираз.

## Значення, що повертається {#return-values}
Повертає остачу від ділення *x* на *y*.

* * *

## Приклади {#examples}

### Обчислення остачі від ділення 10 на 3 {#calculate-the-modulus-of-10-divided-by-3}

```sql
SELECT MOD(10, 3) AS remainder;
```
