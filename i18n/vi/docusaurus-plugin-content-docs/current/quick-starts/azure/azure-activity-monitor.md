---
title: Giám sát hoạt động Azure
hide_title: true
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
  - cspm
  - azure
  - microsoft azure
description: "Truy vấn nhật ký hoạt động Azure Monitor bằng StackQL: lọc sự kiện phía máy chủ theo thời gian, tài nguyên hoặc ID tương quan, đếm theo mức độ nghiêm trọng và nối với máy ảo."
image: "/img/tutorials/azure/stackql-azure-provider-featured-image.png"
---

# Khả năng quan sát hoạt động Azure bằng StackQL

Trong hướng dẫn này, bạn sẽ tìm hiểu cách truy vấn và lập báo cáo nhật ký hoạt động Azure bằng StackQL, một công cụ phát triển mạnh mẽ cho phép truy vấn và triển khai hạ tầng và tài nguyên đám mây bằng cú pháp SQL.

Đã kiểm thử với <span class="tutorial_tested_on">backend SQL nhúng</span> <span class="tutorial_tested_on">macos</span> <span class="tutorial_tested_on">linux</span> <span class="tutorial_tested_on">powershell</span>

## Truy vấn cơ bản {#basic-query}

Truy vấn cơ bản dưới đây sử dụng tài nguyên [__`azure.monitor.activity_logs`__](https://azure-provider.stackql.io/services/monitor/activity_logs/) trong provider [__`azure`__](https://azure-provider.stackql.io/); thay `subscriptionId` bằng giá trị của bạn.  Trong truy vấn này, chúng ta sẽ dùng `jsonnet` với các biến bên ngoài để tiền xử lý truy vấn StackQL, xem [Sử dụng biến](/getting-started/variables) để biết thêm thông tin.  Trong trường hợp này, chúng ta đã viết nó dưới dạng một khối mã `jsonnet` nội tuyến, nhưng bạn cũng có thể cung cấp nó dưới dạng một tệp bên ngoài bằng đối số `--iqldata` hoặc `-q` của lệnh `stackql exec` hoặc `stackql shell`.

Hàm [__`JSON_EXTRACT`__](/language-spec/functions/json/json_extract) và [__`SPLIT_PART`__](/language-spec/functions/string/split_part) được dùng để xử lý dữ liệu phản hồi.

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

## Chạy truy vấn ở chế độ tương tác {#running-the-query-interactively}

Từ một máy đã được xác thực với Azure (bao gồm cả Azure Portal Cloud Shell):

```bash
# to run on mac or linux
./stackql shell --var startdate=$(date -u -d '1 day ago' '+%Y-%m-%dT%H:%M:%SZ')
```

hoặc dùng PowerShell:

```powershell
# to run using windows powershell
$startDate = (Get-Date).AddDays(-1).ToString("yyyy-MM-ddTHH:mm:ssZ")
stackql.exe shell --var startdate=$startDate
```

Bạn sẽ thấy đầu ra dạng bảng với các cột: `event_timestamp`, `caller`, `ip_address`, `subscriptionId`, `tenantId`, `resource_group`, `resource_name`, `level`, `action`, `category`, `operation` và `status`.

## Tạo báo cáo CSV {#generating-a-csv-report}

Bằng cách lưu truy vấn StackQL vào một tệp (`activity_report.iql`), bạn có thể tạo báo cáo `csv` từ truy vấn hoạt động như sau (xem [__`--output csv`__](/getting-started/output-modes#csv-output) để biết thêm thông tin về đầu ra của `stackql`):

```bash
# to run on mac or linux
./stackql exec \
--output csv \
-i activity_report.iql \
-f activity_report.csv \
--var startdate=$(date -u -d '1 day ago' '+%Y-%m-%dT%H:%M:%SZ')
```

hoặc dùng PowerShell:

```powershell
# to run using windows powershell
$startDate = (Get-Date).AddDays(-1).ToString("yyyy-MM-ddTHH:mm:ssZ")
stackql.exe exec `
--output csv `
-i activity_report.iql `
-f activity_report.csv `
--var startdate=$startDate
```

## Lọc phía máy chủ bằng KQL {#server-side-filtering-with-kql}

Việc lọc phía máy chủ trong Azure được thực hiện thông qua KQL (Kusto Query Language). Bằng cách mở rộng mệnh đề `$filter` trong truy vấn, bạn có thể lọc dữ liệu trực tiếp trên máy chủ. Cách này rất hiệu quả vì nó giảm lượng dữ liệu được truyền qua mạng.  Dưới đây là một số ví dụ về lọc phía máy chủ (để ngắn gọn, các truy vấn này dùng trực tiếp tham số thay vì dùng bộ tiền xử lý jsonnet):


### Chỉ định khoảng ngày cho hoạt động {#specifying-a-date-range-for-activities}

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

### Lấy hoạt động của một nhóm tài nguyên {#getting-activities-for-a-resource-group}

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

### Lấy hoạt động của một loại tài nguyên {#getting-activities-for-a-resource-type}

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

### Lấy hoạt động của một tài nguyên cụ thể {#getting-activities-for-a-specific-resource}

Lưu ý rằng chúng ta đã thêm một trường bổ sung (`correlationId`) mà chúng ta sẽ dùng trong bộ lọc phía máy chủ tiếp theo...

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

### Lấy hoạt động theo ID tương quan {#getting-activities-for-a-correlation-id}

ID tương quan là một định danh duy nhất dùng để lần theo và nhóm các sự kiện liên quan đến một thao tác hoặc giao dịch cụ thể trong Azure. Truy vấn này giúp xác định chính xác các sự kiện cụ thể trong một khoảng thời gian nhất định có liên quan đến một thao tác cụ thể, giúp việc khắc phục sự cố hoặc hiểu trình tự các hoạt động trở nên dễ dàng hơn.  Dưới đây là một ví dụ sử dụng `correlationId` thu được trong truy vấn trước:

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

## Lọc phía ứng dụng khách bằng mệnh đề `WHERE` {#client-side-filtering-with-the-where-clause}

Có thể lọc phía ứng dụng khách bằng mệnh đề `WHERE` truyền thống của SQL. Cách này lọc dữ liệu sau khi dữ liệu đã được lấy từ máy chủ, mang lại sự linh hoạt trong việc xử lý và phân tích dữ liệu trong môi trường StackQL.

### Lấy hoạt động của một người dùng cụ thể {#getting-activities-for-a-specific-user}

Trong truy vấn ví dụ, mệnh đề `WHERE` lọc các bản ghi dựa trên `caller` (người dùng), bổ sung thêm một lớp tinh lọc dữ liệu.

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

## Chạy truy vấn tóm tắt/tổng hợp {#running-summaryaggregate-queries}

Vì StackQL là một công cụ SQL hoàn chỉnh, các truy vấn `GROUP BY`/tổng hợp (bao gồm [__`AVG`__](/language-spec/functions/aggregate/avg), [__`COUNT`__](/language-spec/functions/aggregate/count), [__`SUM`__](/language-spec/functions/aggregate/sum), [__`MIN`__](/language-spec/functions/aggregate/min), [__`MAX`__](/language-spec/functions/aggregate/max) và nhiều hàm khác) đều được hỗ trợ nguyên bản cho mọi provider, dịch vụ và tài nguyên.

### Đếm sự kiện theo mức độ nghiêm trọng {#count-events-by-severity-level}

Trong ví dụ này, chúng ta sẽ nhóm các sự kiện kể từ một thời điểm bắt đầu theo mức độ nghiêm trọng và tính số lượng sự kiện cho mỗi mức.

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

## Sử dụng `JOIN` để bổ sung thông tin cho hoạt động {#using-join-to-enrich-activities}

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

hoặc dùng biến từ `jsonnet`, được cung cấp dưới dạng tệp bằng đối số `--iqldata` hoặc `-q`, hoặc dưới dạng khối jsonnet như minh họa dưới đây:

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

Các tham số `WHERE` không được phân biệt rõ trong một truy vấn `JOIN`, chẳng hạn như `subscription_id` trong trường hợp trên, được dùng chung cho cả hai tài nguyên (trong trường hợp này là `azure.monitor.activity_logs` và `azure.compute.virtual_machines`).

:::
