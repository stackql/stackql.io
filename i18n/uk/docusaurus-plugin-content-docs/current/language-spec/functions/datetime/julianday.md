---
title: JULIANDAY
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Функція JULIANDAY у StackQL: дробова кількість днів від полудня 24 листопада 4714 року до н. е. для значення часу, корисна для арифметики дат."
image: "/img/stackql-featured-image.png"
---
Повертає юліанський день - кількість днів від полудня за Гринвічем 24 листопада 4714 року до н. е. [(пролептичний григоріанський календар)](https://en.wikipedia.org/wiki/Proleptic_Gregorian_calendar).  

Див. також:  
[[` SELECT `]](/language-spec/select) [[ Модифікатори дати ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## Синтаксис {#syntax}

```sql
SELECT JULIANDAY(datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*datetime_expression*__  
Значення дати й часу.

__*modifier*__  
Нуль або більше модифікаторів, що застосовуються до *datetime_expression*.  Докладніше про модифікатори див. [**Модифікатори дати**](/language-spec/functions/datetime/date_modifiers)

## Значення, що повертається {#return-values}

Повертає [юліанський день](https://en.wikipedia.org/wiki/Julian_day) вхідного *datetime_expression* після застосування *modifier*: кількість днів, включно з дробовою частиною для часу доби, від полудня за Гринвічем 24 листопада 4714 року до н. е. за [пролептичним григоріанським календарем](https://en.wikipedia.org/wiki/Proleptic_Gregorian_calendar).

* * *

## Приклади {#examples}

### Обчислення кількості днів між двома датами {#calculate-the-days-between-two-dates}

```sql
SELECT name, timeCreated,
round(julianday('now')-julianday(timeCreated)) as days_since
FROM google.storage.buckets WHERE project = 'stackql';
```

:::tip 

Скористайтеся функцією [**ROUND**](/language-spec/functions/math/round), щоб перетворити числа з рухомою комою на найближче ціле число.

:::

Докладніше див. [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)
