---
title: IIF
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Функция IIF в StackQL: возвращает одно из двух значений в зависимости от того, истинно ли условие; встроенное выражение if-then-else."
image: "/img/stackql-featured-image.png"
---
Возвращает значение в зависимости от результата вычисления входного выражения.  

См. также:  
[[` SELECT `]](/language-spec/select)

* * * 

## Синтаксис {#syntax}

```sql
SELECT IIF(expression, result_if_true, result_if_false) 
FROM <multipartIdentifier>;
```
> Функция `iif()` логически эквивалентна выражению `CASE` вида `CASE WHEN expression THEN result_if_true ELSE result_if_false END`

## Аргументы {#arguments}

__*expression*__  
Вычисляемое логическое выражение.

:::tip 

Аргумент *expression* может быть вложенной функцией, например `JSON_EXTRACT`.

:::

__*result_if_true*__  
Результат, который возвращается, если значение *expression* равно `true`.

__*result_if_false*__  
Результат, который возвращается, если значение *expression* равно `false`.

## Возвращаемое значение {#return-values}
Тип данных аргументов *result_if_true* или *result_if_false*.

* * *

## Примеры {#examples}

### Классификация размеров дисков экземпляров Compute Engine {#categorize-disk-sizes-of-compute-engine-instances}

```sql
SELECT iif(CAST(json_extract(disks, '$[0].diskSizeGb') AS INTEGER) > 10, '> 10GB', '<= 10GB') as disk_size
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Подробнее см. [https://www.sqlite.org/lang_corefunc.html#iif](https://www.sqlite.org/lang_corefunc.html#iif)
