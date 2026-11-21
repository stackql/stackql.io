---
title: 全域旗標
hide_title: false
hide_table_of_contents: true
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "stackql 二進位檔的全域旗標：涵蓋驗證、輸出格式、登錄庫位置、記錄和 SQL 後端設定，適用於每個命令。"
image: "/img/stackql-featured-image.png"
---

StackQL 命令都接受全域旗標，但部分旗標只適用於特定模式。可在命令列上設定這些旗標，或在支援的情況下，於 StackQL 初始化檔案（[`.stackqlrc`](/getting-started/using-stackql#the-stackqlrc-initialization-file)）中設定。`--pgsrv.*` 旗標列於 [PostgreSQL 伺服器選項](/command-line-usage/srv#server-options)。

| 旗標 | 資料型別 | 描述 | 預設值 |
| -- | -- | -- | -- |
| <span class="nowrap">`--access.config`</span> | string | 呼叫端存取設定，為 JSON 封套 `{"path": "/path/to/access.json"}`（也接受單純的檔案路徑）。啟用[存取管理](/command-line-usage/access-management)：呼叫端透過 JWT、靜態 bearer 權杖、相互 TLS 或本機 OS 身分進行驗證，陳述式則依據政策規則進行授權。本機權杖憑證從 `STACKQL_CALLER_TOKEN` 讀取。自 StackQL `v1.0` 發行版本起提供。 | |
| <span class="nowrap">`--allow-persistent-secrets`</span> | flag | 允許工作階段使用 `SET "$.secret_store"` 選取持續性 OS 祕密存放區（macOS Keychain、Linux Secret Service 或 Windows Credential Manager）。即使設定此旗標，平面檔案存放區仍會遭拒。請參閱 [Provider 工作階段與祕密](/getting-started/provider-sessions#persistent-os-secret-stores)。自 StackQL `v1.0` 發行版本起提供。 | `false` |
| <span class="nowrap">`--allow-secret-dereference`</span> | flag | 允許在 SQL 運算式中解除參考祕密和憑證值（`"$secrets.name"`、`"$auth.provider.field"`），其後果是這些值可能進入結果、記錄和持續保存的查詢成品。一般的 Provider 驗證不需要此旗標。請參閱 [SQL 中的祕密值](/getting-started/provider-sessions#secret-values-in-sql)。自 StackQL `v1.0` 發行版本起提供。 | `false` |
| <span class="nowrap">`--apirequesttimeout`</span> | integer | API 要求逾時（秒），0 表示不逾時 | `45` |
| <span class="nowrap">`--approot`</span> | string | 應用程式設定和快取的根路徑 | `{cwd}/.stackql` |
| <span class="nowrap">`--approotfilemode`</span> | uint32 | 應用程式設定和快取的檔案模式 | `493` |
| <span class="nowrap">`--auth`</span> | string | json 格式的 `auth` 內容鍵值對 |  |
| <span class="nowrap">`--cachekeycount`</span> | integer | 快取的初始鍵數量 | `100` |
| <span class="nowrap">`--configfile`</span> | string | 設定檔的完整路徑 | `{cwd}/.stackqlrc` |
| <span class="nowrap">`--cpuprofile`</span> | string | `cpuprofile` 檔案，若為空則不使用 | `null` |
| <span class="nowrap">`--dataflow.dependency.max`</span> | int | 指定查詢的資料流相依性深度上限 | `50` |
| <span class="nowrap">`--dataflow.components.max`</span> | int | 指定查詢的資料流弱連通元件數上限 | `50` |
| <span class="nowrap">`--dbInternal`</span> | string | 用來設定 DBMS 內部維護查詢處理方式的 JSON / YAML 字串 | `{}` |
| <span class="nowrap">`-d`, `--delimiter`</span> | string | csv 輸出的分隔符號。僅限單一字元。所有非 csv 輸出都會忽略此旗標 | `,` |
| <span class="nowrap">`--dryrun`</span> | flag | `dryrun` 旗標；只執行前置處理器，並傳回範本化的輸出 | `false` |
| <span class="nowrap">`--env.file`</span> | string | 選用的 dotenv 樣式檔案，會在啟動時載入處理程序環境（所有命令）；MCP [`reload_credentials`](/mcp/reload_credentials) 工具會視需要重新載入此檔案。檔案不存在時不會出錯；值為空的鍵會略過；現有的變數會被覆寫。不會從 `.stackqlrc` 載入。在 StackQL `v0.10.542` 之後的發行版本中提供。 | `null` |
| <span class="nowrap">`--execution.concurrency.limit`</span> | int | 查詢執行的並行限制 | `1` |
| <span class="nowrap">`--export.alias`</span> | string | 匯出別名前置詞（命名空間或 schema） | |
| <span class="nowrap">`--gc`</span> | string | 表示 GC 設定的 JSON / YAML 字串 | `{}` |
| <span class="nowrap">`-H`, `--hideheaders`</span> | flag | 停用資料行標頭，僅在使用 `--output csv` 或 `--output text` 時有效 | `false` |
| <span class="nowrap">`--http.log.enabled`</span> | flag | 在終端機中顯示 http 要求資訊 | `false` |
| <span class="nowrap">`--http.proxy.host`</span> | string | Proxy 主機（留空表示不使用 Proxy） | `null` |
| <span class="nowrap">`--http.proxy.password`</span> | string | Proxy 密碼 | `null` |
| <span class="nowrap">`--http.proxy.port`</span> | integer | Proxy 連接埠，任何小於或等於 0 的數字都會使用指定協定的預設連接埠（例如 `http -> 80`） | `-1` |
| <span class="nowrap">`--http.proxy.scheme`</span> | string | Proxy 協定，`http` 或 `https` | `http` |
| <span class="nowrap">`--http.proxy.user`</span> | string | Proxy 使用者 | `null` |
| <span class="nowrap">`--http.response.maxResults`</span> | integer | 每個 http 要求的結果數上限，任何小於或等於 0 的數字表示不限制 | `-1` |
| <span class="nowrap">`--http.response.pageLimit`</span> | integer | 每個資源傳回的結果頁數上限，任何小於或等於 0 的數字表示不限制 | `20` |
| <span class="nowrap">`--indirect.depth.max`</span> | integer | 間接查詢的深度上限：檢視和子查詢 | `5` |
| <span class="nowrap">`-i`, `--infile`</span> | string | 讀取查詢的輸入檔案（IQL 檔案） | `{stdin}` |
| <span class="nowrap">`-q`, `--iqldata`</span> | string | 用於範本化的內容（資料）檔案（`json` 或 `jsonnet` 檔案）| |
| <span class="nowrap">`--loglevel`</span> | string | 記錄層級，必須是 \{`panic`, `fatal`, `error`, `warn`, `info`, `debug`, `trace`\} 其中之一（也接受 `warning` 作為 `warn` 的別名） | `fatal` |
| <span class="nowrap">`--mcp.config`</span> | string | 用於 [MCP 伺服器](/command-line-usage/mcp#mcp-configuration-object)（`stackql mcp` 和 `stackql srv`）的 JSON / YAML 設定物件 | `{}` |
| <span class="nowrap">`--mcp.log.format`</span> | string | MCP 稽核記錄的編碼，`jsonl` 或 `otel`（OTLP/JSON 記錄項目）；會覆寫 `--mcp.config` 中的 `server.audit.format`。請參閱[記錄格式](/command-line-usage/mcp#log-format)。自 StackQL `v0.11.660` 發行版本起提供。 | `jsonl` |
| <span class="nowrap">`--mcp.protocol.version`</span> | string | 宣告的最新 MCP 協定修訂版本，可為 `auto`、`2026-07-28`（僅限無工作階段模式）或較舊的修訂版本，例如 `2025-11-25`；會覆寫 `--mcp.config` 中的 `server.protocol_version`。請參閱[固定修訂版本](/command-line-usage/mcp#pinning-the-revision)。自 StackQL `v0.12.732` 發行版本起提供。 | `auto` |
| <span class="nowrap">`--mcp.server.type`</span> | string | MCP 伺服器類型，為 \{`http`, `stdio`, `reverse_proxy`\} 其中之一。請參閱 [MCP 伺服器類型](/command-line-usage/mcp#mcp-server-type)。 | `http` |
| <span class="nowrap">`--metadatattl`</span> | integer | 快取中繼資料文件的 TTL，以秒為單位 | `3600` |
| <span class="nowrap">`--namespaces`</span> | string | 表示快取、檢視等所用命名空間的 JSON / YAML 字串 | `{}` |
| <span class="nowrap">`--offline`</span> | flag | 使用快取資料離線工作 | `false` |
| <span class="nowrap">`-f`, `--outfile`</span> | string | 寫入結果的輸出檔案 | `{stdout}` |
| <span class="nowrap">`-o`, `--output`</span> | string | 輸出格式，必須是 \{`json`, `jsonl`, `table`, `csv`, `text`, `otel`\} 其中之一（也接受 `ndjson` 作為 `jsonl` 的別名）。`otel` 會發出 OpenTelemetry 記錄項目，請參閱[輸出模式](/getting-started/output-modes#otel-output)；自 StackQL `v0.11.660` 發行版本起提供。若在 `--otel.config` 中設定了匯出工具，`otel` 記錄項目也會推送至該端點 | `table` |
| <span class="nowrap">`--otel.config`</span> | string | 設定 `otel` 輸出格式的 JSON / YAML 字串；`exporter.endpoint`、`exporter.headers`、`exporter.timeout_ms` 和 `exporter.batch_size` 會將記錄項目推送至 OTLP/HTTP 記錄端點，請參閱[推送至 OTLP 端點](/getting-started/output-modes#pushing-to-an-otlp-endpoint)。自 StackQL `v0.12.732` 發行版本起提供 | `{}` |
| <span class="nowrap">`--preview`</span> | string | 啟用預覽功能的 JSON 字串。`{"unstable": true}` 會公開由文件驅動的 `stackql_unstable_<provider>.<service>.<resource>` 關聯，這些關聯由 `omnisdk` 直接從 Provider 文件解析並執行，資料列會在產生時以串流方式傳回；其餘的鍵（`batchSize`、`flushInterval`、`endpoint`、`insecureSkipTLSVerify`）用於調整該後端。預覽功能並不穩定：行為、語法和輸出可能在發行版本之間變更或中斷，且不另行通知，因此請勿在生產環境中依賴此功能。自 `v1.0` 起，同一個選擇加入設定也會公開 `stackql_preview.iac.run` 和 `stackql_preview.iac.destroy` 關聯，這兩個關聯會透過 `omnisdk` 套用和拆除宣告的資源集，並將狀態保存在應用程式根目錄下。請參閱[預覽功能](https://github.com/stackql/stackql/blob/main/docs/preview.md) | `{}` |
| <span class="nowrap">`--provider`</span> | string | 初始預設 Provider。例如，使用 `--provider github` 時，`repos.repos` 會解析為 `github.repos.repos`，不需要先執行 `USE` 陳述式。不會向 Provider 進行驗證。 | 無 |
| <span class="nowrap">`--querycachesize`</span> | integer | 查詢計畫 LRU 快取的大小（項目數） | `10000` |
| <span class="nowrap">`--registry`</span> | string | 設定 Provider 登錄庫位置和 Provider 文件簽章驗證的 JSON / YAML 字串 | 請參閱 [`--registry`](#--registry) |
| <span class="nowrap">`--session`</span> | string | 表示工作階段設定的 JSON / YAML 字串 | `{}` |
| <span class="nowrap">`--sqlBackend`</span> | string | 表示 SQL 後端系統設定的 JSON / YAML 字串 | 請參閱 [`--sqlBackend`](#--sqlbackend) |
| <span class="nowrap">`--store.txn`</span> | string | 表示 Txn 存放區設定的 JSON / YAML 字串 | `{}` |
| <span class="nowrap">`--tls.CABundle`</span> | string | CA 套件組合的路徑，未指定時使用系統預設值 |  |
| <span class="nowrap">`--tls.allowInsecure`</span> | flag | 允許信任不安全的憑證（不建議） |  |
| <span class="nowrap">`--upstream.errors`</span> | string | 用於在 Provider 讀取時明確容許特定 HTTP 回應狀態的 JSON / YAML 政策。請參閱 [`--upstream.errors`](#--upstream-errors)。 | `{}` |
| <span class="nowrap">`--var`</span> | string | 以鍵值對形式提供的外部變數，透過 `jsonnet` 資料檔案載入 StackQL 查詢 |  |
| <span class="nowrap">`-v`, `--verbose`</span> | flag | 詳細輸出 | `false` |
| <span class="nowrap">`-h`, `--help`</span> | | stackql 的內容相關說明 | |
| <span class="nowrap">`--version`</span> | | 顯示 StackQL 程式的版本 | |

`--colorscheme` 已淘汰，不再變更 shell 色彩。`--usenonpreferredapis` 仍可接受，但在目前的引擎中不會改變 API 的選擇。

## `--registry` {#--registry}

`--registry` 旗標設定從何處讀取 Provider 文件，以及是否驗證其簽章。值為包含下列鍵的 JSON 字串：

| 鍵 | 描述 | 預設值 |
| -- | -- | -- |
| `url` | 登錄庫 URL。省略時使用公用 StackQL Provider 登錄庫。 | 公用登錄庫 |
| `localDocRoot` | 存放 Provider 文件的本機目錄，會直接讀取，而非從登錄庫下載。用於[開發 Provider](/developers/developing-stackql-providers#testing-a-provider-locally) 時。 | |
| `srcPrefix` | 登錄庫根目錄下存放 Provider 來源文件的路徑前置詞。 | `src` |
| `verifyConfig.nopVerify` | `true` 會略過簽章驗證。設為 `false` 時，每份讀取的 Provider 和服務文件都必須帶有內嵌的 ML-DSA-65（FIPS 204）簽章，包括以文件為基礎的預覽關聯。 | `true` |
| `verifyConfig.publicKeys` | 將金鑰 ID 對應至十六進位編碼 ML-DSA-65 公開金鑰的 JSON 物件，適用於私人登錄庫。它會取代內嵌的生產環境金鑰；明確指定空物件會產生錯誤。 | 內嵌金鑰 |

### 範例：要求已簽署的 Provider 文件 {#example-require-signed-provider-documents}

針對從公用登錄庫讀取的每份 Provider 和服務文件啟用驗證，並以內嵌的生產環境金鑰進行檢查：

```bash
stackql --registry '{"verifyConfig": {"nopVerify": false}}' shell
```

### 範例：從本機目錄讀取未簽署的文件 {#example-read-unsigned-documents-from-a-local-directory}

將登錄庫指向開發中的 Provider 文件目錄。這些文件未經簽署，因此驗證保持關閉：

```bash
stackql --registry '{"localDocRoot": "/path/to/provider-dev/openapi", "verifyConfig": {"nopVerify": true}}' shell
```

:::note

簽章驗證自 StackQL `v1.0` 發行版本起提供，且預設為關閉。簽章涵蓋文件確切的 YAML 位元組，因此任何編輯（包括格式或換行字元的變更）都會使簽章失效；請使用登錄庫的 `pqsig sign-doc` 工具重新簽署。未簽署的本機開發文件必須保持 `nopVerify: true`。先前的憑證選項和 `.sig` sidecar 檔案已不再使用，僅以 sidecar 發行的封存檔必須重新發行，才能在驗證模式下使用。驗證會在讀取文件時確認其真實性；封存檔的下載和解壓縮不在涵蓋範圍內。

:::

## `--sqlBackend` {#--sqlbackend}

`--sqlBackend` 旗標為 StackQL 設定外部 SQL 後端，讓您使用外部資料庫引擎（例如 PostgreSQL）取代預設的內嵌 SQL 引擎。這適用於利用外部資料庫進行查詢處理、持續性儲存，或與現有的資料庫基礎架構整合。

值為包含下列鍵的 JSON 字串：

| 鍵 | 描述 | 範例值 |
| -- | -- | -- |
| `dbEngine` | 資料庫引擎類型 | `postgres_tcp` |
| `sqlDialect` | 要使用的 SQL 方言 | `postgres` |
| `dsn` | 資料庫的資料來源名稱（連線字串） | 請參閱下方範例 |

### 範例：使用 PostgreSQL 後端 {#example-using-a-postgresql-backend}

```bash
./stackql \
  --sqlBackend='{"dbEngine": "postgres_tcp", "sqlDialect": "postgres", "dsn": "postgres://user:password@hostname:port/dbname?sslmode=require"}' \
  shell
```

### 範例：使用 Databricks PostgreSQL 介面 {#example-using-databricks-postgresql-interface}

Databricks 提供與 PostgreSQL 相容的介面，可作為 StackQL 的 SQL 後端。DSN 使用標準的 PostgreSQL 連線字串格式，並搭配您的 Databricks 憑證：

```bash
./stackql \
  --sqlBackend='{"dbEngine": "postgres_tcp", "sqlDialect": "postgres", "dsn": "postgres://user%40domain.com:your-access-token@your-databricks-host.cloud.databricks.com/databricks_postgres?sslmode=require"}' \
  shell
```

<br/>

:::note
使用者名稱或密碼中的特殊字元（例如 `@`）必須在 DSN 中進行 URL 編碼（例如 `@` 會變成 `%40`）。
:::

## `--upstream.errors` {#--upstream-errors}

使用 `http.ignoreStatusCodes` 列出 Provider 讀取時應明確容許的 HTTP 回應狀態。項目為字串：可使用確切的狀態碼（例如 `"503"`）或類別（例如 `"5xx"`）。預設值 `{}` 不會對引擎處理上游回應的一般方式新增任何例外。此設定不會重試失敗的要求，也不適用於變更或驗證交換。

被忽略的回應不會產生任何資料列。查詢中的其他讀取仍可傳回資料列，因此成功的查詢可能會傳回不完整的結果。StackQL 會將被忽略的 HTTP 狀態回報為診斷資訊。

例如，[模擬 GitHub 聯結測試](https://github.com/stackql/stackql/blob/256187fab2410b7c999e7208bd888b12c5df9b9e/test/robot/functional/stackql_mocked_from_cmd_line.robot#L2104)就使用了此選項：

```bash
stackql exec --upstream.errors='{"http":{"ignoreStatusCodes":["5xx"]}}' \
  "SELECT r.name, c.login FROM github.repos.repos r
   JOIN github.repos.collaborators c ON c.repo = r.name
   WHERE r.org = 'example-org' AND c.owner = 'example-org'
   ORDER BY r.name;"
```

在此測試中，某個存放庫的 collaborators 要求會傳回 HTTP `500`。查詢仍會傳回其他存放庫的協作者資料列，而 stderr 會包含 `ignoring upstream response: 500 INTERNAL SERVER ERROR`。若預期只有一種失敗，建議使用確切的狀態碼。
