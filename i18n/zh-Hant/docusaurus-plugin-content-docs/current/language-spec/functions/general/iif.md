---
title: IIF
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 IIF 函式：依條件是否成立傳回兩個值其中之一，是內嵌的 if-then-else 運算式。"
image: "/img/stackql-featured-image.png"
---
根據輸入運算式的求值結果傳回值。  

另請參閱：  
[[` SELECT `]](/language-spec/select)

* * * 

## 語法 {#syntax}

```sql
SELECT IIF(expression, result_if_true, result_if_false) 
FROM <multipartIdentifier>;
```
> `iif()` 函式在邏輯上等同於 `CASE` 運算式 `CASE WHEN expression THEN result_if_true ELSE result_if_false END`

## 引數 {#arguments}

__*expression*__  
要求值的布林運算式。

:::tip 

*expression* 引數可以是 `JSON_EXTRACT` 這類巢狀函式。

:::

__*result_if_true*__  
*expression* 求值為 `true` 時要傳回的結果。

__*result_if_false*__  
*expression* 求值為 `false` 時要傳回的結果。

## 傳回值 {#return-values}
*result_if_true* 或 *result_if_false* 引數的資料型別。

* * *

## 範例 {#examples}

### 將 compute engine 執行個體的磁碟大小分類 {#categorize-disk-sizes-of-compute-engine-instances}

```sql
SELECT iif(CAST(json_extract(disks, '$[0].diskSizeGb') AS INTEGER) > 10, '> 10GB', '<= 10GB') as disk_size
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

如需詳細資訊，請參閱 [https://www.sqlite.org/lang_corefunc.html#iif](https://www.sqlite.org/lang_corefunc.html#iif)
