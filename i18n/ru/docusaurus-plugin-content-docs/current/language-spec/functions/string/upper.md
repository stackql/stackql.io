---
title: UPPER
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Функция UPPER в StackQL: преобразование строки в верхний регистр."
image: "/img/stackql-featured-image.png"
---
Возвращает входную строку в верхнем регистре.  

См. также:  
[[` SELECT `]](/language-spec/select) [[` LOWER `]](/language-spec/functions/string/lower) [[ Типы данных ]](/language-spec/data-types)

* * * 

## Синтаксис {#syntax}

```sql
SELECT UPPER(character_expression) FROM <multipartIdentifier>;
```
> Функция `upper()` работает только с символами ASCII.

## Аргументы {#arguments}

__*character_expression*__  
Строка из символов ASCII.

## Возвращаемое значение {#return-values}
Возвращает строковое значение.

* * *

## Примеры {#examples}

### Получение строки в верхнем регистре {#return-a-string-in-uppercase}

```sql
SELECT UPPER(name), location FROM google.storage.buckets WHERE project = 'stackql-demo';
```

Подробнее см. [https://www.sqlite.org/lang_corefunc.html#upper](https://www.sqlite.org/lang_corefunc.html#upper)
