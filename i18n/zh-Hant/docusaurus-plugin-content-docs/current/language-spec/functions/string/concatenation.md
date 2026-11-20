---
title: 串連運算子
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 || 串連運算子：將兩個或多個字串值合併為一個字串。"
image: "/img/stackql-featured-image.png"
---
傳回串連（即合併）兩個或多個字串值後所產生的字串。  

另請參閱：  
[[` SELECT `]](/language-spec/select) 

* * * 

## 語法 {#syntax}

```sql
SELECT character_expression1 || character_expression2 FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*character_expression*__  
字串常值或字串資料行。

## 傳回值 {#return-values}

傳回字串。

:::note

從 StackQL `v1.0` 起，剖析器將 `||` 視為串連運算子，其優先順序高於比較運算子，因此 `WHERE id = prefix || suffix` 會將 `id` 與串連後的字串進行比較。較早的發行版本在語彙分析時將 `||` 視為布林 `OR`。布林析取請使用 `OR`。[工作階段變數](/getting-started/session-variables)也可以串連：`$prefix || '-' || $suffix`。

:::

* * *

## 範例 {#examples}

### 串連兩個欄位 {#concatenate-two-fields-together}

```sql
SELECT name || ' (' || location || ')' as bucket_location
FROM google.storage.buckets 
WHERE project = 'stackql-demo';
```

如需詳細資訊，請參閱 [https://sqlite.org/lang_expr.html](https://sqlite.org/lang_expr.html)。
