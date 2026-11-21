---
title: UPDATE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "UPDATE у StackQL: змінює вказані поля екземпляра ресурсу (еквівалент HTTP PATCH), а ціль визначає речення WHERE."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Оновлює вказані поля ресурсу (синонім операції `patch`).

Див. також:
[[ Ієрархія ресурсів StackQL ]](/getting-started/resource-hierarchy) [[ `REPLACE` ]](/language-spec/replace)

* * * 

## Синтаксис {#syntax}

*updateStatement::=*

<RailroadDiagram 
type="update"
/>

&nbsp;  
&nbsp;

```sql
UPDATE [ /*+ AWAIT  */ ] <multipartIdentifier>
SET field_name = value [, field_name = value ...]
[ WHERE condition ];
```

* * *

## Приклади {#examples}

### Проста інструкція `UPDATE` {#basic-update-statement}
Виконайте інструкцію `UPDATE`, щоб встановити мітки бакета Google Cloud Storage.

```sql
-- Set the labels on a Google Cloud Storage bucket
UPDATE google.storage.buckets
SET data__labels = '{"env": "prod", "team": "platform"}'
WHERE bucket = 'stackql-demo-bucket';
```

### Інструкція `UPDATE` (блокувальна) {#update-statement-blocking}
Виконайте інструкцію `UPDATE`, щоб змінити правило брандмауера Compute Engine. Це блокувальний (синхронний) виклик.

```sql
-- Synchronous update of a google firewall 
UPDATE /*+ AWAIT */ google.compute.firewalls
SET
 data__network = 'https://www.googleapis.com/compute/v1/projects/stackql-demo/global/networks/my-vpc',
 data__direction = 'INGRESS',
 data__sourceRanges = '["0.0.0.0/0"]',
 data__allowed = '[{"IPProtocol": "tcp", "ports": ["22"]}, {"IPProtocol": "tcp", "ports": ["6443"]},{"IPProtocol": "icmp"}]'
WHERE firewall = 'test-fw'
AND project = 'stackql-demo';
```

### Інструкція `UPDATE` з функціями JSON {#update-statement-with-json-functions}
Виконайте інструкцію `UPDATE`, значення якої сформовано за допомогою функцій JSON. Функція, яка не читає жодного стовпця, обчислюється до надсилання запиту до API. Значення в `SET` не може читати рядок, що оновлюється, тому, щоб змінити частину наявного значення, спершу прочитайте його за допомогою `SELECT`, а потім встановіть змінене значення.

```sql
-- Set the protocols and ports a firewall rule allows
UPDATE google.compute.firewalls
SET data__allowed = json_array(
  json_object('IPProtocol', 'tcp', 'ports', json_array('22', '443')),
  json_object('IPProtocol', 'icmp')
)
WHERE firewall = 'test-fw'
AND project = 'stackql-demo';
```
