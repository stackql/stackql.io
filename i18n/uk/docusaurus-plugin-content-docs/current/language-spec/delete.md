---
title: DELETE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "DELETE у StackQL: видалення одного або кількох екземплярів хмарного ресурсу чи ресурсу SaaS інструкцією SQL з обов'язковими параметрами в реченні WHERE."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Видаляє один або кілька екземплярів ресурсу. 

Див. також:  
[[ Ієрархія ресурсів StackQL ]](/getting-started/resource-hierarchy)

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

## Приклади {#examples}

### Базова інструкція `DELETE` {#basic-delete-statement}
Виконайте базову інструкцію DELETE, щоб видалити розгорнутий ресурс в автентифікованому сеансі.

```sql
-- Delete a Compute Engine resource instance by name
DELETE FROM google.compute.instances 
WHERE instance = 'demo-instance-1' 
AND project = 'stackql-demo' AND zone = 'australia-southeast1-a';
```
