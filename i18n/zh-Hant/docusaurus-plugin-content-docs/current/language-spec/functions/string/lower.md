---
title: LOWER
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 LOWER 函式：將字串轉換為小寫。"
image: "/img/stackql-featured-image.png"
---
以小寫傳回輸入字串。  

另請參閱：  
[[` SELECT `]](/language-spec/select) [[` UPPER `]](/language-spec/functions/string/upper) [[ 資料型別 ]](/language-spec/data-types)

* * * 

## 語法 {#syntax}

```sql
SELECT LOWER(character_expression) FROM <multipartIdentifier>;
```
> `lower()` 函式僅適用於 ASCII 字元。

## 引數 {#arguments}

__*character_expression*__  
ASCII 字串。

## 傳回值 {#return-values}
傳回字串值。

* * *

## 範例 {#examples}

### 以小寫傳回字串 {#return-a-string-in-lowercase}

```sql
SELECT name, LOWER(location) FROM google.storage.buckets WHERE project = 'stackql-demo';
```

如需詳細資訊，請參閱 [https://www.sqlite.org/lang_corefunc.html#lower](https://www.sqlite.org/lang_corefunc.html#lower)。
