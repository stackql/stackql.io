---
title: reload_credentials
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - reload_credentials
  - model context protocol
description: 認証情報を --env.file の dotenv ファイルから再読み込みし、プロバイダーごとの解決状況を報告する MCP ツール
image: "/img/stackql-featured-image.png"
---

[`--env.file`](/command-line-usage/global-flags) フラグで指定された dotenv 形式のファイルをサーバーのプロセス環境に再度読み込み、プロバイダーごとの認証情報の解決状況を報告します。これにより、環境が起動時に固定されるという問題が解決します。セッションの途中でディスク上に書き込まれた認証情報やローテーションされた認証情報が、再起動なしで実行中のサーバーに反映されます。

env ファイルはプロセス環境を置き換えるのではなく、その上に重ねて適用されます。認証情報の解決では、常にリクエスト時点のプロセス環境が読み取られます。そのため、起動時にすでに存在する変数（シェルまたはクライアントの `env` ブロックからのもの）は、`--env.file` の有無にかかわらず引き続き機能します。再読み込み時には、ファイル内にあって値が空でないキーだけが書き込まれ、設定が解除される変数はありません。そのため、再読み込みが失敗したり一部だけ完了したりしても、それまで機能していた認証情報は保持されます。

シークレットの値が返されたり、ログや監査に記録されたりすることはありません。このツールが報告するのは変数名と状態だけです。`--env.file` が設定されていない場合、このツールは単なる状態チェックとして動作します。クエリが認証情報の解決で失敗すると、MCP エラーには `reload_credentials` を呼び出して再試行するようエージェントに指示するヒントが含まれます。そのため、エージェント型のクライアントは自己修復できます。

:::note

StackQL `v0.10.542` より後のリリースで利用できます。

:::

関連項目：
[[ MCP の概要 ]](/command-line-usage/mcp) [[ 認証情報の（再）読み込み ]](/command-line-usage/mcp#credential-resourcing---envfile--reload_credentials) [[ `server_info` ]](/mcp/server_info) [[ Claude Desktop ]](/getting-started/claude-desktop)

* * *

## 入力 {#inputs}

| 引数 | 必須 | 説明 |
|--|--|--|
|`provider`|いいえ|状態レポートの対象を 1 つのプロバイダーに限定します。省略した場合は、すべてのプロバイダーが報告されます。|

* * *

## 出力 {#output}

| レンダラー | 形状 |
|--|--|
| テーブル | env ファイルの読み込み結果と、プロバイダーごとに 1 行の認証情報の解決状況 |

<br/>

構造化ペイロードには次のものが含まれます：

| フィールド | 説明 |
|--|--|
|`env_file`|設定された `--env.file` のパス（設定されていない場合は空）。|
|`env_file_sourced`|この呼び出しでファイルが見つかり、読み込まれた場合は `true`。|
|`sourced_vars`|ファイルから設定された環境変数の名前（名前のみで、値は含まれません）。|
|`providers`|プロバイダーごとに 1 行（以下を参照）。|

<br/>

`providers` の各行には次の列があります：

| 列 | 説明 |
|--|--|
|`provider`|プロバイダー名。|
|`auth_type`|プロバイダーに設定された認証タイプ。|
|`sourced_from`|認証情報の解決元。たとえば `env:OKTA_SECRET_KEY`、`file:/path/key.json`、`inline` または `none`。|
|`status`|`ok`、`unresolved` または `not_checked`。|
|`detail`|`unresolved` の場合のエラーテキスト。|

<br/>

認証情報のバイト列を介さずに認証する認証タイプ（対話型の gcloud、azure CLI、oauth2 クライアント認証情報、ロールの引き受けチェーン、OCI 署名、null）は、`not_checked` を報告します。

* * *

## ゲート制御 {#gating}

`read_only` を含むすべてのサーバーモードで許可されます。このツールは、サーバー自身のプロセス環境以外は何も変更しません。

`reverse_proxy` バックエンドモードではサポートされていません。クエリはリモートの stackql サーバープロセスで実行されるため、このツールは、バックエンドのサーバーで認証情報を再読み込みするようユーザーに指示するエラーを返します。

* * *

## 例 {#example}

StackQL MCP サーバー（`--env.file` を指定して起動したもの）を登録した任意の MCP クライアントで、次のプロンプトを試してください：

```
My okta query failed with a credential error - I've just written the key
to the credentials file.  Reload credentials and retry the query.
```
