---
title: DELETE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "DELETE dans StackQL : supprimer une ou plusieurs instances d'une ressource cloud ou SaaS avec une instruction SQL, les paramètres obligatoires étant fournis par la clause WHERE."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Supprime une ou plusieurs instances d'une ressource. 

Voir aussi :  
[[ Hiérarchie des ressources StackQL ]](/getting-started/resource-hierarchy)

* * * 

## Syntaxe {#syntax}

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

## Exemples {#examples}

### Instruction `DELETE` de base {#basic-delete-statement}
Dans une session authentifiée, exécutez une instruction DELETE de base pour supprimer une ressource déployée.

```sql
-- Delete a Compute Engine resource instance by name
DELETE FROM google.compute.instances 
WHERE instance = 'demo-instance-1' 
AND project = 'stackql-demo' AND zone = 'australia-southeast1-a';
```
