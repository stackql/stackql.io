---
title: UPPER
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 UPPER 函式：將字串轉換為大寫。"
image: "/img/stackql-featured-image.png"
---
以大寫形式傳回輸入字串。  

另請參閱：  
[[` SELECT `]](/language-spec/select) [[` LOWER `]](/language-spec/functions/string/lower) [[ 資料型別 ]](/language-spec/data-types)

* * * 

## 語法 {#syntax}

```sql
SELECT UPPER(character_expression) FROM <multipartIdentifier>;
```
> `upper()` 函式僅適用於 ASCII 字元。

## 引數 {#arguments}

__*character_expression*__  
ASCII 字串。

## 傳回值 {#return-values}
傳回字串值。

* * *

## 範例 {#examples}

### 以大寫傳回字串 {#return-a-string-in-uppercase}

```sql
SELECT UPPER(name), location FROM google.storage.buckets WHERE project = 'stackql-demo';
```

如需詳細資訊，請參閱 [https://www.sqlite.org/lang_corefunc.html#upper](https://www.sqlite.org/lang_corefunc.html#upper)。
