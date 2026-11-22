---
title: NULLIF
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Функция NULLIF в StackQL: возвращает NULL, если два аргумента равны, иначе возвращает первый аргумент."
image: "/img/stackql-featured-image.png"
---
Принимает два аргумента и возвращает значение `NULL`, если они равны; в противном случае возвращается первый аргумент.  

> Функция `NULLIF` эквивалентна выражению `CASE WHEN expression1 = expression2 THEN NULL ELSE expression1 END`

> Функция `NULLIF` полезна с агрегатными функциями, например [`AVG`](/language-spec/functions/aggregate/avg), [`MAX`](/language-spec/functions/aggregate/max), [`MIN`](/language-spec/functions/aggregate/min), [`SUM`](/language-spec/functions/aggregate/sum) и [`COUNT`](/language-spec/functions/aggregate/count), когда результирующий набор содержит специальные значения, которые не равны `NULL`, но при агрегировании должны рассматриваться как `NULL`.

См. также:  
[[` SELECT `]](/language-spec/select) [[` COALESCE `]](/language-spec/functions/general/coalesce) [[` IFNULL `]](/language-spec/functions/general/ifnull)

* * * 

## Синтаксис {#syntax}

```sql
SELECT NULLIF(expression1, expression2) FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*expression*__  
Два выражения любого типа данных, которые возвращают `NULL` или значение, отличное от `NULL`.

> Функция `NULLIF` просматривает свои аргументы слева направо в поисках аргумента, который задает функцию сравнения, и использует эту функцию сравнения для всех сравнений строковых значений.  Если ни один из аргументов не задает функцию сравнения, используется порядок сравнения `BINARY`.

## Возвращаемое значение {#return-values}

Возвращает `NULL`, если два аргумента *expression* дают одно и то же значение; в противном случае возвращается первое значение. 

* * *

## Примеры {#examples}

### Подсчет строк, в которых поле не равно заданному значению {#count-records-where-a-field-is-not-equal-to-a-given-value}

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

Подробнее см. [https://www.sqlite.org/lang_corefunc.html#nullif](https://www.sqlite.org/lang_corefunc.html#nullif)
