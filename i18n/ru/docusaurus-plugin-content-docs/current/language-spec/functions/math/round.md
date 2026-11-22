---
title: ROUND
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Функция ROUND в StackQL: округляет числовой аргумент до заданного числа знаков после запятой, по умолчанию до нуля."
image: "/img/stackql-featured-image.png"
---
Округляет числовой аргумент.  

См. также:  
[[` SELECT `]](/language-spec/select) 

* * * 

## Синтаксис {#syntax}

```sql
SELECT ROUND(numeric_expression [, precision]) FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*numeric_expression*__  
Выражение, относящееся к категории точных или приближенных числовых типов данных.

__*precision*__  
Количество цифр справа от десятичной запятой, до которого округляется *numeric_expression*.

> Если аргумент *precision* не указан, он считается равным 0.

## Возвращаемое значение {#return-values}
Возвращает числовое значение.

* * *

## Примеры {#examples}

### Округление числа с плавающей запятой до двух знаков после запятой {#round-a-floating-point-number-to-two-decimal-places}

```sql
SELECT name, timeCreated,
round(julianday('now')-julianday(timeCreated), 2) as days_since
FROM google.storage.buckets WHERE project = 'stackql';
```
Подробнее см. [https://www.sqlite.org/lang_corefunc.html#round](https://www.sqlite.org/lang_corefunc.html#round)
