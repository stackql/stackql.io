---
title: run_mutation_query
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - run_mutation_query
  - model context protocol
description: StackQL を介してプロバイダーに対し INSERT、UPDATE、REPLACE または DELETE を実行する MCP ツール
image: "/img/stackql-featured-image.png"
---

プロバイダーに対して `INSERT`、`UPDATE`、`REPLACE` または `DELETE` を実行します。**実際の副作用があります**：呼び出しが成功すると、クラウドの状態が変更されます。

サーバーの[モード](/command-line-usage/mcp#server-modes)によってゲート制御されます：

| モード | `INSERT` / `UPDATE` / `REPLACE` | `DELETE` |
|--|--|--|
|`read_only`|拒否|拒否|
|`safe`（デフォルト）|承認が必要（エリシテーション）|承認が必要|
|`delete_safe`|許可|承認が必要|
|`full_access`|許可|許可|

<br/>

:::info

複数の文を含むペイロードは、その中で最も権限の高い文によってゲート制御されます

:::

ミューテーションに承認が必要で、クライアントが MCP のエリシテーション（elicitation）機能を公開していない場合、呼び出しは拒否されます。

関連項目：
[[ MCP の概要 ]](/command-line-usage/mcp) [[ `run_lifecycle_operation` ]](/mcp/run_lifecycle_operation) [[ `describe_method` ]](/mcp/describe_method) [[ INSERT 言語仕様 ]](/language-spec/insert) [[ DELETE 言語仕様 ]](/language-spec/delete)

* * *

## 入力 {#inputs}

| 引数 | 必須 | 説明 |
|--|--|--|
|`sql`|はい|`INSERT`、`UPDATE`、`REPLACE` または `DELETE` 文。|
|`source`|いいえ|SQL が [`query_library_get`](/mcp/query_library_get) から得られた場合の Query Library のエントリ ID。出典を示すためだけのもので、実行や承認には影響しません。|
|`format`|いいえ|テキストのレンダリング形式：`markdown` または `json`。省略した場合は、サーバーのレンダリング設定を使用します。|

* * *

## 出力 {#output}

| レンダラー | 形状 |
|--|--|
| KV | `{messages: [...], timestamp}`：API のレスポンスボディではなく、送信の確認 |

* * *

## 例 {#example}

StackQL MCP サーバーを登録した、エリシテーションに対応する任意の MCP クライアントで、次のプロンプトを試してください：

```
The firewall rule allow-test-old in project my-project  
is no longer needed.  Please remove it.
```

サーバーが `full_access` モードで実行されている場合を除き、MCP クライアントは削除が送信される前に承認リクエストを表示します。

次のクリップは、StackQL MCP サーバーがデフォルトの `safe` モードで実行されている場合のエリシテーションの流れを示しています。クラウド側の変更が送信される前に、クライアントは `DELETE` を承認するよう求められます。公式の [MCP Inspector](https://github.com/modelcontextprotocol/inspector) を `npx @modelcontextprotocol/inspector` で起動すると、手元の StackQL MCP サーバーに対してこれを再現できます：


<video autoPlay loop muted playsInline style={{width: 'calc(100% - 4rem)', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/mcp_inspector_run_mutation_query_web.webm" type="video/webm" />
  <source src="/img/videos/mcp_inspector_run_mutation_query_web.mp4" type="video/mp4" />
</video>
