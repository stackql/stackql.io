---
title: JSON_PATCH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: Применение JSON MergePatch к JSON-документу с помощью SQL в StackQL для изменения или слияния JSON-объектов.
image: "/img/stackql-featured-image.png"
---
Применяет JSON MergePatch (RFC 7396) к JSON-документу и изменяет его в соответствии с заданными операциями. Эта функция используется, чтобы добавлять, изменять или удалять элементы JSON-объектов, но JSON-массивы она обрабатывает как неделимые единицы.

См. также:  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract)

* * * 

## Синтаксис {#syntax}

```sql
SELECT JSON_PATCH(target_json, patch_json) FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*target_json*__  
JSON-документ, к которому применяется патч.

__*patch_json*__  
JSON-документ с описанием изменений, которые нужно применить по алгоритму MergePatch.

## Возвращаемое значение {#return-values}
Возвращает измененный JSON-документ после применения патча.

* * *

## Примеры {#examples}

### Простой MergePatch {#simple-mergepatch}

```sql
-- Example: Simple merge of new elements into the target JSON object
SELECT JSON_PATCH('{"a":1,"b":2}', '{"c":3,"d":4}') AS patched_json;
```

Вывод: `{"a":1,"b":2,"c":3,"d":4}`

### Замена массива значением {#replace-an-array-with-a-value}

```sql
-- Example: Replacing an array with a single value in the JSON object
SELECT JSON_PATCH('{"a":[1,2],"b":2}', '{"a":9}') AS patched_json;
```

Вывод: `{"a":9,"b":2}`

### Удаление элемента с помощью null {#remove-element-using-null}

```sql
-- Example: Removing an element by setting it to null
SELECT JSON_PATCH('{"a":1,"b":2}', '{"a":9,"b":null,"c":8}') AS patched_json;
```

Вывод: `{"a":9,"c":8}`

### Вложенный MergePatch {#nested-mergepatch}

```sql
-- Example: Nested merge patch updating sub-keys within a nested JSON object
SELECT JSON_PATCH('{"a":{"x":1,"y":2},"b":3}', '{"a":{"y":9},"c":8}') AS patched_json;
```

Вывод: `{"a":{"x":1,"y":9},"b":3,"c":8}`

Эта возможность позволяет гибко и динамически обновлять JSON-данные, хранящиеся в базе данных, что особенно полезно для приложений, которым требуется условное изменение структур данных.
