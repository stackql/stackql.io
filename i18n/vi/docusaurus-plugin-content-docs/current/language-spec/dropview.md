---
title: DROP VIEW
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "DROP VIEW trong StackQL: xóa một dạng xem hoặc dạng xem vật chất hóa được tạo bằng CREATE VIEW."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Xóa một `view` hoặc `materialized view` trong một phiên.  

Xem thêm:  
 [[ `CREATE` ]](/language-spec/createview) [[ `REFRESH` ]](/language-spec/refreshview)

* * * 

## Cú pháp {#syntax}

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

## Ví dụ {#examples}

### Xóa dạng xem vật chất hóa {#drop-a-materialized-view}
```sql
DROP MATERIALIZED VIEW vw_ec2_instance_types;
```
