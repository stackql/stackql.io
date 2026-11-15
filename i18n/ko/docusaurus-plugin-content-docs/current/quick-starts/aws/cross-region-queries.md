---
title: 리전 간 쿼리
hide_title: true
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
  - cspm
  - aws
  - amazon web services
description: "리전별 루프 대신 리전 목록과 비동기 실행을 사용하여 하나의 StackQL 문으로 여러 리전의 AWS 리소스를 쿼리하고 집계합니다."
image: "/img/tutorials/aws/stackql-aws-provider-featured-image.png"
---

# StackQL을 사용한 비동기 리전 간 쿼리

이 가이드에서는 StackQL의 강력한 기능, 즉 AWS의 여러 리전에 걸쳐 리소스를 쿼리하고 집계하는 기능을 보여 줍니다. 이 작업에는 여러 엔드포인트에 대한 수많은 API 요청이 수반되며, 이러한 요청은 비동기적으로 수행됩니다. 사용자 입장에서는 이 과정이 매끄럽고 투명하게 처리됩니다.

> StackQL은 SQL 구문을 사용하여 클라우드 인프라와 리소스를 쿼리하고 배포할 수 있는 강력한 개발 도구입니다.

테스트 환경 <span class="tutorial_tested_on">임베디드 SQL 백엔드</span> <span class="tutorial_tested_on">postgres SQL 백엔드</span> <span class="tutorial_tested_on">macos</span> <span class="tutorial_tested_on">linux</span> <span class="tutorial_tested_on">powershell</span>

## AWS 리전 및 리소스 이해 {#understanding-aws-regions-and-resources}

AWS 리소스는 전 세계 여러 위치에서 호스팅됩니다. 이러한 위치는 리전과 가용 영역으로 구성되며, 사용자가 다양한 위치에 인스턴스를 배치하고 데이터를 저장할 수 있도록 설계되었습니다.

## AWS 리전의 문제점 {#the-problem-with-aws-regions}

AWS로 작업할 때는 AWS Management Console이 리전 범위로 한정된 리소스 보기를 제공한다는 점을 이해하는 것이 중요합니다. 사용자는 한 번에 하나의 특정 리전 내에서 리소스와 상호 작용하고 리소스를 관리할 수 있습니다. 마찬가지로 AWS Command Line Interface(CLI) 명령과 Software Development Kit(SDK)는 리전을 작업의 진입점으로 사용합니다. 따라서 명령을 실행하거나 프로그램을 실행하려면 리전을 지정해야 합니다.

> 클라우드 서비스의 리전 기반 프레임워크는 리소스가 여러 리전에 걸쳐 있을 때 복잡성을 야기합니다. 또한 암호화폐 채굴 악용을 포함한 보안 침해는 조직이 일반적으로 사용하는 주 리전을 벗어나 다양한 위치의 리소스를 이용하는 경우가 많습니다.

## StackQL 쿼리에서 리전 지정 {#specifying-regions-in-stackql-queries}

StackQL을 사용하면 리전을 지정하여 서로 다른 지리적 위치에 있는 리소스를 대상으로 삼을 수 있습니다. 이는 전 세계에 분산된 리소스를 관리할 때 특히 유용합니다.

### 예제: 여러 리전의 EC2 인스턴스 나열 {#example-listing-ec2-instances-across-multiple-regions}

```sql
SELECT instance_id, region
FROM aws.ec2.instances
WHERE region IN ('us-east-1', 'eu-west-1');
```

이 쿼리는 `us-east-1`(버지니아 북부) 및 `eu-west-1`(아일랜드) 리전의 EC2 인스턴스 목록을 반환하며, StackQL이 단일 쿼리로 여러 리전의 데이터를 가져올 수 있음을 보여 줍니다. 다음과 같이 집계 쿼리에도 사용할 수 있습니다.

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

## 비동기 쿼리의 강점 {#the-power-of-asynchronous-queries}

StackQL은 이러한 리전 간 쿼리를 비동기적으로 실행하여 AWS에 여러 API 요청을 병렬로 보냅니다. 따라서 한 리전의 요청이 완료될 때까지 기다린 후 다음 리전으로 넘어갈 필요 없이 데이터를 효율적으로 조회할 수 있습니다.

## 결론 {#conclusion}

StackQL의 비동기 리전 간 쿼리는 서로 다른 지리적 위치에 있는 AWS 리소스를 관리하기 위한 강력하고 효율적인 방법을 제공합니다. StackQL은 이러한 쿼리에 SQL과 유사한 구문을 활용하여 복잡한 클라우드 관리 작업을 단순화합니다. 따라서 AWS 인프라를 최적화하려는 개발자와 클라우드 관리자에게 매우 유용한 도구입니다.
