---
title: DROP VIEW
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "DROP VIEW في StackQL: إزالة طريقة عرض أو طريقة عرض مجسدة تم إنشاؤها باستخدام CREATE VIEW."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';
//import Gist from 'react-gist';

تحذف طريقة عرض (`view`) أو طريقة عرض مجسدة (`materialized view`) داخل جلسة.  

راجع أيضًا:  
 [[ `CREATE` ]](/language-spec/createview) [[ `REFRESH` ]](/language-spec/refreshview)

* * * 

## بناء الجملة {#syntax}

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

## أمثلة {#examples}

### حذف طريقة عرض مجسدة {#drop-a-materialized-view}
```sql
DROP MATERIALIZED VIEW vw_ec2_instance_types;
```
