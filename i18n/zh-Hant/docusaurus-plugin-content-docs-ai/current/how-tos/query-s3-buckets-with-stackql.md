---
title: 如何使用 StackQL 查詢 S3 儲存貯體
description: 以 aws.s3.buckets 資料表列出 S3 儲存貯體及其區域，並從以 Identifier 為鍵的 awscc.s3.buckets Cloud Control 資料表取得加密、版本控制、公開存取封鎖等設定。
keywords: [stackql, aws, s3, 儲存貯體, sql, 雲端安全, 公開存取]
proficiencyLevel: Beginner
faq:
  - question: 儲存貯體是全域的，為什麼 S3 儲存貯體查詢仍使用 region = 'us-east-1'？
    answer: 儲存貯體名稱在全域範圍內是唯一的，但區域仍是清單方法的必要路由參數；us-east-1 會傳回帳戶的儲存貯體清單。每個儲存貯體的所在區域是該清單的 bucket_region 資料行。
  - question: 如何取得特定儲存貯體的設定？
    answer: 查詢 Cloud Control 資料表 awscc.s3.buckets，並以儲存貯體名稱作為 Identifier。該資料列包含加密、版本控制、公開存取封鎖、記錄和標籤等設定。
  - question: StackQL 可以建立或刪除 S3 儲存貯體嗎？
    answer: 可以。aws.s3.buckets 公開了 INSERT（create_bucket）和 DELETE（delete_bucket）方法，Cloud Control 資料表 awscc.s3.buckets 則公開了 INSERT（create_resource）、UPDATE（update_resource）和 DELETE（delete_resource）。SHOW INSERT INTO awscc.s3.buckets 會產生可寫入屬性的範本。
---

# 如何使用 StackQL 查詢 S3 儲存貯體

S3 儲存貯體以兩個相關的資料表公開：`aws.s3.buckets` 列出帳戶中的儲存貯體，`awscc.s3.buckets` 則傳回由 `Identifier` 識別之單一儲存貯體的完整設定。前者以 `aws` Provider 中的 S3 API 為基礎，後者以 `awscc` Provider 中的 AWS Cloud Control API 為基礎。

## 必要條件 {#prerequisites}

- 已匯出 AWS 憑證，請參閱[如何讓 StackQL 向 AWS 進行驗證](/ai/how-tos/authenticate-stackql-to-aws)
- 已安裝兩個 AWS Provider：`REGISTRY PULL aws;` 和 `REGISTRY PULL awscc;`

## 列出儲存貯體 {#list-buckets}

```sql
SELECT name, bucket_region, creation_date
FROM aws.s3.buckets
WHERE region = 'us-east-1';
```

`region` 述詞是底層 API 呼叫的必要路由參數；`us-east-1` 會傳回帳戶的儲存貯體。

## 檢查單一儲存貯體 {#inspect-one-bucket}

Cloud Control 資料表會傳回指定儲存貯體的設定屬性：

```sql
SELECT
  bucket_name,
  bucket_encryption,
  versioning_configuration,
  public_access_block_configuration
FROM awscc.s3.buckets
WHERE region = 'us-east-1'
AND Identifier = 'my-bucket-name';
```

設定資料行會傳回結構化 JSON；`DESCRIBE awscc.s3.buckets` 會列出所有可用的資料行，包括 `logging_configuration`、`lifecycle_configuration`、`object_lock_configuration`、`tags` 和 `arn`。

## 稽核模式：先列舉，再檢查 {#audit-pattern-enumerate-then-inspect}

整個帳戶的設定稽核會結合這兩個資料表：先列出儲存貯體名稱，再針對每個儲存貯體查詢詳細資料表。在指令碼中使用時（shell 迴圈，或透過 pystackql 使用 Python），逐一處理清單結果並收集詳細資料列；「是否已啟用版本控制？」或「公開存取封鎖是否完整？」等個別儲存貯體的檢查，就成為對收集結果套用的資料列述詞。這是 StackQL AWS 教學中記載的 S3 資源清單模式。

## 建立儲存貯體 {#creating-buckets}

可寫入屬性的探索方式與可讀取屬性相同：

```sql
SHOW INSERT INTO awscc.s3.buckets;
```

這會產生涵蓋資源可寫入欄位的 `INSERT` 範本，形式為 Cloud Control 所接受的期望狀態文件。若只要包含必要欄位，請使用 `/*+ REQUIRED */` 查詢提示。

## 相關概念 {#related-concepts}

- [如何使用 StackQL 查詢 AWS EC2 執行個體](/ai/how-tos/query-aws-ec2-instances-with-stackql)：區域服務模式
- [如何讓 StackQL 向 AWS 進行驗證](/ai/how-tos/authenticate-stackql-to-aws)：憑證設定
- [什麼是 SQL for APIs？](/ai/canonical-definitions/what-is-sql-for-apis)：為什麼清單與詳細資料是不同的方法
- [什麼是可查詢基礎架構？](/ai/canonical-definitions/what-is-queryable-infrastructure)：稽核使用案例的背景
- [常見 StackQL 錯誤](/ai/troubleshooting/common-stackql-errors)：缺少必要參數的說明
