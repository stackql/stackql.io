---
title: DATE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Функция DATE в StackQL: дата в виде строки YYYY-MM-DD, полученная из значения времени с необязательными модификаторами."
image: "/img/stackql-featured-image.png"
---
Возвращает строку даты в формате YYYY-MM-DD.  

См. также:  
[[` SELECT `]](/language-spec/select) [[ Модификаторы даты ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## Синтаксис {#syntax}

```sql
SELECT DATE(datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*datetime_expression*__  
Значение даты и времени.

__*modifier*__  
Ноль или более модификаторов, применяемых к *datetime_expression*.  Подробнее о модификаторах см. [**Модификаторы даты**](/language-spec/functions/datetime/date_modifiers)

## Возвращаемое значение {#return-values}

Возвращает строку с компонентом даты входного значения *datetime_expression* после применения *modifier*.

* * *

## Примеры {#examples}

### Получение компонента даты из значения даты и времени {#return-the-date-component-of-a-datetime-value}

```sql
SELECT name, timeCreated,
date(timeCreated) as date_only
FROM google.storage.buckets WHERE project = 'stackql';
```

### Получение компонента местной даты из значения даты и времени {#return-the-local-date-component-of-a-datetime-value}

```sql
SELECT name, timeCreated,
date(timeCreated, 'localtime') as local_date
FROM google.storage.buckets WHERE project = 'stackql';
```

Подробнее см. [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)
