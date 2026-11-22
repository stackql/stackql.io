---
title: COALESCE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Функция COALESCE в StackQL: возвращает первый отличный от NULL аргумент из двух или более, обычно чтобы подставить значение по умолчанию вместо отсутствующего."
image: "/img/stackql-featured-image.png"
---
Возвращает первый аргумент, отличный от `NULL`, из последовательности двух или более аргументов; может использоваться, чтобы подставить значение по умолчанию вместо значения `NULL`.  

> Функция `COALESCE` эквивалентна функции `NVL` в Oracle или функции `IFNULL` в MySQL.

См. также:  
[[` SELECT `]](/language-spec/select) [[` IFNULL `]](/language-spec/functions/general/ifnull) [[` NULLIF `]](/language-spec/functions/general/nullif)

* * * 

## Синтаксис {#syntax}

```sql
SELECT COALESCE(expression1, expression2 [, ..]) FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*expression*__  
Два или более выражений любого типа данных, которые возвращают `NULL` или значение, отличное от `NULL`.

> Если все аргументы *expression* равны `NULL`, функция `COALESCE` возвращает `NULL`.

## Возвращаемое значение {#return-values}

Возвращает первое значение, отличное от `NULL`, из последовательности аргументов *expression*.

* * *

## Примеры {#examples}

### Замена значения `NULL` значением по умолчанию {#replace-a-null-value-with-a-default-value}

```sql
SELECT name, coalesce(website, cors, 'not hosting shared resources') AS website_or_cors_config
FROM google.storage.buckets where project = 'stackql';
```

Подробнее см. [https://www.sqlite.org/lang_corefunc.html#coalesce](https://www.sqlite.org/lang_corefunc.html#coalesce)
