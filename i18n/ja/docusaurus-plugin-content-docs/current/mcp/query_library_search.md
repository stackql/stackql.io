---
title: query_library_search
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - query_library_search
  - query library
  - model context protocol
description: 厳選された StackQL のクエリテンプレートを、自然言語の意図、プロバイダー、サービス、タグで検索する MCP ツール
image: "/img/stackql-featured-image.png"
---

自然言語の意図に一致するテンプレートを [StackQL Query Library](pathname:///docs/query-library/) から検索します。SQL を一から作成する前にこのツールを参照し、結果の `id` を [`query_library_get`](/mcp/query_library_get) に渡して、テンプレートを確認し、パラメーターを検証します。

このツールが取得するのはライブラリのコンテンツだけです。SQL の実行やクラウド API の呼び出しは行わず、プロバイダーの認証情報も必要としません。

関連項目：
[[ MCP の概要 ]](/command-line-usage/mcp) [[ `query_library_get` ]](/mcp/query_library_get) [[ `validate_select_query` ]](/mcp/validate_select_query)

* * *

## 入力 {#inputs}

| 引数 | 必須 | 説明 |
|--|--|--|
|`intent`|はい|タスクの自然言語による説明。|
|`provider`|いいえ|プロバイダーによるフィルター（たとえば `aws`）。|
|`service`|いいえ|サービスによるフィルター（たとえば `s3`）。|
|`tags`|いいえ|タグの配列。エントリは、指定されたすべてのタグに一致する必要があります。|
|`include_mutations`|いいえ|ミューテーションのテンプレートを含めます。デフォルトは `false` です。`read_only` モードでは無視され、ミューテーションのエントリは常に除外されます。|
|`limit`|いいえ|ヒットの最大数。省略した場合や正の値でない場合のデフォルトは `5` です。上限は `20` です。|
|`format`|いいえ|テキストのレンダリング形式：`markdown` または `json`。省略した場合は、サーバーのレンダリング設定を使用します（デフォルトは `markdown`）。|

* * *

## 出力 {#output}

デフォルトのテキストビューは、`id`、`title`、`mutation`、`required_params`、`score` のランク付けされた表で、その後にソースの引用と、該当する場合はミス時のガイダンスが続きます。構造化ペイロードには次のものが含まれます：

| フィールド | 説明 |
|--|--|
|`hits`|ランク付けされたエントリ。`id`、`title`、省略可能な `description`、`providers`、`services`、`required_params`、および `mutation` と `score` を含みます。完全なパラメーター宣言を得るには、続けて `query_library_get` を使用します。|
|`miss`|ヒットがない場合、または最上位のスコアが一致のしきい値を下回る場合は `true`。スコアの低い最も近い一致が返されることもあります。|
|`guidance`|ミスした場合に、クエリを作成するためにサーバーの指示、検出ツール、`validate_select_query` を使うようエージェントに案内します。|
|`source_tier`|コンテンツのソース：`primary`、`fallback` または `snapshot`。|
|`source_url`|使用したカタログの URL または組み込みパス（引用用）。|
|`stale`|`true` の場合、フェッチの失敗後にキャッシュまたは組み込みスナップショットからコンテンツが提供されました。|

非推奨のエントリは除外されます。サーバーはカタログをキャッシュし、ホストされたライブラリから、その GitHub ソース、さらに組み込みスナップショットへとフォールバックできます。

* * *

## ゲート制御 {#gating}

設定されたツールの許可リストに従い、すべてのサーバーモードで許可されます。承認もプロバイダーの認証情報も必要ありません。ミューテーションのテンプレートを検索しても、その実行が認可されるわけではありません。

* * *

## 例 {#example}

ツールの引数：

```json
{
  "intent": "List S3 bucket names and regions",
  "provider": "aws",
  "service": "s3",
  "limit": 5
}
```

`hits` の `id`（たとえば `aws/s3/buckets-list`）を [`query_library_get`](/mcp/query_library_get) で使用します。`miss` が `true` の場合は、最も近いテンプレートを参考として確認するか、SQL を作成する前に [`list_methods`](/mcp/list_methods) と [`describe_method`](/mcp/describe_method) を使用します。
