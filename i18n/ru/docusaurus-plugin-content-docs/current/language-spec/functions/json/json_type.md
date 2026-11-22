---
title: JSON_TYPE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Функция JSON_TYPE в StackQL: тип (object, array, text, integer, real, true, false или null) самого внешнего элемента JSON-значения или элемента по пути внутри него."
image: "/img/stackql-featured-image.png"
---
Возвращает тип данных самого внешнего элемента JSON-значения или элемента по необязательному пути.  

См. также:  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[ Типы данных ]](/language-spec/data-types)

* * * 

:::tip 

Используйте инструкцию [**DESCRIBE**](/language-spec/describe), чтобы найти типы данных `array` или `object`, которые можно использовать с JSON-функциями StackQL.

:::

:::note 

Если StackQL работает с SQL-бэкендом `postgres`, используйте вместо нее функцию `json_typeof`.

:::

## Синтаксис {#syntax}

```sql
SELECT JSON_TYPE(json_array_or_object [, json_path]) 
FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*json_array_or_object*__  
JSON-массив или JSON-объект.

__*json_path*__  
Необязательный. Путь JSON к массиву или объекту внутри *json_array_or_object*.  См. [jsonpath.com](https://jsonpath.com/).

> Если *json_path* не существует в *json_array_or_object*, функция возвращает `NULL`.

> Функция `json_type()` выдает ошибку, если какой-либо из ее аргументов сформирован неправильно.

## Возвращаемое значение {#return-values}
Строковое значение, обозначающее найденный тип данных.

> Возможные типы данных: `null`, `true`, `false`, `integer`, `real`, `text`, `array` или `object`.

* * *

## Примеры {#examples}

### Получение типа данных поля, вложенного в JSON-объект или JSON-массив {#return-the-datatype-of-a-field-nested-within-a-json-object-or-array}

```sql
SELECT json_type(disks, '$[0].licenses[0]') as datatype
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
/* returns text */
```

Подробнее см. [https://www.sqlite.org/json1.html#jtype](https://www.sqlite.org/json1.html#jtype)
