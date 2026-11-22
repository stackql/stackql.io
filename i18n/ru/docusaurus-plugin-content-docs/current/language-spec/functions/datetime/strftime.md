---
title: STRFTIME
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Функция STRFTIME в StackQL: форматирует значение даты и времени по строке формата с теми же подстановками, что и функция strftime в C."
image: "/img/stackql-featured-image.png"
---
Возвращает значение даты и времени, отформатированное по указанному формату.  

См. также:  
[[` SELECT `]](/language-spec/select) [[ Модификаторы даты ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## Синтаксис {#syntax}

```sql
SELECT STRFTIME(format, datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*format*__  
Нужный формат для значения даты и времени, заданного значением *datetime_expression*.  См. таблицу форматов ниже.

__*datetime_expression*__  
Значение даты или времени, которое нужно отформатировать.

__*modifier*__  
Ноль или более модификаторов, применяемых к *datetime_expression*.  Подробнее о модификаторах см. [**Модификаторы даты**](/language-spec/functions/datetime/date_modifiers)

### Форматы {#formats}

| Формат | Описание                                |
|--------|-----------------------------------------|
| %d     | день месяца: 01-31                      |
| %f     | секунды с дробной частью: SS.SSS        |
| %H     | час: 00-24                              |
| %j     | день года: 001-366                      |
| %J     | номер юлианского дня                    |
| %m     | месяц: 01-12                            |
| %M     | минута: 00-59                           |
| %s     | секунды с 1970-01-01                    |
| %S     | секунды: 00-59                          |
| %w     | день недели 0-6, воскресенье==0         |
| %W     | неделя года: 00-53                      |
| %Y     | год: 0000-9999                          |
| %%     | экранирование знака процента: %         |

## Возвращаемое значение {#return-values}

Возвращает отформатированную строку с датой и временем в нужном формате.

* * *

## Примеры {#examples}

### Получение даты в формате США {#return-a-date-in-us-format}

```sql
SELECT name, strftime('%m/%d/%Y', creationTimestamp) as created_at
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Преобразование значения даты и времени в метку времени Unix {#convert-a-datetime-to-an-epoch-timestamp}

```sql
SELECT name, strftime('%s', creationTimestamp) as created_at
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Вычисление числа дней между двумя датами {#calculate-the-days-between-two-dates}

```sql
SELECT name, (strftime('%s','now')-strftime('%s', creationTimestamp))/86400 as days_since_creation
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Подробнее см. [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)
