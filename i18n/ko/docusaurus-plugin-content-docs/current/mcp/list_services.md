---
title: list_services
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_services
  - model context protocol
description: StackQL 프로바이더의 서비스를 나열하는 MCP 도구
image: "/img/stackql-featured-image.png"
---

단일 프로바이더가 제공하는 서비스를 반환합니다. 서비스는 StackQL [리소스 계층 구조](/getting-started/resource-hierarchy)의 두 번째 수준입니다. 리소스를 자세히 살펴보기 전에 범위를 좁히려면 [`list_providers`](/mcp/list_providers) 도구 다음에 이 도구를 호출하세요.

참고 항목:
[[ MCP 개요 ]](/command-line-usage/mcp) [[ `list_providers` ]](/mcp/list_providers) [[ `list_resources` ]](/mcp/list_resources)

* * *

## 입력 {#inputs}

| 인수 | 필수 | 설명 |
|--|--|--|
|`provider`|예|프로바이더 이름(예: `google`, `aws`, `azure`, `github`)입니다.|

<br/>

:::info

식별자에는 공백 문자, 세미콜론 또는 백슬래시를 포함할 수 없습니다. 이러한 입력은 문이 만들어지기 전에 거부됩니다.

:::

* * *

## 출력 {#output}

| 렌더러 | 형태 |
|--|--|
| 테이블 | 지정된 프로바이더 아래의 서비스당 1행 |

* * *

## 게이트 제어 {#gating}

모든 서버 모드에서 허용됩니다. 읽기 전용입니다.

* * *

## 예제 {#example}

StackQL MCP 서버가 등록되어 있고 엘리시테이션(elicitation)을 지원하는 MCP 클라이언트에서 다음 프롬프트를 사용해 보세요:

```
List the services available in Google Cloud
```

다음은 Claude Desktop의 예제입니다:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_services_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_services_web.mp4" type="video/mp4" />
</video>
