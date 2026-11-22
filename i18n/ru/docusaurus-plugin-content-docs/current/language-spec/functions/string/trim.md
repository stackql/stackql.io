---
title: TRIM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Функция TRIM в StackQL: удаление пробелов или любых символов из заданного набора с обоих концов строки."
image: "/img/stackql-featured-image.png"
---
Возвращает строку, полученную удалением пробелов или любых символов из заданного набора с обоих концов входной строки.  

См. также:  
[[` SELECT `]](/language-spec/select) [[` LTRIM `]](/language-spec/functions/string/ltrim) [[` RTRIM `]](/language-spec/functions/string/rtrim) 

* * * 

## Синтаксис {#syntax}

```sql
SELECT TRIM(character_expression[, character ]) FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*character_expression*__  
Строковый литерал или строковый столбец.

__*character*__  
Необязательная строка символов: каждый содержащийся в ней символ удаляется из начала и конца *character_expression*.  Если она не указана, удаляются пробелы (но не символы табуляции или новой строки).

## Возвращаемое значение {#return-values}
Возвращает строку.

* * *

## Примеры {#examples}

### Удаление начальных и конечных пробелов из строкового столбца {#remove-leading-and-trailing-spaces-from-a-string-column}

```sql
SELECT trim(description) AS leading_and_trailing_whitespace_removed
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Подробнее см. [https://www.sqlite.org/lang_corefunc.html#trim](https://www.sqlite.org/lang_corefunc.html#trim)
