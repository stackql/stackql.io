---
title: list_services
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_services
  - model context protocol
description: StackQL プロバイダー配下のサービスを一覧表示する MCP ツール
image: "/img/stackql-featured-image.png"
---

単一のプロバイダーが公開するサービスを返します。サービスは StackQL の[リソース階層](/getting-started/resource-hierarchy)の 2 番目のレベルです。リソースを掘り下げる前に対象範囲を絞り込むため、[`list_providers`](/mcp/list_providers) の後にこのツールを呼び出します。

関連項目：
[[ MCP の概要 ]](/command-line-usage/mcp) [[ `list_providers` ]](/mcp/list_providers) [[ `list_resources` ]](/mcp/list_resources)

* * *

## 入力 {#inputs}

| 引数 | 必須 | 説明 |
|--|--|--|
|`provider`|はい|プロバイダー名（`google`、`aws`、`azure`、`github` など）。|

<br/>

:::info

識別子には空白文字、セミコロン、バックスラッシュを含めることはできません。このような入力は、文が構築される前に拒否されます。

:::

* * *

## 出力 {#output}

| レンダラー | 形状 |
|--|--|
| テーブル | 指定したプロバイダー配下のサービスごとに 1 行 |

* * *

## ゲート制御 {#gating}

すべてのサーバーモードで許可されます。読み取り専用です。

* * *

## 例 {#example}

StackQL MCP サーバーを登録した任意の MCP クライアントで、次のプロンプトを試してください：

```
List the services available in Google Cloud
```

Claude Desktop での例を次に示します：

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_services_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_services_web.mp4" type="video/mp4" />
</video>
