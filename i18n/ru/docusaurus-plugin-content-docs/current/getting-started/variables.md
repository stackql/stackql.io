---
title: Использование переменных
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Использование переменных в StackQL: значения из файлов данных Jsonnet или JSON, ссылки на них в инструкциях, загрузка внешних значений и пробный запуск."
image: "/img/stackql-featured-image.png"
---
См. также:
[[` exec `]](/command-line-usage/exec) [[` INSERT `]](/language-spec/insert)

Переменные можно передавать скриптам StackQL в виде конфигурационных данных.  Конфигурационные данные StackQL могут быть в формате json или [jsonnet](https://jsonnet.org/ "Jsonnet").

Jsonnet предоставляет дополнительные возможности, такие как условная логика, импорт и наследование, а также циклические конструкции.

Данные можно передать в файлах `json` или `jsonnet`, на которые указывает параметр `--iqldata` команды `stackql exec`, или включить в скрипт `iql` с помощью декораторов `<<<` и `>>>`, как показано здесь:

```sql
<<<jsonnet
//
// jsonnet data
//
>>>
/* stackql statements ... */
```

## Пример конфигурации Jsonnet {#jsonnet-configuration-example}
В следующем примере показано, как с помощью Jsonnet передать конфигурационные данные скрипту StackQL.  Данные передаются с помощью параметра `--iqldata` команды `exec`, как показано ниже:

```shell
stackql exec -i myscript.iql \
--iqldata vars.jsonnet \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }'
```

### Данные Jsonnet {#jsonnet-data}
С помощью jsonnet можно повторно использовать переменные и применять массивы в циклических конструкциях вашего кода StackQL.  Приведенный ниже код jsonnet находится в файле `vars.jsonnet`, на который указывает параметр `--iqldata`.

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

### Ссылки на данные Jsonnet в инструкции StackQL {#referencing-jsonnet-data-in-a-stackql-statement}
В следующем фрагменте кода показано, как ссылаться на значения jsonnet из команды StackQL.

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

### Загрузка внешних переменных с помощью Jsonnet {#sourcing-external-variables-using-jsonnet}
В следующих фрагментах кода показано, как загрузить внешние переменные в файл конфигурации Jsonnet, из которого формируется запрос StackQL.

#### Запрос StackQL (`env-var-input.iql`) {#stackql-query-env-var-inputiql}

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

#### Файл данных Jsonnet (`vars.jsonnet`) {#jsonnet-data-file-varsjsonnet}

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

#### Выполнение из командной строки {#command-line-execution}

```
/path/to/stackql exec -i $(pwd)/env-var-input.iql  --var project=stackql-demo --iqldata $(pwd)/vars.jsonnet
```

## Пример конфигурации Json {#json-configuration-example}
Для передачи конфигурационных данных скрипту StackQL можно также использовать простой json, как показано ниже:

```shell
stackql exec -i myscript.iql \
--iqldata vars.json \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }'
```

### Данные Json {#json-data}

Приведенные ниже данные json находятся в файле `vars.json`, на который указывает параметр `--iqldata`.

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

### Ссылки на данные Json в инструкции StackQL {#referencing-json-data-in-a-stackql-statement}
На приведенные выше данные можно сослаться в инструкции `INSERT` StackQL, как показано здесь:

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

## Параметр dryrun для предварительного просмотра команды {#dryrun-option-to-preview-command}
С помощью параметра `--dryrun` можно вывести предварительно обработанный скрипт StackQL, полученный из вашего входного скрипта и файла данных `json` или `jsonnet`, как показано ниже:

```shell
./stackql exec -i myscript.iql \
--iqldata vars.json \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }' \
--dryrun \
--output text
```
