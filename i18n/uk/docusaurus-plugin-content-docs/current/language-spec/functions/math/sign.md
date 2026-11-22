---
title: SIGN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Функція SIGN у StackQL: повертає -1, 0 або 1 залежно від того, чи є числовий аргумент від'ємним, нульовим чи додатним."
image: "/img/stackql-featured-image.png"
---
Повертає -1, якщо вхідне значення від'ємне, 0, якщо вхідне значення дорівнює 0, і 1, якщо вхідне значення додатне.  

Див. також:  
[[` SELECT `]](/language-spec/select) 

* * * 

## Синтаксис {#syntax}

```sql
SELECT SIGN(numeric_expression) FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*numeric_expression*__  
Вираз із категорії точних або наближених числових типів даних.

> Якщо *numeric_expression* дорівнює `NULL` або є значенням типу `string` чи BLOB, яке неможливо без втрат перетворити на число, функція повертає `NULL`.

## Значення, що повертається {#return-values}

Повертає `-1`, `0` або `1`.

* * *

## Приклади {#examples}

### Визначення знака числа {#determine-the-sign-of-a-number}

```sql
SELECT name, sign(strftime('%s', creationTimestamp)-strftime('%s','now')) as sign
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Докладніше див. [https://www.sqlite.org/lang_corefunc.html#sign](https://www.sqlite.org/lang_corefunc.html#sign)
