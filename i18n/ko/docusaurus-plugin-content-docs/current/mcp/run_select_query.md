---
title: run_select_query
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - run_select_query
  - model context protocol
description: 프로바이더에 대해 StackQL SELECT 문을 실행하는 MCP 도구
image: "/img/stackql-featured-image.png"
---

구성된 프로바이더에 대해 `SELECT` 문을 실행하고 결과 행을 반환합니다. 읽기 전용이므로 변경 작업과 수명 주기 작업은 허용되지 않습니다. 이러한 작업에는 [`run_mutation_query`](/mcp/run_mutation_query) 또는 [`run_lifecycle_operation`](/mcp/run_lifecycle_operation) 도구를 사용하세요.

결과 행은 클라이언트에 반환되지만 감사 로그에서는 의도적으로 제외됩니다.

참고 항목:
[[ MCP 개요 ]](/command-line-usage/mcp) [[ `validate_select_query` ]](/mcp/validate_select_query) [[ `list_methods` ]](/mcp/list_methods) [[ SELECT 언어 사양 ]](/language-spec/select)

* * *

## 입력 {#inputs}

| 인수 | 필수 | 설명 |
|--|--|--|
|`sql`|예|`SELECT` 문입니다. 메타데이터 문(`SHOW`, `DESCRIBE`, `DESC`, `EXPLAIN`)도 정책 게이트에서 읽기 전용으로 분류되어 허용됩니다. `DESC` 문은 StackQL `v0.12.732` 이상 릴리스에서 인식됩니다.|
|`row_limit`|아니요|클라이언트에 반환되는 행 수의 상한입니다. `0`(기본값)은 클라이언트 측 상한이 없음을 의미합니다.|
|`source`|아니요|SQL을 [`query_library_get`](/mcp/query_library_get) 도구에서 가져온 경우의 Query Library 항목 ID입니다. 출처 표시 용도로만 사용되며, 실행이나 승인에는 영향을 주지 않습니다.|
|`format`|아니요|텍스트 렌더링 형식(`markdown` 또는 `json`)입니다. 생략하면 서버의 렌더링 설정을 사용합니다.|

* * *

## 출력 {#output}

| 렌더러 | 형태 |
|--|--|
| 테이블 | `{rows: [...]}` (행당 객체 1개, 열 이름을 키로 사용) |

* * *

## 게이트 제어 {#gating}

페이로드의 모든 문이 읽기 전용이면 모든 서버 모드(`read_only`, `safe`, `delete_safe`, `full_access`)에서 허용됩니다. `v1.0`부터 이 도구는 `full_access` 모드를 포함한 모든 모드에서 `INSERT`, `UPDATE`, `REPLACE`, `DELETE`, `EXEC` 문을 거부하며, [`run_mutation_query`](/mcp/run_mutation_query) 또는 [`run_lifecycle_operation`](/mcp/run_lifecycle_operation) 도구를 안내하는 오류를 반환합니다. 여러 문을 포함하는 페이로드는 그중 가장 높은 권한이 필요한 문을 기준으로 분류되므로, 앞에 `SELECT` 문이 있고 그 뒤에 변경 문이 있는 경우에도 같은 방식으로 거부됩니다. 이 도구는 `destructiveHint: false` 및 `openWorldHint: true` 힌트를 선언합니다.

* * *

## 예제 {#example}

StackQL MCP 서버가 등록된 MCP 클라이언트에서 다음 프롬프트를 사용해 보세요:

```
List the VPC networks in my Google Cloud project my-project  
and tell me which ones use auto subnet creation.
```

다음은 Claude Desktop의 예제입니다:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_run_select_query_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_run_select_query_web.mp4" type="video/mp4" />
</video>
