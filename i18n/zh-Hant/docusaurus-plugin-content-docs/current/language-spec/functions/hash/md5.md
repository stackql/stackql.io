---
title: MD5
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - md5
  - 雜湊
  - 摘要
description: "StackQL 中的 MD5 函式：傳回字串的小寫十六進位 MD5 摘要，用於指紋識別與變更偵測，而非安全性用途。"
image: "/img/stackql-featured-image.png"
---
以 32 個小寫十六進位字元傳回字串的 MD5 摘要。適用於 StackQL `v1.0` 及更新版本。

另請參閱：  
[[` SELECT `]](/language-spec/select) [[ 串連 ]](/language-spec/functions/string/concatenation)

* * * 

## 語法 {#syntax}

```sql
SELECT MD5(expression) FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*expression*__  
字串，或強制轉換為文字的值。`NULL` 引數會傳回 `NULL`。

## 傳回值 {#return-values}

傳回 32 個字元的小寫十六進位字串。

> `MD5` 用於指紋識別與變更偵測。它不是密碼或安全性的基本元件。在 PostgreSQL 後端上，會使用資料庫本身的 `md5` 函式。

* * *

## 範例 {#examples}

### 為衍生值產生指紋 {#fingerprint-a-derived-value}

變數會以具型別的值代入，整數會轉換為文字並與尾碼串連，再對轉為小寫的結果計算摘要。相同的輸入一律產生相同的 32 字元十六進位字串，因此該值可作為穩定的鍵。

```sql
SET some_id = 42;
SET suffix = 'East';

SELECT md5(lower(CONVERT($some_id, TEXT) || '-' || $suffix)) AS digest;
```

### 偵測設定變更 {#detect-configuration-changes}

對序列化的 JSON 資料行計算雜湊，可得到一個簡短的值，用於儲存並在每次執行之間比較。摘要與上次執行不同的儲存貯體，其 IAM 設定已經變更，而不必儲存整份文件或比較其差異。

```sql
SELECT name, md5(json_extract(iamConfiguration, '$')) AS iam_digest
FROM google.storage.buckets
WHERE project = 'stackql-demo';
```
