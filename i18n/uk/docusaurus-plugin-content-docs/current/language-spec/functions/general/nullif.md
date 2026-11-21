---
title: NULLIF
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Функція NULLIF у StackQL: повертає NULL, якщо два аргументи рівні, інакше повертає перший аргумент."
image: "/img/stackql-featured-image.png"
---
Приймає два аргументи й повертає значення `NULL`, якщо вони рівні, інакше повертає перший аргумент.  

> Функція `NULLIF` еквівалентна виразу `CASE WHEN expression1 = expression2 THEN NULL ELSE expression1 END`

> Функція `NULLIF` корисна з агрегатними функціями, як-от [`AVG`](/language-spec/functions/aggregate/avg), [`MAX`](/language-spec/functions/aggregate/max), [`MIN`](/language-spec/functions/aggregate/min), [`SUM`](/language-spec/functions/aggregate/sum) і [`COUNT`](/language-spec/functions/aggregate/count), коли набір результатів містить спеціальні значення, які не є `NULL`, але які ви хочете трактувати як `NULL` для цілей агрегування.

Див. також:  
[[` SELECT `]](/language-spec/select) [[` COALESCE `]](/language-spec/functions/general/coalesce) [[` IFNULL `]](/language-spec/functions/general/ifnull)

* * * 

## Синтаксис {#syntax}

```sql
SELECT NULLIF(expression1, expression2) FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*expression*__  
Два вирази будь-якого типу даних, результатом яких є значення `NULL` або значення, відмінне від `NULL`.

> Функція `NULLIF` шукає серед своїх аргументів зліва направо аргумент, що визначає функцію зіставлення, і використовує цю функцію зіставлення для всіх порівнянь рядкових значень.  Якщо жоден аргумент не визначає функції зіставлення, використовується зіставлення `BINARY`.

## Значення, що повертається {#return-values}

Повертає `NULL`, якщо два аргументи *expression* мають однакове значення, інакше повертає перше значення. 

* * *

## Приклади {#examples}

### Підрахунок рядків, у яких поле не дорівнює вказаному значенню {#count-records-where-a-field-is-not-equal-to-a-given-value}

```sql
SELECT COUNT(NULLIF(name, 'controller-0'))
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
/*
equivalent to: 
SELECT COUNT(*) FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a' 
AND name != 'controller-0';
*/
```

Докладніше див. [https://www.sqlite.org/lang_corefunc.html#nullif](https://www.sqlite.org/lang_corefunc.html#nullif)
