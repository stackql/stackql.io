---
title: Usar variáveis
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Usar variáveis no StackQL: fornecer valores de arquivos Jsonnet ou JSON, referenciá-los em instruções, obter valores externos e pré-visualizar com uma execução de teste."
image: "/img/stackql-featured-image.png"
---
Veja também:  
[[` exec `]](/command-line-usage/exec) [[` INSERT `]](/language-spec/insert)

As variáveis podem ser fornecidas aos scripts do StackQL na forma de dados de configuração. Os dados de configuração do StackQL podem estar no formato json ou [jsonnet](https://jsonnet.org/ "Jsonnet").

O Jsonnet oferece funcionalidades adicionais, como lógica condicional, importações e herança, além de estruturas de loop.

Os dados podem ser fornecidos por meio de arquivos `json` ou `jsonnet` referenciados no parâmetro `--iqldata` do comando `stackql exec`, ou podem ser incluídos em um script `iql` com os decoradores `<<<` e `>>>`, como mostrado aqui:

```sql
<<<jsonnet
//
// jsonnet data
//
>>>
/* stackql statements ... */
```

## Exemplo de configuração com Jsonnet {#jsonnet-configuration-example}
O exemplo a seguir demonstra o uso do Jsonnet para fornecer dados de configuração a um script do StackQL. Os dados são fornecidos com o parâmetro `--iqldata` do comando `exec`, como mostrado a seguir:

```shell
stackql exec -i myscript.iql \
--iqldata vars.jsonnet \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }'
```

### Dados Jsonnet {#jsonnet-data}
Com o jsonnet, você pode reutilizar variáveis e usar arrays em estruturas de loop no seu código do StackQL. O jsonnet a seguir seria o conteúdo do arquivo `vars.jsonnet` referenciado no parâmetro `--iqldata`.

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

### Referenciar dados Jsonnet em uma instrução do StackQL {#referencing-jsonnet-data-in-a-stackql-statement}
O trecho de código a seguir demonstra como referenciar valores jsonnet em um comando do StackQL.

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

### Obter variáveis externas com Jsonnet {#sourcing-external-variables-using-jsonnet}
Os trechos de código a seguir demonstram como obter variáveis externas em um arquivo de configuração Jsonnet usado para renderizar uma consulta do StackQL.

#### Consulta do StackQL (`env-var-input.iql`) {#stackql-query-env-var-inputiql}

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

#### Arquivo de dados Jsonnet (`vars.jsonnet`) {#jsonnet-data-file-varsjsonnet}

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

#### Execução na linha de comando {#command-line-execution}

```
/path/to/stackql exec -i $(pwd)/env-var-input.iql  --var project=stackql-demo --iqldata $(pwd)/vars.jsonnet
```

## Exemplo de configuração com Json {#json-configuration-example}
Também é possível usar json simples para fornecer dados de configuração a um script do StackQL, como mostrado a seguir:

```shell
stackql exec -i myscript.iql \
--iqldata vars.json \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }'
```

### Dados Json {#json-data}

Os dados json a seguir seriam o conteúdo do arquivo `vars.json` referenciado no parâmetro `--iqldata`.

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

### Referenciar dados Json em uma instrução do StackQL {#referencing-json-data-in-a-stackql-statement}
Os dados mostrados acima seriam referenciados em uma instrução `INSERT` do StackQL, como mostrado aqui:

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

## Opção dryrun para pré-visualizar o comando {#dryrun-option-to-preview-command}
Você pode usar a opção `--dryrun` para gerar como saída um script do StackQL pré-processado a partir do seu script de entrada e do arquivo de dados `json` ou `jsonnet`, como mostrado a seguir:

```shell
./stackql exec -i myscript.iql \
--iqldata vars.json \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }' \
--dryrun \
--output text
```
