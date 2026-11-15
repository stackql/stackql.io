---
title: REPLACE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 REPLACE: UPDATE 문이 수행하는 부분 업데이트와 달리, HTTP PUT과 동일하게 리소스 인스턴스의 모든 필드를 대체합니다."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

리소스의 모든 필드를 업데이트합니다(`put` 작업과 동일).

참고 항목:  
[[ StackQL 리소스 계층 구조 ]](/getting-started/resource-hierarchy) [[ `UPDATE` ]](/language-spec/update)

* * * 

## 구문 {#syntax}

*replaceStatement::=*

<RailroadDiagram 
type="replace"
/>

&nbsp;  
&nbsp;

```sql
REPLACE [ /*+ AWAIT  */ ] <multipartIdentifier>
SET field_name = value [, field_name = value ...]
[ WHERE condition ];
```

* * *

## 예제 {#examples}

### 기본 `REPLACE` 문 {#basic-replace-statement}
`REPLACE` 문을 실행하여 GitHub 리포지토리의 토픽을 모두 교체합니다.

```sql
-- Replace all of the topics on a GitHub repository
REPLACE github.repos.topics
SET data__names = '["sql", "cloud-infrastructure"]'
WHERE owner = 'my-org'
AND repo = 'my-repo';
```

### `REPLACE` 문(블로킹) {#replace-statement-blocking}
`REPLACE` 문을 실행하여 Compute Engine 방화벽 규칙을 교체합니다. 이 호출은 블로킹(동기) 호출입니다.

```sql
-- Synchronous update of a google firewall 
REPLACE /*+ AWAIT */ google.compute.firewalls
SET
 data__name = 'test-fw',
 data__network = 'https://www.googleapis.com/compute/v1/projects/stackql-demo/global/networks/my-vpc',
 data__direction = 'INGRESS',
 data__sourceRanges = '["0.0.0.0/0"]',
 data__allowed = '[{"IPProtocol": "tcp", "ports": ["22"]}, {"IPProtocol": "tcp", "ports": ["6443"]},{"IPProtocol": "icmp"}]'
WHERE firewall = 'test-fw'
AND project = 'stackql-demo';
```

### JSON 함수를 사용하는 `REPLACE` 문 {#replace-statement-with-json-functions}
JSON 함수로 값을 구성하는 `REPLACE` 문을 실행합니다. 열을 읽지 않는 함수는 요청을 보내기 전에 평가됩니다. `SET` 값은 교체 대상 행을 읽을 수 없으므로, 기존 값의 일부를 변경하려면 먼저 `SELECT` 문으로 값을 읽은 다음 수정한 값을 설정하세요.

```sql
-- Replace the topics on a GitHub repository with a list built by json_array
REPLACE github.repos.topics
SET data__names = json_array('sql', 'cloud-infrastructure', 'infrastructure-as-code')
WHERE owner = 'my-org'
AND repo = 'my-repo';
```
