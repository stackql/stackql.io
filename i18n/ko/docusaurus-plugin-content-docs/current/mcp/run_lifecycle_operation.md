---
title: run_lifecycle_operation
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - run_lifecycle_operation
  - model context protocol
description: StackQL EXEC 수명 주기 작업을 실행하는 MCP 도구
image: "/img/stackql-featured-image.png"
---

StackQL `EXEC` 수명 주기 작업을 실행합니다. 수명 주기 작업은 CRUD가 아닌 프로바이더 작업입니다(VM 시작/중지, 키 교체, 워크플로 트리거 등). **실제 부작용이 있습니다.**

서버 [모드](/command-line-usage/mcp#server-modes)에 의해 게이트 제어됩니다.

| 모드 | 수명 주기 `EXEC` |
|--|--|
|`read_only`|거부됨|
|`safe` (기본값)|엘리시테이션(elicitation)으로 승인 필요|
|`delete_safe`|승인 필요|
|`full_access`|허용됨|

<br/>

:::info

여러 문을 포함하는 페이로드는 그중 가장 높은 권한이 필요한 문을 기준으로 게이트 제어됩니다

:::

승인이 필요한데 클라이언트가 엘리시테이션 기능을 알리지 않은 경우 호출이 거부됩니다.

참고 항목:
[[ MCP 개요 ]](/command-line-usage/mcp) [[ `run_mutation_query` ]](/mcp/run_mutation_query) [[ `describe_method` ]](/mcp/describe_method) [[ EXEC 언어 사양 ]](/language-spec/exec)

* * *

## 입력 {#inputs}

| 인수 | 필수 | 설명 |
|--|--|--|
|`sql`|예|수명 주기 메서드를 대상으로 하는 StackQL `EXEC` 문입니다.|
|`source`|아니요|SQL을 [`query_library_get`](/mcp/query_library_get) 도구에서 가져온 경우의 Query Library 항목 ID입니다. 출처 표시 용도로만 사용되며, 실행이나 승인에는 영향을 주지 않습니다.|
|`format`|아니요|텍스트 렌더링 형식(`markdown` 또는 `json`)입니다. 생략하면 서버의 렌더링 설정을 사용합니다.|

* * *

## 출력 {#output}

| 렌더러 | 형태 |
|--|--|
| KV | `{messages: [...], timestamp}` |

* * *

## 예제 {#example}

StackQL MCP 서버가 등록되어 있고 엘리시테이션을 지원하는 MCP 클라이언트에서 다음 프롬프트를 사용해 보세요:

```
The Compute instance web-01 in us-central1-a  
(project my-project) is stuck.  Please stop it.
```

서버가 `full_access` 모드로 실행 중이 아니면, MCP 클라이언트는 작업이 전송되기 전에 승인 요청을 표시합니다.

다음은 Claude Desktop의 예제입니다:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_run_lifecycle_operation_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_run_lifecycle_operation_web.mp4" type="video/mp4" />
</video>
