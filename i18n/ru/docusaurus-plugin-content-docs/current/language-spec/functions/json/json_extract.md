---
title: JSON_EXTRACT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Функция JSON_EXTRACT в StackQL: извлечение значений из JSON-объекта или массива по пути, основной инструмент для преобразования вложенных полей ответов API в плоский вид."
image: "/img/stackql-featured-image.png"
---
Возвращает одно или несколько значений из правильно сформированного JSON-объекта или JSON-массива.  

См. также:  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_ARRAY_LENGTH `]](/language-spec/functions/json/json_array_length) [[ Типы данных ]](/language-spec/data-types)

* * * 

:::tip 

Используйте инструкцию [**DESCRIBE**](/language-spec/describe), чтобы найти типы данных `array` или `object`, которые можно использовать с JSON-функциями StackQL. Используйте функцию [**JSON_TYPE**](/language-spec/functions/json/json_type), чтобы определить типы данных вложенных объектов или элементов массивов.

:::

:::note 

Функция `json_extract` реализована как функция `json_extract_path_text`, если вы используете stackql с SQL-бэкендом `postgres`

:::


## Синтаксис {#syntax}

```sql
SELECT JSON_EXTRACT(json_array_or_object, json_path[, json_path1, .. json_pathN]) 
FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*json_array_or_object*__  
JSON-массив или JSON-объект.

__*json_path*__  
Путь JSON к массиву или объекту внутри *json_array_or_object*.  См. [jsonpath.com](https://jsonpath.com/).

> Если указан один аргумент *json_path*, возвращается скалярный результат, тип данных которого соответствует значению по этому пути.

> Если указано несколько аргументов *json_path*, результатом будет массив из объектов, найденных по соответствующим путям.

> Если указать `'$'` в качестве аргумента *json_path*, функция вернет весь JSON-объект, соответствующий столбцу, который задан аргументом *json_array_or_object*.

> ПРИМЕЧАНИЕ: для разработки и тестирования выражений пути JSON можно использовать такие инструменты, как [`json-viewer`](https://aarrange.com/json-viewer)

## Возвращаемое значение {#return-values}
Строковое значение, целое число, объект, массив, логическое значение или null в зависимости от переданных на вход *json_array_or_object* и *json_path*.

* * *

## Примеры {#examples}

### Получение объектов дисков, связанных с каждым экземпляром Google Compute Engine {#return-the-disk-objects-associated-with-each-google-compute-engine-instance}

```sql
SELECT name, json_extract(disks, '$') as disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Получение имени устройства и размера первого диска, подключенного к каждому экземпляру Google Compute Engine {#return-the-device-name-and-size-of-the-first-disk-attached-to-each-google-compute-engine-instance}

```sql
SELECT name, json_extract(disks, '$[0].deviceName') as device_name,
json_extract(disks, '$[0].diskSizeGb') as disk_size
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Получение уровня сетевого обслуживания первого сетевого интерфейса (NIC), подключенного к каждому экземпляру Google Compute Engine {#return-the-network-tier-of-the-first-nic-attached-to-each-google-compute-engine-instance}

```sql
SELECT name, json_extract(networkInterfaces, '$[0].accessConfigs[0].networkTier') as network_tier
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Получение списка областей доступа первого сервисного аккаунта, связанного с каждым экземпляром Google Compute Engine {#return-a-list-of-scopes-associated-with-the-first-service-account-associated-with-each-google-compute-engine-instance}

```sql
SELECT name, json_extract(serviceAccounts, '$[0].scopes') as scopes 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
/* returns a list for each service account */
```

Подробнее см. [https://www.sqlite.org/json1.html#jex](https://www.sqlite.org/json1.html#jex)
