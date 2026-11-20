---
title: DROP VIEW
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 DROP VIEW：移除以 CREATE VIEW 建立的檢視或具體化檢視。"
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';
//import Gist from 'react-gist';

在工作階段中卸除 `view` 或 `materialized view`。  

另請參閱：  
 [[ `CREATE` ]](/language-spec/createview) [[ `REFRESH` ]](/language-spec/refreshview)

* * * 

## 語法 {#syntax}

*dropViewStatement::=*

<RailroadDiagram 
type="dropview"
/>

&nbsp;  
&nbsp;  

```sql
DROP [ MATERIALIZED ] VIEW <viewName>;
```

* * *

## 範例 {#examples}

### 卸除具體化檢視 {#drop-a-materialized-view}
```sql
DROP MATERIALIZED VIEW vw_ec2_instance_types;
```
