---
title: Provider 工作階段與祕密
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - Provider 工作階段
  - 多帳戶
  - 憑證
  - 祕密
  - keychain
description: "StackQL 的 Provider 工作階段：為 Provider 別名繫結各自的憑證、租用標籤與參數預設值，依工作階段覆寫憑證欄位，並將祕密保存在工作階段記憶體或 OS 祕密存放區。"
image: "/img/stackql-featured-image.png"
---

另請參閱：  
[[` SET `]](/language-spec/set) [[` UNSET `]](/language-spec/unset) [[ 工作階段變數 ]](/getting-started/session-variables) [[ 全域旗標 ]](/command-line-usage/global-flags)

工作階段可以用別名繫結 Provider，並為其指定專屬的憑證、租用標籤、參數預設值與要求並行限制，因此可以在同一個工作階段中，甚至在同一個陳述式中，查詢同一個 Provider 的多個帳戶或租用戶。也可以針對工作階段覆寫憑證欄位，並將祕密保存在工作階段記憶體中，或在維運人員選擇加入後，保存在作業系統的祕密存放區中。以上所有設定都透過 [`SET`](/language-spec/set) 與 [`UNSET`](/language-spec/unset) 完成，適用於 StackQL `v1.0` 及更新版本。

這裡說明的所有內容都限定在目前的工作階段。連到 `stackql srv` 伺服器的各個獨立連線不會共用繫結或祕密，而且每個 MCP 要求都是獨立的工作階段，因此使用 MCP 伺服器時，請在同一個陳述式批次中完成設定與查詢。

## Provider 別名 {#provider-aliases}

`SET "$.providers.<alias>"` 會將別名繫結到 Provider。其值是存放在字串中的 JSON 文件：

| 鍵 | 必要 | 描述 |
|--|--|--|
| `provider` | 是 | 別名所解析成的標準 Provider，例如 `aws`。 |
| `tenancy` | 是 | 分組標籤，例如帳戶或租用戶名稱。StackQL 不會向 Provider 核對此標籤。 |
| `auth` | `auth` 與 `auth_secret` 擇一 | 採用別名格式的驗證文件，此格式與 Provider 在 [`--auth`](/command-line-usage/global-flags) 物件中的項目不同：以 AWS 為例，鍵為 `access_key_id_env_var` 和 `secret_access_key_env_var`，而不是 `keyIDenvvar` 和 `credentialsenvvar`；別名格式無法辨識的鍵會以 `invalid session configuration field or type` 失敗。別名不會沿用標準 Provider 的憑證。 |
| `auth_secret` | `auth` 與 `auth_secret` 擇一 | 保存驗證文件的[工作階段祕密](#session-secrets)名稱。 |
| `params` | 否 | 已宣告之 Provider 參數的預設值，包括已宣告的伺服器參數。查詢中的述詞會覆寫預設值。 |
| `tuning` | 否 | 此別名之要求的 `max_concurrent` 與 `max_per_host` 限制。Provider 與租用標籤都相同的別名會共用這些控制項，且必須宣告相同的 tuning 設定。 |
<br/>
以下範例以不同的別名繫結兩個 AWS 帳戶，各自從名稱不同的環境變數讀取金鑰，並在一個陳述式中同時查詢兩者：

```sql
SET "$.providers.production" = '{
  "provider": "aws",
  "tenancy": "production-account",
  "auth": {
    "type": "aws_signing_v4",
    "access_key_id_env_var": "PRODUCTION_AWS_ACCESS_KEY_ID",
    "secret_access_key_env_var": "PRODUCTION_AWS_SECRET_ACCESS_KEY"
  },
  "params": {"region": "ap-southeast-2"},
  "tuning": {"max_concurrent": 4, "max_per_host": 4}
}';

SET "$.providers.development" = '{
  "provider": "aws",
  "tenancy": "development-account",
  "auth": {
    "type": "aws_signing_v4",
    "access_key_id_env_var": "DEVELOPMENT_AWS_ACCESS_KEY_ID",
    "secret_access_key_env_var": "DEVELOPMENT_AWS_SECRET_ACCESS_KEY"
  },
  "params": {"region": "ap-southeast-2"},
  "tuning": {"max_concurrent": 2}
}';

SELECT name, 'production' AS account FROM production.s3.buckets
UNION ALL
SELECT name, 'development' AS account FROM development.s3.buckets;
```

環境變數參考會從 StackQL 處理程序的環境中解析；不存在僅限於工作階段的環境。暫時性 AWS 憑證還需要工作階段權杖欄位或其環境變數參考。

對中繼資料與資料陳述式而言，別名的行為與其 Provider 相同：`SHOW`、`DESCRIBE`、`SELECT`、`INSERT`、`UPDATE`、`DELETE` 與 `EXEC` 都接受以別名取代 Provider 名稱。別名不適用於原生內建關聯，也不適用於預覽版 IaC 關聯。使用不同別名的 `UNION ALL` 分支會依序執行。

每個查詢都會取得別名憑證的獨立快照。變更繫結會影響之後的查詢，不會影響已在執行中的查詢。`UNSET "$.providers.<alias>"` 會移除繫結。

## 覆寫憑證欄位 {#overriding-credential-fields}

`SET "$.auth.<provider>.<field>"` 會在工作階段中變更 Provider 驗證內容的一個欄位，欄位名稱即 Provider 之 `--auth` 項目中的鍵：

```sql
SET "$.auth.github.password_var" = 'GITHUB_TOKEN_READONLY';
SELECT name FROM github.repos.repos WHERE org = 'stackql';
UNSET "$.auth.github.password_var";
```

與別名相同，變更會套用到 `SET` 之後才開始的查詢。若查詢的 Provider 驗證已被取消設定，或其別名沒有可用的 `auth`，查詢會在送出任何要求之前失敗。

## 工作階段祕密 {#session-secrets}

根據預設，每個工作階段都有一個僅存在於記憶體中的祕密存放區，不需任何設定。`SET "$.secrets.<name>"` 會儲存一個常值字串，通常是 JSON 驗證文件，Provider 別名則以 `auth_secret` 參考它：

```sql
SET "$.secrets.production" = '{"type":"aws_signing_v4","access_key_id_env_var":"PRODUCTION_AWS_ACCESS_KEY_ID","secret_access_key_env_var":"PRODUCTION_AWS_SECRET_ACCESS_KEY"}';
SET "$.providers.production" = '{"provider":"aws","tenancy":"production-account","auth_secret":"production","params":{"region":"ap-southeast-2"}}';

SELECT name FROM production.s3.buckets;

UNSET "$.secrets.production";
```

輪替祕密會影響之後的查詢；刪除祕密則會讓之後的使用失敗。新的工作階段會取得全新的空白存放區，工作階段結束時會釋放該存放區。`UNSET "$.secret_store"` 會捨棄目前的存放區繫結，並以空白的工作階段記憶體重新開始。

一般的憑證使用，以及儲存變數或設定的 `SET` 陳述式，都不會將祕密寫入 SQL 後端。將敏感資料放入一般變數或資料表，是呼叫端的責任。

## 持續性 OS 祕密存放區 {#persistent-os-secret-stores}

持續性儲存預設為停用，只有維運人員以 `--allow-persistent-secrets` 啟動 StackQL 才能啟用；無法透過 SQL 授與。設定此旗標後，工作階段可以使用下列陳述式選取 OS 存放區：

```sql
SET "$.secret_store" = '{"backend": "os", "namespace": "stackql/provider-auth"}';
```

存放區是 macOS Keychain、Linux Secret Service（需搭配工作階段服務可正常運作的 `secret-tool`）或 Windows Credential Manager。只接受 `os` 後端：即使設定了旗標，平面檔案存放區、檔案路徑以及任何隱含的檔案後援都會遭到拒絕；OS 功能無法使用時會直接失敗，而不會改用檔案。

寫入 OS 存放區的值在 SQL 工作階段結束後仍會保留，必須以 `UNSET "$.secrets.<name>"` 明確刪除。重設存放區繫結不會刪除已保存的值。對存放區的存取取決於 StackQL 處理程序的作業系統身分；命名空間並不是個別使用者的存取控制，因此請勿將這項選擇加入的功能視為多使用者保管庫。

## SQL 中的祕密值 {#secret-values-in-sql}

除非維運人員以 `--allow-secret-dereference` 啟動 StackQL，否則祕密值絕不會被解除參考到 SQL 中。設定此旗標後，`"$secrets.production"`、`"$secrets.production.credentials"`、`"$auth.github.password"` 與 `"$providers.production.auth.credentials"` 等參考就能在運算式中解析：

```sql
SELECT upper("$secrets.production.credentials") AS credential;

/*
-- the same statement in a session started without --allow-secret-dereference
SELECT upper("$secrets.production.credentials") AS credential;
-- FAILS with secret variable dereferencing is disabled
*/
```

如註解掉的副本所示，未設定此旗標而啟動時，相同的陳述式會在任何求值之前遭到拒絕。設定此旗標即表示承認明確解除參考的值可能進入 SQL 暫存、結果、記錄以及保存的查詢成品。一般的 Provider 驗證不需要此旗標。

## 權限 {#permissions}

啟用[存取管理](/command-line-usage/access-management)時，讀取變數與祕密需要 `session/variables:read`，`SET` 或 `UNSET` 則需要 `session/config:write`。解析後的別名會在執行前以其標準 Provider 的身分進行授權。個別變數與個別祕密的擁有權及授與並未實作。
