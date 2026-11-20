---
title: Usar variables
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Usar variables en StackQL: suministrar valores desde archivos de datos Jsonnet o JSON, referenciarlos en instrucciones, obtener valores externos y previsualizar con una ejecución de prueba."
image: "/img/stackql-featured-image.png"
---
Consulte también:  
[[` exec `]](/command-line-usage/exec) [[` INSERT `]](/language-spec/insert)

Las variables se pueden suministrar a los scripts de StackQL en forma de datos de configuración. Los datos de configuración de StackQL pueden estar en formato json o [jsonnet](https://jsonnet.org/ "Jsonnet").

Jsonnet ofrece funcionalidades adicionales como lógica condicional, importaciones y herencia, además de estructuras de bucle.

Los datos se pueden proporcionar mediante archivos `json` o `jsonnet` referenciados con el parámetro `--iqldata` del comando `stackql exec`, o se pueden incluir en un script `iql` con los decoradores `<<<` y `>>>`, como se muestra aquí:

```sql
<<<jsonnet
//
// jsonnet data
//
>>>
/* stackql statements ... */
```

## Ejemplo de configuración con Jsonnet {#jsonnet-configuration-example}
El siguiente ejemplo muestra el uso de Jsonnet para suministrar datos de configuración a un script de StackQL. Los datos se suministran con el parámetro `--iqldata` del comando `exec`, como se muestra a continuación:

```shell
stackql exec -i myscript.iql \
--iqldata vars.jsonnet \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }'
```

### Datos de Jsonnet {#jsonnet-data}
Con jsonnet puede reutilizar variables y usar arrays dentro de estructuras de bucle en su código de StackQL. El siguiente jsonnet sería el contenido del archivo `vars.jsonnet` al que hace referencia el parámetro `--iqldata`.

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

### Hacer referencia a datos de Jsonnet en una instrucción de StackQL {#referencing-jsonnet-data-in-a-stackql-statement}
El siguiente fragmento de código muestra cómo hacer referencia a valores de jsonnet desde un comando de StackQL.

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

### Obtener variables externas con Jsonnet {#sourcing-external-variables-using-jsonnet}
Los siguientes fragmentos de código muestran cómo incorporar variables externas a un archivo de configuración de Jsonnet que se usa para renderizar una consulta de StackQL.  

#### Consulta de StackQL (`env-var-input.iql`) {#stackql-query-env-var-inputiql}

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

#### Archivo de datos de Jsonnet (`vars.jsonnet`) {#jsonnet-data-file-varsjsonnet}

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

#### Ejecución en la línea de comandos {#command-line-execution}

```
/path/to/stackql exec -i $(pwd)/env-var-input.iql  --var project=stackql-demo --iqldata $(pwd)/vars.jsonnet
```

## Ejemplo de configuración con JSON {#json-configuration-example}
También se puede usar json simple para suministrar datos de configuración a un script de StackQL, como se muestra a continuación:

```shell
stackql exec -i myscript.iql \
--iqldata vars.json \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }'
```

### Datos JSON {#json-data}

Los siguientes datos json serían el contenido del archivo `vars.json` al que hace referencia el parámetro `--iqldata`.

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

### Hacer referencia a datos JSON en una instrucción de StackQL {#referencing-json-data-in-a-stackql-statement}
Los datos anteriores se referenciarían en una instrucción `INSERT` de StackQL como se muestra aquí:

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

## Opción dryrun para previsualizar un comando {#dryrun-option-to-preview-command}
Puede usar la opción `--dryrun` para obtener un script de StackQL preprocesado a partir de su script de entrada y de su archivo de datos `json` o `jsonnet`, como se muestra a continuación:

```shell
./stackql exec -i myscript.iql \
--iqldata vars.json \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }' \
--dryrun \
--output text
```
