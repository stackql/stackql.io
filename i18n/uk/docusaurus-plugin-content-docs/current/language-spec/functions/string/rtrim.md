---
title: RTRIM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Функція RTRIM у StackQL: видалення пробілів або будь-яких символів із заданого набору з правого боку рядка."
image: "/img/stackql-featured-image.png"
---
Повертає рядок, утворений видаленням пробілів або будь-яких символів із заданого набору з правого боку вхідного рядка.  

Див. також:  
[[` SELECT `]](/language-spec/select) [[` LTRIM `]](/language-spec/functions/string/ltrim) [[` TRIM `]](/language-spec/functions/string/trim) 

* * * 

## Синтаксис {#syntax}

```sql
SELECT RTRIM(character_expression[, character ]) FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*character_expression*__  
Рядковий літерал або рядковий стовпець.

__*character*__  
Необов'язковий рядок символів: кожен символ із нього видаляється з кінця *character_expression*.  Якщо його не вказано, видаляються пробіли (але не символи табуляції чи нового рядка).

## Значення, що повертається {#return-values}
Повертає рядок.

* * *

## Приклади {#examples}

### Видалення кінцевих пробілів із рядкового стовпця {#remove-trailing-spaces-from-a-string-column}

```sql
SELECT rtrim(description) AS trailing_whitespace_removed
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Докладніше див. [https://www.sqlite.org/lang_corefunc.html#rtrim](https://www.sqlite.org/lang_corefunc.html#rtrim)
