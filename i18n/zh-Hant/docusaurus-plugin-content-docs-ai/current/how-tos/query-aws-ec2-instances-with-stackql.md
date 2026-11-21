---
title: 如何使用 StackQL 查詢 AWS EC2 執行個體
description: 將 EC2 執行個體當作 SQL 資料表 aws.ec2.instances 查詢：依區域篩選、選取執行個體類型與 IP 位址等屬性，並以 GROUP BY 跨區域彙總。
keywords: [stackql, aws, ec2, 查詢執行個體, sql, 雲端資源清單, 多區域]
proficiencyLevel: Beginner
faq:
  - question: 為什麼查詢 EC2 執行個體時必須提供區域？
    answer: EC2 DescribeInstances API 是區域性的，因此 region 是底層呼叫的必要參數。StackQL 會將必要的 API 參數呈現為必要的 WHERE 子句述詞。以 region IN (...) 提供多個區域時，會扇出為各區域平行的 API 呼叫，並彙總為單一結果集。
  - question: aws.ec2.instances 有哪些可用的資料行？
    answer: 資料行包括 instance_id、instance_type、image_id、launch_time、private_ip_address、public_ip_address、private_dns_name、public_dns_name、subnet_id、vpc_id、key_name，以及 state、placement、security_groups、network_interfaces 與 tags 等結構化資料行。執行 DESCRIBE aws.ec2.instances 可查看完整清單。
  - question: 可以使用 StackQL 停止、啟動或終止執行個體嗎？
    answer: 可以。終止對應到 DELETE，而生命週期動作（start_instances、stop_instances、reboot_instances、monitor_instances）則是同一資源上的 EXEC 方法。SHOW METHODS IN aws.ec2.instances 會列出每項作業、其 SQL 動詞及必要參數。
---

# 如何使用 StackQL 查詢 AWS EC2 執行個體

EC2 執行個體以資料表 `aws.ec2.instances` 的形式公開。EC2 `DescribeInstances` API 傳回的任何欄位都是可查詢的資料行，而由於該 API 是區域性的，`region` 是必要的 `WHERE` 述詞。

## 必要條件 {#prerequisites}

- 已將 AWS 憑證匯出為環境變數，請參閱[如何讓 StackQL 向 AWS 進行驗證](/ai/how-tos/authenticate-stackql-to-aws)
- 已安裝 AWS Provider：`REGISTRY PULL aws;`

## 基本查詢 {#basic-query}

列出單一區域中的執行個體及其類型與位址資訊：

```sql
SELECT
  instance_id,
  instance_type,
  private_ip_address,
  public_ip_address,
  launch_time
FROM aws.ec2.instances
WHERE region = 'us-east-1';
```

## 先探索 schema {#discover-the-schema-first}

兩個中繼資料命令讓資源能夠自我描述，不需要查閱 AWS 文件：

```sql
DESCRIBE aws.ec2.instances;
SHOW METHODS IN aws.ec2.instances;
```

`DESCRIBE` 會列出可用的資料行。`SHOW METHODS` 會列出各項作業（`describe_instances` SELECT 方法、`terminate_instances` DELETE 方法，以及 `start_instances`、`stop_instances`、`reboot_instances` 等 EXEC 生命週期方法）及每項作業的必要參數。這些命令無需憑證即可運作。

## 多區域彙總 {#multi-region-aggregation}

`region IN (...)` 述詞會為每個區域平行執行一次 API 呼叫並彙總結果，讓區域性服務能像全域資料表一樣查詢：

```sql
SELECT region, instance_type, COUNT(*) as num_instances
FROM aws.ec2.instances
WHERE region IN ('us-east-1','us-west-2','eu-west-1','ap-southeast-2')
GROUP BY region, instance_type;
```

此模式是全帳戶資源清單與漂移基準的基礎：結果是即時的答案，而非快照。

## 依屬性篩選 {#filtering-on-attributes}

標準 SQL 述詞適用於任何資料行。例如，暴露於網際網路的執行個體：

```sql
SELECT instance_id, instance_type, public_ip_address
FROM aws.ec2.instances
WHERE region = 'us-east-1'
AND public_ip_address IS NOT NULL;
```

`state` 和 `tags` 等結構化資料行會傳回 JSON，可使用所設定 SQL 後端的 JSON 函式加以拆解。

## 相關概念 {#related-concepts}

- [如何讓 StackQL 向 AWS 進行驗證](/ai/how-tos/authenticate-stackql-to-aws)：憑證設定
- [如何使用 StackQL 查詢 S3 儲存貯體](/ai/how-tos/query-s3-buckets-with-stackql)：S3 的模式，兩者略有不同
- [什麼是基礎架構漂移？](/ai/canonical-definitions/what-is-infrastructure-drift)：將這些查詢用作基準
- [什麼是可查詢基礎架構？](/ai/canonical-definitions/what-is-queryable-infrastructure)：資料表抽象背後的模型
- [開始使用 StackQL](/ai/tutorials/getting-started-with-stackql)：端對端設定
