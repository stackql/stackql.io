---
title: list_providers
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_providers
  - model context protocol
description: StackQL MCP 서버에서 사용할 수 있는 클라우드 및 SaaS 프로바이더를 나열하는 MCP 도구
image: "/img/stackql-featured-image.png"
---

로컬 StackQL 캐시로 이미 가져온 프로바이더를 반환합니다. 프로바이더는 StackQL [리소스 계층 구조](/getting-started/resource-hierarchy)의 최상위 수준입니다. 에이전트가 쿼리할 수 있는 대상을 탐색해야 할 때 이 도구를 진입점으로 사용하세요.

*레지스트리*에서 사용할 수 있는 프로바이더(아직 가져오지 않은 프로바이더 포함)는 [`list_registry`](/mcp/list_registry) 도구를 참조하세요.

참고 항목:
[[ MCP 개요 ]](/command-line-usage/mcp) [[ `list_services` ]](/mcp/list_services) [[ `list_registry` ]](/mcp/list_registry) [[ `pull_provider` ]](/mcp/pull_provider)

* * *

## 입력 {#inputs}

없음.

* * *

## 출력 {#output}

| 렌더러 | 형태 |
|--|--|
| 테이블(LLM용 마크다운 표) | 프로바이더당 1행 |

<br />

각 행에는 레지스트리에 게시된 프로바이더 이름과 버전 메타데이터가 포함됩니다.

* * *

## 게이트 제어 {#gating}

모든 서버 모드(`read_only`, `safe`, `delete_safe`, `full_access`)에서 허용됩니다. 읽기 전용입니다.

* * *

## 예제 {#example}

StackQL MCP 서버가 등록되어 있고 엘리시테이션(elicitation)을 지원하는 MCP 클라이언트에서 다음 프롬프트를 사용해 보세요:

```
Which cloud providers are available for you to query right now?
```

다음은 Claude Desktop의 예제입니다:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_providers_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_providers_web.mp4" type="video/mp4" />
</video>
