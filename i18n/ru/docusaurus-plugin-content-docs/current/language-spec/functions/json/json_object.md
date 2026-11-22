---
title: JSON_OBJECT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Функция JSON_OBJECT в StackQL: построение JSON-объекта из ключей и значений, переданных в аргументах, например для формирования тел запросов для INSERT и UPDATE."
image: "/img/stackql-featured-image.png"
---
Возвращает объект, построенный из переданных аргументов.  

См. также:  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[ Типы данных ]](/language-spec/data-types)

* * * 

:::tip 

Используйте инструкцию [**DESCRIBE**](/language-spec/describe), чтобы найти типы данных `array` или `object`, которые можно использовать с JSON-функциями StackQL.

:::

:::note 

Если stackql работает с SQL-бэкендом `postgres`, используйте функцию `jsonb_build_object` вместо `json_object`: StackQL передает `json_object` в PostgreSQL без изменений, а одноименная функция PostgreSQL принимает ключи и значения в виде текстовых массивов.

:::

## Синтаксис {#syntax}

```sql
SELECT JSON_OBJECT(key1, value1, key2, value2, ...) 
FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*key1, key2, ...*__  
Метки JSON-объекта.

__*value1, value2, ...*__  
Значения, соответствующие меткам в JSON-объекте.

## Возвращаемое значение {#return-values}
Строковое значение, представляющее правильно сформированный JSON-объект.

* * *

## Примеры {#examples}

### Построение массива JSON-объектов из столбца {#build-an-array-of-json-objects-from-a-column}

```sql
SELECT JSON_GROUP_ARRAY(json_object('instance', selfLink)) as instance_links
FROM google.compute.instances 
WHERE project = 'stackql-k8s-the-hard-way-demo'
AND zone = 'australia-southeast1-a'
AND name like '%-dev-controller-%';
/* returns a data structure required for a targetPool in GCP */
```

Подробнее см. [https://sqlite.org/json1.html#jobj](https://sqlite.org/json1.html#jobj)
