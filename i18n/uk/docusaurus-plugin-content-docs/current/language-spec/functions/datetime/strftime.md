---
title: STRFTIME
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Функція STRFTIME у StackQL: форматування значення дати й часу за рядком формату з тими самими підстановками, що й у функції strftime мови C."
image: "/img/stackql-featured-image.png"
---
Повертає значення дати й часу, відформатоване у вказаному форматі.  

Див. також:  
[[` SELECT `]](/language-spec/select) [[ Модифікатори дати ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## Синтаксис {#syntax}

```sql
SELECT STRFTIME(format, datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*format*__  
Потрібний формат для значення дати й часу, вказаного в *datetime_expression*.  Див. таблицю форматів нижче.

__*datetime_expression*__  
Значення дати або часу, яке потрібно відформатувати.

__*modifier*__  
Нуль або більше модифікаторів, що застосовуються до *datetime_expression*.  Докладніше про модифікатори див. [**Модифікатори дати**](/language-spec/functions/datetime/date_modifiers)

### Формати {#formats}

| Формат | Опис                           |
|--------|--------------------------------|
| %d     | день місяця: 01-31             |
| %f     | секунди з дробовою частиною: SS.SSS |
| %H     | година: 00-24                  |
| %j     | день року: 001-366             |
| %J     | номер юліанського дня          |
| %m     | місяць: 01-12                  |
| %M     | хвилина: 00-59                 |
| %s     | секунди від 1970-01-01         |
| %S     | секунди: 00-59                 |
| %w     | день тижня 0-6, де неділя==0   |
| %W     | тиждень року: 00-53            |
| %Y     | рік: 0000-9999                 |
| %%     | екранування символу відсотка: % |

## Значення, що повертається {#return-values}

Повертає відформатований рядок у потрібному форматі дати й часу.

* * *

## Приклади {#examples}

### Отримання дати у форматі США {#return-a-date-in-us-format}

```sql
SELECT name, strftime('%m/%d/%Y', creationTimestamp) as created_at
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Перетворення значення дати й часу на позначку часу Unix {#convert-a-datetime-to-an-epoch-timestamp}

```sql
SELECT name, strftime('%s', creationTimestamp) as created_at
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Обчислення кількості днів між двома датами {#calculate-the-days-between-two-dates}

```sql
SELECT name, (strftime('%s','now')-strftime('%s', creationTimestamp))/86400 as days_since_creation
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Докладніше див. [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)
