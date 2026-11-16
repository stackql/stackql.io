---
title: server_info
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - server_info
  - model context protocol
description: StackQL MCP 서버의 ID와 런타임 정보를 반환하는 MCP 도구
image: "/img/stackql-featured-image.png"
---

StackQL MCP 서버의 ID와 런타임 정보(버전, 기반 SQL 엔진, 프로바이더 레지스트리 위치, 서버 모드, 레거시 읽기 전용 플래그)를 반환합니다. 필요할 때 이 도구를 호출하세요. 예를 들어 거부되었거나 게이트 제어 대상인 작업을 진단하거나 버전별 동작을 확인할 때 호출합니다. 세션 시작 시나 쿼리 전에는 호출할 필요가 없습니다.

참고 항목:
[[ MCP 개요 ]](/command-line-usage/mcp) [[ `list_providers` ]](/mcp/list_providers) [[ `list_registry` ]](/mcp/list_registry)

* * *

## 입력 {#inputs}

없음.

* * *

## 출력 {#output}

| 렌더러 | 형태 |
|--|--|
| KV(LLM용 markdown 키/값 블록) | 아래 필드가 포함된 구조화된 페이로드 |

<br/><br/>

| 필드 | 설명 |
|--|--|
|`version`|StackQL 빌드 버전입니다.|
|`commit`|Git 커밋 짧은 해시입니다.|
|`build_date`|RFC3339 빌드 타임스탬프입니다.|
|`platform`|`os/arch`(예: `linux/amd64`)입니다.|
|`transport`|`http` 또는 `stdio`입니다.|
|`sql_backend`|기반 SQL 엔진(예: `sqlite3`)입니다.|
|`provider_registry`|프로바이더 레지스트리 URL입니다.|
|`mode`|활성 서버 모드(`read_only`, `safe`, `delete_safe` 또는 `full_access`)입니다.|
|`is_read_only`|레거시 부울 플래그입니다(`mode == read_only`인 경우에만 `true`).|

* * *

## 게이트 제어 {#gating}

모든 서버 모드에서 항상 허용됩니다. 변경 작업으로 감사 로그에 기록되지 않습니다.

* * *

## 예제 {#example}

StackQL MCP 서버가 등록되어 있고 엘리시테이션(elicitation)을 지원하는 MCP 클라이언트에서 다음 프롬프트를 사용해 보세요:

```
What StackQL server am I connected to?  
Show me the version, mode, and where it pulls providers from.
```

다음은 Claude Desktop의 예제입니다:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_server_info_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_server_info_web.mp4" type="video/mp4" />
</video>
