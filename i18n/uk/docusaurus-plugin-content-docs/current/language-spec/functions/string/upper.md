---
title: UPPER
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Функція UPPER у StackQL: перетворення рядка на верхній регістр."
image: "/img/stackql-featured-image.png"
---
Повертає вхідний рядок у верхньому регістрі.  

Див. також:  
[[` SELECT `]](/language-spec/select) [[` LOWER `]](/language-spec/functions/string/lower) [[ Типи даних ]](/language-spec/data-types)

* * * 

## Синтаксис {#syntax}

```sql
SELECT UPPER(character_expression) FROM <multipartIdentifier>;
```
> Функція `upper()` працює лише із символами ASCII.

## Аргументи {#arguments}

__*character_expression*__  
Рядок ASCII.

## Значення, що повертається {#return-values}
Повертає рядкове значення.

* * *

## Приклади {#examples}

### Отримання рядка у верхньому регістрі {#return-a-string-in-uppercase}

```sql
SELECT UPPER(name), location FROM google.storage.buckets WHERE project = 'stackql-demo';
```

Докладніше див. [https://www.sqlite.org/lang_corefunc.html#upper](https://www.sqlite.org/lang_corefunc.html#upper)
