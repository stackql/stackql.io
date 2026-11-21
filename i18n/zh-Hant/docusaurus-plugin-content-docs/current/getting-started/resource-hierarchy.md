---
title: 資源階層
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 資源階層：Provider、服務、資源與方法如何將雲端與 SaaS API 對應為可查詢的 SQL 資料表。"
image: "/img/stackql-featured-image.png"
---
import ImageSwitcher from '/js/ImageSwitcher/ImageSwitcher.js';

另請參閱：  
[[` SHOW `]](/language-spec/show) [[` DESCRIBE `]](/language-spec/describe)

* * * 

StackQL 中的物件以下列階層表示，下圖以 Google Cloud Provider 為例：

<ImageSwitcher 
lightImageSrc="/img/resource-heirarchy.png"
darkImageSrc="/img/resource-heirarchy-darkbg.png"
alttext="StackQL 資源階層"/>

### 使用 StackQL 物件表示法 {#using-the-stackql-object-notation}

在特定 Provider 的範圍內，此階層以物件表示法 `<provider>.<service>.<resource>` 表示，以下是在 [`SELECT`](/language-spec/select) 陳述式中使用此物件表示法的範例：  

```sql
-- Selecting all resources deployed within a service using a basic SELECT statement
SELECT * FROM google.compute.instances
WHERE project = 'stackql-demo'
AND zone = 'australia-southeast1-a';
```

:::note

如果資源或服務的名稱中包含句點（`.`），請使用反引號符號（__`__）括住資源名稱，例如：

```sql
SELECT * FROM google.container.`projects.zones.clusters.nodePools`;
```

:::

### 顯示 Provider 中的服務 {#showing-services-in-a-provider}

可以使用 [`SHOW SERVICES`](/language-spec/show) 命令列出 Provider 中的服務，如下所示：  

```sql
-- Returns all of the available services in a cloud provider
SHOW SERVICES IN google;
```  

### 顯示服務中的資源 {#showing-resources-within-a-service}

同樣地，可以使用 [`SHOW RESOURCES`](/language-spec/show) 列出服務中的資源，如下所示：

```sql
-- Returns all of the available resources in a cloud provider service
SHOW RESOURCES IN google.compute;
```  

### 顯示資源中的欄位 {#showing-fields-within-a-resource}

可以使用 [`DESCRIBE`](/language-spec/describe) 陳述式顯示資源中的欄位，如下所示：

```sql
-- Returns all of the available fields in a resource
DESCRIBE google.compute.instances;
```

### 顯示資源的可執行方法 {#showing-executable-methods-for-a-resource}

可以使用 [`SHOW METHODS`](/language-spec/show) 命令顯示資源可用的方法，如下所示：

```sql
-- Show all of the available methods for a resource
SHOW METHODS IN google.compute.instances;
```
