---
title: REGEXP_REPLACE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の REGEXP_REPLACE 関数：文字列のうち正規表現に一致する部分を置換文字列に置き換えます。"
image: "/img/stackql-featured-image.png"
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

一致する部分文字列を置換文字列に置き換えます。  

関連項目：  
[[` SELECT `]](/language-spec/select) [[ 正規表現リファレンス ]](/language-spec/functions/re/reference) 

* * * 

## 構文 {#syntax}

```sql
SELECT REGEXP_REPLACE(source, pattern, replacement) FROM <multipartIdentifier>;
```
## 引数 {#arguments}

__*source*__  
正規表現 `pattern` に一致する部分を `replacement` で置換する対象の、文字列リテラルまたは文字列型の列です。  

__*pattern*__  
検索する正規表現パターンです。  

__*replacement*__  
置換文字列です。

> 置換文字列は記述したとおりに挿入されます。`$1` のようなキャプチャグループ参照は展開されません。  

## 戻り値 {#return-values}
文字列を返します。

* * *

## 例 {#examples}

### 列内の正規表現パターンを置換する {#replace-a-regular-expression-pattern-in-a-column}

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

詳細については、[https://github.com/google/re2/wiki/Syntax](https://github.com/google/re2/wiki/Syntax) を参照してください。
