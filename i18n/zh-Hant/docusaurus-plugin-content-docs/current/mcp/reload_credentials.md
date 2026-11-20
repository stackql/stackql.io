---
title: reload_credentials
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - reload_credentials
  - model context protocol
description: 從 --env.file dotenv 檔案重新載入憑證，並回報各 Provider 解析狀態的 MCP 工具
image: "/img/stackql-featured-image.png"
---

將 [`--env.file`](/command-line-usage/global-flags) 旗標所指定的 dotenv 格式檔案重新載入伺服器的處理程序環境，然後回報每個 Provider 的憑證解析狀態。這解決了環境在啟動時即固定的問題：在工作階段期間於磁碟上寫入或輪替的憑證，不需重新啟動即可送達執行中的伺服器。

env 檔案會疊加在處理程序環境之上，而不是取代它：憑證解析一律在要求時讀取即時的處理程序環境，因此啟動時已存在的變數（來自 shell 或用戶端的 `env` 區塊）無論是否使用 `--env.file` 都能繼續運作。重新載入時，只會寫入檔案中存在且值不為空的鍵，且絕不會取消設定任何變數，因此重新載入失敗或只完成一部分時，先前可用的憑證都會保留。

祕密值絕不會被傳回、記錄或稽核；此工具只回報變數名稱與狀態。未設定 `--env.file` 時，此工具會降級為單純的狀態探查。當查詢因憑證解析失敗時，MCP 錯誤會附帶提示資訊，指示代理程式呼叫 `reload_credentials` 並重試，讓代理式用戶端能夠自我修復。

:::note

適用於 `v0.10.542` 之後的 StackQL 發行版本。

:::

另請參閱：
[[ MCP 概觀 ]](/command-line-usage/mcp) [[ 憑證（重新）載入 ]](/command-line-usage/mcp#credential-resourcing---envfile--reload_credentials) [[ `server_info` ]](/mcp/server_info) [[ Claude Desktop ]](/getting-started/claude-desktop)

* * *

## 輸入 {#inputs}

| 引數 | 必要 | 描述 |
|--|--|--|
|`provider`|否|將狀態報告的範圍限定為單一 Provider。省略時，會回報所有 Provider。|

* * *

## 輸出 {#output}

| 轉譯器 | 結構 |
|--|--|
| 表格 | env 檔案的載入結果，加上每個 Provider 一個資料列的憑證解析狀態 |

<br/>

結構化承載包含：

| 欄位 | 描述 |
|--|--|
|`env_file`|所設定的 `--env.file` 路徑（未設定時為空）。|
|`env_file_sourced`|在此次呼叫中找到並載入該檔案時為 `true`。|
|`sourced_vars`|從檔案設定的環境變數名稱：只有名稱，絕不包含值。|
|`providers`|每個 Provider 一個資料列（請參閱下方）。|

<br/>

每個 `providers` 資料列包含：

| 資料行 | 描述 |
|--|--|
|`provider`|Provider 名稱。|
|`auth_type`|為該 Provider 設定的驗證類型。|
|`sourced_from`|憑證的解析來源，例如 `env:OKTA_SECRET_KEY`、`file:/path/key.json`、`inline` 或 `none`。|
|`status`|`ok`、`unresolved` 或 `not_checked`。|
|`detail`|為 `unresolved` 時的錯誤文字。|

<br/>

在憑證位元組路徑之外進行驗證的驗證類型（互動式 gcloud、azure CLI、oauth2 用戶端憑證、擔任角色鏈、OCI 簽署、null）會回報 `not_checked`。

* * *

## 管控 {#gating}

在所有伺服器模式中皆允許，包括 `read_only`：此工具除了伺服器自身的處理程序環境之外，不會變更任何內容。

不支援 `reverse_proxy` 後端模式：查詢會在遠端 stackql 伺服器處理程序中執行，因此此工具會傳回錯誤，指示使用者在後端伺服器上重新載入憑證。

* * *

## 範例 {#example}

請在任何已註冊 StackQL MCP 伺服器（以 `--env.file` 啟動）的 MCP 用戶端中試用以下提示詞：

```
My okta query failed with a credential error - I've just written the key
to the credentials file.  Reload credentials and retry the query.
```
