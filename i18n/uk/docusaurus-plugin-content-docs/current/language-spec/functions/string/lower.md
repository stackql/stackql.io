---
title: LOWER
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Функція LOWER у StackQL: перетворення рядка на нижній регістр."
image: "/img/stackql-featured-image.png"
---
Повертає вхідний рядок у нижньому регістрі.  

Див. також:  
[[` SELECT `]](/language-spec/select) [[` UPPER `]](/language-spec/functions/string/upper) [[ Типи даних ]](/language-spec/data-types)

* * * 

## Синтаксис {#syntax}

```sql
SELECT LOWER(character_expression) FROM <multipartIdentifier>;
```
> Функція `lower()` працює лише із символами ASCII.

## Аргументи {#arguments}

__*character_expression*__  
Рядок ASCII.

## Значення, що повертається {#return-values}
Повертає рядкове значення.

* * *

## Приклади {#examples}

### Отримання рядка в нижньому регістрі {#return-a-string-in-lowercase}

```sql
SELECT name, LOWER(location) FROM google.storage.buckets WHERE project = 'stackql-demo';
```

Докладніше див. [https://www.sqlite.org/lang_corefunc.html#lower](https://www.sqlite.org/lang_corefunc.html#lower)
