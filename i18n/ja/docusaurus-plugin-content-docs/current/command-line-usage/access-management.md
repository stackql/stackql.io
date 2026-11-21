---
title: アクセス管理
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - アクセス管理
  - 認証
  - 認可
  - jwt
  - mtls
  - Bearer トークン
description: "StackQL のオプトイン方式の呼び出し元アクセス管理：JWT、Bearer トークン、相互 TLS、ローカル OS ID で呼び出し元を認証し、ディレクトリでプリンシパルとグループにマッピングし、ポリシールールで文を認可します。"
image: "/img/stackql-featured-image.png"
---

呼び出し元のアクセス管理は、StackQL セッションの呼び出し元を認証し、各文をポリシーに照らして認可します。これはプロバイダーの認証情報とは独立しています。プロバイダーの認証情報は、引き続き `--auth`、環境変数、または[プロバイダーセッション](/getting-started/provider-sessions)を通じて指定します。デフォルトでは無効になっており、有効にしないデプロイはこれまでどおりに動作します。StackQL `v1.0` 以降で利用できます。

関連項目：  
[[ グローバルフラグ ]](/command-line-usage/global-flags) [[` mcp `]](/command-line-usage/mcp) [[` whoami `]](/language-spec/functions/session/whoami) [[` groups `]](/language-spec/functions/session/groups)

* * * 

## アクセス管理の有効化 {#enabling-access-management}

アクセス管理は、JSON 設定ファイルを指す `--access.config` グローバルフラグを付けて StackQL を起動すると有効になります。次の例では、保護モードで対話型シェルを起動します。`exec` と `mcp` も同じ方法で有効にします。

```bash
stackql --access.config '{"path": "/etc/stackql/access.json"}' shell
```

`--access.config` は、`path` キーを 1 つだけ持つ JSON エンベロープを受け取ります。ファイルパスをそのまま指定することもできます。設定は起動時に 1 回だけ読み込まれるため、キーやポリシーのローテーションは計画的な再起動によって行ってください。

アクセス管理が有効な間は、`exec`、`shell`、`mcp`、`version` だけが実行されます。`registry` を含むほかのコマンドは、保護されたパスを迂回せずに実行を拒否します。`srv` と MCP の `reverse_proxy` サーバータイプも拒否します。PostgreSQL ワイヤプロトコルの依存関係が、接続ごとの認証の証跡をまだ公開していないためです。SQL の `AUTH` 文と `AUTH REVOKE` 文は、保護モードでは拒否されます。代わりに、ホストの設定を通じてプロバイダーの認証情報をプロビジョニングしてください。保護された `exec` が失敗すると、エラーを表示した後に終了ステータス `1` を返します。

## 認証モード {#authentication-modes}

設定ファイルでは、`jwt`（省略時のデフォルト）、`bearer`、`mtls`、`local_os` のいずれか 1 つの `type` を選択します。設定は厳密な JSON です。不明なフィールドや、別のモードに属するフィールドは拒否されます。すべてのモードが、同じ `rules` と同じ[プリンシパルディレクトリ](#principal-directory)を共有します。

### JWT {#jwt}

呼び出し元は署名付きトークンを提示し、トークンは設定に固定されたキーに照らしてオフラインで検証されます。最小限の設定では、トークンが持つ必要のある発行者とオーディエンスを指定し、公開鍵を 1 つ列挙し、`analysts` グループのメンバーが読み取り文を実行できるようにします（`readers` グループのメンバーはルールなしで実行できます。[ポリシールール](#policy-rules)を参照してください）：

```json
{
  "type": "jwt",
  "issuer": "https://identity.example",
  "audience": "stackql",
  "keys": [
    {"kid": "primary", "algorithm": "EdDSA", "public_key_pem": "-----BEGIN PUBLIC KEY-----\n...\n-----END PUBLIC KEY-----\n"}
  ],
  "rules": [
    {"subject": "group:analysts", "resource": "host/sql/read", "action": "execute"}
  ]
}
```

発行者、オーディエンス、公開鍵は設定に固定されており、OIDC ディスカバリはありません。受け入れられるアルゴリズムは `EdDSA`、`RS256`、`RS384`、`RS512`、`PS256`、`PS384`、`PS512`、`ES256`、`ES384`、`ES512` で、RSA キーは 2048 ビット以上である必要があります。各キーは、設定されたアルゴリズムにバインドされます。不明なキー ID、無効な署名、有効期限の欠落または経過、未来の `nbf` または `iat`、誤った発行者またはオーディエンスを持つトークンは拒否されます。トークンに含まれるグループクレームと URL は、信頼の判断にもディレクトリのメンバーシップにも使用されません。

### 静的 Bearer トークン {#static-bearer-token}

呼び出し元は不透明なトークンを提示し、設定には、受け入れる各トークンの SHA-256 ダイジェストと、そのトークンが識別するサブジェクトを列挙します。トークン発行者を運用するほどではない、少数の固定されたサービス ID に適しています：

```json
{
  "type": "bearer",
  "issuer": "local-tokens",
  "tokens": [{"subject": "alice", "sha256": "<64 hexadecimal characters: the SHA-256 of the token>"}],
  "rules": [{"subject": "group:analysts", "resource": "host/sql/read", "action": "execute"}]
}
```

設定するのは各トークンの SHA-256 ダイジェストだけで、ダイジェストは一意である必要があります。設定されたダイジェストを変更するには再起動が必要です。

### 相互 TLS {#mutual-tls}

呼び出し元は、HTTPS の MCP トランスポートで提示するクライアント証明書によって識別されます。設定には、その証明書を発行している必要がある CA と、どのサブジェクト代替名が ID を保持するかを指定します：

```json
{
  "type": "mtls",
  "issuer": "client-certificates",
  "client_ca_pem": "-----BEGIN CERTIFICATE-----\n...\n-----END CERTIFICATE-----\n",
  "certificate_identity": "uri_san",
  "rules": [{"subject": "group:analysts", "resource": "host/sql/read", "action": "execute"}]
}
```

クライアント証明書チェーンは `client_ca_pem` に照らして検証され、ID のサブジェクトは、設定された種類（`uri_san` または `dns_san`）のちょうど 1 つの SAN から取得されます。Common Name へのフォールバックはありません。相互 TLS は、HTTPS の MCP トランスポートでのみ利用できます。

### ローカル OS ID {#local-os-identity}

認証情報はやり取りされません。サブジェクトは、StackQL プロセスを実行しているオペレーティングシステムの ID です。設定に必要なのは `issuer` ラベルとルールだけです：

```json
{
  "type": "local_os",
  "issuer": "this-host",
  "rules": [{"subject": "group:analysts", "resource": "host/sql/read", "action": "execute"}]
}
```

ID のサブジェクトは、StackQL プロセスの実効 Unix UID または Windows SID です。passwd エントリのない数値のコンテナー UID も含まれます。認証情報は提示されず、`STACKQL_CALLER_TOKEN` は無視されます。このモードはローカル専用で、HTTP の呼び出し元には拒否されます。オペレーティングシステムのグループメンバーシップではアクセスは付与されず、付与するのはディレクトリのメンバーシップだけです。

### セッションの期限 {#session-deadline}

デフォルトでは、クエリに認証に由来する期限はありません。新しい文ごとに認証情報が検証され、ディレクトリのメンバーシップが更新されるため、期限切れの認証情報で新しい処理を開始することはできません。ただし、期限切れによって、すでに認可された文が中断されることはありません。`"enforce_session_deadline": true` を設定すると、`max_session_seconds`（デフォルト `300`、最大 `86400`）の時点でキャンセルします。この時間は、JWT または証明書の有効期限によって短縮されます。これによってトークンが更新されることはありません。

## プリンシパルディレクトリ {#principal-directory}

ID はプリンシパルに、プリンシパルはグループにマッピングされます。マッピングは、設定された SQLite または PostgreSQL バックエンドの 4 つのテーブルで行われます：

| テーブル | 内容 |
|--|--|
| `stackql_access_principals` | プリンシパル ID と、文字列属性の JSON オブジェクト。`tenant` 属性は、プレビュー版 IaC のステートを名前空間で区切ります。 |
| `stackql_access_identities` | 検証済みの `(issuer, subject)` ペアとプリンシパル ID の対応。 |
| `stackql_access_groups` | グループ ID。 |
| `stackql_access_memberships` | プリンシパルとグループのバインディング。 |

保護されたクエリを提供する前に、信頼できるデータベース管理用の接続を通じて、テーブル、ID、メンバーシップをプロビジョニングしてください：

```sql
INSERT INTO stackql_access_principals (id, attributes) VALUES ('alice', '{}');
INSERT INTO stackql_access_groups (id) VALUES ('readers');
INSERT INTO stackql_access_identities (issuer, subject, principal_id)
VALUES ('https://identity.example', 'alice', 'alice');
INSERT INTO stackql_access_memberships (principal_id, group_id) VALUES ('alice', 'readers');
```

モードの実際の発行者とサブジェクトを使用してください。たとえば、ローカル OS ID では `('this-host', '1000')`、相互 TLS では `('client-certificates', 'spiffe://example/alice')` のようになります。バックエンドは明示的に選択します：

```bash
stackql --access.config '{"path":"/etc/stackql/access.json"}' \
  --sqlBackend '{"dbEngine":"postgres_tcp","dsn":"postgres://runtime@localhost/stackql?sslmode=require","sqlDialect":"postgres"}' \
  exec 'SELECT whoami(), groups();'
```

ディレクトリは、文ごとに不変のスナップショットを作成します。取り消しは、管理用の接続で `DELETE FROM stackql_access_memberships` を実行して行います。次の文では新しいスナップショットが使用されます。不明な ID、スキーマの欠落、不正な形式のデータ、ディレクトリの障害は、いずれもアクセスを拒否します。通常の保護された SQL からディレクトリのテーブルを参照することはできません。メンバーシップの書き込みは管理用の接続で行い、データベースファイルを保護し、バックエンドがサポートしている場合は管理用と実行時用で別々のデータベース権限を使用してください。

## ポリシールール {#policy-rules}

各ルールは、1 つの `resource` に対する 1 つの `action` を、`group:<group id>` 形式の `subject` に付与します。信頼できるプロバイダードキュメントから得た、コンパイル済みのメソッド識別子と操作識別子を使用してください。操作のワイルドカードは、読み取りだけでなくミューテーションメソッドも付与します。

`readers` グループは、ホストの安全な読み取り SQL 機能を暗黙的に持ちます。このグループ外のプリンシパルによる読み取りには `host/sql/read` が必要で、そのほかの文には `host/sql/<statement kind>` に対する `execute` が必要です。プロバイダーの操作と認証情報の使用には常に明示的なルールが必要で、コレクションへの付与によって、その基になるメソッドが付与されることはありません。追加のホストチェック：

| リソース | アクション | スコープ |
|--|--|--|
| `host/sql/read` | `execute` | 暗黙のリーダーグループ外の読み取り文 |
| `host/sql/exec` | `execute` | ネストされた `EXEC` を含む、ネイティブの `EXEC` |
| `host/iac/run`, `host/iac/destroy` | `execute` | `SELECT` 構文であっても対象となる、プレビュー版の IaC リレーション |
| `host/function/<name>` | `execute` | 既知の純粋関数からなる小さな許可リストに含まれない関数 |
| `host/backend` | `read` | 不透明な、保存済みおよび外部のバックエンドリレーション |
| `host/credentials` | `reload` | MCP の `reload_credentials` ツール |
| `session/variables` | `read` | [セッション変数](/getting-started/session-variables)とシークレットの参照解決 |
| `session/config` | `write` | `SET` と `UNSET` |

保存済みクエリの展開は実行前にチェックされ、保護されたパスでは計画キャッシュと行キャッシュの再利用が無効になります。

## エントリポイント {#entry-points}

| 認証 | `exec`, `shell`, MCP stdio | HTTPS MCP |
|--|--|--|
| JWT | `STACKQL_CALLER_TOKEN` 環境変数 | `Authorization: Bearer` リクエストヘッダー |
| 静的 Bearer | `STACKQL_CALLER_TOKEN` 環境変数 | `Authorization: Bearer` リクエストヘッダー |
| ローカル OS | プロセスの実効 UID または SID。認証情報なし | 拒否 |
| 相互 TLS | 拒否：検証済みの TLS 接続がない | 検証済みのクライアントチェーンと、設定された 1 つの SAN |

ローカルのトークン認証情報は、プロセスの環境を通じて指定してください。SQL リテラルやコマンドライン引数として指定してはいけません。HTTPS では、呼び出し元の検証機能が、[HTTP クライアント認証](/command-line-usage/mcp#http-client-authentication)で説明されている共有の `auth_token_env_var` Bearer ゲートを置き換えます。サーバーの TLS 証明書とキーの設定は必須で、認証情報のないリクエストがサーバーの環境トークンにフォールバックすることはありません。

## 呼び出し元の確認 {#inspecting-the-caller}

認証済みのセッションでは、`whoami()` はプリンシパル ID を返し、`groups()` はディレクトリのグループを JSON 配列として返します：

```sql
SELECT whoami() AS principal, groups() AS memberships;
```

```
|-----------|-------------|
| principal | memberships |
|-----------|-------------|
| alice     | ["readers"] |
|-----------|-------------|
```

どちらの関数も、`--access.config` で開始されたセッションが必要です。

## ウォークスルー {#walkthroughs}

StackQL のリポジトリには、認証モードごとに 1 つずつ、[実行可能な 4 つのウォークスルー](https://github.com/stackql/stackql/blob/main/docs/examples/access-management/README.md)が含まれています。ウォークスルーは、デモ用のディレクトリをプロビジョニングしてクエリを検証する Python スクリプトによって駆動されます。クラウドアカウントは不要で、プロジェクトのテストスイートによって実行されます。
