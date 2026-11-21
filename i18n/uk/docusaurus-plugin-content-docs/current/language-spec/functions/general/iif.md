---
title: IIF
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Функція IIF у StackQL: повертає одне з двох значень залежно від того, чи істинна умова; вбудований вираз if-then-else."
image: "/img/stackql-featured-image.png"
---
Повертає значення залежно від результату обчислення вхідного виразу.  

Див. також:  
[[` SELECT `]](/language-spec/select)

* * * 

## Синтаксис {#syntax}

```sql
SELECT IIF(expression, result_if_true, result_if_false) 
FROM <multipartIdentifier>;
```
> Функція `iif()` логічно еквівалентна такому виразу `CASE`: `CASE WHEN expression THEN result_if_true ELSE result_if_false END`

## Аргументи {#arguments}

__*expression*__  
Логічний вираз, який потрібно обчислити.

:::tip 

Аргумент *expression* може бути вкладеною функцією, як-от `JSON_EXTRACT`.

:::

__*result_if_true*__  
Результат, який буде повернуто, якщо значенням *expression* є `true`.

__*result_if_false*__  
Результат, який буде повернуто, якщо значенням *expression* є `false`.

## Значення, що повертається {#return-values}
Тип даних аргументів *result_if_true* або *result_if_false*.

* * *

## Приклади {#examples}

### Категоризація розмірів дисків екземплярів compute engine {#categorize-disk-sizes-of-compute-engine-instances}

```sql
SELECT iif(CAST(json_extract(disks, '$[0].diskSizeGb') AS INTEGER) > 10, '> 10GB', '<= 10GB') as disk_size
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Докладніше див. [https://www.sqlite.org/lang_corefunc.html#iif](https://www.sqlite.org/lang_corefunc.html#iif)
