---
title: UPDATE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の UPDATE：WHERE 句で対象を特定し、リソースインスタンスの指定したフィールドを変更します（HTTP PATCH に相当）。"
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

リソースの指定したフィールドを更新します（`patch` 操作と同義）。

関連項目：  
[[ StackQL リソース階層 ]](/getting-started/resource-hierarchy) [[ `REPLACE` ]](/language-spec/replace)

* * * 

## 構文 {#syntax}

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

## 例 {#examples}

### 基本的な `UPDATE` 文 {#basic-update-statement}
`UPDATE` 文を実行して、Google Cloud Storage バケットのラベルを設定します。

```sql
-- Set the labels on a Google Cloud Storage bucket
UPDATE google.storage.buckets
SET data__labels = '{"env": "prod", "team": "platform"}'
WHERE bucket = 'stackql-demo-bucket';
```

### `UPDATE` 文（ブロッキング） {#update-statement-blocking}
`UPDATE` 文を実行して、Compute Engine のファイアウォールルールを変更します。これはブロッキング（同期）呼び出しです。

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

### JSON 関数を使用する `UPDATE` 文 {#update-statement-with-json-functions}
値を JSON 関数で組み立てる `UPDATE` 文を実行します。列を読み取らない関数は、リクエストの送信前に評価されます。`SET` の値は更新対象の行を読み取れません。そのため、既存の値の一部を変更するには、まず `SELECT` で値を読み取り、変更後の値を設定します。

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
