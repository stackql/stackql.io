---
title: DESCRIBE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 DESCRIBE: 쿼리하거나 변경하기 전에 리소스의 필드 또는 특정 메서드의 매개 변수와 반환 형식을 나열합니다."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';
import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

리소스 또는 리소스의 특정 메서드를 설명합니다.

참고 항목:
[[ StackQL 리소스 계층 구조 ]](/getting-started/resource-hierarchy)

* * *

## 구문 {#syntax}

*describeStatement::=*

<RailroadDiagram
type="describe"
/>

&nbsp;
&nbsp;

```sql
DESCRIBE [ METHOD ] [ EXTENDED ] <multipartIdentifier> ;
DESC [ EXTENDED ] <multipartIdentifier> ;
```

리소스를 설명할 때는 `DESC` 키워드를 `DESCRIBE` 키워드의 별칭으로 사용할 수 있습니다. `DESCRIBE METHOD` 문에서는 전체 키워드를 사용해야 합니다.

## 응답 {#response}

<Tabs
  defaultValue="describe-resource"
  values={[
    { label: 'DESCRIBE', value: 'describe-resource', },
    { label: 'DESCRIBE METHOD', value: 'describe-method', },
  ]
}>
<TabItem value="describe-resource">

| 열 | 설명 |
| --- | --- |
| `name` | 필드 이름 |
| `type` | 데이터 형식(`string`, `integer`, `boolean`, `number`, `object`, `array`) |
| `description` | 필드 설명(`EXTENDED` 키워드를 지정한 경우) |

</TabItem>
<TabItem value="describe-method">

| 열 | 설명 |
| --- | --- |
| `name` | 필드 이름 |
| `type` | 데이터 형식(`string`, `integer`, `boolean`, `number`, `object`, `array`) |
| `param_type` | `input_required`, `input_optional` 또는 `output` |
| `shape` | 객체 및 배열 필드의 JSON Schema 하위 집합이며, 스칼라 필드에서는 비어 있습니다. 중첩된 `properties`, `items`, `required`, `enum`, `default`, `description` 키를 포함하며, 프로바이더가 제공하는 경우 부울 값(`readOnly`, `writeOnly`, `deprecated`)도 선택적으로 포함합니다 |
| `description` | 필드 설명(`EXTENDED` 키워드를 지정한 경우) |

</TabItem>
</Tabs>

* * *

## 예제 {#examples}

### 기본 `DESCRIBE` 문 {#basic-describe-statement}
인증된 세션에서 기본 `DESCRIBE` 문을 실행하여 리소스의 필드를 나열합니다.

```sql
-- Show the available fields in a Compute Engine resource
DESCRIBE google.compute.instances;
```

### 확장 `DESCRIBE` 문 {#extended-describe-statement}
인증된 세션에서 확장 `DESCRIBE` 문을 실행하여 리소스의 필드와 각 필드의 설명을 나열합니다.

```sql
-- Show the available fields in a Compute Engine resource
DESCRIBE EXTENDED google.compute.instances;
```

### `DESC` 별칭 {#desc-alias}
`DESC` 키워드는 리소스에 대한 `DESCRIBE` 키워드의 약식 표현이며, `EXTENDED` 키워드 유무와 관계없이 사용할 수 있습니다. `DESCRIBE` 문이 읽기 전용 문으로 처리되는 모든 곳에서 이 별칭도 읽기 전용 문으로 처리되며, 여기에는 `read_only` 및 `safe` 모드에서 MCP 서버의 `run_select_query` 도구가 포함됩니다.

```sql
-- Same result as DESCRIBE EXTENDED google.compute.instances
DESC EXTENDED google.compute.instances;
```

### 기본 `DESCRIBE METHOD` 문 {#basic-describe-method-statement}
리소스의 단일 메서드를 검사합니다. 결과에는 입력 매개 변수와 응답 필드가 포함되며, 중첩 구조는 `shape` 열에 렌더링됩니다.

```sql
-- Show the inputs and outputs for the buckets.get method
DESCRIBE METHOD google.storage.buckets.get;
```

### 확장 `DESCRIBE METHOD` 문 {#extended-describe-method-statement}
확장 `DESCRIBE METHOD` 문을 실행하여 형태와 함께 필드별 설명을 포함합니다.

```sql
-- Show inputs, outputs, and descriptions for the buckets.insert method
DESCRIBE METHOD EXTENDED google.storage.buckets.insert;
```

### `INSERT` 페이로드 확인 {#discovering-an-insert-payload}
`DESCRIBE METHOD` 문은 `INSERT` 문(또는 모든 변경 메서드)이 허용하는 필드를 확인하는 표준 방법입니다. `shape` 열에는 에이전트와 사람이 한 번의 쿼리로 페이로드를 구성하는 데 사용할 수 있는 중첩 구조가 담겨 있습니다.

```sql
-- See what google.compute.instances.insert requires
DESCRIBE METHOD EXTENDED google.compute.instances.insert;
```
