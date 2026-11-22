---
title: UNSET
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - 工作階段變數
  - unset
  - Provider 工作階段
  - 祕密
description: "StackQL 中的 UNSET：移除工作階段變數，或清除目前工作階段中以 SET 設定的 Provider 別名、憑證欄位與祕密。"
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

移除工作階段變數，或清除目前工作階段中以 [`SET`](/language-spec/set) 設定的執行階段設定。StackQL `v1.0` 及更新版本提供此功能。

另請參閱：  
[[` SET `]](/language-spec/set) [[ 工作階段變數 ]](/getting-started/session-variables) [[ Provider 工作階段 ]](/getting-started/provider-sessions)

* * * 

## 語法 {#syntax}

*unsetStatement::=*

<RailroadDiagram
type="unset"
/>

&nbsp;  
&nbsp;  

```sql
UNSET [ SESSION ] <target> [, <target> ...];
```

__*target*__  
`SET` 接受的任何目標：一般或以雙引號括住的變數名稱，或以雙引號括住且以 $ 開頭的設定路徑。

__*SESSION*__  
選用，且是唯一存在的範圍：`UNSET SESSION name` 與 `UNSET name` 是相同的陳述式，因為每個變數與設定都屬於目前工作階段。`UNSET GLOBAL` 會以 `configuration scope "global" is not supported; use SESSION` 遭到拒絕。

`UNSET name` 等同於 `SET name = DEFAULT`。同一個清單中的目標會依序移除，移除尚未設定的名稱不會產生錯誤。變數取消設定後再對其解除參考，會在發出任何 Provider 要求之前失敗。

| 目標 | 效果 |
|--|--|
| `name` | 移除工作階段變數。 |
| `"$.auth.<provider>.<field>"` | 從 Provider 的工作階段驗證內容中刪除該欄位。覆寫之前生效的值不會還原。 |
| `"$.providers.<alias>"` | 移除 Provider 別名繫結。 |
| `"$.secrets.<name>"` | 從工作階段的存放區刪除祕密。若為持續性的作業系統存放區，則會刪除已儲存的值。 |
| `"$.secret_store"` | 捨棄目前的存放區繫結，並以空的工作階段記憶體重新開始。已保存在作業系統存放區中的值不會被刪除。 |

* * *

## 範例 {#examples}

### 移除變數 {#remove-a-variable}

變數會從工作階段中移除，因此註解掉的查詢會以 `variable "$region" is not defined` 失敗，而不會以過時或空的值執行。若要重複使用該名稱，請重新設定。

```sql
SET region = 'ap-southeast-2';
UNSET region;

/*
SELECT name FROM aws.s3.buckets WHERE region = $region;
-- FAILS with variable "$region" is not defined
*/
```

### 移除多個目標 {#remove-several-targets}

一個陳述式可以同時移除變數與設定路徑，且目標會依序處理。此處在一個清單中移除一般變數、名稱含點號的變數，以及一項憑證覆寫。

```sql
UNSET region, "providers.saved", "$.auth.github.password_var";
```

### 明確指定工作階段範圍 {#make-the-session-scope-explicit}

這兩個陳述式的作用相同：`SESSION` 指的是唯一存在的範圍。明確的寫法適合也會在區分工作階段狀態與全域狀態的資料庫上執行的指令碼。註解掉的陳述式會失敗，因為不存在共用狀態。

```sql
UNSET region;
UNSET SESSION region;

/*
UNSET GLOBAL region;
-- FAILS with configuration scope "global" is not supported; use SESSION
*/
```

### 清除憑證覆寫 {#clear-a-credential-override}

先前以 `SET "$.auth.github.password_var"` 設定的欄位，會從 GitHub Provider 的工作階段驗證內容中刪除，該內容的其他欄位則不受影響。覆寫之前生效的值不會還原，因此如果 Provider 仍需要該欄位，請重新設定。

```sql
UNSET "$.auth.github.password_var";
```

### 移除 Provider 別名及其祕密 {#remove-a-provider-alias-and-its-secret}

別名 `production` 是以 `SET "$.providers.production"` 繫結的，並透過 `auth_secret` 參考儲存在 `$.secrets.production` 的祕密。在一個陳述式中同時移除兩者，會捨棄該繫結（因此 `production.s3.buckets` 不再是有效的關聯），並從工作階段存放區刪除該祕密。`SESSION` 在此處與其他地方一樣是選用的。

```sql
UNSET SESSION "$.providers.production", "$.secrets.production";
```
