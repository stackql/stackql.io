---
title: DELETE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "DELETE in StackQL: entfernt eine oder mehrere Instanzen einer Cloud- oder SaaS-Ressource per SQL-Anweisung mit den erforderlichen Parametern aus der WHERE-Klausel."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Löscht eine oder mehrere Instanzen einer Ressource. 

Siehe auch:  
[[ StackQL-Ressourcenhierarchie ]](/getting-started/resource-hierarchy)

* * * 

## Syntax {#syntax}

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

## Beispiele {#examples}

### Einfache `DELETE`-Anweisung {#basic-delete-statement}
Führen Sie eine einfache DELETE-Anweisung aus, um eine bereitgestellte Ressource in einer authentifizierten Sitzung zu entfernen.

```sql
-- Delete a Compute Engine resource instance by name
DELETE FROM google.compute.instances 
WHERE instance = 'demo-instance-1' 
AND project = 'stackql-demo' AND zone = 'australia-southeast1-a';
```
