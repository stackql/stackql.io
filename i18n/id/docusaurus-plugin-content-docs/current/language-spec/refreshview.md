---
title: REFRESH VIEW
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "REFRESH VIEW di StackQL: membangun ulang hasil tersimpan dari tampilan terwujud berdasarkan kueri yang mendefinisikannya."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Menyegarkan data `materialized view` dalam suatu sesi.  

Lihat juga:  
[[ `CREATE` ]](/language-spec/createview) [[ `DROP` ]](/language-spec/dropview)

* * * 

## Sintaksis {#syntax}

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

## Contoh {#examples}

### Menyegarkan tampilan terwujud {#refresh-a-materialized-view}
```sql
REFRESH MATERIALIZED VIEW vw_ec2_instance_types;
```
