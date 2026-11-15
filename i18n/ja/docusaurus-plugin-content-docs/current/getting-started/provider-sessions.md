---
title: プロバイダーセッションとシークレット
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - プロバイダーセッション
  - マルチアカウント
  - 認証情報
  - シークレット
  - キーチェーン
description: "StackQL のプロバイダーセッション：独自の認証情報、テナンシー、パラメーターのデフォルト値を持つエイリアスのバインド、セッション単位の認証情報の上書き、メモリまたは OS ストアでのシークレット保持。"
image: "/img/stackql-featured-image.png"
---

関連項目：  
[[` SET `]](/language-spec/set) [[` UNSET `]](/language-spec/unset) [[ セッション変数 ]](/getting-started/session-variables) [[ グローバルフラグ ]](/command-line-usage/global-flags)

セッションでは、独自の認証情報、テナンシーラベル、パラメーターのデフォルト値、リクエストの同時実行数の上限を持つエイリアスとして、プロバイダーをバインドできます。これにより、同じプロバイダーの複数のアカウントやテナントを、1 つのセッション内で、さらには 1 つの文でクエリできます。認証情報フィールドはセッション単位で上書きすることもできます。シークレットはセッションメモリに保持できるほか、運用者がオプトインした場合はオペレーティングシステムのシークレットストアにも保持できます。これらはすべて [`SET`](/language-spec/set) と [`UNSET`](/language-spec/unset) で設定し、StackQL `v1.0` 以降で利用できます。

ここで説明する内容はすべて、現在のセッションのスコープに限定されます。`stackql srv` サーバーへの独立した接続同士はバインディングやシークレットを共有せず、MCP リクエストはそれぞれが独立したセッションです。そのため、MCP サーバーを使用する場合は、1 つの文のバッチ内で設定とクエリを行ってください。

## プロバイダーエイリアス {#provider-aliases}

`SET "$.providers.<alias>"` は、エイリアスをプロバイダーにバインドします。値は、文字列に格納された JSON ドキュメントです：

| キー | 必須 | 説明 |
|--|--|--|
| `provider` | はい | エイリアスの解決先となる正規のプロバイダー（たとえば `aws`）。 |
| `tenancy` | はい | アカウント名やテナント名などのグループ化ラベル。StackQL はこれをプロバイダーに照らして検証しません。 |
| `auth` | `auth` または `auth_secret` のいずれか | プロバイダーの [`--auth`](/command-line-usage/global-flags) オブジェクト内のエントリと同じ形状の認証ドキュメント。エイリアスは、正規のプロバイダーの認証情報を借用しません。 |
| `auth_secret` | `auth` または `auth_secret` のいずれか | 認証ドキュメントを保持する[セッションシークレット](#session-secrets)の名前。 |
| `params` | いいえ | 宣言済みのプロバイダーパラメーター（宣言済みのサーバーパラメーターを含む）のデフォルト値。クエリ内の述語はデフォルト値より優先されます。 |
| `tuning` | いいえ | エイリアスのリクエストに対する `max_concurrent` と `max_per_host` の上限。プロバイダーとテナンシーが同じエイリアスはこれらの制御を共有するため、同一のチューニングを宣言する必要があります。 |
<br/>
次の例では、2 つの AWS アカウントを別々のエイリアスでバインドし、それぞれが異なる名前の環境変数からキーを読み取り、1 つの文で両方をクエリします：

```sql
SET "$.providers.production" = '{
  "provider": "aws",
  "tenancy": "production-account",
  "auth": {
    "type": "aws_signing_v4",
    "access_key_id_env_var": "PRODUCTION_AWS_ACCESS_KEY_ID",
    "secret_access_key_env_var": "PRODUCTION_AWS_SECRET_ACCESS_KEY"
  },
  "params": {"region": "ap-southeast-2"},
  "tuning": {"max_concurrent": 4, "max_per_host": 4}
}';

SET "$.providers.development" = '{
  "provider": "aws",
  "tenancy": "development-account",
  "auth": {
    "type": "aws_signing_v4",
    "access_key_id_env_var": "DEVELOPMENT_AWS_ACCESS_KEY_ID",
    "secret_access_key_env_var": "DEVELOPMENT_AWS_SECRET_ACCESS_KEY"
  },
  "params": {"region": "ap-southeast-2"},
  "tuning": {"max_concurrent": 2}
}';

SELECT name, 'production' AS account FROM production.s3.buckets
UNION ALL
SELECT name, 'development' AS account FROM development.s3.buckets;
```

環境変数の参照は、StackQL プロセスの環境から解決されます。セッションローカルな環境はありません。一時的な AWS 認証情報には、セッショントークンのフィールドまたは環境変数の参照も必要です。

エイリアスは、メタデータ文とデータ文ではそのプロバイダーと同じように動作します。`SHOW`、`DESCRIBE`、`SELECT`、`INSERT`、`UPDATE`、`DELETE`、`EXEC` はいずれも、プロバイダー名の代わりにエイリアスを受け付けます。エイリアスは、ネイティブの組み込みリレーションやプレビュー版の IaC リレーションには適用されません。異なるエイリアスを使用する `UNION ALL` の各分岐は、順番に実行されます。

各クエリは、エイリアスの認証情報の独立したスナップショットを取得します。バインディングの変更はそれ以降のクエリに影響し、すでに実行中のクエリには影響しません。`UNSET "$.providers.<alias>"` はバインディングを削除します。

## 認証情報フィールドの上書き {#overriding-credential-fields}

`SET "$.auth.<provider>.<field>"` は、セッション内でプロバイダーの認証コンテキストのフィールドを 1 つ変更します。フィールド名は、プロバイダーの `--auth` エントリのキーです：

```sql
SET "$.auth.github.credentialsenvvar" = 'GITHUB_TOKEN_READONLY';
SELECT name FROM github.repos.repos WHERE org = 'stackql';
UNSET "$.auth.github.credentialsenvvar";
```

エイリアスと同様に、変更は `SET` の後に開始されたクエリに適用されます。認証が解除されたプロバイダー、または使用可能な `auth` を持たないエイリアスに対するクエリは、リクエストが送信される前に失敗します。

## セッションシークレット {#session-secrets}

デフォルトで、すべてのセッションはメモリ専用のシークレットストアを持ち、設定は不要です。`SET "$.secrets.<name>"` はリテラル文字列（通常は JSON 形式の認証ドキュメント）を格納し、プロバイダーエイリアスは `auth_secret` でそれを参照します：

```sql
SET "$.secrets.production" = '{"type":"aws_signing_v4","access_key_id_env_var":"PRODUCTION_AWS_ACCESS_KEY_ID","secret_access_key_env_var":"PRODUCTION_AWS_SECRET_ACCESS_KEY"}';
SET "$.providers.production" = '{"provider":"aws","tenancy":"production-account","auth_secret":"production","params":{"region":"ap-southeast-2"}}';

SELECT name FROM production.s3.buckets;

UNSET "$.secrets.production";
```

シークレットをローテーションすると、それ以降のクエリに影響します。削除すると、それ以降の使用は失敗します。新しいセッションには新しい空のストアが割り当てられ、セッションを終了するとストアは解放されます。`UNSET "$.secret_store"` は現在のストアのバインディングを破棄し、空のセッションメモリで再開します。

通常の認証情報の使用や、変数または設定を格納する `SET` 文は、シークレットを SQL バックエンドに書き込みません。機密データを通常の変数やテーブルに格納する場合は、呼び出し元の責任になります。

## 永続的な OS シークレットストア {#persistent-os-secret-stores}

永続ストレージはデフォルトで無効です。有効にできるのは、運用者が `--allow-persistent-secrets` を指定して StackQL を起動した場合だけで、SQL から許可することはできません。このフラグが設定されている場合、セッションでは次の文で OS ストアを選択します：

```sql
SET "$.secret_store" = '{"backend": "os", "namespace": "stackql/provider-auth"}';
```

ストアは macOS Keychain、Linux Secret Service（セッションサービスが動作している `secret-tool`）、または Windows Credential Manager です。受け付けられるのは `os` バックエンドだけです。フラットファイルのストア、ファイルパス、暗黙的なファイルへのフォールバックは、フラグが設定されていても拒否されます。OS の機能が利用できない場合は、ファイルにフォールバックせずに失敗します。

OS ストアに書き込まれた値は SQL セッションの終了後も残り、`UNSET "$.secrets.<name>"` で明示的に削除します。ストアのバインディングをリセットしても、すでに永続化された値は削除されません。ストアへのアクセスは、StackQL プロセスのオペレーティングシステム上の ID によって制御されます。名前空間はユーザーごとのアクセス制御ではないため、このオプトインをマルチユーザー向けの保管庫として扱わないでください。

## SQL でのシークレット値 {#secret-values-in-sql}

運用者が `--allow-secret-dereference` を指定して StackQL を起動しない限り、シークレット値が SQL 内で参照解決されることはありません。このフラグを指定すると、`"$secrets.production"`、`"$secrets.production.credentials"`、`"$auth.github.password"`、`"$providers.production.auth.credentials"` などの参照が式の中で解決されます：

```sql
SELECT upper("$secrets.production.credentials") AS credential;

/*
-- the same statement in a session started without --allow-secret-dereference
SELECT upper("$secrets.production.credentials") AS credential;
-- FAILS with secret variable dereferencing is disabled
*/
```

コメントアウトされたコピーが示すように、フラグなしで起動した場合、同じ文は何も評価されないうちに拒否されます。このフラグは、明示的に参照解決された値が SQL のステージング、結果、ログ、永続化されたクエリのアーティファクトに入り得ることを了承するものです。通常のプロバイダー認証には、このフラグは必要ありません。

## 権限 {#permissions}

[アクセス管理](/command-line-usage/access-management)が有効な場合、変数とシークレットの読み取りには `session/variables:read` が、`SET` または `UNSET` には `session/config:write` が必要です。解決されたエイリアスは、実行前にその正規のプロバイダーとして認可されます。変数ごと、シークレットごとの所有権と権限の付与は実装されていません。
