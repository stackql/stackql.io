---
title: Menggunakan variabel
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Menggunakan variabel di StackQL: memasok nilai dari file data Jsonnet atau JSON, merujuknya dalam pernyataan, mengambil nilai eksternal, dan meninjau dengan uji coba."
image: "/img/stackql-featured-image.png"
---
Lihat juga:  
[[` exec `]](/command-line-usage/exec) [[` INSERT `]](/language-spec/insert)

Variabel dapat diberikan ke skrip StackQL dalam bentuk data konfigurasi. Data konfigurasi StackQL dapat berupa json atau [jsonnet](https://jsonnet.org/ "Jsonnet").

Jsonnet menyediakan fitur tambahan seperti logika kondisional, impor dan pewarisan, serta struktur perulangan.

Data dapat disediakan melalui file `json` atau `jsonnet` yang dirujuk melalui parameter `--iqldata` dari perintah `stackql exec`, atau dapat disertakan dalam skrip `iql` menggunakan dekorator `<<<` dan `>>>` seperti yang ditunjukkan berikut:

```sql
<<<jsonnet
//
// jsonnet data
//
>>>
/* stackql statements ... */
```

## Contoh konfigurasi Jsonnet {#jsonnet-configuration-example}
Contoh berikut menunjukkan penggunaan Jsonnet untuk menyediakan data konfigurasi ke skrip StackQL. Data disediakan menggunakan parameter `--iqldata` pada perintah `exec` seperti yang ditunjukkan di bawah ini:

```shell
stackql exec -i myscript.iql \
--iqldata vars.jsonnet \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }'
```

### Data Jsonnet {#jsonnet-data}
Dengan jsonnet, Anda dapat menggunakan kembali variabel dan menggunakan array di dalam struktur perulangan dalam kode StackQL Anda. Jsonnet berikut akan menjadi isi file `vars.jsonnet` yang dirujuk dalam parameter `--iqldata`.

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

### Merujuk data Jsonnet dalam pernyataan StackQL {#referencing-jsonnet-data-in-a-stackql-statement}
Cuplikan kode berikut menunjukkan cara merujuk nilai jsonnet dari perintah StackQL.

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

### Mengambil variabel eksternal menggunakan Jsonnet {#sourcing-external-variables-using-jsonnet}
Cuplikan kode berikut menunjukkan cara mengambil variabel eksternal ke dalam file konfigurasi Jsonnet yang digunakan untuk merender kueri StackQL.

#### Kueri StackQL (`env-var-input.iql`) {#stackql-query-env-var-inputiql}

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

#### File data Jsonnet (`vars.jsonnet`) {#jsonnet-data-file-varsjsonnet}

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

#### Menjalankan di baris perintah {#command-line-execution}

```
/path/to/stackql exec -i $(pwd)/env-var-input.iql  --var project=stackql-demo --iqldata $(pwd)/vars.jsonnet
```

## Contoh konfigurasi Json {#json-configuration-example}
Format json sederhana juga dapat digunakan untuk menyediakan data konfigurasi ke skrip StackQL seperti yang ditunjukkan di bawah ini:

```shell
stackql exec -i myscript.iql \
--iqldata vars.json \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }'
```

### Data Json {#json-data}

Data json berikut akan menjadi isi file `vars.json` yang dirujuk dalam parameter `--iqldata`.

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

### Merujuk data Json dalam pernyataan StackQL {#referencing-json-data-in-a-stackql-statement}
Data yang ditampilkan di atas akan dirujuk dalam pernyataan `INSERT` StackQL seperti yang ditunjukkan berikut:

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

## Opsi dryrun untuk melihat pratinjau perintah {#dryrun-option-to-preview-command}
Anda dapat menggunakan opsi `--dryrun` untuk menghasilkan output skrip StackQL yang telah diproses awal dari skrip input dan file data `json` atau `jsonnet` Anda, seperti yang ditunjukkan di bawah ini:

```shell
./stackql exec -i myscript.iql \
--iqldata vars.json \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }' \
--dryrun \
--output text
```
