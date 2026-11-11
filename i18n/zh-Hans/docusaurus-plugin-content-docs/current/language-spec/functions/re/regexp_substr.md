---
title: REGEXP_SUBSTR
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 REGEXP_SUBSTR 函数：提取输入中与正则表达式匹配的子字符串。"
image: "/img/stackql-featured-image.png"
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

从输入字符串中返回与指定模式匹配的子字符串。  

另请参阅：  
[[` SELECT `]](/language-spec/select) [[ 正则表达式参考 ]](/language-spec/functions/re/reference) 

* * * 

## 语法 {#syntax}

```sql
SELECT REGEXP_SUBSTR(source, pattern) FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*source*__  
要根据由正则表达式（`pattern`）定义的模式从中提取子字符串的字符串字面量或字符串列。  

__*pattern*__  
要搜索的正则表达式模式。  

## 返回值 {#return-values}
返回一个字符串。

* * *

## 示例 {#examples}

### 根据正则表达式提取子字符串 {#extract-a-substring-based-upon-a-regular-expression}

<Tabs
  defaultValue="iql"
  values={[
    { label: 'StackQL', value: 'iql', },
    { label: '结果', value: 'results', },
  ]
}>
<TabItem value="iql">

```sql
select name,
json_extract(properties, '$.storageProfile.imageReference.offer') as os,
regexp_substr(
  json_extract(properties, '$.storageProfile.imageReference.exactVersion'), 
  '[0-9][0-9]\.[0-9][0-9]') as os_version 
from azure.compute.virtual_machines 
WHERE subscription_id = '273769f6-545f-45b2-8ab8-2f14ec5768dc';
```

</TabItem>
<TabItem value="results">

```
|------|---------------|------------|
| name |      os       | os_version |
|------|---------------|------------|
| vm0  | UbuntuServer  |      16.04 |
|------|---------------|------------|
| vm1  | UbuntuServer  |      16.04 |
|------|---------------|------------|
| vm2  | UbuntuServer  |      20.04 |
|------|---------------|------------|
```

</TabItem>
</Tabs>

有关更多信息，请参阅 [https://github.com/nalgeon/sqlean/blob/main/docs/re.md](https://github.com/nalgeon/sqlean/blob/main/docs/re.md)
