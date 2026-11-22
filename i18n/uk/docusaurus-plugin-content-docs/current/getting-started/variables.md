---
title: Використання змінних
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Змінні у StackQL: значення з файлів даних Jsonnet або JSON, посилання на них в інструкціях, завантаження зовнішніх значень і попередній перегляд пробним запуском."
image: "/img/stackql-featured-image.png"
---
Див. також:  
[[` exec `]](/command-line-usage/exec) [[` INSERT `]](/language-spec/insert)

Змінні можна передавати скриптам StackQL у вигляді конфігураційних даних.  Конфігураційні дані StackQL можуть мати формат json або [jsonnet](https://jsonnet.org/ "Jsonnet").

Jsonnet надає додаткові можливості, як-от умовна логіка, імпорт і успадкування, а також циклічні конструкції.

Дані можна передати у файлах `json` або `jsonnet`, на які вказує параметр `--iqldata` команди `stackql exec`, або включити в скрипт `iql` за допомогою декораторів `<<<` і `>>>`, як показано тут:

```sql
<<<jsonnet
//
// jsonnet data
//
>>>
/* stackql statements ... */
```

## Приклад конфігурації Jsonnet {#jsonnet-configuration-example}
У наступному прикладі показано, як за допомогою Jsonnet передати конфігураційні дані скрипту StackQL.  Дані передають через параметр `--iqldata` команди `exec`, як показано нижче:

```shell
stackql exec -i myscript.iql \
--iqldata vars.jsonnet \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }'
```

### Дані Jsonnet {#jsonnet-data}
За допомогою jsonnet ви можете повторно використовувати змінні й застосовувати масиви в циклічних конструкціях вашого коду StackQL.  Наведений нижче код jsonnet міститься у файлі `vars.jsonnet`, на який вказує параметр `--iqldata`.

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

### Посилання на дані Jsonnet в інструкції StackQL {#referencing-jsonnet-data-in-a-stackql-statement}
У наступному фрагменті коду показано, як посилатися на значення jsonnet з команди StackQL.

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

### Завантаження зовнішніх змінних за допомогою Jsonnet {#sourcing-external-variables-using-jsonnet}
У наступних фрагментах коду показано, як завантажити зовнішні змінні у файл конфігурації Jsonnet, з якого формується запит StackQL.  

#### Запит StackQL (`env-var-input.iql`) {#stackql-query-env-var-inputiql}

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

#### Файл даних Jsonnet (`vars.jsonnet`) {#jsonnet-data-file-varsjsonnet}

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

#### Виконання з командного рядка {#command-line-execution}

```
/path/to/stackql exec -i $(pwd)/env-var-input.iql  --var project=stackql-demo --iqldata $(pwd)/vars.jsonnet
```

## Приклад конфігурації Json {#json-configuration-example}
Передати конфігураційні дані скрипту StackQL можна й за допомогою простого json, як показано нижче:

```shell
stackql exec -i myscript.iql \
--iqldata vars.json \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }'
```

### Дані Json {#json-data}

Наведені нижче дані json містяться у файлі `vars.json`, на який вказує параметр `--iqldata`.

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

### Посилання на дані Json в інструкції StackQL {#referencing-json-data-in-a-stackql-statement}
Інструкція `INSERT` у StackQL посилається на наведені вище дані, як показано тут:

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

## Параметр dryrun для попереднього перегляду команди {#dryrun-option-to-preview-command}
За допомогою параметра `--dryrun` ви можете вивести попередньо оброблений скрипт StackQL, отриманий із вашого вхідного скрипту та файлу даних `json` або `jsonnet`, як показано нижче:

```shell
./stackql exec -i myscript.iql \
--iqldata vars.json \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }' \
--dryrun \
--output text
```
