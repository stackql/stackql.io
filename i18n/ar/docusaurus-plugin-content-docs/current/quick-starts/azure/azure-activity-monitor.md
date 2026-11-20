---
title: مراقبة نشاط Azure
hide_title: true
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
  - cspm
  - azure
  - microsoft azure
description: "الاستعلام عن سجلات نشاط Azure Monitor باستخدام StackQL: تصفية الأحداث على الخادم حسب الوقت أو المورد أو معرف الارتباط، وإحصاؤها حسب الخطورة، وربطها بالأجهزة الظاهرية."
image: "/img/tutorials/azure/stackql-azure-provider-featured-image.png"
---

# قابلية مراقبة نشاط Azure باستخدام StackQL

ستتعلم في هذا الدليل كيفية الاستعلام عن سجلات نشاط Azure وإعداد تقارير عنها باستخدام StackQL، وهي أداة تطوير قوية تتيح الاستعلام عن البنية التحتية والموارد السحابية ونشرها باستخدام بناء جملة SQL.

تم الاختبار باستخدام <span class="tutorial_tested_on">واجهة SQL الخلفية المضمنة</span> <span class="tutorial_tested_on">macos</span> <span class="tutorial_tested_on">linux</span> <span class="tutorial_tested_on">powershell</span>

## استعلام أساسي {#basic-query}

يستخدم الاستعلام الأساسي الموضح أدناه المورد [__`azure.monitor.activity_logs`__](https://azure-provider.stackql.io/services/monitor/activity_logs/) في الموفر [__`azure`__](https://azure-provider.stackql.io/)؛ استبدل قيمة `subscriptionId` بقيمتك. سنستخدم في هذا الاستعلام `jsonnet` مع متغيرات خارجية للمعالجة المسبقة لاستعلام StackQL، راجع [استخدام المتغيرات](/getting-started/variables) لمزيد من المعلومات. في هذه الحالة كتبنا ذلك ككتلة تعليمات برمجية `jsonnet` داخل الاستعلام نفسه، ولكن يمكن أيضًا توفيرها كملف خارجي باستخدام الوسيطة `--iqldata` أو `-q` للأمر `stackql exec` أو `stackql shell`.

يتم استخدام الدالتين [__`JSON_EXTRACT`__](/language-spec/functions/json/json_extract) و[__`SPLIT_PART`__](/language-spec/functions/string/split_part) لمعالجة بيانات الاستجابة.

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

## تشغيل الاستعلام بشكل تفاعلي {#running-the-query-interactively}

من جهاز تمت مصادقته لدى Azure (بما في ذلك Azure Portal Cloud Shell):

```bash
# to run on mac or linux
./stackql shell --var startdate=$(date -u -d '1 day ago' '+%Y-%m-%dT%H:%M:%SZ')
```

أو باستخدام PowerShell:

```powershell
# to run using windows powershell
$startDate = (Get-Date).AddDays(-1).ToString("yyyy-MM-ddTHH:mm:ssZ")
stackql.exe shell --var startdate=$startDate
```

يجب أن يظهر لك إخراج جدولي يحتوي على أعمدة لكل من: `event_timestamp` و`caller` و`ip_address` و`subscriptionId` و`tenantId` و`resource_group` و`resource_name` و`level` و`action` و`category` و`operation` و`status`.

## إنشاء تقرير CSV {#generating-a-csv-report}

بحفظ استعلام StackQL في ملف (`activity_report.iql`)، يمكنك إنشاء تقرير `csv` من استعلام النشاط على النحو التالي (راجع [__`--output csv`__](/getting-started/output-modes#csv-output) لمزيد من المعلومات حول أنواع الإخراج في `stackql`):

```bash
# to run on mac or linux
./stackql exec \
--output csv \
-i activity_report.iql \
-f activity_report.csv \
--var startdate=$(date -u -d '1 day ago' '+%Y-%m-%dT%H:%M:%SZ')
```

أو باستخدام PowerShell:

```powershell
# to run using windows powershell
$startDate = (Get-Date).AddDays(-1).ToString("yyyy-MM-ddTHH:mm:ssZ")
stackql.exe exec `
--output csv `
-i activity_report.iql `
-f activity_report.csv `
--var startdate=$startDate
```

## التصفية على جانب الخادم باستخدام KQL {#server-side-filtering-with-kql}

تتم التصفية على جانب الخادم في Azure باستخدام KQL (Kusto Query Language). بتوسيع العبارة `$filter` في استعلامك، يمكنك تصفية البيانات مباشرة على الخادم. يتميز ذلك بكفاءة عالية لأنه يقلل كمية البيانات المنقولة عبر الشبكة. فيما يلي بعض الأمثلة على التصفية على جانب الخادم (للاختصار، تستخدم هذه الاستعلامات المعلمات مباشرة بدلًا من استخدام المعالج المسبق jsonnet):


### تحديد نطاق زمني للأنشطة {#specifying-a-date-range-for-activities}

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

### الحصول على الأنشطة الخاصة بمجموعة موارد {#getting-activities-for-a-resource-group}

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

### الحصول على الأنشطة الخاصة بنوع مورد {#getting-activities-for-a-resource-type}

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

### الحصول على الأنشطة الخاصة بمورد محدد {#getting-activities-for-a-specific-resource}

لاحظ أننا أضفنا الحقل الإضافي (`correlationId`) الذي سنستخدمه في المرشح التالي على جانب الخادم...

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

### الحصول على الأنشطة الخاصة بمعرف ارتباط {#getting-activities-for-a-correlation-id}

معرف الارتباط هو معرف فريد مخصص لتتبع الأحداث المرتبطة بعملية أو معاملة معينة في Azure وتجميعها. يساعد هذا الاستعلام في تحديد أحداث معينة ضمن إطار زمني محدد ترتبط بعملية معينة، مما يسهل استكشاف الأخطاء وإصلاحها أو فهم تسلسل الأنشطة. إليك مثالًا يستخدم قيمة `correlationId` التي حصلنا عليها في الاستعلام السابق:

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

## التصفية على جانب العميل باستخدام العبارة `WHERE` {#client-side-filtering-with-the-where-clause}

يمكن إجراء التصفية على جانب العميل باستخدام عبارة SQL التقليدية `WHERE`. في هذه الحالة تتم تصفية البيانات بعد استردادها من الخادم، مما يوفر مرونة في معالجة البيانات وتحليلها داخل بيئة StackQL.

### الحصول على الأنشطة الخاصة بمستخدم محدد {#getting-activities-for-a-specific-user}

في استعلام المثال، تصفي العبارة `WHERE` الصفوف حسب الحقل `caller` (المستخدم)، مما يوفر طبقة إضافية لتنقيح البيانات.

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

## تشغيل استعلامات التلخيص/التجميع {#running-summaryaggregate-queries}

بما أن StackQL محرك SQL كامل، فإن استعلامات `GROUP BY` والاستعلامات التجميعية (بما في ذلك [__`AVG`__](/language-spec/functions/aggregate/avg) و[__`COUNT`__](/language-spec/functions/aggregate/count) و[__`SUM`__](/language-spec/functions/aggregate/sum) و[__`MIN`__](/language-spec/functions/aggregate/min) و[__`MAX`__](/language-spec/functions/aggregate/max) وغيرها) متاحة بشكل أصلي لجميع الموفرين والخدمات والموارد.

### حساب عدد الأحداث حسب مستوى الخطورة {#count-events-by-severity-level}

في هذا المثال، سنجمع الأحداث التي وقعت منذ تاريخ ووقت بدء محددين في مجموعات حسب مستوى خطورتها، ونحسب عدد الأحداث في كل مستوى.

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

## استخدام الربط `JOIN` لإثراء الأنشطة {#using-join-to-enrich-activities}

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

أو باستخدام متغيرات من `jsonnet` يتم توفيرها كملف باستخدام الوسيطة `--iqldata` أو `-q`، أو ككتلة jsonnet كما هو موضح هنا:

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

تكون معلمات `WHERE` غير المنسوبة إلى مورد معين في استعلام `JOIN`، مثل `subscription_id` في الحالة أعلاه، مشتركة بين الموردين كليهما (`azure.monitor.activity_logs` و`azure.compute.virtual_machines` في هذه الحالة).

:::
