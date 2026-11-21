---
title: pull_provider
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - pull_provider
  - model context protocol
description: StackQL プロバイダーをレジストリからローカルキャッシュにインストールする MCP ツール
image: "/img/stackql-featured-image.png"
---

StackQL レジストリから単一のプロバイダーをローカルの approot キャッシュにインストールします。`version` を省略すると、最新の公開済みバージョンがプルされます。

書き込むのはローカルキャッシュの状態のみで、クラウドのコントロールプレーンやデータプレーンには影響しません。そのため、このツールはすべてのサーバーモード（`read_only` を含む）で許可されます。プルする前に、[`list_registry`](/mcp/list_registry) を使用してプロバイダー名と利用可能なバージョンを調べます。

関連項目：
[[ MCP の概要 ]](/command-line-usage/mcp) [[ `list_registry` ]](/mcp/list_registry) [[ `list_providers` ]](/mcp/list_providers) [[ registry コマンド ]](/command-line-usage/registry)

* * *

## 入力 {#inputs}

| 引数 | 必須 | 説明 |
|--|--|--|
|`provider`|はい|レジストリで公開されているプロバイダー名（`aws`、`google`、`cloudflare` など）。|
|`version`|いいえ|プルする公開済みバージョン。省略すると、最新の公開済みバージョンがインストールされます。|

* * *

## 出力 {#output}

| レンダラー | 形状 |
|--|--|
| KV | プロバイダー（およびバージョン）がローカルキャッシュにインストールされたことの確認 |

* * *

## ゲート制御 {#gating}

すべてのサーバーモードで許可されます。クラウド側への影響はなく、変更されるのはローカルキャッシュの状態のみです。

* * *

## 例 {#example}

StackQL MCP サーバーを登録した任意の MCP クライアントで、次のプロンプトを試してください：

```
I want to start querying Google resources with StackQL.  
Can you install the Google provider for me?
```

Claude Desktop での例を次に示します：

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_pull_provider_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_pull_provider_web.mp4" type="video/mp4" />
</video>
