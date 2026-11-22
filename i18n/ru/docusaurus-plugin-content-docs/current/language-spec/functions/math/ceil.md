---
title: CEIL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Функция CEIL в StackQL: округление числового аргумента вверх до ближайшего целого числа."
image: "/img/stackql-featured-image.png"
---
Округляет числовой аргумент вверх до ближайшего целого числа.  

См. также:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Синтаксис {#syntax}

```sql
SELECT CEIL(numeric_expression) FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*numeric_expression*__  
Выражение, относящееся к категории точных или приближенных числовых типов данных.

## Возвращаемое значение {#return-values}
Возвращает наименьшее целое число, которое больше указанного числового выражения или равно ему.

* * *

## Примеры {#examples}

### Округление числа с плавающей запятой вверх до ближайшего (большего) целого числа {#round-up-a-floating-point-number-to-the-closest-larger-integer}

```sql
SELECT name, timeCreated,
ceil(julianday('now')-julianday(timeCreated)) as days_since_ceiling
FROM google.storage.buckets WHERE project = 'stackql';
```
