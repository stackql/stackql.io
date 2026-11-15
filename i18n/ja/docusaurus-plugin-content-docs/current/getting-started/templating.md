---
title: インフラストラクチャテンプレートの作成
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の SHOW INSERT で、任意のリソースの INSERT テンプレート（全パラメーターまたは必須パラメーターのみ）をインフラストラクチャ定義の出発点として生成します。"
image: "/img/stackql-featured-image.png"
---
関連項目：  
[[` exec `]](/command-line-usage/exec) [[` INSERT `]](/language-spec/insert) [[` SHOW `]](/language-spec/show) [[ 変数の使用 ]](/getting-started/variables)

StackQL では、`INSERT` 文を使用してクラウドリソースをデプロイします。さまざまなクラウドリソースの `INSERT` テンプレートは、`SHOW INSERT` コマンドを使用して生成できます。

### `SHOW INSERT` コマンドを使用した `INSERT` テンプレートの生成 {#generating-an-insert-template-using-the-show-insert-command}

テンプレートを生成する例を次に示します。

```sql
SHOW INSERT INTO google.compute.addresses;
```
これは、次のように `shell` から実行することも、`exec` コマンドを使用して実行することもできます。

```shell
./stackql exec 'show insert into google.compute.addresses' --output text --hideheaders
```

*__戻り値：__*  

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

> このテンプレートを `json` または `jsonnet` のデータファイルと組み合わせて使用すると、実行時に値を指定できます。詳細については、[変数の使用](/getting-started/variables)を参照してください。

### 必須パラメーターのみを含む `INSERT` テンプレートの生成 {#generating-an-insert-template-with-only-required-parameters}

必須の属性のみを含む StackQL の `INSERT` テンプレートを生成するには、次のように `/*+ REQUIRED */` クエリヒントを使用します。

```sql
SHOW INSERT /*+ REQUIRED */ INTO google.compute.instances;
```
これは、次のように `shell` から実行することも、`exec` コマンドを使用して実行することもできます。

```shell
./stackql exec 'show insert /*+ REQUIRED */ into google.compute.instances' --output text -H
```

*__戻り値：__*  

```sql
INSERT INTO google.compute.instances(
  project,
  zone,
  data__machineType,
  data__name
)
SELECT
  '{{ .values.project }}',
  '{{ .values.zone }}',
  '{{ .values.data__machineType }}',
  '{{ .values.data__name }}'
;
```
