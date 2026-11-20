---
title: JSON_ARRAY_LENGTH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 JSON_ARRAY_LENGTH 函式：JSON 陣列的元素數目，陣列可位於最上層或 JSON 值內的路徑。"
image: "/img/stackql-featured-image.png"
---
傳回 JSON 陣列中的元素數目。  

另請參閱：  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[ 資料型別 ]](/language-spec/data-types)

* * * 

:::tip 

使用 [**DESCRIBE**](/language-spec/describe) 陳述式找出可搭配 StackQL JSON 函式使用的 `array` 或 `object` 資料型別。使用 [**JSON_TYPE**](/language-spec/functions/json/json_type) 函式判斷巢狀物件或陣列元素的資料型別。

:::

## 語法 {#syntax}

```sql
SELECT JSON_ARRAY_LENGTH(json_array_or_object [, json_path]) FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*json_array_or_object*__  
JSON 陣列或物件。

> 如果 *json_array_or_object* 是 JSON 物件（而非陣列）且未指定 *json_path*，則傳回 `0`。

__*json_path*__  
選用的 JSON 路徑，指向物件內所含的陣列。請參閱 [jsonpath.com](https://jsonpath.com/)。

> 如果提供了 *json_path*，`json_array_length` 函式會在 *json_array_or_object* 中找出該路徑並傳回陣列的長度；如果 *json_path* 是物件（而非陣列），則傳回 `0`。如果在 *json_array_or_object* 中找不到 *json_path*，則傳回 `NULL`。

> 如果 *json_array_or_object* 不是格式正確的 JSON，或 *json_path* 不是格式正確的 JSON 路徑，則會擲回錯誤。

## 傳回值 {#return-values}
傳回整數。

* * *

## 範例 {#examples}

### 傳回附加至 Google Compute Engine 執行個體的磁碟數目 {#return-the-number-of-disks-attached-to-google-compute-engine-instances}

```sql
SELECT name, json_array_length(disks) as num_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### 傳回附加至 Google Compute Engine 執行個體的網路介面數目 {#return-the-number-of-network-interfaces-attached-to-google-compute-engine-instances}

```sql
SELECT name, json_array_length(networkInterfaces) as num_nics
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### 傳回與 Google Storage 儲存貯體相關聯的 ACL 數目 {#return-the-number-of-acls-associated-with-google-storage-buckets}

```sql
SELECT name, json_array_length(acl) as num_acls
FROM google.storage.buckets 
WHERE project = 'stackql';
```

### 傳回與服務帳戶相關聯的範圍數目 {#return-the-number-of-scopes-associated-with-a-service-account}

```sql
SELECT name, json_array_length(serviceAccounts, '$[0].scopes') as num_scopes 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

如需詳細資訊，請參閱 [https://www.sqlite.org/json1.html#jarraylen](https://www.sqlite.org/json1.html#jarraylen)
