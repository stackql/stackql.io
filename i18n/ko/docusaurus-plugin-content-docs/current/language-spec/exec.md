---
title: EXEC
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 EXEC: 인스턴스 시작이나 중지처럼 프로바이더 리소스 메서드를 직접 호출하며, 쿼리 힌트로 결과를 반환하거나 완료를 기다립니다."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

프로바이더 리소스 메서드(기본 제공 프로시저)를 실행합니다.

참고 항목:  
[[ StackQL 리소스 계층 구조 ]](/getting-started/resource-hierarchy)

* * * 

## 구문 {#syntax}

*execStatement::=*

<RailroadDiagram 
type="exec"
/>

&nbsp;  
&nbsp;

```sql
EXEC [ queryHint ] <multipartIdentifier>.<methodName> [ <methodArguments> ];
```

## 쿼리 힌트 {#query-hints}

`EXEC` 명령에서 지원되는 쿼리 힌트는 아래와 같습니다.

| 힌트 | 설명 |
|----------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `/*+ SHOWRESULTS */` | 결과 집합을 반환합니다. 데이터를 반환하는 메서드용으로 설계되었지만, 가능하면 `SELECT` 문을 사용하세요. |
| `/*+ AWAIT  */` | 작업을 블로킹(동기) 방식으로 만들어 장기 실행 작업(예: 변경 작업)에 대한 스크립트 제어를 제공합니다. 참고: 모든 프로바이더에서 지원되는 것은 아닙니다 |

* * *

## 예제 {#examples}

### Google Compute Engine 인스턴스 중지 {#stopping-a-google-compute-engine-instance}
인증된 세션에서 `EXEC` 문을 실행하여 실행 중인 Compute Engine 인스턴스를 중지합니다.

```sql
-- Stop a running Compute Engine resource instance
EXEC google.compute.instances.stop 
@instance = 'demo-instance-1', 
@project = 'stackql-demo', 
@zone = 'australia-southeast1-a';
```

### Google Compute Engine 인스턴스 시작(동기 옵션) {#starting-a-google-compute-engine-instance-synchronous-option}
인증된 세션에서 `EXEC` 문을 실행하여 중지된 Compute Engine 인스턴스를 시작합니다. `/*+ AWAIT  */` 쿼리 힌트를 사용하여 작업을 블로킹(동기) 방식으로 만듭니다.

```sql
-- Start a stopped Compute Engine resource instance
EXEC /*+ AWAIT  */ google.compute.instances.start 
@instance = 'demo-instance-1', 
@project = 'stackql-demo', 
@zone = 'australia-southeast1-a';
```

### AWS EC2 인스턴스 시작 및 중지 {#starting-and-stopping-an-aws-ec2-instance}
인증된 세션에서 `EXEC` 문을 실행하여 AWS EC2 인스턴스를 시작합니다.

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

### Google Compute Engine 인스턴스에 디스크 연결 {#attach-a-disk-to-a-google-compute-engine-instance}
인증된 세션에서 `EXEC` 문을 실행하여 Compute Engine 인스턴스에 디스크를 연결합니다.

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

### Azure Spot VM 제거 시뮬레이션 {#simulate-azure-spot-vm-eviction}
`EXEC` 문을 실행하여 Azure Spot VM 인스턴스의 제거를 시뮬레이션합니다.

```sql
exec azure.compute.virtual_machines.simulate_eviction
@subscription_id = '0123456789',
@resource_group_name = 'vmss-flex',
@vm_name = 'vmss-flex-manual-1';
```
