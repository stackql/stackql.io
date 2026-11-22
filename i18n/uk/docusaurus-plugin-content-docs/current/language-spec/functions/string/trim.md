---
title: TRIM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Функція TRIM у StackQL: видалення пробілів або будь-яких символів із заданого набору з обох кінців рядка."
image: "/img/stackql-featured-image.png"
---
Повертає рядок, утворений видаленням пробілів або будь-яких символів із заданого набору з обох кінців вхідного рядка.  

Див. також:  
[[` SELECT `]](/language-spec/select) [[` LTRIM `]](/language-spec/functions/string/ltrim) [[` RTRIM `]](/language-spec/functions/string/rtrim) 

* * * 

## Синтаксис {#syntax}

```sql
SELECT TRIM(character_expression[, character ]) FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*character_expression*__  
Рядковий літерал або рядковий стовпець.

__*character*__  
Необов'язковий рядок символів: кожен символ із нього видаляється з початку й кінця *character_expression*.  Якщо його не вказано, видаляються пробіли (але не символи табуляції чи нового рядка).

## Значення, що повертається {#return-values}
Повертає рядок.

* * *

## Приклади {#examples}

### Видалення початкових і кінцевих пробілів із рядкового стовпця {#remove-leading-and-trailing-spaces-from-a-string-column}

```sql
SELECT trim(description) AS leading_and_trailing_whitespace_removed
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Докладніше див. [https://www.sqlite.org/lang_corefunc.html#trim](https://www.sqlite.org/lang_corefunc.html#trim)
