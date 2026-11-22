---
title: srv
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "stackql srv コマンドは StackQL を PostgreSQL ワイヤプロトコルサーバーとして実行し、psql、BI ツール、言語ドライバーから SQL でクラウドや SaaS の API をクエリできるようにします。"
image: "/img/stackql-featured-image.png"
---

StackQL をサービスとして起動するためのコマンドです。起動したサービスには [Postgres ワイヤプロトコル](https://www.postgresql.org/docs/current/protocol.html)クライアントからアクセスでき、クライアントは mTLS で認証できます。`srv` コマンドは、PostgreSQL ワイヤプロトコルサーバーと並行して [Model Context Protocol（MCP）](/command-line-usage/mcp)サーバーを実行することもできます。これにより、従来のデータベースクライアントと AI エージェントの両方からのデュアルプロトコルアクセスが可能になります。

* * * 

### 構文 {#syntax}

`stackql srv [server_options] [flags]`

* * *

:::note

StackQL サーバーは Postgres ワイヤプロトコルを使用しますが、Postgres サーバーではなく、Postgres サーバーを必要ともしません。ワイヤプロトコルは、クライアントがサーバーと通信するために使用されます。サーバーは StackQL を使用して、リモートのクラウドベンダーや SaaS ベンダーに対してクエリや DML 操作を実行し、結果をクライアントに返します。

:::

拡張クエリプロトコル（`psycopg` 3、`pgx`、JDBC が使用する `Parse` / `Bind` / `Execute`）で送信されたパラメーターは、サーバー側で文字列リテラルとしてバインドされます。`v0.12.742` 以降では、文字列リテラル、引用符で囲まれた識別子、コメントの中にある `$n` プレースホルダーはそのまま残され、パラメーター値内のバックスラッシュはエスケープされます。また、テキスト値の `NULL` は文字列 `NULL` のままです（SQL の `NULL` は別途伝達されます）。

### サーバーオプション {#server-options}

| オプション | 説明 | デフォルト |
|--|--|--|
|<span class="nowrap">`--pgsrv.address`</span>|サーバーがリッスンするアドレス。|`0.0.0.0`|
|<span class="nowrap">`--pgsrv.port`</span>|サーバーがリッスンする TCP ポート。|`5466`|
|<span class="nowrap">`--pgsrv.tls`</span>|JSON の TLS 設定オブジェクト。下記の [mTLS の例](#examples)を参照してください。|なし|
|<span class="nowrap">`--pgsrv.debug.enable`</span>|PostgreSQL クライアント向けのクエリのデバッグ出力を有効にします。たとえば `stackql srv --pgsrv.debug.enable=true` のように指定します。クライアントにデバッグ情報を渡す必要がある場合を除き、無効のままにしてください。|`false`|
|<span class="nowrap">`--pgsrv.sundry`</span>|`--pgsrv.tls` が設定されていない場合にのみ PostgreSQL ワイヤプロトコルサーバーに渡される、高度な JSON または YAML のマッピング。|`{}`|
|<span class="nowrap">`--pgsrv.loglevel`</span>|受け付けられますが、現在のエンジンではロガーのレベルを設定しません。代わりに `--loglevel` を使用してください。|`WARN`|

<br/>

> その他のオプションについては、[グローバルフラグ](/command-line-usage/global-flags)を参照してください

### MCP サーバーオプション {#mcp-server-options}

`srv` コマンドを MCP サポート付きで実行する場合、次の追加オプションを使用できます。

| オプション | 説明 |
|--|--|
|<span class="nowrap">`--mcp.server.type`</span>|MCP サーバーの種類：`http`（インメモリ）または `reverse_proxy`（TCP ベース）|
|<span class="nowrap">`--mcp.config`</span>|MCP サーバーの JSON 設定オブジェクト（[MCP ドキュメント](/command-line-usage/mcp)を参照）|
|<span class="nowrap">`--mcp.log.format`</span>|MCP 監査ログのエンコーディング：`jsonl`（デフォルト）または `otel`（OTLP/JSON ログレコード）。設定オブジェクトの `server.audit.format` を上書きする|
|<span class="nowrap">`--mcp.protocol.version`</span>|公開する最新の MCP プロトコルリビジョン：`auto`（デフォルト）、`2026-07-28`（セッションレスのみ）、または `2025-11-25` などの古いリビジョン。設定オブジェクトの `server.protocol_version` を上書きする|

<br/>

:::info[v0.12.742 での破壊的変更]

`v0.12.742` 以降、MCP HTTP トランスポート（`--mcp.server.type=http` または `reverse_proxy`）は、設定オブジェクトが `server.auth_token_env_var` で Bearer トークンを指定するか、`"allow_unauthenticated": true` で明示的にオプトアウトしない限り、起動を拒否します。[HTTP クライアント認証](/command-line-usage/mcp#http-client-authentication)を参照してください。

:::

:::tip

デプロイモード、設定オプション、AI アシスタントとの統合など、MCP サーバーの設定と使用に関する詳細については、[MCP コマンドのドキュメント](/command-line-usage/mcp)を参照してください。

:::

:::info

サーバーを起動する前に、プロバイダーの認証に必要な環境変数を設定してください。詳細については、[プロバイダーの使用](/getting-started/using-a-provider)を参照してください。

:::

長時間稼働するサーバーでは、[`--env.file`](/command-line-usage/global-flags) フラグを使用すると、変数をシェルにエクスポートする代わりに、起動前にディスク上の dotenv 形式のファイルに認証情報を配置したりローテーションしたりできます。

```bash
stackql srv --pgsrv.port 5444 --env.file /etc/stackql/credentials.env
```

`stackql srv` では、このファイルは起動時に 1 回だけ読み込まれます。セッションの途中で再読み込みする手段は、MCP の [`reload_credentials`](/mcp/reload_credentials) ツールです。

### フラグ {#flags}

| フラグ | 説明 |
|--|--|
|`-h,--help`|ヘルプ情報を表示する|
|`-v,--verbose`|詳細モードでクエリを実行し、追加の出力を stdout に送信します。グローバルフラグ `-f`（`--outfile`）を設定した場合、<br/>この追加のログ情報はクエリ結果とともに出力ファイルに書き込まれます。|

<br/>

> その他のオプションについては、[グローバルフラグ](/command-line-usage/global-flags)を参照してください

* * *

### 例 {#examples}

StackQL サーバープロセスを起動し、`psql` コマンドラインクライアントを使用して接続します。  

まず、[stackql/stackql](https://raw.githubusercontent.com/stackql/stackql/main/test/server/mtls/openssl.cnf) から `openssl.cnf` 設定ファイルをダウンロードします。  

次に、相互 TLS 認証（mTLS）のためにサーバーとクライアントが使用する証明書ファイルとキーファイルを生成します。  

```bash
openssl req -x509 -keyout ~/stackqlcreds/server_key.pem -out ~/stackqlcreds/server_cert.pem -config ./openssl.cnf -days 365
openssl req -x509 -keyout ~/stackqlcreds/client_key.pem -out ~/stackqlcreds/client_cert.pem -config ./openssl.cnf -days 365
chmod 400 ~/stackqlcreds/client_key.pem
```

続いて、環境変数を設定します。クライアントとサーバーが同じホスト上にある場合、これらは共有できます。  

```bash
export PGPORT=5444
export PGSSLCERT=~/stackqlcreds/client_cert.pem
export PGSSLKEY=~/stackqlcreds/client_key.pem
export PGSSLROOTCERT=~/stackqlcreds/server_cert.pem
export PGSSLSRVKEY=~/stackqlcreds/server_key.pem
export CLIENT_CERT=$(base64 -w 0 ~/stackqlcreds/client_cert.pem)
export PGSSLMODE=allow
```

サーバーが必要とするプロバイダーの認証情報を設定します。GitHub の場合、プロバイダーは次の 2 つの変数からユーザー名と個人用アクセストークンを読み込みます：  

```bash
export STACKQL_GITHUB_USERNAME=youruser
export STACKQL_GITHUB_PASSWORD=ghp_youraccesstoken
```

次に、1 つのターミナルでサーバーを起動します。  

```bash
stackql srv \
--pgsrv.address=0.0.0.0 \
--pgsrv.port=$PGPORT \
--pgsrv.tls='{ "keyFilePath": "'${PGSSLSRVKEY}'", "certFilePath": "'${PGSSLROOTCERT}'", "clientCAs": [ "'${CLIENT_CERT}'" ] }'
```

別のターミナルで、`psql` コマンドラインクライアントを使用してサーバーに接続します。  

```bash
psql -h 127.0.0.1 -d stackql
```

次に、認証済みのプロバイダーに対して StackQL クエリを実行します。  

```
psql (12.11 (Ubuntu 12.11-0ubuntu0.20.04.1), server 0.0.0)
Type "help" for help.

stackql=> SHOW SERVICES IN github LIKE '%repos%';
      id      | name  |           title
--------------+-------+----------------------------
 repos:v0.3.3 | repos | GitHub v3 REST API - repos
(1 row)

stackql=>
```
