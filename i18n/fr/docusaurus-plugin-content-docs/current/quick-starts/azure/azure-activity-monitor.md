---
title: Surveiller l'activité Azure
hide_title: true
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
  - cspm
  - azure
  - microsoft azure
description: Interrogez et déployez l'infrastructure et les ressources cloud Azure en SQL
image: "/img/tutorials/azure/stackql-azure-provider-featured-image.png"
---

# Observabilité de l'activité Azure avec StackQL

Dans ce guide, vous apprendrez à interroger les journaux d'activité Azure et à en produire des rapports avec StackQL, un outil de développement puissant qui permet d'interroger et de déployer l'infrastructure et les ressources cloud à l'aide de la syntaxe SQL.  

Testé avec <span class="tutorial_tested_on">backend SQL embarqué</span> <span class="tutorial_tested_on">macos</span> <span class="tutorial_tested_on">linux</span> <span class="tutorial_tested_on">powershell</span>  

## Requête de base {#basic-query}

La requête de base ci-dessous utilise la ressource [__`azure.monitor.activity_logs`__](https://azure.stackql.io/providers/azure/monitor/activity_logs/) du provider [__`azure`__](https://azure.stackql.io/providers/azure/) ; remplacez le `subscriptionId` par le vôtre.  Dans cette requête, nous utilisons `jsonnet` avec des variables externes pour le prétraitement de la requête StackQL ; pour plus d'informations, consultez [Utiliser des variables](/getting-started/variables).  Ici, nous l'avons fait avec un bloc de code `jsonnet` en ligne, mais ces données peuvent aussi être fournies dans un fichier externe à l'aide de l'argument `--iqldata` ou `-q` de `stackql exec` ou de `stackql shell`.  

Les fonctions [__`JSON_EXTRACT`__](/language-spec/functions/json/json_extract) et [__`SPLIT_PART`__](/language-spec/functions/string/split_part) servent à manipuler les données de la réponse.  

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

## Exécuter la requête de manière interactive {#running-the-query-interactively}

Depuis une machine authentifiée auprès d'Azure (y compris le Cloud Shell du portail Azure) :

```bash
# to run on mac or linux
./stackql shell --var startdate=$(date -u -d '1 day ago' '+%Y-%m-%dT%H:%M:%SZ')
```

ou avec PowerShell :

```powershell
# to run using windows powershell
$startDate = (Get-Date).AddDays(-1).ToString("yyyy-MM-ddTHH:mm:ssZ")
stackql.exe shell --var startdate=$startDate
```

Vous devriez voir une sortie tabulaire avec les colonnes suivantes : `event_timestamp`, `caller`, `ip_address`, `subscriptionId`, `tenantId`, `resource_group`, `resource_name`, `level`, `action`, `category`, `operation` et `status`.   

## Générer un rapport CSV {#generating-a-csv-report}

En enregistrant la requête StackQL dans un fichier (`activity_report.iql`), vous pouvez produire un rapport `csv` à partir de la requête d'activité comme suit (consultez [__`--output csv`__](/getting-started/output-modes#csv-output) pour plus d'informations sur les sorties de `stackql`) :

```bash
# to run on mac or linux
./stackql exec \
--output csv \
-i activity_report.iql \
-f activity_report.csv \
--var startdate=$(date -u -d '1 day ago' '+%Y-%m-%dT%H:%M:%SZ')
```

ou avec PowerShell :

```powershell
# to run using windows powershell
$startDate = (Get-Date).AddDays(-1).ToString("yyyy-MM-ddTHH:mm:ssZ")
stackql.exe exec `
--output csv `
-i activity_report.iql `
-f activity_report.csv `
--var startdate=$startDate
```

## Filtrage côté serveur avec KQL {#server-side-filtering-with-kql}

Le filtrage côté serveur dans Azure repose sur KQL (Kusto Query Language). En étendant la clause `$filter` de votre requête, vous pouvez filtrer les données directement sur le serveur. Cette méthode est très efficace, car elle réduit la quantité de données transférées sur le réseau.  Voici quelques exemples de filtrage côté serveur (par souci de concision, ces requêtes utilisent directement des paramètres plutôt que le préprocesseur jsonnet) :


### Indiquer une plage de dates pour les activités {#specifying-a-date-range-for-activities}

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

### Obtenir les activités d'un groupe de ressources {#getting-activities-for-a-resource-group}

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

### Obtenir les activités d'un type de ressource {#getting-activities-for-a-resource-type}

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

### Obtenir les activités d'une ressource précise {#getting-activities-for-a-specific-resource}

Notez que nous avons ajouté un champ supplémentaire (`correlationId`), que nous utiliserons dans le prochain filtre côté serveur...  

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

### Obtenir les activités d'un ID de corrélation {#getting-activities-for-a-specific-resource-1}

L'ID de corrélation est un identifiant unique qui sert à suivre et à regrouper les événements liés à une opération ou à une transaction donnée dans Azure. Cette requête aide à repérer, dans un intervalle de temps donné, les événements liés à une opération particulière, ce qui facilite le dépannage ou la compréhension de la séquence des activités.  Voici un exemple qui utilise un `correlationId` relevé dans la requête précédente :  

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

## Filtrage côté client avec la clause `WHERE` {#client-side-filtering-with-the-where-clause}

Le filtrage côté client se fait avec la clause SQL `WHERE` habituelle. Cette méthode filtre les données après leur récupération depuis le serveur, ce qui offre de la souplesse pour manipuler et analyser les données dans l'environnement StackQL.  

### Obtenir les activités d'un utilisateur précis {#getting-activities-for-a-specific-user}

Dans la requête d'exemple, la clause `WHERE` filtre les enregistrements selon le `caller` (l'utilisateur), ce qui ajoute un niveau supplémentaire d'affinage des données.  

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

## Exécuter des requêtes de synthèse ou d'agrégation {#running-summaryaggregate-queries}

Comme StackQL est un moteur SQL complet, les requêtes `GROUP BY` et d'agrégation (notamment [__`AVG`__](/language-spec/functions/aggregate/avg), [__`COUNT`__](/language-spec/functions/aggregate/count), [__`SUM`__](/language-spec/functions/aggregate/sum), [__`MIN`__](/language-spec/functions/aggregate/min), [__`MAX`__](/language-spec/functions/aggregate/max) et d'autres) sont disponibles nativement pour tous les providers, services et ressources.

### Compter les événements par niveau de gravité {#count-events-by-severity-level}

Dans cet exemple, nous regroupons par gravité les événements postérieurs à une date et heure de début et calculons le nombre d'événements pour chaque niveau.

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

## Utiliser `JOIN` pour enrichir les activités {#using-join-to-enrich-activities}

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

ou avec des variables issues de `jsonnet`, fournies dans un fichier à l'aide de l'argument `--iqldata` ou `-q`, ou dans un bloc jsonnet comme illustré ici :

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

Les paramètres `WHERE` d'une requête `JOIN` qui ne sont pas désambiguïsés, comme `subscriptionId` dans le cas ci-dessus, sont partagés par les deux ressources (`azure.monitor.activity_logs` et `azure.compute.virtual_machines` dans ce cas).

:::
