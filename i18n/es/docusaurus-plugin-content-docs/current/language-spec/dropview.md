---
title: DROP VIEW
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "DROP VIEW en StackQL: eliminar una vista o una vista materializada creada con CREATE VIEW."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Elimina una `view` o una `materialized view` dentro de una sesión.  

Consulte también:  
 [[ `CREATE` ]](/language-spec/createview) [[ `REFRESH` ]](/language-spec/refreshview)

* * * 

## Sintaxis {#syntax}

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

## Ejemplos {#examples}

### Eliminar una vista materializada {#drop-a-materialized-view}
```sql
DROP MATERIALIZED VIEW vw_ec2_instance_types;
```
