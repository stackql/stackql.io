---
title: DATE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Функція DATE у StackQL: повертає дату як рядок у форматі YYYY-MM-DD зі значення часу та необов'язкових модифікаторів."
image: "/img/stackql-featured-image.png"
---
Повертає дату як рядкове значення у форматі YYYY-MM-DD.  

Див. також:  
[[` SELECT `]](/language-spec/select) [[ Модифікатори дати ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## Синтаксис {#syntax}

```sql
SELECT DATE(datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*datetime_expression*__  
Значення дати й часу.

__*modifier*__  
Нуль або більше модифікаторів, які застосовуються до *datetime_expression*.  Докладніше про модифікатори див. [**Модифікатори дати**](/language-spec/functions/datetime/date_modifiers)

## Значення, що повертається {#return-values}

Повертає рядкове значення, що відповідає компоненту дати вхідного значення *datetime_expression* із застосованим *modifier*.

* * *

## Приклади {#examples}

### Отримання компонента дати зі значення дати й часу {#return-the-date-component-of-a-datetime-value}

```sql
SELECT name, timeCreated,
date(timeCreated) as date_only
FROM google.storage.buckets WHERE project = 'stackql';
```

### Отримання компонента локальної дати зі значення дати й часу {#return-the-local-date-component-of-a-datetime-value}

```sql
SELECT name, timeCreated,
date(timeCreated, 'localtime') as local_date
FROM google.storage.buckets WHERE project = 'stackql';
```

Докладніше див. [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)
