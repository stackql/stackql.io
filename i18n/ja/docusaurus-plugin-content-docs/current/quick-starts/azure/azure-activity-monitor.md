---
title: Azure アクティビティの監視
hide_title: true
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
  - cspm
  - azure
  - microsoft azure
description: SQL を使用した Azure クラウドインフラストラクチャとリソースのクエリとデプロイ
image: "/img/tutorials/azure/stackql-azure-provider-featured-image.png"
---

# StackQL を使用した Azure アクティビティのオブザーバビリティ

このガイドでは、StackQL を使用して Azure アクティビティログをクエリし、レポートする方法を説明します。StackQL は、SQL 構文でクラウドインフラストラクチャとリソースのクエリとデプロイを行える強力な開発ツールです。  

動作確認済み <span class="tutorial_tested_on">組み込み SQL バックエンド</span> <span class="tutorial_tested_on">macos</span> <span class="tutorial_tested_on">linux</span> <span class="tutorial_tested_on">powershell</span>  

## 基本的なクエリ {#basic-query}

次の基本的なクエリでは、[__`azure`__](https://azure.stackql.io/providers/azure/) プロバイダーの [__`azure.monitor.activity_logs`__](https://azure.stackql.io/providers/azure/monitor/activity_logs/) リソースを使用します。`subscriptionId` は実際の値に置き換えてください。このクエリでは、StackQL クエリの前処理に外部変数付きの `jsonnet` を使用します。詳細については、[変数の使用](/getting-started/variables)を参照してください。ここではインラインの `jsonnet` コードブロックとして記述していますが、`stackql exec` または `stackql shell` の `--iqldata` 引数または `-q` 引数を使用して、外部ファイルとして指定することもできます。  

レスポンスデータの加工には、[__`JSON_EXTRACT`__](/language-spec/functions/json/json_extract) 関数と [__`SPLIT_PART`__](/language-spec/functions/string/split_part) 関数を使用します。  

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

## クエリを対話的に実行する {#running-the-query-interactively}

Azure に認証済みのマシン（Azure Portal Cloud Shell を含む）から、次のように実行します：

```bash
# to run on mac or linux
./stackql shell --var startdate=$(date -u -d '1 day ago' '+%Y-%m-%dT%H:%M:%SZ')
```

または PowerShell を使用する場合：

```powershell
# to run using windows powershell
$startDate = (Get-Date).AddDays(-1).ToString("yyyy-MM-ddTHH:mm:ssZ")
stackql.exe shell --var startdate=$startDate
```

`event_timestamp`、`caller`、`ip_address`、`subscriptionId`、`tenantId`、`resource_group`、`resource_name`、`level`、`action`、`category`、`operation` および `status` の列を持つ表形式の出力が表示されます。   

## CSV レポートの生成 {#generating-a-csv-report}

StackQL クエリをファイル（`activity_report.iql`）に保存すると、次のようにしてアクティビティクエリから `csv` レポートを生成できます（`stackql` の出力の詳細については、[__`--output csv`__](/getting-started/output-modes#csv-output) を参照してください）：

```bash
# to run on mac or linux
./stackql exec \
--output csv \
-i activity_report.iql \
-f activity_report.csv \
--var startdate=$(date -u -d '1 day ago' '+%Y-%m-%dT%H:%M:%SZ')
```

または PowerShell を使用する場合：

```powershell
# to run using windows powershell
$startDate = (Get-Date).AddDays(-1).ToString("yyyy-MM-ddTHH:mm:ssZ")
stackql.exe exec `
--output csv `
-i activity_report.iql `
-f activity_report.csv `
--var startdate=$startDate
```

## KQL によるサーバー側のフィルタリング {#server-side-filtering-with-kql}

Azure のサーバー側のフィルタリングは、KQL（Kusto Query Language）を使用して実現します。クエリの `$filter` 句を拡張すると、サーバー上で直接データをフィルタリングできます。この方法はネットワーク経由で転送されるデータ量を減らすため、非常に効率的です。サーバー側のフィルタリングの例を以下にいくつか示します（簡潔にするため、これらのクエリでは jsonnet プリプロセッサーを使用せずに、パラメーターを直接指定しています）：


### アクティビティの日付範囲を指定する {#specifying-a-date-range-for-activities}

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

### リソースグループのアクティビティを取得する {#getting-activities-for-a-resource-group}

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

### リソースタイプのアクティビティを取得する {#getting-activities-for-a-resource-type}

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

### 特定のリソースのアクティビティを取得する {#getting-activities-for-a-specific-resource}

次のサーバー側のフィルターで使用するフィールド（`correlationId`）を追加している点に注意してください。  

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

### 特定のリソースのアクティビティを取得する {#getting-activities-for-a-specific-resource-1}

相関 ID は、Azure で特定の操作やトランザクションに関連するイベントを追跡し、グループ化するための一意の識別子です。このクエリを使用すると、指定した期間内で特定の操作に関連するイベントを特定できるため、トラブルシューティングやアクティビティの順序の把握が容易になります。前のクエリで取得した `correlationId` を使用した例を次に示します。  

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

## `WHERE` 句によるクライアント側のフィルタリング {#client-side-filtering-with-the-where-clause}

クライアント側のフィルタリングは、従来の SQL の `WHERE` 句を使用して行えます。この方法ではサーバーからデータを取得した後にフィルタリングするため、StackQL 環境内でのデータの加工や分析を柔軟に行えます。  

### 特定のユーザーのアクティビティを取得する {#getting-activities-for-a-specific-user}

この例のクエリでは、`WHERE` 句で `caller`（ユーザー）に基づいて行をフィルタリングし、データをさらに絞り込みます。  

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

## 要約クエリと集約クエリの実行 {#running-summaryaggregate-queries}

StackQL は完全な SQL エンジンであるため、`GROUP BY` や集約クエリ（[__`AVG`__](/language-spec/functions/aggregate/avg)、[__`COUNT`__](/language-spec/functions/aggregate/count)、[__`SUM`__](/language-spec/functions/aggregate/sum)、[__`MIN`__](/language-spec/functions/aggregate/min)、[__`MAX`__](/language-spec/functions/aggregate/max) など）を、すべてのプロバイダー、サービス、リソースでネイティブに利用できます。

### 重大度レベル別にイベント数を数える {#count-events-by-severity-level}

この例では、開始日時以降のイベントを重大度でグループ化し、レベルごとの件数を計算します。

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

## `JOIN` によるアクティビティ情報の拡充 {#using-join-to-enrich-activities}

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

または、`jsonnet` の変数を使用することもできます。変数は `--iqldata` 引数または `-q` 引数でファイルとして指定するか、ここに示すように jsonnet ブロックとして指定します：

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

`JOIN` クエリ内で曖昧さが解消されていない `WHERE` パラメーター（上記の例の `subscriptionId` など）は、両方のリソース（この例では `azure.monitor.activity_logs` と `azure.compute.virtual_machines`）で共有されます。

:::
