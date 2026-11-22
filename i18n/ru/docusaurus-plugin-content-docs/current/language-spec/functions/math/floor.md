---
title: FLOOR
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Функция FLOOR в StackQL: округление числового аргумента вниз до ближайшего целого числа."
image: "/img/stackql-featured-image.png"
---
Округляет числовой аргумент вниз до ближайшего целого числа.  

См. также:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Синтаксис {#syntax}

```sql
SELECT FLOOR(numeric_expression) FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*numeric_expression*__  
Выражение, относящееся к категории точных или приближенных числовых типов данных.

## Возвращаемое значение {#return-values}
Возвращает наибольшее целое число, которое меньше указанного числового выражения или равно ему.

* * *

## Примеры {#examples}

### Округление числа с плавающей запятой вниз до ближайшего (меньшего) целого числа {#round-down-a-floating-point-number-to-the-closest-smaller-integer}

```sql
SELECT name, timeCreated,
floor(julianday('now')-julianday(timeCreated)) as days_since_floor
FROM google.storage.buckets WHERE project = 'stackql';
```
