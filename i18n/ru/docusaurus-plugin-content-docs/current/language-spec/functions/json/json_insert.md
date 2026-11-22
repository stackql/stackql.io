---
title: JSON_INSERT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: Вставка новых значений в JSON-документ без перезаписи существующих данных с помощью SQL в StackQL.
image: "/img/stackql-featured-image.png"
---
Вставляет новые значения в JSON-документ по указанным путям, не перезаписывая существующие значения.

См. также:  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[` JSON_SET `]](/language-spec/functions/json/json_set) [[` JSON_REPLACE `]](/language-spec/functions/json/json_replace)

* * * 

## Синтаксис {#syntax}

```sql
SELECT JSON_INSERT(json_column, path, new_value) FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*json_column*__  
Изменяемый JSON-столбец или JSON-выражение.

__*path*__  
Выражение пути JSON, указывающее, куда нужно вставить новое значение. Если путь уже существует, исходное значение по этому пути не заменяется.

__*new_value*__  
Значение, которое нужно вставить по указанному пути в JSON-документе.

## Возвращаемое значение {#return-values}
Возвращает JSON-документ со вставленным новым значением, если указанный путь еще не существует; в противном случае JSON-документ возвращается без изменений.

:::note Поведение, если ключ существует

| Функция                                                      | Перезаписать, если уже существует? | Создать, если не существует? |
|--------------------------------------------------------------|------------------------------|---------------------------|
| [`json_insert()`](/language-spec/functions/json/json_insert) | Нет                          | Да                        |
| [`json_replace()`](/language-spec/functions/json/json_replace) | Да                           | Нет                       |
| [`json_set()`](/language-spec/functions/json/json_set)       | Да                           | Да                        |

:::

* * *

## Примеры {#examples}

### Вставка новой пары "ключ-значение" в JSON-объект {#insert-a-new-key-value-pair-into-a-json-object}

```sql
-- Example: Insert a new 'region' key with the value 'Europe' into a JSON object
SELECT JSON_INSERT('{"name":"ResourceOne","type":"Storage"}', '$.region', 'Europe') as updated_object;

/* expected output...
|-----------------------------------------------------------|
|                      updated_object                       |
|-----------------------------------------------------------|
| {"name":"ResourceOne","type":"Storage","region":"Europe"} |
|-----------------------------------------------------------|
*/
```

Этот запрос вставляет в JSON-документ новый ключ 'region' со значением 'Europe', если ключа 'region' еще нет. Если ключ 'region' уже есть в документе, исходный документ возвращается без изменений.
