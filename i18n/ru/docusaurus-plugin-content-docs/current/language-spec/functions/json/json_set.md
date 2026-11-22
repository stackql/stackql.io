---
title: JSON_SET
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: Обновление или вставка значений в JSON-документ с помощью SQL в StackQL.
image: "/img/stackql-featured-image.png"
---
Обновляет существующие значения или вставляет новые значения в JSON-документ по указанным путям.

См. также:  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[` JSON_INSERT `]](/language-spec/functions/json/json_insert) [[` JSON_REPLACE `]](/language-spec/functions/json/json_replace)

* * * 

## Синтаксис {#syntax}

```sql
SELECT JSON_SET(json_column, path, value_to_set) FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*json_column*__  
Изменяемый JSON-столбец или JSON-выражение.

__*path*__  
Выражение пути JSON, указывающее, где нужно установить значение. Если путь существует, значение обновляется; если путь не существует, добавляется новая пара "ключ-значение".

__*value_to_set*__  
Значение, которое нужно установить по указанному пути в JSON-документе.

## Возвращаемое значение {#return-values}
Возвращает JSON-документ с установленным по указанному пути значением. Если путь существует, существующее значение обновляется; в противном случае добавляется новая пара "ключ-значение".

:::note Поведение, если ключ существует

| Функция                                                      | Перезаписать, если уже существует? | Создать, если не существует? |
|--------------------------------------------------------------|------------------------------|---------------------------|
| [`json_insert()`](/language-spec/functions/json/json_insert) | Нет                          | Да                        |
| [`json_replace()`](/language-spec/functions/json/json_replace) | Да                           | Нет                       |
| [`json_set()`](/language-spec/functions/json/json_set)       | Да                           | Да                        |

:::

* * *

## Примеры {#examples}

### Обновление существующего значения в JSON-объекте {#update-an-existing-value-in-a-json-object}

```sql
-- Example: Update the 'status' key's value to 'active' in a JSON object
SELECT JSON_SET('{"name":"ServiceOne","status":"pending","region":"US"}', '$.status', 'active') as updated_json;

-- Expected output:
-- |---------------------------------------------------------|
-- |                    updated_json                         |
-- |---------------------------------------------------------|
-- | {"name":"ServiceOne","status":"active","region":"US"}   |
-- |---------------------------------------------------------|
```

Этот запрос устанавливает для ключа 'status' в JSON-документе значение 'active', если ключ 'status' существует. Если ключа 'status' в документе нет, он добавляется со значением 'active'.

### Добавление новой пары "ключ-значение" в JSON-объект {#add-a-new-key-value-pair-to-a-json-object}

```sql
-- Example: Add a new key 'lastChecked' with a date value to a JSON object
SELECT JSON_SET('{"name":"ServiceOne","status":"pending"}', '$.lastChecked', '2024-04-29') as updated_json;

/* expected output...
|-----------------------------------------------------------------------|
|                          updated_json                                 |
|-----------------------------------------------------------------------|
| {"name":"ServiceOne","status":"pending","lastChecked":"2024-04-29"}   |
|-----------------------------------------------------------------------|
*/
```

Этот запрос добавляет в JSON-документ новый ключ 'lastChecked' со значением '2024-04-29', если ключа 'lastChecked' еще нет. Если он есть, существующее значение заменяется значением '2024-04-29'.
