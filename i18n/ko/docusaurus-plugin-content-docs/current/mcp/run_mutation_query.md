---
title: run_mutation_query
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - run_mutation_query
  - model context protocol
description: StackQL을 통해 프로바이더에 대해 INSERT, UPDATE, REPLACE 또는 DELETE를 실행하는 MCP 도구
image: "/img/stackql-featured-image.png"
---

프로바이더에 대해 `INSERT`, `UPDATE`, `REPLACE` 또는 `DELETE` 문을 실행합니다. **실제 부작용이 있습니다.** 호출이 성공하면 클라우드 상태가 변경됩니다.

서버 [모드](/command-line-usage/mcp#server-modes)에 의해 게이트 제어됩니다.

| 모드 | `INSERT` / `UPDATE` / `REPLACE` | `DELETE` |
|--|--|--|
|`read_only`|거부됨|거부됨|
|`safe` (기본값)|엘리시테이션(elicitation)으로 승인 필요|승인 필요|
|`delete_safe`|허용됨|승인 필요|
|`full_access`|허용됨|허용됨|

<br/>

:::info

여러 문을 포함하는 페이로드는 그중 가장 높은 권한이 필요한 문을 기준으로 게이트 제어됩니다

:::

변경 작업에 승인이 필요한데 클라이언트가 MCP 엘리시테이션 기능을 알리지 않은 경우 호출이 거부됩니다.

참고 항목:
[[ MCP 개요 ]](/command-line-usage/mcp) [[ `run_lifecycle_operation` ]](/mcp/run_lifecycle_operation) [[ `describe_method` ]](/mcp/describe_method) [[ INSERT 언어 사양 ]](/language-spec/insert) [[ DELETE 언어 사양 ]](/language-spec/delete)

* * *

## 입력 {#inputs}

| 인수 | 필수 | 설명 |
|--|--|--|
|`sql`|예|`INSERT`, `UPDATE`, `REPLACE` 또는 `DELETE` 문입니다.|
|`source`|아니요|SQL을 [`query_library_get`](/mcp/query_library_get) 도구에서 가져온 경우의 Query Library 항목 ID입니다. 출처 표시 용도로만 사용되며, 실행이나 승인에는 영향을 주지 않습니다.|
|`format`|아니요|텍스트 렌더링 형식(`markdown` 또는 `json`)입니다. 생략하면 서버의 렌더링 설정을 사용합니다.|

* * *

## 출력 {#output}

| 렌더러 | 형태 |
|--|--|
| KV | `{messages: [...], timestamp}` (API 응답 본문이 아닌 전송 확인) |

* * *

## 예제 {#example}

StackQL MCP 서버가 등록되어 있고 엘리시테이션을 지원하는 MCP 클라이언트에서 다음 프롬프트를 사용해 보세요:

```
The firewall rule allow-test-old in project my-project  
is no longer needed.  Please remove it.
```

서버가 `full_access` 모드로 실행 중이 아니면, MCP 클라이언트는 삭제 작업이 전송되기 전에 승인 요청을 표시합니다.

다음 영상은 StackQL MCP 서버가 기본 `safe` 모드로 실행 중일 때의 엘리시테이션 흐름을 보여 줍니다. 클라우드 측 변경이 전송되기 전에 클라이언트에 `DELETE` 문을 승인하라는 요청이 표시됩니다. 공식 [MCP Inspector](https://github.com/modelcontextprotocol/inspector)를 `npx @modelcontextprotocol/inspector` 명령으로 실행하여 자체 StackQL MCP 서버에서 이 과정을 재현할 수 있습니다.


<video autoPlay loop muted playsInline style={{width: 'calc(100% - 4rem)', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/mcp_inspector_run_mutation_query_web.webm" type="video/webm" />
  <source src="/img/videos/mcp_inspector_run_mutation_query_web.mp4" type="video/mp4" />
</video>
