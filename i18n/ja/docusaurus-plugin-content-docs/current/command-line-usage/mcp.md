---
title: mcp
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "AI エージェント向けに StackQL を Model Context Protocol サーバーとして実行する stackql mcp コマンド：デプロイモード、安全モード、認証情報の再読み込み、監査ログ。"
image: "/img/stackql-featured-image.png"
---

StackQL を Model Context Protocol（MCP）サーバーとして起動するコマンドです。AI エージェントやアシスタントが StackQL のクエリ機能を使用してクラウドインフラストラクチャを操作できるようになります。

:::info[MCP とは？]

[Model Context Protocol (MCP)](https://modelcontextprotocol.io/) は、AI アプリケーションが外部のデータソースやツールとやり取りする方法を標準化するオープンなプロトコルです。StackQL を MCP サーバーとして実行すると、AI エージェント（Claude、Cursor、Continue、カスタムエージェントなど）が自然言語を使用して、複数のプロバイダーにわたるクラウドインフラストラクチャをクエリし、管理できるようになります。

:::

* * *

### 構文 {#syntax}

スタンドアロンの MCP サーバーには `stackql mcp [flags]` を使用します。

MCP サーバーを PostgreSQL ワイヤプロトコルサーバーと同じプロセスで実行するには `stackql srv [flags]` を使用します。

* * *

### デプロイモード {#deployment-modes}

StackQL の MCP サーバーは、さまざまなアーキテクチャ要件に合わせて 3 つの構成でデプロイできます。以下の例では `"allow_unauthenticated": true` で認証なしの提供にオプトインしており、これはローカル開発に適しています。他のプロセスやホストから到達できる場合は、代わりに Bearer トークンを設定してください（[HTTP クライアント認証](#http-client-authentication)を参照）。

#### 1. スタンドアロン MCP サーバー {#1-standalone-mcp-server}

指定したポートで、StackQL を専用の MCP サーバーとして実行します。

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true}}'
```

**使用場面**：MCP プロトコルによるアクセスのみが必要で、PostgreSQL ワイヤプロトコルとの互換性が不要な場合に使用します。

#### 2. MCP + PostgreSQL サーバー（インメモリ） {#2-mcp--postgresql-server-in-memory}

MCP サーバーと PostgreSQL サーバーを同時に実行し、両者間でインメモリ通信を行います。

```bash
stackql srv \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true}}' \
  --pgsrv.port 5665
```

**使用場面**：AI エージェント向けの MCP プロトコルによるアクセスと、従来のデータベースクライアント向けの PostgreSQL ワイヤプロトコルの両方が必要で、インメモリ通信による最大のパフォーマンスを得たい場合に使用します。

#### 3. MCP + PostgreSQL サーバー（リバースプロキシ） {#3-mcp--postgresql-server-reverse-proxy}

TCP ベースの通信で両方のサーバーを実行します。分散デプロイと TLS 暗号化をサポートします。

```bash
stackql srv \
  --mcp.server.type=reverse_proxy \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9004", "allow_unauthenticated": true}, "backend": {"dsn": "postgres://stackql:stackql@127.0.0.1:5446?default_query_exec_mode=simple_protocol"}}' \
  --pgsrv.port 5446
```

**TLS 暗号化を使用する場合**：

```bash
stackql srv \
  --mcp.server.type=reverse_proxy \
  --mcp.config '{"server": {"tls_cert_file": "/path/to/server_cert.pem", "tls_key_file": "/path/to/server_key.pem", "transport": "http", "address": "127.0.0.1:9004", "auth_token_env_var": "STACKQL_MCP_TOKEN"}, "backend": {"dsn": "postgres://stackql:stackql@127.0.0.1:5446?default_query_exec_mode=simple_protocol"}}' \
  --pgsrv.port 5446
```

TLS はトランスポートを暗号化しますが、クライアントを認証しないため、この例では `STACKQL_MCP_TOKEN` に保持された Bearer トークンも必要です。

**使用場面**：MCP と PostgreSQL のワークロードを異なるプロセスやホストに分離する必要がある場合、または MCP エンドポイントに TLS 暗号化が必要な場合に使用します。

* * *

### 設定オプション {#configuration-options}

#### MCP サーバータイプ {#mcp-server-type}

| タイプ | 説明 |
|--|--|
|`http`|直接 HTTP サーバーモード：MCP リクエストは StackQL エンジンによってプロセス内で処理されます。|
|`reverse_proxy`|リバースプロキシモード：MCP リクエストは、設定された DSN を介してバックエンドの PostgreSQL サーバーに送られます。`stackql srv` と組み合わせて使用します。|
|`stdio`|標準入出力トランスポート：サーバーは `stdin` からリクエストを読み取り、`stdout` にレスポンスを書き込みます。エディターに組み込まれた MCP クライアント（Claude Desktop、Cursor など）が使用します。|

#### MCP ログ形式 {#mcp-log-format}

`--mcp.log.format` フラグで監査ログのエンコーディングを選択します。値は `jsonl`（デフォルト）または `otel`（OpenTelemetry OTLP/JSON ログレコード）です。設定オブジェクトの `server.audit.format` より優先されます。[ログ形式](#log-format)を参照してください。

#### MCP プロトコルバージョン {#mcp-protocol-version}

`--mcp.protocol.version` フラグは、サーバーが公開する最新のプロトコルリビジョンを設定します。値は `auto`（デフォルト、サポートされているすべてのリビジョン）、`2026-07-28`（セッションレスのみ）、または `2025-11-25` などの古いリビジョン（そのリビジョンとそれ以前）です。設定オブジェクトの `server.protocol_version` より優先されます。[リビジョンの固定](#pinning-the-revision)を参照してください。

#### MCP 設定オブジェクト {#mcp-configuration-object}

`--mcp.config` フラグは、次の構造を持つ JSON オブジェクトを受け付けます。

##### サーバー設定 {#server-configuration}

| フィールド | 説明 | 必須 |
|--|--|--|
|`server.transport`|トランスポートプロトコル：`http` または `stdio`。|はい|
|`server.address`|MCP サーバーをバインドするアドレスとポート（例：`127.0.0.1:9912`）。HTTP トランスポートのみ。|`http` の場合|
|`server.tls_cert_file`|HTTPS 用の TLS 証明書ファイルのパス。|いいえ|
|`server.tls_key_file`|HTTPS 用の TLS 秘密鍵ファイルのパス。|いいえ|
|`server.auth_token_env_var`|事前共有された Bearer トークンを保持する環境変数の名前。設定すると、すべての HTTP リクエストに `Authorization: Bearer <token>` が必要になります。変数が未設定または空の場合、サーバーは起動を拒否します。[HTTP クライアント認証](#http-client-authentication)を参照してください。|`http` の場合、これか `allow_unauthenticated` のいずれか|
|`server.allow_unauthenticated`|クライアント認証なしで HTTP トランスポートを提供するための明示的なオプトイン。デフォルトは `false` です。|`http` の場合、これか `auth_token_env_var` のいずれか|
|`server.mode`|ミューテーションとライフサイクル操作をゲート制御する安全コントラクト。`read_only`、`safe`（デフォルト）、`delete_safe`、`full_access` のいずれか。[サーバーモード](#server-modes)を参照してください。|いいえ|
|`server.read_only`|`mode: read_only` の**レガシー**なブール値エイリアス。両方のフィールドが設定されている場合は `mode` が優先されます。|いいえ|
|`server.stateless`|セッションなし（`Mcp-Session-Id` なし）で Streamable HTTP を提供します。プロトコルリビジョン `2026-07-28` は HTTP 上でこの方式で提供されます。デフォルトは `false` です。`stdio` では無視されます。[プロトコルリビジョンのサポート](#protocol-revision-support)を参照してください。|いいえ|
|`server.protocol_version`|公開する最新のプロトコルリビジョン：`auto`（デフォルト）、`2026-07-28`、または `2025-11-25` などの古いリビジョン。`2026-07-28` を指定すると、Streamable HTTP では `stateless` が暗黙的に有効になります。[リビジョンの固定](#pinning-the-revision)を参照してください。|いいえ|
|`server.audit`|監査サブシステムの設定。`audit.format`（`jsonl` または `otel`）を含みます。[監査ログ](#audit-log)を参照してください。|いいえ|

##### バックエンド設定（リバースプロキシモードのみ） {#backend-configuration-reverse-proxy-mode-only}

| フィールド | 説明 | 必須 |
|--|--|--|
|`backend.dsn`|バックエンドの StackQL サーバーへの PostgreSQL 接続文字列。|はい（`reverse_proxy` の場合）|

<br />

:::info

最適な互換性を得るため、バックエンド DSN には `default_query_exec_mode=simple_protocol` パラメーターを含めてください。

:::

##### 許可リスト {#allowlists}

| フィールド | 説明 |
|--|--|
|`enabled_tools`|サーバーが公開を許可されるツール名の配列。省略した場合または空の場合は、すべての組み込みツールが登録されます。限られたサブセットだけを公開する場合に使用します（例：読み取り専用のインベントリサーバー）。|
|`enabled_prompts`|プロンプト名の配列。`enabled_tools` と同じセマンティクスで、プロンプトに適用されます。|

<br/>

`server_info` と `list_providers` のみを公開するサーバーの例：

```json
{
  "server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true},
  "enabled_tools": ["server_info", "list_providers"]
}
```

* * *

### プロトコルリビジョンのサポート {#protocol-revision-support}

プロトコルリビジョン `2026-07-28`、`server.stateless`、`otel` 監査ログ形式は StackQL `v0.11.660` 以降で利用できます。`server.protocol_version` と `--mcp.protocol.version` フラグは StackQL `v0.12.732` 以降で利用できます。

サーバーは [Go MCP SDK](https://github.com/modelcontextprotocol/go-sdk) がサポートする Model Context Protocol のすべてのリビジョンに対応し、クライアントごとにネゴシエーションします。そのため、異なるリビジョンのクライアントが混在していても 1 つのサーバーで動作します。

| リビジョン | ライフサイクル | 承認リクエスト（`safe` / `delete_safe`） |
|--|--|--|
|`2026-07-28`（公開リビジョン）|ハンドシェイクなし：`server/discover` の後、すべてのリクエストが `_meta` に `io.modelcontextprotocol/protocolVersion` と `io.modelcontextprotocol/clientCapabilities` を含めます（HTTP では、さらに `Mcp-Protocol-Version`、`Mcp-Method`、`Mcp-Name` ヘッダーも含めます）。|`input_required` 結果。クライアントが `inputResponses` を付けて再試行します（SEP-2322）。|
|`2025-11-25`、`2025-06-18`|`initialize` / `notifications/initialized` ハンドシェイク。|サーバーから開始される `elicitation/create` リクエスト。|
|`2025-03-26`、`2024-11-05`|同上。|同上。|

<br />

**stdio** は 1 つのプロセスですべてのリビジョンを提供します。`2026-07-28` クライアントの最初のリクエストはハンドシェイクなしで処理され、古いクライアントの `initialize` も引き続き動作します。Claude Desktop、npm / PyPI ランチャー、Docker イメージでは設定は不要です。

**Streamable HTTP** のデフォルトは、クライアントごとにセッションを持つステートフルなモデル（`Mcp-Session-Id`）で、SDK は `2025-11-25` までのリビジョンでこのモデルを提供します。`2026-07-28` のリクエストには JSON-RPC エラー `-32022`（サポートされていないプロトコルバージョン。`v0.12.718` までのリリースでは単なる HTTP 400）が返され、その `data.supported` にはハンドシェイク方式のリビジョンが列挙されます。`server/discover` も同じ内容を公開します。そのため、クライアントは下位のリビジョンでネゴシエーションし、既存の HTTP 連携はセッションと承認リクエストを変わらず維持します。HTTP で `2026-07-28` をネイティブに提供するには、`server.stateless` を設定します（または、[後述](#pinning-the-revision)のとおりリビジョンを `2026-07-28` に固定します）。

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true, "stateless": true}}'
```

セッションレスのサーバーは `Mcp-Session-Id` を発行せず、`GET` と `DELETE` には 405 を返し（破棄するセッションがないため）、`tools/list`、`prompts/list`、`resources/list` の結果を接続に依存しないものに保ち、承認のラウンドトリップを `input_required` で処理します。古いクライアントの `initialize` も引き続き受け付け、読み取りも提供します。ただし、そのクライアントが初期化時に宣言したエリシテーション（elicitation）機能を保持できません（リクエストごとに一時的なセッションが割り当てられるため）。そのため、セッションレスのサーバーでは、古いクライアントは承認が必要な書き込みを承認できません。現行リビジョンのホストには `stateless` を、古いクライアントがまだ含まれる環境にはデフォルトを選択してください。

サーバーは呼び出しをまたぐ状態を保持しません。モード、監査、プロバイダーの認証情報はプロセスレベルの設定であるため、明示的なハンドルの背後に移す必要のあるものはありません。

#### リビジョンの固定 {#pinning-the-revision}

デフォルトでは、サーバーはサポートされているすべてのリビジョンを公開し、クライアントごとにネゴシエーションします。`--mcp.protocol.version` フラグ、または設定オブジェクトの `server.protocol_version`（フラグが優先されます）で、公開する最新のリビジョンを設定します。

```bash
# sessionless only; stateless Streamable HTTP is implied
stackql mcp \
  --mcp.server.type=http \
  --mcp.protocol.version=2026-07-28 \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true}}'

# handshake lifecycle only
stackql mcp --mcp.server.type=stdio --mcp.protocol.version=2025-11-25
```

| 値 | 公開されるリビジョン | 効果 |
|--|--|--|
|`auto`（デフォルト）または未指定|サポートされているすべてのリビジョン。|上記のとおりネゴシエーションします。双方が対応する最も新しいリビジョンが選ばれます。|
|`2026-07-28`|`2026-07-28` のみ。|セッションレスのみで、Streamable HTTP では `stateless` が暗黙的に有効になります。古いクライアントの `initialize` には `2025-11-25` が返されます。これは、そのレスポンスを新しいライフサイクルとして解釈せず、切断するようクライアントに促す合図です。|
|`2025-11-25`（またはそれより古いリビジョン）|そのリビジョンとそれ以前のすべてのリビジョン。|ハンドシェイク方式のライフサイクルのみ：`2026-07-28` のリクエストには、公開リビジョンを `data.supported` に列挙した JSON-RPC エラー `-32022` が返されるため、クライアントは再ネゴシエーションできます。古いハンドシェイク方式のクライアントは自身のリビジョンを維持します。|

<br />

それ以外の値は起動時の設定検証で失敗し、エラーには有効な値が示されます。

#### トランスポートの制限 {#transport-limits}

Streamable HTTP のリクエストボディの上限は 4 MiB（超えると HTTP 413）、1000 階層より深くネストされた JSON は解析前に拒否され、`stdio` の 1 フレームの上限は 8 MiB です。これらの制限はいずれも設定できません。いずれも、サーバーが受け付ける SQL 文よりはるかに大きな値です。

* * *

### 認証情報の（再）読み込み（`--env.file` + `reload_credentials`） {#credential-resourcing---envfile--reload_credentials}

:::note

`--env.file` と `reload_credentials` ツールは StackQL `v0.10.542` より後のリリースで利用できます。

:::

プロセスの環境ブロックは起動時に固定されます。このため、MCP サーバーには 2 つの問題が生じます。

- 認証情報の環境変数がない状態でデスクトップクライアント（Claude Desktop、Cursor など）から起動された MCP `stdio` サーバーは、それらの変数を参照できません。クライアントは自身の環境をサブプロセスに渡すためです。
- ローテーションされた短期間有効な認証情報は、長時間実行されているサーバーセッションに反映されません。

`--env.file` フラグは、変更可能な認証情報ストアとして機能する dotenv 形式のファイルを指定することで、両方の問題を解消します。

```bash
stackql mcp --mcp.server.type=stdio --env.file /path/to/stackql-credentials.env
```

#### 読み込みのセマンティクス {#sourcing-semantics}

このファイルは、すべてのエントリポイント（`exec`、`shell`、`srv`、`registry`、`mcp`）で起動時にプロセス環境に読み込まれます。

- フラグが空または未設定の場合は何も行われません。認証情報はリクエスト時にプロセス環境から遅延解決され、従来の動作とまったく同じです。
- 指定したパスにファイルが存在しない場合は、何もせずに許容されます（ファイルは後から作成できます）。
- 形式が不正なファイルや読み取れないファイルは、起動時の致命的なエラーになります。
- 値が空でないキーのみが設定されます。既存のプロセス環境変数は上書き**されます**（ファイルが信頼できる唯一の情報源です）。設定が解除されることはありません。
- このフラグはコマンドラインでのみ指定できます。`.stackqlrc` からは読み込まれません。

ファイル形式は 1 行に 1 つの `KEY=VALUE` です。`#` コメント、空行、省略可能な `export ` プレフィックス、値を囲む省略可能な一重引用符または二重引用符、CRLF 改行はすべて許容されます。

```bash
# stackql-credentials.env
export OKTA_SECRET_KEY="00abc...xyz"
STACKQL_GITHUB_USERNAME=myuser
STACKQL_GITHUB_PASSWORD='ghp_mytoken'
```

#### `reload_credentials` {#reload_credentials}

[`reload_credentials`](/mcp/reload_credentials) ツールは、セッションの途中で `--env.file` のファイルをプロセス環境に再読み込みし、プロバイダーごとの認証情報の解決状況を報告します。シークレットの値は返されず、ログにも監査にも記録されません。記録されるのは名前とステータスのみです。`--env.file` が設定されていない場合、このツールは単なるステータス確認になります。

認証情報の解決でクエリが失敗すると、MCP エラーには `reload_credentials` を呼び出して再試行するようエージェントに指示するヒントが含まれます。そのため、エージェント型のクライアントは自己回復します。認証情報をファイルに書き込み、エージェントに再読み込みを依頼すれば、失敗したクエリは再試行で成功します。サーバーの再起動は不要です。

`reload_credentials` は `reverse_proxy` バックエンドモードではサポートされていません。クエリはリモートの stackql サーバープロセスで実行されるため、このツールはバックエンドのサーバーで再読み込みするようユーザーに指示するエラーを返します。

#### Windows と Claude Desktop {#windows-and-claude-desktop}

`setx` が書き込むのはレジストリであり、実行中のプロセスではありません。また、Claude Desktop は自身の（古い）環境を MCP サブプロセスに渡します。そのため、認証情報を変更するには通常 Claude Desktop を完全に再起動する必要があります。`--env.file` を使用すると、認証情報ファイルはいつでも作成またはローテーションでき、`reload_credentials` が再起動なしでそれを読み込みます。設定例については、[Claude Desktop で StackQL を使用する](/getting-started/claude-desktop)を参照してください。

* * *

### HTTP クライアント認証 {#http-client-authentication}

:::note

`server.auth_token_env_var` と `server.allow_unauthenticated` は StackQL `v0.12.742` 以降で利用できます。

:::

HTTP トランスポートにはクライアント認証が必要です。`server.auth_token_env_var` には、事前共有された Bearer トークンを保持する環境変数の名前を指定します。すべてのリクエストには `Authorization: Bearer <token>` が必要で、ない場合は `401` が返されます。トークンは環境から読み取られるため、`--mcp.config` やプロセスの引数に現れることはありません。

```bash
export STACKQL_MCP_TOKEN="$(openssl rand -hex 32)"

stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "auth_token_env_var": "STACKQL_MCP_TOKEN"}}'
```

:::info[サーバー認証が必要]

トークンなしでの提供は明示的なオプトインです。`server` に `"allow_unauthenticated": true` を設定してください。どちらのキーもない場合、サーバーは両方のキーを示すメッセージを出して起動時に終了します。トークンの変数が未設定または空の場合も終了します。`v0.12.742` リリース以降、`http` トランスポートを使用する場合は、`server` に `auth_token_env_var` または `"allow_unauthenticated": true` のいずれかを追加する必要があります。

:::

TLS（`tls_cert_file` / `tls_key_file`）はトランスポートを暗号化しますが、クライアントを認証しません。ループバック以外のリスナーでは、TLS とトークンを組み合わせてください。クロスオリジンのブラウザーリクエスト（`Origin` の不一致、またはクロスサイトの `Sec-Fetch-Site`）は、認証の有無にかかわらず `403` で拒否されます。同梱の `stackql_mcp_client` は `Authorization` ヘッダーを送信しないため、これを使用するサーバーは認証なしの提供にオプトインする必要があります。

`v1.0` 以降、[`--access.config`](/command-line-usage/access-management) を指定してサーバーを起動すると、共有トークンによるゲートが呼び出し元ごとの認証に置き換わります。各 HTTPS リクエストは、設定された JWT、静的 Bearer トークン、または相互 TLS の方式で検証され、ポリシールールに照らして認可されます。この場合、サーバーの TLS は必須で、HTTP 経由の `local_os` 認証は拒否されます。また、認証情報のないリクエストがサーバーの環境トークンにフォールバックすることはありません。アクセス管理は `reverse_proxy` サーバータイプでは利用できません。

* * *

### サーバーモード {#server-modes}

`server.mode` で 4 つの安全コントラクトのいずれかを選択します。4 つすべてで `SELECT` とメタデータの読み取りが許可されます。違いはミューテーションとライフサイクル操作の扱いです。

| モード | `SELECT` / メタデータ | `INSERT` / `UPDATE` / `REPLACE` | `DELETE` | `EXEC`（ライフサイクル） |
|--|--|--|--|--|
|`read_only`|許可|拒否|拒否|拒否|
|`safe`（デフォルト）|許可|承認が必要|承認が必要|承認が必要|
|`delete_safe`|許可|許可|承認が必要|承認が必要|
|`full_access`|許可|許可|許可|許可|

<br />

**拒否**の場合は、クライアントに即座にエラーが返されます。

**承認が必要**の場合は、ネゴシエーションされた[プロトコルリビジョン](#protocol-revision-support)に応じた MCP エリシテーションフローが使用されます。

- クライアントがエリシテーション機能を公開している場合（初期化時、または `2026-07-28` ではリクエストごとの `_meta` のクライアント機能で）、サーバーは短いメッセージ（ツール名、クエリクラス、SQL）で操作の承認をユーザーに求めます。`2026-07-28` では、承認リクエストは ID `stackql_approval` の `elicitation/create` 入力リクエストを含む `input_required` 結果として返され、クライアントは回答を `inputResponses` に入れて呼び出しを再試行します。それより前のリビジョンでは、サーバーが呼び出しの途中で `elicitation/create` リクエストを自ら送信します。ユーザーは承認、拒否、またはキャンセルします。
- クライアントがエリシテーションを公開して**いない**場合、ツールは拒否され、その理由を説明し運用者に `full_access` モードを案内するメッセージが返されます。

承認にはクライアントが応答するため、承認はエージェントがユーザーの関与なしに動作することを防ぐものであり、信頼できない呼び出し元を防ぐものではありません。呼び出し元が信頼できない場合は、HTTP トランスポートに[クライアント認証](#http-client-authentication)を組み合わせてください。

モードはサーバーごとにグローバルです。ツールごとの上書きはありません。

`v1.0` 以降、[`run_select_query`](/mcp/run_select_query) は `full_access` を含むすべてのモードで `INSERT`、`UPDATE`、`REPLACE`、`DELETE`、`EXEC` を拒否し、エラーでは `run_mutation_query` または `run_lifecycle_operation` が示されます。また、すべてのツールには `title` と、4 つの MCP 動作ヒントが明示的なブール値として付与されます（`readOnlyHint`、`destructiveHint`、`idempotentHint`、`openWorldHint`）。これらは、ポリシーゲートが適用するのと同じ分類から導出されます。`openWorldHint` は、レジストリまたはプロバイダーのエンドポイントにアクセスする `list_registry`、`pull_provider`、`run_select_query`、`run_mutation_query`、`run_lifecycle_operation` では `true`、ローカルの状態のみを読み取るツールでは `false` です。ヒントはクライアントの UX 向けの参考情報であり、強制は引き続きポリシーゲートが行います。

:::info[ペイロードのゲート制御]

リリース `v0.12.742` 以降、複数の文を含むペイロードは、すべてのモードで最も権限の高い文によってゲート制御されます。そのため、`SELECT 1; DELETE ...` は `DELETE` として扱われ、監査レコードの `query_class` は `mutation_delete` になります。`validate_select_query` は 1 つの文のみを受け付けます。

:::

#### デフォルトモードの動作変更 {#default-mode-behaviour-change}

以前のリリースでは、安全性は単一の `read_only: true/false` フラグでのみ適用され、そのデフォルトは「適用なし」でした。現在のデフォルト（`mode: safe`）では、初期状態でミューテーションにユーザーの承認が必要です。エリシテーションに対応したクライアント（Claude Desktop、Cursor、Continue など）を使用している運用者には、ミューテーションごとに 1 回の承認リクエストが表示されます。自動化を実行している運用者や、同梱の `stackql_mcp_client`（エリシテーションを公開しません）を使用している運用者は、より制限の緩いモードに明示的にオプトインする必要があります。`INSERT`、`UPDATE`、`REPLACE` を実行するには `mode: delete_safe`、さらに `DELETE` とライフサイクル操作も実行するには `mode: full_access` を指定します。

レガシーの `read_only: true` JSON / YAML キーは後方互換性のために引き続き受け付けられ、`mode: read_only` と同等に扱われます。

#### 例 {#examples}

```bash
# Read-only: SELECTs proceed; mutations and lifecycle refused immediately.
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true, "mode": "read_only"}}'

# Delete-safe: INSERT/UPDATE/REPLACE proceed; DELETE and EXEC need approval.
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true, "mode": "delete_safe"}}'

# Full access: everything proceeds without prompting.  Use only with trusted
# clients and operators.
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true, "mode": "full_access"}}'
```

* * *

### 監査ログ {#audit-log}

すべてのツール呼び出しは、設定された監査シンクに 1 件のレコードを書き込みます。形式はデフォルトで JSONL、または OpenTelemetry ログレコードです（[ログ形式](#log-format)を参照）。監査は**デフォルトで有効**です。監査が答えるのは「エージェントが何をしたか」であり、「エージェントが何を見たか」ではありません。`SELECT` 文の結果行は、どちらの形式でも意図的に記録されません。

#### 記録される内容 {#what-gets-recorded}

| フィールド | 説明 |
|--|--|
|`timestamp`|呼び出し開始時刻（RFC3339）。|
|`tool`|ツール名（例：`run_select_query`）。|
|`mode`|呼び出し時に有効なサーバーモード。|
|`decision`|`allow` / `refuse_immediate` / `needs_approval_accepted` / `needs_approval_declined` / `needs_approval_cancelled` / `needs_approval_unavailable`.|
|`query_class`|`select` / `mutation_create` / `mutation_delete` / `lifecycle` / `unknown`。複数の文を含むペイロードでは、最も権限の高い文のクラス。|
|`sql`|クエリツール（`run_select_query`、`run_mutation_query`、`run_lifecycle_operation`、`validate_select_query`）の SQL 文字列。|
|`args`|メタデータツール（`list_*`、`describe_method`）では階層フィールド、クエリツールでは SQL + `row_limit`。|
|`duration_ms`|ゲート + ハンドラーの実経過時間。|
|`error`|ツールがエラーになった場合、または拒否された場合のエラーメッセージ。|

<br />

`SELECT` 文の結果行は意図的に除外されています。結果行は非常に大きくなる可能性があり、機密データを含む場合もあるためです。

#### ファイルシンク {#file-sink}

現在提供されているシンクは `file` のみです。1 行に 1 つの JSON オブジェクトを書き込み、レコードごとに fsync し、サイズ / 経過日数 / バックアップ数に基づく lumberjack 方式のローテーションを行います。

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{
    "server": {
      "transport": "http",
      "address": "127.0.0.1:9912",
      "allow_unauthenticated": true,
      "audit": {
        "file": {
          "path": "/var/log/stackql-mcp.log",
          "max_size_mb": 100,
          "max_backups": 5,
          "max_age_days": 30
        }
      }
    }
  }'
```

場所は 2 つの方法で指定できます。

- `audit.file.path`：完全なファイルパス（絶対パス、または cwd からの相対パス）。
- `audit.file.dir`：ディレクトリ。シンクはその中のベース名として `stackql_mcp_server_<RFC3339-utc-second>.log` を選択します。

`mcp.config` でどちらも設定されていない場合、MCP サーバーは `dir` のデフォルトを cwd にするため、既存の運用者には従来と同じ動作になります。基盤となる汎用シンクパッケージ自体は、ディレクトリを暗黙に選択することを拒否します。「ログをどこに出力するか」は常に呼び出し元が決定します。

解決された絶対パスは、起動時に `sink file: /path/to/file.log` として stderr にログ出力されます。

#### ログ形式 {#log-format}

監査ストリームには 2 つのエンコーディングがあり、どちらも同じファイルシンクに書き込まれます。

```bash
stackql mcp --mcp.server.type=stdio --mcp.log.format=jsonl   # default, unchanged
stackql mcp --mcp.server.type=stdio --mcp.log.format=otel    # OTLP/JSON log records
```

このフラグは `mcp.config` の `server.audit.format`（`"audit": {"format": "otel"}`）より優先されます。`jsonl` は上記のレコードとバイト単位で互換性があります。`otel` は 1 行に 1 つの OTLP/JSON `LogsData` オブジェクトを書き込みます。これは OpenTelemetry Collector の `otlp_json_file` レシーバーがそのまま取り込める形式であるため（otelcol-contrib 0.160.0 で検証済み）、ストリームは transform プロセッサーなしで任意の OTLP パイプラインに届きます。

```yaml
receivers:
  otlp_json_file:
    include: [/var/log/stackql-mcp.log]
exporters:
  debug: {}
service:
  pipelines:
    logs:
      receivers: [otlp_json_file]
      exporters: [debug]
```

各ツール呼び出しは 1 件のログレコードを生成します。承認ゲートを通過した呼び出しでは、エリシテーションの決定について 2 件目のレコードが生成されます。1 回の呼び出しのレコードは `gen_ai.tool.call.id` とトレース ID を共有します。呼び出し元が `params._meta` で W3C `traceparent` を指定した場合はそれが使用されます。指定がない場合は MCP セッションごとに 1 つのトレース ID が生成されるため、エージェントのセッションのレコードを関連付けることができます。

出力される属性セットはバージョン管理されたインターフェイスです。インストルメンテーションスコープは `github.com/stackql/stackql/pkg/mcp_server/audit`（バージョン `1.0.0`）で、`schemaUrl` は安定版の規約を `https://opentelemetry.io/schemas/1.44.0` に固定します。`gen_ai.*` と `mcp.*` の属性は [GenAI](https://github.com/open-telemetry/semantic-conventions-genai/blob/main/docs/gen-ai/gen-ai-spans.md#execute-tool-span) および [MCP](https://github.com/open-telemetry/semantic-conventions-genai/blob/main/docs/gen-ai/mcp.md) のセマンティック規約に従います。これらの規約は Development ステータスです。属性セットが変更されるとスコープのバージョンが上がります。

| 属性 | レコード | 値 |
|--|--|--|
|`service.name`、`service.version`|リソース|`stackql`、StackQL のバージョン|
|`gen_ai.operation.name`|ツール呼び出し|`execute_tool`|
|`mcp.method.name`|両方|ツール呼び出しレコードでは `tools/call`、決定レコードでは `elicitation/create`|
|`gen_ai.tool.name`|両方|ツール名|
|`gen_ai.tool.call.id`|両方|呼び出しごとに生成され、両方のレコードで共有|
|`mcp.protocol.version`|両方|ネゴシエーションされたリビジョン|
|`mcp.session.id`|両方|セッション ID（セッションレスの HTTP リクエストでは存在しない）|
|`stackql.mode`|両方|サーバーモード|
|`stackql.decision`|両方|上の表の `decision` の値|
|`stackql.query`|ツール呼び出し|クエリツールの場合、そのままの SQL|
|`stackql.query_class`|ツール呼び出し|`query_class` の値|
|`stackql.provider`|ツール呼び出し|メタデータツールの場合、プロバイダー|
|`stackql.query.source`|ツール呼び出し|指定された場合、Query Library のエントリ ID|
|`stackql.duration_ms`|ツール呼び出し|実経過時間|
|`stackql.rows_returned`|ツール呼び出し|行を返すツールの場合、行数|
|`error.type`、`error.message`|ツール呼び出し|失敗時：拒否した決定、またはゲート通過後の場合は `tool_error`|

<br />

Body は `execute_tool <tool>` / `elicitation <tool>` で、重大度は `INFO`、呼び出しが失敗または拒否された場合は `ERROR` です。秘匿化のコントラクトはどちらの形式でも同じです。結果の値はシリアル化されず、その結果を生成した文のみがシリアル化されます。

#### 障害モード {#failure-modes}

シンクがエラーを返した場合のレスポンスの動作は、`audit.failure_mode` によって決まります。

| failure_mode | 効果 |
|--|--|
|`strict`（デフォルト）|基になるツールが成功した場合でも、ツール呼び出しはクライアントに監査エラーを返します。これは意図的な動作です。検出されない `DELETE` よりも、クライアントへの曖昧なレスポンスのほうが望ましいためです。|
|`strict_mutations`|`SELECT` / メタデータの読み取りは stderr に注記を出力して続行し、ミューテーションとライフサイクル操作は監査エラーを返します。|
|`best_effort`|常に stderr にログを出力して続行します。|

#### 実行順序 {#sequencing}

監査の書き込みは、ツールの実行（またはゲートによる除外）の**後**、クライアントにレスポンスが返される**前**に行われます。strict モードでは、成功した DELETE で監査の書き込みが失敗すると、行は削除されていてもクライアントはエラーを受け取ります。これは、監査されないままのミューテーションが発生しないようにするための設計です。

#### 監査の無効化 {#disabling-audit}

監査を完全に無効にして、監査機能のなかった `v0.10.474` より前のリリースと同じ状態にするには、次のように設定します。

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true, "audit": {"disabled": true}}}'
```

* * *

### 利用可能な MCP ツール {#available-mcp-tools}

MCP サーバーとして実行すると、StackQL は設定された `enabled_tools` 許可リストの範囲で次のツールを公開します。各ツールは、レンダリングされたテキストビュー（LLM 向け）と型付きの構造化ペイロード（プログラムから利用するクライアント向け）の両方を返します。デフォルトのテキストレンダリングは、複数行の均一な結果では markdown の表、まばらな結果や単一レコードの結果では markdown の KV ブロックです。引数を持つツールは、省略可能な `format`（`markdown` または `json`）による上書きを受け付けます。指定しない場合はサーバーのレンダリング設定が使用されます。以下の入力はタスク固有の引数を示し、`?` は省略可能な入力を表します。

ツール名をクリックすると、入力、ゲート制御の動作、プロンプトの例を含む完全なリファレンスページが表示されます。

| ツール | レンダラー | 説明 | 入力 |
|--|--|--|--|
|[`server_info`](/mcp/server_info)|KV|サーバーの識別情報とランタイム：stackql のバージョン、基盤の SQL エンジン、プロバイダーレジストリの場所、モード、読み取り専用フラグ。必要なときに呼び出します。たとえば、拒否された操作やゲート制御の対象の操作を診断するときや、バージョン固有の動作を確認するときです。|なし|
|[`list_providers`](/mcp/list_providers)|テーブル|ローカルキャッシュにプル済みのプロバイダー（階層の最上位）。|なし|
|[`list_services`](/mcp/list_services)|テーブル|プロバイダー配下のサービス。|`provider`|
|[`list_resources`](/mcp/list_resources)|テーブル|`provider`.`service` 配下のリソース。|`provider`, `service`|
|[`list_methods`](/mcp/list_methods)|テーブル|リソースのアクセスメソッド（HTTP 操作）と、それぞれが対応する SQL 動詞および必須パラメーター。クエリを書く前に呼び出します。必須の `WHERE` パラメーターはここから推定されます。|`provider`, `service`, `resource`|
|[`describe_method`](/mcp/describe_method)|KV|1 つのメソッドの完全な I/O コントラクト（常に EXTENDED）：必須および省略可能な入力と、`SELECT` が参照できる出力フィールド。|`provider`, `service`, `resource`, `method`|
|[`query_library_search`](/mcp/query_library_search)|テーブル|自然言語の意図から厳選されたクエリテンプレートを検索します。読み取り専用です。ミューテーションのエントリはオプトインが必要で、`read_only` モードでは除外されます。|`intent`, `provider?`, `service?`, `tags?`, `include_mutations?`, `limit?`|
|[`query_library_get`](/mcp/query_library_get)|KV|テンプレートとそのパラメーター宣言を取得します。`params` が空でない場合は、値を検証し、実行せずにレンダリングされた SQL と `next_tool` を返します。|`id`, `params?`|
|[`validate_select_query`](/mcp/validate_select_query)|KV|単一の `SELECT` を実行せずに解析し、計画を作成します。`{valid, errors}` を返します。`SELECT` のみで、文は 1 つだけです。|`sql`|
|[`run_select_query`](/mcp/run_select_query)|テーブル|`SELECT` を実行します。`{rows}` を返します。読み取りのみ。|`sql`, `row_limit?`|
|[`run_mutation_query`](/mcp/run_mutation_query)|KV|プロバイダーに対して `INSERT`/`UPDATE`/`REPLACE`/`DELETE` を実行します。**実際の副作用があります。** `{messages, timestamp}` を返します。サーバーの[モード](#server-modes)によってゲート制御されます。|`sql`|
|[`run_lifecycle_operation`](/mcp/run_lifecycle_operation)|KV|stackql の `EXEC` ライフサイクル操作を実行します。`{messages, timestamp}` を返します。サーバーの[モード](#server-modes)によってゲート制御されます。|`sql`|
|[`list_registry`](/mcp/list_registry)|テーブル|設定されたレジストリで利用可能なプロバイダー（とそのバージョン）。プル済みのプロバイダーのみを一覧表示する `list_providers` とは異なります。|`provider?`|
|[`pull_provider`](/mcp/pull_provider)|KV|レジストリから 1 つのプロバイダーをローカルキャッシュにインストールします。影響はローカルキャッシュの状態のみで、クラウドのコントロールプレーンやデータプレーンには影響しません。|`provider`, `version?`|
|[`reload_credentials`](/mcp/reload_credentials)|テーブル|`--env.file` の dotenv ファイルから認証情報をプロセス環境に再読み込みし、プロバイダーごとの解決状況（`ok`、`unresolved`、`not_checked`）を報告します。シークレットの値は返しません。すべてのモードで許可されます。|`provider?`|

SQL を一から書く前に `query_library_search` を確認し、次に `query_library_get` を使用して一致するテンプレートを確認し、レンダリングしてください。クエリツールは、ライブラリのエントリ ID を含む省略可能な `source` も受け付けます。これは帰属を示すためだけのもので、実行には通常のサーバーモードと承認ポリシーが引き続き適用されます。

:::note

`describe_resource` は StackQL `v0.12.732` で廃止されました。これを呼び出していた MCP クライアント、プロンプト、エージェントの指示は、代わりに [`list_methods`](/mcp/list_methods) を呼び出し、続けて `describe_method` を呼び出してください。各アクセスメソッドが独自の形式を返すため、リソースには単一のフィールドリストがありません。そのため、列名は、クエリのルーティング先のメソッドに対する [`describe_method`](/mcp/describe_method) から取得します。SQL の [`DESCRIBE`](/language-spec/describe) 文は変更されていません。

:::

### 利用可能な MCP プロンプト {#available-mcp-prompts}

静的なプロンプトが 1 つ公開されています。

| プロンプト | 説明 |
|--|--|
|`write_safe_select`|stackql のリソースに対して安全な `SELECT` クエリを書くためのガイダンス。`SHOW METHODS IN <provider>.<service>.<resource>` を使用して、適切な読み取りメソッドと必須の `WHERE` パラメーターを見つける方法を説明します。|

* * *

### フラグ {#flags}

| フラグ | 説明 |
|--|--|
|`--mcp.server.type`|MCP サーバータイプ：`http`、`stdio`、または `reverse_proxy`（後者は `stackql srv` と組み合わせて使用）。|
|`--mcp.config`|MCP サーバーの JSON 設定オブジェクト。YAML も受け付けます。|
|`--mcp.log.format`|監査ログのエンコーディング：`jsonl`（デフォルト）または `otel`（OpenTelemetry OTLP/JSON ログレコード）。`server.audit.format` より優先されます。[ログ形式](#log-format)を参照してください。|
|`--mcp.protocol.version`|公開する最新のプロトコルリビジョン：`auto`（デフォルト）、`2026-07-28`、または `2025-11-25` などの古いリビジョン。[リビジョンの固定](#pinning-the-revision)を参照してください。|
|`--env.file`|起動時に読み込まれ、`reload_credentials` ツールによって必要に応じて再読み込みされる dotenv 形式の認証情報ファイル。[認証情報の（再）読み込み](#credential-resourcing---envfile--reload_credentials)を参照してください。|
|`--pgsrv.port`|PostgreSQL ワイヤプロトコルサーバーの TCP ポート（`stackql srv` と組み合わせて使用）。|
|`-h`, `--help`|ヘルプ情報を表示します。|
|`-v`, `--verbose`|追加の出力を含む詳細モードで実行します。|

&nbsp;
&nbsp;
> その他のオプションについては、[グローバルフラグ](/command-line-usage/global-flags)を参照してください

:::info

MCP サーバーを起動する前に、プロバイダーの認証に必要な環境変数を設定するか、[`--env.file`](#credential-resourcing---envfile--reload_credentials) で dotenv 形式の認証情報ファイルを指定する必要があります。詳細については、[プロバイダーの使用](/getting-started/using-a-provider)を参照してください。

:::

* * *

### 例 {#examples-1}

#### 基本的なスタンドアロン MCP サーバー {#basic-standalone-mcp-server}

プロバイダーの認証と、クライアント用の Bearer トークンを設定して、スタンドアロン MCP サーバーを起動します。

```bash
export GOOGLE_CREDENTIALS=$(cat /path/to/google-credentials.json)
export STACKQL_MCP_TOKEN="$(openssl rand -hex 32)"

stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "auth_token_env_var": "STACKQL_MCP_TOKEN"}}' \
  --registry='{"url": "https://registry.stackql.app/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

#### MCP サーバーと PostgreSQL サーバー（インメモリ） {#mcp-server-with-postgresql-server-in-memory}

MCP サーバーと PostgreSQL サーバーの両方を実行し、2 つのプロトコルでアクセスできるようにします。

```bash
export GOOGLE_CREDENTIALS=$(cat /path/to/google-credentials.json)

stackql srv \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true}}' \
  --pgsrv.port 5665 \
  --registry='{"url": "https://registry.stackql.app/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

#### 既知のパスに監査ログを出力する読み取り専用のインベントリサーバー {#read-only-inventory-server-with-audit-to-a-known-path}

変更を一切行わないインベントリスキャン用のエージェントに適しています。

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{
    "server": {
      "transport": "http",
      "address": "127.0.0.1:9912",
      "allow_unauthenticated": true,
      "mode": "read_only",
      "audit": {"file": {"path": "/var/log/stackql-mcp-audit.log"}}
    }
  }' \
  --registry='{"url": "https://registry.stackql.app/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

#### フルアクセスの自動化サーバー {#full-access-automation-server}

ミューテーションが必要で、エリシテーションリクエストに応答できない、信頼できる自動化パイプライン向けです。パイプラインは `STACKQL_MCP_TOKEN` に保持された Bearer トークンを提示します。

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{
    "server": {
      "transport": "http",
      "address": "127.0.0.1:9912",
      "auth_token_env_var": "STACKQL_MCP_TOKEN",
      "mode": "full_access"
    }
  }' \
  --registry='{"url": "https://registry.stackql.app/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

#### TLS を使用したセキュアな MCP サーバー {#secure-mcp-server-with-tls}

リバースプロキシモードで、TLS 暗号化を有効にした MCP サーバーを起動します。

まず、TLS 証明書を生成します（開発用の自己署名証明書）。

```bash
openssl req -x509 -newkey rsa:4096 -keyout server_key.pem -out server_cert.pem -days 365 -nodes
```

次に、サーバーを起動します。

```bash
export STACKQL_MCP_TOKEN="$(openssl rand -hex 32)"

stackql srv \
  --mcp.server.type=reverse_proxy \
  --mcp.config '{
    "server": {
      "tls_cert_file": "server_cert.pem",
      "tls_key_file": "server_key.pem",
      "transport": "http",
      "address": "127.0.0.1:9004",
      "auth_token_env_var": "STACKQL_MCP_TOKEN"
    },
    "backend": {
      "dsn": "postgres://stackql:stackql@127.0.0.1:5446?default_query_exec_mode=simple_protocol"
    }
  }' \
  --pgsrv.port 5446 \
  --registry='{"url": "https://registry.stackql.app/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

* * *

### MCP サーバーのテスト {#testing-your-mcp-server}

リポジトリには、スクリプトや回帰テスト用の開発用 MCP クライアント（`stackql_mcp_client`）が含まれています。次のコマンドでビルドします。

```bash
python cicd/python/build.py --build-mcp-client
```

これにより `./build/stackql_mcp_client` が生成されます。

`stackql_mcp_client` は `Authorization` ヘッダーを送信しないため、対象のサーバーでは `"allow_unauthenticated": true` を設定する必要があります（上記の例のとおり）。トークンで保護されたサーバーは `401` を返します。また、MCP のエリシテーション機能も公開**しません**。非対話型のスクリプト用だからです。`safe` のサーバーに対しては、ミューテーションとライフサイクルの呼び出しで「client does not support elicitation」による拒否を受け取ります。`delete_safe` のサーバーに対しては、`DELETE` と `EXEC` の呼び出しでこの拒否を受け取ります。エリシテーションリクエストを使用した対話型のテストには、エディターに組み込まれた MCP クライアント（Claude Desktop、Cursor、Continue）を使用してください。

```bash
# List all available tools
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912

# Server identity, mode, audit / registry context
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action server_info

# Re-source the --env.file credentials file and report per-provider
# credential status - a good first diagnostic when queries fail on auth
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action reload_credentials

# List providers already pulled into the local cache
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action list_providers

# List providers (and versions) available in the registry
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action list_registry

# Install a provider from the registry into the local cache
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action pull_provider \
  --exec.args '{"provider": "kafka"}'

# List services for a provider
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action list_services \
  --exec.args '{"provider": "google"}'

# List access methods for a resource (this is how an agent finds required
# WHERE parameters before writing a query)
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action list_methods \
  --exec.args '{"provider": "google", "service": "compute", "resource": "networks"}'

# Validate a SELECT without executing
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action validate_select_query \
  --exec.args '{"sql": "select name from google.compute.networks where project = '"'"'my-project'"'"';"}'

# Execute a SELECT
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action run_select_query \
  --exec.args '{"sql": "select name from google.compute.networks where project = '"'"'my-project'"'"';"}'

# Mutation (a DELETE) - succeeds only against a full_access server:
# stackql_mcp_client does not support elicitation, so safe and delete_safe
# servers refuse it.
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action run_mutation_query \
  --exec.args '{"sql": "delete from google.compute.firewalls where project = '"'"'my-project'"'"' and firewall = '"'"'old-rule'"'"';"}'
```

#### レスポンスの例 {#example-responses}

`server_info`（デフォルトモードのサーバーに対して。Windows 上の StackQL `v1.0.888`、stdio 経由）：

```json
{
  "version": "1.0.888",
  "commit": "f7b4116",
  "build_date": "2026-11-05T00:26:03Z",
  "platform": "Windows",
  "transport": "stdio",
  "sql_backend": "sqlite3",
  "provider_registry": "https://registry.stackql.app/providers",
  "mode": "safe",
  "is_read_only": false
}
```

`run_select_query`：

```json
{
  "rows": [
    {"name": "default"},
    {"name": "pathfinders-test-01"},
    {"name": "returning-test-03"}
  ]
}
```

`run_mutation_query`（`full_access` サーバーに対して）：

```json
{
  "messages": ["The operation was despatched successfully"],
  "timestamp": "2026-05-16T10:47:33+10:00 AEST"
}
```

同じミューテーションの監査ログ行の例：

```json
{"timestamp":"2026-05-16T00:47:33Z","tool":"run_mutation_query","mode":"full_access","decision":"allow","query_class":"mutation_delete","sql":"delete from google.compute.firewalls where project = 'my-project' and firewall = 'old-rule';","args":{"sql":"delete from google.compute.firewalls where project = 'my-project' and firewall = 'old-rule';","row_limit":0},"duration_ms":42}
```

* * *

### AI アシスタントとの統合 {#integration-with-ai-assistants}

StackQL の MCP サーバーを AI アシスタントと統合するには、アシスタントの設定で `stackql` を MCP サーバーとして登録します。エディターに組み込まれたほとんどの MCP クライアントは、`stdio` 経由でサーバーを実行します。その場合は `--mcp.server.type=stdio` を使用し、アシスタントがプロセスを直接起動します。HTTP に対応したスタンドアロンのエージェントは、長時間実行されている `stackql mcp --mcp.server.type=http` プロセスに接続できます。

Claude Desktop については、Anthropic Connector Directory への掲載（推奨されるインストール方法）、ダウンロード可能な MCP Bundle（`.mcpb`）、手動設定を含め、[Claude Desktop で StackQL を使用する](/getting-started/claude-desktop)を参照してください。

:::note

設定ファイルの場所は、オペレーティングシステムと AI アシスタントによって異なります。正しいパスと形式については、AI アシスタントのドキュメントを参照してください。

:::

* * *

### アーキテクチャに関する考慮事項 {#architecture-considerations}

デプロイモードを選択する際は、次の点を考慮してください。

1. **スタンドアロン（`stackql mcp`）**：最もシンプルな構成で、開発や、`stdio` 経由のエディター組み込みクライアントに最適です。
2. **インメモリのデュアルサーバー（`mcp.server.type=http` を指定した `stackql srv`）**：1 つのプロセスで MCP と PostgreSQL ワイヤプロトコルの両方を提供します。最高のパフォーマンスが得られ、ほとんどの本番デプロイに適しています。
3. **リバースプロキシ（`mcp.server.type=reverse_proxy` を指定した `stackql srv`）**：
   - プロセスやホストをまたいだワークロードの分離が可能
   - セキュアな MCP エンドポイントのための TLS 暗号化をサポート
   - MCP と PostgreSQL のインターフェイスを個別にスケーリング可能
   - エンタープライズ CA との統合に柔軟に対応（注：エンタープライズ CA のサポートは実験的機能）

また、安全性については次の点を考慮してください。

- デフォルトの `mode: safe` から始めてください。エリシテーションに対応したクライアント（Claude Desktop、Cursor、Continue）は、ミューテーションの前にユーザーに確認を求めます。
- 書き込みを行うべきでないインベントリ用や分析用のエージェントには、`mode: read_only` を固定します。
- 作成や更新は自由に行いたいが、破壊的な操作には人間の確認を挟みたい場合は、`mode: delete_safe` を固定します。
- `mode: full_access` は信頼できる自動化パイプライン専用とし、誰かがレビューする監査ログと組み合わせる場合にのみ使用します。
- 他のプロセスやホストから到達できる HTTP リスナーには、`server.auth_token_env_var` を設定します。`allow_unauthenticated` はローカル開発用です。TLS だけではクライアントは認証されません。

:::caution

エンタープライズ認証局（CA）で TLS を使用する場合は、追加の設定が必要になることがあります。この機能は実験的なもので、使用する CA の実装に応じて調整が必要になる場合があります。

:::

* * *

### 参考資料 {#further-reading}

- パッケージ開発者向けのリファレンスは、stackql リポジトリの [`pkg/mcp_server` README](https://github.com/stackql/stackql/blob/main/pkg/mcp_server/README.md) です。
- コントリビューター向けの技術ドキュメントは stackql リポジトリの [`docs/mcp.md`](https://github.com/stackql/stackql/blob/main/docs/mcp.md) にあり、すべてのツールについてのレスポンス例が含まれています。
