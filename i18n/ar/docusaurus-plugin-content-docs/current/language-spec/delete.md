---
title: DELETE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "DELETE في StackQL: إزالة مثيل واحد أو أكثر من مورد سحابي أو مورد SaaS بجملة SQL، مع أخذ المعلمات المطلوبة من عبارة WHERE."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

تحذف مثيلًا واحدًا أو أكثر من مورد. 

راجع أيضًا:  
[[ التسلسل الهرمي لموارد StackQL ]](/getting-started/resource-hierarchy)

* * * 

## بناء الجملة {#syntax}

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

## أمثلة {#examples}

### جملة `DELETE` أساسية {#basic-delete-statement}
شغّل جملة DELETE أساسية لإزالة مورد منشور في جلسة مصادق عليها.

```sql
-- Delete a Compute Engine resource instance by name
DELETE FROM google.compute.instances 
WHERE instance = 'demo-instance-1' 
AND project = 'stackql-demo' AND zone = 'australia-southeast1-a';
```
