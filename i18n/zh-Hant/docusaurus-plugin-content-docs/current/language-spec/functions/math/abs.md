---
title: ABS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 ABS 函式：數值引數的絕對值。"
image: "/img/stackql-featured-image.png"
---
傳回數值引數的絕對值。  

另請參閱：  
[[` SELECT `]](/language-spec/select) 

* * * 

## 語法 {#syntax}

```sql
SELECT ABS(numeric_expression) FROM <multipartIdentifier>;
```

> 如果 `X` 為 `NULL`，`abs(X)` 會傳回 `NULL`。如果 `X` 是無法轉換為數值的字串或 blob，`abs(X)` 會傳回 `0.0`。如果 `X` 是整數 -9223372036854775808，`abs(X)` 會擲回整數溢位錯誤，因為沒有對應的 64 位元二補數正值。

## 引數 {#arguments}

__*numeric_expression*__  
屬於精確數值或近似數值資料型別類別的運算式。

## 傳回值 {#return-values}

傳回正數值。

* * *

## 範例 {#examples}

### 傳回數字的絕對值 {#return-the-absolute-value-of-a-number}

```sql
SELECT name, abs(strftime('%s', creationTimestamp)-strftime('%s','now')) as abs_value
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

如需詳細資訊，請參閱 [https://www.sqlite.org/lang_mathfunc.html](https://www.sqlite.org/lang_mathfunc.html)。
