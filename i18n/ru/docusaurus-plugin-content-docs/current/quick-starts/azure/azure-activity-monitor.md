---
title: Мониторинг активности Azure
hide_title: true
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
  - cspm
  - azure
  - microsoft azure
description: "Запросы к журналам активности Azure Monitor в StackQL: фильтрация событий на сервере по времени, ресурсу или ID корреляции, подсчет по уровню серьезности и соединение с ВМ."
image: "/img/tutorials/azure/stackql-azure-provider-featured-image.png"
---

# Наблюдаемость активности Azure с помощью StackQL

В этом руководстве вы узнаете, как выполнять запросы к журналам активности Azure и формировать по ним отчеты с помощью StackQL - мощного инструмента разработчика, который позволяет выполнять запросы к облачной инфраструктуре и ресурсам и развертывать их с помощью синтаксиса SQL.  

Проверено с <span class="tutorial_tested_on">встроенный SQL-бэкенд</span> <span class="tutorial_tested_on">macos</span> <span class="tutorial_tested_on">linux</span> <span class="tutorial_tested_on">powershell</span>  

## Базовый запрос {#basic-query}

Приведенный ниже базовый запрос использует ресурс [__`azure.monitor.activity_logs`__](https://azure-provider.stackql.io/services/monitor/activity_logs/) провайдера [__`azure`__](https://azure-provider.stackql.io/); замените значение `subscriptionId` своим.  В этом запросе мы используем `jsonnet` с внешними переменными для предварительной обработки запроса StackQL, подробнее см. [Использование переменных](/getting-started/variables).  Здесь мы сделали это с помощью встроенного блока кода `jsonnet`, но эти данные также можно передать во внешнем файле с помощью аргумента `--iqldata` или `-q` команды `stackql exec` или `stackql shell`.  

Для обработки данных ответа используются функции [__`JSON_EXTRACT`__](/language-spec/functions/json/json_extract) и [__`SPLIT_PART`__](/language-spec/functions/string/split_part).  

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

## Интерактивный запуск запроса {#running-the-query-interactively}

На машине, прошедшей аутентификацию в Azure (в том числе в Azure Portal Cloud Shell):

```bash
# to run on mac or linux
./stackql shell --var startdate=$(date -u -d '1 day ago' '+%Y-%m-%dT%H:%M:%SZ')
```

или в PowerShell:

```powershell
# to run using windows powershell
$startDate = (Get-Date).AddDays(-1).ToString("yyyy-MM-ddTHH:mm:ssZ")
stackql.exe shell --var startdate=$startDate
```

Вы увидите вывод в виде таблицы со столбцами: `event_timestamp`, `caller`, `ip_address`, `subscriptionId`, `tenantId`, `resource_group`, `resource_name`, `level`, `action`, `category`, `operation` и `status`.   

## Формирование CSV-отчета {#generating-a-csv-report}

Сохранив запрос StackQL в файл (`activity_report.iql`), вы можете сформировать отчет в формате `csv` по запросу активности следующим образом (подробнее о выводе `stackql` см. [__`--output csv`__](/getting-started/output-modes#csv-output)):

```bash
# to run on mac or linux
./stackql exec \
--output csv \
-i activity_report.iql \
-f activity_report.csv \
--var startdate=$(date -u -d '1 day ago' '+%Y-%m-%dT%H:%M:%SZ')
```

или в PowerShell:

```powershell
# to run using windows powershell
$startDate = (Get-Date).AddDays(-1).ToString("yyyy-MM-ddTHH:mm:ssZ")
stackql.exe exec `
--output csv `
-i activity_report.iql `
-f activity_report.csv `
--var startdate=$startDate
```

## Фильтрация на стороне сервера с помощью KQL {#server-side-filtering-with-kql}

Фильтрация на стороне сервера в Azure выполняется с помощью KQL (Kusto Query Language). Расширив предложение `$filter` в запросе, вы можете фильтровать данные непосредственно на сервере. Этот способ очень эффективен, так как сокращает объем данных, передаваемых по сети.  Ниже приведено несколько примеров фильтрации на стороне сервера (для краткости в этих запросах параметры указаны напрямую, без препроцессора jsonnet):


### Указание диапазона дат для активности {#specifying-a-date-range-for-activities}

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

### Получение активности для группы ресурсов {#getting-activities-for-a-resource-group}

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

### Получение активности для типа ресурса {#getting-activities-for-a-resource-type}

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

### Получение активности для конкретного ресурса {#getting-activities-for-a-specific-resource}

Обратите внимание, что мы добавили дополнительное поле (`correlationId`), которое используем в следующем серверном фильтре...  

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

### Получение активности по идентификатору корреляции {#getting-activities-for-a-correlation-id}

Идентификатор корреляции - это уникальный идентификатор, который используется для отслеживания и группировки событий, связанных с определенной операцией или транзакцией в Azure. Этот запрос помогает точно найти события в заданном интервале времени, связанные с определенной операцией, что упрощает устранение неполадок и понимание последовательности действий.  Ниже приведен пример со значением `correlationId`, полученным в предыдущем запросе:  

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

## Фильтрация на стороне клиента с помощью предложения `WHERE` {#client-side-filtering-with-the-where-clause}

Фильтрацию на стороне клиента можно выполнять с помощью традиционного предложения SQL `WHERE`. Этот способ фильтрует данные после их получения с сервера и дает гибкость в обработке и анализе данных в среде StackQL.  

### Получение активности конкретного пользователя {#getting-activities-for-a-specific-user}

В этом примере запроса предложение `WHERE` фильтрует строки по значению `caller` (пользователь), что дополнительно уточняет данные.  

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

## Выполнение сводных и агрегатных запросов {#running-summaryaggregate-queries}

Поскольку StackQL - полноценный SQL-движок, запросы с `GROUP BY` и агрегатные запросы (включая [__`AVG`__](/language-spec/functions/aggregate/avg), [__`COUNT`__](/language-spec/functions/aggregate/count), [__`SUM`__](/language-spec/functions/aggregate/sum), [__`MIN`__](/language-spec/functions/aggregate/min), [__`MAX`__](/language-spec/functions/aggregate/max) и другие) изначально доступны для всех провайдеров, сервисов и ресурсов.

### Подсчет событий по уровню серьезности {#count-events-by-severity-level}

В этом примере мы сгруппируем события, начиная с заданных даты и времени, по уровню серьезности и подсчитаем их количество для каждого уровня.

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

## Использование `JOIN` для обогащения данных об активности {#using-join-to-enrich-activities}

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

или с использованием переменных из `jsonnet`, переданных в файле с помощью аргумента `--iqldata` или `-q` либо в виде блока jsonnet, как показано здесь:

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

Параметры `WHERE` в запросе с `JOIN`, для которых не указано, к какому ресурсу они относятся, например `subscription_id` в примере выше, используются обоими ресурсами (в этом случае `azure.monitor.activity_logs` и `azure.compute.virtual_machines`).

:::
