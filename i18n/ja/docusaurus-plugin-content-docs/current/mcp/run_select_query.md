---
title: run_select_query
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - run_select_query
  - model context protocol
description: プロバイダーに対して StackQL の SELECT を実行する MCP ツール
image: "/img/stackql-featured-image.png"
---

設定されたプロバイダーに対して `SELECT` 文を実行し、結果の行を返します。読み取り専用で、ミューテーションとライフサイクル操作は受け付けません。それらには [`run_mutation_query`](/mcp/run_mutation_query) または [`run_lifecycle_operation`](/mcp/run_lifecycle_operation) を使用します。

結果の行はクライアントに返されますが、意図的に監査ログからは除外されています。

関連項目：
[[ MCP の概要 ]](/command-line-usage/mcp) [[ `validate_select_query` ]](/mcp/validate_select_query) [[ `list_methods` ]](/mcp/list_methods) [[ SELECT 言語仕様 ]](/language-spec/select)

* * *

## 入力 {#inputs}

| 引数 | 必須 | 説明 |
|--|--|--|
|`sql`|はい|`SELECT` 文。メタデータ文（`SHOW`、`DESCRIBE`、`DESC`、`EXPLAIN`）もポリシーゲートによって読み取り専用に分類され、受け付けられます。`DESC` は StackQL `v0.12.732` 以降で認識されます。|
|`row_limit`|いいえ|クライアントに返される行数の上限。`0`（デフォルト）は、クライアント側の上限がないことを意味します。|
|`source`|いいえ|SQL が [`query_library_get`](/mcp/query_library_get) から得られた場合の Query Library のエントリ ID。出典を示すためだけのもので、実行や承認には影響しません。|
|`format`|いいえ|テキストのレンダリング形式：`markdown` または `json`。省略した場合は、サーバーのレンダリング設定を使用します。|

* * *

## 出力 {#output}

| レンダラー | 形状 |
|--|--|
| テーブル | `{rows: [...]}`：列名をキーとする、行ごとに 1 つのオブジェクト |

* * *

## ゲート制御 {#gating}

ペイロード内のすべての文が読み取り専用の場合、すべてのサーバーモード（`read_only`、`safe`、`delete_safe`、`full_access`）で許可されます。`v1.0` 以降、このツールは `full_access` を含むすべてのモードで `INSERT`、`UPDATE`、`REPLACE`、`DELETE`、`EXEC` を拒否し、[`run_mutation_query`](/mcp/run_mutation_query) または [`run_lifecycle_operation`](/mcp/run_lifecycle_operation) を示すエラーを返します。複数の文を含むペイロードは、その中で最も権限の高い文によって分類されます。そのため、先頭の `SELECT` の後ろにあるミューテーションも同じように拒否されます。このツールは `destructiveHint: false` と `openWorldHint: true` を宣言します。

* * *

## 例 {#example}

StackQL MCP サーバーを登録した、エリシテーション（elicitation）に対応する任意の MCP クライアントで、次のプロンプトを試してください：

```
List the VPC networks in my Google Cloud project my-project  
and tell me which ones use auto subnet creation.
```

Claude Desktop での例を次に示します：

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_run_select_query_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_run_select_query_web.mp4" type="video/mp4" />
</video>
