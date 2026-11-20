---
title: mcp
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "stackql mcp 命令將 StackQL 作為供 AI 代理程式使用的 Model Context Protocol 伺服器執行：部署模式、安全模式、憑證重新載入與稽核記錄。"
image: "/img/stackql-featured-image.png"
---

用於將 StackQL 啟動為 Model Context Protocol（MCP）伺服器的命令，讓 AI 代理程式和助理能夠使用 StackQL 的查詢功能與雲端基礎架構互動。

:::info[什麼是 MCP？]

[Model Context Protocol（MCP）](https://modelcontextprotocol.io/) 是一種開放協定，用於將 AI 應用程式與外部資料來源和工具互動的方式標準化。將 StackQL 作為 MCP 伺服器執行，您就能讓 AI 代理程式（Claude、Cursor、Continue、自訂代理程式等）使用自然語言查詢和管理跨多個 Provider 的雲端基礎架構。

:::

* * *

### 語法 {#syntax}

使用 `stackql mcp [flags]` 執行獨立的 MCP 伺服器。

使用 `stackql srv [flags]` 在同一個處理程序中同時執行 MCP 伺服器與 PostgreSQL 線路協定伺服器。

* * *

### 部署模式 {#deployment-modes}

StackQL 的 MCP 伺服器可以用三種不同的設定方式部署，以符合各種架構需求。下方範例以 `"allow_unauthenticated": true` 選擇加入未驗證的服務，適合本機開發；對於其他處理程序或主機可以連線的任何部署，請改為設定 bearer 權杖（請參閱 [HTTP 用戶端驗證](#http-client-authentication)）。

#### 1. 獨立 MCP 伺服器 {#1-standalone-mcp-server}

在指定的連接埠上將 StackQL 作為專用 MCP 伺服器執行。

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true}}'
```

**使用情境**：只需要 MCP 協定存取，而不需要 PostgreSQL 線路協定相容性時。

#### 2. MCP + PostgreSQL 伺服器（記憶體內） {#2-mcp--postgresql-server-in-memory}

同時執行 MCP 和 PostgreSQL 伺服器，兩者之間透過記憶體內通訊。

```bash
stackql srv \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true}}' \
  --pgsrv.port 5665
```

**使用情境**：同時需要供 AI 代理程式使用的 MCP 協定存取，以及供傳統資料庫用戶端使用的 PostgreSQL 線路協定，並希望透過記憶體內通訊獲得最高效能時。

#### 3. MCP + PostgreSQL 伺服器（反向代理） {#3-mcp--postgresql-server-reverse-proxy}

以 TCP 通訊執行兩個伺服器，支援分散式部署和 TLS 加密。

```bash
stackql srv \
  --mcp.server.type=reverse_proxy \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9004", "allow_unauthenticated": true}, "backend": {"dsn": "postgres://stackql:stackql@127.0.0.1:5446?default_query_exec_mode=simple_protocol"}}' \
  --pgsrv.port 5446
```

**使用 TLS 加密：**

```bash
stackql srv \
  --mcp.server.type=reverse_proxy \
  --mcp.config '{"server": {"tls_cert_file": "/path/to/server_cert.pem", "tls_key_file": "/path/to/server_key.pem", "transport": "http", "address": "127.0.0.1:9004", "auth_token_env_var": "STACKQL_MCP_TOKEN"}, "backend": {"dsn": "postgres://stackql:stackql@127.0.0.1:5446?default_query_exec_mode=simple_protocol"}}' \
  --pgsrv.port 5446
```

TLS 會加密傳輸，但不會驗證用戶端，因此此範例也要求使用存放在 `STACKQL_MCP_TOKEN` 中的 bearer 權杖。

**使用情境**：需要將 MCP 和 PostgreSQL 工作負載分散到不同的處理程序或主機，或需要為 MCP 端點使用 TLS 加密時。

* * *

### 設定選項 {#configuration-options}

#### MCP 伺服器類型 {#mcp-server-type}

| 類型 | 描述 |
|--|--|
|`http`|直接 HTTP 伺服器模式：MCP 要求由 StackQL 引擎在處理程序內處理。|
|`reverse_proxy`|反向代理模式：MCP 要求會透過設定的 DSN 分派到後端 PostgreSQL 伺服器。與 `stackql srv` 搭配使用。|
|`stdio`|標準輸入/輸出傳輸方式：伺服器從 `stdin` 讀取要求，並將回應寫入 `stdout`。由內嵌於編輯器的 MCP 用戶端（Claude Desktop、Cursor 等）使用。|

#### MCP 記錄格式 {#mcp-log-format}

`--mcp.log.format` 旗標會選擇稽核記錄的編碼：`jsonl`（預設）或 `otel`（OpenTelemetry OTLP/JSON 記錄項目）。它會覆寫設定物件中的 `server.audit.format`。請參閱[記錄格式](#log-format)。

#### MCP 協定版本 {#mcp-protocol-version}

`--mcp.protocol.version` 旗標會設定伺服器所宣告的最新協定修訂版：`auto`（預設，所有支援的修訂版）、`2026-07-28`（僅限無工作階段）或較舊的修訂版，例如 `2025-11-25`（該修訂版及更早的修訂版）。它會覆寫設定物件中的 `server.protocol_version`。請參閱[固定修訂版](#pinning-the-revision)。

#### MCP 設定物件 {#mcp-configuration-object}

`--mcp.config` 旗標接受具有下列結構的 JSON 物件。

##### 伺服器設定 {#server-configuration}

| 欄位 | 描述 | 必要 |
|--|--|--|
|`server.transport`|傳輸協定：`http` 或 `stdio`。|是|
|`server.address`|MCP 伺服器要繫結的位址和連接埠（例如 `127.0.0.1:9912`）。僅限 HTTP 傳輸方式。|使用 `http` 時|
|`server.tls_cert_file`|HTTPS 使用的 TLS 憑證檔案路徑。|否|
|`server.tls_key_file`|HTTPS 使用的 TLS 私密金鑰檔案路徑。|否|
|`server.auth_token_env_var`|存放預先共用 bearer 權杖的環境變數名稱。設定後，每個 HTTP 要求都必須帶有 `Authorization: Bearer <token>`；如果該變數未設定或為空，伺服器會拒絕啟動。請參閱 [HTTP 用戶端驗證](#http-client-authentication)。|使用 `http` 時，此欄位與 `allow_unauthenticated` 擇一|
|`server.allow_unauthenticated`|明確選擇加入，以不進行用戶端驗證的方式提供 HTTP 傳輸方式。預設為 `false`。|使用 `http` 時，此欄位與 `auth_token_env_var` 擇一|
|`server.mode`|管控變更與生命週期作業的安全契約。可為 `read_only`、`safe`（預設）、`delete_safe`、`full_access` 之一。請參閱[伺服器模式](#server-modes)。|否|
|`server.read_only`|`mode: read_only` 的**舊版**布林值別名。兩個欄位都設定時，以 `mode` 為準。|否|
|`server.stateless`|以不使用工作階段的方式提供 Streamable HTTP（沒有 `Mcp-Session-Id`），這正是透過 HTTP 提供協定修訂版 `2026-07-28` 的方式。預設為 `false`。`stdio` 會忽略此欄位。請參閱[協定修訂版支援](#protocol-revision-support)。|否|
|`server.protocol_version`|所宣告的最新協定修訂版：`auto`（預設）、`2026-07-28` 或較舊的修訂版，例如 `2025-11-25`。對 Streamable HTTP 而言，`2026-07-28` 隱含 `stateless`。請參閱[固定修訂版](#pinning-the-revision)。|否|
|`server.audit`|稽核子系統設定，包括 `audit.format`（`jsonl` 或 `otel`）。請參閱[稽核記錄](#audit-log)。|否|

##### 後端設定（僅限反向代理模式） {#backend-configuration-reverse-proxy-mode-only}

| 欄位 | 描述 | 必要 |
|--|--|--|
|`backend.dsn`|後端 StackQL 伺服器的 PostgreSQL 連線字串。|是（使用 `reverse_proxy` 時）|

<br />

:::info

後端 DSN 應包含 `default_query_exec_mode=simple_protocol` 參數，以獲得最佳相容性。

:::

##### 允許清單 {#allowlists}

| 欄位 | 描述 |
|--|--|
|`enabled_tools`|允許伺服器發佈的工具名稱陣列。省略或為空時，會註冊所有內建工具。用於只公開一小部分工具（例如唯讀的資源清單伺服器）。|
|`enabled_prompts`|提示詞名稱陣列。語意與 `enabled_tools` 相同，但適用於提示詞。|

<br/>

範例：只發佈 `server_info` 和 `list_providers` 的伺服器：

```json
{
  "server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true},
  "enabled_tools": ["server_info", "list_providers"]
}
```

* * *

### 協定修訂版支援 {#protocol-revision-support}

協定修訂版 `2026-07-28`、`server.stateless` 和 `otel` 稽核記錄格式從 StackQL `v0.11.660` 發行版本起提供。`server.protocol_version` 和 `--mcp.protocol.version` 旗標從 StackQL `v0.12.732` 發行版本起提供。

伺服器支援 [Go MCP SDK](https://github.com/modelcontextprotocol/go-sdk) 所支援的每個 Model Context Protocol 修訂版，並與每個用戶端個別協商，因此由各種用戶端組成的群組可以搭配同一個伺服器運作。

| 修訂版 | 生命週期 | 核准要求（`safe` / `delete_safe`） |
|--|--|--|
|`2026-07-28`（已宣告）|無交握：先呼叫 `server/discover`，之後每個要求都在 `_meta` 中攜帶 `io.modelcontextprotocol/protocolVersion` 和 `io.modelcontextprotocol/clientCapabilities`（在 HTTP 上另加 `Mcp-Protocol-Version`、`Mcp-Method` 和 `Mcp-Name` 標頭）。|`input_required` 結果，由用戶端以 `inputResponses` 重試（SEP-2322）。|
|`2025-11-25`、`2025-06-18`|`initialize` / `notifications/initialized` 交握。|由伺服器發起的 `elicitation/create` 要求。|
|`2025-03-26`、`2024-11-05`|同上。|同上。|

<br />

**stdio** 在單一處理程序上提供所有修訂版：`2026-07-28` 用戶端的第一個要求不需交握即可獲得服務，舊版用戶端的 `initialize` 也仍然有效。Claude Desktop、npm / PyPI 啟動器或 Docker 映像檔都不需要任何設定。

**Streamable HTTP** 預設採用具狀態、每個用戶端一個工作階段的模型（`Mcp-Session-Id`），SDK 為 `2025-11-25` 及之前的修訂版提供此模型。對 `2026-07-28` 要求，伺服器會以 JSON-RPC 錯誤 `-32022` 回應（不支援的協定版本；在 `v0.12.718` 及之前的發行版本中是一般的 HTTP 400），其 `data.supported` 列出交握修訂版，`server/discover` 也宣告相同內容，因此用戶端會向下協商，現有的 HTTP 整合可保留其工作階段與核准要求而不受影響。設定 `server.stateless`（或將修訂版固定為 `2026-07-28`，[如下所述](#pinning-the-revision)），即可透過 HTTP 原生提供 `2026-07-28`：

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true, "stateless": true}}'
```

無工作階段的伺服器不核發 `Mcp-Session-Id`，對 `GET` 和 `DELETE` 回應 405（沒有可拆除的工作階段），讓 `tools/list`、`prompts/list` 和 `resources/list` 的結果不隨連線而變，並透過 `input_required` 完成核准的來回流程。它仍接受舊版用戶端的 `initialize` 並為其提供讀取，但無法保留該用戶端在初始化時宣告的徵詢（elicitation）能力（每個要求都會取得一個暫時的工作階段），因此舊版用戶端無法在無工作階段的伺服器上核准受管控的寫入。主機都使用目前修訂版時請選擇 `stateless`；用戶端群組仍包含舊版用戶端時請使用預設值。

伺服器不保留跨呼叫的狀態：模式、稽核與 Provider 憑證都是處理程序層級的設定，因此不需要將任何狀態移到明確的控制代碼之後。

#### 固定修訂版 {#pinning-the-revision}

根據預設，伺服器會宣告所有支援的修訂版，並與每個用戶端個別協商。`--mcp.protocol.version` 旗標或設定物件中的 `server.protocol_version`（以旗標為優先）會設定所宣告的最新修訂版：

```bash
# sessionless only; stateless Streamable HTTP is implied
stackql mcp \
  --mcp.server.type=http \
  --mcp.protocol.version=2026-07-28 \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true}}'

# handshake lifecycle only
stackql mcp --mcp.server.type=stdio --mcp.protocol.version=2025-11-25
```

| 值 | 宣告的修訂版 | 效果 |
|--|--|--|
|`auto`（預設）或未設定|所有支援的修訂版。|如上所述進行協商：採用雙方都支援的最高修訂版。|
|`2026-07-28`|僅 `2026-07-28`。|僅限無工作階段，且 Streamable HTTP 隱含 `stateless`。舊版用戶端的 `initialize` 會收到 `2025-11-25` 的回應，提示該用戶端中斷連線，而不是把回應當成新的生命週期來解讀。|
|`2025-11-25`（或任何更舊的修訂版）|該修訂版及其之前的所有修訂版。|僅限交握生命週期：`2026-07-28` 要求會收到 JSON-RPC 錯誤 `-32022`，其 `data.supported` 列出所宣告的修訂版，讓用戶端可以重新協商。較舊的交握用戶端保留各自的修訂版。|

<br />

其他任何值都會在啟動時無法通過設定驗證，錯誤訊息會列出合法的值。

#### 傳輸限制 {#transport-limits}

Streamable HTTP 的要求本文上限為 4 MiB（超過時傳回 HTTP 413），巢狀深度超過 1000 層的 JSON 會在剖析前遭拒，單一 `stdio` 訊框的上限為 8 MiB。這些限制都無法設定，且都遠高於伺服器所接受的任何 SQL 陳述式。

* * *

### 憑證（重新）載入（`--env.file` + `reload_credentials`） {#credential-resourcing---envfile--reload_credentials}

:::note

`--env.file` 和 `reload_credentials` 工具在 StackQL `v0.10.542` 之後的發行版本中提供。

:::

處理程序的環境區塊在產生時即已固定。這對 MCP 伺服器造成兩個問題：

- 由桌面用戶端（Claude Desktop、Cursor 等）產生、且未帶有憑證環境變數的 MCP `stdio` 伺服器，永遠看不到這些變數：用戶端會將自己的環境傳給子處理程序。
- 輪替後的短期憑證永遠無法送達長時間執行的伺服器工作階段。

`--env.file` 旗標透過指定一個 dotenv 格式的檔案作為可變的憑證存放區，來彌補這兩個缺口：

```bash
stackql mcp --mcp.server.type=stdio --env.file /path/to/stackql-credentials.env
```

#### 載入語意 {#sourcing-semantics}

對每個進入點（`exec`、`shell`、`srv`、`registry`、`mcp`），此檔案都會在啟動時載入處理程序環境：

- 旗標為空或未設定時，不會發生任何事：憑證會在要求時才從處理程序環境延遲解析，與過去的行為完全相同。
- 指定路徑上沒有檔案時會被容許，不執行任何動作（檔案可以稍後再建立）。
- 檔案格式錯誤或無法讀取時，會在啟動時造成嚴重錯誤。
- 只會設定值不為空的鍵；現有的處理程序環境變數**會**被覆寫（此檔案是事實來源）；不會取消設定任何變數。
- 此旗標只能在命令列上指定，不會從 `.stackqlrc` 載入。

檔案格式為每行一個 `KEY=VALUE`。`#` 註解、空白行、選用的 `export ` 前置詞、值兩側選用的單引號或雙引號，以及 CRLF 行尾都可接受：

```bash
# stackql-credentials.env
export OKTA_SECRET_KEY="00abc...xyz"
STACKQL_GITHUB_USERNAME=myuser
STACKQL_GITHUB_PASSWORD='ghp_mytoken'
```

#### `reload_credentials` {#reload_credentials}

[`reload_credentials`](/mcp/reload_credentials) 工具會在工作階段中途將 `--env.file` 檔案重新載入處理程序環境，然後回報每個 Provider 的憑證解析狀態。祕密值永遠不會被傳回、記錄或稽核，只有名稱和狀態。未設定 `--env.file` 時，此工具會退化為單純的狀態探查。

查詢因憑證解析失敗時，MCP 錯誤會帶有提示資訊，指示代理程式呼叫 `reload_credentials` 並重試，因此代理式用戶端可以自我修復：將憑證寫入檔案、請代理程式重新載入，失敗的查詢在重試時就會成功，不需要重新啟動伺服器。

`reverse_proxy` 後端模式不支援 `reload_credentials`：查詢是在遠端 stackql 伺服器處理程序中執行，因此此工具會傳回錯誤，指示使用者在後端伺服器上重新載入。

#### Windows 與 Claude Desktop {#windows-and-claude-desktop}

`setx` 寫入的是 Windows registry，而不是執行中的處理程序，而且 Claude Desktop 會將自己的（過時）環境傳給 MCP 子處理程序，因此變更憑證通常需要完全重新啟動 Claude Desktop。使用 `--env.file` 時，憑證檔案可以隨時建立或輪替，`reload_credentials` 不需要任何重新啟動就能讀取它。如需完整的設定範例，請參閱[在 Claude Desktop 中使用 StackQL](/getting-started/claude-desktop)。

* * *

### HTTP 用戶端驗證 {#http-client-authentication}

:::note

`server.auth_token_env_var` 和 `server.allow_unauthenticated` 從 StackQL `v0.12.742` 發行版本起提供。

:::

HTTP 傳輸方式需要用戶端驗證。`server.auth_token_env_var` 指定一個存放預先共用 bearer 權杖的環境變數；每個要求都必須帶有 `Authorization: Bearer <token>`，否則會收到 `401` 回應。權杖是從環境讀取，因此永遠不會出現在 `--mcp.config` 或處理程序引數中。

```bash
export STACKQL_MCP_TOKEN="$(openssl rand -hex 32)"

stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "auth_token_env_var": "STACKQL_MCP_TOKEN"}}'
```

:::info[需要伺服器驗證]

不使用權杖提供服務需要明確選擇加入：在 `server` 中設定 `"allow_unauthenticated": true`。兩個鍵都未設定時，伺服器會在啟動時結束，並顯示列出這兩個鍵的訊息；權杖變數未設定或為空時，伺服器也會結束。從 `v0.12.742` 發行版本起，使用 `http` 傳輸方式時，您必須在 `server` 中加入 `auth_token_env_var` 或 `"allow_unauthenticated": true`。

:::

TLS（`tls_cert_file` / `tls_key_file`）會加密傳輸，但不會驗證用戶端；對於回送介面以外的任何接聽程式，請將它與權杖搭配使用。跨來源的瀏覽器要求（`Origin` 不符，或 `Sec-Fetch-Site` 為跨網站）無論驗證與否，都會以 `403` 拒絕。隨附的 `stackql_mcp_client` 不會傳送 `Authorization` 標頭，因此它所驅動的伺服器必須選擇加入未驗證的服務。

從 `v1.0` 起，使用 [`--access.config`](/command-line-usage/access-management) 啟動伺服器時，會以每個呼叫端各自的驗證取代共用權杖的管控：每個 HTTPS 要求都會以設定的 JWT、靜態 bearer 或雙向 TLS 機制進行驗證，並依政策規則授權。在這種情況下，伺服器 TLS 為必要，透過 HTTP 的 `local_os` 驗證會被拒絕，而且沒有憑證的要求永遠不會退回使用伺服器的環境權杖。`reverse_proxy` 伺服器類型無法使用存取管理。

* * *

### 伺服器模式 {#server-modes}

`server.mode` 會選擇四種安全契約之一。四種都允許 `SELECT` 和中繼資料讀取；差別在於如何處理變更與生命週期作業。

| 模式 | `SELECT` / 中繼資料 | `INSERT` / `UPDATE` / `REPLACE` | `DELETE` | `EXEC`（生命週期） |
|--|--|--|--|--|
|`read_only`|允許|拒絕|拒絕|拒絕|
|`safe`（預設）|允許|需要核准|需要核准|需要核准|
|`delete_safe`|允許|允許|需要核准|需要核准|
|`full_access`|允許|允許|允許|允許|

<br />

**拒絕**會立即向用戶端傳回錯誤。

**需要核准**會使用 MCP 徵詢流程，其形式取決於協商的[協定修訂版](#protocol-revision-support)：

- 如果用戶端已宣告徵詢能力（在初始化時，或在 `2026-07-28` 上透過每個要求 `_meta` 中的用戶端能力），伺服器會以簡短訊息（工具名稱、查詢類別、SQL）要求使用者核准該動作。在 `2026-07-28` 上，核准要求會以 `input_required` 結果傳回，其中在 id `stackql_approval` 下攜帶一個 `elicitation/create` 輸入要求，用戶端再將答覆放在 `inputResponses` 中重試呼叫；在較早的修訂版上，伺服器會在呼叫進行中自行傳送 `elicitation/create` 要求。使用者可以接受、拒絕或取消。
- 如果用戶端**未**宣告徵詢能力，工具呼叫會被拒絕，並附上說明此缺口的訊息，指引維運人員改用 `full_access` 模式。

核准是由用戶端回答，因此它防範的是代理程式在使用者不知情下行動，而不是不受信任的呼叫端。呼叫端不受信任時，請將 HTTP 傳輸方式與[用戶端驗證](#http-client-authentication)搭配使用。

模式對每個伺服器是全域的。沒有個別工具的覆寫設定。

從 `v1.0` 起，[`run_select_query`](/mcp/run_select_query) 在所有模式下（包括 `full_access`）都會拒絕 `INSERT`、`UPDATE`、`REPLACE`、`DELETE` 和 `EXEC`；錯誤訊息會指出 `run_mutation_query` 或 `run_lifecycle_operation`。每個工具也都帶有 `title`，以及以明確布林值表示的四個 MCP 行為提示資訊（`readOnlyHint`、`destructiveHint`、`idempotentHint`、`openWorldHint`），這些值衍生自政策管控所強制執行的同一套分類。`openWorldHint` 對 `list_registry`、`pull_provider`、`run_select_query`、`run_mutation_query` 和 `run_lifecycle_operation` 為 `true`，因為它們會存取登錄庫或 Provider 端點；對僅讀取本機狀態的工具則為 `false`。這些提示資訊僅供用戶端 UX 參考；強制執行仍由政策管控負責。

:::info[承載管控]

從 `v0.12.742` 發行版本起，在所有模式下，包含多個陳述式的承載都由其中權限最高的陳述式管控，因此 `SELECT 1; DELETE ...` 會被視為 `DELETE`，稽核記錄的 `query_class` 為 `mutation_delete`。`validate_select_query` 只接受恰好一個陳述式。

:::

#### 預設模式的行為變更 {#default-mode-behaviour-change}

先前的發行版本只透過單一 `read_only: true/false` 旗標強制執行安全性，其預設值為「不強制執行」。目前的預設值（`mode: safe`）讓變更一開始就需要使用者核准。使用支援徵詢之用戶端（Claude Desktop、Cursor、Continue 等）的維運人員，每次變更都會看到一個核准要求。執行自動化作業或使用隨附 `stackql_mcp_client`（不宣告徵詢能力）的維運人員，必須明確選擇加入限制較少的模式：`mode: delete_safe` 可執行 `INSERT`、`UPDATE` 和 `REPLACE`，`mode: full_access` 則還可執行 `DELETE` 和生命週期作業。

舊版的 `read_only: true` JSON / YAML 鍵為了回溯相容仍可接受，並視同 `mode: read_only`。

#### 範例 {#examples}

```bash
# Read-only: SELECTs proceed; mutations and lifecycle refused immediately.
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true, "mode": "read_only"}}'

# Delete-safe: INSERT/UPDATE proceed; DELETE and EXEC need approval.
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true, "mode": "delete_safe"}}'

# Full access: everything proceeds without prompting.  Use only with trusted
# clients and operators.
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true, "mode": "full_access"}}'
```

* * *

### 稽核記錄 {#audit-log}

每次工具呼叫都會將一筆記錄寫入設定的稽核 sink，預設為 JSONL，或為 OpenTelemetry 記錄項目（請參閱[記錄格式](#log-format)）。稽核**預設為開啟**。稽核回答的是「代理程式做了什麼」，而不是「代理程式看到了什麼」：無論採用哪種格式，都刻意不記錄 `SELECT` 陳述式的結果資料列。

#### 記錄的內容 {#what-gets-recorded}

| 欄位 | 描述 |
|--|--|
|`timestamp`|呼叫開始時的實際時間（RFC3339）。|
|`tool`|工具名稱（例如 `run_select_query`）。|
|`mode`|呼叫時生效的伺服器模式。|
|`decision`|`allow` / `refuse_immediate` / `needs_approval_accepted` / `needs_approval_declined` / `needs_approval_cancelled` / `needs_approval_unavailable`.|
|`query_class`|`select` / `mutation_create` / `mutation_delete` / `lifecycle` / `unknown`。對於包含多個陳述式的承載，為其中權限最高者的類別。|
|`sql`|查詢工具（`run_select_query`、`run_mutation_query`、`run_lifecycle_operation`、`validate_select_query`）的 SQL 字串。|
|`args`|中繼資料工具（`list_*`、`describe_method`）的階層欄位；查詢工具則為 SQL + `row_limit`。|
|`duration_ms`|管控 + 處理常式的實際經過時間。|
|`error`|工具發生錯誤或遭拒時的錯誤訊息。|

<br />

`SELECT` 陳述式的結果資料列刻意排除在外：它們可能非常龐大，也可能包含敏感資料。

#### 檔案 sink {#file-sink}

目前唯一提供的 sink 是 `file`。每行一個 JSON 物件，每筆記錄之後都會執行 fsync，並依大小 / 存在時間 / 備份數量進行 lumberjack 式的輪替。

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{
    "server": {
      "transport": "http",
      "address": "127.0.0.1:9912",
      "allow_unauthenticated": true,
      "audit": {
        "file": {
          "path": "/var/log/stackql-mcp.log",
          "max_size_mb": 100,
          "max_backups": 5,
          "max_age_days": 30
        }
      }
    }
  }'
```

指定位置的方式有兩種：

- `audit.file.path`：完整的檔案路徑（絕對路徑，或相對於 cwd 的路徑）。
- `audit.file.dir`：一個目錄；sink 會在其中使用 `stackql_mcp_server_<RFC3339-utc-second>.log` 作為檔名。

`mcp.config` 中兩者皆未設定時，MCP 伺服器會將 `dir` 預設為 cwd，讓現有的維運人員看到與先前相同的行為。底層的通用 sink 套件本身拒絕自行默默選擇目錄：「記錄要寫到哪裡」一律由呼叫端決定。

解析後的絕對路徑會在啟動時以 `sink file: /path/to/file.log` 的形式記錄到 stderr。

#### 記錄格式 {#log-format}

稽核串流有兩種編碼，寫入同一個檔案 sink：

```bash
stackql mcp --mcp.server.type=stdio --mcp.log.format=jsonl   # default, unchanged
stackql mcp --mcp.server.type=stdio --mcp.log.format=otel    # OTLP/JSON log records
```

此旗標會覆寫 `mcp.config` 中的 `server.audit.format`（`"audit": {"format": "otel"}`）。`jsonl` 與上述記錄逐位元組相容。`otel` 每行寫入一個 OTLP/JSON `LogsData` 物件，這正是 OpenTelemetry Collector 的 `otlp_json_file` 接收器可直接擷取的形式（已針對 otelcol-contrib 0.160.0 驗證），因此串流不需要任何轉換處理器就能進入任何 OTLP 管線：

```yaml
receivers:
  otlp_json_file:
    include: [/var/log/stackql-mcp.log]
exporters:
  debug: {}
service:
  pipelines:
    logs:
      receivers: [otlp_json_file]
      exporters: [debug]
```

每次工具呼叫產生一筆記錄；經過核准管控的呼叫會為徵詢決策產生第二筆記錄。同一次呼叫的記錄共用 `gen_ai.tool.call.id` 和一個追蹤 id。呼叫端在 `params._meta` 中提供的 W3C `traceparent` 會被採用；否則每個 MCP 工作階段會產生一個追蹤 id，讓代理程式工作階段的記錄可以彼此關聯。

輸出的屬性集是一個有版本的介面：檢測範圍為 `github.com/stackql/stackql/pkg/mcp_server/audit`，版本 `1.0.0`，`schemaUrl` 則將穩定版慣例固定為 `https://opentelemetry.io/schemas/1.44.0`。`gen_ai.*` 和 `mcp.*` 屬性遵循 [GenAI](https://github.com/open-telemetry/semantic-conventions-genai/blob/main/docs/gen-ai/gen-ai-spans.md#execute-tool-span) 和 [MCP](https://github.com/open-telemetry/semantic-conventions-genai/blob/main/docs/gen-ai/mcp.md) 語意慣例，這些慣例目前為 Development 狀態；屬性集變更時會提高範圍版本。

| 屬性 | 記錄 | 值 |
|--|--|--|
|`service.name`, `service.version`|資源|`stackql`、StackQL 版本|
|`gen_ai.operation.name`|工具呼叫|`execute_tool`|
|`mcp.method.name`|兩者|工具呼叫記錄上為 `tools/call`，決策記錄上為 `elicitation/create`|
|`gen_ai.tool.name`|兩者|工具名稱|
|`gen_ai.tool.call.id`|兩者|每次呼叫產生，由兩筆記錄共用|
|`mcp.protocol.version`|兩者|協商的修訂版|
|`mcp.session.id`|兩者|工作階段 id（無工作階段的 HTTP 要求沒有此屬性）|
|`stackql.mode`|兩者|伺服器模式|
|`stackql.decision`|兩者|上表中的 `decision` 值|
|`stackql.query`|工具呼叫|原始 SQL，適用於查詢工具|
|`stackql.query_class`|工具呼叫|`query_class` 值|
|`stackql.provider`|工具呼叫|Provider，適用於中繼資料工具|
|`stackql.query.source`|工具呼叫|Query Library 項目 id（有提供時）|
|`stackql.duration_ms`|工具呼叫|實際經過時間|
|`stackql.rows_returned`|工具呼叫|資料列數，適用於傳回資料列的工具|
|`error.type`, `error.message`|工具呼叫|失敗時：拒絕的決策，或通過管控後的 `tool_error`|

<br />

本文為 `execute_tool <tool>` / `elicitation <tool>`；嚴重性為 `INFO`，呼叫失敗或遭拒時為 `ERROR`。兩種格式的遮蔽契約相同：永遠不會序列化結果值，只會序列化產生結果的陳述式。

#### 失敗模式 {#failure-modes}

sink 傳回錯誤時，回應行為取決於 `audit.failure_mode`。

| failure_mode | 效果 |
|--|--|
|`strict`（預設）|即使底層工具執行成功，工具呼叫仍會將稽核錯誤傳回給用戶端。這是刻意的設計：寧可讓用戶端收到模稜兩可的回應，也不要有未被察覺的 `DELETE`。|
|`strict_mutations`|`SELECT` / 中繼資料讀取會繼續進行，並在 stderr 留下註記；變更與生命週期作業則會顯示稽核錯誤。|
|`best_effort`|一律記錄到 stderr 並繼續進行。|

#### 執行順序 {#sequencing}

稽核寫入發生在工具執行（或被管控擋下）**之後**，但在回應傳回用戶端**之前**。在 strict 模式下，成功的 DELETE 若稽核寫入失敗，代表資料列已刪除，但用戶端會收到錯誤：這是刻意的設計，確保沒有任何變更能在未經稽核的情況下通過。

#### 停用稽核 {#disabling-audit}

若要完全關閉稽核（如同在沒有稽核功能的 `v0.10.474` 之前的發行版本）：

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true, "audit": {"disabled": true}}}'
```

* * *

### 可用的 MCP 工具 {#available-mcp-tools}

以 MCP 伺服器執行時，StackQL 會公開下列工具，並受設定的 `enabled_tools` 允許清單限制。每個工具都會同時傳回轉譯後的文字檢視（供 LLM 使用）和具型別的結構化承載（供程式化用戶端使用）。預設的文字轉譯方式是：結構一致的多資料列結果使用 markdown 表格，稀疏或單筆記錄的結果使用 markdown KV 區塊。具有引數的工具接受選用的 `format`（`markdown` 或 `json`）覆寫；否則使用伺服器的轉譯設定。下方的輸入欄列出各工作特定的引數；`?` 表示選用輸入。

按一下任何工具名稱即可開啟完整的參考頁面，其中包括輸入、管控行為和範例提示詞。

| 工具 | 轉譯器 | 描述 | 輸入 |
|--|--|--|--|
|[`server_info`](/mcp/server_info)|KV|伺服器身分與執行階段資訊：stackql 版本、後端 SQL 引擎、Provider 登錄庫位置、模式、唯讀旗標。視需要呼叫，例如診斷遭拒或受管控的作業，或檢查特定版本的行為。|無|
|[`list_providers`](/mcp/list_providers)|表格|已提取到本機快取的 Provider，位於階層的最上層。|無|
|[`list_services`](/mcp/list_services)|表格|某個 Provider 下的服務。|`provider`|
|[`list_resources`](/mcp/list_resources)|表格|`provider`.`service` 下的資源。|`provider`, `service`|
|[`list_methods`](/mcp/list_methods)|表格|資源的存取方法（HTTP 作業），包含每個方法對應的 SQL 動詞及其必要參數。撰寫任何查詢之前請先呼叫：必要的 `WHERE` 參數就是從這裡推斷出來的。|`provider`, `service`, `resource`|
|[`describe_method`](/mcp/describe_method)|KV|單一方法的完整 I/O 契約（一律為 EXTENDED）：必要與選用的輸入，以及 `SELECT` 可參考的輸出欄位。|`provider`, `service`, `resource`, `method`|
|[`query_library_search`](/mcp/query_library_search)|表格|依自然語言意圖尋找精選的查詢範本。唯讀；變更項目需要選擇加入，且在 `read_only` 模式下會被排除。|`intent`, `provider?`, `service?`, `tags?`, `include_mutations?`, `limit?`|
|[`query_library_get`](/mcp/query_library_get)|KV|擷取範本及其參數宣告。若 `params` 不為空，則驗證值並傳回轉譯後的 SQL 與 `next_tool`，但不執行。|`id`, `params?`|
|[`validate_select_query`](/mcp/validate_select_query)|KV|剖析並規劃單一 `SELECT`，但不執行。傳回 `{valid, errors}`。僅限 `SELECT`；恰好一個陳述式。|`sql`|
|[`run_select_query`](/mcp/run_select_query)|表格|執行 `SELECT`。傳回 `{rows}`。僅限讀取。|`sql`, `row_limit?`|
|[`run_mutation_query`](/mcp/run_mutation_query)|KV|對 Provider 執行 `INSERT`/`UPDATE`/`REPLACE`/`DELETE`。**會產生實際副作用。** 傳回 `{messages, timestamp}`。由伺服器[模式](#server-modes)管控。|`sql`|
|[`run_lifecycle_operation`](/mcp/run_lifecycle_operation)|KV|執行 stackql `EXEC` 生命週期作業。傳回 `{messages, timestamp}`。由伺服器[模式](#server-modes)管控。|`sql`|
|[`list_registry`](/mcp/list_registry)|表格|設定的登錄庫中可用的 Provider（及其版本）。與 `list_providers` 不同，後者只列出已提取的 Provider。|`provider?`|
|[`pull_provider`](/mcp/pull_provider)|KV|從登錄庫將單一 Provider 安裝到本機快取。僅影響本機快取狀態，對雲端控制平面或資料平面沒有影響。|`provider`, `version?`|
|[`reload_credentials`](/mcp/reload_credentials)|表格|從 `--env.file` dotenv 檔案將憑證重新載入處理程序環境，並回報每個 Provider 的解析狀態（`ok`、`unresolved`、`not_checked`）。永遠不會傳回祕密值。在所有模式下都允許。|`provider?`|

從頭撰寫 SQL 之前，請先查閱 `query_library_search`，再使用 `query_library_get` 檢查並轉譯相符的範本。查詢工具也接受選用的 `source`，其中包含程式庫項目 ID，僅用於歸屬標示；執行仍受一般的伺服器模式和核准政策約束。

:::note

`describe_resource` 已在 StackQL `v0.12.732` 中淘汰；曾呼叫它的 MCP 用戶端、提示詞或代理程式指示，應改為先呼叫 [`list_methods`](/mcp/list_methods)，再呼叫 `describe_method`。資源沒有單一的欄位清單，因為每個存取方法都會傳回各自的形狀，所以資料行名稱來自查詢所路由到之方法的 [`describe_method`](/mcp/describe_method)。SQL [`DESCRIBE`](/language-spec/describe) 陳述式維持不變。

:::

### 可用的 MCP 提示詞 {#available-mcp-prompts}

發佈一個靜態提示詞。

| 提示詞 | 描述 |
|--|--|
|`write_safe_select`|針對 stackql 資源撰寫安全 `SELECT` 查詢的指引。說明如何使用 `SHOW METHODS IN <provider>.<service>.<resource>` 找出正確的讀取方法和必要的 `WHERE` 參數。|

* * *

### 旗標 {#flags}

| 旗標 | 描述 |
|--|--|
|`--mcp.server.type`|MCP 伺服器類型：`http`、`stdio` 或 `reverse_proxy`（後者與 `stackql srv` 搭配使用）。|
|`--mcp.config`|MCP 伺服器的 JSON 設定物件。也接受 YAML。|
|`--mcp.protocol.version`|所宣告的最新協定修訂版：`auto`（預設）、`2026-07-28` 或較舊的修訂版，例如 `2025-11-25`。請參閱[固定修訂版](#pinning-the-revision)。|
|`--env.file`|dotenv 格式的憑證檔案，在啟動時載入，並由 `reload_credentials` 工具視需要重新載入。請參閱[憑證（重新）載入](#credential-resourcing---envfile--reload_credentials)。|
|`--pgsrv.port`|PostgreSQL 線路協定伺服器的 TCP 連接埠（與 `stackql srv` 搭配使用）。|
|`-h`, `--help`|顯示說明資訊。|
|`-v`, `--verbose`|以詳細模式執行，輸出額外資訊。|

&nbsp;
&nbsp;
> 如需其他選項，請參閱[全域旗標](/command-line-usage/global-flags)

:::info

啟動 MCP 伺服器之前，您需要設定 Provider 驗證所需的環境變數，或使用 [`--env.file`](#credential-resourcing---envfile--reload_credentials) 指定 dotenv 格式的憑證檔案。如需詳細資訊，請參閱[使用 Provider](/getting-started/using-a-provider)。

:::

* * *

### 範例 {#examples-1}

#### 基本的獨立 MCP 伺服器 {#basic-standalone-mcp-server}

啟動一個具備 Provider 驗證、並為其用戶端設定 bearer 權杖的獨立 MCP 伺服器：

```bash
export GOOGLE_CREDENTIALS=$(cat /path/to/google-credentials.json)
export STACKQL_MCP_TOKEN="$(openssl rand -hex 32)"

stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "auth_token_env_var": "STACKQL_MCP_TOKEN"}}' \
  --registry='{"url": "https://registry.stackql.app/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

#### MCP 伺服器搭配 PostgreSQL 伺服器（記憶體內） {#mcp-server-with-postgresql-server-in-memory}

同時執行 MCP 和 PostgreSQL 伺服器，提供雙協定存取：

```bash
export GOOGLE_CREDENTIALS=$(cat /path/to/google-credentials.json)

stackql srv \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true}}' \
  --pgsrv.port 5665 \
  --registry='{"url": "https://registry.stackql.app/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

#### 稽核記錄寫入已知路徑的唯讀資源清單伺服器 {#read-only-inventory-server-with-audit-to-a-known-path}

適用於永遠不應進行變更的資源清單掃描代理程式：

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{
    "server": {
      "transport": "http",
      "address": "127.0.0.1:9912",
      "allow_unauthenticated": true,
      "mode": "read_only",
      "audit": {"file": {"path": "/var/log/stackql-mcp-audit.log"}}
    }
  }' \
  --registry='{"url": "https://registry.stackql.app/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

#### 完整存取的自動化伺服器 {#full-access-automation-server}

適用於需要變更、且無法回應徵詢要求的受信任自動化管線。管線會出示存放在 `STACKQL_MCP_TOKEN` 中的 bearer 權杖：

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{
    "server": {
      "transport": "http",
      "address": "127.0.0.1:9912",
      "auth_token_env_var": "STACKQL_MCP_TOKEN",
      "mode": "full_access"
    }
  }' \
  --registry='{"url": "https://registry.stackql.app/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

#### 使用 TLS 的安全 MCP 伺服器 {#secure-mcp-server-with-tls}

以反向代理模式啟動使用 TLS 加密的 MCP 伺服器：

首先，產生 TLS 憑證（開發用的自我簽署憑證）：

```bash
openssl req -x509 -newkey rsa:4096 -keyout server_key.pem -out server_cert.pem -days 365 -nodes
```

然後啟動伺服器：

```bash
export STACKQL_MCP_TOKEN="$(openssl rand -hex 32)"

stackql srv \
  --mcp.server.type=reverse_proxy \
  --mcp.config '{
    "server": {
      "tls_cert_file": "server_cert.pem",
      "tls_key_file": "server_key.pem",
      "transport": "http",
      "address": "127.0.0.1:9004",
      "auth_token_env_var": "STACKQL_MCP_TOKEN"
    },
    "backend": {
      "dsn": "postgres://stackql:stackql@127.0.0.1:5446?default_query_exec_mode=simple_protocol"
    }
  }' \
  --pgsrv.port 5446 \
  --registry='{"url": "https://registry.stackql.app/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

* * *

### 測試您的 MCP 伺服器 {#testing-your-mcp-server}

存放庫隨附一個開發用 MCP 用戶端（`stackql_mcp_client`），用於指令碼與迴歸測試。使用下列命令建置：

```bash
python cicd/python/build.py --build-mcp-client
```

這會產生 `./build/stackql_mcp_client`。

請注意，`stackql_mcp_client` 不會傳送 `Authorization` 標頭，因此它連線的伺服器必須設定 `"allow_unauthenticated": true`（如上方範例所示）；受權杖保護的伺服器會以 `401` 回應它。它也**不會**宣告 MCP 徵詢能力，因為它是用於非互動式指令碼。對 `safe` 伺服器，它在變更與生命週期呼叫上會收到「client does not support elicitation」拒絕；對 `delete_safe` 伺服器，則是在 `DELETE` 和 `EXEC` 呼叫上。若要以徵詢要求進行互動式測試，請使用內嵌於編輯器的 MCP 用戶端（Claude Desktop、Cursor、Continue）。

```bash
# List all available tools
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912

# Server identity, mode, audit / registry context
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action server_info

# Re-source the --env.file credentials file and report per-provider
# credential status - a good first diagnostic when queries fail on auth
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action reload_credentials

# List providers already pulled into the local cache
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action list_providers

# List providers (and versions) available in the registry
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action list_registry

# Install a provider from the registry into the local cache
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action pull_provider \
  --exec.args '{"provider": "kafka"}'

# List services for a provider
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action list_services \
  --exec.args '{"provider": "google"}'

# List access methods for a resource (this is how an agent finds required
# WHERE parameters before writing a query)
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action list_methods \
  --exec.args '{"provider": "google", "service": "compute", "resource": "networks"}'

# Validate a SELECT without executing
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action validate_select_query \
  --exec.args '{"sql": "select name from google.compute.networks where project = '"'"'my-project'"'"';"}'

# Execute a SELECT
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action run_select_query \
  --exec.args '{"sql": "select name from google.compute.networks where project = '"'"'my-project'"'"';"}'

# Mutation - succeeds only against a full_access server, or against
# delete_safe (for INSERT/UPDATE) or safe with an elicitation-capable client.
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action run_mutation_query \
  --exec.args '{"sql": "delete from google.compute.firewalls where project = '"'"'my-project'"'"' and firewall = '"'"'old-rule'"'"';"}'
```

#### 回應範例 {#example-responses}

`server_info`（針對預設模式的伺服器，Windows 上透過 stdio 執行的 StackQL `v1.0.888`）：

```json
{
  "version": "1.0.888",
  "commit": "f7b4116",
  "build_date": "2026-11-05T00:26:03Z",
  "platform": "Windows",
  "transport": "stdio",
  "sql_backend": "sqlite3",
  "provider_registry": "https://registry.stackql.app/providers",
  "mode": "safe",
  "is_read_only": false
}
```

`run_select_query`:

```json
{
  "rows": [
    {"name": "default"},
    {"name": "pathfinders-test-01"},
    {"name": "returning-test-03"}
  ]
}
```

`run_mutation_query`（針對 `full_access` 伺服器）：

```json
{
  "messages": ["The operation was despatched successfully"],
  "timestamp": "2026-05-16T10:47:33+10:00 AEST"
}
```

同一個變更的稽核記錄行範例：

```json
{"timestamp":"2026-05-16T00:47:33Z","tool":"run_mutation_query","mode":"full_access","decision":"allow","query_class":"mutation_delete","sql":"delete from google.compute.firewalls where project = 'my-project' and firewall = 'old-rule';","args":{"sql":"delete from google.compute.firewalls where project = 'my-project' and firewall = 'old-rule';","row_limit":0},"duration_ms":42}
```

* * *

### 與 AI 助理整合 {#integration-with-ai-assistants}

若要將 StackQL 的 MCP 伺服器與 AI 助理整合，請在助理的設定中將 `stackql` 註冊為 MCP 伺服器。大多數內嵌於編輯器的 MCP 用戶端都透過 `stdio` 執行伺服器；對於這類用戶端，請使用 `--mcp.server.type=stdio`，由助理直接啟動處理程序。使用 HTTP 的獨立代理程式可以連線到長時間執行的 `stackql mcp --mcp.server.type=http` 處理程序。

關於 Claude Desktop，包括 Anthropic Connector Directory 中的項目（建議的安裝方式）、可下載的 MCP Bundle 安裝套件（`.mcpb`）和手動設定，請參閱[在 Claude Desktop 中使用 StackQL](/getting-started/claude-desktop)。

:::note

設定檔的位置因作業系統和 AI 助理而異。請查閱您的 AI 助理文件，以取得正確的路徑和格式。

:::

* * *

### 架構考量 {#architecture-considerations}

選擇部署模式時，請考量：

1. **獨立（`stackql mcp`）**：設定最簡單，適合開發用途，以及透過 `stdio` 連線的內嵌於編輯器用戶端。
2. **記憶體內雙伺服器（`stackql srv` 搭配 `mcp.server.type=http`）**：單一處理程序同時提供 MCP 和 PostgreSQL 線路協定。效能最佳，適合大多數生產環境部署。
3. **反向代理（`stackql srv` 搭配 `mcp.server.type=reverse_proxy`）**：
   - 可將工作負載分散到不同的處理程序或主機
   - 支援以 TLS 加密保護 MCP 端點
   - 可獨立調整 MCP 和 PostgreSQL 介面的規模
   - 提供整合企業 CA 的彈性（注意：企業 CA 支援為實驗性功能）

另外，在安全性方面：

- 從預設的 `mode: safe` 開始。支援徵詢的用戶端（Claude Desktop、Cursor、Continue）會在任何變更之前提示使用者。
- 對永遠不應寫入的資源清單或分析代理程式，固定使用 `mode: read_only`。
- 當您希望能自由建立和更新，但破壞性作業需要有人參與時，固定使用 `mode: delete_safe`。
- `mode: full_access` 僅保留給受信任的自動化管線，且只能搭配有人審查的稽核記錄使用。
- 在其他處理程序或主機可以連線的任何 HTTP 接聽程式上設定 `server.auth_token_env_var`；`allow_unauthenticated` 用於本機開發。單靠 TLS 不會驗證用戶端。

:::caution

搭配企業憑證授權單位（CA）使用 TLS 時，可能需要額外的設定。此功能為實驗性質，可能需要依您的 CA 實作進行調整。

:::

* * *

### 延伸閱讀 {#further-reading}

- 套件開發人員參考文件是 stackql 存放庫中的 [`pkg/mcp_server` README](https://github.com/stackql/stackql/blob/main/pkg/mcp_server/README.md)。
- 供貢獻者參考的技術文件位於 stackql 存放庫中的 [`docs/mcp.md`](https://github.com/stackql/stackql/blob/main/docs/mcp.md)，其中包含每個工具的完整回應範例。
