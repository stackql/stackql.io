---
title: describe_method
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - describe_method
  - model context protocol
description: StackQL のアクセスメソッド 1 つについて完全な I/O コントラクトを返す MCP ツール
image: "/img/stackql-featured-image.png"
---

単一のアクセスメソッドの完全な I/O コントラクトを返します。常に EXTENDED であり、入力、リクエストボディのスキーマ、出力の完全な形状を含みます。エージェントは列名をここから取得します。各行には `param_type` があり、その値は `input_required`（必須の完全一致 `WHERE` 述語）、`input_optional`、`output`（`SELECT` または `RETURNING` で参照できるフィールド）のいずれかです。各メソッドはそれぞれ独自の形状を返すため、リソースには単一のフィールドリストがありません。そのため、[`list_methods`](/mcp/list_methods) で選択したメソッドに対して最初のクエリを実行する前に、このツールを呼び出してください。

関連項目：
[[ MCP の概要 ]](/command-line-usage/mcp) [[ `list_methods` ]](/mcp/list_methods) [[ `run_select_query` ]](/mcp/run_select_query)

* * *

## 入力 {#inputs}

| 引数 | 必須 | 説明 |
|--|--|--|
|`provider`|はい|プロバイダー名。|
|`service`|はい|そのプロバイダー配下のサービス。|
|`resource`|はい|`provider.service` 配下のリソース。|
|`method`|はい|[`list_methods`](/mcp/list_methods) が返すメソッド名。|

<br/>

:::info

識別子には空白文字、セミコロン、バックスラッシュを含めることはできません。このような入力は、文が構築される前に拒否されます。

:::

* * *

## 出力 {#output}

| レンダラー | 形状 |
|--|--|
| KV | 入力フィールドおよび出力フィールドごとに 1 行：名前、型、`param_type`（`input_required`、`input_optional` または `output`）、形状、説明 |

* * *

## ゲート制御 {#gating}

すべてのサーバーモードで許可されます。読み取り専用です。

* * *

## 例 {#example}

StackQL MCP サーバーを登録した、エリシテーション（elicitation）に対応する任意の MCP クライアントで、次のプロンプトを試してください：

```
Show me everything I need to know to call the insert method  
on Google Compute networks -- required parameters,  
optional parameters, and the request body shape.
```

Claude Desktop での例を次に示します：

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_describe_method_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_describe_method_web.mp4" type="video/mp4" />
</video>
