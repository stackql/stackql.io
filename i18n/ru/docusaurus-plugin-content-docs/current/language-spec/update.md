---
title: UPDATE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "UPDATE в StackQL: изменение указанных полей экземпляра ресурса, эквивалентное HTTP PATCH; целевой экземпляр определяется предложением WHERE."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Обновляет указанные поля ресурса (синоним операции `patch`).

См. также:  
[[ Иерархия ресурсов StackQL ]](/getting-started/resource-hierarchy) [[ `REPLACE` ]](/language-spec/replace)

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

## Примеры {#examples}

### Простая инструкция `UPDATE` {#basic-update-statement}
Выполните инструкцию `UPDATE`, чтобы задать метки бакета Google Cloud Storage.

```sql
-- Set the labels on a Google Cloud Storage bucket
UPDATE google.storage.buckets
SET data__labels = '{"env": "prod", "team": "platform"}'
WHERE bucket = 'stackql-demo-bucket';
```

### Инструкция `UPDATE` (блокирующая) {#update-statement-blocking}
Выполните инструкцию `UPDATE`, чтобы изменить правило брандмауэра Compute Engine. Это блокирующий (синхронный) вызов.

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

### Инструкция `UPDATE` с JSON-функциями {#update-statement-with-json-functions}
Выполните инструкцию `UPDATE`, значение которой построено с помощью JSON-функций. Функция, которая не читает столбцы, вычисляется до отправки запроса к API. Значение в `SET` не может читать обновляемую строку, поэтому, чтобы изменить часть существующего значения, сначала прочитайте его с помощью `SELECT`, а затем задайте измененное значение.

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
