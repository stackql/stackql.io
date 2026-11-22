---
title: IFNULL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Функція IFNULL у StackQL: повертає перший із рівно двох аргументів, відмінний від NULL; це форма COALESCE із двома аргументами."
image: "/img/stackql-featured-image.png"
---
Приймає рівно два аргументи й повертає перший аргумент, відмінний від `NULL`; її можна використовувати для заміни значення `NULL` значенням за замовчуванням.  

> Функція `IFNULL` еквівалентна функції [`COALESCE`](/language-spec/functions/general/coalesce) лише з двома аргументами, а функція [`COALESCE`](/language-spec/functions/general/coalesce) може мати більше двох аргументів.

Див. також:  
[[` SELECT `]](/language-spec/select) [[` COALESCE `]](/language-spec/functions/general/coalesce) [[` NULLIF `]](/language-spec/functions/general/nullif)

* * * 

## Синтаксис {#syntax}

```sql
SELECT IFNULL(expression1, expression2) FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*expression*__  
Два вирази будь-якого типу даних, результатом яких є значення `NULL` або значення, відмінне від `NULL`.

> Якщо обидва аргументи *expression* дорівнюють `NULL`, функція `IFNULL` повертає `NULL`.


## Значення, що повертається {#return-values}

Повертає перше значення, відмінне від `NULL`, із двох аргументів *expression*. 

* * *

## Приклади {#examples}

### Заміна значення `NULL` значенням за замовчуванням {#replace-a-null-value-with-a-default-value}

```sql
SELECT name, ifnull(website, 'Not hosting a website') AS website 
FROM google.storage.buckets where project = 'stackql';
```

Докладніше див. [https://www.sqlite.org/lang_corefunc.html#ifnull](https://www.sqlite.org/lang_corefunc.html#ifnull)
