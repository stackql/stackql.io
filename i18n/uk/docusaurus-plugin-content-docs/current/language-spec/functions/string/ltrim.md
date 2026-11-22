---
title: LTRIM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Функція LTRIM у StackQL: видалення пробілів або будь-яких символів із заданого набору з лівого боку рядка."
image: "/img/stackql-featured-image.png"
---
Повертає рядок, утворений видаленням пробілів або будь-яких символів із заданого набору з лівого боку вхідного рядка.  

Див. також:  
[[` SELECT `]](/language-spec/select) [[` RTRIM `]](/language-spec/functions/string/rtrim) [[` TRIM `]](/language-spec/functions/string/trim) 

* * * 

## Синтаксис {#syntax}

```sql
SELECT LTRIM(character_expression[, character ]) FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*character_expression*__  
Рядковий літерал або рядковий стовпець.

__*character*__  
Необов'язковий рядок символів: кожен символ із нього видаляється з початку *character_expression*.  Якщо його не вказано, видаляються пробіли (але не символи табуляції чи нового рядка).

## Значення, що повертається {#return-values}
Повертає рядок.

* * *

## Приклади {#examples}

### Видалення початкових пробілів із рядкового стовпця {#remove-leading-spaces-from-a-string-column}

```sql
SELECT ltrim(description) AS leading_whitespace_removed
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Докладніше див. [https://www.sqlite.org/lang_corefunc.html#ltrim](https://www.sqlite.org/lang_corefunc.html#ltrim)
