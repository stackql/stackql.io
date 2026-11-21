---
title: WHOAMI
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - 存取管理
  - whoami
  - 主體
description: "StackQL 中的 WHOAMI 函式：啟用存取管理時，傳回已驗證呼叫端的主體 ID。"
image: "/img/stackql-featured-image.png"
---
傳回已驗證呼叫端的主體 ID。工作階段必須在啟用[存取管理](/command-line-usage/access-management)的情況下啟動。StackQL `v1.0` 及更新版本提供此函式。

另請參閱：  
[[` GROUPS `]](/language-spec/functions/session/groups) [[ 存取管理 ]](/command-line-usage/access-management)

* * * 

## 語法 {#syntax}

```sql
SELECT WHOAMI() [ AS <alias> ];
```

## 引數 {#arguments}

無。

## 傳回值 {#return-values}

傳回為目前陳述式建立的主體 ID 字串。在未使用 `--access.config` 啟動的工作階段中，此函式會失敗。

* * *

## 範例 {#examples}

### 檢查已建立的身分 {#inspect-the-established-identity}

這兩個函式都不接受引數，用於描述伺服器為此陳述式驗證的呼叫端。`whoami()` 傳回主體 ID，`groups()` 傳回呼叫端的目錄群組，因此在執行任何其他作業之前同時執行兩者，可以快速確認權杖或憑證是否對應到預期的主體。

```sql
SELECT whoami() AS principal, groups() AS memberships;
```
