---
title: GROUP_CONCAT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Агрегатна функція GROUP_CONCAT у StackQL: конкатенація відмінних від NULL значень стовпця або групування в один рядок із необов'язковим роздільником."
image: "/img/stackql-featured-image.png"
---
Повертає рядкове значення, що є конкатенацією всіх відмінних від `NULL` значень вхідного стовпця або групування.  

> Порядок конкатенованих елементів довільний.

Див. також:  
[[` SELECT `]](/language-spec/select)

* * * 

## Синтаксис {#syntax}

```sql
SELECT GROUP_CONCAT(columnExpression [, separator]) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## Аргументи {#arguments}

__*columnExpression*__  
Стовпець або вираз.  

> Принаймні одне значення *columnExpression* має бути відмінним від `NULL`, інакше повертається `NULL`.

__*separator*__  
Роздільник для результату конкатенації; якщо *separator* не вказано, використовується значення за замовчуванням `,`.

__*groupByColumn*__  
Стовпець або стовпці, за якими виконуються підсумкові чи агрегатні операції.  Речення `GROUP BY` повертає один рядок для кожного групування стовпців.

## Значення, що повертається {#return-values}

Повертає рядкове значення у вигляді списку значень, розділених роздільником *separator* або `,`.

* * *

## Приклади {#examples}

### Отримання списку значень стовпця для всього ресурсу {#return-a-list-of-values-for-a-column-over-an-entire-resource}

```sql
SELECT group_concat(name) as instance_list
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Отримання списку значень виразу стовпця, згрупованих за іншим виразом стовпця {#return-a-list-of-values-for-a-column-expression-grouped-by-another-column-expression}

```sql
SELECT SPLIT_PART(machineType, '/', -1) as machine_type, group_concat(name) as instance_list
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY SPLIT_PART(machineType, '/', -1);
```

Докладніше див. [https://www.sqlite.org/lang_aggfunc.html#group_concat](https://www.sqlite.org/lang_aggfunc.html#group_concat).
