---
title: list_resources
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_resources
  - model context protocol
description: StackQL の provider.service 配下のリソースを一覧表示する MCP ツール
image: "/img/stackql-featured-image.png"
---

`provider.service` 配下のリソースを返します。リソースは StackQL の[リソース階層](/getting-started/resource-hierarchy)の 3 番目のレベルです。リソースは、StackQL クエリの `FROM` 句に記述する対象です。

関連項目：
[[ MCP の概要 ]](/command-line-usage/mcp) [[ `list_services` ]](/mcp/list_services) [[ `list_methods` ]](/mcp/list_methods) [[ `describe_method` ]](/mcp/describe_method)

* * *

## 入力 {#inputs}

| 引数 | 必須 | 説明 |
|--|--|--|
|`provider`|はい|プロバイダー名。|
|`service`|はい|そのプロバイダー配下のサービス。|

<br/>

:::info

識別子には空白文字、セミコロン、バックスラッシュを含めることはできません。このような入力は、文が構築される前に拒否されます。

:::

* * *

## 出力 {#output}

| レンダラー | 形状 |
|--|--|
| テーブル | `provider.service` 配下のリソースごとに 1 行 |

* * *

## ゲート制御 {#gating}

すべてのサーバーモードで許可されます。読み取り専用です。

* * *

## 例 {#example}

StackQL MCP サーバーを登録した、エリシテーション（elicitation）に対応する任意の MCP クライアントで、次のプロンプトを試してください：

```
List the resources in the google.storage service
```

Claude Desktop での例を次に示します：

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_resources_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_resources_web.mp4" type="video/mp4" />
</video>
