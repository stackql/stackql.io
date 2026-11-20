---
title: 常見 StackQL 錯誤
description: 最常見 StackQL 失敗的解決方法：未安裝 Provider、缺少必要的 WHERE 參數、驗證失敗、GitHub 速率限制、空白結果與 MCP 拒絕變更。
keywords: [stackql, 錯誤, 疑難排解, 驗證, 登錄庫, 必要參數, mcp]
proficiencyLevel: Beginner
faq:
  - question: 為什麼我的查詢顯示找不到資料表或 Provider？
    answer: 因為本機未安裝該 Provider。Provider 定義是依需求提取的：請執行 REGISTRY PULL <provider>（例如 REGISTRY PULL aws），然後重新執行查詢。SHOW PROVIDERS 會列出目前已安裝的 Provider。
  - question: 為什麼我的查詢失敗，並要求我不知道的參數？
    answer: 雲端 API 有必要的路由參數，例如 AWS 的 region、Azure 的 subscriptionId、Google 的 project，StackQL 會將其呈現為必要的 WHERE 述詞。執行 SHOW METHODS IN provider.service.resource 以查看每個存取方法及其 RequiredParams，然後以等值述詞提供這些參數。
  - question: 為什麼我已設定憑證，查詢仍傳回授權錯誤？
    answer: 依序檢查三件事：變數是否在啟動 StackQL 的同一個 shell 或處理程序環境中匯出（伺服器與 MCP 處理程序繼承的是其啟動時的環境，而不是您目前的 shell）；變數名稱是否完全符合 Provider 的慣例；以及主體是否確實具有該作業的權限，因為 Provider 的 401/403 回應會原封不動地經由 StackQL 傳回。
---

# 常見 StackQL 錯誤

大多數 StackQL 失敗可歸為五類：缺少 Provider、缺少必要參數、驗證問題、Provider 端限制，以及受管控的作業。每一類都有快速的診斷方式。

## 找不到 Provider 或資料表 {#provider-or-table-not-found}

**症狀**：查詢參照 `aws.ec2.instances`（或任何資源），但找不到對應的 Provider 或資料表。

**原因**：Provider 定義並未內建，而是在每台電腦上個別從登錄庫提取。

**修正方式**：

```sql
SHOW PROVIDERS;          -- what is installed
REGISTRY LIST;           -- what is available
REGISTRY PULL aws;       -- install (repeat per provider)
```

`REGISTRY LIST` 也會列出 Provider 版本；重新提取會升級到最新發佈的規格。

## 缺少必要參數 {#missing-required-parameters}

**症狀**：語法正確的 `SELECT` 失敗或未傳回任何結果，原因是未提供必要參數（通常是 `region`、`subscriptionId`、`project`、`org` 或父資源識別碼）。

**原因**：底層 API 作業有必要的路由參數；StackQL 會將其呈現為必要的 `WHERE` 述詞。

**修正方式**：先探索需求，再以等值（或 `IN`）述詞提供這些參數：

```sql
SHOW METHODS IN aws.ec2.instances;
```

`RequiredParams` 資料行會依存取方法告訴您 `WHERE` 子句確切必須包含哪些內容；以 `aws.ec2.instances` 為例，`describe` 方法需要 `region`。無論是人類或代理程式，這個探索步驟都是文件記載的做法（MCP 伺服器透過 `list_methods` 發佈相同的資料）。

## 驗證失敗 {#authentication-failures}

**症狀**：中繼資料命令（`SHOW`、`DESCRIBE`）可以運作，但 `SELECT` 或變更因 Provider 的 401/403 錯誤而失敗。

**原因**：中繼資料來自本機的 Provider 規格，不需要憑證；資料作業則會呼叫 Provider。憑證不在處理程序環境中、名稱錯誤或權限不足。

**修正方式**：確認變數已在啟動 StackQL 的環境中匯出（伺服器或 MCP 處理程序會繼承其啟動時的環境，設定變數後忘記重新啟動是常見的疏失）、名稱符合 Provider 的慣例（`AWS_ACCESS_KEY_ID`/`AWS_SECRET_ACCESS_KEY`、`AZURE_TENANT_ID`/`AZURE_CLIENT_ID`/`AZURE_CLIENT_SECRET`、`GOOGLE_CREDENTIALS`、`STACKQL_GITHUB_USERNAME`/`STACKQL_GITHUB_PASSWORD`），以及主體具有該作業所需的權限。[/ai/how-tos](/ai/how-tos) 下各 Provider 的操作指南涵蓋了每種慣例。

## GitHub 速率限制 {#github-rate-limiting}

**症狀**：未經驗證的 GitHub 查詢短暫可用，隨後失敗。

**原因**：GitHub 對未經驗證要求的限制是每個 IP 每小時 60 個要求，而一個 StackQL 查詢可能會發出多個分頁 API 呼叫。

**修正方式**：使用個人存取權杖進行驗證（即使是沒有任何範圍的權杖也能大幅提高限制），請參閱[如何讓 StackQL 向 GitHub 進行驗證](/ai/how-tos/authenticate-stackql-to-github)。

## 不應為空的空白結果 {#empty-results-that-should-not-be-empty}

**症狀**：查詢成功，但對於您確知存在的資源傳回 0 個資料列。

**檢查清單**：路由述詞指向正確的範圍（正確的區域、專案、訂用帳戶或組織）；您的述詞所選取的存取方法是您預期的方法（`SHOW METHODS` 會顯示哪些述詞組合會選取哪個方法，例如 `github.repos.repos` 上的 `org =` 與 `username =`）；以及憑證能看見這些資源（雲端 IAM 可能會在不提示的情況下篩選清單結果）。

## MCP 變更遭拒 {#mcp-mutation-refused}

**症狀**：代理程式透過 MCP 伺服器執行的 `INSERT`/`UPDATE`/`DELETE` 遭到拒絕，訊息中提到徵詢（elicitation）或伺服器模式。

**原因**：這是安全契約正常運作，而不是錯誤。預設的 `safe` 模式要求變更須經人工核准；未宣告 MCP 徵詢功能的用戶端無法給予核准。

**修正方式**：使用支援徵詢的用戶端（Claude Desktop、Cursor、Continue）並核准要求；或者，僅限受信任的非互動式自動化，以 `"mode": "full_access"` 執行伺服器。詳細資訊請參閱 [StackQL MCP 架構](/ai/architecture/stackql-mcp-architecture)。

## 相關概念 {#related-concepts}

- [如何讓 StackQL 向 AWS 進行驗證](/ai/how-tos/authenticate-stackql-to-aws)：以及 Azure、Google Cloud 與 GitHub 的對應頁面
- [StackQL MCP 架構](/ai/architecture/stackql-mcp-architecture)：模式與徵詢的說明
- [開始使用 StackQL](/ai/tutorials/getting-started-with-stackql)：可避免上述大多數問題的全新設定
- [什麼是 SQL for APIs？](/ai/canonical-definitions/what-is-sql-for-apis)：為什麼會有必要參數
- [StackQL 常見問題](/ai/faqs/stackql-faq)：更廣泛的問題
