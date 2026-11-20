---
title: DELETE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 DELETE：以 SQL 陳述式移除雲端或 SaaS 資源的一個或多個執行個體，必要參數取自 WHERE 子句。"
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

刪除資源的一個或多個執行個體。 

另請參閱：  
[[ StackQL 資源階層 ]](/getting-started/resource-hierarchy)

* * * 

## 語法 {#syntax}

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

## 範例 {#examples}

### 基本 `DELETE` 陳述式 {#basic-delete-statement}
在已驗證的工作階段中執行基本 DELETE 陳述式，以移除已部署的資源。

```sql
-- Delete a Compute Engine resource instance by name
DELETE FROM google.compute.instances 
WHERE instance = 'demo-instance-1' 
AND project = 'stackql-demo' AND zone = 'australia-southeast1-a';
```
