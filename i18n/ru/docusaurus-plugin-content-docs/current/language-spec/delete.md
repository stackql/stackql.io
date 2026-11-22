---
title: DELETE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "DELETE в StackQL: удаление одного или нескольких экземпляров облачного ресурса или ресурса SaaS SQL-инструкцией с обязательными параметрами в предложении WHERE."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Удаляет один или несколько экземпляров ресурса. 

См. также:  
[[ Иерархия ресурсов StackQL ]](/getting-started/resource-hierarchy)

* * * 

## Синтаксис {#syntax}

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

## Примеры {#examples}

### Простая инструкция `DELETE` {#basic-delete-statement}
Выполните простую инструкцию DELETE, чтобы удалить развернутый ресурс в аутентифицированном сеансе.

```sql
-- Delete a Compute Engine resource instance by name
DELETE FROM google.compute.instances 
WHERE instance = 'demo-instance-1' 
AND project = 'stackql-demo' AND zone = 'australia-southeast1-a';
```
