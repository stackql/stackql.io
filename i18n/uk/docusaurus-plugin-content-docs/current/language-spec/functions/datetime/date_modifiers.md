---
title: Модифікатори дати
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Модифікатори дати й часу в StackQL: зсув результатів DATE, TIME, DATETIME, JULIANDAY і STRFTIME на дні, місяці чи роки, до початку періоду, дня тижня тощо."
image: "/img/stackql-featured-image.png"
---

## Огляд {#overview}

У функціях дати й часу StackQL після значення дати або часу може йти нуль або більше модифікаторів, які змінюють дату та/або час. Кожен модифікатор - це перетворення, яке застосовується до значення часу ліворуч від нього. Модифікатори застосовуються по черзі зліва направо. Доступні такі модифікатори:

| Модифікатор          | Опис                                                                  |
|----------------------|-----------------------------------------------------------------------|
| NNN day(s)           | Додає ±NNN днів до дати й часу                                        |
| NNN hour(s)          | Додає ±NNN годин до дати й часу                                       |
| NNN minute(s)        | Додає ±NNN хвилин до дати й часу                                      |
| NNN[.NNNN] second(s) | Додає ±NNN секунд до дати й часу або віднімає їх                      |
| NNN month(s)         | Додає ±NNN місяців до дати й часу                                     |
| NNN year(s)          | Додає ±NNN років до дати й часу                                       |
| start of month       | Назад до початку місяця                                               |
| start of year        | Назад до початку року                                                 |
| start of day         | Назад до початку дня                                                  |
| weekday N            | Переміщує дату вперед до наступної дати, номер дня тижня якої дорівнює N; дата, що вже припадає на день тижня N, повертається без змін |
| localtime            | Повертає місцевий час                                                |
| utc                  | Повертає час UTC                                                      |
| unixepoch            | Час Unix                                                              |

## Приклади {#examples}

### Отримання поточної дати й часу за допомогою значення часу `now` {#return-the-current-date-and-time-using-the-now-time-value}

```sql
SELECT name, date('now') as date, time('now') as time
FROM google.storage.buckets WHERE project = 'stackql';
```

### Перетворення UTC на місцевий час за допомогою модифікатора `localtime` {#convert-utc-to-localtime-using-the-localtime-modifier}

```sql
SELECT name, timeCreated,
datetime(timeCreated, 'localtime') as local_time_created
FROM google.storage.buckets WHERE project = 'stackql';
```

### Перетворення місцевого часу на UTC {#convert-datetime-to-utc}

Модифікатор `utc` вважає вхідне значення місцевим часом машини, на якій запущено StackQL, і перетворює його на UTC.  Значення, яке вже в UTC, наприклад `'now'` або позначка часу із суфіксом `Z`, повертається без змін.

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

### Отримання вказаного дня тижня в певну дату або після неї {#get-the-next-specified-weekday-after-a-given-date}

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

Щоб перетворити значення дати й часу на позначку часу епохи Unix, див. [**STRFTIME**](/language-spec/functions/datetime/strftime).  Щоб обчислити різницю між двома об'єктами дати й часу, див. [**STRFTIME**](/language-spec/functions/datetime/strftime) або [**JULIANDAY**](/language-spec/functions/datetime/julianday).

:::
