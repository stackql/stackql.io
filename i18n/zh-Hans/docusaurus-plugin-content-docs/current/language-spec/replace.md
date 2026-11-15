---
title: REPLACE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 REPLACE：替换资源实例的所有字段，相当于 HTTP PUT，与 UPDATE 执行的部分更新相对。"
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

更新资源中的所有字段（等同于 `put` 操作）。

另请参阅：  
[[ StackQL 资源层次结构 ]](/getting-started/resource-hierarchy) [[ `UPDATE` ]](/language-spec/update)

* * * 

## 语法 {#syntax}

*replaceStatement::=*

<RailroadDiagram 
type="replace"
/>

&nbsp;  
&nbsp;

```sql
REPLACE [ /*+ AWAIT  */ ] <multipartIdentifier>
SET field_name = value [, field_name = value ...]
[ WHERE condition ];
```

* * *

## 示例 {#examples}

### 基本 `REPLACE` 语句 {#basic-replace-statement}
运行 `REPLACE` 语句，替换 GitHub 仓库的全部主题。

```sql
-- Replace all of the topics on a GitHub repository
REPLACE github.repos.topics
SET data__names = '["sql", "cloud-infrastructure"]'
WHERE owner = 'my-org'
AND repo = 'my-repo';
```

### `REPLACE` 语句（阻塞） {#replace-statement-blocking}
运行 `REPLACE` 语句，替换 Compute Engine 防火墙规则。这是一次阻塞（同步）调用。

```sql
-- Synchronous update of a google firewall 
REPLACE /*+ AWAIT */ google.compute.firewalls
SET
 data__name = 'test-fw',
 data__network = 'https://www.googleapis.com/compute/v1/projects/stackql-demo/global/networks/my-vpc',
 data__direction = 'INGRESS',
 data__sourceRanges = '["0.0.0.0/0"]',
 data__allowed = '[{"IPProtocol": "tcp", "ports": ["22"]}, {"IPProtocol": "tcp", "ports": ["6443"]},{"IPProtocol": "icmp"}]'
WHERE firewall = 'test-fw'
AND project = 'stackql-demo';
```

### 使用 JSON 函数的 `REPLACE` 语句 {#replace-statement-with-json-functions}
运行一个值由 JSON 函数构建的 `REPLACE` 语句。不读取任何列的函数会在发送请求之前求值。`SET` 的值不能读取正在替换的行，因此要修改现有值的一部分，请先用 `SELECT` 读取它，再设置修改后的值。

```sql
-- Replace the topics on a GitHub repository with a list built by json_array
REPLACE github.repos.topics
SET data__names = json_array('sql', 'cloud-infrastructure', 'infrastructure-as-code')
WHERE owner = 'my-org'
AND repo = 'my-repo';
```
