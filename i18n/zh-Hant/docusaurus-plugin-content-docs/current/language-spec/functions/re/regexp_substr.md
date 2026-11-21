---
title: REGEXP_SUBSTR
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 REGEXP_SUBSTR 函式：擷取輸入中符合正規表示式的子字串。"
image: "/img/stackql-featured-image.png"
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

從輸入字串傳回符合指定模式的子字串。  

另請參閱：  
[[` SELECT `]](/language-spec/select) [[ 正規表示式參考 ]](/language-spec/functions/re/reference) 

* * * 

## 語法 {#syntax}

```sql
SELECT REGEXP_SUBSTR(source, pattern) FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*source*__  
字串常值或字串資料行，您要依據由正規表示式（`pattern`）定義的模式從中擷取子字串。  

__*pattern*__  
要搜尋的正規表示式模式。  

## 傳回值 {#return-values}
傳回字串。

* * *

## 範例 {#examples}

### 依據正規表示式擷取子字串 {#extract-a-substring-based-upon-a-regular-expression}

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
json_extract(properties, '$.storageProfile.imageReference.offer') as os,
regexp_substr(
  json_extract(properties, '$.storageProfile.imageReference.exactVersion'), 
  '[0-9][0-9]\\.[0-9][0-9]') as os_version 
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

如需詳細資訊，請參閱 [https://github.com/google/re2/wiki/Syntax](https://github.com/google/re2/wiki/Syntax)。
