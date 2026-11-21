---
title: EXEC
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 EXEC：直接呼叫 Provider 資源方法，可用查詢提示傳回結果或等待完成，例如啟動或停止執行個體。"
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

執行 Provider 資源方法（內建程序）。  

另請參閱：  
[[ StackQL 資源階層 ]](/getting-started/resource-hierarchy)

* * * 

## 語法 {#syntax}

*execStatement::=*

<RailroadDiagram 
type="exec"
/>

&nbsp;  
&nbsp;

```sql
EXEC [ queryHint ] <multipartIdentifier>.<methodName> [ <methodArguments> ];
```

## 查詢提示 {#query-hints}

`EXEC` 命令支援的查詢提示摘要如下：  

| 提示                 | 描述 |
|----------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `/*+ SHOWRESULTS */` | 傳回結果集，專為會傳回資料的方法設計，但若可行，請使用 `SELECT`。 |
| `/*+ AWAIT  */`      | 讓作業變成封鎖式（同步），為長時間執行的作業（例如變更作業）提供指令碼控制。注意：並非所有 Provider 都支援此功能 |

* * *

## 範例 {#examples}

### 停止 Google Compute Engine 執行個體 {#stopping-a-google-compute-engine-instance}
在已驗證的工作階段中執行 `EXEC` 陳述式，以停止執行中的 Compute Engine 執行個體。

```sql
-- Stop a running Compute Engine resource instance
EXEC google.compute.instances.stop 
@instance = 'demo-instance-1', 
@project = 'stackql-demo', 
@zone = 'australia-southeast1-a';
```

### 啟動 Google Compute Engine 執行個體（同步選項） {#starting-a-google-compute-engine-instance-synchronous-option}
在已驗證的工作階段中執行 `EXEC` 陳述式，以啟動已停止的 Compute Engine 執行個體，並使用 `/*+ AWAIT  */` 查詢提示讓作業變成封鎖式（同步）。

```sql
-- Start a stopped Compute Engine resource instance
EXEC /*+ AWAIT  */ google.compute.instances.start 
@instance = 'demo-instance-1', 
@project = 'stackql-demo', 
@zone = 'australia-southeast1-a';
```

### 啟動和停止 AWS EC2 執行個體 {#starting-and-stopping-an-aws-ec2-instance}
在已驗證的工作階段中執行 `EXEC` 陳述式，以啟動和停止 AWS EC2 執行個體。

```sql
-- Start an EC2 instance
EXEC aws.ec2.instances.start_instances 
@InstanceId = 'i-0fc989cf4efcd8b88', 
@region = 'ap-southeast-2';
-- Stop an EC2 instance
EXEC aws.ec2.instances.stop_instances 
@InstanceId = 'i-0fc989cf4efcd8b88', 
@region = 'ap-southeast-2';
```

### 將磁碟連結至 Google Compute Engine 執行個體 {#attach-a-disk-to-a-google-compute-engine-instance}
在已驗證的工作階段中執行 `EXEC` 陳述式，以將磁碟連結至 Compute Engine 執行個體。

```sql
-- Attach a Disk resource to a Compute Engine instance
EXEC google.compute.instances.attach_disk 
@zone='australia-southeast1-a', 
@project='stackql-demo', 
@instance='demo-instance-1' 
@@json=
'{
"source": "projects/stackql-demo/zones/australia-southeast1-a/disks/test10gbdisk2", 
"boot": false 
}';
```

### 模擬 Azure Spot VM 收回 {#simulate-azure-spot-vm-eviction}
執行 `EXEC` 陳述式，以模擬 Azure Spot VM 執行個體的收回。 

```sql
exec azure.compute.virtual_machines.simulate_eviction
@subscription_id = '0123456789',
@resource_group_name = 'vmss-flex',
@vm_name = 'vmss-flex-manual-1';
```
