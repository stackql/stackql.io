---
title: EXEC
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の EXEC：インスタンスの起動や停止など、プロバイダーのリソースメソッドを直接呼び出し、クエリヒントで結果の返却や完了の待機を指定します。"
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

プロバイダーのリソースメソッド（組み込みプロシージャ）を実行します。  

関連項目：  
[[ StackQL リソース階層 ]](/getting-started/resource-hierarchy)

* * * 

## 構文 {#syntax}

*execStatement::=*

<RailroadDiagram 
type="exec"
/>

&nbsp;  
&nbsp;

```sql
EXEC [ queryHint ] <multipartIdentifier>.<methodName> [ <methodArguments> ];
```

## クエリヒント {#query-hints}

`EXEC` コマンドでサポートされているクエリヒントを以下にまとめます。  

| ヒント | 説明 |
|----------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `/*+ SHOWRESULTS */` | 結果セットを返します。データを返すメソッド向けですが、可能な場合は `SELECT` を使用してください。 |
| `/*+ AWAIT  */`      | 操作をブロッキング（同期）にし、長時間実行される操作（ミューテーション操作など）のスクリプト制御を提供します。注記：すべてのプロバイダーでサポートされているわけではありません |

* * *

## 例 {#examples}

### Google Compute Engine インスタンスを停止する {#stopping-a-google-compute-engine-instance}
認証済みのセッションで `EXEC` 文を実行し、実行中の Compute Engine インスタンスを停止します。

```sql
-- Stop a running Compute Engine resource instance
EXEC google.compute.instances.stop 
@instance = 'demo-instance-1', 
@project = 'stackql-demo', 
@zone = 'australia-southeast1-a';
```

### Google Compute Engine インスタンスを起動する（同期オプション） {#starting-a-google-compute-engine-instance-synchronous-option}
認証済みのセッションで `EXEC` 文を実行し、停止中の Compute Engine インスタンスを起動します。`/*+ AWAIT  */` クエリヒントを使用して、操作をブロッキング（同期）にします。

```sql
-- Start a stopped Compute Engine resource instance
EXEC /*+ AWAIT  */ google.compute.instances.start 
@instance = 'demo-instance-1', 
@project = 'stackql-demo', 
@zone = 'australia-southeast1-a';
```

### AWS EC2 インスタンスを起動および停止する {#starting-and-stopping-an-aws-ec2-instance}
認証済みのセッションで `EXEC` 文を実行し、AWS EC2 インスタンスを起動および停止します。

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

### Google Compute Engine インスタンスにディスクをアタッチする {#attach-a-disk-to-a-google-compute-engine-instance}
認証済みのセッションで `EXEC` 文を実行し、Compute Engine インスタンスにディスクをアタッチします。

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

### Azure Spot VM のエビクションをシミュレートする {#simulate-azure-spot-vm-eviction}
`EXEC` 文を実行し、Azure Spot VM インスタンスのエビクションをシミュレートします。

```sql
exec azure.compute.virtual_machines.simulate_eviction
@subscription_id = '0123456789',
@resource_group_name = 'vmss-flex',
@vm_name = 'vmss-flex-manual-1';
```
