---
title: REFRESH VIEW
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の REFRESH VIEW：マテリアライズドビューに保存された結果を、定義クエリから再構築します。"
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

セッション内の `materialized view` のデータをリフレッシュします。  

関連項目：  
[[ `CREATE` ]](/language-spec/createview) [[ `DROP` ]](/language-spec/dropview)

* * * 

## 構文 {#syntax}

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

## 例 {#examples}

### マテリアライズドビューをリフレッシュする {#refresh-a-materialized-view}
```sql
REFRESH MATERIALIZED VIEW vw_ec2_instance_types;
```
