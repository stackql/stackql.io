---
title: list_registry
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_registry
  - model context protocol
description: StackQL 프로바이더 레지스트리에서 사용할 수 있는 프로바이더와 버전을 나열하는 MCP 도구
image: "/img/stackql-featured-image.png"
---

구성된 StackQL 프로바이더 레지스트리에서 사용할 수 있는 프로바이더(및 해당 버전)를 반환합니다. 이 도구는 로컬 캐시로 이미 가져온 프로바이더만 보고하는 [`list_providers`](/mcp/list_providers) 도구와 다릅니다.

단일 프로바이더의 게시된 버전을 나열하려면 선택적 `provider` 인수를 전달하세요. 에이전트가 아직 로컬에 없는 항목을 설치해야 할 때는 [`pull_provider`](/mcp/pull_provider) 도구와 함께 사용하세요.

참고 항목:
[[ MCP 개요 ]](/command-line-usage/mcp) [[ `list_providers` ]](/mcp/list_providers) [[ `pull_provider` ]](/mcp/pull_provider) [[ registry 명령 ]](/command-line-usage/registry)

* * *

## 입력 {#inputs}

| 인수 | 필수 | 설명 |
|--|--|--|
|`provider`|아니요|프로바이더 이름입니다. 지정하면 결과가 해당 프로바이더의 게시된 버전으로 좁혀지고, 생략하면 모든 레지스트리 항목이 반환됩니다.|

* * *

## 출력 {#output}

| 렌더러 | 형태 |
|--|--|
| 테이블 | 레지스트리 항목당 1행(프로바이더 및 버전 메타데이터) |

* * *

## 게이트 제어 {#gating}

모든 서버 모드에서 허용됩니다. 클라우드 컨트롤 플레인이나 데이터 플레인에는 영향을 주지 않으며, 레지스트리만 읽습니다.

* * *

## 예제 {#example}

StackQL MCP 서버가 등록된 MCP 클라이언트에서 다음 프롬프트를 사용해 보세요:

```
What providers does the StackQL registry offer,  
and which versions of the aws provider are published?
```

다음은 Claude Desktop의 예제입니다:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_registry_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_registry_web.mp4" type="video/mp4" />
</video>
