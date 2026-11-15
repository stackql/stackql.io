---
title: UPDATE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 UPDATE：更改资源实例的指定字段，相当于 HTTP PATCH，目标由 WHERE 子句确定。"
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

更新资源中的指定字段（与 `patch` 操作同义）。

另请参阅：  
[[ StackQL 资源层次结构 ]](/getting-started/resource-hierarchy) [[ `REPLACE` ]](/language-spec/replace)

* * * 

## 语法 {#syntax}

*updateStatement::=*

<RailroadDiagram 
type="update"
/>

&nbsp;  
&nbsp;

```sql
UPDATE [ /*+ AWAIT  */ ] <multipartIdentifier>
SET field_name = value [, field_name = value ...]
[ WHERE condition ];
```

* * *

## 示例 {#examples}

### 基本 `UPDATE` 语句 {#basic-update-statement}
运行 `UPDATE` 语句，设置 Google Cloud Storage 存储桶的标签。

```sql
-- Set the labels on a Google Cloud Storage bucket
UPDATE google.storage.buckets
SET data__labels = '{"env": "prod", "team": "platform"}'
WHERE bucket = 'stackql-demo-bucket';
```

### `UPDATE` 语句（阻塞） {#update-statement-blocking}
运行 `UPDATE` 语句，修改 Compute Engine 防火墙规则。这是一次阻塞（同步）调用。

```sql
-- Synchronous update of a google firewall 
UPDATE /*+ AWAIT */ google.compute.firewalls
SET
 data__network = 'https://www.googleapis.com/compute/v1/projects/stackql-demo/global/networks/my-vpc',
 data__direction = 'INGRESS',
 data__sourceRanges = '["0.0.0.0/0"]',
 data__allowed = '[{"IPProtocol": "tcp", "ports": ["22"]}, {"IPProtocol": "tcp", "ports": ["6443"]},{"IPProtocol": "icmp"}]'
WHERE firewall = 'test-fw'
AND project = 'stackql-demo';
```

### 使用 JSON 函数的 `UPDATE` 语句 {#update-statement-with-json-functions}
运行一个值由 JSON 函数构建的 `UPDATE` 语句。不读取任何列的函数会在发送请求之前求值。`SET` 的值不能读取正在更新的行，因此要修改现有值的一部分，请先用 `SELECT` 读取它，再设置修改后的值。

```sql
-- Set the protocols and ports a firewall rule allows
UPDATE google.compute.firewalls
SET data__allowed = json_array(
  json_object('IPProtocol', 'tcp', 'ports', json_array('22', '443')),
  json_object('IPProtocol', 'icmp')
)
WHERE firewall = 'test-fw'
AND project = 'stackql-demo';
```
