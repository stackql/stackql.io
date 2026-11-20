---
title: REPLACE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 REPLACE：取代資源執行個體的每個欄位，相當於 HTTP PUT，有別於 UPDATE 執行的部分更新。"
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

更新資源中的所有欄位（等同於 `put` 作業）。

另請參閱：  
[[ StackQL 資源階層 ]](/getting-started/resource-hierarchy) [[ `UPDATE` ]](/language-spec/update)

* * * 

## 語法 {#syntax}

*replaceStatement::=*

<RailroadDiagram 
type="replace"
/>

&nbsp;  
&nbsp;

```sql
REPLACE [ /*+ AWAIT  */ ] <multipartIdentifier>
SET field_name = value [, field_name = value ...]
[ WHERE condition ];
```

* * *

## 範例 {#examples}

### 基本 `REPLACE` 陳述式 {#basic-replace-statement}
執行 `REPLACE` 陳述式，取代 GitHub 存放庫上的所有主題。

```sql
-- Replace all of the topics on a GitHub repository
REPLACE github.repos.topics
SET data__names = '["sql", "cloud-infrastructure"]'
WHERE owner = 'my-org'
AND repo = 'my-repo';
```

### `REPLACE` 陳述式（封鎖式） {#replace-statement-blocking}
執行 `REPLACE` 陳述式，取代 Compute Engine 防火牆規則。這是封鎖式（同步）叫用。

```sql
-- Synchronous update of a google firewall 
REPLACE /*+ AWAIT */ google.compute.firewalls
SET
 data__name = 'test-fw',
 data__network = 'https://www.googleapis.com/compute/v1/projects/stackql-demo/global/networks/my-vpc',
 data__direction = 'INGRESS',
 data__sourceRanges = '["0.0.0.0/0"]',
 data__allowed = '[{"IPProtocol": "tcp", "ports": ["22"]}, {"IPProtocol": "tcp", "ports": ["6443"]},{"IPProtocol": "icmp"}]'
WHERE firewall = 'test-fw'
AND project = 'stackql-demo';
```

### 使用 JSON 函式的 `REPLACE` 陳述式 {#replace-statement-with-json-functions}
執行值由 JSON 函式建構的 `REPLACE` 陳述式。不讀取任何資料行的函式會在傳送要求之前求值。`SET` 值無法讀取正在取代的資料列，因此若要變更現有值的一部分，請先以 `SELECT` 讀取該值，再設定修改後的值。

```sql
-- Replace the topics on a GitHub repository with a list built by json_array
REPLACE github.repos.topics
SET data__names = json_array('sql', 'cloud-infrastructure', 'infrastructure-as-code')
WHERE owner = 'my-org'
AND repo = 'my-repo';
```
