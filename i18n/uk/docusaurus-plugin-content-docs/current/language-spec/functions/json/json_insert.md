---
title: JSON_INSERT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: Вставлення нових значень у документ JSON без перезапису наявних даних за допомогою SQL у StackQL.
image: "/img/stackql-featured-image.png"
---
Вставляє нові значення в документ JSON за вказаними шляхами без перезапису наявних значень.

Див. також:  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[` JSON_SET `]](/language-spec/functions/json/json_set) [[` JSON_REPLACE `]](/language-spec/functions/json/json_replace)

* * * 

## Синтаксис {#syntax}

```sql
SELECT JSON_INSERT(json_column, path, new_value) FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*json_column*__  
Стовпець або вираз JSON, який потрібно змінити.

__*path*__  
Вираз шляху JSON, за яким потрібно вставити нове значення. Якщо шлях уже існує, початкове значення за цим шляхом не замінюється.

__*new_value*__  
Значення, яке потрібно вставити за вказаним шляхом у документі JSON.

## Значення, що повертається {#return-values}
Повертає документ JSON зі вставленим новим значенням, якщо вказаного шляху ще немає; інакше функція повертає документ JSON без змін.

:::note Поведінка, якщо ключ уже існує

| Функція                                                      | Перезаписати, якщо вже існує? | Створити, якщо не існує? |
|--------------------------------------------------------------|-------------------------------|--------------------------|
| [`json_insert()`](/language-spec/functions/json/json_insert) | Ні                            | Так                      |
| [`json_replace()`](/language-spec/functions/json/json_replace) | Так                           | Ні                       |
| [`json_set()`](/language-spec/functions/json/json_set)       | Так                           | Так                      |

:::

* * *

## Приклади {#examples}

### Вставлення нової пари "ключ-значення" в об'єкт JSON {#insert-a-new-key-value-pair-into-a-json-object}

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

Цей запит вставляє в документ JSON новий ключ 'region' зі значенням 'Europe', якщо ключа 'region' ще немає. Якщо ключ 'region' уже є в документі, функція повертає початковий документ без змін.
