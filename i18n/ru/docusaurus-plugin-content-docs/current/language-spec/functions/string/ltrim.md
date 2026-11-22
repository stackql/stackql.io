---
title: LTRIM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Функция LTRIM в StackQL: удаление пробелов или любых символов из заданного набора с левой стороны строки."
image: "/img/stackql-featured-image.png"
---
Возвращает строку, полученную удалением пробелов или любых символов из заданного набора с левой стороны входной строки.  

См. также:  
[[` SELECT `]](/language-spec/select) [[` RTRIM `]](/language-spec/functions/string/rtrim) [[` TRIM `]](/language-spec/functions/string/trim) 

* * * 

## Синтаксис {#syntax}

```sql
SELECT LTRIM(character_expression[, character ]) FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*character_expression*__  
Строковый литерал или строковый столбец.

__*character*__  
Необязательная строка символов: каждый содержащийся в ней символ удаляется из начала *character_expression*.  Если она не указана, удаляются пробелы (но не символы табуляции или новой строки).

## Возвращаемое значение {#return-values}
Возвращает строку.

* * *

## Примеры {#examples}

### Удаление начальных пробелов из строкового столбца {#remove-leading-spaces-from-a-string-column}

```sql
SELECT ltrim(description) AS leading_whitespace_removed
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Подробнее см. [https://www.sqlite.org/lang_corefunc.html#ltrim](https://www.sqlite.org/lang_corefunc.html#ltrim)
