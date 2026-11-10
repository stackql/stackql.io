---
title: Variablen verwenden
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "Variablen in StackQL verwenden: Werte aus Jsonnet- oder JSON-Datendateien liefern, sie in Anweisungen referenzieren, externe Werte beziehen und mit Dry Run eine Vorschau anzeigen."
image: "/img/stackql-featured-image.png"
---
Siehe auch:  
[[` exec `]](/command-line-usage/exec) [[` INSERT `]](/language-spec/insert)

Variablen können StackQL-Skripten in Form von Konfigurationsdaten übergeben werden.  StackQL-Konfigurationsdaten können als json oder [jsonnet](https://jsonnet.org/ "Jsonnet") vorliegen.

Jsonnet bietet zusätzliche Funktionen wie bedingte Logik, Imports und Vererbung sowie Schleifenstrukturen.

Daten können über `json`- oder `jsonnet`-Dateien bereitgestellt werden, die über den Parameter `--iqldata` des Befehls `stackql exec` referenziert werden, oder mit den folgenden Dekoratoren `<<<` und `>>>` in ein `iql`-Skript eingebettet werden, wie hier gezeigt:

```sql
<<<jsonnet
//
// jsonnet data
//
>>>
/* stackql statements ... */
```

## Beispiel für eine Jsonnet-Konfiguration {#jsonnet-configuration-example}
Das folgende Beispiel zeigt die Verwendung von Jsonnet, um einem StackQL-Skript Konfigurationsdaten zu liefern.  Die Daten werden über den Parameter `--iqldata` des Befehls `exec` übergeben, wie unten gezeigt:

```shell
stackql exec -i myscript.iql \
--iqldata vars.jsonnet \
--keyfilepath stackql-demo.json
```

### Jsonnet-Daten {#jsonnet-data}
Mit jsonnet können Sie Variablen wiederverwenden und Arrays in Schleifenstrukturen in Ihrem StackQL-Code nutzen.  Das folgende jsonnet wäre der Inhalt der Datei `vars.jsonnet`, die im Parameter `--iqldata` referenziert wird.

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

### Jsonnet-Daten in einer StackQL-Anweisung referenzieren {#referencing-jsonnet-data-in-an-stackql-statement}
Der folgende Codeausschnitt zeigt, wie jsonnet-Werte aus einem StackQL-Befehl referenziert werden.

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

### Externe Variablen mit Jsonnet beziehen {#sourcing-external-variables-using-jsonnet}
Die folgenden Codeausschnitte zeigen, wie externe Variablen in eine Jsonnet-Konfigurationsdatei bezogen werden, die zum Rendern einer StackQL-Abfrage verwendet wird.  

#### StackQL-Abfrage (`env-var-input.iql`) {#stackql-query-env-var-inputiql}

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

#### Jsonnet-Datendatei (`vars.jsonnet`) {#jsonnet-data-file-varsjsonnet}

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

#### Ausführung auf der Kommandozeile {#command-line-execution}

```
/path/to/stackql exec -i $(pwd)/env-var-input.iql  --var project=stackql-demo --iqldata $(pwd)/vars.jsonnet
```

## Beispiel für eine Json-Konfiguration {#json-configuration-example}
Auch einfaches json kann verwendet werden, um einem StackQL-Skript Konfigurationsdaten zu liefern, wie unten gezeigt:

```shell
stackql exec -i myscript.iql \
--iqldata vars.json \
--keyfilepath stackql-demo.json
```

### Json-Daten {#json-data}

Die folgenden JSON-Daten stehen in der Datei `vars.json`, auf die der Parameter `--iqldata` verweist.

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

### Json-Daten in einer StackQL-Anweisung referenzieren {#referencing-json-data-in-an-stackql-statement}
Die oben gezeigten Daten würden in einer StackQL-`INSERT`-Anweisung wie hier gezeigt referenziert:

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

## Dryrun-Option zur Vorschau des Befehls {#dryrun-option-to-preview-command}
Mit der Option `--dryrun` können Sie ein vorverarbeitetes StackQL-Skript aus Ihrem Eingabeskript und Ihrer `json`- oder `jsonnet`-Datendatei ausgeben, wie unten gezeigt:

```shell
./stackql exec -i myscript.iql \
--iqldata vars.json \
--keyfilepath stackql-demo.json \
--dryrun \
--output text
```
