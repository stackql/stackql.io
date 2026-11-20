---
title: 変数の使用
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL での変数の使用：Jsonnet または JSON のデータファイルから値を渡し、文の中で参照し、外部の値を取り込み、ドライランでプレビューします。"
image: "/img/stackql-featured-image.png"
---
関連項目：  
[[` exec `]](/command-line-usage/exec) [[` INSERT `]](/language-spec/insert)

変数は、設定データの形式で StackQL スクリプトに渡すことができます。StackQL の設定データには、json または [jsonnet](https://jsonnet.org/ "Jsonnet") の形式を使用できます。

Jsonnet には、条件ロジック、インポート、継承に加えて、ループ構造などの追加機能があります。

データは、`stackql exec` コマンドの `--iqldata` パラメーターで参照する `json` または `jsonnet` ファイルで渡すことができます。また、次のように `<<<` と `>>>` のデコレーターを使用して、`iql` スクリプトに含めることもできます。

```sql
<<<jsonnet
//
// jsonnet data
//
>>>
/* stackql statements ... */
```

## Jsonnet の設定例 {#jsonnet-configuration-example}
次の例は、Jsonnet を使用して StackQL スクリプトに設定データを渡す方法を示しています。データは、次のように `exec` コマンドの `--iqldata` パラメーターを使用して渡します。

```shell
stackql exec -i myscript.iql \
--iqldata vars.jsonnet \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }'
```

### Jsonnet データ {#jsonnet-data}
jsonnet を使用すると、StackQL コードの中で変数を再利用したり、ループ構造の中で配列を使用したりできます。次の jsonnet は、`--iqldata` パラメーターで参照する `vars.jsonnet` ファイルの内容です。

```json
// variables
local project = 'stackql-demo';
local region = 'australia-southeast1';
local name = 'myapp';
local nw_name = name + '-vpc';
local nw_self_link = 'https://compute.googleapis.com/compute/v1/projects/' + project + '/global/networks/' + nw_name + '/';

{
  // global config
  global: {
    project: project,
  },

  // firewall rules
  firewalls: [
    {
      allowed: [{IPProtocol: 'tcp'}, {IPProtocol: 'udp'}, {IPProtocol: 'icmp'}], 
      direction: 'INGRESS', 
      name: name + '-allow-internal-fw', 
      network: nw_self_link,
      sourceRanges: ['10.240.0.0/24', '10.200.0.0/16']
    },
    {
      allowed: [{IPProtocol: 'tcp', ports: ['22']}, {IPProtocol: 'tcp', ports: ['6443']},{IPProtocol: 'icmp'}],
      direction: 'INGRESS', 
      name: name + '-allow-external-fw', 
      network: nw_self_link,
      sourceRanges: ['0.0.0.0/0']
    }
  ],
}
```

### StackQL 文での Jsonnet データの参照 {#referencing-jsonnet-data-in-a-stackql-statement}
次のコードスニペットは、StackQL コマンドから jsonnet の値を参照する方法を示しています。

```sql
--
-- create firewall rules
--
{{range .firewalls}}
INSERT /*+ AWAIT  */ INTO google.compute.firewalls
(
 project,
 data__name,
 data__network,
 data__direction,
 data__sourceRanges,
 data__allowed
) 
SELECT
'{{ .global.project }}',
'{{.name}}',
'{{.network}}',
'{{.direction}}',
'{{.sourceRanges}}',
'{{.allowed}}';
{{end}}
```

### Jsonnet を使用した外部変数の取り込み {#sourcing-external-variables-using-jsonnet}
次のコードスニペットは、StackQL クエリのレンダリングに使用する Jsonnet 設定ファイルに、外部変数を取り込む方法を示しています。

#### StackQL クエリ（`env-var-input.iql`） {#stackql-query-env-var-inputiql}

```sql
INSERT /*+ AWAIT  */ INTO google.compute.networks
(
 project,
 data__name,
 data__autoCreateSubnetworks,
 data__routingConfig
) 
SELECT
'{{ .global.project }}',
'{{ .network.name }}',
{{ .network.autoCreateSubnetworks }},
'{{ .network.routingConfig }}';
```

#### Jsonnet データファイル（`vars.jsonnet`） {#jsonnet-data-file-varsjsonnet}

```json
// variables
local name = 'env-var-input-demo';
local project = std.extVar("project");

{
  // global config
  global: {
    project: project,
  },
  // network
  network: {
    autoCreateSubnetworks: false,
    name: name + '-vpc',
    routingConfig: {routingMode: 'REGIONAL'}
  },
}
```

#### コマンドラインでの実行 {#command-line-execution}

```
/path/to/stackql exec -i $(pwd)/env-var-input.iql  --var project=stackql-demo --iqldata $(pwd)/vars.jsonnet
```

## Json の設定例 {#json-configuration-example}
次のように、シンプルな json を使用して StackQL スクリプトに設定データを渡すこともできます。

```shell
stackql exec -i myscript.iql \
--iqldata vars.json \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }'
```

### Json データ {#json-data}

次の json データは、`--iqldata` パラメーターで参照する `vars.json` ファイルの内容です。

```json
{
  "project": "stackql-demo",
  "firewalls": [
    {
      "allowed": [{"IPProtocol": "tcp"}, {"IPProtocol": "udp"}, {"IPProtocol": "icmp"}], 
      "direction": "INGRESS", 
      "name": "myapp-allow-internal-fw", 
      "network": "https://compute.googleapis.com/compute/v1/projects/stackql-demo/global/networks/myapp-vpc/",
      "sourceRanges": ["10.240.0.0/24", "10.200.0.0/16"]
    },
    {
      "allowed": [{"IPProtocol": "tcp", "ports": ["22"]}, {"IPProtocol": "tcp", "ports": ["6443"]},{"IPProtocol": "icmp"}],
      "direction": "INGRESS", 
      "name": "myapp-allow-external-fw", 
      "network": "https://compute.googleapis.com/compute/v1/projects/stackql-demo/global/networks/myapp-vpc/",
      "sourceRanges": ["0.0.0.0/0"]
    }
  ]
}
```

### StackQL 文での Json データの参照 {#referencing-json-data-in-a-stackql-statement}
上記のデータは、次のように StackQL の `INSERT` 文で参照します。

```sql
INSERT /*+ AWAIT  */ INTO google.compute.firewalls
(
 project,
 data__name,
 data__network,
 data__direction,
 data__sourceRanges,
 data__allowed
) 
SELECT
'{{ project }}',
'{{ firewalls[0].name }}',
'{{ firewalls[0].network }}',
'{{ firewalls[0].direction }}',
'{{ firewalls[0].sourceRanges }}',
'{{ firewalls[0].allowed }}';

INSERT /*+ AWAIT  */ INTO google.compute.firewalls
(
 project,
 data__name,
 data__network,
 data__direction,
 data__sourceRanges,
 data__allowed
) 
SELECT
'{{ project }}',
'{{ firewalls[1].name }}',
'{{ firewalls[1].network }}',
'{{ firewalls[1].direction }}',
'{{ firewalls[1].sourceRanges }}',
'{{ firewalls[1].allowed }}';
```

## コマンドをプレビューするためのドライランオプション {#dryrun-option-to-preview-command}
次のように `--dryrun` オプションを使用すると、入力スクリプトと `json` または `jsonnet` のデータファイルから前処理された StackQL スクリプトを出力できます。

```shell
./stackql exec -i myscript.iql \
--iqldata vars.json \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }' \
--dryrun \
--output text
```
