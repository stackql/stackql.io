---
title: Using Variables
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Query and Deploy Cloud Infrastructure and Resources using SQL
custom_edit_url: null
image: https://storage.googleapis.com/stackql-web-assets/blog/stackql-blog-post-featured-image.png
---
See also:  
[[` exec `]](/docs/command-line-usage/exec) [[` INSERT `]](/docs/language-spec/insert)

Variables can be supplied in the form of configuration data to StackQL scripts.  StackQL configuration data can be in the form of json or [jsonnet](https://jsonnet.org/ "Jsonnet").

Jsonnet provides additional features such as conditional logic, imports and inheritence, as well as loop structures.

Data can be provided through `json` or `jsonnet` files referenced through the `--iqldata` parameter of the `stackql exec` command or can be included in an `iql` script using the following `<<<` and `>>>` decorators as shown here:

```sql
<<<jsonnet
//
// jsonnet data
//
>>>
/* stackql statements ... */
```

### Jsonnet Configuration Example
The following example demonstrates the use of Jsonnet to supply configuration data to an StackQL script.  Data is supplied using the `--iqldata` parameter to the `exec` command as shown below:

```shell
stackql exec -i myscript.iql \
--iqldata vars.jsonnet \
--keyfilepath stackql-demo.json
```

#### Jsonnet Data
Using jsonnet you can re-use variables and use arrays within loop structures in your StackQL code.  The following jsonnet would be in the contents of the `vars.jsonnet` file referenced in the `--iqldata` parameter.

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

#### Referencing Jsonnet Data in an StackQL statement
The following code snippet demonstrates how to reference jsonnet values from an StackQL command.

```sql
--
-- create firewall rules
--
{{range .firewalls}}
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
'{{ .global.project }}',
'{{.name}}',
'{{.network}}',
'{{.direction}}',
'{{.sourceRanges}}',
'{{.allowed}}';
{{end}}
```

### Json Configuration Example
Simple json can also be used to supply configuration data to an StackQL script as shown below:

```shell
stackql exec -i myscript.iql \
--iqldata vars.json \
--keyfilepath stackql-demo.json
```

#### Json Data

```json
The following json data would be in the contents of the `vars.json` file referenced in the `--iqldata` parameter.

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

#### Referencing Json Data in an StackQL statement
The data shown above would be referenced in an StackQL `INSERT` statement as shown here:

```sql
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

### Dryrun Option to Preview Command
You can use the `--dryrun` option to output a preprocessed StackQL script using your input script and `json` or `jsonnet` data file as shown below:

```shell
./stackql exec -i myscript.iql \
--iqldata vars.json \
--keyfilepath stackql-demo.json \
--dryrun \
--output text
```