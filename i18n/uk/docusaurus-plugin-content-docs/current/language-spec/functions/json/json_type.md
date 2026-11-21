---
title: JSON_TYPE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Функція JSON_TYPE у StackQL: тип (object, array, text, integer, real, true, false або null) зовнішнього елемента значення JSON або шляху в ньому."
image: "/img/stackql-featured-image.png"
---
Повертає тип даних зовнішнього елемента значення JSON або елемента за необов'язковим шляхом.  

Див. також:  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[ Типи даних ]](/language-spec/data-types)

* * * 

:::tip 

Скористайтеся інструкцією [**DESCRIBE**](/language-spec/describe), щоб знайти типи даних `array` або `object`, які можна використовувати з функціями JSON у StackQL.

:::

:::note 

Якщо ви використовуєте StackQL із бекендом SQL `postgres`, використовуйте натомість функцію `json_typeof`.

:::

## Синтаксис {#syntax}

```sql
SELECT JSON_TYPE(json_array_or_object [, json_path]) 
FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*json_array_or_object*__  
Масив або об'єкт JSON.

__*json_path*__  
Необов'язковий. Шлях JSON до масиву або об'єкта всередині *json_array_or_object*.  Див. [jsonpath.com](https://jsonpath.com/).

> Якщо *json_path* не існує в *json_array_or_object*, функція повертає `NULL`.

> Функція `json_type()` видає помилку, якщо хоча б один її аргумент сформовано неправильно.

## Значення, що повертається {#return-values}
Рядкове значення з назвою визначеного типу даних.

> Можливі типи даних: `null`, `true`, `false`, `integer`, `real`, `text`, `array` або `object`.

* * *

## Приклади {#examples}

### Отримання типу даних поля, вкладеного в об'єкт або масив JSON {#return-the-datatype-of-a-field-nested-within-a-json-object-or-array}

```sql
SELECT json_type(disks, '$[0].licenses[0]') as datatype
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
/* returns text */
```

Докладніше див. [https://www.sqlite.org/json1.html#jtype](https://www.sqlite.org/json1.html#jtype)
