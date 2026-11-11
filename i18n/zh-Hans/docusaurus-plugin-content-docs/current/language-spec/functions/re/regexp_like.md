---
title: REGEXP_LIKE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 REGEXP_LIKE 函数：测试字符串是否与正则表达式匹配，返回 true 或 false，用于 WHERE 子句中的模式过滤。"
image: "/img/stackql-featured-image.png"
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

检查源字符串是否与模式匹配，并返回 `true` 或 `false`。  

另请参阅：  
[[` SELECT `]](/language-spec/select) [[ 正则表达式参考 ]](/language-spec/functions/re/reference) 

* * * 

## 语法 {#syntax}

```sql
SELECT REGEXP_LIKE(source, pattern) FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*source*__  
要测试其中是否存在由正则表达式（`pattern`）定义的模式的字符串字面量或字符串列。  

__*pattern*__  
要搜索的正则表达式模式。  

## 返回值 {#return-values}
为 `false` 时返回 `0`，为 `true` 时返回 `1`。  

* * *

## 示例 {#examples}

### 判断字符串是否包含某个模式 {#determine-if-a-string-contains-a-pattern}

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
regexp_like(name, 'vm[0-9]+') as name_starts_with_vm 
from azure.compute.virtual_machines 
WHERE subscription_id = '273769f6-545f-45b2-8ab8-2f14ec5768dc';
```

</TabItem>
<TabItem value="results">

```
|------|---------------------|
| name | name_starts_with_vm |
|------|---------------------|
| vm0  |                   1 |
|------|---------------------|
| vm1  |                   1 |
|------|---------------------|
| test |                   0 |
|------|---------------------|
```

</TabItem>
</Tabs>

有关更多信息，请参阅 [https://github.com/nalgeon/sqlean/blob/main/docs/re.md](https://github.com/nalgeon/sqlean/blob/main/docs/re.md)
