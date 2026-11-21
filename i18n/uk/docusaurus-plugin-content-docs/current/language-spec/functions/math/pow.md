---
title: POW
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: Піднесення одного числа до степеня іншого за допомогою SQL у StackQL.
image: "/img/stackql-featured-image.png"
---
Обчислює *x* у степені *y*.

Див. також:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Синтаксис {#syntax}

```sql
SELECT POW(x, y) FROM <multipartIdentifier>;
-- or using the equivalent
SELECT POWER(x, y) FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*x*__  
Основа степеня - числовий вираз.

__*y*__  
Показник степеня, до якого підносять основу *x*, - також числовий вираз.

## Значення, що повертається {#return-values}
Повертає результат піднесення *x* до степеня *y*.

* * *

## Приклади {#examples}

### Обчислення x у степені y {#calculate-x-raised-to-the-power-y}


```sql
SELECT POW(2, 3) AS result;
```
