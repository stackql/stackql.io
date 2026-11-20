---
title: グローバルフラグ
hide_title: false
hide_table_of_contents: true
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "stackql バイナリのグローバルフラグ：認証、出力形式、レジストリの場所、ログ、SQL バックエンドの設定で、すべてのコマンドで利用できます。"
image: "/img/stackql-featured-image.png"
---

グローバルフラグは StackQL のコマンドで受け付けられますが、一部のフラグは特定のモードでのみ適用されます。コマンドラインで設定するか、サポートされている場合は StackQL の初期化ファイル（[`.stackqlrc`](/getting-started/using-stackql#the-stackqlrc-initialization-file)）で設定します。`--pgsrv.*` フラグは [PostgreSQL サーバーオプション](/command-line-usage/srv#server-options)に記載されています。

| フラグ | データ型 | 説明 | デフォルト |
| -- | -- | -- | -- |
| <span class="nowrap">`--access.config`</span> | string | 呼び出し元のアクセス設定。JSON エンベロープ `{"path": "/path/to/access.json"}` で指定します（ファイルパスをそのまま指定することもできます）。[アクセス管理](/command-line-usage/access-management)を有効にします。呼び出し元は JWT、静的 Bearer トークン、相互 TLS、ローカル OS ID のいずれかで認証され、文はポリシールールに照らして認可されます。ローカルのトークン認証情報は `STACKQL_CALLER_TOKEN` から読み込まれます。StackQL `v1.0` 以降で利用できます。 | |
| <span class="nowrap">`--allow-persistent-secrets`</span> | flag | セッションが `SET "$.secret_store"` で永続的な OS シークレットストア（macOS Keychain、Linux Secret Service、Windows Credential Manager）を選択できるようにします。フラットファイルのストアは、このフラグがあっても拒否されます。[プロバイダーセッションとシークレット](/getting-started/provider-sessions#persistent-os-secret-stores)を参照してください。StackQL `v1.0` 以降で利用できます。 | `false` |
| <span class="nowrap">`--allow-secret-dereference`</span> | flag | SQL 式でシークレットと認証情報の値を参照解決できるようにします（`"$secrets.name"`、`"$auth.provider.field"`）。その結果、これらの値が結果、ログ、永続化されたクエリのアーティファクトに含まれる可能性があります。通常のプロバイダー認証には必要ありません。[SQL でのシークレット値](/getting-started/provider-sessions#secret-values-in-sql)を参照してください。StackQL `v1.0` 以降で利用できます。 | `false` |
| <span class="nowrap">`--apirequesttimeout`</span> | integer | API リクエストのタイムアウト（秒）。0 でタイムアウトなし | `45` |
| <span class="nowrap">`--approot`</span> | string | アプリケーションの設定とキャッシュのルートパス | `{cwd}/.stackql` |
| <span class="nowrap">`--approotfilemode`</span> | uint32 | アプリケーションの設定とキャッシュのファイルモード | `493` |
| <span class="nowrap">`--auth`</span> | string | JSON 形式の `auth` コンテキストのキーと値のペア |  |
| <span class="nowrap">`--cachekeycount`</span> | integer | キャッシュの初期キー数 | `100` |
| <span class="nowrap">`--configfile`</span> | string | 設定ファイルのフルパス | `{cwd}/.stackqlrc` |
| <span class="nowrap">`--cpuprofile`</span> | string | `cpuprofile` ファイル。空の場合はなし | `null` |
| <span class="nowrap">`--dataflow.dependency.max`</span> | int | 1 つのクエリにおけるデータフロー依存関係の最大深度 | `50` |
| <span class="nowrap">`--dataflow.components.max`</span> | int | 1 つのクエリにおけるデータフローの弱連結成分の最大数 | `50` |
| <span class="nowrap">`--dbInternal`</span> | string | DBMS のハウスキーピングクエリの処理を設定する JSON / YAML 文字列 | `{}` |
| <span class="nowrap">`-d`, `--delimiter`</span> | string | CSV 出力の区切り文字。1 文字のみ。CSV 以外の出力ではすべて無視されます | `,` |
| <span class="nowrap">`--dryrun`</span> | flag | `dryrun` フラグ。プリプロセッサーのみを実行し、テンプレート処理された出力が返されます | `false` |
| <span class="nowrap">`--env.file`</span> | string | 起動時（すべてのコマンド）にプロセスの環境に読み込まれる、省略可能な dotenv 形式のファイル。MCP の [`reload_credentials`](/mcp/reload_credentials) ツールは、要求に応じてこのファイルを再読み込みします。ファイルがなくてもエラーにはなりません。値が空のキーはスキップされ、既存の変数は上書きされます。`.stackqlrc` からは読み込まれません。StackQL `v0.10.542` より後のリリースで利用できます。 | `null` |
| <span class="nowrap">`--execution.concurrency.limit`</span> | int | クエリ実行の同時実行数の上限 | `1` |
| <span class="nowrap">`--export.alias`</span> | string | エクスポートのエイリアスのプレフィックス（名前空間またはスキーマ） | |
| <span class="nowrap">`--gc`</span> | string | GC の設定を表す JSON / YAML 文字列 | `{}` |
| <span class="nowrap">`-H`, `--hideheaders`</span> | flag | 列ヘッダーを無効にします。`--output csv` または `--output text` でのみ有効です | `false` |
| <span class="nowrap">`--http.log.enabled`</span> | flag | HTTP リクエストの情報をターミナルに表示します | `false` |
| <span class="nowrap">`--http.proxy.host`</span> | string | プロキシホスト（プロキシを使用しない場合は空のまま） | `null` |
| <span class="nowrap">`--http.proxy.password`</span> | string | プロキシのパスワード | `null` |
| <span class="nowrap">`--http.proxy.port`</span> | integer | プロキシのポート。0 以下の数値を指定すると、そのスキームのデフォルトポートになります（たとえば `http -> 80`） | `-1` |
| <span class="nowrap">`--http.proxy.scheme`</span> | string | プロキシのスキーム。`http` または `https` | `http` |
| <span class="nowrap">`--http.proxy.user`</span> | string | プロキシのユーザー | `null` |
| <span class="nowrap">`--http.response.maxResults`</span> | integer | HTTP リクエストあたりの最大結果数。0 以下の数値を指定すると無制限になります | `-1` |
| <span class="nowrap">`--http.response.pageLimit`</span> | integer | リソースごとに返される結果の最大ページ数。0 以下の数値を指定すると無制限になります | `20` |
| <span class="nowrap">`--indirect.depth.max`</span> | integer | 間接クエリ（ビューとサブクエリ）の最大深度 | `5` |
| <span class="nowrap">`-i`, `--infile`</span> | string | クエリの読み込み元となる入力ファイル（IQL ファイル） | `{stdin}` |
| <span class="nowrap">`-q`, `--iqldata`</span> | string | テンプレート機能用のコンテキスト（データ）ファイル（`json` または `jsonnet` ファイル）| |
| <span class="nowrap">`--loglevel`</span> | string | ログレベル。\{`panic`, `fatal`, `error`, `warn`, `info`, `debug`, `trace`\} のいずれかである必要があります（`warning` は `warn` のエイリアスとして受け付けます） | `fatal` |
| <span class="nowrap">`--mcp.config`</span> | string | [MCP サーバー](/command-line-usage/mcp#mcp-configuration-object)（`stackql mcp` と `stackql srv`）の JSON / YAML 設定オブジェクト | `{}` |
| <span class="nowrap">`--mcp.log.format`</span> | string | MCP 監査ログのエンコーディング。`jsonl` または `otel`（OTLP/JSON のログレコード）です。`--mcp.config` の `server.audit.format` を上書きします。[ログ形式](/command-line-usage/mcp#log-format)を参照してください。StackQL `v0.11.660` 以降で利用できます。 | `jsonl` |
| <span class="nowrap">`--mcp.protocol.version`</span> | string | 公開する最新の MCP プロトコルリビジョン。`auto`、`2026-07-28`（セッションレスのみ）、または `2025-11-25` などの古いリビジョンです。`--mcp.config` の `server.protocol_version` を上書きします。[リビジョンの固定](/command-line-usage/mcp#pinning-the-revision)を参照してください。StackQL `v0.12.732` 以降で利用できます。 | `auto` |
| <span class="nowrap">`--mcp.server.type`</span> | string | MCP サーバータイプ。\{`http`, `stdio`, `reverse_proxy`\} のいずれかです。[MCP サーバータイプ](/command-line-usage/mcp#mcp-server-type)を参照してください。 | `http` |
| <span class="nowrap">`--metadatattl`</span> | integer | キャッシュされたメタデータドキュメントの TTL（秒） | `3600` |
| <span class="nowrap">`--namespaces`</span> | string | キャッシュ、ビューなどの名前空間を表す JSON / YAML 文字列 | `{}` |
| <span class="nowrap">`--offline`</span> | flag | キャッシュされたデータを使用してオフラインで動作します | `false` |
| <span class="nowrap">`-f`, `--outfile`</span> | string | 結果を書き込む出力ファイル | `{stdout}` |
| <span class="nowrap">`-o`, `--output`</span> | string | 出力形式。\{`json`, `jsonl`, `table`, `csv`, `text`, `otel`\} のいずれかである必要があります（`ndjson` は `jsonl` のエイリアスとして受け付けます）。`otel` は OpenTelemetry のログレコードを出力します。[出力モード](/getting-started/output-modes#otel-output)を参照してください。StackQL `v0.11.660` 以降で利用できます。`--otel.config` でエクスポーターを設定すると、`otel` のレコードはそのエンドポイントにもプッシュされます | `table` |
| <span class="nowrap">`--otel.config`</span> | string | `otel` 出力形式を設定する JSON / YAML 文字列。`exporter.endpoint`、`exporter.headers`、`exporter.timeout_ms`、`exporter.batch_size` で、レコードを OTLP/HTTP のログエンドポイントにプッシュします。[OTLP エンドポイントへのプッシュ](/getting-started/output-modes#pushing-to-an-otlp-endpoint)を参照してください。StackQL `v0.12.732` 以降で利用できます | `{}` |
| <span class="nowrap">`--preview`</span> | string | プレビュー版の機能を有効にする JSON 文字列。`{"unstable": true}` は、ドキュメント駆動の `stackql_unstable_<provider>.<service>.<resource>` リレーションを公開します。これらのリレーションは、`omnisdk` によってプロバイダードキュメントから直接解決されて実行され、行は生成されるそばからストリーミングされます。残りのキー（`batchSize`、`flushInterval`、`endpoint`、`insecureSkipTLSVerify`）は、そのバックエンドを調整します。プレビュー版の機能は安定していません。動作、構文、出力はリリース間で予告なく変更されたり動作しなくなったりする可能性があるため、本番環境ではこれに依存しないでください。`v1.0` 以降では、同じオプトインによって `stackql_preview.iac.run` リレーションと `stackql_preview.iac.destroy` リレーションも公開されます。これらは `omnisdk` を通じて宣言されたリソースセットを適用および破棄し、ステートはアプリケーションルートの下に保持されます。[プレビュー版の機能](https://github.com/stackql/stackql/blob/main/docs/preview.md)を参照してください | `{}` |
| <span class="nowrap">`--provider`</span> | string | 初期のデフォルトプロバイダー。たとえば `--provider github` を指定すると、事前に `USE` 文を実行しなくても `repos.repos` が `github.repos.repos` に解決されます。プロバイダーへの認証は行いません。 | なし |
| <span class="nowrap">`--querycachesize`</span> | integer | クエリ計画の LRU キャッシュのサイズ（エントリ数） | `10000` |
| <span class="nowrap">`--registry`</span> | string | プロバイダーレジストリの場所と、プロバイダードキュメントの署名検証を設定する JSON / YAML 文字列 | [`--registry`](#--registry) を参照 |
| <span class="nowrap">`--session`</span> | string | セッションの設定を表す JSON / YAML 文字列 | `{}` |
| <span class="nowrap">`--sqlBackend`</span> | string | SQL バックエンドシステムの設定を表す JSON / YAML 文字列 | [`--sqlBackend`](#--sqlbackend) を参照 |
| <span class="nowrap">`--store.txn`</span> | string | Txn ストアの設定を表す JSON / YAML 文字列 | `{}` |
| <span class="nowrap">`--tls.CABundle`</span> | string | CA バンドルへのパス。指定しない場合はシステムのデフォルトが使用されます |  |
| <span class="nowrap">`--tls.allowInsecure`</span> | flag | 安全でない証明書の信頼を許可します（非推奨） |  |
| <span class="nowrap">`--upstream.errors`</span> | string | プロバイダーの読み取りで HTTP レスポンスステータスを明示的に許容するための JSON / YAML ポリシー。[`--upstream.errors`](#--upstream-errors) を参照してください。 | `{}` |
| <span class="nowrap">`--var`</span> | string | `jsonnet` データファイルを通じて StackQL クエリに取り込むための、キーと値のペアとして指定する外部変数 |  |
| <span class="nowrap">`-v`, `--verbose`</span> | flag | 詳細出力 | `false` |
| <span class="nowrap">`-h`, `--help`</span> | | stackql のコンテキストに応じたヘルプ | |
| <span class="nowrap">`--version`</span> | | StackQL プログラムのバージョンを表示します | |

`--colorscheme` は非推奨で、シェルの色を変更しなくなりました。`--usenonpreferredapis` は受け付けられますが、現在のエンジンでは API の選択を変更しません。

## `--registry` {#--registry}

`--registry` フラグは、プロバイダードキュメントの読み込み元と、その署名を検証するかどうかを設定します。値は、次のキーを持つ JSON 文字列です：

| キー | 説明 | デフォルト |
| -- | -- | -- |
| `url` | レジストリの URL。省略した場合は、StackQL の公開プロバイダーレジストリが使用されます。 | 公開レジストリ |
| `localDocRoot` | プロバイダードキュメントを格納するローカルディレクトリ。レジストリからダウンロードする代わりに、ここから読み込みます。[プロバイダーを開発する](/developers/developing-stackql-providers#testing-a-provider-locally)際に使用します。 | |
| `srcPrefix` | プロバイダーのソースドキュメントが置かれている、レジストリルート配下のパスプレフィックス。 | `src` |
| `verifyConfig.nopVerify` | `true` の場合は署名の検証をスキップします。`false` に設定すると、読み込まれるすべてのプロバイダードキュメントとサービスドキュメントに、組み込みの ML-DSA-65（FIPS 204）署名が必要になります。ドキュメントに基づくプレビュー版のリレーションも対象です。 | `true` |
| `verifyConfig.publicKeys` | プライベートレジストリ用に、キー ID を 16 進エンコードされた ML-DSA-65 公開鍵にマッピングする JSON オブジェクト。組み込みの本番用キーを置き換えます。明示的な空のオブジェクトはエラーになります。 | 組み込みのキー |

### 例：署名付きプロバイダードキュメントを必須にする {#example-require-signed-provider-documents}

公開レジストリから読み込まれるすべてのプロバイダードキュメントとサービスドキュメントについて、検証を有効にします。検証には、組み込みの本番用キーが使用されます：

```bash
stackql --registry '{"verifyConfig": {"nopVerify": false}}' shell
```

### 例：ローカルディレクトリから署名なしのドキュメントを読み込む {#example-read-unsigned-documents-from-a-local-directory}

レジストリの参照先を、開発中のプロバイダードキュメントのディレクトリにします。これらのドキュメントは署名されていないため、検証は無効のままにします：

```bash
stackql --registry '{"localDocRoot": "/path/to/provider-dev/openapi", "verifyConfig": {"nopVerify": true}}' shell
```

:::note

署名の検証は StackQL `v1.0` 以降で利用でき、デフォルトでは無効です。署名はドキュメントの YAML のバイト列そのものを対象とするため、書式や改行の変更を含め、どのような編集でも署名は無効になります。レジストリの `pqsig sign-doc` ツールで再署名してください。署名されていないローカルの開発用ドキュメントでは、`nopVerify: true` のままにする必要があります。以前の証明書オプションと `.sig` サイドカーファイルは使用されなくなりました。サイドカーのみで公開されたアーカイブは、検証付きで使用する前に再公開する必要があります。検証は、読み込み時にドキュメントを認証します。アーカイブのダウンロードと展開は対象外です。

:::

## `--sqlBackend` {#--sqlbackend}

`--sqlBackend` フラグは StackQL の外部 SQL バックエンドを設定し、デフォルトの組み込み SQL エンジンの代わりに、外部のデータベースエンジン（PostgreSQL など）を使用できるようにします。これは、クエリ処理、永続ストレージ、既存のデータベースインフラストラクチャとの統合に外部データベースを活用する場合に便利です。

値は、次のキーを持つ JSON 文字列です：

| キー | 説明 | 値の例 |
| -- | -- | -- |
| `dbEngine` | データベースエンジンの種類 | `postgres_tcp` |
| `sqlDialect` | 使用する SQL 方言 | `postgres` |
| `dsn` | データベースのデータソース名（接続文字列） | 下記の例を参照 |

### 例：PostgreSQL バックエンドの使用 {#example-using-a-postgresql-backend}

```bash
./stackql \
  --sqlBackend='{"dbEngine": "postgres_tcp", "sqlDialect": "postgres", "dsn": "postgres://user:password@hostname:port/dbname?sslmode=require"}' \
  shell
```

### 例：Databricks の PostgreSQL インターフェイスの使用 {#example-using-databricks-postgresql-interface}

Databricks は、StackQL の SQL バックエンドとして使用できる PostgreSQL 互換のインターフェイスを公開しています。DSN には、Databricks の認証情報を含む標準の PostgreSQL 接続文字列形式を使用します：

```bash
./stackql \
  --sqlBackend='{"dbEngine": "postgres_tcp", "sqlDialect": "postgres", "dsn": "postgres://user%40domain.com:your-access-token@your-databricks-host.cloud.databricks.com/databricks_postgres?sslmode=require"}' \
  shell
```

<br/>

:::note
ユーザー名またはパスワードに含まれる特殊文字（`@` など）は、DSN 内で URL エンコードする必要があります（たとえば、`@` は `%40` になります）。
:::

## `--upstream.errors` {#--upstream-errors}

`http.ignoreStatusCodes` には、プロバイダーの読み取りで明示的に許容する HTTP レスポンスステータスを列挙します。各エントリは文字列です。`"503"` のような正確なコード、または `"5xx"` のようなクラスを指定します。デフォルトの `{}` では、アップストリームのレスポンスに対するエンジンの通常の処理に例外は追加されません。この設定は、失敗したリクエストを再試行せず、ミューテーションや認証のやり取りにも適用されません。

無視されたレスポンスからは行が返されません。クエリ内のほかの読み取りは引き続き行を返すことができるため、成功したクエリが不完全な結果を返す場合があります。StackQL は、無視された HTTP ステータスを診断情報として報告します。

たとえば、[GitHub のモックを使用した結合テスト](https://github.com/stackql/stackql/blob/256187fab2410b7c999e7208bd888b12c5df9b9e/test/robot/functional/stackql_mocked_from_cmd_line.robot#L2104)では、このオプションを使用しています：

```bash
stackql exec --upstream.errors='{"http":{"ignoreStatusCodes":["5xx"]}}' \
  "SELECT r.name, c.login FROM github.repos.repos r
   JOIN github.repos.collaborators c ON c.repo = r.name
   WHERE r.org = 'example-org' AND c.owner = 'example-org'
   ORDER BY r.name;"
```

このテストでは、1 つのリポジトリに対するコラボレーターのリクエストが HTTP `500` を返します。それでもクエリはほかのリポジトリのコラボレーターの行を返し、stderr には `ignoring upstream response: 500 INTERNAL SERVER ERROR` が含まれます。想定される失敗が 1 つだけの場合は、正確なステータスコードを使用することを推奨します。
