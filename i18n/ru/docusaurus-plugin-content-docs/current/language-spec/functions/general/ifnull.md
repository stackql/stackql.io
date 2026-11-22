---
title: IFNULL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Функция IFNULL в StackQL: возвращает первый из ровно двух аргументов, отличный от NULL; это форма COALESCE с двумя аргументами."
image: "/img/stackql-featured-image.png"
---
Принимает ровно два аргумента и возвращает первый аргумент, отличный от `NULL`; может использоваться, чтобы подставить значение по умолчанию вместо значения `NULL`.  

> Функция `IFNULL` эквивалентна функции [`COALESCE`](/language-spec/functions/general/coalesce) только с двумя аргументами; функция [`COALESCE`](/language-spec/functions/general/coalesce) может принимать больше двух аргументов.

См. также:  
[[` SELECT `]](/language-spec/select) [[` COALESCE `]](/language-spec/functions/general/coalesce) [[` NULLIF `]](/language-spec/functions/general/nullif)

* * * 

## Синтаксис {#syntax}

```sql
SELECT IFNULL(expression1, expression2) FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*expression*__  
Два выражения любого типа данных, которые возвращают `NULL` или значение, отличное от `NULL`.

> Если оба аргумента *expression* равны `NULL`, функция `IFNULL` возвращает `NULL`.


## Возвращаемое значение {#return-values}

Возвращает первое значение, отличное от `NULL`, из двух аргументов *expression*. 

* * *

## Примеры {#examples}

### Замена значения `NULL` значением по умолчанию {#replace-a-null-value-with-a-default-value}

```sql
SELECT name, ifnull(website, 'Not hosting a website') AS website 
FROM google.storage.buckets where project = 'stackql';
```

Подробнее см. [https://www.sqlite.org/lang_corefunc.html#ifnull](https://www.sqlite.org/lang_corefunc.html#ifnull)
