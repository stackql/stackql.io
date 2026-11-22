---
title: REFRESH VIEW
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "REFRESH VIEW в StackQL: повторное построение сохраненных результатов материализованного представления на основе определяющего его запроса."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';
//import Gist from 'react-gist';

Обновляет данные материализованного представления (`materialized view`) в рамках сеанса.  

См. также:  
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

## Примеры {#examples}

### Обновление материализованного представления {#refresh-a-materialized-view}
```sql
REFRESH MATERIALIZED VIEW vw_ec2_instance_types;
```
