---
title: pull_provider
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - pull_provider
  - model context protocol
description: 레지스트리에서 로컬 캐시로 StackQL 프로바이더를 설치하는 MCP 도구
image: "/img/stackql-featured-image.png"
---

StackQL 레지스트리에서 단일 프로바이더를 로컬 approot 캐시로 설치합니다. `version` 인수를 생략하면 게시된 최신 버전을 가져옵니다.

로컬 캐시 상태만 기록합니다. 클라우드 컨트롤 플레인이나 데이터 플레인에는 영향을 주지 않으므로 이 도구는 모든 서버 모드(`read_only` 모드 포함)에서 허용됩니다. 가져오기 전에 [`list_registry`](/mcp/list_registry) 도구를 사용하여 프로바이더 이름과 사용 가능한 버전을 확인하세요.

참고 항목:
[[ MCP 개요 ]](/command-line-usage/mcp) [[ `list_registry` ]](/mcp/list_registry) [[ `list_providers` ]](/mcp/list_providers) [[ registry 명령 ]](/command-line-usage/registry)

* * *

## 입력 {#inputs}

| 인수 | 필수 | 설명 |
|--|--|--|
|`provider`|예|레지스트리에 게시된 프로바이더 이름(예: `aws`, `google`, `cloudflare`)입니다.|
|`version`|아니요|가져올 게시된 버전입니다. 생략하면 게시된 최신 버전이 설치됩니다.|

* * *

## 출력 {#output}

| 렌더러 | 형태 |
|--|--|
| KV | 프로바이더(및 버전)가 로컬 캐시에 설치되었다는 확인 |

* * *

## 게이트 제어 {#gating}

모든 서버 모드에서 허용됩니다. 클라우드 측에는 영향이 없으며, 로컬 캐시 상태만 변경됩니다.

* * *

## 예제 {#example}

StackQL MCP 서버가 등록된 MCP 클라이언트에서 다음 프롬프트를 사용해 보세요:

```
I want to start querying Google resources with StackQL.  
Can you install the Google provider for me?
```

다음은 Claude Desktop의 예제입니다:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_pull_provider_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_pull_provider_web.mp4" type="video/mp4" />
</video>
