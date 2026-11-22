---
title: TIME
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Функция TIME в StackQL: возвращает время в виде строки HH:MM:SS из значения времени и необязательных модификаторов."
image: "/img/stackql-featured-image.png"
---
Возвращает строку времени в формате HH:MM:SS.  

См. также:  
[[` SELECT `]](/language-spec/select) [[ Модификаторы даты ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## Синтаксис {#syntax}

```sql
SELECT TIME(datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*datetime_expression*__  
Значение даты и времени.

__*modifier*__  
Ноль или более модификаторов, применяемых к *datetime_expression*.  Подробнее о модификаторах см. [**Модификаторы даты**](/language-spec/functions/datetime/date_modifiers)

## Возвращаемое значение {#return-values}

Возвращает строку с компонентом времени входного значения *datetime_expression* после применения *modifier*.

* * *

## Примеры {#examples}

### Получение компонента времени из значения даты и времени {#return-the-time-component-of-a-datetime-value}

```sql
SELECT name, timeCreated,
time(timeCreated) as time_only
FROM google.storage.buckets WHERE project = 'stackql';
```

### Получение компонента местного времени из значения даты и времени {#return-the-local-time-component-of-a-datetime-value}

```sql
SELECT name, timeCreated,
time(timeCreated, 'localtime') as local_time
FROM google.storage.buckets WHERE project = 'stackql';
```

Подробнее см. [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)
