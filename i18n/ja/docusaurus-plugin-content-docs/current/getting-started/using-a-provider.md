---
title: プロバイダーの使用
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL でのプロバイダーの使用：レジストリからプルし、環境変数または認証情報で認証して、そのサービスとリソースをクエリします。"
image: "/img/stackql-featured-image.png"
---

関連項目：  
[[` exec `]](/command-line-usage/exec) [[` shell `]](/command-line-usage/shell) [[ StackQL Provider Registry ]](/providers)

StackQL のクエリは、Google Cloud Platform、Amazon Web Services、Microsoft Azure、Digital Ocean など、さまざまなクラウドベンダーに対して実行できます。StackQL のプロバイダー仕様は [__StackQL Provider Registry__](/providers) からプルされます。プルは、StackQL の [__`REGISTRY PULL`__](/language-spec/registry) コマンドで行うことも、[__`stackql registry`__](/command-line-usage/registry) コマンドラインユーティリティで行うこともできます。


## プロバイダーのインストール {#installing-a-provider}

利用可能なプロバイダーの一覧を表示するには、次のコマンドを実行します。

```sql
REGISTRY LIST;
```

プロバイダーをインストールするには、次のコマンドを実行します。

```sql
REGISTRY PULL okta;
```

現在のシステムにインストールされているプロバイダーを表示するには、次のコマンドを実行します。

```sql
SHOW PROVIDERS;
```

## プロバイダーへの認証 {#authenticating-to-a-provider}

クラウドベンダーへの認証は、多くの場合、StackQL の一連の処理における最初のステップです。[`SHOW`](/language-spec/show) や [`DESCRIBE`](/language-spec/describe) などのメタコマンドは認証なしでプロバイダーに対して実行できますが、[`SELECT`](/language-spec/select)、[`INSERT`](/language-spec/insert)、[`EXEC`](/language-spec/exec) コマンドには通常、認証済みのセッションが必要です。

認証は、システムに設定された環境変数（または CI システムでシークレットとして読み込まれた環境変数）を使用して行います。プロバイダーごとに、認証に使用する固有のデフォルト変数があります。そのため、[StackQL Provider Registry のドキュメント](/providers)で、使用するプロバイダーのドキュメントを確認してください。これらの変数は、[`--env.file`](/command-line-usage/global-flags) グローバルフラグを使用して、起動時に dotenv 形式のファイルから読み込むこともできます。

:::tip

複数の認証オブジェクトを指定すると、同じ対話型シェルや非対話型のバッチ処理の中で、複数のプロバイダーに認証できます。例を次に示します。

```
export AWS_ACCESS_KEY_ID=YOURACCESSKEYID
export AWS_SECRET_ACCESS_KEY=YOURSECRETACCESSKEY
export DIGITALOCEAN_TOKEN=dop_v1_yourdigitaloceantoken
export STACKQL_GITHUB_USERNAME=yourghuser
export STACKQL_GITHUB_PASSWORD=ghp_yourgithubpersonalaccesstoken
```

これは、1 つのクエリで複数のプロバイダーのデータを結合する場合に特に便利です。

:::
