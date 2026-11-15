---
title: run_lifecycle_operation
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - run_lifecycle_operation
  - model context protocol
description: StackQL の EXEC ライフサイクル操作を実行する MCP ツール
image: "/img/stackql-featured-image.png"
---

StackQL の `EXEC` ライフサイクル操作を実行します。これは CRUD 以外のプロバイダーアクション（VM の起動や停止、キーのローテーション、ワークフローのトリガーなど）です。**実際の副作用があります**。

サーバーの[モード](/command-line-usage/mcp#server-modes)によってゲート制御されます：

| モード | ライフサイクル `EXEC` |
|--|--|
|`read_only`|拒否|
|`safe`（デフォルト）|承認が必要（エリシテーション）|
|`delete_safe`|承認が必要|
|`full_access`|許可|

<br/>

:::info

複数の文を含むペイロードは、その中で最も権限の高い文によってゲート制御されます

:::

承認が必要で、クライアントがエリシテーション（elicitation）を公開していない場合、呼び出しは拒否されます。

関連項目：
[[ MCP の概要 ]](/command-line-usage/mcp) [[ `run_mutation_query` ]](/mcp/run_mutation_query) [[ `describe_method` ]](/mcp/describe_method) [[ EXEC 言語仕様 ]](/language-spec/exec)

* * *

## 入力 {#inputs}

| 引数 | 必須 | 説明 |
|--|--|--|
|`sql`|はい|ライフサイクルメソッドを対象とする StackQL の `EXEC` 文。|
|`source`|いいえ|SQL が [`query_library_get`](/mcp/query_library_get) から得られた場合の Query Library のエントリ ID。出典を示すためだけのもので、実行や承認には影響しません。|
|`format`|いいえ|テキストのレンダリング形式：`markdown` または `json`。省略した場合は、サーバーのレンダリング設定を使用します。|

* * *

## 出力 {#output}

| レンダラー | 形状 |
|--|--|
| KV | `{messages: [...], timestamp}` |

* * *

## 例 {#example}

StackQL MCP サーバーを登録した、エリシテーションに対応する任意の MCP クライアントで、次のプロンプトを試してください：

```
The Compute instance web-01 in us-central1-a  
(project my-project) is stuck.  Please stop it.
```

サーバーが `full_access` モードで実行されている場合を除き、MCP クライアントは操作が送信される前に承認リクエストを表示します。

Claude Desktop での例を次に示します：

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_run_lifecycle_operation_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_run_lifecycle_operation_web.mp4" type="video/mp4" />
</video>
