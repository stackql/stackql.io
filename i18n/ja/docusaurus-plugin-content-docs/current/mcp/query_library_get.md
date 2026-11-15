---
title: query_library_get
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - query_library_get
  - query library
  - model context protocol
description: StackQL のクエリテンプレートを取得し、指定されたパラメーターを検証して、レンダリング済みの SQL を実行せずに返す MCP ツール
image: "/img/stackql-featured-image.png"
---

[`query_library_search`](/mcp/query_library_search) が返した `id` を指定して、[Query Library](pathname:///docs/query-library/) のエントリを取得します。

`params` を省略するか空にすると、未加工のテンプレート、パラメーター宣言、注記を返します。空でない `params` オブジェクトを渡すと、サーバーが値を検証し、テンプレートに代入します。SQL を返すのは、レンダリングが成功した場合だけです。モデルが自分で値を代入する必要はありません。

このツールは SQL を実行せず、クラウドの認証情報も確認しません。このツールのパラメーター検証は、インストール済みのプロバイダーに対して `SELECT` を解析して計画を作成する [`validate_select_query`](/mcp/validate_select_query) とは異なります。

関連項目：
[[ MCP の概要 ]](/command-line-usage/mcp) [[ `query_library_search` ]](/mcp/query_library_search) [[ `run_select_query` ]](/mcp/run_select_query) [[ `run_mutation_query` ]](/mcp/run_mutation_query) [[ `run_lifecycle_operation` ]](/mcp/run_lifecycle_operation)

* * *

## 入力 {#inputs}

| 引数 | 必須 | 説明 |
|--|--|--|
|`id`|はい|検索結果のクエリ ID（たとえば `aws/s3/buckets-list`）。|
|`params`|いいえ|宣言済みのパラメーター名を値に対応付けるオブジェクト。テンプレートをレンダリングせずに確認する場合は、省略するか `{}` を渡します。|
|`format`|いいえ|テキストのレンダリング形式：`markdown` または `json`。省略した場合は、サーバーのレンダリング設定を使用します（デフォルトは `markdown`）。|

不明なパラメーターは拒否されます。値が指定されておらず、デフォルト値もない必須パラメーターは、`missing_params` で報告されます。指定された値は、宣言された型と制約に照らしてチェックされます。未解決のプレースホルダーはエラーになります。

* * *

## 出力 {#output}

デフォルトのテキストビューは KV ブロックで、識別情報、ミューテーションかどうか、次に使うツール、有効性、ソースへのリンク、および該当する場合はテンプレート、レンダリング済みの SQL、エラーを含みます。完全なメタデータを得るには、構造化ペイロードまたは `format: "json"` を使用します：

| フィールド | 説明 |
|--|--|
|`id`, `title`, `description`|エントリの識別情報と概要。|
|`mutation`, `verb`|エントリがリソースを変更するかどうか、および宣言されている場合はその操作の種類（`select`、`mutation` または `lifecycle`）。|
|`next_tool`|エントリを実行するためのツール：`run_select_query`、`run_mutation_query` または `run_lifecycle_operation`。取得時にこのツールが呼び出されることはありません。|
|`params`|パラメーター宣言。名前、型、必須フラグ、および該当する場合はデフォルト値、説明、例、列挙値、パターンを含みます。|
|`template`, `notes`|未加工のテンプレートと作成者の注記。`params` が省略されているか空の場合に返されます。|
|`rendered`, `valid`|レンダリングが試行されたかどうか、および成功したかどうか。未加工のテンプレートを確認する場合は、どちらも `false` です。|
|`sql`|レンダリング済みの SQL。レンダリングが成功した場合にのみ含まれます。|
|`missing_params`, `unknown_params`, `errors`|レンダリングが失敗した場合の検証の詳細。これらが解決されるまで SQL を実行しないでください。|
|`auth`, `permissions`|宣言されている場合、実行に必要な認証情報の環境変数と、プロバイダー固有の権限。取得自体にはどちらも必要ありません。|
|`cost`, `outputs`, `related`|宣言されている場合、ファンアウトとコストに関するヒント、結果の列の説明、関連エントリの ID。|
|`doc_url`|人が読むためのライブラリページ。|
|`source_tier`, `source_url`, `stale`|コンテンツの出所：`primary`、`fallback` または `snapshot`、その URL または組み込みパス、およびフェッチの失敗後にキャッシュまたはスナップショットからコンテンツが提供されたかどうか。|

不明な ID または不正な形式の ID はエラーを返します。有効な ID を見つけるには `query_library_search` を使用します。

* * *

## ゲート制御 {#gating}

設定されたツールの許可リストに従い、すべてのサーバーモードで許可されます。ミューテーションまたはライフサイクルのテンプレートを取得またはレンダリングしても、クラウド側への影響はなく、承認も必要ありません。返された SQL の実行は別の呼び出しであり、サーバーの[モード](/command-line-usage/mcp#server-modes)によって制御されます。

* * *

## 例 {#example}

まず、エントリを確認します：

```json
{
  "id": "aws/s3/buckets-list"
}
```

次に、ルーティングに必要なリージョンを指定します：

```json
{
  "id": "aws/s3/buckets-list",
  "params": {
    "region": "us-east-1"
  }
}
```

`rendered` と `valid` を確認したら、返された `SELECT` を [`validate_select_query`](/mcp/validate_select_query) で検証します。続いて、`next_tool` に示されたツールで実行します。その際、返された `sql` を渡し、出典を示すために `source` にライブラリエントリの `id` を設定します。実行時には、プロバイダーの認証情報と権限が必要です。
