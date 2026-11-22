---
title: REFRESH VIEW
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "REFRESH VIEW у StackQL: перебудова збережених результатів матеріалізованого подання із запиту, який його визначає."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Оновлює дані в поданні `materialized view` у межах сеансу.  

Див. також:  
[[ `CREATE` ]](/language-spec/createview) [[ `DROP` ]](/language-spec/dropview)

* * * 

## Синтаксис {#syntax}

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

## Приклади {#examples}

### Оновлення матеріалізованого подання {#refresh-a-materialized-view}
```sql
REFRESH MATERIALIZED VIEW vw_ec2_instance_types;
```
