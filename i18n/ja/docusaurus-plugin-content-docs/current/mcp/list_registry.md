---
title: list_registry
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_registry
  - model context protocol
description: StackQL プロバイダーレジストリで利用可能なプロバイダーとバージョンを一覧表示する MCP ツール
image: "/img/stackql-featured-image.png"
---

設定済みの StackQL プロバイダーレジストリで利用可能なプロバイダー（およびそのバージョン）を返します。ローカルキャッシュにプル済みのプロバイダーのみを報告する [`list_providers`](/mcp/list_providers) とは異なります。

省略可能な `provider` 引数を渡すと、単一のプロバイダーの公開済みバージョンを一覧表示します。まだローカルにないものをエージェントがインストールする必要がある場合は、[`pull_provider`](/mcp/pull_provider) と組み合わせて使用します。

関連項目：
[[ MCP の概要 ]](/command-line-usage/mcp) [[ `list_providers` ]](/mcp/list_providers) [[ `pull_provider` ]](/mcp/pull_provider) [[ registry コマンド ]](/command-line-usage/registry)

* * *

## 入力 {#inputs}

| 引数 | 必須 | 説明 |
|--|--|--|
|`provider`|いいえ|プロバイダー名。指定すると、結果はそのプロバイダーの公開済みバージョンに絞り込まれます。省略すると、レジストリのすべてのエントリが返されます。|

* * *

## 出力 {#output}

| レンダラー | 形状 |
|--|--|
| テーブル | レジストリエントリごとに 1 行（プロバイダーとバージョンのメタデータ） |

* * *

## ゲート制御 {#gating}

すべてのサーバーモードで許可されます。クラウドのコントロールプレーンやデータプレーンには影響しません。読み取るのはレジストリのみです。

* * *

## 例 {#example}

StackQL MCP サーバーを登録した、エリシテーション（elicitation）に対応する任意の MCP クライアントで、次のプロンプトを試してください：

```
What providers does the StackQL registry offer,  
and which versions of the aws provider are published?
```

Claude Desktop での例を次に示します：

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_registry_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_registry_web.mp4" type="video/mp4" />
</video>
