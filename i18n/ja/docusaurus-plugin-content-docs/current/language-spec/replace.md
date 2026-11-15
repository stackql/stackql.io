---
title: REPLACE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の REPLACE：UPDATE による部分更新とは異なり、HTTP PUT と同様にリソースインスタンスのすべてのフィールドを置き換えます。"
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

リソースのすべてのフィールドを更新します（`put` 操作と同義）。

関連項目：  
[[ StackQL リソース階層 ]](/getting-started/resource-hierarchy) [[ `UPDATE` ]](/language-spec/update)

* * * 

## 構文 {#syntax}

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

## 例 {#examples}

### 基本的な `REPLACE` 文 {#basic-replace-statement}
GitHub リポジトリのトピックをすべて置換する `REPLACE` 文を実行します。

```sql
-- Replace all of the topics on a GitHub repository
REPLACE github.repos.topics
SET data__names = '["sql", "cloud-infrastructure"]'
WHERE owner = 'my-org'
AND repo = 'my-repo';
```

### `REPLACE` 文（ブロッキング） {#replace-statement-blocking}
Compute Engine のファイアウォールルールを置換する `REPLACE` 文を実行します。これはブロッキング（同期）呼び出しです。

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

### JSON 関数を使用する `REPLACE` 文 {#replace-statement-with-json-functions}
値を JSON 関数で組み立てる `REPLACE` 文を実行します。列を読み取らない関数は、リクエストの送信前に評価されます。`SET` の値は置換対象の行を読み取れません。そのため、既存の値の一部を変更するには、まず `SELECT` で値を読み取り、変更後の値を設定します。

```sql
-- Replace the topics on a GitHub repository with a list built by json_array
REPLACE github.repos.topics
SET data__names = json_array('sql', 'cloud-infrastructure', 'infrastructure-as-code')
WHERE owner = 'my-org'
AND repo = 'my-repo';
```
