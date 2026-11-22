---
title: JSON_ARRAY_LENGTH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Функция JSON_ARRAY_LENGTH в StackQL: число элементов JSON-массива на верхнем уровне или по указанному пути внутри JSON-значения."
image: "/img/stackql-featured-image.png"
---
Возвращает число элементов JSON-массива.  

См. также:  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[ Типы данных ]](/language-spec/data-types)

* * * 

:::tip 

Используйте инструкцию [**DESCRIBE**](/language-spec/describe), чтобы найти типы данных `array` или `object`, которые можно использовать с JSON-функциями StackQL. Используйте функцию [**JSON_TYPE**](/language-spec/functions/json/json_type), чтобы определить типы данных вложенных объектов или элементов массивов.

:::

## Синтаксис {#syntax}

```sql
SELECT JSON_ARRAY_LENGTH(json_array_or_object [, json_path]) FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*json_array_or_object*__  
JSON-массив или JSON-объект.

> Если *json_array_or_object* - JSON-объект (а не массив) и *json_path* не указан, возвращается `0`.

__*json_path*__  
Необязательный путь JSON к массиву внутри объекта.  См. [jsonpath.com](https://jsonpath.com/).

> Если указан *json_path*, функция `json_array_length` находит этот путь в *json_array_or_object* и возвращает длину массива или `0`, если *json_path* указывает на объект (а не массив).  Если путь *json_path* не найден в *json_array_or_object*, возвращается `NULL`.

> Если *json_array_or_object* - неправильно сформированный JSON или *json_path* - неправильно сформированный путь JSON, выдается ошибка.

## Возвращаемое значение {#return-values}
Возвращает целое число.

* * *

## Примеры {#examples}

### Получение числа дисков, подключенных к экземплярам Google Compute Engine {#return-the-number-of-disks-attached-to-google-compute-engine-instances}

```sql
SELECT name, json_array_length(disks) as num_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Получение числа сетевых интерфейсов, подключенных к экземплярам Google Compute Engine {#return-the-number-of-network-interfaces-attached-to-google-compute-engine-instances}

```sql
SELECT name, json_array_length(networkInterfaces) as num_nics
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Получение числа ACL, связанных с бакетами Google Storage {#return-the-number-of-acls-associated-with-google-storage-buckets}

```sql
SELECT name, json_array_length(acl) as num_acls
FROM google.storage.buckets 
WHERE project = 'stackql';
```

### Получение числа областей, связанных с сервисным аккаунтом {#return-the-number-of-scopes-associated-with-a-service-account}

```sql
SELECT name, json_array_length(serviceAccounts, '$[0].scopes') as num_scopes 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Подробнее см. [https://www.sqlite.org/json1.html#jarraylen](https://www.sqlite.org/json1.html#jarraylen)
