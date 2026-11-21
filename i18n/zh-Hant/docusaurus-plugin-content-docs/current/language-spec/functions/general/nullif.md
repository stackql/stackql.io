---
title: NULLIF
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 NULLIF 函式：兩個引數相等時傳回 NULL，否則傳回第一個引數。"
image: "/img/stackql-featured-image.png"
---
接受兩個引數，若兩者相等則傳回 `NULL` 值，否則傳回第一個引數。  

> `NULLIF` 函式相當於 `CASE WHEN expression1 = expression2 THEN NULL ELSE expression1 END`

> 當結果集包含不是 `NULL`、但您希望在彙總時視為 `NULL` 的特殊值時，`NULLIF` 很適合搭配 [`AVG`](/language-spec/functions/aggregate/avg)、[`MAX`](/language-spec/functions/aggregate/max)、[`MIN`](/language-spec/functions/aggregate/min)、[`SUM`](/language-spec/functions/aggregate/sum) 與 [`COUNT`](/language-spec/functions/aggregate/count) 等彙總函式使用。

另請參閱：  
[[` SELECT `]](/language-spec/select) [[` COALESCE `]](/language-spec/functions/general/coalesce) [[` IFNULL `]](/language-spec/functions/general/ifnull)

* * * 

## 語法 {#syntax}

```sql
SELECT NULLIF(expression1, expression2) FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*expression*__  
兩個任何資料型別的運算式，其求值結果為 `NULL` 或非 `NULL` 值。

> `NULLIF` 函式會由左至右搜尋其引數，找出定義了定序函式的引數，並將該定序函式用於所有字串比較。如果兩個引數都未定義定序函式，則使用 `BINARY` 定序。

## 傳回值 {#return-values}

如果兩個 *expression* 引數求值為相同的值，則傳回 `NULL`，否則傳回第一個值。 

* * *

## 範例 {#examples}

### 計算欄位不等於指定值的記錄數 {#count-records-where-a-field-is-not-equal-to-a-given-value}

```sql
SELECT COUNT(NULLIF(name, 'controller-0'))
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
/*
equivalent to: 
SELECT COUNT(*) FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a' 
AND name != 'controller-0';
*/
```

如需詳細資訊，請參閱 [https://www.sqlite.org/lang_corefunc.html#nullif](https://www.sqlite.org/lang_corefunc.html#nullif)
