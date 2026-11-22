---
title: list_providers
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_providers
  - model context protocol
description: StackQL MCP サーバーで利用可能なクラウドおよび SaaS のプロバイダーを一覧表示する MCP ツール
image: "/img/stackql-featured-image.png"
---

ローカルの StackQL キャッシュにプル済みのプロバイダーを返します。プロバイダーは StackQL の[リソース階層](/getting-started/resource-hierarchy)の最上位です。エージェントがクエリできる対象を調べる必要がある場合は、このツールをエントリポイントとして使用します。

*レジストリ*で利用可能なプロバイダー（まだプルされていないものを含む）については、[`list_registry`](/mcp/list_registry) を参照してください。

関連項目：
[[ MCP の概要 ]](/command-line-usage/mcp) [[ `list_services` ]](/mcp/list_services) [[ `list_registry` ]](/mcp/list_registry) [[ `pull_provider` ]](/mcp/pull_provider)

* * *

## 入力 {#inputs}

なし。

* * *

## 出力 {#output}

| レンダラー | 形状 |
|--|--|
| テーブル（LLM 向けの Markdown 表） | プロバイダーごとに 1 行 |

<br />

各行には、レジストリが公開しているとおりのプロバイダー名とバージョンのメタデータが含まれます。

* * *

## ゲート制御 {#gating}

すべてのサーバーモード（`read_only`、`safe`、`delete_safe`、`full_access`）で許可されます。読み取り専用です。

* * *

## 例 {#example}

StackQL MCP サーバーを登録した任意の MCP クライアントで、次のプロンプトを試してください：

```
Which cloud providers are available for you to query right now?
```

Claude Desktop での例を次に示します：

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_providers_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_providers_web.mp4" type="video/mp4" />
</video>
