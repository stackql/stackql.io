---
title: JSON_SET
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: Оновлення або вставлення значень у документі JSON за допомогою SQL у StackQL.
image: "/img/stackql-featured-image.png"
---
Оновлює наявні значення або вставляє нові значення в документ JSON за вказаними шляхами.

Див. також:  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[` JSON_INSERT `]](/language-spec/functions/json/json_insert) [[` JSON_REPLACE `]](/language-spec/functions/json/json_replace)

* * * 

## Синтаксис {#syntax}

```sql
SELECT JSON_SET(json_column, path, value_to_set) FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*json_column*__  
Стовпець або вираз JSON, який потрібно змінити.

__*path*__  
Вираз шляху JSON, що вказує, де встановити значення. Якщо шлях існує, значення оновлюється; якщо шляху не існує, додається нова пара "ключ-значення".

__*value_to_set*__  
Значення, яке потрібно встановити за вказаним шляхом у документі JSON.

## Значення, що повертається {#return-values}
Повертає документ JSON зі значенням, встановленим за вказаним шляхом. Якщо шлях існує, наявне значення оновлюється; інакше додається нова пара "ключ-значення".

:::note Поведінка, якщо ключ уже існує

| Функція                                                      | Перезаписати, якщо вже існує? | Створити, якщо не існує? |
|--------------------------------------------------------------|-------------------------------|--------------------------|
| [`json_insert()`](/language-spec/functions/json/json_insert) | Ні                            | Так                      |
| [`json_replace()`](/language-spec/functions/json/json_replace) | Так                           | Ні                       |
| [`json_set()`](/language-spec/functions/json/json_set)       | Так                           | Так                      |

:::

* * *

## Приклади {#examples}

### Оновлення наявного значення в об'єкті JSON {#update-an-existing-value-in-a-json-object}

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

Цей запит змінює значення ключа 'status' у документі JSON на 'active', якщо ключ 'status' існує. Якщо ключа 'status' у документі немає, його буде додано зі значенням 'active'.

### Додавання нової пари "ключ-значення" до об'єкта JSON {#add-a-new-key-value-pair-to-a-json-object}

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

Цей запит додає до документа JSON новий ключ 'lastChecked' зі значенням '2024-04-29', якщо ключа 'lastChecked' ще немає. Якщо він уже є, наявне значення буде оновлено до '2024-04-29'.
