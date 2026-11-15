---
title: DELETE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "DELETE no StackQL: remover uma ou mais instâncias de um recurso na nuvem ou de SaaS com uma instrução SQL, obtendo os parâmetros obrigatórios da cláusula WHERE."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Exclui uma ou mais instâncias de um recurso.

Veja também:  
[[ Hierarquia de recursos do StackQL ]](/getting-started/resource-hierarchy)

* * * 

## Sintaxe {#syntax}

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

## Exemplos {#examples}

### Instrução `DELETE` básica {#basic-delete-statement}
Execute uma instrução DELETE básica para remover um recurso implantado em uma sessão autenticada.

```sql
-- Delete a Compute Engine resource instance by name
DELETE FROM google.compute.instances 
WHERE instance = 'demo-instance-1' 
AND project = 'stackql-demo' AND zone = 'australia-southeast1-a';
```
