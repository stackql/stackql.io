---
title: TRUNC
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 TRUNC 函式：數字的整數部分，向零捨入並捨棄任何小數。"
image: "/img/stackql-featured-image.png"
---

透過向零捨入傳回數字的整數部分，去除任何小數部分。

另請參閱：  
[[`SELECT`]](/language-spec/select) 

* * * 

## 語法 {#syntax}

```sql
SELECT TRUNC(numeric_expression) FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*numeric_expression*__  
屬於精確數值或近似數值資料型別類別的運算式。

## 傳回值 {#return-values}
傳回截斷為整數部分的數值。

* * *

## 範例 {#examples}

### 將 Google Cloud Storage 儲存貯體建立至今的天數截斷為整數天 {#truncate-the-number-of-days-since-creation-of-google-cloud-storage-buckets-to-whole-days}

```sql
SELECT name, timeCreated,
TRUNC(julianday('now') - julianday(timeCreated)) as days_since
FROM google.storage.buckets WHERE project = 'stackql';
```
