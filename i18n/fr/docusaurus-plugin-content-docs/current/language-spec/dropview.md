---
title: DROP VIEW
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "DROP VIEW dans StackQL : supprimer une vue ou une vue matérialisée créée avec CREATE VIEW."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';
//import Gist from 'react-gist';

Supprime une `view` ou une `materialized view` au sein d'une session.  

Voir aussi :  
 [[ `CREATE` ]](/language-spec/createview) [[ `REFRESH` ]](/language-spec/refreshview)

* * * 

## Syntaxe {#syntax}

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

## Exemples {#examples}

### Supprimer une vue matérialisée {#drop-a-materialized-view}
```sql
DROP MATERIALIZED VIEW vw_ec2_instance_types;
```
