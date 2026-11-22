---
title: LOWER
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Функция LOWER в StackQL: преобразование строки в нижний регистр."
image: "/img/stackql-featured-image.png"
---
Возвращает входную строку в нижнем регистре.  

См. также:  
[[` SELECT `]](/language-spec/select) [[` UPPER `]](/language-spec/functions/string/upper) [[ Типы данных ]](/language-spec/data-types)

* * * 

## Синтаксис {#syntax}

```sql
SELECT LOWER(character_expression) FROM <multipartIdentifier>;
```
> Функция `lower()` работает только с символами ASCII.

## Аргументы {#arguments}

__*character_expression*__  
Строка из символов ASCII.

## Возвращаемое значение {#return-values}
Возвращает строковое значение.

* * *

## Примеры {#examples}

### Получение строки в нижнем регистре {#return-a-string-in-lowercase}

```sql
SELECT name, LOWER(location) FROM google.storage.buckets WHERE project = 'stackql-demo';
```

Подробнее см. [https://www.sqlite.org/lang_corefunc.html#lower](https://www.sqlite.org/lang_corefunc.html#lower)
