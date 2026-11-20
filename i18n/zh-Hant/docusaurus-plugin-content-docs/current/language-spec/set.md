---
title: SET
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - 工作階段變數
  - set
  - Provider 工作階段
  - 祕密
description: "StackQL 中的 SET：指派具型別的工作階段變數，並透過以 $ 開頭的目標，為目前工作階段設定 Provider 別名、憑證欄位與祕密。"
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

指派工作階段變數，或設定目前工作階段的執行階段設定值。StackQL `v1.0` 及更新版本提供此功能。

另請參閱：  
[[` UNSET `]](/language-spec/unset) [[ 工作階段變數 ]](/getting-started/session-variables) [[ Provider 工作階段 ]](/getting-started/provider-sessions) [[` IDENTIFIER `]](/language-spec/functions/general/identifier)

* * * 

## 語法 {#syntax}

*setStatement::=*

<RailroadDiagram
type="set"
/>

*setExpression::=*

<RailroadDiagram
type="setExpression"
/>

&nbsp;  
&nbsp;  

```sql
SET [ SESSION ] <target> = { <literal> | DEFAULT | <reference> } [, <target> = { <literal> | DEFAULT | <reference> } ...];
```

__*target*__  
一般識別碼（`region`）、以雙引號括住的名稱（`"providers.saved"`），或以雙引號括住且以 $ 開頭的設定路徑（`"$.auth.aws.password"`）。

__*literal*__  
字串、數字、布林值或 `null`。字串以單引號括住。陣列與物件不是純量值；請將 JSON 文件以字串形式保存。

__*DEFAULT*__  
重設目標，等同於 [`UNSET`](/language-spec/unset)。明確指定的 `null` 是會被儲存的值，而非重設。

__*reference*__  
以雙引號括住、參考現有變數的 `"$name"`，例如 `SET "$.providers.production" = "$providers.saved";`。

__*SESSION*__  
選用，且是 StackQL 唯一的範圍。`SET SESSION name = ...` 與 `SET name = ...` 是相同的陳述式：每項指派都屬於目前工作階段，不屬於其他任何範圍。此關鍵字的存在，是為了讓指令碼的寫法與在區分 `SET SESSION` 和 `SET GLOBAL` 的資料庫上相同。`SET GLOBAL` 可以剖析，但會以 `configuration scope "global" is not supported; use SESSION` 遭到拒絕，因為不存在跨工作階段共用的狀態。

同一個 `SET` 清單中的指派會依序套用，且不構成交易：如果清單中的第二項指派失敗，第一項指派仍然有效。

## 目標 {#targets}

| 目標 | 效果 |
|--|--|
| `name` 或 `"name"` | 儲存純量工作階段變數，在之後的陳述式中以 `$name` 解除參考。單靠點號不會選取設定：`"providers.saved"` 是名為 `providers.saved` 的變數。較舊的 `"variables.name"` 寫法是 `name` 的別名。 |
| `"$.auth.<provider>.<field>"` | 為此工作階段設定該 Provider 驗證內容中的一個欄位。欄位是該 Provider 的 [`--auth`](/command-line-usage/global-flags) 物件的鍵，例如 `credentialsenvvar`、`password` 或 `token_url`。 |
| `"$.providers.<alias>"` | 繫結 Provider 別名，該別名具有自己的憑證、租用標籤、參數預設值與調校設定。值是字串中的 JSON 文件，請參閱 [Provider 工作階段](/getting-started/provider-sessions#provider-aliases)。 |
| `"$.secrets.<name>"` | 將祕密儲存至工作階段的祕密存放區。值是常值字串，通常是 JSON `auth` 文件，由 Provider 別名以 `auth_secret` 參考。 |
| `"$.secret_store"` | 選取持續性的作業系統祕密存放區，`{"backend":"os","namespace":"..."}`。需要維運人員旗標 `--allow-persistent-secrets`。 |

每種形式都僅影響目前工作階段。連線至 [`srv`](/command-line-usage/srv) 伺服器的各個獨立連線看不到彼此的指派，而每個 MCP 要求都是獨立的工作階段，因此使用 MCP 伺服器時，請在同一個陳述式批次中完成設定與查詢。

* * *

## 範例 {#examples}

### 設定工作階段變數 {#set-session-variables}

每個陳述式都會以指定的名稱儲存一個純量，且值會保留其型別：`region` 是字串，`minimum_count` 是整數，`enabled` 是布林值。`null` 會被當作值儲存，因此之後 `$optional_value` 會解析為 SQL `NULL`，而不會因未定義而失敗。最後一個陳述式在一個清單中指派兩個變數。

```sql
SET region = 'ap-southeast-2';
SET minimum_count = 3;
SET enabled = true;
SET optional_value = null;
SET suffix = 'East', some_id = 42;
```

### 明確指定工作階段範圍 {#make-the-session-scope-explicit}

前兩個陳述式完全相同：`SESSION` 指的是唯一存在的範圍，兩項指派都會持續到變數被取消設定或工作階段結束為止。若希望指令碼的寫法與在區分工作階段設定和全域設定的資料庫上相同，請使用此關鍵字。註解掉的陳述式僅供對照：它不會指派任何值，並以 `configuration scope "global" is not supported; use SESSION` 失敗，因為工作階段之間不共用任何狀態。

```sql
SET region = 'ap-southeast-2';
SET SESSION region = 'ap-southeast-2';

/*
SET GLOBAL region = 'ap-southeast-2';
-- FAILS with configuration scope "global" is not supported; use SESSION
*/
```

### 在查詢中使用變數 {#use-a-variable-in-a-query}

在任何運算式位置使用不加引號的 `$name` 解除參考變數。值會保留其型別：字串會逸出為 SQL 常值，數字、布林值與 `null` 則直接傳遞。`WHERE` 子句中的變數會在規劃要求之前解析，因此會像常值一樣下推至 Provider。

```sql
SET project = 'stackql-demo';
SET zone = 'australia-southeast1-a';

SELECT name, status FROM google.compute.instances
WHERE project = $project AND zone = $zone;
```

### 重設變數 {#reset-a-variable}

`DEFAULT` 會移除變數，與 [`UNSET`](/language-spec/unset) 的行為完全相同，因此在重新設定之前，之後的 `$region` 會以 `variable "$region" is not defined` 失敗；註解掉的查詢示範了這個失敗。這與指派 `null` 不同，後者會保留變數，其值為 `NULL`。

```sql
SET region = 'ap-southeast-2';
SET region = DEFAULT;

/*
SELECT name FROM aws.s3.buckets WHERE region = $region;
-- FAILS with variable "$region" is not defined
*/
```

### 從另一個變數複製值 {#copy-a-value-from-another-variable}

右側可以是以雙引號括住的現有變數參考，這會複製該變數當下保存的值。此處先將 Provider 繫結儲存為名為 `providers.saved` 的一般變數（點號不會使它成為設定），再將它複製到 `$.providers.production` 路徑以啟用。之後移除儲存的副本不會影響作用中的繫結。

```sql
SET "providers.saved" = '{"provider":"aws","tenancy":"production-account","auth":{"type":"aws_signing_v4","access_key_id_env_var":"PRODUCTION_AWS_ACCESS_KEY_ID","secret_access_key_env_var":"PRODUCTION_AWS_SECRET_ACCESS_KEY"}}';
SET "$.providers.production" = "$providers.saved";
UNSET "providers.saved";
```

### 為此工作階段覆寫憑證欄位 {#override-a-credential-field-for-this-session}

Provider 的驗證內容，是啟動時透過 [`--auth`](/command-line-usage/global-flags) 為其提供的物件。`$.auth` 下的路徑會在工作階段的剩餘期間取代該物件的一個欄位，其他欄位則維持原狀。此處將 GitHub Provider 指向另一個保存唯讀權杖的環境變數；該變數會從 StackQL 處理程序的環境中讀取，之後針對 `github.*` 的查詢都會使用它，直到該欄位被取消設定或工作階段結束為止。

```sql
SET "$.auth.github.credentialsenvvar" = 'GITHUB_TOKEN_READONLY';
```

### 繫結 Provider 別名 {#bind-a-provider-alias}

Provider 別名是 Provider 的第二個獨立設定的執行個體，在查詢中用來取代 Provider 名稱。值是字串中的 JSON 文件：`provider` 指定底層 Provider，`tenancy` 標示帳戶，`auth` 是與 `--auth` 結構相同的憑證區塊，`params` 則為 `region` 等要求參數提供預設值。之後針對 `production.s3.buckets` 的查詢會以這些憑證執行，而 `aws.s3.buckets` 仍保留啟動時的設定。完整的鍵清單請參閱 [Provider 工作階段](/getting-started/provider-sessions#provider-aliases)。

```sql
SET "$.providers.production" = '{
  "provider": "aws",
  "tenancy": "production-account",
  "auth": {
    "type": "aws_signing_v4",
    "access_key_id_env_var": "PRODUCTION_AWS_ACCESS_KEY_ID",
    "secret_access_key_env_var": "PRODUCTION_AWS_SECRET_ACCESS_KEY"
  },
  "params": {"region": "ap-southeast-2"}
}';

SELECT name FROM production.s3.buckets;
```
