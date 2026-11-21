---
title: list_methods
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_methods
  - model context protocol
description: StackQL 리소스의 액세스 메서드를 나열하는 MCP 도구
image: "/img/stackql-featured-image.png"
---

리소스에 바인딩된 액세스 메서드(HTTP 작업)를 반환합니다. 사용 가능한 읽기 메서드, 변경 작업 및 수명 주기 작업이 포함됩니다. 에이전트는 **쿼리를 작성하기 전에** 이 도구를 호출해야 합니다. 필수 `WHERE` 매개 변수는 선택한 읽기 메서드에서 추론되기 때문입니다. SQL 동사가 의도와 일치하고 필수 매개 변수를 제공할 수 있는 메서드를 선택한 다음, 해당 메서드에 대해 [`describe_method`](/mcp/describe_method) 도구를 호출하여 출력 필드를 포함한 전체 계약을 확인하세요.

참고 항목:
[[ MCP 개요 ]](/command-line-usage/mcp) [[ `list_resources` ]](/mcp/list_resources) [[ `describe_method` ]](/mcp/describe_method) [[ `validate_select_query` ]](/mcp/validate_select_query)

* * *

## 입력 {#inputs}

| 인수 | 필수 | 설명 |
|--|--|--|
|`provider`|예|프로바이더 이름입니다.|
|`service`|예|해당 프로바이더의 서비스입니다.|
|`resource`|예|`provider.service` 아래의 리소스입니다.|

<br/>

:::info

식별자에는 공백 문자, 세미콜론 또는 백슬래시를 포함할 수 없습니다. 이러한 입력은 문이 만들어지기 전에 거부됩니다.

:::

* * *

## 출력 {#output}

| 렌더러 | 형태 |
|--|--|
| 테이블 | 액세스 메서드당 1행, 매핑되는 SQL 동사와 필수 매개 변수 포함 |

* * *

## 게이트 제어 {#gating}

모든 서버 모드에서 허용됩니다. 읽기 전용입니다.

* * *

## 예제 {#example}

StackQL MCP 서버가 등록된 MCP 클라이언트에서 다음 프롬프트를 사용해 보세요:

```
For Google Compute instances, what methods are available  
and what parameters are required?
```

다음은 Claude Desktop의 예제입니다:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_methods_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_methods_web.mp4" type="video/mp4" />
</video>
