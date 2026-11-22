---
title: ABS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Функция ABS в StackQL: абсолютное значение числового аргумента."
image: "/img/stackql-featured-image.png"
---
Возвращает абсолютное значение числового аргумента.  

См. также:  
[[` SELECT `]](/language-spec/select) 

* * * 

## Синтаксис {#syntax}

```sql
SELECT ABS(numeric_expression) FROM <multipartIdentifier>;
```

> Функция `abs(X)` возвращает `NULL`, если `X` равен `NULL`. Функция `abs(X)` возвращает `0.0`, если `X` - строковое значение или blob, которые невозможно преобразовать в числовое значение. Если `X` - целое число -9223372036854775808, функция `abs(X)` выдает ошибку целочисленного переполнения, так как для него не существует эквивалентного положительного 64-битного значения в дополнительном коде.

## Аргументы {#arguments}

__*numeric_expression*__  
Выражение, относящееся к категории точных или приближенных числовых типов данных.

## Возвращаемое значение {#return-values}

Возвращает неотрицательное числовое значение.

* * *

## Примеры {#examples}

### Получение абсолютного значения числа {#return-the-absolute-value-of-a-number}

```sql
SELECT name, abs(strftime('%s', creationTimestamp)-strftime('%s','now')) as abs_value
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Подробнее см. [https://www.sqlite.org/lang_corefunc.html#abs](https://www.sqlite.org/lang_corefunc.html#abs)
