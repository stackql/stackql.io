---
title: Utiliser des variables
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Utiliser des variables dans StackQL : valeurs fournies par des fichiers Jsonnet ou JSON, références dans les instructions, valeurs externes et aperçu par exécution à blanc."
image: "/img/stackql-featured-image.png"
---
Voir aussi :  
[[` exec `]](/command-line-usage/exec) [[` INSERT `]](/language-spec/insert)

Des variables peuvent être fournies aux scripts StackQL sous forme de données de configuration. Les données de configuration StackQL peuvent être au format json ou [jsonnet](https://jsonnet.org/ "Jsonnet").

Jsonnet apporte des fonctionnalités supplémentaires telles que la logique conditionnelle, les imports et l'héritage, ainsi que des structures de boucle.

Les données peuvent être fournies par des fichiers `json` ou `jsonnet` référencés par le paramètre `--iqldata` de la commande `stackql exec`, ou être incluses dans un script `iql` à l'aide des décorateurs `<<<` et `>>>` suivants, comme illustré ici :

```sql
<<<jsonnet
//
// jsonnet data
//
>>>
/* stackql statements ... */
```

## Exemple de configuration Jsonnet {#jsonnet-configuration-example}
L'exemple suivant montre comment utiliser Jsonnet pour fournir des données de configuration à un script StackQL. Les données sont fournies avec le paramètre `--iqldata` de la commande `exec`, comme illustré ci-dessous :

```shell
stackql exec -i myscript.iql \
--iqldata vars.jsonnet \
--keyfilepath stackql-demo.json
```

### Données Jsonnet {#jsonnet-data}
Avec jsonnet, vous pouvez réutiliser des variables et utiliser des tableaux dans des structures de boucle de votre code StackQL. Le jsonnet suivant constitue le contenu du fichier `vars.jsonnet` référencé par le paramètre `--iqldata`.

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

### Référencer des données Jsonnet dans une instruction StackQL {#referencing-jsonnet-data-in-an-stackql-statement}
L'extrait de code suivant montre comment référencer des valeurs jsonnet depuis une commande StackQL.

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

### Récupérer des variables externes avec Jsonnet {#sourcing-external-variables-using-jsonnet}
Les extraits de code suivants montrent comment récupérer des variables externes dans un fichier de configuration Jsonnet utilisé pour produire une requête StackQL.  

#### Requête StackQL (`env-var-input.iql`) {#stackql-query-env-var-inputiql}

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

#### Fichier de données Jsonnet (`vars.jsonnet`) {#jsonnet-data-file-varsjsonnet}

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

#### Exécution en ligne de commande {#command-line-execution}

```
/path/to/stackql exec -i $(pwd)/env-var-input.iql  --var project=stackql-demo --iqldata $(pwd)/vars.jsonnet
```

## Exemple de configuration Json {#json-configuration-example}
Du json simple peut aussi servir à fournir des données de configuration à un script StackQL, comme illustré ci-dessous :

```shell
stackql exec -i myscript.iql \
--iqldata vars.json \
--keyfilepath stackql-demo.json
```

### Données Json {#json-data}

Les données json suivantes constituent le contenu du fichier `vars.json` référencé par le paramètre `--iqldata`.

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

### Référencer des données Json dans une instruction StackQL {#referencing-json-data-in-an-stackql-statement}
Les données présentées ci-dessus sont référencées dans une instruction `INSERT` StackQL, comme illustré ici :

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

## Option dryrun pour prévisualiser une commande {#dryrun-option-to-preview-command}
Vous pouvez utiliser l'option `--dryrun` pour afficher un script StackQL prétraité à partir de votre script d'entrée et de votre fichier de données `json` ou `jsonnet`, comme illustré ci-dessous :

```shell
./stackql exec -i myscript.iql \
--iqldata vars.json \
--keyfilepath stackql-demo.json \
--dryrun \
--output text
```
