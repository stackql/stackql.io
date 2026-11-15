---
title: UNSET
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - セッション変数
  - unset
  - プロバイダーセッション
  - シークレット
description: "StackQL の UNSET：セッション変数を削除するか、現在のセッションで SET により設定したプロバイダーエイリアス、認証情報フィールド、シークレットをクリアします。"
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

セッション変数を削除するか、現在のセッションで [`SET`](/language-spec/set) により設定したランタイム構成をクリアします。StackQL `v1.0` 以降で利用できます。

関連項目：  
[[` SET `]](/language-spec/set) [[ セッション変数 ]](/getting-started/session-variables) [[ プロバイダーセッション ]](/getting-started/provider-sessions)

* * * 

## 構文 {#syntax}

*unsetStatement::=*

<RailroadDiagram
type="unset"
/>

&nbsp;  
&nbsp;  

```sql
UNSET [ SESSION ] <target> [, <target> ...];
```

__*target*__  
`SET` が受け付ける任意のターゲットです。プレーンな変数名、二重引用符で囲んだ変数名、または二重引用符で囲んだドル記号で始まる構成パスを指定します。

__*SESSION*__  
省略可能で、存在する唯一のスコープです。すべての変数と構成設定は現在のセッションに属するため、`UNSET SESSION name` と `UNSET name` は同じ文です。`UNSET GLOBAL` は拒否され、`configuration scope "global" is not supported; use SESSION` が返されます。

`UNSET name` は `SET name = DEFAULT` と同等です。1 つのリスト内のターゲットは順番に削除され、設定されていない名前を削除してもエラーにはなりません。削除した変数を参照すると、プロバイダーへのリクエストが行われる前に失敗します。

| ターゲット | 効果 |
|--|--|
| `name` | セッション変数を削除します。 |
| `"$.auth.<provider>.<field>"` | プロバイダーのセッション認証コンテキストからそのフィールドを削除します。上書き前に有効だった値は復元されません。 |
| `"$.providers.<alias>"` | プロバイダーエイリアスのバインディングを削除します。 |
| `"$.secrets.<name>"` | セッションのストアからシークレットを削除します。永続的な OS ストアの場合は、保存されている値が削除されます。 |
| `"$.secret_store"` | 現在のストアのバインディングを破棄し、空のセッションメモリで開始します。OS ストアにすでに永続化されている値は削除されません。 |

* * *

## 例 {#examples}

### 変数を削除する {#remove-a-variable}

変数はセッションから削除されるため、コメントアウトされたクエリは古い値や空の値で実行されるのではなく、`variable "$region" is not defined` で失敗します。再利用するには、その名前をもう一度設定します。

```sql
SET region = 'ap-southeast-2';
UNSET region;

/*
SELECT name FROM aws.s3.buckets WHERE region = $region;
-- FAILS with variable "$region" is not defined
*/
```

### 複数のターゲットを削除する {#remove-several-targets}

1 つの文で変数と構成パスをまとめて削除でき、ターゲットは順番に処理されます。ここでは、プレーンな変数、ドット区切りの名前を持つ変数、認証情報の上書きを 1 つのリストで削除します。

```sql
UNSET region, "providers.saved", "$.auth.github.credentialsenvvar";
```

### セッションスコープを明示する {#make-the-session-scope-explicit}

この 2 つの文は同じ動作をします。`SESSION` は存在する唯一のスコープを指定します。明示的な形式は、セッション状態とグローバル状態を区別するデータベースでも実行するスクリプトに適しています。共有状態は存在しないため、コメントアウトされた文は失敗します。

```sql
UNSET region;
UNSET SESSION region;

/*
UNSET GLOBAL region;
-- FAILS with configuration scope "global" is not supported; use SESSION
*/
```

### 認証情報の上書きをクリアする {#clear-a-credential-override}

先に `SET "$.auth.github.credentialsenvvar"` で設定したフィールドが、GitHub プロバイダーのセッション認証コンテキストから削除されます。そのコンテキストのほかのフィールドは変更されません。上書き前に有効だった値は復元されないため、プロバイダーで引き続き必要な場合はフィールドをもう一度設定してください。

```sql
UNSET "$.auth.github.credentialsenvvar";
```

### プロバイダーエイリアスとそのシークレットを削除する {#remove-a-provider-alias-and-its-secret}

エイリアス `production` は `SET "$.providers.production"` でバインドされており、`auth_secret` を介して `$.secrets.production` に保存されたシークレットを参照しています。1 つの文で両方を削除すると、バインディングが破棄されて `production.s3.buckets` は有効なリレーションではなくなり、シークレットもセッションストアから削除されます。ほかの場所と同様に、ここでも `SESSION` は省略可能です。

```sql
UNSET SESSION "$.providers.production", "$.secrets.production";
```
