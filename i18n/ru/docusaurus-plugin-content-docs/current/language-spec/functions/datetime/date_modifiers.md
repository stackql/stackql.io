---
title: Модификаторы даты
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Модификаторы даты и времени в StackQL: сдвиг результатов DATE, TIME, DATETIME, JULIANDAY и STRFTIME на дни, месяцы или годы, к началу периода, дню недели и т. д."
image: "/img/stackql-featured-image.png"
---

## Обзор {#overview}

В функциях даты и времени StackQL за значением даты или времени может следовать ноль или более модификаторов, которые изменяют дату и/или время. Каждый модификатор - это преобразование, которое применяется к значению времени слева от него. Модификаторы применяются по порядку слева направо. Доступны следующие модификаторы:

| Модификатор | Описание |
|----------------------|-----------------------------------------------------------------------|
| NNN day(s) | Добавляет ±NNN дней к дате и времени |
| NNN hour(s) | Добавляет ±NNN часов к дате и времени |
| NNN minute(s) | Добавляет ±NNN минут к дате и времени |
| NNN[.NNNN] second(s) | Добавляет ±NNN секунд к дате и времени или вычитает их |
| NNN month(s) | Добавляет ±NNN месяцев к дате и времени |
| NNN year(s) | Добавляет ±NNN лет к дате и времени |
| start of month | Назад к началу месяца |
| start of year | Назад к началу года |
| start of day | Назад к началу дня |
| weekday N | Сдвигает дату вперед до следующей даты, номер дня недели которой равен N; дата, которая уже приходится на день недели N, возвращается без изменений |
| localtime | Возвращает местное время |
| utc | Возвращает время UTC |
| unixepoch | Время Unix |

## Примеры {#examples}

### Получение текущей даты и времени с помощью значения времени `now` {#return-the-current-date-and-time-using-the-now-time-value}

```sql
SELECT name, date('now') as date, time('now') as time
FROM google.storage.buckets WHERE project = 'stackql';
```

### Преобразование UTC в местное время с помощью модификатора `localtime` {#convert-utc-to-localtime-using-the-localtime-modifier}

```sql
SELECT name, timeCreated,
datetime(timeCreated, 'localtime') as local_time_created
FROM google.storage.buckets WHERE project = 'stackql';
```

### Преобразование местного времени в UTC {#convert-datetime-to-utc}

Модификатор `utc` считает входное значение местным временем машины, на которой запущен StackQL, и преобразует его в UTC.  Значение, которое уже указано в UTC, например `'now'` или метка времени с суффиксом `Z`, возвращается без изменений.

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

### Получение указанного дня недели в заданную дату или после нее {#get-the-next-specified-weekday-after-a-given-date}

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

Чтобы преобразовать значение даты и времени в метку времени эпохи Unix, см. [**STRFTIME**](/language-spec/functions/datetime/strftime).  Чтобы вычислить разницу между двумя объектами даты и времени, см. [**STRFTIME**](/language-spec/functions/datetime/strftime) или [**JULIANDAY**](/language-spec/functions/datetime/julianday).

:::
