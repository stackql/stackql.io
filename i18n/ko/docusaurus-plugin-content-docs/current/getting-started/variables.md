---
title: 변수 사용
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL에서 변수 사용: Jsonnet 또는 JSON 데이터 파일로 값을 제공하고, 문에서 참조하며, 외부 값을 로드하고, 드라이 런으로 미리 봅니다."
image: "/img/stackql-featured-image.png"
---
참고 항목:  
[[` exec `]](/command-line-usage/exec) [[` INSERT `]](/language-spec/insert)

변수는 구성 데이터 형태로 StackQL 스크립트에 제공할 수 있습니다. StackQL 구성 데이터는 json 또는 [jsonnet](https://jsonnet.org/ "Jsonnet") 형식일 수 있습니다.

Jsonnet은 조건부 논리, 가져오기, 상속, 루프 구조 같은 추가 기능을 제공합니다.

데이터는 `stackql exec` 명령의 `--iqldata` 매개 변수로 참조하는 `json` 또는 `jsonnet` 파일을 통해 제공하거나, 다음과 같이 `<<<` 및 `>>>` 데코레이터를 사용하여 `iql` 스크립트에 포함할 수 있습니다.

```sql
<<<jsonnet
//
// jsonnet data
//
>>>
/* stackql statements ... */
```

## Jsonnet 구성 예제 {#jsonnet-configuration-example}
다음 예제는 Jsonnet을 사용하여 StackQL 스크립트에 구성 데이터를 제공하는 방법을 보여 줍니다. 데이터는 아래와 같이 `exec` 명령의 `--iqldata` 매개 변수를 사용하여 제공합니다.

```shell
stackql exec -i myscript.iql \
--iqldata vars.jsonnet \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }'
```

### Jsonnet 데이터 {#jsonnet-data}
jsonnet을 사용하면 StackQL 코드에서 변수를 재사용하고 루프 구조 안에서 배열을 사용할 수 있습니다. 다음 jsonnet은 `--iqldata` 매개 변수에서 참조하는 `vars.jsonnet` 파일의 내용입니다.

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

### StackQL 문에서 Jsonnet 데이터 참조 {#referencing-jsonnet-data-in-a-stackql-statement}
다음 코드 조각은 StackQL 명령에서 jsonnet 값을 참조하는 방법을 보여 줍니다.

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

### Jsonnet을 사용한 외부 변수 로드 {#sourcing-external-variables-using-jsonnet}
다음 코드 조각은 StackQL 쿼리를 렌더링하는 데 사용하는 Jsonnet 구성 파일에 외부 변수를 로드하는 방법을 보여 줍니다.  

#### StackQL 쿼리(`env-var-input.iql`) {#stackql-query-env-var-inputiql}

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

#### Jsonnet 데이터 파일(`vars.jsonnet`) {#jsonnet-data-file-varsjsonnet}

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

#### 명령줄 실행 {#command-line-execution}

```
/path/to/stackql exec -i $(pwd)/env-var-input.iql  --var project=stackql-demo --iqldata $(pwd)/vars.jsonnet
```

## JSON 구성 예제 {#json-configuration-example}
아래와 같이 간단한 json을 사용하여 StackQL 스크립트에 구성 데이터를 제공할 수도 있습니다.

```shell
stackql exec -i myscript.iql \
--iqldata vars.json \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }'
```

### JSON 데이터 {#json-data}

다음 json 데이터는 `--iqldata` 매개 변수에서 참조하는 `vars.json` 파일의 내용입니다.

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

### StackQL 문에서 JSON 데이터 참조 {#referencing-json-data-in-a-stackql-statement}
위에 표시된 데이터는 다음과 같이 StackQL `INSERT` 문에서 참조합니다.

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

## 명령 미리 보기를 위한 드라이 런 옵션 {#dryrun-option-to-preview-command}
아래와 같이 `--dryrun` 옵션을 사용하면 입력 스크립트와 `json` 또는 `jsonnet` 데이터 파일을 사용하여 전처리된 StackQL 스크립트를 출력할 수 있습니다.

```shell
./stackql exec -i myscript.iql \
--iqldata vars.json \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }' \
--dryrun \
--output text
```
