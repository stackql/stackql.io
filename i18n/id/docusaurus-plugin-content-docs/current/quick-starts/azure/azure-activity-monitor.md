---
title: Memantau aktivitas Azure
hide_title: true
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
  - cspm
  - azure
  - microsoft azure
description: "Mengkueri log aktivitas Azure Monitor dengan StackQL: filter di sisi server menurut waktu, sumber daya, atau ID korelasi, penghitungan per keparahan, dan join dengan VM."
image: "/img/tutorials/azure/stackql-azure-provider-featured-image.png"
---

# Observabilitas aktivitas Azure menggunakan StackQL

Dalam panduan ini, Anda akan mempelajari cara mengkueri dan membuat laporan log aktivitas Azure menggunakan StackQL, alat pengembang yang andal yang memungkinkan Anda mengkueri dan men-deploy infrastruktur serta sumber daya cloud menggunakan sintaksis SQL.  

Diuji dengan <span class="tutorial_tested_on">backend SQL tersemat</span> <span class="tutorial_tested_on">macos</span> <span class="tutorial_tested_on">linux</span> <span class="tutorial_tested_on">powershell</span>  

## Kueri dasar {#basic-query}

Kueri dasar di bawah ini menggunakan sumber daya [__`azure.monitor.activity_logs`__](https://azure-provider.stackql.io/services/monitor/activity_logs/) di provider [__`azure`__](https://azure-provider.stackql.io/); ganti nilai `subscriptionId` dengan milik Anda.  Dalam kueri ini, kita akan menggunakan `jsonnet` dengan variabel eksternal untuk prapemrosesan kueri StackQL, lihat [Menggunakan variabel](/getting-started/variables) untuk informasi selengkapnya.  Dalam kasus ini, kita melakukannya sebagai blok kode `jsonnet` sebaris, tetapi variabel ini juga dapat diberikan sebagai file eksternal menggunakan argumen `--iqldata` atau `-q` pada perintah `stackql exec` atau `stackql shell`.  

Fungsi [__`JSON_EXTRACT`__](/language-spec/functions/json/json_extract) dan [__`SPLIT_PART`__](/language-spec/functions/string/split_part) digunakan untuk memanipulasi data respons.  

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

## Menjalankan kueri secara interaktif {#running-the-query-interactively}

Dari mesin yang terautentikasi ke Azure (termasuk Azure Portal Cloud Shell):

```bash
# to run on mac or linux
./stackql shell --var startdate=$(date -u -d '1 day ago' '+%Y-%m-%dT%H:%M:%SZ')
```

atau menggunakan PowerShell:

```powershell
# to run using windows powershell
$startDate = (Get-Date).AddDays(-1).ToString("yyyy-MM-ddTHH:mm:ssZ")
stackql.exe shell --var startdate=$startDate
```

Anda akan melihat output berbentuk tabel dengan kolom: `event_timestamp`, `caller`, `ip_address`, `subscriptionId`, `tenantId`, `resource_group`, `resource_name`, `level`, `action`, `category`, `operation`, dan `status`.   

## Membuat laporan CSV {#generating-a-csv-report}

Dengan menyimpan kueri StackQL ke file (`activity_report.iql`), Anda dapat menghasilkan laporan `csv` dari kueri aktivitas sebagai berikut (lihat [__`--output csv`__](/getting-started/output-modes#csv-output) untuk informasi selengkapnya tentang output `stackql`):

```bash
# to run on mac or linux
./stackql exec \
--output csv \
-i activity_report.iql \
-f activity_report.csv \
--var startdate=$(date -u -d '1 day ago' '+%Y-%m-%dT%H:%M:%SZ')
```

atau menggunakan PowerShell:

```powershell
# to run using windows powershell
$startDate = (Get-Date).AddDays(-1).ToString("yyyy-MM-ddTHH:mm:ssZ")
stackql.exe exec `
--output csv `
-i activity_report.iql `
-f activity_report.csv `
--var startdate=$startDate
```

## Pemfilteran di sisi server dengan KQL {#server-side-filtering-with-kql}

Pemfilteran di sisi server di Azure dilakukan dengan menggunakan KQL (Kusto Query Language). Dengan memperluas klausa `$filter` di kueri Anda, Anda dapat memfilter data langsung di server. Metode ini sangat efisien karena mengurangi jumlah data yang ditransfer melalui jaringan.  Berikut beberapa contoh pemfilteran di sisi server (agar ringkas, kueri ini menggunakan parameter secara langsung alih-alih menggunakan praprosesor jsonnet):


### Menentukan rentang tanggal untuk aktivitas {#specifying-a-date-range-for-activities}

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

### Mendapatkan aktivitas untuk grup sumber daya {#getting-activities-for-a-resource-group}

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

### Mendapatkan aktivitas untuk tipe sumber daya {#getting-activities-for-a-resource-type}

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

### Mendapatkan aktivitas untuk sumber daya tertentu {#getting-activities-for-a-specific-resource}

Perhatikan bahwa kita menambahkan field tambahan (`correlationId`) yang akan kita gunakan di filter sisi server berikutnya...  

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

### Mendapatkan aktivitas untuk ID korelasi {#getting-activities-for-a-correlation-id}

ID korelasi adalah pengidentifikasi unik yang digunakan untuk melacak dan mengelompokkan peristiwa yang terkait dengan operasi atau transaksi tertentu di Azure. Kueri ini membantu menemukan peristiwa spesifik dalam rentang waktu tertentu yang terkait dengan suatu operasi, sehingga memudahkan pemecahan masalah atau pemahaman urutan aktivitas.  Berikut contoh yang menggunakan `correlationId` yang diperoleh dari kueri sebelumnya:  

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

## Pemfilteran di sisi klien dengan klausa `WHERE` {#client-side-filtering-with-the-where-clause}

Pemfilteran di sisi klien dapat dilakukan menggunakan klausa `WHERE` SQL tradisional. Metode ini memfilter data setelah data diambil dari server, sehingga memberikan fleksibilitas dalam manipulasi dan analisis data di dalam lingkungan StackQL.  

### Mendapatkan aktivitas untuk pengguna tertentu {#getting-activities-for-a-specific-user}

Dalam contoh kueri ini, klausa `WHERE` memfilter baris berdasarkan kolom `caller` (pengguna), sehingga memberikan lapisan penyempurnaan data tambahan.  

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

## Menjalankan kueri ringkasan/agregat {#running-summaryaggregate-queries}

Karena StackQL adalah mesin SQL yang lengkap, kueri `GROUP BY`/agregat (termasuk [__`AVG`__](/language-spec/functions/aggregate/avg), [__`COUNT`__](/language-spec/functions/aggregate/count), [__`SUM`__](/language-spec/functions/aggregate/sum), [__`MIN`__](/language-spec/functions/aggregate/min), [__`MAX`__](/language-spec/functions/aggregate/max), dan lainnya) tersedia secara bawaan untuk semua provider, layanan, dan sumber daya.

### Menghitung peristiwa per tingkat keparahan {#count-events-by-severity-level}

Dalam contoh ini, kita akan mengelompokkan peristiwa sejak tanggal dan waktu mulai tertentu berdasarkan tingkat keparahannya dan menghitung jumlah untuk setiap tingkat.

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

## Menggunakan `JOIN` untuk memperkaya aktivitas {#using-join-to-enrich-activities}

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

atau menggunakan variabel dari `jsonnet`, yang diberikan sebagai file menggunakan argumen `--iqldata` atau `-q`, atau sebagai blok jsonnet seperti yang ditunjukkan di sini:

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

Parameter `WHERE` dalam kueri `JOIN` yang tidak didisambiguasi, seperti `subscription_id` dalam kasus di atas, digunakan bersama oleh kedua sumber daya (dalam kasus ini `azure.monitor.activity_logs` dan `azure.compute.virtual_machines`).

:::
