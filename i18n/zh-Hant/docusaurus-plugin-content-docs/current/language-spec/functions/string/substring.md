---
title: SUBSTRING
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 SUBSTRING 函式（別名 SUBSTR）：依起始位置和長度傳回字串或二進位值的一部分。"
image: "/img/stackql-featured-image.png"
---
傳回字元或二進位運算式的一部分。`SUBSTR` 是 `SUBSTRING` 的別名。

另請參閱：  
[[` SELECT `]](/language-spec/select) [[ 資料型別 ]](/language-spec/data-types)

* * * 

:::info

若要從 JSON 物件擷取欄位，請使用 [**JSON_EXTRACT**](/language-spec/functions/json/json_extract) 函式。

:::

## 語法 {#syntax}

```sql
SELECT SUBSTRING(expression, start[, length]) FROM <multipartIdentifier>;
SELECT SUBSTR(expression, start[, length]) FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*expression*__  
字串或 BLOB。

> 如果 *expression* 是字串，則字元索引指的是實際的 UTF-8 字元。如果 *expression* 是 BLOB，則索引指的是位元組。

__*start*__  
整數，表示要傳回之輸入運算式子序列的第一個字元或位元組。如果 *start* 為負數，則子字串的第一個字元會從右側而非左側開始計算。

> *expression* 最左側的字元編號為 1。

__*length*__  
選用的整數值，表示從 *start* 值起要傳回的子序列長度。如果省略 *length*，則傳回 *expression* 所指定之字串或 BLOB 直到結尾的所有字元。

> 如果 *length* 為負數，則傳回 *start* 之前的字元，數量為 *length* 的絕對值：`SUBSTR('abcdef', 4, -2)` 傳回 `bc`。

## 傳回值 {#return-values}

依 *expression* 的型別傳回字串或 BLOB 值。

* * *

## 範例 {#examples}

### 從資料行傳回前 *n* 個字元 {#return-the-first-n-characters-from-a-column}

```sql
SELECT SUBSTRING(name, 1, 5) as "first five chars" 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### 從資料行截去前 *n* 個字元 {#truncate-the-first-n-characters-from-a-column}

```sql
SELECT SUBSTR(name, 5) as "from char 5" 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### 從資料行傳回最後 *n* 個字元 {#return-the-last-n-characters-from-a-column}

```sql
SELECT SUBSTRING(name, -5) as "last five chars" 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

如需詳細資訊，請參閱 [https://www.sqlite.org/lang_corefunc.html#substr](https://www.sqlite.org/lang_corefunc.html#substr)。
