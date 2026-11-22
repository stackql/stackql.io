---
title: ABS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Функція ABS у StackQL: абсолютне значення числового аргументу."
image: "/img/stackql-featured-image.png"
---
Повертає абсолютне значення числового аргументу.  

Див. також:  
[[` SELECT `]](/language-spec/select) 

* * * 

## Синтаксис {#syntax}

```sql
SELECT ABS(numeric_expression) FROM <multipartIdentifier>;
```

> Функція `abs(X)` повертає `NULL`, якщо `X` має значення `NULL`. Функція `abs(X)` повертає `0.0`, якщо `X` - рядок або blob, який не можна перетворити на числове значення. Якщо `X` - ціле число -9223372036854775808, функція `abs(X)` видає помилку переповнення цілого числа, оскільки відповідного додатного значення розрядністю 64 біти в доповняльному коді не існує.

## Аргументи {#arguments}

__*numeric_expression*__  
Вираз, тип даних якого належить до категорії точних або наближених числових типів.

## Значення, що повертається {#return-values}

Повертає невід'ємне числове значення.

* * *

## Приклади {#examples}

### Отримання абсолютного значення числа {#return-the-absolute-value-of-a-number}

```sql
SELECT name, abs(strftime('%s', creationTimestamp)-strftime('%s','now')) as abs_value
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Докладніше див. [https://www.sqlite.org/lang_corefunc.html#abs](https://www.sqlite.org/lang_corefunc.html#abs)
