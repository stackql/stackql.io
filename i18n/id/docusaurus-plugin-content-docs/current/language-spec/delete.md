---
title: DELETE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "DELETE di StackQL: menghapus satu atau beberapa instance sumber daya cloud atau SaaS dengan pernyataan SQL, dengan parameter wajib diambil dari klausa WHERE."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Menghapus satu atau beberapa instance sumber daya.

Lihat juga:  
[[ Hierarki sumber daya StackQL ]](/getting-started/resource-hierarchy)

* * * 

## Sintaksis {#syntax}

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

## Contoh {#examples}

### Pernyataan `DELETE` dasar {#basic-delete-statement}
Jalankan pernyataan DELETE dasar untuk menghapus sumber daya yang telah di-deploy dalam sesi yang terautentikasi.

```sql
-- Delete a Compute Engine resource instance by name
DELETE FROM google.compute.instances 
WHERE instance = 'demo-instance-1' 
AND project = 'stackql-demo' AND zone = 'australia-southeast1-a';
```
