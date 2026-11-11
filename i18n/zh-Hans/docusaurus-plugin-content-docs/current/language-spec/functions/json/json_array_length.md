---
title: JSON_ARRAY_LENGTH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 JSON_ARRAY_LENGTH 函数：返回 JSON 数组在顶层或 JSON 值内某个路径处的元素数量。"
image: "/img/stackql-featured-image.png"
---
返回 JSON 数组中的元素数量。  

另请参阅：  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[ 数据类型 ]](/language-spec/data-types)

* * * 

:::tip 

使用 [**DESCRIBE**](/language-spec/describe) 函数查找可用于 StackQL JSON 函数的 `array` 或 `object` 数据类型。使用 [**JSON_TYPE**](/language-spec/functions/json/json_type) 函数解析嵌套对象或数组元素的数据类型。

:::

## 语法 {#syntax}

```sql
SELECT JSON_ARRAY_LENGTH(json_array_or_object [, json_path]) FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*json_array_or_object*__  
JSON 数组或对象。

> 如果 *json_array_or_object* 是 JSON 对象（而非数组）且未指定 *json_path*，则返回 `0`。

__*json_path*__  
指向对象内所含数组的可选 JSON 路径。请参阅 [jsonpath.com](https://jsonpath.com/)。

> 如果提供了 *json_path*，`json_array_length` 函数会在 *json_array_or_object* 中定位该路径并返回数组的长度；如果 *json_path* 是对象（而非数组），则返回 `0`。如果在 *json_array_or_object* 中无法定位 *json_path*，则返回 `NULL`。

> 如果 *json_array_or_object* 或 *json_path* 不是 JSON 对象或格式不正确，则会抛出错误。

## 返回值 {#return-values}
返回一个整数。

* * *

## 示例 {#examples}

### 返回附加到 Google Compute Engine 实例的磁盘数量 {#return-the-number-of-disks-attached-to-google-compute-engine-instances}

```sql
SELECT name, json_array_length(disks) as num_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### 返回附加到 Google Compute Engine 实例的网络接口数量 {#return-the-number-of-network-interfaces-attached-to-google-compute-engine-instances}

```sql
SELECT name, json_array_length(networkInterfaces) as num_nics
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### 返回与 Google Storage 存储桶关联的 ACL 数量 {#return-the-number-of-acls-associated-with-google-storage-buckets}

```sql
SELECT name, json_array_length(acl) as num_acls
FROM google.storage.buckets 
WHERE project = 'stackql';
```

### 返回与服务账号关联的作用域数量 {#return-the-number-of-scopes-associated-with-a-service-account}

```sql
SELECT name, json_array_length(serviceAccounts, '$[0].scopes') as num_scopes 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

有关更多信息，请参阅 [https://www.sqlite.org/json1.html#jarraylen](https://www.sqlite.org/json1.html#jarraylen)
