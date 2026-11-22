---
title: JULIANDAY
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Функция JULIANDAY в StackQL: дробное число дней с полудня 24 ноября 4714 года до н. э. для значения времени, удобное для вычислений с датами."
image: "/img/stackql-featured-image.png"
---
Возвращает юлианский день - число дней, прошедших с полудня по Гринвичу 24 ноября 4714 года до н. э. [(пролептический григорианский календарь)](https://en.wikipedia.org/wiki/Proleptic_Gregorian_calendar).  

См. также:  
[[` SELECT `]](/language-spec/select) [[ Модификаторы даты ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## Синтаксис {#syntax}

```sql
SELECT JULIANDAY(datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*datetime_expression*__  
Значение даты и времени.

__*modifier*__  
Ноль или более модификаторов, применяемых к *datetime_expression*.  Подробнее о модификаторах см. [**Модификаторы даты**](/language-spec/functions/datetime/date_modifiers)

## Возвращаемое значение {#return-values}

Возвращает [юлианский день](https://en.wikipedia.org/wiki/Julian_day) входного значения *datetime_expression* после применения *modifier*: число дней, включая дробную часть для времени суток, прошедших с полудня по Гринвичу 24 ноября 4714 года до н. э. по [пролептическому григорианскому календарю](https://en.wikipedia.org/wiki/Proleptic_Gregorian_calendar).

* * *

## Примеры {#examples}

### Вычисление числа дней между двумя датами {#calculate-the-days-between-two-dates}

```sql
SELECT name, timeCreated,
round(julianday('now')-julianday(timeCreated)) as days_since
FROM google.storage.buckets WHERE project = 'stackql';
```

:::tip 

Используйте функцию [**ROUND**](/language-spec/functions/math/round), чтобы преобразовать числа с плавающей запятой в ближайшее целое число.

:::

Подробнее см. [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)
