---
title: RTRIM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Функция RTRIM в StackQL: удаление пробелов или любых символов из заданного набора с правой стороны строки."
image: "/img/stackql-featured-image.png"
---
Возвращает строку, полученную удалением пробелов или любых символов из заданного набора с правой стороны входной строки.  

См. также:  
[[` SELECT `]](/language-spec/select) [[` LTRIM `]](/language-spec/functions/string/ltrim) [[` TRIM `]](/language-spec/functions/string/trim) 

* * * 

## Синтаксис {#syntax}

```sql
SELECT RTRIM(character_expression[, character ]) FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*character_expression*__  
Строковый литерал или строковый столбец.

__*character*__  
Необязательная строка символов: каждый содержащийся в ней символ удаляется из конца *character_expression*.  Если она не указана, удаляются пробелы (но не символы табуляции или новой строки).

## Возвращаемое значение {#return-values}
Возвращает строку.

* * *

## Примеры {#examples}

### Удаление конечных пробелов из строкового столбца {#remove-trailing-spaces-from-a-string-column}

```sql
SELECT rtrim(description) AS trailing_whitespace_removed
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Подробнее см. [https://www.sqlite.org/lang_corefunc.html#rtrim](https://www.sqlite.org/lang_corefunc.html#rtrim)
