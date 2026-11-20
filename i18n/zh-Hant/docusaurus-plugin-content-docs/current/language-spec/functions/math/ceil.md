---
title: CEIL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 CEIL 函式：將數值引數無條件進位至最接近的整數。"
image: "/img/stackql-featured-image.png"
---
將數值引數無條件進位至最接近的整數。  

另請參閱：  
[[`SELECT`]](/language-spec/select) 

* * * 

## 語法 {#syntax}

```sql
SELECT CEIL(numeric_expression) FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*numeric_expression*__  
屬於精確數值或近似數值資料型別類別的運算式。

## 傳回值 {#return-values}
傳回大於或等於指定數值運算式的最小整數。

* * *

## 範例 {#examples}

### 將浮點數無條件進位至最接近的（較大）整數 {#round-up-a-floating-point-number-to-the-closest-larger-integer}

```sql
SELECT name, timeCreated,
ceil(julianday('now')-julianday(timeCreated)) as days_since_ceiling
FROM google.storage.buckets WHERE project = 'stackql';
```
