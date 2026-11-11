---
title: SPLIT_PART
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 SPLIT_PART 函数：按分隔符拆分字符串并返回指定位置（从 1 开始）的部分，适合拆解资源 ID 和 ARN。"
image: "/img/stackql-featured-image.png"
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

按分隔符拆分源字符串，并返回指定的部分（从 1 开始计数）。  

另请参阅：  
[[` SELECT `]](/language-spec/select) 

* * * 

## 语法 {#syntax}

```sql
SELECT SPLIT_PART(source, sep, part) FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*source*__  
要执行拆分的字符串字面量或字符串列。

__*sep*__  
用作分隔符的一个或多个字符。  

> 如果 `sep` 由多个字符组成，则每个字符都被视为分隔符。  

> 仅支持 ASCII（1 字节）符号作为分隔符。  

__*part*__  
要返回的拆分后字符串的元素（从 1 开始）。负值用于从最后一个元素开始倒数。  

## 返回值 {#return-values}
返回一个字符串。

* * *

## 示例 {#examples}

### 从以正斜杠分隔的字符串中提取元素 {#extract-elements-from-a-string-seperated-by-a-forward-slash}


<Tabs
  defaultValue="iql"
  values={[
    { label: 'StackQL', value: 'iql', },
    { label: '结果', value: 'results', },
  ]
}>
<TabItem value="iql">

```sql
SELECT name,  
 split_part(id, '/', 3) as subscription,
 split_part(id, '/', 5) as resource_group,
 json_extract(properties, '$.hardwareProfile.vmSize') as vm_size
FROM azure.compute.virtual_machines 
 WHERE resource_group_name = 'stackql-ops-cicd-dev-01' 
 AND subscription_id = '273769f6-545f-45b2-8ab8-2f14ec5768dc';
```

</TabItem>
<TabItem value="results">

```
|------|--------------------------------------|-------------------------|-----------------|
| name |             subscription             |     resource_group      |     vm_size     |
|------|--------------------------------------|-------------------------|-----------------|
| test | 273769f6-545f-45b2-8ab8-2f14ec5768dc | stackql-ops-cicd-dev-01 | Standard_D2s_v3 |
|------|--------------------------------------|-------------------------|-----------------|
```

</TabItem>
</Tabs>

### 从 URL 中提取最后一个元素 {#extract-the-last-element-from-a-url}


<Tabs
  defaultValue="iql"
  values={[
    { label: 'StackQL', value: 'iql', },
    { label: '结果', value: 'results', },
  ]
}>
<TabItem value="iql">

```sql
SELECT
name,
SPLIT_PART(network, '/', -1) as network,
SPLIT_PART(region, '/', -1) as region
FROM google.compute.vpn_gateways WHERE project = 'stackql-demo';
```

</TabItem>
<TabItem value="results">

```
+------------------------+-----------------------+----------------------+
| name                   | network               | region               |
+------------------------+-----------------------+----------------------+
| gcp-aws-vpn-gateway    | stackql-shrd-vpc-nw   | australia-southeast1 |
+------------------------+-----------------------+----------------------+
```

</TabItem>
</Tabs>

有关更多信息，请参阅 [https://github.com/nalgeon/sqlean/blob/main/docs/text.md](https://github.com/nalgeon/sqlean/blob/main/docs/text.md)
