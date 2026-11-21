---
title: 使用變數
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "在 StackQL 中使用變數：從 Jsonnet 或 JSON 資料檔案提供值、在陳述式中參考這些值、取得外部值，並以試執行預覽。"
image: "/img/stackql-featured-image.png"
---
另請參閱：  
[[` exec `]](/command-line-usage/exec) [[` INSERT `]](/language-spec/insert)

變數能以設定資料的形式提供給 StackQL 指令碼。StackQL 設定資料可以是 json 或 [jsonnet](https://jsonnet.org/ "Jsonnet") 格式。

Jsonnet 提供額外的功能，例如條件邏輯、匯入與繼承，以及迴圈結構。

資料可以透過 `stackql exec` 命令的 `--iqldata` 參數所參考的 `json` 或 `jsonnet` 檔案提供，也可以使用下列 `<<<` 與 `>>>` 裝飾符號包含在 `iql` 指令碼中，如下所示：

```sql
<<<jsonnet
//
// jsonnet data
//
>>>
/* stackql statements ... */
```

## Jsonnet 設定範例 {#jsonnet-configuration-example}
下列範例示範如何使用 Jsonnet 為 StackQL 指令碼提供設定資料。資料是透過 `exec` 命令的 `--iqldata` 參數提供，如下所示：

```shell
stackql exec -i myscript.iql \
--iqldata vars.jsonnet \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }'
```

### Jsonnet 資料 {#jsonnet-data}
使用 jsonnet，您可以在 StackQL 程式碼中重複使用變數，並在迴圈結構中使用陣列。下列 jsonnet 即為 `--iqldata` 參數所參考之 `vars.jsonnet` 檔案的內容。

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

### 在 StackQL 陳述式中參考 Jsonnet 資料 {#referencing-jsonnet-data-in-a-stackql-statement}
下列程式碼片段示範如何從 StackQL 命令參考 jsonnet 值。

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
'{{ $.global.project }}',
'{{.name}}',
'{{.network}}',
'{{.direction}}',
'{{.sourceRanges}}',
'{{.allowed}}';
{{end}}
```

### 使用 Jsonnet 取得外部變數 {#sourcing-external-variables-using-jsonnet}
下列程式碼片段示範如何將外部變數引入用來轉譯 StackQL 查詢的 Jsonnet 設定檔。  

#### StackQL 查詢（`env-var-input.iql`） {#stackql-query-env-var-inputiql}

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

#### Jsonnet 資料檔案（`vars.jsonnet`） {#jsonnet-data-file-varsjsonnet}

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

#### 命令列執行 {#command-line-execution}

```
/path/to/stackql exec -i $(pwd)/env-var-input.iql  --var project=stackql-demo --iqldata $(pwd)/vars.jsonnet
```

## Json 設定範例 {#json-configuration-example}
也可以使用簡單的 json 為 StackQL 指令碼提供設定資料，如下所示：

```shell
stackql exec -i myscript.iql \
--iqldata vars.json \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }'
```

### Json 資料 {#json-data}

下列 json 資料即為 `--iqldata` 參數所參考之 `vars.json` 檔案的內容。

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

### 在 StackQL 陳述式中參考 Json 資料 {#referencing-json-data-in-a-stackql-statement}
上述資料會在 StackQL `INSERT` 陳述式中以如下方式參考：

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
'{{ .project }}',
'{{ (index .firewalls 0).name }}',
'{{ (index .firewalls 0).network }}',
'{{ (index .firewalls 0).direction }}',
'{{ (index .firewalls 0).sourceRanges }}',
'{{ (index .firewalls 0).allowed }}';

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
'{{ .project }}',
'{{ (index .firewalls 1).name }}',
'{{ (index .firewalls 1).network }}',
'{{ (index .firewalls 1).direction }}',
'{{ (index .firewalls 1).sourceRanges }}',
'{{ (index .firewalls 1).allowed }}';
```

## 以試執行選項預覽命令 {#dryrun-option-to-preview-command}
您可以使用 `--dryrun` 選項，以您的輸入指令碼與 `json` 或 `jsonnet` 資料檔案輸出預先處理過的 StackQL 指令碼，如下所示：

```shell
./stackql exec -i myscript.iql \
--iqldata vars.json \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }' \
--dryrun \
--output text
```
