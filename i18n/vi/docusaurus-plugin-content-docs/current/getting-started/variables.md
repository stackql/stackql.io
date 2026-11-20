---
title: Sử dụng biến
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Sử dụng biến trong StackQL: cung cấp giá trị từ tệp dữ liệu Jsonnet hoặc JSON, tham chiếu chúng trong câu lệnh, lấy giá trị bên ngoài và xem trước bằng chạy thử."
image: "/img/stackql-featured-image.png"
---
Xem thêm:  
[[` exec `]](/command-line-usage/exec) [[` INSERT `]](/language-spec/insert)

Biến có thể được cung cấp cho tập lệnh StackQL dưới dạng dữ liệu cấu hình. Dữ liệu cấu hình StackQL có thể ở dạng json hoặc [jsonnet](https://jsonnet.org/ "Jsonnet").

Jsonnet cung cấp thêm các tính năng như logic điều kiện, import và kế thừa, cũng như cấu trúc vòng lặp.

Dữ liệu có thể được cung cấp qua các tệp `json` hoặc `jsonnet` được tham chiếu qua tham số `--iqldata` của lệnh `stackql exec`, hoặc có thể được đưa vào tập lệnh `iql` bằng các decorator `<<<` và `>>>` như minh họa dưới đây:

```sql
<<<jsonnet
//
// jsonnet data
//
>>>
/* stackql statements ... */
```

## Ví dụ cấu hình Jsonnet {#jsonnet-configuration-example}
Ví dụ sau minh họa cách dùng Jsonnet để cung cấp dữ liệu cấu hình cho tập lệnh StackQL. Dữ liệu được cung cấp bằng tham số `--iqldata` của lệnh `exec` như bên dưới:

```shell
stackql exec -i myscript.iql \
--iqldata vars.jsonnet \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }'
```

### Dữ liệu Jsonnet {#jsonnet-data}
Với jsonnet, bạn có thể tái sử dụng biến và dùng mảng trong cấu trúc vòng lặp trong mã StackQL của mình. Đoạn jsonnet sau sẽ là nội dung của tệp `vars.jsonnet` được tham chiếu trong tham số `--iqldata`.

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

### Tham chiếu dữ liệu Jsonnet trong câu lệnh StackQL {#referencing-jsonnet-data-in-a-stackql-statement}
Đoạn mã sau minh họa cách tham chiếu giá trị jsonnet từ một lệnh StackQL.

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

### Lấy biến bên ngoài bằng Jsonnet {#sourcing-external-variables-using-jsonnet}
Các đoạn mã sau minh họa cách đưa biến bên ngoài vào tệp cấu hình Jsonnet dùng để render một truy vấn StackQL.

#### Truy vấn StackQL (`env-var-input.iql`) {#stackql-query-env-var-inputiql}

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

#### Tệp dữ liệu Jsonnet (`vars.jsonnet`) {#jsonnet-data-file-varsjsonnet}

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

#### Thực thi từ dòng lệnh {#command-line-execution}

```
/path/to/stackql exec -i $(pwd)/env-var-input.iql  --var project=stackql-demo --iqldata $(pwd)/vars.jsonnet
```

## Ví dụ cấu hình Json {#json-configuration-example}
Cũng có thể dùng json đơn giản để cung cấp dữ liệu cấu hình cho tập lệnh StackQL như bên dưới:

```shell
stackql exec -i myscript.iql \
--iqldata vars.json \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }'
```

### Dữ liệu Json {#json-data}

Dữ liệu json sau sẽ là nội dung của tệp `vars.json` được tham chiếu trong tham số `--iqldata`.

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

### Tham chiếu dữ liệu Json trong câu lệnh StackQL {#referencing-json-data-in-a-stackql-statement}
Dữ liệu ở trên sẽ được tham chiếu trong câu lệnh `INSERT` của StackQL như sau:

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

## Tùy chọn chạy thử để xem trước lệnh {#dryrun-option-to-preview-command}
Bạn có thể dùng tùy chọn `--dryrun` để xuất ra tập lệnh StackQL đã được tiền xử lý từ tập lệnh đầu vào và tệp dữ liệu `json` hoặc `jsonnet` của bạn như bên dưới:

```shell
./stackql exec -i myscript.iql \
--iqldata vars.json \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }' \
--dryrun \
--output text
```
