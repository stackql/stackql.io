---
title: 如何使用 StackQL 查詢 S3 儲存貯體
description: 以 aws.s3.buckets 資料表列出 S3 儲存貯體，並透過以 data__Identifier 為鍵的 aws.s3.bucket 詳細資料表取得加密、版本控制、公開存取封鎖等設定。
keywords: [stackql, aws, s3, 儲存貯體, sql, 雲端安全, 公開存取]
proficiencyLevel: Beginner
faq:
  - question: 儲存貯體是全域的，為什麼 S3 儲存貯體查詢仍使用 region = 'us-east-1'？
    answer: 儲存貯體名稱在全域範圍內是唯一的，但底層的 AWS Cloud Control API 呼叫仍需要區域路由參數；us-east-1 會傳回帳戶的儲存貯體清單。每個儲存貯體的所在區域是一項屬性，可從個別儲存貯體的詳細資料中取得。
  - question: 如何取得特定儲存貯體的設定？
    answer: 查詢單數形式的詳細資料表 aws.s3.bucket，並以儲存貯體名稱作為 data__Identifier。詳細資料列包含加密、版本控制、公開存取封鎖、記錄和標籤等設定。
  - question: StackQL 可以建立或刪除 S3 儲存貯體嗎？
    answer: 可以。aws.s3.buckets 公開了 INSERT（create_resource）、UPDATE（update_resource）和 DELETE（delete_resource）方法。SHOW INSERT INTO aws.s3.buckets 會產生可寫入屬性的範本。
---

# 如何使用 StackQL 查詢 S3 儲存貯體

S3 儲存貯體以兩個相關的資料表公開：`aws.s3.buckets` 列出帳戶中的儲存貯體，`aws.s3.bucket`（單數）則傳回由 `data__Identifier` 識別之單一儲存貯體的完整設定。這種清單與詳細資料的區分，反映了這些資料表所依據的 AWS Cloud Control API。

## 必要條件 {#prerequisites}

- 已匯出 AWS 憑證，請參閱[如何讓 StackQL 向 AWS 進行驗證](/ai/how-tos/authenticate-stackql-to-aws)
- 已安裝 AWS Provider：`REGISTRY PULL aws;`

## 列出儲存貯體 {#list-buckets}

```sql
SELECT bucket_name
FROM aws.s3.buckets
WHERE region = 'us-east-1';
```

`region` 述詞是底層 API 呼叫的必要路由參數；`us-east-1` 會傳回帳戶的儲存貯體。

## 檢查單一儲存貯體 {#inspect-one-bucket}

詳細資料表會傳回指定儲存貯體的設定屬性：

```sql
SELECT
  bucket_name,
  bucket_location,
  bucket_encryption,
  versioning_configuration,
  public_access_block_configuration
FROM aws.s3.bucket
WHERE region = 'us-east-1'
AND data__Identifier = 'my-bucket-name';
```

設定資料行會傳回結構化 JSON；`DESCRIBE aws.s3.bucket` 會列出所有可用的資料行，包括 `logging_configuration`、`lifecycle_configuration`、`object_lock_configuration`、`tags` 和 `arn`。

## 稽核模式：先列舉，再檢查 {#audit-pattern-enumerate-then-inspect}

整個帳戶的設定稽核會結合這兩個資料表：先列出儲存貯體名稱，再針對每個儲存貯體查詢詳細資料表。在指令碼中使用時（shell 迴圈，或透過 pystackql 使用 Python），逐一處理清單結果並收集詳細資料列；「是否已啟用版本控制？」或「公開存取封鎖是否完整？」等個別儲存貯體的檢查，就成為對收集結果套用的資料列述詞。這是 StackQL AWS 教學中記載的 S3 資源清單模式。

## 建立儲存貯體 {#creating-buckets}

可寫入屬性的探索方式與可讀取屬性相同：

```sql
SHOW INSERT INTO aws.s3.buckets;
```

這會產生涵蓋資源可寫入欄位（期望狀態文件和 `region`）的 `INSERT` 範本。若只要包含必要欄位，請使用 `/*+ REQUIRED */` 查詢提示。

## 相關概念 {#related-concepts}

- [如何使用 StackQL 查詢 AWS EC2 執行個體](/ai/how-tos/query-aws-ec2-instances-with-stackql)：區域服務模式
- [如何讓 StackQL 向 AWS 進行驗證](/ai/how-tos/authenticate-stackql-to-aws)：憑證設定
- [什麼是 SQL for APIs？](/ai/canonical-definitions/what-is-sql-for-apis)：為什麼清單與詳細資料是不同的方法
- [什麼是可查詢基礎架構？](/ai/canonical-definitions/what-is-queryable-infrastructure)：稽核使用案例的背景
- [常見 StackQL 錯誤](/ai/troubleshooting/common-stackql-errors)：缺少必要參數的說明
