---
title: LENGTH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Функция LENGTH в StackQL: число символов в строке или число байтов в значении BLOB."
image: "/img/stackql-featured-image.png"
---
Возвращает число символов во входном строковом выражении до первого символа NUL (`char(0)`) или число байтов в выражении BLOB.  

См. также:  
[[` SELECT `]](/language-spec/select) 

* * * 

:::info

Чтобы получить длину JSON-массива, используйте функцию [**JSON_ARRAY_LENGTH**](/language-spec/functions/json/json_array_length).

:::

## Синтаксис {#syntax}

```sql
SELECT LENGTH(expression) FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*expression*__  
Строка или значение BLOB.

> Возвращает `NULL`, если *expression* имеет значение `NULL`.

> Если *expression* имеет числовое значение, то возвращается длина строкового представления *expression*.

## Возвращаемое значение {#return-values}
Возвращает целое число.

* * *

## Примеры {#examples}

### Получение длины строкового столбца {#return-the-length-of-a-string-column}

```sql
SELECT name, length(name) AS len
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Подробнее см. [https://www.sqlite.org/lang_corefunc.html#length](https://www.sqlite.org/lang_corefunc.html#length)
