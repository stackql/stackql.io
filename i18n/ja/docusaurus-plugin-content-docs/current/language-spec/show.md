---
title: SHOW
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の SHOW：プロバイダー、サービス、リソース、メソッドを一覧表示し、INSERT テンプレートを生成し、実行中のバイナリのバージョン、コントリビューター、依存関係を確認します。"
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

コレクション内の項目を一覧表示するために使用します。たとえば、プロバイダーで利用できるすべてのサービスや、特定のサービスで利用できるすべてのリソースを表示します。実行中の StackQL バイナリのバージョン、コントリビューター、ソフトウェア依存関係も確認できます。

関連項目：  
[[ StackQL リソース階層 ]](/getting-started/resource-hierarchy)

* * * 

## 構文 {#syntax}

*showStatement::=*

<RailroadDiagram 
type="show"
/>

&nbsp;  
&nbsp;  

```sql
SHOW [ EXTENDED ]
{ PROVIDERS | SERVICES | RESOURCES | METHODS }
[ { IN | FROM } { <provider> | <service> | <resource> } ]
[ { LIKE <pattern> | WHERE <expression> } ];

SHOW [ EXTENDED ] INSERT INTO <resource>;
SHOW [ EXTENDED ] VERSION;
SHOW [ EXTENDED ] CONTRIBUTORS;
SHOW [ EXTENDED ] DEPENDENCIES [ LIKE '<pattern>' ];
```

`VERSION`、`CONTRIBUTORS`、`DEPENDENCIES` には、プロバイダーや認証の設定は必要ありません。`DEPENDENCIES` がサポートするのは省略可能な `LIKE` 句のみです。`IN`、`FROM`、`WHERE`、`LIMIT` はサポートされていません。

* * *

## 例 {#examples}

### すべてのプロバイダーを一覧表示する {#list-all-providers}
ローカルの StackQL 環境で利用できるプロバイダーを一覧表示します。対象は、プロバイダーレジストリからプル済みのプロバイダーと、StackQL に組み込まれているプロバイダーです。
```sql
-- Returns the providers available locally
SHOW PROVIDERS;
```
<details>
<summary><em><strong>結果：</strong></em></summary>

```
|----------------------|--------------|
|         name         |   version    |
|----------------------|--------------|
| aws                  | v26.03.00379 |
|----------------------|--------------|
| databricks_account   | v26.03.00381 |
|----------------------|--------------|
| databricks_workspace | v26.03.00381 |
|----------------------|--------------|
| google               | v25.12.00357 |
|----------------------|--------------|
| k8s                  | v24.12.00276 |
|----------------------|--------------|
```

</details>

### クラウドベンダーのすべてのサービスを一覧表示する {#list-all-services-in-a-cloud-provider}
特定のプロバイダーで利用できるすべてのサービスを一覧表示します。追加情報を返すには、省略可能な EXTENDED キーワードを使用します。
```sql
-- Returns all services available in a provider
SHOW EXTENDED SERVICES IN google;
```
<details>
<summary><em><strong>結果：</strong></em></summary>

```
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
|               id               |       name        |          title          |                          description                          |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
| compute:v25.12.00357           | compute           | Compute Engine API      | Creates and runs virtual machines on Google Cloud Platform.   |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
| storage:v25.12.00357           | storage           | Cloud Storage API       | Stores and retrieves unstructured object data at global sc... |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
| bigquery:v25.12.00357          | bigquery          | BigQuery API            | A data platform for customers to create, manage, share and... |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
| aiplatform:v25.12.00357        | aiplatform        | Vertex AI API           | Train high-quality custom machine learning models with min... |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
| geminicloudassist:v25.12.00357 | geminicloudassist | Gemini Cloud Assist API | The AI-powered assistant for Google Cloud.                    |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
| ...                            | ...               | ...                     | ...                                                           |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
```

</details>


### クラウドベンダーのサービス内のすべてのリソースを一覧表示する {#list-all-resources-within-a-cloud-provider-service}
クラウドベンダーの特定のサービスで利用できるすべてのリソースを一覧表示します。省略可能な `EXTENDED` キーワードを指定すると、リソース名に加えてリソースの説明も返されます。
```sql
-- Returns all resources available in a cloud provider service
SHOW EXTENDED RESOURCES IN google.compute;
```
<details>
<summary><em><strong>結果：</strong></em></summary>

```
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
|                       name                        |                        id                        |                         description                          |
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
| instances                                         | google.compute.instances                         | Virtual machine instances running on Google Cloud Platform...|
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
| disks                                             | google.compute.disks                             | Persistent block storage volumes attached to compute inst... |
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
| networks                                          | google.compute.networks                          | Virtual private cloud (VPC) networking resources and conf... |
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
| backend_services                                  | google.compute.backend_services                  | Backend services used for load balancing and traffic distr...|
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
| firewalls                                         | google.compute.firewalls                         | Firewall rules controlling ingress and egress network traf...|
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
| ...                                               | ...                                              | ...                                                          |
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
```

</details>

### クラウドベンダーのサービス内のリソースで利用できるメソッドを表示する {#show-available-methods-for-a-resource-within-a-cloud-provider-service}
特定のリソースで利用できるすべてのメソッドを一覧表示します。
```sql
-- Returns all methods available for a cloud provider resource
SHOW EXTENDED METHODS IN google.compute.instances;
```
<details>
<summary><em><strong>結果：</strong></em></summary>

```
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
|               MethodName               |         RequiredParams         | SQLVerb |                 description                 |
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
| list                                   | project, zone                  | SELECT  | Retrieves the list of instances contained...|
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
| insert                                 | project, zone                  | INSERT  | Creates an instance resource in the speci...|
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
| delete                                 | instance, project, zone        | DELETE  | Deletes the specified Instance resource...  |
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
| start                                  | instance, project, zone        | EXEC    | Starts an instance that was stopped using...|
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
| stop                                   | instance, project, zone        | EXEC    | Stops a running instance, shutting it dow...|
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
| ...                                    | ...                            | ...     | ...                                         |
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
```

</details>

### `SHOW INSERT` コマンドを使用した `INSERT` テンプレートの生成 {#generating-an-insert-template-using-the-show-insert-command}

次のように、`SHOW` コマンドを使用して `INSERT` 文のテンプレートを生成できます。

```sql
SHOW INSERT INTO google.compute.addresses;
```
<details>
<summary><em><strong>結果：</strong></em></summary>

```sql
INSERT INTO google.compute.addresses(
  project,
  region,
  data__address,
  data__addressType,
  data__description,
  data__ipVersion,
  data__name,
  data__network,
  data__networkTier,
  data__prefixLength,
  data__purpose,
  data__subnetwork
)
SELECT
  '{{ .values.project }}',
  '{{ .values.region }}',
  '{{ .values.data__address }}',
  '{{ .values.data__addressType }}',
  '{{ .values.data__description }}',
  '{{ .values.data__ipVersion }}',
  '{{ .values.data__name }}',
  '{{ .values.data__network }}',
  '{{ .values.data__networkTier }}',
   {{ .values.data__prefixLength }},
  '{{ .values.data__purpose }}',
  '{{ .values.data__subnetwork }}'
```

</details>

> 返されるフィールドは、プロバイダーが必須とするものだけに制限できます。その例については、[__インフラストラクチャテンプレートの作成__](/getting-started/templating)を参照してください。

> このテンプレートは、`json` または `jsonnet` のデータファイルと組み合わせて使用し、実行時に値を指定できます。詳細については、[__変数の使用__](/getting-started/variables)を参照してください。

### StackQL のバージョンを表示する {#show-the-stackql-version}

実行中の StackQL プロセスのバージョンを返します。shell モード、exec モード、Postgres ワイヤプロトコルのサーバーモードで、どのビルドがクエリに応答しているかを確認する必要がある場合に便利です。同じ情報は MCP の `server_info` ツールからも取得できます。

```sql
-- Returns the version of the running StackQL process
SHOW VERSION;
```
<details>
<summary><em><strong>結果：</strong></em></summary>

```
+---------+
| version |
+---------+
| 1.2.3   |
+---------+
```

</details>

省略可能な `EXTENDED` キーワードを使用すると、git コミット、ビルドのタイムスタンプ、ビルドプラットフォームも返されます。

```sql
-- Returns version plus build provenance
SHOW EXTENDED VERSION;
```
<details>
<summary><em><strong>結果：</strong></em></summary>

```
+---------+---------+----------------------+-----------------+
| version | commit  |     build_date       |    platform     |
+---------+---------+----------------------+-----------------+
| 1.2.3   | a1b2c3d | 2026-05-15T04:12:00Z | linux/amd64     |
+---------+---------+----------------------+-----------------+
```

</details>

> `SHOW VERSION` には、プロバイダー、レジストリ、認証の設定は必要ありません。どのセッションでも安全に実行できます。

### StackQL のコントリビューターを一覧表示する {#list-stackql-contributors}

実行中の StackQL バイナリに組み込まれたコントリビューターのランキングを返します。このスナップショットには、StackQL リポジトリにスターを付けた人間の GitHub コントリビューターが、コントリビューション数の多い順に掲載されています。内容はビルドごとに固定されており、実行時に GitHub へクエリすることはありません。

```sql
SHOW CONTRIBUTORS;
SHOW EXTENDED CONTRIBUTORS;
```

| 列 | 説明 |
| --- | --- |
| `contributor` | GitHub のユーザー名 |
| `contributions` | スナップショットに記録されたコントリビューション数（`EXTENDED` 指定時に含まれる） |

### バイナリのソフトウェア依存関係を一覧表示する {#list-the-binarys-software-dependencies}

実行中の StackQL 実行ファイルに含まれる Go モジュールのオフラインインベントリを返します。これはバイナリの軽量なソフトウェア部品表（SBOM）であり、ユーザーやエージェントが実行時にクエリできます。直接および推移的な Go の依存関係が含まれます。インストール済みのプロバイダー定義、個別のラッパー、CI ツール、テスト専用モジュール、コンテナイメージ、オペレーティングシステムのパッケージは対象外です。

```sql
SHOW DEPENDENCIES;
SHOW EXTENDED DEPENDENCIES;
```

デフォルトの列は `name`、`version`、`license`、`description` です。`EXTENDED` を指定すると、返される行を変えずに、以下の残りの列が追加されます。

| 列 | 説明 |
| --- | --- |
| `name` | 実効 Go モジュールパス（Go モジュールの置き換えが適用される場合は置き換え先のパス） |
| `version` | 正確なモジュールバージョン（プレリリースバージョンと疑似バージョンを含む） |
| `license` | 検出されたライセンス識別子（特定できない場合や曖昧な場合は `UNKNOWN`） |
| `description` | コンポーネントの説明（利用できる場合） |
| `source_url` | ソースリポジトリの URL（`EXTENDED` 指定時に含まれる） |
| `license_url` | モジュールのタグまたはソースリビジョンに固定されたライセンスファイルの URL（`EXTENDED` 指定時に含まれる） |
| `purl` | モジュールとバージョンを識別する Package URL（`EXTENDED` 指定時に含まれる） |
| `checksum` | `h1:` プレフィックス付きの Go モジュールチェックサムで、実行ファイルやアーカイブの SHA-256 ダイジェストではない（`EXTENDED` 指定時に含まれる） |
| `original_name` | 置き換えが適用される場合の、元々要求されたモジュールパス（`EXTENDED` 指定時に含まれる） |
| `original_version` | 置き換えが適用される場合の、元々要求されたバージョン（`EXTENDED` 指定時に含まれる） |

行は実効モジュール名、次にバージョンの順で並べ替えられます。置き換えが適用されたモジュールには、置き換え先のバージョン、チェックサム、帰属情報が使用されます。置き換えのないモジュールでは、`original_name` と `original_version` は `NULL` です。そのほかの取得できないメタデータも `NULL` になります。ただし、特定できないライセンスは `UNKNOWN` です。ローカルファイルシステムへの置き換えには、架空のバージョン、URL、チェックサムは設定されません。

実効モジュール名、元のモジュール名、説明を検索するには `LIKE` を使用します。

```sql
SHOW DEPENDENCIES LIKE '%stackql%';
SHOW EXTENDED DEPENDENCIES LIKE '%vitess%';
SHOW EXTENDED DEPENDENCIES LIKE 'github.com/chzyer/readline';
```

一致の判定では大文字と小文字は区別されません。`%` は 0 文字以上の任意の文字に、`_` はちょうど 1 文字に一致します。ライセンス列とバージョン列は検索対象外です。SQL の文字列リテラル内の単一引用符は 2 つ重ねてください。SQL 文字列のデコード後、バックスラッシュはリテラルとして扱われ、`LIKE` のワイルドカードをエスケープしません。

一致するモジュールがない場合、結果は 0 行になります。CSV では列ヘッダーが保持され、PostgreSQL ワイヤプロトコルのクライアントでは列のメタデータが保持されます。JSON 出力は `null` です。

このインベントリは、ビルド時に組み込まれたメタデータと実行ファイルの Go ビルド情報を組み合わせたものです。クエリには、ネットワークアクセス、Go のインストール、ソースのチェックアウト、モジュールキャッシュは必要ありません。生成されたメタデータを含まないビルドでも、リンクされたモジュールは報告されます。その場合、欠けている帰属情報は `UNKNOWN` または `NULL` と表示されます。

出力の対象はバイナリの Go の依存関係のみです。依存関係どうしの関係は含まれず、SPDX や CycloneDX のドキュメントもエクスポートしません。また、デプロイ全体を網羅する SBOM でも、ライセンスコンプライアンスの判定でもありません。

* * *
