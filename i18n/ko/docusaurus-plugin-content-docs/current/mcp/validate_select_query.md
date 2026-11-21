---
title: validate_select_query
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - validate_select_query
  - model context protocol
description: StackQL SELECT 문을 실행하지 않고 구문 분석하고 실행 계획을 수립하는 MCP 도구
image: "/img/stackql-featured-image.png"
---

단일 `SELECT` 문을 실행하지 않고 구문 분석하고 실행 계획을 수립합니다. `{valid, errors}` 객체를 반환합니다. 에이전트가 실제 API 호출을 수행하기 전에 쿼리가 컴파일되고 탐색한 메서드에 바인딩되는지 확인하려는 경우에 유용합니다.

:::info

`SELECT` 문만, 정확히 하나의 문만 허용됩니다. 둘 이상의 문을 포함하는 페이로드는 `validate_select_query accepts exactly one statement` 오류와 함께 거부됩니다.

:::

참고 항목:
[[ MCP 개요 ]](/command-line-usage/mcp) [[ `list_methods` ]](/mcp/list_methods) [[ `run_select_query` ]](/mcp/run_select_query)

* * *

## 입력 {#inputs}

| 인수 | 필수 | 설명 |
|--|--|--|
|`sql`|예|단일 `SELECT` 문입니다.|

* * *

## 출력 {#output}

| 렌더러 | 형태 |
|--|--|
| KV | `valid`(bool) 및 `errors`(문자열 배열, 유효한 경우 비어 있음) |

* * *

## 게이트 제어 {#gating}

모든 서버 모드에서 허용됩니다. API 호출은 수행되지 않습니다.

* * *

## 예제 {#example}

StackQL MCP 서버가 등록된 MCP 클라이언트에서 다음 프롬프트를 사용해 보세요:

```
Can you validate this query before I run it:

select name, network  
from google.compute.subnetworks  
where project = 'my-project' and region = 'us-central1';
```

다음은 Claude Desktop의 예제입니다:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_validate_select_query_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_validate_select_query_web.mp4" type="video/mp4" />
</video>
