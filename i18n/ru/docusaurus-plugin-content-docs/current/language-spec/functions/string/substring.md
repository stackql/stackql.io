---
title: SUBSTRING
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Функция SUBSTRING в StackQL (псевдоним SUBSTR): получение части строкового или двоичного значения по начальной позиции и длине."
image: "/img/stackql-featured-image.png"
---
Возвращает часть символьного или двоичного выражения.  `SUBSTR` - псевдоним для `SUBSTRING`.

См. также:  
[[` SELECT `]](/language-spec/select) [[ Типы данных ]](/language-spec/data-types)

* * * 

:::info

Чтобы извлечь поле из JSON-объекта, используйте функцию [**JSON_EXTRACT**](/language-spec/functions/json/json_extract).

:::

## Синтаксис {#syntax}

```sql
SELECT SUBSTRING(expression, start[, length]) FROM <multipartIdentifier>;
SELECT SUBSTR(expression, start[, length]) FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*expression*__  
Строка или значение BLOB.

> Если *expression* является строкой, то индексы символов соответствуют фактическим символам UTF-8. Если *expression* является значением BLOB, то индексы соответствуют байтам.

__*start*__  
Целое число, обозначающее первый символ или байт возвращаемой подпоследовательности входного выражения.  Если *start* имеет отрицательное значение, то первый символ подстроки определяется отсчетом справа, а не слева.

> Крайний левый символ *expression* имеет номер 1.

__*length*__  
Необязательное целое число, обозначающее длину подпоследовательности, возвращаемой после значения *start*.  Если аргумент *length* не указан, то возвращаются все символы до конца строки или значения BLOB, заданного аргументом *expression*.

> Если *length* имеет отрицательное значение, то возвращаются символы, предшествующие *start*, в количестве, равном абсолютному значению *length*: `SUBSTR('abcdef', 4, -2)` возвращает `bc`.

## Возвращаемое значение {#return-values}

Возвращает строку или значение BLOB в зависимости от типа *expression*.

* * *

## Примеры {#examples}

### Получение первых *n* символов из столбца {#return-the-first-n-characters-from-a-column}

```sql
SELECT SUBSTRING(name, 1, 5) as "first five chars" 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Усечение первых *n* символов из столбца {#truncate-the-first-n-characters-from-a-column}

```sql
SELECT SUBSTR(name, 5) as "from char 5" 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Получение последних *n* символов из столбца {#return-the-last-n-characters-from-a-column}

```sql
SELECT SUBSTRING(name, -5) as "last five chars" 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Подробнее см. [https://www.sqlite.org/lang_corefunc.html#substr](https://www.sqlite.org/lang_corefunc.html#substr)
