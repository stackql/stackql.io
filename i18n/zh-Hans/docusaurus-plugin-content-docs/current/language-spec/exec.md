---
title: EXEC
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 EXEC：直接调用 Provider 资源方法（例如启动或停止实例），可使用查询提示返回结果或等待操作完成。"
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

执行 Provider 资源方法（内置过程）。  

另请参阅：  
[[ StackQL 资源层次结构 ]](/getting-started/resource-hierarchy)

* * * 

## 语法 {#syntax}

*execStatement::=*

<RailroadDiagram 
type="exec"
/>

&nbsp;  
&nbsp;

```sql
EXEC [ queryHint ] <multipartIdentifier>.<methodName> [ <methodArguments> ];
```

## 查询提示 {#query-hints}

`EXEC` 命令支持的查询提示汇总如下：  

| 提示                 | 说明 |
|----------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `/*+ SHOWRESULTS */` | 返回结果集，适用于返回数据的方法，但如果可能，请使用 `SELECT`。 |
| `/*+ AWAIT  */`      | 使操作变为阻塞（同步）操作，为长时间运行的操作（例如变更操作）提供脚本控制。注意：并非所有 Provider 都支持此功能 |

* * *

## 示例 {#examples}

### 停止 Google Compute Engine 实例 {#stopping-a-google-compute-engine-instance}
在已通过身份验证的会话中运行 `EXEC` 语句，停止正在运行的 Compute Engine 实例。

```sql
-- Stop a running Compute Engine resource instance
EXEC google.compute.instances.stop 
@instance = 'demo-instance-1', 
@project = 'stackql-demo', 
@zone = 'australia-southeast1-a';
```

### 启动 Google Compute Engine 实例（同步选项） {#starting-a-google-compute-engine-instance-synchronous-option}
在已通过身份验证的会话中运行 `EXEC` 语句，启动已停止的 Compute Engine 实例，并使用 `/*+ AWAIT  */` 查询提示使操作变为阻塞（同步）操作。

```sql
-- Start a stopped Compute Engine resource instance
EXEC /*+ AWAIT  */ google.compute.instances.stop 
@instance = 'demo-instance-1', 
@project = 'stackql-demo', 
@zone = 'australia-southeast1-a';
```

### 启动和停止 AWS EC2 实例 {#starting-and-stopping-an-aws-ec2-instance}
在已通过身份验证的会话中运行 `EXEC` 语句，启动 AWS EC2 实例。

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

### 将磁盘附加到 Google Compute Engine 实例 {#attach-a-disk-to-a-google-compute-engine-instance}
在已通过身份验证的会话中运行 `EXEC` 语句，将磁盘附加到 Compute Engine 实例。

```sql
-- Attach a Disk resource to a Compute Engine instance
EXEC google.compute.instances.attachDisk 
@zone='australia-southeast1-a', 
@project='stackql-demo', 
@instance='demo-instance-1' 
@@json=
'{
"source": "projects/stackql-demo/zones/australia-southeast1-a/disks/test10gbdisk2", 
"boot": false 
}';
```

### 模拟 Azure Spot VM 逐出 {#simulate-azure-spot-vm-eviction}
运行 `EXEC` 语句，模拟 Azure Spot VM 实例的逐出。 

```sql
exec azure.compute.virtual_machines.simulate_eviction
@subscription_id = '0123456789',
@resource_group_name = 'vmss-flex',
@vm_name = 'vmss-flex-manual-1';
```
