---
title: COALESCE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Функція COALESCE у StackQL: повертає перший відмінний від NULL аргумент із двох або більше, зазвичай для заміни відсутнього значення значенням за замовчуванням."
image: "/img/stackql-featured-image.png"
---
Повертає перший аргумент, відмінний від `NULL`, із послідовності двох або більше аргументів; її можна використовувати для заміни значення `NULL` значенням за замовчуванням.  

> Функція `COALESCE` еквівалентна функції `NVL` в Oracle або функції `IFNULL` у MySQL.

Див. також:  
[[` SELECT `]](/language-spec/select) [[` IFNULL `]](/language-spec/functions/general/ifnull) [[` NULLIF `]](/language-spec/functions/general/nullif)

* * * 

## Синтаксис {#syntax}

```sql
SELECT COALESCE(expression1, expression2 [, ..]) FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*expression*__  
Два або більше виразів будь-якого типу даних, результатом яких є значення `NULL` або значення, відмінне від `NULL`.

> Якщо всі аргументи *expression* дорівнюють `NULL`, функція `COALESCE` повертає `NULL`.

## Значення, що повертається {#return-values}

Повертає перше значення, відмінне від `NULL`, із послідовності аргументів *expression*.

* * *

## Приклади {#examples}

### Заміна значення `NULL` значенням за замовчуванням {#replace-a-null-value-with-a-default-value}

```sql
SELECT name, coalesce(website, cors, 'not hosting shared resources') AS website_or_cors_config
FROM google.storage.buckets where project = 'stackql';
```

Докладніше див. [https://www.sqlite.org/lang_corefunc.html#coalesce](https://www.sqlite.org/lang_corefunc.html#coalesce)
