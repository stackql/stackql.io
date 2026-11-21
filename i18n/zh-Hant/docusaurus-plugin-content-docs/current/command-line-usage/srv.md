---
title: srv
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "stackql srv 命令會將 StackQL 作為 PostgreSQL 線路協定伺服器執行，讓 psql、BI 工具和語言驅動程式能以 SQL 查詢雲端與 SaaS API。"
image: "/img/stackql-featured-image.png"
---

用於將 StackQL 作為服務啟動的命令，啟動後可由使用 mTLS 驗證的 [Postgres 線路協定](https://www.postgresql.org/docs/current/protocol.html) 用戶端存取。`srv` 命令也可以在 PostgreSQL 線路協定伺服器之外，同時執行 [Model Context Protocol (MCP)](/command-line-usage/mcp) 伺服器，讓傳統資料庫用戶端與 AI 代理程式都能透過雙協定存取。

* * * 

### 語法 {#syntax}

`stackql srv [server_options] [flags]`

* * *

:::note

雖然 StackQL 伺服器使用 Postgres 線路協定，但它不是 Postgres 伺服器，也不需要 Postgres 伺服器。用戶端使用線路協定與伺服器通訊，而伺服器使用 StackQL 對遠端雲端與 SaaS 供應商執行查詢或 DML 作業，並將結果傳回用戶端。

:::

透過延伸查詢協定（`Parse` / `Bind` / `Execute`，`psycopg` 3、`pgx` 和 JDBC 使用此協定）傳送的參數，會在伺服器端繫結為字串常值。自 `v0.12.742` 起，字串常值、加上引號的識別碼或註解中的 `$n` 預留位置會保持不變，參數值中的反斜線會被逸出，而文字值 `NULL` 仍為字串 `NULL`（SQL `NULL` 另行傳遞）。

### 伺服器選項 {#server-options}

| 選項 | 描述 | 預設 |
|--|--|--|
|<span class="nowrap">`--pgsrv.address`</span>|伺服器接聽的位址。|`0.0.0.0`|
|<span class="nowrap">`--pgsrv.port`</span>|伺服器接聽的 TCP 連接埠。|`5466`|
|<span class="nowrap">`--pgsrv.tls`</span>|JSON TLS 設定物件。請參閱下方的 [mTLS 範例](#examples)。|無|
|<span class="nowrap">`--pgsrv.debug.enable`</span>|為 PostgreSQL 用戶端啟用查詢偵錯輸出，例如 `stackql srv --pgsrv.debug.enable=true`。除非用戶端應接收偵錯資訊，否則請保持關閉。|`false`|
|<span class="nowrap">`--pgsrv.sundry`</span>|進階 JSON 或 YAML 對應，僅在未設定 `--pgsrv.tls` 時傳遞給 PostgreSQL 線路伺服器。|`{}`|
|<span class="nowrap">`--pgsrv.loglevel`</span>|可接受此選項，但在目前的引擎中不會設定記錄器層級。請改用 `--loglevel`。|`WARN`|

<br/>

> 如需其他選項，請參閱[全域旗標](/command-line-usage/global-flags)

### MCP 伺服器選項 {#mcp-server-options}

以 MCP 支援執行 `srv` 命令時，可使用下列額外選項：

| 選項 | 描述 |
|--|--|
|<span class="nowrap">`--mcp.server.type`</span>|MCP 伺服器類型：`http`（記憶體內）或 `reverse_proxy`（以 TCP 為基礎）|
|<span class="nowrap">`--mcp.config`</span>|MCP 伺服器的 JSON 設定物件（請參閱 [MCP 文件](/command-line-usage/mcp)）|
|<span class="nowrap">`--mcp.log.format`</span>|MCP 稽核記錄編碼：`jsonl`（預設）或 `otel`（OTLP/JSON 記錄項目）；會覆寫設定物件中的 `server.audit.format`|
|<span class="nowrap">`--mcp.protocol.version`</span>|宣告的最新 MCP 協定修訂版：`auto`（預設）、`2026-07-28`（僅限無工作階段）或較舊的修訂版，例如 `2025-11-25`；會覆寫設定物件中的 `server.protocol_version`|

<br/>

:::info[v0.12.742 中的重大變更]

自 `v0.12.742` 起，除非設定物件以 `server.auth_token_env_var` 指定 bearer 權杖，或以 `"allow_unauthenticated": true` 明確選擇退出，否則 MCP HTTP 傳輸方式（`--mcp.server.type=http` 或 `reverse_proxy`）會拒絕啟動。請參閱 [HTTP 用戶端驗證](/command-line-usage/mcp#http-client-authentication)。

:::

:::tip

如需設定和使用 MCP 伺服器的詳細資訊，包括部署模式、設定選項以及與 AI 助理的整合，請參閱 [MCP 命令文件](/command-line-usage/mcp)。

:::

:::info

啟動伺服器之前，您需要設定 Provider 驗證所需的環境變數，如需詳細資訊，請參閱[使用 Provider](/getting-started/using-a-provider)。

:::

對於長時間執行的伺服器，[`--env.file`](/command-line-usage/global-flags) 旗標可讓您在啟動前，將憑證寫入或輪替至磁碟上的 dotenv 樣式檔案，而不必將變數匯出到 shell 中：

```bash
stackql srv --pgsrv.port 5444 --env.file /etc/stackql/credentials.env
```

對於 `stackql srv`，此檔案只會在啟動時讀取一次；工作階段中途重新讀取憑證的途徑是 MCP [`reload_credentials`](/mcp/reload_credentials) 工具。

### 旗標 {#flags}

| 旗標 | 描述 |
|--|--|
|`-h,--help`|顯示說明資訊|
|`-v,--verbose`|以詳細模式執行查詢，並將額外輸出傳送至 stdout；若設定了全域 `-f`（`--outfile`）旗標，<br/>這些額外的記錄資訊會連同查詢結果一起寫入輸出檔案|

<br/>

> 如需其他選項，請參閱[全域旗標](/command-line-usage/global-flags)

* * *

### 範例 {#examples}

啟動 StackQL 伺服器處理程序，並使用 `psql` 命令列用戶端連線。

首先，從 [stackql/stackql](https://raw.githubusercontent.com/stackql/stackql/main/test/server/mtls/openssl.cnf) 下載 `openssl.cnf` 設定檔。

接著，產生伺服器和用戶端用於相互 TLS 驗證（mTLS）的憑證與金鑰檔案。

```bash
openssl req -x509 -keyout ~/stackqlcreds/server_key.pem -out ~/stackqlcreds/server_cert.pem -config ./openssl.cnf -days 365
openssl req -x509 -keyout ~/stackqlcreds/client_key.pem -out ~/stackqlcreds/client_cert.pem -config ./openssl.cnf -days 365
chmod 400 ~/stackqlcreds/client_key.pem
```

現在設定環境變數；如果用戶端和伺服器位於同一部主機上，這些變數可以共用。

```bash
export PGPORT=5444
export PGSSLCERT=~/stackqlcreds/client_cert.pem
export PGSSLKEY=~/stackqlcreds/client_key.pem
export PGSSLROOTCERT=~/stackqlcreds/server_cert.pem
export PGSSLSRVKEY=~/stackqlcreds/server_key.pem
export CLIENT_CERT=$(base64 -w 0 ~/stackqlcreds/client_cert.pem)
export PGSSLMODE=allow
```

設定伺服器所需 Provider 的憑證。以 GitHub 為例，該 Provider 會從下列兩個變數讀取使用者名稱和個人存取權杖：

```bash
export STACKQL_GITHUB_USERNAME=youruser
export STACKQL_GITHUB_PASSWORD=ghp_youraccesstoken
```

現在在一個終端機中啟動伺服器。

```bash
stackql srv \
--pgsrv.address=0.0.0.0 \
--pgsrv.port=$PGPORT \
--pgsrv.tls='{ "keyFilePath": "'${PGSSLSRVKEY}'", "certFilePath": "'${PGSSLROOTCERT}'", "clientCAs": [ "'${CLIENT_CERT}'" ] }'
```

在另一個終端機中，使用 `psql` 命令列用戶端連線到伺服器。

```bash
psql -h 127.0.0.1 -d stackql
```

現在對已驗證的 Provider 執行 StackQL 查詢。

```
psql (12.11 (Ubuntu 12.11-0ubuntu0.20.04.1), server 0.0.0)
Type "help" for help.

stackql=> SHOW SERVICES IN github LIKE '%repos%';
      id      | name  |           title
--------------+-------+----------------------------
 repos:v0.3.3 | repos | GitHub v3 REST API - repos
(1 row)

stackql=>
```
