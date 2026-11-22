---
title: JSON_EACH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Функция с табличным значением JSON_EACH в StackQL: разворачивает JSON-массив или объект в строки по элементам или членам для соединения и фильтрации вложенных ответов API."
image: "/img/stackql-featured-image.png"
---
Функция с табличным значением, которая возвращает таблицу из одной строки для каждого элемента массива или члена объекта.

См. также:  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[` JSON_ARRAY_LENGTH `]](/language-spec/functions/json/json_array_length) [[ Типы данных ]](/language-spec/data-types)

* * * 

:::tip 

Используйте инструкцию [**DESCRIBE**](/language-spec/describe), чтобы найти типы данных `array`, которые можно использовать с функцией `json_each`.

:::

:::note 

Функция `json_each` реализована как функция `json_array_elements_text`, если вы используете stackql с SQL-бэкендом `postgres`

:::

## Синтаксис {#syntax}

```sql
SELECT <alias>.<projectedField>, ... , json_each.value  
FROM <multipartIdentifier> <alias>, JSON_EACH(json_array_or_object [, json_path]);
```
## Аргументы {#arguments}

__*json_array_or_object*__  
JSON-массив или JSON-объект.

__*json_path*__  
Необязательный. Путь JSON к массиву или объекту внутри *json_array_or_object*.  См. [jsonpath.com](https://jsonpath.com/).

> Если *json_path* не существует в *json_array_or_object*, функция не возвращает ни одной строки.

> Функция `json_each()` выдает ошибку, если *json_array_or_object* - неправильно сформированный JSON.

## Возвращаемое значение {#return-values}
Таблица из одной строки для каждого элемента массива или члена объекта.

* * *

## Примеры {#examples}

### Разворачивание массива строк в таблицу, соединенную со связанными полями {#unnest-an-array-of-strings-into-a-table-joined-with-associated-fields}

для контекста:  

```sql
describe google.cloudresourcemanager.projects_iam_policies;
```

возвращает...

```
|-----------|--------|                                                                                                                                                                                                     
|   name    |  type  |                                                                                                                                                                                                     
|-----------|--------|                                                                                                                                                                                                     
| condition | object |                                                                                                                                                                                                     
|-----------|--------|                                                                                                                                                                                                     
| members   | array  |                                                                                                                                                                                                     
|-----------|--------|                                                                                                                                                                                                     
| role      | string |                                                                                                                                                                                                     
|-----------|--------|  
```

Следующий запрос `stackql` использует функцию `json_each`, чтобы развернуть массив `members` в таблицу, соединенную с полем `role` и, при необходимости, с полем `condition`.  

```sql
select 
iam.role, 
SPLIT_PART(json_each.value, ':', 2) as member 
from google.cloudresourcemanager.projects_iam_policies iam, json_each(members) 
where projectsId = 'stackql';
```

вернет...

```
|--------------------------------------|-------------------------------------------------------|
|                 role                 |                        member                         |
|--------------------------------------|-------------------------------------------------------|
| roles/cloudbuild.builds.builder      | 1234567890@cloudbuild.gserviceaccount.com             |
|--------------------------------------|-------------------------------------------------------|
| roles/editor                         | 1234567890-compute@developer.gserviceaccount.com      |
|--------------------------------------|-------------------------------------------------------|
| roles/editor                         | 1234567890@cloudservices.gserviceaccount.com          |
|--------------------------------------|-------------------------------------------------------|
| roles/editor                         | stackql@appspot.gserviceaccount.com                   |
|--------------------------------------|-------------------------------------------------------|
| ...                                  | ...                                                   |
|--------------------------------------|-------------------------------------------------------|
```

Подробнее см. [https://www.sqlite.org/json1.html#jeach](https://www.sqlite.org/json1.html#jeach)
