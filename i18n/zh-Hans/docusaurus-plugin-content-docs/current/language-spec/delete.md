---
title: DELETE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 DELETE：使用 SQL 语句删除云或 SaaS 资源的一个或多个实例，所需参数取自 WHERE 子句。"
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

删除资源的一个或多个实例。 

另请参阅：  
[[ StackQL 资源层次结构 ]](/getting-started/resource-hierarchy)

* * * 

## 语法 {#syntax}

*deleteStatement::=*

<RailroadDiagram 
type="delete"
/>

&nbsp;  
&nbsp;  

```sql
DELETE FROM <multipartIdentifier>
WHERE <expression> ;
```

* * *

## 示例 {#examples}

### 基本 `DELETE` 语句 {#basic-delete-statement}
在已通过身份验证的会话中运行基本 DELETE 语句，删除已部署的资源。

```sql
-- Delete a Compute Engine resource instance by name
DELETE FROM google.compute.instances 
WHERE instance = 'demo-instance-1' 
AND project = 'stackql-demo' AND zone = 'australia-southeast1-a';
```
