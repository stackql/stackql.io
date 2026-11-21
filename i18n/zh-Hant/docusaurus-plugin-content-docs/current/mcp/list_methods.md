---
title: list_methods
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_methods
  - model context protocol
description: 列出 StackQL 資源存取方法的 MCP 工具
image: "/img/stackql-featured-image.png"
---

傳回繫結至資源的存取方法（HTTP 作業），也就是可用的讀取方法、變更與生命週期作業。代理程式應在**撰寫任何查詢之前**呼叫此工具，因為必要的 `WHERE` 參數是從所選的讀取方法推斷而來。請挑選 SQL 動詞符合意圖、且能提供其必要參數的方法，然後對該方法呼叫 [`describe_method`](/mcp/describe_method)，取得包含輸出欄位在內的完整契約。

另請參閱：
[[ MCP 概觀 ]](/command-line-usage/mcp) [[ `list_resources` ]](/mcp/list_resources) [[ `describe_method` ]](/mcp/describe_method) [[ `validate_select_query` ]](/mcp/validate_select_query)

* * *

## 輸入 {#inputs}

| 引數 | 必要 | 描述 |
|--|--|--|
|`provider`|是|Provider 名稱。|
|`service`|是|該 Provider 下的服務。|
|`resource`|是|`provider.service` 下的資源。|

<br/>

:::info

識別碼不得包含空白字元、分號或反斜線；此類輸入會在建立任何陳述式之前遭到拒絕。

:::

* * *

## 輸出 {#output}

| 轉譯器 | 結構 |
|--|--|
| 表格 | 每個存取方法各一資料列，包含其對應的 SQL 動詞與必要參數 |

* * *

## 管控 {#gating}

在所有伺服器模式下皆允許。唯讀。

* * *

## 範例 {#example}

在任何已註冊 StackQL MCP 伺服器且支援徵詢（elicitation）的 MCP 用戶端中試試這個提示詞：

```
For Google Compute instances, what methods are available  
and what parameters are required?
```

以下是在 Claude Desktop 中的範例：

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_methods_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_methods_web.mp4" type="video/mp4" />
</video>
