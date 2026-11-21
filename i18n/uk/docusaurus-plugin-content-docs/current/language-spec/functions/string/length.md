---
title: LENGTH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Функція LENGTH у StackQL: кількість символів у рядку або кількість байтів у значенні BLOB."
image: "/img/stackql-featured-image.png"
---
Повертає кількість символів у рядковому вхідному виразі до першого символу `NULL` або кількість байтів у виразі BLOB.  

Див. також:  
[[` SELECT `]](/language-spec/select) 

* * * 

:::info

Щоб отримати довжину масиву JSON, скористайтеся функцією [**JSON_ARRAY_LENGTH**](/language-spec/functions/json/json_array_length).

:::

## Синтаксис {#syntax}

```sql
SELECT LENGTH(expression) FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*expression*__  
Рядок ASCII або значення BLOB.

> Повертає `NULL`, якщо *expression* дорівнює `NULL`.

> Якщо *expression* є числом, то повертається довжина *expression*, перетвореного на рядок.

## Значення, що повертається {#return-values}
Повертає ціле число.

* * *

## Приклади {#examples}

### Отримання довжини рядкового стовпця {#return-the-length-of-a-string-column}

```sql
SELECT name, length(name) AS len
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Докладніше див. [https://www.sqlite.org/lang_corefunc.html#length](https://www.sqlite.org/lang_corefunc.html#length)
