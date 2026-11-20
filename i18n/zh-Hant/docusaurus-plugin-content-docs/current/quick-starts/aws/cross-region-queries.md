---
title: 跨區域查詢
hide_title: true
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
  - cspm
  - aws
  - amazon web services
description: "以單一 StackQL 陳述式跨多個區域查詢並彙總 AWS 資源，以區域清單與非同步執行取代逐區域迴圈。"
image: "/img/tutorials/aws/stackql-aws-provider-featured-image.png"
---

# 使用 StackQL 進行非同步跨區域查詢

在本指南中，我們將示範 StackQL 的一項強大功能：跨 AWS 多個區域查詢並彙總資源。這需要對不同端點發出大量 API 要求，而這些要求會以非同步方式執行。整個過程對使用者而言是無縫且透明的。

> StackQL 是一款強大的開發工具，可使用 SQL 語法查詢及部署雲端基礎架構與資源。

測試環境：<span class="tutorial_tested_on">內嵌式 SQL 後端</span> <span class="tutorial_tested_on">postgres SQL 後端</span> <span class="tutorial_tested_on">macos</span> <span class="tutorial_tested_on">linux</span> <span class="tutorial_tested_on">powershell</span>

## 了解 AWS 區域與資源 {#understanding-aws-regions-and-resources}

AWS 資源託管於全球多個位置。這些位置由區域與可用區域組成，讓使用者能將執行個體放置在不同位置，並將資料儲存在不同位置。

## AWS 區域的問題 {#the-problem-with-aws-regions}

使用 AWS 時，必須了解 AWS Management Console 提供的是以區域為範圍的資源檢視，使用者一次只能在特定區域內與資源互動及管理資源。同樣地，AWS Command Line Interface（CLI）命令與軟體開發套件（SDK）也以區域作為作業的進入點，因此必須指定區域才能執行命令或執行程式。

> 當資源橫跨多個區域時，雲端服務的區域架構會帶來複雜性。此外，資安入侵事件（包括加密貨幣挖礦攻擊）經常利用各個區域的資源，範圍超出組織通常使用的主要區域。

## 在 StackQL 查詢中指定區域 {#specifying-regions-in-stackql-queries}

使用 StackQL，您可以指定區域，以鎖定不同地理位置的資源。這對於管理分散在全球各地的資源特別有用。

### 範例：列出多個區域的 EC2 執行個體 {#example-listing-ec2-instances-across-multiple-regions}

```sql
SELECT instance_id, region
FROM aws.ec2.instances
WHERE region IN ('us-east-1', 'eu-west-1');
```

此查詢會傳回 `us-east-1`（維吉尼亞北部）與 `eu-west-1`（愛爾蘭）區域的 EC2 執行個體清單，展示 StackQL 如何在單一查詢中跨區域擷取資料。這也可以搭配彙總查詢使用，例如：

```sql
SELECT region, COUNT(*) as num_functions
FROM aws.lambda.functions
WHERE region IN (
  'us-east-1','us-east-2','us-west-1','us-west-2',
  'ap-south-1','ap-northeast-3','ap-northeast-2',
  'ap-southeast-1','ap-southeast-2','ap-northeast-1',
  'ca-central-1','eu-central-1','eu-west-1',
  'eu-west-2','eu-west-3','eu-north-1','sa-east-1')
GROUP BY region;
```

## 非同步查詢的威力 {#the-power-of-asynchronous-queries}

StackQL 會以非同步方式執行這些跨區域查詢，對 AWS 平行發出多個 API 要求。如此可確保有效率地擷取資料，不必等待某個區域的要求完成後才處理下一個區域。

## 結論 {#conclusion}

StackQL 的非同步跨區域查詢為管理不同地理位置的 AWS 資源提供了穩健且有效率的方法。透過以類似 SQL 的語法撰寫這些查詢，StackQL 簡化了複雜的雲端管理工作，成為希望最佳化 AWS 基礎架構的開發人員與雲端管理員不可或缺的工具。
