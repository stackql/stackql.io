---
title: REGEXP_SUBSTR
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の REGEXP_SUBSTR 関数：入力のうち正規表現に一致する部分文字列を抽出します。"
image: "/img/stackql-featured-image.png"
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

入力文字列から、指定したパターンに一致する部分文字列を返します。  

関連項目：  
[[` SELECT `]](/language-spec/select) [[ 正規表現リファレンス ]](/language-spec/functions/re/reference) 

* * * 

## 構文 {#syntax}

```sql
SELECT REGEXP_SUBSTR(source, pattern) FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*source*__  
正規表現（`pattern`）で定義したパターンに基づいて部分文字列を抽出する、文字列リテラルまたは文字列型の列です。  

__*pattern*__  
検索する正規表現パターンです。  

## 戻り値 {#return-values}
文字列を返します。

* * *

## 例 {#examples}

### 正規表現に基づいて部分文字列を抽出する {#extract-a-substring-based-upon-a-regular-expression}

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

詳細については、[https://github.com/nalgeon/sqlean/blob/main/docs/re.md](https://github.com/nalgeon/sqlean/blob/main/docs/re.md) を参照してください。
