---
title: クロスリージョンクエリ
hide_title: true
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
  - cspm
  - aws
  - amazon web services
description: "リージョンごとのループの代わりに、リージョンのリストと非同期実行を使用して、1 つの StackQL 文で複数リージョンの AWS リソースをクエリし、集約します。"
image: "/img/tutorials/aws/stackql-aws-provider-featured-image.png"
---

# StackQL による非同期クロスリージョンクエリ

このガイドでは、StackQL の強力な機能である、AWS の複数のリージョンにまたがるリソースをクエリして集約する機能を紹介します。これには異なるエンドポイントへの多数の API リクエストが伴い、それらは非同期に実行されます。この処理はシームレスであり、ユーザーからは透過的に行われます。

> StackQL は、SQL 構文を使用してクラウドインフラストラクチャとリソースのクエリとデプロイを行える強力な開発ツールです。

動作確認済み <span class="tutorial_tested_on">組み込み SQL バックエンド</span> <span class="tutorial_tested_on">postgres SQL バックエンド</span> <span class="tutorial_tested_on">macos</span> <span class="tutorial_tested_on">linux</span> <span class="tutorial_tested_on">powershell</span>

## AWS のリージョンとリソースの理解 {#understanding-aws-regions-and-resources}

AWS リソースは、世界中の複数のロケーションでホストされています。これらのロケーションはリージョンとアベイラビリティーゾーンで構成されており、ユーザーがさまざまな場所にインスタンスを配置し、データを保存できるように設計されています。

## AWS リージョンの問題点 {#the-problem-with-aws-regions}

AWS を扱う際は、AWS Management Console がリージョン単位でスコープされたリソースのビューを提供することを理解しておくことが重要です。ユーザーは、一度に 1 つの特定のリージョン内でリソースを操作および管理します。同様に、AWS Command Line Interface（CLI）のコマンドとソフトウェア開発キット（SDK）も、リージョンを操作のエントリポイントとして使用します。そのため、コマンドやプログラムを実行するにはリージョンを指定する必要があります。

> クラウドサービスのリージョン単位の枠組みは、リソースが複数のリージョンにまたがる場合に複雑さをもたらします。さらに、暗号通貨のマイニングを目的とした悪用を含むセキュリティ侵害では、組織が通常使用するプライマリリージョンを越えて、さまざまな場所のリソースが利用されることがよくあります。

## StackQL クエリでのリージョンの指定 {#specifying-regions-in-stackql-queries}

StackQL では、リージョンを指定して、地理的に異なる場所にあるリソースを対象にできます。これは、世界中に分散しているリソースを管理する場合に特に便利です。

### 例：複数のリージョンにわたる EC2 インスタンスの一覧表示 {#example-listing-ec2-instances-across-multiple-regions}

```sql
SELECT instance_id, region
FROM aws.ec2.instances
WHERE region IN ('us-east-1', 'eu-west-1');
```

このクエリは、`us-east-1`（バージニア北部）リージョンと `eu-west-1`（アイルランド）リージョンの EC2 インスタンスの一覧を返します。StackQL が 1 つのクエリで複数のリージョンにわたってデータを取得できることを示しています。これは、次のような集約クエリでも使用できます。

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

## 非同期クエリの利点 {#the-power-of-asynchronous-queries}

StackQL は、これらのクロスリージョンクエリを非同期に実行し、AWS に対して複数の API リクエストを並列に送信します。これにより、あるリージョンのリクエストの完了を待ってから次のリージョンに進む必要がなく、効率的にデータを取得できます。

## まとめ {#conclusion}

StackQL の非同期クロスリージョンクエリは、地理的に異なる場所にある AWS リソースを管理するための堅牢で効率的な方法を提供します。StackQL は、これらのクエリに SQL に似た構文を使用することで、複雑なクラウド管理タスクを簡素化します。そのため、AWS インフラストラクチャの最適化を目指す開発者やクラウド管理者にとって、非常に価値のあるツールです。
