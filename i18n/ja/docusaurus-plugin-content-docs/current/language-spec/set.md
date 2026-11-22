---
title: SET
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - セッション変数
  - set
  - プロバイダーセッション
  - シークレット
description: "StackQL の SET：型付きのセッション変数を割り当て、ドル記号付きのターゲットで現在のセッションのプロバイダーエイリアス、認証情報フィールド、シークレットを設定します。"
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

セッション変数を割り当てるか、現在のセッションのランタイム設定を構成します。StackQL `v1.0` 以降で利用できます。

関連項目：  
[[` UNSET `]](/language-spec/unset) [[ セッション変数 ]](/getting-started/session-variables) [[ プロバイダーセッション ]](/getting-started/provider-sessions) [[` IDENTIFIER `]](/language-spec/functions/general/identifier)

* * * 

## 構文 {#syntax}

*setStatement::=*

<RailroadDiagram
type="set"
/>

*setExpression::=*

<RailroadDiagram
type="setExpression"
/>

&nbsp;  
&nbsp;  

```sql
SET [ SESSION ] <target> = { <literal> | DEFAULT | <reference> } [, <target> = { <literal> | DEFAULT | <reference> } ...];
```

__*target*__  
プレーンな識別子（`region`）、二重引用符で囲んだ名前（`"providers.saved"`）、または二重引用符で囲み、ドル記号を接頭辞に持つ設定パス（`"$.auth.aws.password"`）のいずれかです。

__*literal*__  
文字列、数値、ブール値、または `null` です。文字列は単一引用符で囲みます。配列とオブジェクトはスカラー値ではありません。JSON ドキュメントは文字列として保持してください。

__*DEFAULT*__  
ターゲットをリセットします。[`UNSET`](/language-spec/unset) と同等です。明示的な `null` は保存される値であり、リセットではありません。

__*reference*__  
既存の変数への、二重引用符で囲んだ `"$name"` 形式の参照です。たとえば `SET "$.providers.production" = "$providers.saved";` のように指定します。

__*SESSION*__  
省略可能で、StackQL が持つ唯一のスコープです。`SET SESSION name = ...` と `SET name = ...` は同じ文です。すべての割り当ては現在のセッションに属し、それ以外には属しません。このキーワードは、`SET SESSION` と `SET GLOBAL` が異なるデータベースと同じようにスクリプトを読めるようにするために存在します。`SET GLOBAL` は解析されますが、セッション間で共有される状態がないため、`configuration scope "global" is not supported; use SESSION` というエラーで拒否されます。

1 つの `SET` リスト内の割り当ては順番に適用され、トランザクションではありません。リスト内の 2 番目の割り当てが失敗しても、1 番目の割り当ては有効なままです。

## ターゲット {#targets}

| ターゲット | 効果 |
|--|--|
| `name` または `"name"` | スカラーのセッション変数を保存します。この変数は、後続の文で `$name` として参照します。ドットだけでは設定は選択されません。`"providers.saved"` は `providers.saved` という名前の変数です。古い `"variables.name"` という表記は `name` のエイリアスです。 |
| `"$.auth.<provider>.<field>"` | このセッションについて、プロバイダーの認証コンテキストのフィールドを 1 つ設定します。フィールドは、そのプロバイダーの [`--auth`](/command-line-usage/global-flags) オブジェクトのキーです（たとえば `credentialsenvvar`、`password`、`token_url`）。 |
| `"$.providers.<alias>"` | 独自の認証情報、テナンシーラベル、パラメーターのデフォルト値、チューニング設定を持つプロバイダーエイリアスをバインドします。値は文字列に格納した JSON ドキュメントです。[プロバイダーセッション](/getting-started/provider-sessions#provider-aliases)を参照してください。 |
| `"$.secrets.<name>"` | セッションのシークレットストアにシークレットを保存します。値はリテラル文字列で、通常は JSON の `auth` ドキュメントです。プロバイダーエイリアスからは `auth_secret` として参照します。 |
| `"$.secret_store"` | 永続的な OS のシークレットストア（`{"backend":"os","namespace":"..."}`）を選択します。運用者が設定するフラグ `--allow-persistent-secrets` が必要です。 |

どの形式も現在のセッションにのみ影響します。[`srv`](/command-line-usage/srv) サーバーへの独立した接続は、互いの割り当てを参照できません。また、MCP リクエストはそれぞれが独立したセッションです。そのため、MCP サーバーを使用する場合は、1 つの文のバッチで設定とクエリを行ってください。

* * *

## 例 {#examples}

### セッション変数を設定する {#set-session-variables}

各文は、指定した名前で 1 つのスカラーを保存し、値はその型を保持します。`region` は文字列、`minimum_count` は整数、`enabled` はブール値です。`null` は値として保存されるため、後で `$optional_value` は未定義として失敗するのではなく、SQL の `NULL` に解決されます。最後の文は、1 つのリストで 2 つの変数を割り当てます。

```sql
SET region = 'ap-southeast-2';
SET minimum_count = 3;
SET enabled = true;
SET optional_value = null;
SET suffix = 'East', some_id = 42;
```

### セッションスコープを明示する {#make-the-session-scope-explicit}

最初の 2 つの文は同一です。`SESSION` は存在する唯一のスコープを指定するもので、どちらの割り当ても、変数が解除されるかセッションが終了するまで有効です。セッション設定とグローバル設定を区別するデータベースと同じようにスクリプトを読めるようにしたい場合に、このキーワードを使用します。コメントアウトされた文は比較のために示しています。この文は何も割り当てず、セッション間で共有される状態がないため、`configuration scope "global" is not supported; use SESSION` というエラーで失敗します。

```sql
SET region = 'ap-southeast-2';
SET SESSION region = 'ap-southeast-2';

/*
SET GLOBAL region = 'ap-southeast-2';
-- FAILS with configuration scope "global" is not supported; use SESSION
*/
```

### クエリで変数を使用する {#use-a-variable-in-a-query}

任意の式の位置で、引用符を付けずに `$name` で変数を参照します。値はその型を保持します。文字列は SQL リテラルとしてエスケープされ、数値、ブール値、`null` はそのまま渡されます。`WHERE` 句の変数はリクエストの計画が作成される前に解決されるため、リテラルと同様にプロバイダーにプッシュされます。

```sql
SET project = 'stackql-demo';
SET zone = 'australia-southeast1-a';

SELECT name, status FROM google.compute.instances
WHERE project = $project AND zone = $zone;
```

### 変数をリセットする {#reset-a-variable}

`DEFAULT` は [`UNSET`](/language-spec/unset) とまったく同じように変数を削除します。そのため、再び設定されるまで、後続の `$region` は `variable "$region" is not defined` というエラーで失敗します。コメントアウトされたクエリはその失敗を示しています。これは `null` を割り当てる場合とは異なります。その場合、変数は `NULL` 値を持ったまま残ります。

```sql
SET region = 'ap-southeast-2';
SET region = DEFAULT;

/*
SELECT name FROM aws.s3.buckets WHERE region = $region;
-- FAILS with variable "$region" is not defined
*/
```

### 別の変数から値をコピーする {#copy-a-value-from-another-variable}

右辺には、既存の変数への二重引用符で囲んだ参照を指定できます。この場合、その時点で変数が保持している値がコピーされます。ここでは、まずプロバイダーのバインディングを `providers.saved` という名前の通常の変数として保存し（ドットがあっても設定にはなりません）、次にそれを `$.providers.production` パスにコピーして有効にします。その後で保存したコピーを削除しても、有効なバインディングには影響しません。

```sql
SET "providers.saved" = '{"provider":"aws","tenancy":"production-account","auth":{"type":"aws_signing_v4","access_key_id_env_var":"PRODUCTION_AWS_ACCESS_KEY_ID","secret_access_key_env_var":"PRODUCTION_AWS_SECRET_ACCESS_KEY"}}';
SET "$.providers.production" = "$providers.saved";
UNSET "providers.saved";
```

### このセッションで認証情報フィールドを上書きする {#override-a-credential-field-for-this-session}

プロバイダーの認証コンテキストは、起動時に [`--auth`](/command-line-usage/global-flags) で渡されるオブジェクトです。`$.auth` の下のパスは、セッションの残りの期間、そのオブジェクトのフィールドを 1 つ置き換え、他のフィールドはそのまま残します。ここでは、GitHub プロバイダーが、読み取り専用のトークンを保持する別の環境変数を参照するように設定します。この変数は StackQL プロセスの環境から読み取られます。フィールドが解除されるかセッションが終了するまで、以降の `github.*` に対するクエリではこの変数が使用されます。

```sql
SET "$.auth.github.password_var" = 'GITHUB_TOKEN_READONLY';
```

### プロバイダーエイリアスをバインドする {#bind-a-provider-alias}

プロバイダーエイリアスは、独立して構成されたプロバイダーの 2 つ目のインスタンスで、クエリではプロバイダー名の代わりに使用します。値は文字列に格納した JSON ドキュメントです。`provider` は基になるプロバイダーを指定し、`tenancy` はアカウントのラベルを指定します。`auth` は `--auth` と同じ形の認証情報ブロックで、`params` は `region` などのリクエストパラメーターのデフォルト値を指定します。その後、`production.s3.buckets` に対するクエリはその認証情報で実行され、`aws.s3.buckets` は起動時の構成を維持します。キーの完全な一覧は[プロバイダーセッション](/getting-started/provider-sessions#provider-aliases)にあります。

```sql
SET "$.providers.production" = '{
  "provider": "aws",
  "tenancy": "production-account",
  "auth": {
    "type": "aws_signing_v4",
    "access_key_id_env_var": "PRODUCTION_AWS_ACCESS_KEY_ID",
    "secret_access_key_env_var": "PRODUCTION_AWS_SECRET_ACCESS_KEY"
  },
  "params": {"region": "ap-southeast-2"}
}';

SELECT name FROM production.s3.buckets;
```
