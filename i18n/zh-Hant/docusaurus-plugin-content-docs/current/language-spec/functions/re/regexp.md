---
title: REGEXP
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: 在 StackQL 中使用 SQL，以正規表示式比對字串中的模式。
image: "/img/stackql-featured-image.png"
---
檢查來源字串是否符合提供的正規表示式模式。

另請參閱：  
[[`SELECT`]](/language-spec/select)

* * * 

## 語法 {#syntax}

```sql
SELECT source_string REGEXP pattern_expression AS match_result FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*source_string*__  
要在其中搜尋模式的字串。

__*pattern_expression*__  
要與來源字串比對的正規表示式模式。

## 傳回值 {#return-values}
如果來源字串符合正規表示式模式，則傳回 `1`，否則傳回 `0`。

* * *

## 範例 {#examples}

### 檢查字串是否包含數字 {#check-if-a-string-contains-numbers}

此範例示範如何檢查字串「the year is 2021」是否包含一個或多個數字字元：

```sql
SELECT 'the year is 2021' REGEXP '[0-9]+' AS match_result;
```

此查詢會傳回 `1`，因為字串包含子字串「2021」，而它符合表示一個或多個數字的模式 `[0-9]+`。

### 驗證電子郵件格式 {#validate-email-format}

若要檢查字串是否為有效的電子郵件格式：

```sql
SELECT 'user@example.com' REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$' AS match_result;
```

此查詢傳回 `1`，表示「user@example.com」的格式與一般電子郵件地址相同，符合指定的正規表示式模式。網域尾碼前的句點寫成 `\\.`，因為 StackQL 字串常值會消耗一個反斜線，所以屬於正規表示式的反斜線要寫兩次（請參閱[正規表示式參考](/language-spec/functions/re/reference)）。
