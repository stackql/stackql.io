---
title: REFRESH VIEW
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 REFRESH VIEW：根据定义查询重建物化视图存储的结果。"
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';
//import Gist from 'react-gist';

在会话中刷新 `materialized view` 中的数据。  

另请参阅：  
[[ `CREATE` ]](/language-spec/createview) [[ `DROP` ]](/language-spec/dropview)

* * * 

## 语法 {#syntax}

*refreshViewStatement::=*

<RailroadDiagram 
type="refreshview"
/>

&nbsp;  
&nbsp;  

```sql
REFRESH MATERIALIZED VIEW <viewName>;
```

* * *

## 示例 {#examples}

### 刷新物化视图 {#refresh-a-materialized-view}
```sql
REFRESH MATERIALIZED VIEW vw_ec2_instance_types;
```
