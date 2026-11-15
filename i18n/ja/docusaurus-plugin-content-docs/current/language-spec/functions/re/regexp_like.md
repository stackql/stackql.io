---
title: REGEXP_LIKE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の REGEXP_LIKE 関数：文字列が正規表現に一致するかを判定して true または false を返し、WHERE 句でのパターンによるフィルターに使用します。"
image: "/img/stackql-featured-image.png"
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

ソース文字列がパターンに一致するかどうかを確認し、`true` または `false` を返します。  

関連項目：  
[[` SELECT `]](/language-spec/select) [[ 正規表現リファレンス ]](/language-spec/functions/re/reference) 

* * * 

## 構文 {#syntax}

```sql
SELECT REGEXP_LIKE(source, pattern) FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*source*__  
正規表現（`pattern`）で定義したパターンが存在するかどうかを調べる、文字列リテラルまたは文字列型の列です。  

__*pattern*__  
検索する正規表現パターンです。  

## 戻り値 {#return-values}
`false` の場合は `0`、`true` の場合は `1` を返します。  

* * *

## 例 {#examples}

### 文字列にパターンが含まれるかどうかを判定する {#determine-if-a-string-contains-a-pattern}

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

詳細については、[https://github.com/nalgeon/sqlean/blob/main/docs/re.md](https://github.com/nalgeon/sqlean/blob/main/docs/re.md) を参照してください。
