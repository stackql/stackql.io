---
title: JSON_EACH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Табличнозначна функція JSON_EACH у StackQL: розгортає масив або об'єкт JSON у рядки, один на кожен елемент або член, щоб з'єднувати й фільтрувати вкладені відповіді API."
image: "/img/stackql-featured-image.png"
---
Табличнозначна функція, що повертає таблицю з одним рядком для кожного елемента масиву або члена об'єкта.

Див. також:  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[` JSON_ARRAY_LENGTH `]](/language-spec/functions/json/json_array_length) [[ Типи даних ]](/language-spec/data-types)

* * * 

:::tip 

Скористайтеся інструкцією [**DESCRIBE**](/language-spec/describe), щоб знайти типи даних `array`, які можна використовувати з функцією `json_each`.

:::

:::note 

Функцію `json_each` реалізовано як функцію `json_array_elements_text`, якщо ви використовуєте stackql з бекендом SQL `postgres`

:::

## Синтаксис {#syntax}

```sql
SELECT <alias>.<projectedField>, ... , json_each.value  
FROM <multipartIdentifier> <alias>, JSON_EACH(json_array_or_object [, json_path]);
```
## Аргументи {#arguments}

__*json_array_or_object*__  
Масив або об'єкт JSON.

__*json_path*__  
Необов'язковий. Шлях JSON до масиву або об'єкта, що міститься в *json_array_or_object*.  Див. [jsonpath.com](https://jsonpath.com/).

> Якщо *json_path* не існує в *json_array_or_object*, функція не повертає жодного рядка.

> Функція `json_each()` видає помилку, якщо *json_array_or_object* не є правильно сформованим JSON.

## Значення, що повертається {#return-values}
Таблиця з одним рядком для кожного елемента масиву або члена об'єкта.

* * *

## Приклади {#examples}

### Розгортання масиву рядкових значень у таблицю, з'єднану з пов'язаними полями {#unnest-an-array-of-strings-into-a-table-joined-with-associated-fields}

для контексту:  

```sql
describe google.cloudresourcemanager.projects_iam_policies;
```

повертає...

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

Наведений нижче запит `stackql` використовує функцію `json_each`, щоб розгорнути масив `members` у таблицю, з'єднану з полем `role` і, за потреби, з полем `condition`.  

```sql
select 
iam.role, 
SPLIT_PART(json_each.value, ':', 2) as member 
from google.cloudresourcemanager.projects_iam_policies iam, json_each(members) 
where projectsId = 'stackql';
```

поверне...

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

Докладніше див. [https://www.sqlite.org/json1.html#jeach](https://www.sqlite.org/json1.html#jeach)
