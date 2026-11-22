---
title: JSON_EXTRACT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 JSON_EXTRACT 函式：依路徑從 JSON 物件或陣列中擷取一或多個值，是攤平巢狀 API 回應欄位的主要工具。"
image: "/img/stackql-featured-image.png"
---
從格式正確的 JSON 物件或陣列中傳回一或多個值。  

另請參閱：  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_ARRAY_LENGTH `]](/language-spec/functions/json/json_array_length) [[ 資料型別 ]](/language-spec/data-types)

* * * 

:::tip 

使用 [**DESCRIBE**](/language-spec/describe) 陳述式找出可搭配 StackQL JSON 函式使用的 `array` 或 `object` 資料型別。使用 [**JSON_TYPE**](/language-spec/functions/json/json_type) 函式判斷巢狀物件或陣列元素的資料型別。

:::

:::note 

如果您搭配 `postgres` SQL 後端使用 stackql，`json_extract` 函式會實作為 `json_extract_path_text` 函式。路徑會以 `.` 分割，並捨棄開頭的 `$`，因此 `'$.a.b'` 會成為鍵 `'a'` 和 `'b'`；在該後端上，`$[0]` 這類陣列索引會被當作物件鍵傳遞，不會選取陣列元素。

:::


## 語法 {#syntax}

```sql
SELECT JSON_EXTRACT(json_array_or_object, json_path[, json_path1, .. json_pathN]) 
FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*json_array_or_object*__  
JSON 陣列或物件。

__*json_path*__  
指向 *json_array_or_object* 內所含之陣列或物件的 JSON 路徑。請參閱 [jsonpath.com](https://jsonpath.com/)。

> 如果只提供一個 *json_path*，則會傳回純量結果，其資料型別對應所解析路徑的資料型別。

> 如果提供多個 *json_path* 引數，結果會是由各路徑所解析之物件組成的陣列。

> 為 *json_path* 引數指定 `'$'` 會傳回 *json_array_or_object* 引數所代表之資料行對應的整個 JSON 物件。

> 注意：您可以使用 [`json-viewer`](https://aarrange.com/json-viewer) 之類的工具來開發和測試 JSON Path 運算式

## 傳回值 {#return-values}
字串、整數、物件、陣列、布林值或 null，視輸入的 *json_array_or_object* 與 *json_path* 而定。

* * *

## 範例 {#examples}

### 傳回與每個 Google Compute Engine 執行個體相關聯的磁碟物件 {#return-the-disk-objects-associated-with-each-google-compute-engine-instance}

```sql
SELECT name, json_extract(disks, '$') as disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### 傳回每個 Google Compute Engine 執行個體所附加之第一個磁碟的裝置名稱與大小 {#return-the-device-name-and-size-of-the-first-disk-attached-to-each-google-compute-engine-instance}

```sql
SELECT name, json_extract(disks, '$[0].deviceName') as device_name,
json_extract(disks, '$[0].diskSizeGb') as disk_size
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### 傳回每個 Google Compute Engine 執行個體所附加之第一個 NIC 的網路層級 {#return-the-network-tier-of-the-first-nic-attached-to-each-google-compute-engine-instance}

```sql
SELECT name, json_extract(networkInterfaces, '$[0].accessConfigs[0].networkTier') as network_tier
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### 傳回與每個 Google Compute Engine 執行個體相關聯之第一個服務帳戶的範圍清單 {#return-a-list-of-scopes-associated-with-the-first-service-account-associated-with-each-google-compute-engine-instance}

```sql
SELECT name, json_extract(serviceAccounts, '$[0].scopes') as scopes 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
/* returns a list for each service account */
```

如需詳細資訊，請參閱 [https://www.sqlite.org/json1.html#jex](https://www.sqlite.org/json1.html#jex)
