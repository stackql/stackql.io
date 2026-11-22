---
title: list_methods
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_methods
  - model context protocol
description: StackQL リソースのアクセスメソッドを一覧表示する MCP ツール
image: "/img/stackql-featured-image.png"
---

リソースにバインドされたアクセスメソッド（HTTP 操作）を返します。利用可能な読み取りメソッド、ミューテーション、ライフサイクル操作が含まれます。これは、エージェントが**クエリを書く前に**呼び出すべきツールです。必須の `WHERE` パラメーターは、選択した読み取りメソッドから推論されるためです。SQL 動詞が意図に一致し、必須パラメーターを指定できるメソッドを選択します。次に、そのメソッドに対して [`describe_method`](/mcp/describe_method) を呼び出し、出力フィールドを含む完全なコントラクトを取得します。

関連項目：
[[ MCP の概要 ]](/command-line-usage/mcp) [[ `list_resources` ]](/mcp/list_resources) [[ `describe_method` ]](/mcp/describe_method) [[ `validate_select_query` ]](/mcp/validate_select_query)

* * *

## 入力 {#inputs}

| 引数 | 必須 | 説明 |
|--|--|--|
|`provider`|はい|プロバイダー名。|
|`service`|はい|そのプロバイダー配下のサービス。|
|`resource`|はい|`provider.service` 配下のリソース。|

<br/>

:::info

識別子には空白文字、セミコロン、バックスラッシュを含めることはできません。このような入力は、文が構築される前に拒否されます。

:::

* * *

## 出力 {#output}

| レンダラー | 形状 |
|--|--|
| テーブル | アクセスメソッドごとに 1 行（対応する SQL 動詞と必須パラメーターを含む） |

* * *

## ゲート制御 {#gating}

すべてのサーバーモードで許可されます。読み取り専用です。

* * *

## 例 {#example}

StackQL MCP サーバーを登録した任意の MCP クライアントで、次のプロンプトを試してください：

```
For Google Compute instances, what methods are available  
and what parameters are required?
```

Claude Desktop での例を次に示します：

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_methods_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_methods_web.mp4" type="video/mp4" />
</video>
