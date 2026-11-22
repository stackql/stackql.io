---
title: JSON_REMOVE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: Удаление элементов из JSON-документа по указанным путям с помощью SQL в StackQL.
image: "/img/stackql-featured-image.png"
---
Удаляет указанные элементы из JSON-документа с помощью выражений пути. Если указанный путь не существует в документе, он игнорируется. Пути обрабатываются последовательно, поэтому каждое удаление может изменить структуру документа для последующих удалений.

См. также:  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract)

* * * 

## Синтаксис {#syntax}

```sql
SELECT JSON_REMOVE(json_document, path1, path2, ...) FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*json_document*__  
JSON-документ, из которого нужно удалить элементы.

__*path1, path2, ...*__  
Одно или несколько выражений пути, указывающих на удаляемые элементы JSON-документа.

## Возвращаемое значение {#return-values}
Возвращает копию JSON-документа без указанных элементов. Если пути не указаны, возвращает входной JSON, переформатированный без лишних пробельных символов.

* * *

## Примеры {#examples}

### Удаление элемента из JSON-массива {#remove-an-element-from-a-json-array}

```sql
-- Example: Remove the third element from a JSON array
SELECT JSON_REMOVE('[0,1,2,3,4]', '$[2]') AS modified_json;
```

Вывод: `'[0,1,3,4]'`

### Удаление нескольких элементов из JSON-массива {#remove-multiple-elements-from-a-json-array}

```sql
-- Example: Remove the first and third elements from a JSON array
SELECT JSON_REMOVE('[0,1,2,3,4]', '$[2]', '$[0]') AS modified_json;
```

Вывод: `'[1,3,4]'`

### Удаление последнего и первого элементов с помощью отрицательного индекса {#remove-last-element-using-negative-index}

```sql
-- Example: Remove the last and first elements from a JSON array
SELECT JSON_REMOVE('[0,1,2,3,4]', '$[#-1]', '$[0]') AS modified_json;
```

Вывод: `'[1,2,3]'`

### Удаление несуществующего пути {#remove-non-existent-path}

```sql
-- Example: Attempt to remove a non-existent key from a JSON object
SELECT JSON_REMOVE('{"x":25,"y":42}', '$.z') AS modified_json;
```

Вывод: `'{"x":25,"y":42}'`

### Удаление существующего ключа из JSON-объекта {#remove-existing-key-from-json-object}

```sql
-- Example: Remove the 'y' key from a JSON object
SELECT JSON_REMOVE('{"x":25,"y":42}', '$.y') AS modified_json;
```

Вывод: `'{"x":25}'`

### Граничный случай: удаление по корневому пути {#edge-case-removal-with-root-path}

```sql
-- Example: Attempt to remove using the root path
SELECT JSON_REMOVE('{"x":25,"y":42}', '$') AS modified_json;
```

Вывод: `NULL`

Эти примеры показывают, как с помощью функции `json_remove()` динамически изменять JSON-данные в разных сценариях: от простого удаления до обработки граничных случаев при изменении структур данных.
