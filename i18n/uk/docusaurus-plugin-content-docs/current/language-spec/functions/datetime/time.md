---
title: TIME
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Функція TIME у StackQL: повертає час у вигляді рядка HH:MM:SS зі значення часу та необов'язкових модифікаторів."
image: "/img/stackql-featured-image.png"
---
Повертає рядок часу у форматі HH:MM:SS.  

Див. також:  
[[` SELECT `]](/language-spec/select) [[ Модифікатори дати ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## Синтаксис {#syntax}

```sql
SELECT TIME(datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*datetime_expression*__  
Значення дати й часу.

__*modifier*__  
Нуль або більше модифікаторів, що застосовуються до *datetime_expression*.  Докладніше про модифікатори див. [**Модифікатори дати**](/language-spec/functions/datetime/date_modifiers)

## Значення, що повертається {#return-values}

Повертає рядок із часовою складовою вхідного *datetime_expression* після застосування *modifier*.

* * *

## Приклади {#examples}

### Отримання часової складової значення дати й часу {#return-the-time-component-of-a-datetime-value}

```sql
SELECT name, timeCreated,
time(timeCreated) as time_only
FROM google.storage.buckets WHERE project = 'stackql';
```

### Отримання часової складової значення дати й часу за місцевим часом {#return-the-local-time-component-of-a-datetime-value}

```sql
SELECT name, timeCreated,
time(timeCreated, 'localtime') as local_time
FROM google.storage.buckets WHERE project = 'stackql';
```

Докладніше див. [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)
