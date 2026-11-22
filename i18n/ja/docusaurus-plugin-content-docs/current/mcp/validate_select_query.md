---
title: validate_select_query
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - validate_select_query
  - model context protocol
description: StackQL の SELECT を実行せずに解析し、計画を作成する MCP ツール
image: "/img/stackql-featured-image.png"
---

単一の `SELECT` 文を実行せずに解析し、計画を作成します。`{valid, errors}` を返します。実際の API 呼び出しを行う前に、クエリがコンパイルでき、検出したメソッドにバインドできることをエージェントが確認したい場合に便利です。

:::info

`SELECT` のみ、かつ文は 1 つだけです。複数の文を含むペイロードは `validate_select_query accepts exactly one statement` で拒否されます。

:::

関連項目：
[[ MCP の概要 ]](/command-line-usage/mcp) [[ `list_methods` ]](/mcp/list_methods) [[ `run_select_query` ]](/mcp/run_select_query)

* * *

## 入力 {#inputs}

| 引数 | 必須 | 説明 |
|--|--|--|
|`sql`|はい|単一の `SELECT` 文。|

* * *

## 出力 {#output}

| レンダラー | 形状 |
|--|--|
| KV | `valid`（bool）と `errors`（文字列の配列。有効な場合は空） |

* * *

## ゲート制御 {#gating}

すべてのサーバーモードで許可されます。API 呼び出しは行われません。

* * *

## 例 {#example}

StackQL MCP サーバーを登録した任意の MCP クライアントで、次のプロンプトを試してください：

```
Can you validate this query before I run it:

select name, network  
from google.compute.subnetworks  
where project = 'my-project' and region = 'us-central1';
```

Claude Desktop での例を次に示します：

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_validate_select_query_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_validate_select_query_web.mp4" type="video/mp4" />
</video>
