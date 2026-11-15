---
title: UPDATE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 UPDATE: 리소스 인스턴스의 지정된 필드를 변경하며 HTTP PATCH에 해당하고, 대상은 WHERE 절로 식별합니다."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

리소스의 지정된 필드를 업데이트합니다(`patch` 작업과 같은 의미).

참고 항목:  
[[ StackQL 리소스 계층 구조 ]](/getting-started/resource-hierarchy) [[ `REPLACE` ]](/language-spec/replace)

* * * 

## 구문 {#syntax}

*updateStatement::=*

<RailroadDiagram 
type="update"
/>

&nbsp;  
&nbsp;

```sql
UPDATE [ /*+ AWAIT  */ ] <multipartIdentifier>
SET field_name = value [, field_name = value ...]
[ WHERE condition ];
```

* * *

## 예제 {#examples}

### 기본 `UPDATE` 문 {#basic-update-statement}
`UPDATE` 문을 실행하여 Google Cloud Storage 버킷의 레이블을 설정합니다.

```sql
-- Set the labels on a Google Cloud Storage bucket
UPDATE google.storage.buckets
SET data__labels = '{"env": "prod", "team": "platform"}'
WHERE bucket = 'stackql-demo-bucket';
```

### `UPDATE` 문(블로킹) {#update-statement-blocking}
`UPDATE` 문을 실행하여 Compute Engine 방화벽 규칙을 수정합니다. 이는 블로킹(동기) 호출입니다.

```sql
-- Synchronous update of a google firewall 
UPDATE /*+ AWAIT */ google.compute.firewalls
SET
 data__network = 'https://www.googleapis.com/compute/v1/projects/stackql-demo/global/networks/my-vpc',
 data__direction = 'INGRESS',
 data__sourceRanges = '["0.0.0.0/0"]',
 data__allowed = '[{"IPProtocol": "tcp", "ports": ["22"]}, {"IPProtocol": "tcp", "ports": ["6443"]},{"IPProtocol": "icmp"}]'
WHERE firewall = 'test-fw'
AND project = 'stackql-demo';
```

### JSON 함수를 사용하는 `UPDATE` 문 {#update-statement-with-json-functions}
JSON 함수로 값을 구성하는 `UPDATE` 문을 실행합니다. 열을 읽지 않는 함수는 요청을 보내기 전에 평가됩니다. `SET` 값은 업데이트 대상 행을 읽을 수 없으므로, 기존 값의 일부를 변경하려면 먼저 `SELECT` 문으로 값을 읽은 다음 수정한 값을 설정하세요.

```sql
-- Set the protocols and ports a firewall rule allows
UPDATE google.compute.firewalls
SET data__allowed = json_array(
  json_object('IPProtocol', 'tcp', 'ports', json_array('22', '443')),
  json_object('IPProtocol', 'icmp')
)
WHERE firewall = 'test-fw'
AND project = 'stackql-demo';
```
