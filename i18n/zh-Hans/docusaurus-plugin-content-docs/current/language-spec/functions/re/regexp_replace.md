---
title: REGEXP_REPLACE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 REGEXP_REPLACE 函数：将字符串中与正则表达式匹配的部分替换为替换字符串。"
image: "/img/stackql-featured-image.png"
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

将匹配的子字符串替换为替换字符串。  

另请参阅：  
[[` SELECT `]](/language-spec/select) [[ 正则表达式参考 ]](/language-spec/functions/re/reference) 

* * * 

## 语法 {#syntax}

```sql
SELECT REGEXP_REPLACE(source, pattern, replacement) FROM <multipartIdentifier>;
```
## 参数 {#arguments}

__*source*__  
要根据由正则表达式（`pattern`）定义的模式从中提取子字符串的字符串字面量或字符串列。  

__*pattern*__  
要搜索的正则表达式模式。  

__*replacement*__  
替换字符串。

> 替换字符串中支持对捕获组 `\1` 到 `\9` 的反向引用。  

## 返回值 {#return-values}
返回一个字符串。

* * *

## 示例 {#examples}

### 替换列中的正则表达式模式 {#replace-a-regular-expression-pattern-in-a-column}

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
regexp_replace(json_extract(properties, '$.timeCreated'), 'T.*', '') as date_created
from azure.compute.virtual_machines 
WHERE subscription_id = '273769f6-545f-45b2-8ab8-2f14ec5768dc';
```

</TabItem>
<TabItem value="results">

```
|------|--------------|
| name | date_created |
|------|--------------|
| vm0  | 2022-09-14   |
|------|--------------|
| vm1  | 2022-09-14   |
|------|--------------|
| vm2  | 2022-09-14   |
|------|--------------|
```

</TabItem>
</Tabs>

有关更多信息，请参阅 [https://github.com/nalgeon/sqlean/blob/main/docs/re.md](https://github.com/nalgeon/sqlean/blob/main/docs/re.md)
