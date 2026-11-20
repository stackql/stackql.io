---
title: Gleam
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - 內嵌式
  - gleam
  - erlang
  - beam
description: 在以 Erlang/BEAM 為目標的 Gleam 應用程式中內嵌 StackQL MCP 伺服器
image: "/img/stackql-featured-image.png"
---

# Gleam

在以 Erlang/BEAM 為目標的 Gleam 應用程式中內嵌 StackQL MCP 伺服器。此程式庫與 OTP 監督機制整合，會下載並驗證已簽署的二進位檔，再透過 stdio 啟動它。

:::info
內嵌的二進位檔會在首次執行時下載並經過 sha256 驗證，或透過 vendoring 納入您的應用程式。請參閱[共同契約](/mcp/embedded#common-contract)。
:::

* * *

## 安裝 {#install}

```
gleam add stackql_mcp
```

僅以 Erlang/BEAM 為目標（而非 JavaScript 目標）。伺服器透過 `child_spec()` 公開為 OTP 子項目，因此可以放在您自己的監督樹中。

* * *

## 快速入門 {#quick-start}

```gleam
import gleam/io
import gleam/int
import gleam/list
import envoy
import stackql_mcp

pub fn main() {
  let config =
    stackql_mcp.Config(
      ..stackql_mcp.default_config(),
      auth: [stackql_mcp.auth_for("github", "null_auth")],
    )

  let assert Ok(server) =
    stackql_mcp.start(
      config: config,
      home: "/home/u",
      os: "linux",
      arch: "x86_64",
      getenv: envoy.get,
    )

  let assert Ok(tools) = stackql_mcp.list_tools(server)
  io.println(int.to_string(list.length(tools)) <> " tools available")

  stackql_mcp.stop(server)
}
```

API 包括用於生命週期的 `stackql_mcp.start()`、`stop()` 與 `child_spec()`；用於作業的 `list_tools()` 與 `call_tool()`（另有在監督器下使用的 `supervised_list_tools()` 與 `supervised_call_tool()` 變體）；以及用於設定的 `Config`、`default_config()` 與 `auth_for()`。

* * *

## 模式 {#modes}

模式透過 `Config.mode` 設定，預設為 `read_only`。若要提升權限，請將其設為 `safe`、`delete_safe` 或 `full_access`：

```gleam
stackql_mcp.Config(..stackql_mcp.default_config(), mode: stackql_mcp.safe)
```

權限提升必須明確指定：除非您選擇加入，否則伺服器不會啟用變更或生命週期工具。

* * *

## Vendoring / 內嵌 {#vendoring--embedding}

啟動時，程式庫會下載該平台的 StackQL 安裝套件，依據內建的 sha256 釘選值進行驗證，並快取在程式庫系列共用的快取中。解析優先順序：

1. 明確指定的 `Config.binary`。
2. `STACKQL_MCP_BIN` 環境變數。
3. 位於 `~/.stackql/mcp-server-bin/<version>/<platform-key>/` 的共用快取。

若為離線或實體隔離的部署，請將 `STACKQL_MCP_BUNDLE` 設為指向本機的安裝套件。

* * *

## 示範應用程式 {#demo-app}

**pipewatch** 是此程式庫規劃中的示範應用程式。它已列入藍圖，但尚未實作。

* * *

## 存放庫連結 {#repo-link}

[github.com/stackql/stackql-mcp-gleam](https://github.com/stackql/stackql-mcp-gleam)
