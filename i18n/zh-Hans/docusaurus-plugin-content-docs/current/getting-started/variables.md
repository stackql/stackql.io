---
title: 使用变量
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "在 StackQL 中使用变量：通过 Jsonnet 或 JSON 数据文件提供值，在语句中引用这些值，引入外部值，并通过试运行预览。"
image: "/img/stackql-featured-image.png"
---
另请参阅：  
[[` exec `]](/command-line-usage/exec) [[` INSERT `]](/language-spec/insert)

可以以配置数据的形式向 StackQL 脚本提供变量。StackQL 配置数据可以采用 json 或 [jsonnet](https://jsonnet.org/ "Jsonnet") 的形式。

Jsonnet 提供了条件逻辑、导入和继承以及循环结构等额外功能。

数据可以通过 `stackql exec` 命令的 `--iqldata` 参数所引用的 `json` 或 `jsonnet` 文件提供，也可以使用 `<<<` 和 `>>>` 修饰符直接包含在 `iql` 脚本中，如下所示：

```sql
<<<jsonnet
//
// jsonnet data
//
>>>
/* stackql statements ... */
```

## Jsonnet 配置示例 {#jsonnet-configuration-example}
以下示例演示如何使用 Jsonnet 向 StackQL 脚本提供配置数据。数据通过 `exec` 命令的 `--iqldata` 参数提供，如下所示：

```shell
stackql exec -i myscript.iql \
--iqldata vars.jsonnet \
--keyfilepath stackql-demo.json
```

### Jsonnet 数据 {#jsonnet-data}
使用 jsonnet，你可以在 StackQL 代码中复用变量，并在循环结构中使用数组。以下 jsonnet 是 `--iqldata` 参数所引用的 `vars.jsonnet` 文件的内容。

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

### 在 StackQL 语句中引用 Jsonnet 数据 {#referencing-jsonnet-data-in-an-stackql-statement}
以下代码片段演示如何在 StackQL 命令中引用 jsonnet 值。

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

### 使用 Jsonnet 引入外部变量 {#sourcing-external-variables-using-jsonnet}
以下代码片段演示如何将外部变量引入用于渲染 StackQL 查询的 Jsonnet 配置文件。  

#### StackQL 查询（`env-var-input.iql`） {#stackql-query-env-var-inputiql}

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

#### Jsonnet 数据文件（`vars.jsonnet`） {#jsonnet-data-file-varsjsonnet}

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

#### 命令行执行 {#command-line-execution}

```
/path/to/stackql exec -i $(pwd)/env-var-input.iql  --var project=stackql-demo --iqldata $(pwd)/vars.jsonnet
```

## Json 配置示例 {#json-configuration-example}
也可以使用简单的 json 向 StackQL 脚本提供配置数据，如下所示：

```shell
stackql exec -i myscript.iql \
--iqldata vars.json \
--keyfilepath stackql-demo.json
```

### Json 数据 {#json-data}

以下 json 数据是 `--iqldata` 参数所引用的 `vars.json` 文件的内容。

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
  ],
}
```

### 在 StackQL 语句中引用 Json 数据 {#referencing-json-data-in-an-stackql-statement}
上面的数据在 StackQL `INSERT` 语句中的引用方式如下所示：

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

INSERT /*+ AWAIT  */ INTO compute.firewalls
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

## 使用试运行选项预览命令 {#dryrun-option-to-preview-command}
你可以使用 `--dryrun` 选项，根据输入脚本以及 `json` 或 `jsonnet` 数据文件输出预处理后的 StackQL 脚本，如下所示：

```shell
./stackql exec -i myscript.iql \
--iqldata vars.json \
--keyfilepath stackql-demo.json \
--dryrun \
--output text
```
