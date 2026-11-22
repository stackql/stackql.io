---
title: REFRESH VIEW
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "REFRESH VIEW في StackQL: إعادة بناء النتائج المخزنة لطريقة عرض مجسدة من الاستعلام الذي يحددها."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

تعمل على تحديث البيانات في طريقة عرض مجسدة (`materialized view`) ضمن جلسة.  

راجع أيضًا:  
[[ `CREATE` ]](/language-spec/createview) [[ `DROP` ]](/language-spec/dropview)

* * * 

## بناء الجملة {#syntax}

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

## أمثلة {#examples}

### تحديث طريقة عرض مجسدة {#refresh-a-materialized-view}
```sql
REFRESH MATERIALIZED VIEW vw_ec2_instance_types;
```
