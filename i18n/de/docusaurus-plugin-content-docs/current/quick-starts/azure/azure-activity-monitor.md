---
title: Azure-Aktivität überwachen
hide_title: true
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
  - cspm
  - azure
  - microsoft azure
description: Azure-Cloud-Infrastruktur und -Ressourcen mit SQL abfragen und bereitstellen
image: "/img/tutorials/azure/stackql-azure-provider-featured-image.png"
---

# Observability von Azure-Aktivitäten mit StackQL

In dieser Anleitung erfahren Sie, wie Sie Azure-Aktivitätsprotokolle mit StackQL abfragen und Berichte daraus erstellen, einem leistungsfähigen Entwicklerwerkzeug, mit dem sich Cloud-Infrastruktur und -Ressourcen per SQL-Syntax abfragen und bereitstellen lassen.  

Getestet mit <span class="tutorial_tested_on">eingebettetes SQL-Backend</span> <span class="tutorial_tested_on">macos</span> <span class="tutorial_tested_on">linux</span> <span class="tutorial_tested_on">powershell</span>  

## Einfache Abfrage {#basic-query}

Die unten gezeigte einfache Abfrage verwendet die Ressource [__`azure.monitor.activity_logs`__](https://azure.stackql.io/providers/azure/monitor/activity_logs/) im Provider [__`azure`__](https://azure.stackql.io/providers/azure/); ersetzen Sie die `subscriptionId` durch Ihre eigene.  In dieser Abfrage verwenden wir `jsonnet` mit externen Variablen für die Vorverarbeitung der StackQL-Abfrage, weitere Informationen finden Sie unter [Variablen verwenden](/getting-started/variables).  In diesem Fall haben wir dies als Inline-`jsonnet`-Codeblock umgesetzt, es kann aber auch als externe Datei über die Argumente `--iqldata` oder `-q` an `stackql exec` oder `stackql shell` übergeben werden.  

Die Funktionen [__`JSON_EXTRACT`__](/language-spec/functions/json/json_extract) und [__`SPLIT_PART`__](/language-spec/functions/string/split_part) werden verwendet, um die Antwortdaten aufzubereiten.  

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

## Die Abfrage interaktiv ausführen {#running-the-query-interactively}

Von einem Rechner aus, der bei Azure authentifiziert ist (einschließlich der Cloud Shell im Azure-Portal):

```bash
# to run on mac or linux
./stackql shell --var startdate=$(date -u -d '1 day ago' '+%Y-%m-%dT%H:%M:%SZ')
```

oder mit PowerShell:

```powershell
# to run using windows powershell
$startDate = (Get-Date).AddDays(-1).ToString("yyyy-MM-ddTHH:mm:ssZ")
stackql.exe shell --var startdate=$startDate
```

Sie sollten eine tabellarische Ausgabe mit den Spalten `event_timestamp`, `caller`, `ip_address`, `subscriptionId`, `tenantId`, `resource_group`, `resource_name`, `level`, `action`, `category`, `operation` und `status` sehen.   

## Einen CSV-Bericht erstellen {#generating-a-csv-report}

Wenn Sie die StackQL-Abfrage in einer Datei (`activity_report.iql`) speichern, können Sie wie folgt einen `csv`-Bericht aus der Aktivitätsabfrage erzeugen (weitere Informationen zu den Ausgaben von `stackql` finden Sie unter [__`--output csv`__](/getting-started/output-modes#csv-output)):

```bash
# to run on mac or linux
./stackql exec \
--output csv \
-i activity_report.iql \
-f activity_report.csv \
--var startdate=$(date -u -d '1 day ago' '+%Y-%m-%dT%H:%M:%SZ')
```

oder mit PowerShell:

```powershell
# to run using windows powershell
$startDate = (Get-Date).AddDays(-1).ToString("yyyy-MM-ddTHH:mm:ssZ")
stackql.exe exec `
--output csv `
-i activity_report.iql `
-f activity_report.csv `
--var startdate=$startDate
```

## Serverseitige Filterung mit KQL {#server-side-filtering-with-kql}

Serverseitige Filterung wird in Azure über KQL (Kusto Query Language) umgesetzt. Indem Sie die `$filter`-Klausel in Ihrer Abfrage erweitern, können Sie Daten direkt auf dem Server filtern. Diese Methode ist sehr effizient, da sie die über das Netzwerk übertragene Datenmenge reduziert.  Nachfolgend einige Beispiele für serverseitige Filterung (der Kürze halber verwenden diese Abfragen die Parameter direkt, statt den jsonnet-Präprozessor zu nutzen):


### Einen Datumsbereich für Aktivitäten angeben {#specifying-a-date-range-for-activities}

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

### Aktivitäten für eine Ressourcengruppe abrufen {#getting-activities-for-a-resource-group}

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

### Aktivitäten für einen Ressourcentyp abrufen {#getting-activities-for-a-resource-type}

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

### Aktivitäten für eine bestimmte Ressource abrufen {#getting-activities-for-a-specific-resource}

Beachten Sie, dass wir ein zusätzliches Feld (`correlationId`) hinzugefügt haben, das wir im nächsten serverseitigen Filter verwenden werden...  

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

### Aktivitäten für eine bestimmte Ressource abrufen {#getting-activities-for-a-specific-resource-1}

Die Korrelations-ID ist ein eindeutiger Bezeichner, mit dem Ereignisse, die zu einer bestimmten Operation oder Transaktion in Azure gehören, nachverfolgt und gruppiert werden. Diese Abfrage hilft dabei, bestimmte Ereignisse innerhalb eines Zeitraums zu finden, die zu einer bestimmten Operation gehören, was die Fehlersuche und das Nachvollziehen der Abfolge von Aktivitäten erleichtert.  Hier ein Beispiel mit einer `correlationId`, die in der vorherigen Abfrage erfasst wurde:  

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

## Clientseitige Filterung mit der `WHERE`-Klausel {#client-side-filtering-with-the-where-clause}

Clientseitige Filterung erfolgt über die klassische SQL-`WHERE`-Klausel. Diese Methode filtert die Daten, nachdem sie vom Server abgerufen wurden, und bietet Flexibilität bei der Aufbereitung und Analyse der Daten innerhalb der StackQL-Umgebung.  

### Aktivitäten für einen bestimmten Benutzer abrufen {#getting-activities-for-a-specific-user}

In der Beispielabfrage filtert die `WHERE`-Klausel die Datensätze nach dem `caller` (dem Benutzer) und bietet damit eine zusätzliche Ebene der Datenverfeinerung.  

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

## Zusammenfassende/aggregierende Abfragen ausführen {#running-summaryaggregate-queries}

Da StackQL eine vollständige SQL-Engine ist, stehen `GROUP BY`-/Aggregatabfragen (einschließlich [__`AVG`__](/language-spec/functions/aggregate/avg), [__`COUNT`__](/language-spec/functions/aggregate/count), [__`SUM`__](/language-spec/functions/aggregate/sum), [__`MIN`__](/language-spec/functions/aggregate/min), [__`MAX`__](/language-spec/functions/aggregate/max) und weitere) nativ für alle Provider, Services und Ressourcen zur Verfügung.

### Ereignisse nach Schweregrad zählen {#count-events-by-severity-level}

In diesem Beispiel gruppieren wir Ereignisse ab einem Startzeitpunkt nach ihrem Schweregrad und berechnen die Anzahl für jede Stufe.

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

## Aktivitäten mit `JOIN` anreichern {#using-join-to-enrich-activities}

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

oder mit Variablen aus `jsonnet`, die entweder als Datei über die Argumente `--iqldata` oder `-q` oder als jsonnet-Block wie hier gezeigt bereitgestellt werden:

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

`WHERE`-Parameter in einer `JOIN`-Abfrage, die nicht eindeutig zugeordnet sind, wie `subscriptionId` im obigen Fall, gelten für beide Ressourcen (in diesem Fall `azure.monitor.activity_logs` und `azure.compute.virtual_machines`).

:::
