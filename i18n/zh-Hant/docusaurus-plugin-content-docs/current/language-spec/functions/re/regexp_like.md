---
title: REGEXP_LIKE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 REGEXP_LIKE 函式：測試字串是否符合正規表示式，真傳回 1、假傳回 0，適用於 WHERE 子句中的模式篩選。"
image: "/img/stackql-featured-image.png"
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

檢查來源字串是否符合模式，為真時傳回 `1`，為假時傳回 `0`。  

另請參閱：  
[[` SELECT `]](/language-spec/select) [[ 正規表示式參考 ]](/language-spec/functions/re/reference) 

* * * 

## 語法 {#syntax}

```sql
SELECT REGEXP_LIKE(source, pattern) FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*source*__  
字串常值或字串資料行，您要測試其中是否存在由正規表示式（`pattern`）定義的模式。  

__*pattern*__  
要搜尋的正規表示式模式。  

## 傳回值 {#return-values}
為真（來源符合模式）時傳回 `1`，為假時傳回 `0`。  

* * *

## 範例 {#examples}

### 判斷字串是否包含模式 {#determine-if-a-string-contains-a-pattern}

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
regexp_like(name, '^vm[0-9]+') as name_starts_with_vm 
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

如需詳細資訊，請參閱 [https://github.com/nalgeon/sqlean/blob/main/docs/re.md](https://github.com/nalgeon/sqlean/blob/main/docs/re.md)。
