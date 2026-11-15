---
title: Azure 활동 모니터링
hide_title: true
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
  - cspm
  - azure
  - microsoft azure
description: SQL을 사용한 Azure 클라우드 인프라 및 리소스의 쿼리와 배포
image: "/img/tutorials/azure/stackql-azure-provider-featured-image.png"
---

# StackQL을 사용한 Azure 활동 관측성

이 가이드에서는 StackQL을 사용하여 Azure 활동 로그를 쿼리하고 보고하는 방법을 알아봅니다. StackQL은 SQL 구문으로 클라우드 인프라와 리소스를 쿼리하고 배포할 수 있는 강력한 개발 도구입니다.  

테스트 환경 <span class="tutorial_tested_on">임베디드 SQL 백엔드</span> <span class="tutorial_tested_on">macos</span> <span class="tutorial_tested_on">linux</span> <span class="tutorial_tested_on">powershell</span>  

## 기본 쿼리 {#basic-query}

아래 기본 쿼리는 [__`azure`__](https://azure.stackql.io/providers/azure/) 프로바이더의 [__`azure.monitor.activity_logs`__](https://azure.stackql.io/providers/azure/monitor/activity_logs/) 리소스를 사용합니다. `subscriptionId` 값은 사용자의 구독 ID로 바꾸세요. 이 쿼리에서는 StackQL 쿼리 전처리에 외부 변수와 함께 `jsonnet`을 사용합니다. 자세한 내용은 [변수 사용](/getting-started/variables)을 참조하세요. 여기서는 인라인 `jsonnet` 코드 블록으로 작성했지만, `stackql exec` 또는 `stackql shell` 명령의 `--iqldata` 또는 `-q` 인수를 사용하여 외부 파일로 제공할 수도 있습니다.  

응답 데이터를 가공하는 데 [__`JSON_EXTRACT`__](/language-spec/functions/json/json_extract) 및 [__`SPLIT_PART`__](/language-spec/functions/string/split_part) 함수를 사용합니다.  

```sql
<<<jsonnet
{
  stackqlQuery: {
    subscriptionId: '631d1c6d-2a65-43e7-93c2-688bfe4e1468',
    filter: "eventTimestamp ge ''" + std.extVar('startdate') + "''",
  }
}
>>>
--
-- StackQL query with jsonnet preprocessing to retrieve all recent Azure activity
--
SELECT
 eventTimestamp as event_timestamp,
 caller,
 JSON_EXTRACT(claims, '$.ipaddr') as ip_address,
 subscriptionId,
 tenantId,
 SPLIT_PART(JSON_EXTRACT(authorization, '$.scope'), '/', 5) as resource_group,
 SPLIT_PART(resourceId, '/', -1) as resource_name,
 level,
 JSON_EXTRACT(authorization, '$.action') as action,
 JSON_EXTRACT(category, '$.localizedValue') as category, 
 JSON_EXTRACT(operationName, '$.localizedValue') as operation, 
 JSON_EXTRACT(status, '$.localizedValue') as status 
FROM azure.monitor.activity_logs
WHERE $filter =  '{{ .stackqlQuery.filter }}'
AND subscription_id = '{{ .stackqlQuery.subscriptionId }}';
```

## 대화형으로 쿼리 실행 {#running-the-query-interactively}

Azure에 인증된 머신(Azure Portal Cloud Shell 포함)에서 다음을 실행합니다:

```bash
# to run on mac or linux
./stackql shell --var startdate=$(date -u -d '1 day ago' '+%Y-%m-%dT%H:%M:%SZ')
```

또는 PowerShell을 사용하는 경우:

```powershell
# to run using windows powershell
$startDate = (Get-Date).AddDays(-1).ToString("yyyy-MM-ddTHH:mm:ssZ")
stackql.exe shell --var startdate=$startDate
```

`event_timestamp`, `caller`, `ip_address`, `subscriptionId`, `tenantId`, `resource_group`, `resource_name`, `level`, `action`, `category`, `operation`, `status` 열이 있는 표 형식의 출력이 표시됩니다.   

## CSV 보고서 생성 {#generating-a-csv-report}

StackQL 쿼리를 파일(`activity_report.iql`)로 저장하면 다음과 같이 활동 쿼리에서 `csv` 보고서를 생성할 수 있습니다(`stackql` 출력에 대한 자세한 내용은 [__`--output csv`__](/getting-started/output-modes#csv-output)를 참조하세요):

```bash
# to run on mac or linux
./stackql exec \
--output csv \
-i activity_report.iql \
-f activity_report.csv \
--var startdate=$(date -u -d '1 day ago' '+%Y-%m-%dT%H:%M:%SZ')
```

또는 PowerShell을 사용하는 경우:

```powershell
# to run using windows powershell
$startDate = (Get-Date).AddDays(-1).ToString("yyyy-MM-ddTHH:mm:ssZ")
stackql.exe exec `
--output csv `
-i activity_report.iql `
-f activity_report.csv `
--var startdate=$startDate
```

## KQL을 사용한 서버 측 필터링 {#server-side-filtering-with-kql}

Azure의 서버 측 필터링은 KQL(Kusto Query Language)을 사용하여 수행합니다. 쿼리의 `$filter` 절을 확장하면 서버에서 직접 데이터를 필터링할 수 있습니다. 이 방법은 네트워크를 통해 전송되는 데이터의 양을 줄이므로 매우 효율적입니다. 다음은 서버 측 필터링의 몇 가지 예제입니다(간결하게 하기 위해 이 쿼리에서는 jsonnet 전처리기 대신 매개 변수를 직접 사용합니다):


### 활동의 날짜 범위 지정 {#specifying-a-date-range-for-activities}

```sql
--
-- List events in a time range
--
SELECT
 eventTimestamp as event_timestamp,
 caller,
 JSON_EXTRACT(claims, '$.ipaddr') as ip_address,
 subscriptionId,
 tenantId,
 SPLIT_PART(JSON_EXTRACT(authorization, '$.scope'), '/', 5) as resource_group,
 SPLIT_PART(resourceId, '/', -1) as resource_name,
 level,
 JSON_EXTRACT(authorization, '$.action') as action,
 JSON_EXTRACT(category, '$.localizedValue') as category, 
 JSON_EXTRACT(operationName, '$.localizedValue') as operation, 
 JSON_EXTRACT(status, '$.localizedValue') as status 
FROM azure.monitor.activity_logs
WHERE $filter =  'eventTimestamp ge ''2024-01-24T00:00:00Z'' and eventTimestamp le ''2024-01-25T00:00:00Z'''
AND subscription_id = '631d1c6d-2a65-43e7-93c2-688bfe4e1468';
```

### 리소스 그룹의 활동 조회 {#getting-activities-for-a-resource-group}

```sql
--
-- List events for a resource group
--
SELECT
 eventTimestamp as event_timestamp,
 caller,
 JSON_EXTRACT(claims, '$.ipaddr') as ip_address,
 subscriptionId,
 tenantId,
 SPLIT_PART(resourceId, '/', -1) as resource_name,
 level,
 JSON_EXTRACT(authorization, '$.action') as action,
 JSON_EXTRACT(category, '$.localizedValue') as category, 
 JSON_EXTRACT(operationName, '$.localizedValue') as operation, 
 JSON_EXTRACT(status, '$.localizedValue') as status 
FROM azure.monitor.activity_logs
WHERE $filter =  'eventTimestamp ge ''2024-01-24T00:00:00Z'' and eventTimestamp le ''2024-01-25T00:00:00Z'' and resourceGroupName eq ''MC_kube-03_aks862a_australiaeast'''
AND subscription_id = '631d1c6d-2a65-43e7-93c2-688bfe4e1468';
```

### 리소스 유형의 활동 조회 {#getting-activities-for-a-resource-type}

```sql
--
-- List events for a resource provider
--
SELECT
 eventTimestamp as event_timestamp,
 caller,
 JSON_EXTRACT(claims, '$.ipaddr') as ip_address,
 subscriptionId,
 tenantId,
 SPLIT_PART(JSON_EXTRACT(authorization, '$.scope'), '/', 5) as resource_group,
 SPLIT_PART(resourceId, '/', -1) as resource_name,
 level,
 JSON_EXTRACT(authorization, '$.action') as action,
 JSON_EXTRACT(category, '$.localizedValue') as category, 
 JSON_EXTRACT(operationName, '$.localizedValue') as operation, 
 JSON_EXTRACT(status, '$.localizedValue') as status 
FROM azure.monitor.activity_logs
WHERE $filter =  'eventTimestamp ge ''2024-01-24T00:00:00Z'' and eventTimestamp le ''2024-01-25T00:00:00Z'' and resourceProvider eq ''Microsoft.Compute'''
AND subscription_id = '631d1c6d-2a65-43e7-93c2-688bfe4e1468';
```

### 특정 리소스의 활동 조회 {#getting-activities-for-a-specific-resource}

참고로 다음 서버 측 필터에서 사용할 필드(`correlationId`)를 하나 더 추가했습니다.  

```sql
--
-- List events for resource using the resourceUri
--
SELECT
 eventTimestamp as event_timestamp,
 caller,
 JSON_EXTRACT(claims, '$.ipaddr') as ip_address,
 subscriptionId,
 tenantId,
  SPLIT_PART(resourceId, '/', -1) as resource_name,
 correlationId,
 level,
 JSON_EXTRACT(authorization, '$.action') as action,
 JSON_EXTRACT(category, '$.localizedValue') as category, 
 JSON_EXTRACT(operationName, '$.localizedValue') as operation, 
 JSON_EXTRACT(status, '$.localizedValue') as status 
FROM azure.monitor.activity_logs
WHERE $filter =  'eventTimestamp ge ''2024-01-24T00:00:00Z'' and eventTimestamp le ''2024-01-25T00:00:00Z'' and resourceUri eq ''/subscriptions/631d1c6d-2a65-43e7-93c2-688bfe4e1468/resourceGroups/MC_kube-03_aks862a_australiaeast/providers/Microsoft.Compute/virtualMachines/aks-agentpool-35064155-1'''
AND subscription_id = '631d1c6d-2a65-43e7-93c2-688bfe4e1468';
```

### 특정 리소스의 활동 조회 {#getting-activities-for-a-specific-resource-1}

상관 관계 ID는 Azure에서 특정 작업 또는 트랜잭션과 관련된 이벤트를 추적하고 그룹화하는 데 사용되는 고유 식별자입니다. 이 쿼리는 지정된 시간 범위 내에서 특정 작업과 관련된 이벤트를 정확히 찾아내는 데 도움이 되므로, 문제를 해결하거나 활동의 순서를 파악하기가 쉬워집니다. 다음은 이전 쿼리에서 얻은 `correlationId` 값을 사용하는 예제입니다:  

```sql
--
-- List events for a correlation Id
--
SELECT
 eventTimestamp as event_timestamp,
 caller,
 JSON_EXTRACT(claims, '$.ipaddr') as ip_address,
 subscriptionId,
 tenantId,
 SPLIT_PART(JSON_EXTRACT(authorization, '$.scope'), '/', 5) as resource_group,
 SPLIT_PART(resourceId, '/', -1) as resource_name,
 level,
 JSON_EXTRACT(authorization, '$.action') as action,
 JSON_EXTRACT(category, '$.localizedValue') as category, 
 JSON_EXTRACT(operationName, '$.localizedValue') as operation, 
 JSON_EXTRACT(status, '$.localizedValue') as status 
FROM azure.monitor.activity_logs
WHERE $filter =  'eventTimestamp ge ''2024-01-24T00:00:00Z'' and eventTimestamp le ''2024-01-25T00:00:00Z'' and correlationId eq ''30b92c6d-4a82-401f-ac73-e28377669539'''
AND subscription_id = '631d1c6d-2a65-43e7-93c2-688bfe4e1468';
```

## `WHERE` 절을 사용한 클라이언트 측 필터링 {#client-side-filtering-with-the-where-clause}

클라이언트 측 필터링은 일반적인 SQL `WHERE` 절을 사용하여 수행할 수 있습니다. 이 방법은 서버에서 데이터를 가져온 후에 필터링하므로, StackQL 환경 내에서 데이터를 유연하게 가공하고 분석할 수 있습니다.  

### 특정 사용자의 활동 조회 {#getting-activities-for-a-specific-user}

예제 쿼리에서 `WHERE` 절은 `caller` 열(사용자)을 기준으로 레코드를 필터링하여 데이터를 한 단계 더 세분화합니다.  

```sql
--
-- Filter events for a particular user (caller)
--
SELECT
 eventTimestamp as event_timestamp,
 caller,
 JSON_EXTRACT(claims, '$.ipaddr') as ip_address,
 subscriptionId,
 tenantId,
 SPLIT_PART(JSON_EXTRACT(authorization, '$.scope'), '/', 5) as resource_group,
 SPLIT_PART(resourceId, '/', -1) as resource_name,
 level,
 JSON_EXTRACT(authorization, '$.action') as action,
 JSON_EXTRACT(category, '$.localizedValue') as category, 
 JSON_EXTRACT(operationName, '$.localizedValue') as operation, 
 JSON_EXTRACT(status, '$.localizedValue') as status 
FROM azure.monitor.activity_logs
WHERE $filter =  'eventTimestamp ge ''2024-01-01T00:00:00Z'''
AND subscription_id = '631d1c6d-2a65-43e7-93c2-688bfe4e1468'
AND caller = 'javen@stackql.io';
```

## 요약/집계 쿼리 실행 {#running-summaryaggregate-queries}

StackQL은 완전한 SQL 엔진이므로 [__`AVG`__](/language-spec/functions/aggregate/avg), [__`COUNT`__](/language-spec/functions/aggregate/count), [__`SUM`__](/language-spec/functions/aggregate/sum), [__`MIN`__](/language-spec/functions/aggregate/min), [__`MAX`__](/language-spec/functions/aggregate/max) 등을 포함한 `GROUP BY`/집계 쿼리를 모든 프로바이더, 서비스, 리소스에서 기본적으로 사용할 수 있습니다.

### 심각도 수준별 이벤트 수 계산 {#count-events-by-severity-level}

이 예제에서는 시작 날짜/시간 이후의 이벤트를 심각도별로 그룹화하고 각 수준의 개수를 계산합니다.

```sql
--
--  Summarize events by severity level
--
SELECT
 level,
 COUNT(*) as num_events
FROM azure.monitor.activity_logs
WHERE $filter =  'eventTimestamp ge ''2024-01-29T00:00:00Z'''
AND subscription_id = '631d1c6d-2a65-43e7-93c2-688bfe4e1468'
GROUP BY level;
/* returns..
|---------------|------------|                                                                                                                                                                               
|     level     | num_events |                                                                                                                                                                               
|---------------|------------|                                                                                                                                                                               
| Error         |         58 |                                                                                                                                                                               
|---------------|------------|                                                                                                                                                                               
| Informational |         57 |                                                                                                                                                                               
|---------------|------------|                                                                                                                                                                               
| Warning       |         57 |                                                                                                                                                                               
|---------------|------------| 
*/
```

## `JOIN` 절을 사용한 활동 보강 {#using-join-to-enrich-activities}

```sql
SELECT
l.eventTimestamp as event_timestamp,
l.level,
JSON_EXTRACT(l.operationName, '$.localizedValue') as operation, 
vm.name,
vm.location,
JSON_EXTRACT(vm.properties, '$.timeCreated') as time_created
FROM azure.monitor.activity_logs l
INNER JOIN azure.compute.virtual_machines vm
ON vm.name = SPLIT_PART(l.resourceId, '/', -1)
WHERE vm.resource_group_name = 'MC_kube-03_aks862a_australiaeast'
AND subscription_id = '631d1c6d-2a65-43e7-93c2-688bfe4e1468'
AND l.$filter =  'eventTimestamp ge ''2024-01-28T00:00:00Z'' and resourceGroupName eq ''MC_kube-03_aks862a_australiaeast''';
```

또는 `jsonnet` 변수를 사용할 수도 있습니다. 변수는 `--iqldata` 또는 `-q` 인수를 사용하여 파일로 제공하거나, 여기에 표시된 것처럼 jsonnet 블록으로 제공합니다:

```sql
<<<jsonnet
{
  stackqlQuery: {
    subscriptionId: '631d1c6d-2a65-43e7-93c2-688bfe4e1468',
    resourceGroupName: 'MC_kube-03_aks862a_australiaeast',
  }
}
>>>
SELECT
l.eventTimestamp as event_timestamp,
l.level,
JSON_EXTRACT(l.operationName, '$.localizedValue') as operation, 
vm.name,
vm.location,
JSON_EXTRACT(vm.properties, '$.timeCreated') as time_created
FROM azure.monitor.activity_logs l
INNER JOIN azure.compute.virtual_machines vm
ON vm.name = SPLIT_PART(l.resourceId, '/', -1)
WHERE vm.resource_group_name = '{{ .stackqlQuery.resourceGroupName}}'
AND subscription_id = '{{ .stackqlQuery.subscriptionId}}'
AND l.$filter =  'eventTimestamp ge ''2024-01-28T00:00:00Z'' and resourceGroupName eq ''{{ .stackqlQuery.resourceGroupName}}''';
```

:::info

`JOIN` 쿼리에서 위 예제의 `subscriptionId` 매개 변수처럼 어느 리소스의 것인지 구분되지 않은 `WHERE` 매개 변수는 두 리소스(이 경우 `azure.monitor.activity_logs` 및 `azure.compute.virtual_machines`)에서 공유됩니다.

:::
