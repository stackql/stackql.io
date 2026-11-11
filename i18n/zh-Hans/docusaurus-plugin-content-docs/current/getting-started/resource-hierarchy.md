---
title: 资源层次结构
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 资源层次结构：Provider、服务、资源和方法如何将云和 SaaS API 映射为可查询的 SQL 表。"
image: "/img/stackql-featured-image.png"
---
import ImageSwitcher from '/js/ImageSwitcher/ImageSwitcher.js';

另请参阅：  
[[` SHOW `]](/language-spec/show) [[` DESCRIBE `]](/language-spec/describe)

* * * 

StackQL 中的对象按以下层次结构表示，并以 Google Cloud Provider 为例进行展示：

<ImageSwitcher 
lightImageSrc="/img/resource-heirarchy.png"
darkImageSrc="/img/resource-heirarchy-darkbg.png"
alttext="StackQL 资源层次结构"/>

### 使用 StackQL 对象表示法 {#using-the-stackql-object-notation}

在给定 Provider 的上下文中，该层次结构用对象表示法 `<provider>.<service>.<resource>` 表示，下面是在 [`SELECT`](/language-spec/select) 语句中使用这种对象表示法的示例：  

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

如果资源或服务的名称中包含句点（`.`），请使用反引号（__`__）将资源名称括起来，例如：

```sql
SELECT * FROM google.container.`projects.zones.clusters.nodePools`;
```

:::

### 显示 Provider 中的服务 {#showing-services-in-a-provider}

可以使用 [`SHOW SERVICES`](/language-spec/show) 命令列出 Provider 中的服务，如下所示：  

```sql
-- Returns all of the available services in a cloud provider
SHOW SERVICES IN google;
```  

### 显示服务中的资源 {#showing-resources-within-a-service}

同样，可以使用 [`SHOW RESOURCES`](/language-spec/show) 列出服务中的资源，如下所示：

```sql
-- Returns all of the available resources in a cloud provider service
SHOW RESOURCES IN google.compute;
```  

### 显示资源中的字段 {#showing-fields-within-a-resource}

可以使用 [`DESCRIBE`](/language-spec/describe) 语句显示资源中的字段，如下所示：

```sql
-- Returns all of the available fields in a resource
DESCRIBE google.compute.instances;
```

### 显示资源的可执行方法 {#showing-executable-methods-for-a-resource}

可以使用 [`SHOW METHODS`](/language-spec/show) 命令显示资源可用的方法，如下所示：

```sql
-- Show all of the available methods for a resource
SHOW METHODS IN google.compute.instances;
```
