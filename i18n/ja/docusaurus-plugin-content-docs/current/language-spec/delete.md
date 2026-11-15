---
title: DELETE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の DELETE：必須パラメーターを WHERE 句から受け取り、SQL 文でクラウドまたは SaaS リソースのインスタンスを削除します。"
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

リソースの 1 つ以上のインスタンスを削除します。

関連項目：  
[[ StackQL リソース階層 ]](/getting-started/resource-hierarchy)

* * * 

## 構文 {#syntax}

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

## 例 {#examples}

### 基本的な `DELETE` 文 {#basic-delete-statement}
認証済みのセッションで基本的な DELETE 文を実行し、デプロイ済みのリソースを削除します。

```sql
-- Delete a Compute Engine resource instance by name
DELETE FROM google.compute.instances 
WHERE instance = 'demo-instance-1' 
AND project = 'stackql-demo' AND zone = 'australia-southeast1-a';
```
