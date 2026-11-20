---
title: GROUPS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - 存取管理
  - groups
  - 主體目錄
description: "StackQL 中的 GROUPS 函式：啟用存取管理時，以 JSON 陣列傳回已驗證呼叫端的目錄群組。"
image: "/img/stackql-featured-image.png"
---
以 JSON 陣列傳回已驗證呼叫端的目錄群組成員資格。工作階段必須在啟用[存取管理](/command-line-usage/access-management)的情況下啟動。StackQL `v1.0` 及更新版本提供此函式。

另請參閱：  
[[` WHOAMI `]](/language-spec/functions/session/whoami) [[ 存取管理 ]](/command-line-usage/access-management)

* * * 

## 語法 {#syntax}

```sql
SELECT GROUPS() [ AS <alias> ];
```

## 引數 {#arguments}

無。

## 傳回值 {#return-values}

傳回 JSON 陣列，內容為從目前陳述式所擷取之主體目錄快照中的群組 ID，例如 `["readers"]`。成員資格會針對每個陳述式重新整理，因此撤銷會在下一個陳述式中反映出來。在未使用 `--access.config` 啟動的工作階段中，此函式會失敗。

* * *

## 範例 {#examples}

### 檢查已建立的身分 {#inspect-the-established-identity}

搭配使用這兩個函式，可顯示呼叫端的主體 ID 及其目前的目錄群組。由於每個陳述式都會重新讀取成員資格，在目錄變更後執行此查詢，即可確認工作階段看得到該變更。

```sql
SELECT whoami() AS principal, groups() AS memberships;
```
