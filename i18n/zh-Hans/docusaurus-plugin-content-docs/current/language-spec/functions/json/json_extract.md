---
title: JSON_EXTRACT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 JSON_EXTRACT 函数：按路径从 JSON 对象或数组中提取一个或多个值，是展平嵌套 API 响应字段的主要工具。"
image: "/img/stackql-featured-image.png"
---
从格式正确的 JSON 对象或数组中返回一个或多个值。  

另请参阅：  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_ARRAY_LENGTH `]](/language-spec/functions/json/json_array_length) [[ 数据类型 ]](/language-spec/data-types)

* * * 

:::tip 

使用 [**DESCRIBE**](/language-spec/describe) 函数查找可用于 StackQL JSON 函数的 `array` 或 `object` 数据类型。使用 [**JSON_TYPE**](/language-spec/functions/json/json_type) 函数解析嵌套对象或数组元素的数据类型。

:::

:::note 

如果你将 stackql 与 `postgres` SQL 后端一起使用，`json_extract` 函数将实现为 `json_extract_path_text` 函数

:::


## 语法 {#syntax}

```sql
SELECT JSON_EXTRACT(json_array_or_object, json_path[, json_path1, .. json_pathN]) 
FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*json_array_or_object*__  
JSON 数组或对象。

__*json_path*__  
指向 *json_array_or_object* 内所含数组或对象的 JSON 路径。请参阅 [jsonpath.com](https://jsonpath.com/)。

> 如果只提供一个 *json_path*，则返回一个标量结果，其数据类型与解析出的路径相对应。

> 如果提供多个 *json_path* 参数，则结果是一个数组，由各个路径解析出的对象组成。

> 将 *json_path* 参数指定为 `'$'` 时，将返回 *json_array_or_object* 参数所表示的列对应的整个 JSON 对象。


## 返回值 {#return-values}
字符串、整数、对象、数组、布尔值或 null，取决于作为输入提供的 *json_array_or_object* 和 *json_path*。

* * *

## 示例 {#examples}

### 返回与每个 Google Compute Engine 实例关联的磁盘对象 {#return-the-disk-objects-associated-with-each-google-compute-engine-instance}

```sql
SELECT name, json_extract(disks, '$') as disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### 返回附加到每个 Google Compute Engine 实例的第一个磁盘的设备名称和大小 {#return-the-device-name-and-size-of-the-first-disk-attached-to-each-google-compute-engine-instance}

```sql
SELECT name, json_extract(disks, '$[0].deviceName') as device_name,
json_extract(disks, '$[0].diskSizeGb') as disk_size
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### 返回附加到每个 Google Compute Engine 实例的第一个 NIC 的网络层级 {#return-the-network-tier-of-first-nic-attached-to-each-google-compute-engine-instances}

```sql
SELECT name, json_extract(networkInterfaces, '$[0].accessConfigs[0].networkTier') as network_tier
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### 返回与每个 Google Compute Engine 实例关联的第一个服务账号所关联的作用域列表 {#return-a-list-of-scopes-associated-with-the-first-service-account-associated-with-each-google-compute-engine-instance}

```sql
SELECT name, json_extract(serviceAccounts, '$[0].scopes') as scopes 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
/* returns a list for each service account */
```

有关更多信息，请参阅 [https://www.sqlite.org/json1.html#jex](https://www.sqlite.org/json1.html#jex)
