---
title: 監控 Azure 活動
hide_title: true
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
  - cspm
  - azure
  - microsoft azure
description: "使用 StackQL 查詢 Azure Monitor 活動記錄：依時間、資源或相互關聯識別碼在伺服器端篩選事件，依嚴重性計數，並與虛擬機器聯結。"
image: "/img/tutorials/azure/stackql-azure-provider-featured-image.png"
---

# 使用 StackQL 實現 Azure 活動可觀測性

在本指南中，您將了解如何使用 StackQL 查詢 Azure 活動記錄並產生報表。StackQL 是一款強大的開發工具，可使用 SQL 語法查詢及部署雲端基礎架構與資源。

測試環境：<span class="tutorial_tested_on">內嵌式 SQL 後端</span> <span class="tutorial_tested_on">macos</span> <span class="tutorial_tested_on">linux</span> <span class="tutorial_tested_on">powershell</span>

## 基本查詢 {#basic-query}

下方的基本查詢使用 [__`azure.monitor.activity_logs`__](https://azure-provider.stackql.io/services/monitor/activity_logs/) 資源，此資源位於 [__`azure`__](https://azure-provider.stackql.io/) Provider 中；請將 `subscriptionId` 取代為您自己的值。在此查詢中，我們將使用帶有外部變數的 `jsonnet` 對 StackQL 查詢進行前置處理，如需詳細資訊，請參閱[使用變數](/getting-started/variables)。此例中我們以內嵌的 `jsonnet` 程式碼區塊完成這項處理，但也可以使用 `stackql exec` 或 `stackql shell` 的 `--iqldata` 或 `-q` 引數，以外部檔案提供。

[__`JSON_EXTRACT`__](/language-spec/functions/json/json_extract) 與 [__`SPLIT_PART`__](/language-spec/functions/string/split_part) 函式用於處理回應資料。

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

## 以互動方式執行查詢 {#running-the-query-interactively}

在已向 Azure 進行驗證的電腦上（包括 Azure Portal Cloud Shell）：

```bash
# to run on mac or linux
./stackql shell --var startdate=$(date -u -d '1 day ago' '+%Y-%m-%dT%H:%M:%SZ')
```

或使用 PowerShell：

```powershell
# to run using windows powershell
$startDate = (Get-Date).AddDays(-1).ToString("yyyy-MM-ddTHH:mm:ssZ")
stackql.exe shell --var startdate=$startDate
```

您應該會看到表格形式的輸出，其中包含下列資料行：`event_timestamp`、`caller`、`ip_address`、`subscriptionId`、`tenantId`、`resource_group`、`resource_name`、`level`、`action`、`category`、`operation` 和 `status`。

## 產生 CSV 報表 {#generating-a-csv-report}

將 StackQL 查詢儲存至檔案（`activity_report.iql`）後，您可以依下列方式從活動查詢產生 `csv` 報表（如需 `stackql` 輸出的詳細資訊，請參閱 [__`--output csv`__](/getting-started/output-modes#csv-output)）：

```bash
# to run on mac or linux
./stackql exec \
--output csv \
-i activity_report.iql \
-f activity_report.csv \
--var startdate=$(date -u -d '1 day ago' '+%Y-%m-%dT%H:%M:%SZ')
```

或使用 PowerShell：

```powershell
# to run using windows powershell
$startDate = (Get-Date).AddDays(-1).ToString("yyyy-MM-ddTHH:mm:ssZ")
stackql.exe exec `
--output csv `
-i activity_report.iql `
-f activity_report.csv `
--var startdate=$startDate
```

## 使用 KQL 進行伺服器端篩選 {#server-side-filtering-with-kql}

Azure 中的伺服器端篩選是透過 KQL（Kusto 查詢語言）達成。只要擴充查詢中的 `$filter` 子句，您就可以直接在伺服器上篩選資料。這種方法非常有效率，因為它會減少透過網路傳輸的資料量。以下是一些伺服器端篩選的範例（為求簡潔，這些查詢直接使用參數，而不使用 jsonnet 前置處理器）：


### 指定活動的日期範圍 {#specifying-a-date-range-for-activities}

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

### 取得資源群組的活動 {#getting-activities-for-a-resource-group}

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

### 取得資源類型的活動 {#getting-activities-for-a-resource-type}

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

### 取得特定資源的活動 {#getting-activities-for-a-specific-resource}

請注意，我們新增了一個額外欄位（`correlationId`），將在下一個伺服器端篩選中使用。

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

### 取得相互關聯識別碼的活動 {#getting-activities-for-a-correlation-id}

相互關聯識別碼是一個唯一識別碼，用於追蹤 Azure 中與特定作業或交易相關的事件並將其分組。此查詢有助於在指定時間範圍內找出與特定作業相關的特定事件，讓疑難排解或了解活動順序更加容易。以下範例使用在上一個查詢中擷取的 `correlationId`：

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

## 使用 `WHERE` 子句進行用戶端篩選 {#client-side-filtering-with-the-where-clause}

用戶端篩選可以使用傳統的 SQL `WHERE` 子句完成。這種方法會在從伺服器擷取資料之後才篩選資料，在 StackQL 環境中提供資料處理與分析的彈性。

### 取得特定使用者的活動 {#getting-activities-for-a-specific-user}

在範例查詢中，`WHERE` 子句根據 `caller`（使用者）篩選資料列，進一步精煉資料。

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

## 執行摘要/彙總查詢 {#running-summaryaggregate-queries}

由於 StackQL 是完整的 SQL 引擎，所有 Provider、服務與資源都原生支援 `GROUP BY`/彙總查詢（包括 [__`AVG`__](/language-spec/functions/aggregate/avg)、[__`COUNT`__](/language-spec/functions/aggregate/count)、[__`SUM`__](/language-spec/functions/aggregate/sum)、[__`MIN`__](/language-spec/functions/aggregate/min)、[__`MAX`__](/language-spec/functions/aggregate/max) 等）。

### 依嚴重性層級計算事件數 {#count-events-by-severity-level}

在此範例中，我們會將自開始日期時間起的事件依嚴重性分組，並計算每個層級的數量。

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

## 使用 `JOIN` 擴充活動資料 {#using-join-to-enrich-activities}

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

或使用來自 `jsonnet` 的變數，這些變數可以透過 `--iqldata` 或 `-q` 引數以檔案提供，也可以如下所示以 jsonnet 區塊提供：

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

`JOIN` 查詢中未明確區分的 `WHERE` 參數（例如上例中的 `subscription_id`）會由兩個資源共用（此例中為 `azure.monitor.activity_logs` 與 `azure.compute.virtual_machines`）。

:::
