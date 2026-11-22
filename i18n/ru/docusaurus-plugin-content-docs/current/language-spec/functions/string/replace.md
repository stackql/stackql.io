---
title: REPLACE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Функция REPLACE в StackQL: замена каждого вхождения подстроки в строке другой строкой."
image: "/img/stackql-featured-image.png"
---
Возвращает копию строки, в которой каждое вхождение подстроки заменено другой строкой.

См. также:  
[[` SELECT `]](/language-spec/select) 

* * * 

## Синтаксис {#syntax}

```sql
SELECT REPLACE(character_expression, pattern, replacement) FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*character_expression*__  
Строковый литерал или строковый столбец, в котором нужно выполнить замену.

__*pattern*__  
Подстрока, которую нужно найти в исходной строке.

> Если *pattern* является пустой строкой или отсутствует в *character_expression*, то *character_expression* возвращается без изменений. 

__*replacement*__  
Строка для замены.

> Если *replacement* изначально не является строкой, то перед обработкой это значение приводится к строке в кодировке UTF-8.

> Для сравнений используется порядок сравнения `BINARY`.

## Возвращаемое значение {#return-values}
Возвращает строку.

* * *

## Примеры {#examples}

### Замена строкового шаблона в столбце {#replace-a-string-pattern-in-a-column}

```sql
SELECT replace(name, 'prd', 'production') AS replacement_string
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Подробнее см. [https://www.sqlite.org/lang_corefunc.html#replace](https://www.sqlite.org/lang_corefunc.html#replace)
