---
title: REGEXP_REPLACE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 REGEXP_REPLACE 函式：將字串中符合正規表示式的部分取代為取代字串。"
image: "/img/stackql-featured-image.png"
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

將相符的子字串取代為取代字串。  

另請參閱：  
[[` SELECT `]](/language-spec/select) [[ 正規表示式參考 ]](/language-spec/functions/re/reference) 

* * * 

## 語法 {#syntax}

```sql
SELECT REGEXP_REPLACE(source, pattern, replacement) FROM <multipartIdentifier>;
```
## 引數 {#arguments}

__*source*__  
字串常值或字串資料行，其中符合正規表示式 `pattern` 的部分會取代為 `replacement`。  

__*pattern*__  
要搜尋的正規表示式模式。  

__*replacement*__  
取代字串。

> 取代字串會依原樣插入：不會展開 `$1` 等擷取群組參考。  

## 傳回值 {#return-values}
傳回字串。

* * *

## 範例 {#examples}

### 取代資料行中的正規表示式模式 {#replace-a-regular-expression-pattern-in-a-column}

<Tabs
  defaultValue="iql"
  values={[
    { label: 'StackQL', value: 'iql', },
    { label: '結果', value: 'results', },
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

如需詳細資訊，請參閱 [https://github.com/google/re2/wiki/Syntax](https://github.com/google/re2/wiki/Syntax)。
