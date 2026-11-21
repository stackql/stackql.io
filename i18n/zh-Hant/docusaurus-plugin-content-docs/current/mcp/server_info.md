---
title: server_info
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - server_info
  - model context protocol
description: 傳回 StackQL MCP 伺服器身分與執行階段資訊的 MCP 工具
image: "/img/stackql-featured-image.png"
---

傳回 StackQL MCP 伺服器的身分與執行階段資訊：版本、後端 SQL 引擎、Provider 登錄庫位置、伺服器模式，以及舊版的唯讀旗標。請視需要呼叫，例如診斷遭拒絕或受管控的作業，或檢查特定版本的行為；在工作階段開始時或查詢之前不需要呼叫。

另請參閱：
[[ MCP 概觀 ]](/command-line-usage/mcp) [[ `list_providers` ]](/mcp/list_providers) [[ `list_registry` ]](/mcp/list_registry)

* * *

## 輸入 {#inputs}

無。

* * *

## 輸出 {#output}

| 轉譯器 | 結構 |
|--|--|
| KV（供 LLM 使用的 markdown 鍵值對區塊） | 包含下列欄位的結構化承載 |

<br/><br/>

| 欄位 | 描述 |
|--|--|
|`version`|StackQL 建置版本。|
|`commit`|Git commit 的短雜湊。|
|`build_date`|RFC3339 建置時間戳記。|
|`platform`|`os/arch`（例如 `linux/amd64`）。|
|`transport`|`http` 或 `stdio`。|
|`sql_backend`|後端 SQL 引擎（例如 `sqlite3`）。|
|`provider_registry`|Provider 登錄庫 URL。|
|`mode`|作用中的伺服器模式：`read_only`、`safe`、`delete_safe` 或 `full_access`。|
|`is_read_only`|舊版布林值旗標（若且唯若 `mode == read_only` 時為 `true`）。|

* * *

## 管控 {#gating}

在所有伺服器模式中一律允許。不會以變更的形式進行稽核。

* * *

## 範例 {#example}

請在任何支援徵詢（elicitation）且已註冊 StackQL MCP 伺服器的 MCP 用戶端中試用以下提示詞：

```
What StackQL server am I connected to?  
Show me the version, mode, and where it pulls providers from.
```

以下是在 Claude Desktop 中的範例：

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_server_info_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_server_info_web.mp4" type="video/mp4" />
</video>
