---
title: describe_method
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - describe_method
  - model context protocol
description: 傳回單一 StackQL 存取方法完整 I/O 契約的 MCP 工具
image: "/img/stackql-featured-image.png"
---

傳回單一存取方法的完整 I/O 契約，一律為 EXTENDED，也就是包含輸入、要求本文 schema 與完整的輸出結構。代理程式從這裡取得資料行名稱：每一資料列都帶有 `param_type`，其值為 `input_required`（必要的完全比對 `WHERE` 述詞）、`input_optional` 或 `output`（`SELECT` 或 `RETURNING` 可以參照的欄位）。資源沒有單一的欄位清單，因為每個方法都傳回各自的結構，所以在第一次對某個方法執行查詢之前，請先對從 [`list_methods`](/mcp/list_methods) 選出的該方法呼叫此工具。

另請參閱：
[[ MCP 概觀 ]](/command-line-usage/mcp) [[ `list_methods` ]](/mcp/list_methods) [[ `run_select_query` ]](/mcp/run_select_query)

* * *

## 輸入 {#inputs}

| 引數 | 必要 | 描述 |
|--|--|--|
|`provider`|是|Provider 名稱。|
|`service`|是|該 Provider 下的服務。|
|`resource`|是|`provider.service` 下的資源。|
|`method`|是|[`list_methods`](/mcp/list_methods) 所傳回的方法名稱。|

<br/>

:::info

識別碼不得包含空白字元、分號或反斜線；此類輸入會在建立任何陳述式之前遭到拒絕。

:::

* * *

## 輸出 {#output}

| 轉譯器 | 結構 |
|--|--|
| KV | 每個輸入與輸出欄位各一資料列：名稱、型別、`param_type`（`input_required`、`input_optional` 或 `output`）、結構與描述 |

* * *

## 管控 {#gating}

在所有伺服器模式下皆允許。唯讀。

* * *

## 範例 {#example}

在任何已註冊 StackQL MCP 伺服器且支援徵詢（elicitation）的 MCP 用戶端中試試這個提示詞：

```
Show me everything I need to know to call the insert method  
on Google Compute networks -- required parameters,  
optional parameters, and the request body shape.
```

以下是在 Claude Desktop 中的範例：

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_describe_method_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_describe_method_web.mp4" type="video/mp4" />
</video>
