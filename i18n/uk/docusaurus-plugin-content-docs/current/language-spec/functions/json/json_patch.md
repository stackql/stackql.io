---
title: JSON_PATCH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: Застосування JSON MergePatch до документа JSON за допомогою SQL у StackQL для змінення або злиття об'єктів JSON.
image: "/img/stackql-featured-image.png"
---
Застосовує JSON MergePatch (RFC-7396) до документа JSON і змінює його відповідно до вказаних операцій. Цю функцію використовують, щоб додавати, змінювати або видаляти елементи в об'єктах JSON, але масиви JSON вона обробляє як атомарні одиниці.

Див. також:  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract)

* * * 

## Синтаксис {#syntax}

```sql
SELECT JSON_PATCH(target_json, patch_json) FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*target_json*__  
Документ JSON, до якого потрібно застосувати патч.

__*patch_json*__  
Документ JSON з описом змін, які потрібно застосувати за алгоритмом MergePatch.

## Значення, що повертається {#return-values}
Повертає змінений документ JSON після застосування патча.

* * *

## Приклади {#examples}

### Простий MergePatch {#simple-mergepatch}

```sql
-- Example: Simple merge of new elements into the target JSON object
SELECT JSON_PATCH('{"a":1,"b":2}', '{"c":3,"d":4}') AS patched_json;
```

Вивід: `{"a":1,"b":2,"c":3,"d":4}`

### Заміна масиву значенням {#replace-an-array-with-a-value}

```sql
-- Example: Replacing an array with a single value in the JSON object
SELECT JSON_PATCH('{"a":[1,2],"b":2}', '{"a":9}') AS patched_json;
```

Вивід: `{"a":9,"b":2}`

### Видалення елемента за допомогою null {#remove-element-using-null}

```sql
-- Example: Removing an element by setting it to null
SELECT JSON_PATCH('{"a":1,"b":2}', '{"a":9,"b":null,"c":8}') AS patched_json;
```

Вивід: `{"a":9,"c":8}`

### Вкладений MergePatch {#nested-mergepatch}

```sql
-- Example: Nested merge patch updating sub-keys within a nested JSON object
SELECT JSON_PATCH('{"a":{"x":1,"y":2},"b":3}', '{"a":{"y":9},"c":8}') AS patched_json;
```

Вивід: `{"a":{"x":1,"y":9},"b":3,"c":8}`

Ця можливість дає змогу гнучко й динамічно оновлювати JSON, що зберігається в базі даних, і особливо корисна для застосунків, яким потрібні умовні зміни структур даних.
