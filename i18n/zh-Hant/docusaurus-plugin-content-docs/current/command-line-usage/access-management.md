---
title: 存取管理
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - 存取管理
  - 驗證
  - 授權
  - jwt
  - mtls
  - bearer 權杖
description: "StackQL 的選擇加入式呼叫端存取管理：以 JWT、bearer 權杖、相互 TLS 或本機 OS 身分驗證呼叫端，對應至目錄中的主體和群組，並以政策規則授權陳述式。"
image: "/img/stackql-featured-image.png"
---

呼叫端存取管理會驗證 StackQL 工作階段的呼叫端，並依據政策授權每個陳述式。它與 Provider 憑證無關，Provider 憑證仍透過下列方式提供：`--auth`、環境變數或 [Provider 工作階段](/getting-started/provider-sessions)。此功能預設為停用，未啟用此功能的部署行為與先前相同。適用於 StackQL `v1.0` 及更新版本。

另請參閱：  
[[ 全域旗標 ]](/command-line-usage/global-flags) [[` mcp `]](/command-line-usage/mcp) [[` whoami `]](/language-spec/functions/session/whoami) [[` groups `]](/language-spec/functions/session/groups)

* * * 

## 啟用存取管理 {#enabling-access-management}

啟用存取管理的方式，是以指向 JSON 設定檔的 `--access.config` 全域旗標啟動 StackQL。此範例以受保護模式啟動互動式 shell；`exec` 和 `mcp` 也以相同方式啟用。

```bash
stackql --access.config '{"path": "/etc/stackql/access.json"}' shell
```

`--access.config` 接受只含單一 `path` 鍵的 JSON 封套；也接受單純的檔案路徑。設定只在啟動時載入一次，因此請透過受控制的重新啟動來輪替金鑰或政策。

啟用存取管理時，只有 `exec`、`shell`、`mcp` 和 `version` 可以執行；其他命令（包括 `registry`）會拒絕執行，而不會繞過受保護的路徑。`srv` 和 MCP `reverse_proxy` 伺服器類型也會拒絕執行，因為 PostgreSQL 線路協定的相依套件尚未公開每個連線的驗證證據。SQL `AUTH` 和 `AUTH REVOKE` 陳述式在受保護模式下會拒絕執行；請改為透過主機設定佈建 Provider 憑證。受保護的 `exec` 失敗時，會在顯示錯誤後傳回結束狀態 `1`。

## 驗證模式 {#authentication-modes}

設定檔會選取一種 `type`：`jwt`（省略時的預設值）、`bearer`、`mtls` 或 `local_os`。設定採用嚴格的 JSON：未知的欄位以及屬於其他模式的欄位都會遭拒。所有模式共用相同的 `rules` 和相同的[主體目錄](#principal-directory)。

### JWT {#jwt}

呼叫端出示已簽署的權杖，系統會依據設定中固定的金鑰離線驗證該權杖。最小設定會指定權杖必須帶有的簽發者和受眾，列出一個公開金鑰，並讓 `analysts` 群組的成員執行讀取陳述式（`readers` 群組的成員不需要規則即可執行，請參閱[政策規則](#policy-rules)）：

```json
{
  "type": "jwt",
  "issuer": "https://identity.example",
  "audience": "stackql",
  "keys": [
    {"kid": "primary", "algorithm": "EdDSA", "public_key_pem": "-----BEGIN PUBLIC KEY-----\n...\n-----END PUBLIC KEY-----\n"}
  ],
  "rules": [
    {"subject": "group:analysts", "resource": "host/sql/read", "action": "execute"}
  ]
}
```

簽發者、受眾和公開金鑰都固定在設定中；不使用 OIDC 探索。接受的演算法為 `EdDSA`、`RS256`、`RS384`、`RS512`、`PS256`、`PS384`、`PS512`、`ES256`、`ES384` 和 `ES512`，且 RSA 金鑰必須至少為 2048 位元。每個金鑰都繫結至其設定的演算法。金鑰 ID 未知、簽章無效、缺少到期時間或已過期、`nbf` 或 `iat` 為未來時間，或簽發者或受眾錯誤的權杖都會遭拒。權杖中攜帶的群組宣告和 URL 不會用於信任判斷或目錄成員資格。

### 靜態 bearer 權杖 {#static-bearer-token}

呼叫端出示不透明權杖，設定中則列出每個可接受權杖的 SHA-256 摘要，以及該權杖所識別的主旨。這適用於少量且固定的服務身分，也就是不值得為此執行權杖簽發者的情況：

```json
{
  "type": "bearer",
  "issuer": "local-tokens",
  "tokens": [{"subject": "alice", "sha256": "<64 hexadecimal characters: the SHA-256 of the token>"}],
  "rules": [{"subject": "group:analysts", "resource": "host/sql/read", "action": "execute"}]
}
```

設定中只包含每個權杖的 SHA-256 摘要，且摘要必須是唯一的。變更設定的摘要需要重新啟動。

### 相互 TLS {#mutual-tls}

呼叫端由其在 HTTPS MCP 傳輸方式上出示的用戶端憑證來識別。設定中包含必須簽發該憑證的 CA，並指定由哪一種主體別名保存身分：

```json
{
  "type": "mtls",
  "issuer": "client-certificates",
  "client_ca_pem": "-----BEGIN CERTIFICATE-----\n...\n-----END CERTIFICATE-----\n",
  "certificate_identity": "uri_san",
  "rules": [{"subject": "group:analysts", "resource": "host/sql/read", "action": "execute"}]
}
```

用戶端憑證鏈會依據 `client_ca_pem` 進行驗證，身分主旨則取自恰好一個所設定種類的 SAN，即 `uri_san` 或 `dns_san`。不會退而使用 Common Name。相互 TLS 僅能透過 HTTPS MCP 傳輸方式使用。

### 本機 OS 身分 {#local-os-identity}

不交換任何憑證：主旨是執行 StackQL 處理程序的作業系統身分。設定只需要 `issuer` 標籤和規則：

```json
{
  "type": "local_os",
  "issuer": "this-host",
  "rules": [{"subject": "group:analysts", "resource": "host/sql/read", "action": "execute"}]
}
```

身分主旨是 StackQL 處理程序的有效 Unix UID 或 Windows SID，包括沒有 passwd 項目的數字容器 UID。不會出示任何憑證，且會忽略 `STACKQL_CALLER_TOKEN`。此模式僅限本機：HTTP 呼叫端會遭拒。作業系統群組成員資格不會授予存取權；只有目錄成員資格才會。

### 工作階段期限 {#session-deadline}

預設情況下，查詢沒有由驗證衍生的期限。每個新的陳述式都會驗證憑證並重新整理目錄成員資格，因此過期的憑證無法啟動新的工作，但到期不會中斷已經獲得授權的陳述式。設定 `"enforce_session_deadline": true` 可在 `max_session_seconds`（預設 `300`，上限 `86400`）時取消，此時間會因 JWT 或憑證到期而縮短。此設定不會更新權杖。

## 主體目錄 {#principal-directory}

身分會對應至主體，主體再對應至群組，這些對應存放在所設定 SQLite 或 PostgreSQL 後端的四個資料表中：

| 資料表 | 內容 |
|--|--|
| `stackql_access_principals` | 主體 ID 和字串屬性的 JSON 物件。`tenant` 屬性會為預覽 IaC 狀態劃分命名空間。 |
| `stackql_access_identities` | 已驗證的 `(issuer, subject)` 配對對應至主體 ID。 |
| `stackql_access_groups` | 群組 ID。 |
| `stackql_access_memberships` | 主體與群組的繫結。 |

在提供受保護的查詢之前，請透過受信任的資料庫管理連線佈建資料表、身分和成員資格：

```sql
INSERT INTO stackql_access_principals (id, attributes) VALUES ('alice', '{}');
INSERT INTO stackql_access_groups (id) VALUES ('readers');
INSERT INTO stackql_access_identities (issuer, subject, principal_id)
VALUES ('https://identity.example', 'alice', 'alice');
INSERT INTO stackql_access_memberships (principal_id, group_id) VALUES ('alice', 'readers');
```

請使用該模式實際的簽發者和主旨：對於本機 OS 身分，可能是 `('this-host', '1000')`；對於相互 TLS，則是 `('client-certificates', 'spiffe://example/alice')`。請明確選取後端：

```bash
stackql --access.config '{"path":"/etc/stackql/access.json"}' \
  --sqlBackend '{"dbEngine":"postgres_tcp","dsn":"postgres://runtime@localhost/stackql?sslmode=require","sqlDialect":"postgres"}' \
  exec 'SELECT whoami(), groups();'
```

目錄會為每個陳述式產生不可變的快照。撤銷的方式是在管理連線上執行 `DELETE FROM stackql_access_memberships`；下一個陳述式就會看到新的快照。未知的身分、缺少 schema、格式錯誤的資料和目錄中斷都會拒絕存取。一般受保護的 SQL 無法存取目錄資料表。請將成員資格的寫入保留在管理連線上、保護資料庫檔案，並在後端支援時，為管理和執行階段使用不同的資料庫權限。

## 政策規則 {#policy-rules}

每條規則會將一個 `resource` 上的一個 `action` 授予格式為 `group:<group id>` 的 `subject`。請使用受信任 Provider 文件中已編譯的方法和作業識別碼；作業萬用字元除了讀取之外，也會授予變更方法。

`readers` 群組隱含擁有主機的安全讀取 SQL 功能。該群組以外的主體進行讀取時需要 `host/sql/read`，其他陳述式則需要 `host/sql/<statement kind>` 上的 `execute`。Provider 作業和憑證的使用一律需要明確的規則，且對集合的授權不會授予其底層方法。其他主機檢查：

| 資源 | 動作 | 範圍 |
|--|--|--|
| `host/sql/read` | `execute` | 隱含讀取者群組以外的讀取陳述式 |
| `host/sql/exec` | `execute` | 原生 `EXEC`，包括巢狀 `EXEC` |
| `host/iac/run`, `host/iac/destroy` | `execute` | 預覽 IaC 關聯，即使它們使用 `SELECT` 語法 |
| `host/function/<name>` | `execute` | 不在已知純函式小型允許清單中的函式 |
| `host/backend` | `read` | 不透明的已儲存和外部後端關聯 |
| `host/credentials` | `reload` | MCP `reload_credentials` 工具 |
| `session/variables` | `read` | 解除參考[工作階段變數](/getting-started/session-variables)和祕密 |
| `session/config` | `write` | `SET` 和 `UNSET` |

已儲存查詢的展開會在執行前進行檢查，且受保護的路徑會停用計畫和資料列快取的重複使用。

## 進入點 {#entry-points}

| 驗證 | `exec`, `shell`, MCP stdio | HTTPS MCP |
|--|--|--|
| JWT | `STACKQL_CALLER_TOKEN` 環境變數 | `Authorization: Bearer` 要求標頭 |
| 靜態 bearer | `STACKQL_CALLER_TOKEN` 環境變數 | `Authorization: Bearer` 要求標頭 |
| 本機 OS | 有效的處理程序 UID 或 SID；無憑證 | 拒絕 |
| 相互 TLS | 拒絕：沒有已驗證的 TLS 連線 | 已驗證的用戶端憑證鏈和一個設定的 SAN |

請透過處理程序環境提供本機權杖憑證，切勿以 SQL 常值或命令列引數提供。透過 HTTPS 時，呼叫端驗證器會取代 [HTTP 用戶端驗證](/command-line-usage/mcp#http-client-authentication) 中所述的共用 `auth_token_env_var` bearer 管控；伺服器 TLS 憑證和金鑰的設定是必要的，且沒有憑證的要求絕不會退而使用伺服器的環境權杖。

## 檢查呼叫端 {#inspecting-the-caller}

在已驗證的工作階段中，`whoami()` 會傳回主體 ID，`groups()` 則以 JSON 陣列傳回目錄群組：

```sql
SELECT whoami() AS principal, groups() AS memberships;
```

```
|-----------|-------------|
| principal | memberships |
|-----------|-------------|
| alice     | ["readers"] |
|-----------|-------------|
```

這兩個函式都需要以 `--access.config` 啟動的工作階段。

## 逐步解說 {#walkthroughs}

StackQL 存放庫隨附[四個可執行的逐步解說](https://github.com/stackql/stackql/blob/main/docs/examples/access-management/README.md)，每種驗證模式各一個，由 Python 指令碼驅動，該指令碼會佈建示範目錄並驗證一個查詢。這些逐步解說不需要雲端帳戶，並由專案的測試套件執行。
