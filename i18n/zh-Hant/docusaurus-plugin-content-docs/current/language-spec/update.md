---
title: UPDATE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 UPDATE：變更資源執行個體的指定欄位，相當於 HTTP PATCH，目標由 WHERE 子句識別。"
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

更新資源中的指定欄位（等同於 `patch` 作業）。

另請參閱：  
[[ StackQL 資源階層 ]](/getting-started/resource-hierarchy) [[ `REPLACE` ]](/language-spec/replace)

* * * 

## 語法 {#syntax}

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

## 範例 {#examples}

### 基本 `UPDATE` 陳述式 {#basic-update-statement}
執行 `UPDATE` 陳述式，設定 Google Cloud Storage 儲存貯體上的標籤。

```sql
-- Set the labels on a Google Cloud Storage bucket
UPDATE google.storage.buckets
SET data__labels = '{"env": "prod", "team": "platform"}'
WHERE bucket = 'stackql-demo-bucket';
```

### `UPDATE` 陳述式（封鎖式） {#update-statement-blocking}
執行 `UPDATE` 陳述式，修改 Compute Engine 防火牆規則。這是封鎖式（同步）叫用。

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

### 使用 JSON 函式的 `UPDATE` 陳述式 {#update-statement-with-json-functions}
執行值由 JSON 函式建構的 `UPDATE` 陳述式。不讀取任何資料行的函式會在傳送要求之前求值。`SET` 值無法讀取正在更新的資料列，因此若要變更現有值的一部分，請先以 `SELECT` 讀取該值，再設定修改後的值。

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
