---
title: DATETIME
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Функция DATETIME в StackQL: вычисляет значение даты и времени из входного значения и нуля или более модификаторов в формате YYYY-MM-DD HH:MM:SS."
image: "/img/stackql-featured-image.png"
---
Возвращает значение даты и времени, вычисленное из входного значения даты и времени и нуля или более модификаторов.  

См. также:  
[[` SELECT `]](/language-spec/select) [[ Модификаторы даты ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## Синтаксис {#syntax}

```sql
SELECT DATETIME(datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*datetime_expression*__  
Значение даты и времени.

__*modifier*__  
Ноль или более модификаторов, применяемых к *datetime_expression*.  Подробнее о модификаторах см. [**Модификаторы даты**](/language-spec/functions/datetime/date_modifiers)

## Возвращаемое значение {#return-values}

Возвращает строку с датой и временем входного значения *datetime_expression* после применения *modifier*.

* * *

## Примеры {#examples}

### Преобразование UTC в местное время с помощью модификатора `localtime` {#convert-utc-to-localtime-using-the-localtime-modifier}

```sql
SELECT name, timeCreated,
datetime(timeCreated, 'localtime') as local_time_created
FROM google.storage.buckets WHERE project = 'stackql';
```

### Преобразование местного времени в UTC {#convert-datetime-to-utc}

Модификатор `utc` считает входное значение местным временем машины, на которой запущен StackQL, и преобразует его в UTC.  Значение, которое уже задано в UTC, например `'now'` или метка времени с суффиксом `Z`, возвращается без изменений.

```sql
SELECT datetime('2026-11-20 17:59:24', 'utc') as utc_time;
```

На машине с часовым поясом UTC+11 этот запрос возвращает `2026-11-20 06:59:24`.

### Вычисление даты относительно другой даты {#calculate-a-date-relative-to-another-date}

```sql
SELECT name, timeCreated,
datetime(timeCreated, '+24 hours') as a_day_later
FROM google.storage.buckets WHERE project = 'stackql';
```

### Получение следующего указанного дня недели после заданной даты {#get-the-next-specified-weekday-after-a-given-date}

```sql
SELECT name, timeCreated,
datetime(timeCreated, 'weekday 0') as following_sunday
FROM google.storage.buckets WHERE project = 'stackql';
```

### Получение начала месяца для заданной даты {#get-the-start-of-the-month-for-a-given-date}

```sql
SELECT name, timeCreated,
datetime(timeCreated, 'start of month') as start_of_month
FROM google.storage.buckets WHERE project = 'stackql';
```

### Преобразование метки времени Unix в значение даты и времени {#convert-a-unix-timestamp-to-a-datetime}

```sql
SELECT datetime(1576417943, 'unixepoch') as date_from_ts
FROM google.storage.buckets WHERE project = 'stackql';
```

:::tip 

Чтобы преобразовать значение даты и времени в метку времени Unix, см. [**STRFTIME**](/language-spec/functions/datetime/strftime).  Чтобы вычислить разницу между двумя значениями даты и времени, см. [**STRFTIME**](/language-spec/functions/datetime/strftime) или [**JULIANDAY**](/language-spec/functions/datetime/julianday).

:::

Подробнее см. [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)
