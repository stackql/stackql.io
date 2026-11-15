---
title: DESCRIBE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の DESCRIBE：クエリやミューテーションの前に、リソースのフィールド、または特定のメソッドのパラメーターと戻り値の型を一覧表示します。"
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';
import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

リソース、またはリソースの特定のメソッドを記述します。

関連項目：
[[ StackQL リソース階層 ]](/getting-started/resource-hierarchy)

* * *

## 構文 {#syntax}

*describeStatement::=*

<RailroadDiagram
type="describe"
/>

&nbsp;
&nbsp;

```sql
DESCRIBE [ METHOD ] [ EXTENDED ] <multipartIdentifier> ;
DESC [ EXTENDED ] <multipartIdentifier> ;
```

リソースを記述する場合は、`DESC` を `DESCRIBE` のエイリアスとして使用できます。`DESCRIBE METHOD` には完全なキーワードが必要です。

## レスポンス {#response}

<Tabs
  defaultValue="describe-resource"
  values={[
    { label: 'DESCRIBE', value: 'describe-resource', },
    { label: 'DESCRIBE METHOD', value: 'describe-method', },
  ]
}>
<TabItem value="describe-resource">

| 列 | 説明 |
| --- | --- |
| `name` | フィールド名 |
| `type` | データ型（`string`、`integer`、`boolean`、`number`、`object`、`array`） |
| `description` | フィールドの説明（`EXTENDED` を指定した場合） |

</TabItem>
<TabItem value="describe-method">

| 列 | 説明 |
| --- | --- |
| `name` | フィールド名 |
| `type` | データ型（`string`、`integer`、`boolean`、`number`、`object`、`array`） |
| `param_type` | `input_required`、`input_optional`、または `output` |
| `shape` | オブジェクトと配列のフィールドに対する JSON Schema のサブセット。スカラーの場合は空です。ネストされた `properties`、`items`、`required`、`enum`、`default`、`description` を含み、プロバイダーが提供している場合はブール値（`readOnly`、`writeOnly`、`deprecated`）も含みます |
| `description` | フィールドの説明（`EXTENDED` を指定した場合） |

</TabItem>
</Tabs>

* * *

## 例 {#examples}

### 基本的な `DESCRIBE` 文 {#basic-describe-statement}
認証済みのセッションから基本的な `DESCRIBE` 文を実行し、リソースのフィールドを一覧表示します。

```sql
-- Show the available fields in a Compute Engine resource
DESCRIBE google.compute.instances;
```

### 拡張 `DESCRIBE` 文 {#extended-describe-statement}
認証済みのセッションから拡張 `DESCRIBE` 文を実行し、リソースのフィールドとその説明を一覧表示します。

```sql
-- Show the available fields in a Compute Engine resource
DESCRIBE EXTENDED google.compute.instances;
```

### `DESC` エイリアス {#desc-alias}
`DESC` は、リソースに対する `DESCRIBE` の短縮形で、`EXTENDED` の有無にかかわらず使用できます。`DESCRIBE` と同様に、どこでも読み取り専用の文として扱われます。これには、`read_only` モードと `safe` モードでの MCP サーバーの `run_select_query` ツールも含まれます。

```sql
-- Same result as DESCRIBE EXTENDED google.compute.instances
DESC EXTENDED google.compute.instances;
```

### 基本的な `DESCRIBE METHOD` 文 {#basic-describe-method-statement}
リソースの 1 つのメソッドをイントロスペクトします。結果には入力パラメーターとレスポンスフィールドが含まれ、ネストされた構造は `shape` 列に表示されます。

```sql
-- Show the inputs and outputs for the buckets.get method
DESCRIBE METHOD google.storage.buckets.get;
```

### 拡張 `DESCRIBE METHOD` 文 {#extended-describe-method-statement}
拡張 `DESCRIBE METHOD` 文を実行し、形状に加えてフィールドごとの説明も含めます。

```sql
-- Show inputs, outputs, and descriptions for the buckets.insert method
DESCRIBE METHOD EXTENDED google.storage.buckets.insert;
```

### `INSERT` のペイロードを確認する {#discovering-an-insert-payload}
`DESCRIBE METHOD` は、`INSERT`（またはミューテーションを行う任意のメソッド）が受け付けるフィールドを確認するための標準的な方法です。`shape` 列にはネストされた構造が含まれており、エージェントと人間はこれを使って 1 回のクエリでペイロードを構築できます。

```sql
-- See what google.compute.instances.insert requires
DESCRIBE METHOD EXTENDED google.compute.instances.insert;
```
