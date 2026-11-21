---
title: DATETIME
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Функція DATETIME у StackQL: обчислення значення дати й часу з вхідного значення та нуля або більше модифікаторів у форматі YYYY-MM-DD HH:MM:SS."
image: "/img/stackql-featured-image.png"
---
Повертає значення дати й часу, обчислене з вхідного значення дати й часу та нуля або більше модифікаторів.  

Див. також:  
[[` SELECT `]](/language-spec/select) [[ Модифікатори дати ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## Синтаксис {#syntax}

```sql
SELECT DATETIME(datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*datetime_expression*__  
Значення дати й часу.

__*modifier*__  
Нуль або більше модифікаторів, що застосовуються до *datetime_expression*.  Докладніше про модифікатори див. [**Модифікатори дати**](/language-spec/functions/datetime/date_modifiers)

## Значення, що повертається {#return-values}

Повертає рядок із датою й часом вхідного *datetime_expression* після застосування *modifier*.

* * *

## Приклади {#examples}

### Перетворення UTC на місцевий час за допомогою модифікатора `localtime` {#convert-utc-to-localtime-using-the-localtime-modifier}

```sql
SELECT name, timeCreated,
datetime(timeCreated, 'localtime') as local_time_created
FROM google.storage.buckets WHERE project = 'stackql';
```

### Перетворення місцевого часу на UTC {#convert-datetime-to-utc}

Модифікатор `utc` трактує вхідне значення як місцевий час на машині, де запущено StackQL, і перетворює його на UTC.  Значення, що вже є в UTC, наприклад `'now'` або позначку часу із суфіксом `Z`, функція повертає без змін.

```sql
SELECT datetime('2026-11-20 17:59:24', 'utc') as utc_time;
```

На машині з часовим поясом UTC+11 цей запит повертає `2026-11-20 06:59:24`.

### Обчислення дати відносно іншої дати {#calculate-a-date-relative-to-another-date}

```sql
SELECT name, timeCreated,
datetime(timeCreated, '+24 hours') as a_day_later
FROM google.storage.buckets WHERE project = 'stackql';
```

### Отримання наступного вказаного дня тижня після певної дати {#get-the-next-specified-weekday-after-a-given-date}

```sql
SELECT name, timeCreated,
datetime(timeCreated, 'weekday 0') as following_sunday
FROM google.storage.buckets WHERE project = 'stackql';
```

### Отримання початку місяця для певної дати {#get-the-start-of-the-month-for-a-given-date}

```sql
SELECT name, timeCreated,
datetime(timeCreated, 'start of month') as start_of_month
FROM google.storage.buckets WHERE project = 'stackql';
```

### Перетворення позначки часу Unix на значення дати й часу {#convert-a-unix-timestamp-to-a-datetime}

```sql
SELECT datetime(1576417943, 'unixepoch') as date_from_ts
FROM google.storage.buckets WHERE project = 'stackql';
```

:::tip 

Щоб перетворити значення дати й часу на позначку часу Unix, див. [**STRFTIME**](/language-spec/functions/datetime/strftime).  Щоб обчислити різницю між двома об'єктами дати й часу, див. [**STRFTIME**](/language-spec/functions/datetime/strftime) або [**JULIANDAY**](/language-spec/functions/datetime/julianday).

:::

Докладніше див. [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)
