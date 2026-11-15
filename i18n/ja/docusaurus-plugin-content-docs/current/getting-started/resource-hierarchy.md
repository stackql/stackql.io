---
title: リソース階層
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL のリソース階層：プロバイダー、サービス、リソース、メソッドがクラウドと SaaS の API をクエリ可能な SQL テーブルに対応付ける仕組み。"
image: "/img/stackql-featured-image.png"
---
import ImageSwitcher from '/js/ImageSwitcher/ImageSwitcher.js';

関連項目：  
[[` SHOW `]](/language-spec/show) [[` DESCRIBE `]](/language-spec/describe)

* * * 

StackQL のオブジェクトは、次の階層で表されます。ここでは Google Cloud プロバイダーを例として示します。

<ImageSwitcher 
lightImageSrc="/img/resource-heirarchy.png"
darkImageSrc="/img/resource-heirarchy-darkbg.png"
alttext="StackQL のリソース階層"/>

### StackQL オブジェクト表記の使用 {#using-the-stackql-object-notation}

この階層は、特定のプロバイダーのコンテキストにおいて、`<provider>.<service>.<resource>` というオブジェクト表記で表されます。[`SELECT`](/language-spec/select) 文でこのオブジェクト表記を使用する例を次に示します。

```sql
-- Selecting all resources deployed within a service using a basic SELECT statement
SELECT * FROM google.compute.instances
WHERE project = 'stackql-demo'
AND zone = 'australia-southeast1-a';
```

```sql
-- Selecting all resources deployed within a service using a basic SELECT statement
SELECT * FROM google.compute.instances
WHERE project = 'stackql-demo'
AND zone = 'australia-southeast1-a';
```

:::note

リソースまたはサービスの名前にピリオド（`.`）が含まれる場合は、バッククォート記号（__`__）でリソース名を囲みます。例を次に示します。

```sql
SELECT * FROM google.container.`projects.zones.clusters.nodePools`;
```

:::

### プロバイダー内のサービスの表示 {#showing-services-in-a-provider}

プロバイダー内のサービスは、次のように [`SHOW SERVICES`](/language-spec/show) コマンドで一覧表示できます。

```sql
-- Returns all of the available services in a cloud provider
SHOW SERVICES IN google;
```  

### サービス内のリソースの表示 {#showing-resources-within-a-service}

同様に、サービス内のリソースは、次のように [`SHOW RESOURCES`](/language-spec/show) で一覧表示できます。

```sql
-- Returns all of the available resources in a cloud provider service
SHOW RESOURCES IN google.compute;
```  

### リソース内のフィールドの表示 {#showing-fields-within-a-resource}

リソース内のフィールドは、次のように [`DESCRIBE`](/language-spec/describe) 文で表示できます。

```sql
-- Returns all of the available fields in a resource
DESCRIBE google.compute.instances;
```

### リソースで実行可能なメソッドの表示 {#showing-executable-methods-for-a-resource}

リソースで使用できるメソッドは、次のように [`SHOW METHODS`](/language-spec/show) コマンドで表示できます。

```sql
-- Show all of the available methods for a resource
SHOW METHODS IN google.compute.instances;
```
