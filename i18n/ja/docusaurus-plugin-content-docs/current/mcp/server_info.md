---
title: server_info
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - server_info
  - model context protocol
description: StackQL MCP サーバーの識別情報とランタイムを返す MCP ツール
image: "/img/stackql-featured-image.png"
---

StackQL MCP サーバーの識別情報とランタイム（バージョン、基盤の SQL エンジン、プロバイダーレジストリの場所、サーバーモード、レガシーの読み取り専用フラグ）を返します。必要なときに呼び出します。たとえば、拒否された操作やゲート制御の対象の操作を診断するときや、バージョン固有の動作を確認するときです。セッションの開始時やクエリの前に呼び出す必要はありません。

関連項目：
[[ MCP の概要 ]](/command-line-usage/mcp) [[ `list_providers` ]](/mcp/list_providers) [[ `list_registry` ]](/mcp/list_registry)

* * *

## 入力 {#inputs}

なし。

* * *

## 出力 {#output}

| レンダラー | 形状 |
|--|--|
| KV（LLM 向けの markdown のキーと値のブロック） | 以下のフィールドを含む構造化ペイロード |

<br/><br/>

| フィールド | 説明 |
|--|--|
|`version`|StackQL のビルドバージョン。|
|`commit`|Git コミットの短縮ハッシュ。|
|`build_date`|RFC3339 形式のビルドタイムスタンプ。|
|`platform`|`os/arch`（たとえば `linux/amd64`）。|
|`transport`|`http` または `stdio`。|
|`sql_backend`|基盤の SQL エンジン（たとえば `sqlite3`）。|
|`provider_registry`|プロバイダーレジストリの URL。|
|`mode`|有効なサーバーモード：`read_only`、`safe`、`delete_safe` または `full_access`。|
|`is_read_only`|レガシーのブール値フラグ（`mode == read_only` の場合に限り `true`）。|

* * *

## ゲート制御 {#gating}

すべてのサーバーモードで常に許可されます。ミューテーションとしては監査されません。

* * *

## 例 {#example}

StackQL MCP サーバーを登録した、エリシテーション（elicitation）に対応する任意の MCP クライアントで、次のプロンプトを試してください：

```
What StackQL server am I connected to?  
Show me the version, mode, and where it pulls providers from.
```

Claude Desktop での例を次に示します：

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_server_info_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_server_info_web.mp4" type="video/mp4" />
</video>
