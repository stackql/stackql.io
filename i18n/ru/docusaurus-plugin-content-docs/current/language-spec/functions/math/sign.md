---
title: SIGN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Функция SIGN в StackQL: возвращает -1, 0 или 1 в зависимости от того, отрицателен, равен нулю или положителен числовой аргумент."
image: "/img/stackql-featured-image.png"
---
Возвращает -1, если входное значение отрицательно, 0, если входное значение равно 0, и 1, если входное значение положительно.  

См. также:  
[[` SELECT `]](/language-spec/select) 

* * * 

## Синтаксис {#syntax}

```sql
SELECT SIGN(numeric_expression) FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*numeric_expression*__  
Выражение, относящееся к категории точных или приближенных числовых типов данных.

> Если *numeric_expression* равно `NULL` либо представляет собой значение типа `string` или BLOB, которое нельзя без потерь преобразовать в число, функция возвращает `NULL`.

## Возвращаемое значение {#return-values}

Возвращает `-1`, `0` или `1`.

* * *

## Примеры {#examples}

### Определение знака числа {#determine-the-sign-of-a-number}

```sql
SELECT name, sign(strftime('%s', creationTimestamp)-strftime('%s','now')) as sign
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Подробнее см. [https://www.sqlite.org/lang_corefunc.html#sign](https://www.sqlite.org/lang_corefunc.html#sign)
