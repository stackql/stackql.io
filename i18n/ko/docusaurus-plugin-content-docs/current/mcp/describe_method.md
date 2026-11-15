---
title: describe_method
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - describe_method
  - model context protocol
description: StackQL 액세스 메서드 하나의 전체 I/O 계약을 반환하는 MCP 도구
image: "/img/stackql-featured-image.png"
---

단일 액세스 메서드의 전체 I/O 계약을 반환합니다. 항상 EXTENDED 형식이며, 입력, 요청 본문 스키마 및 전체 출력 형태를 포함합니다. 에이전트는 이 도구에서 열 이름을 얻습니다. 각 행에는 `param_type` 값이 있으며, 이 값은 `input_required`(필수이며 정확히 일치해야 하는 `WHERE` 조건자), `input_optional` 또는 `output`(`SELECT` 문이나 `RETURNING` 절에서 참조할 수 있는 필드) 중 하나입니다. 각 메서드는 고유한 형태를 반환하므로 리소스에는 단일 필드 목록이 없습니다. 따라서 [`list_methods`](/mcp/list_methods) 도구에서 선택한 메서드에 대해 첫 번째 쿼리를 실행하기 전에 이 도구를 호출하세요.

참고 항목:
[[ MCP 개요 ]](/command-line-usage/mcp) [[ `list_methods` ]](/mcp/list_methods) [[ `run_select_query` ]](/mcp/run_select_query)

* * *

## 입력 {#inputs}

| 인수 | 필수 | 설명 |
|--|--|--|
|`provider`|예|프로바이더 이름입니다.|
|`service`|예|해당 프로바이더의 서비스입니다.|
|`resource`|예|`provider.service` 아래의 리소스입니다.|
|`method`|예|[`list_methods`](/mcp/list_methods) 도구가 반환하는 메서드 이름입니다.|

<br/>

:::info

식별자에는 공백 문자, 세미콜론 또는 백슬래시를 포함할 수 없습니다. 이러한 입력은 문이 만들어지기 전에 거부됩니다.

:::

* * *

## 출력 {#output}

| 렌더러 | 형태 |
|--|--|
| KV | 입력 및 출력 필드당 1행: 이름, 형식, `param_type`(`input_required`, `input_optional` 또는 `output`), 형태 및 설명 |

* * *

## 게이트 제어 {#gating}

모든 서버 모드에서 허용됩니다. 읽기 전용입니다.

* * *

## 예제 {#example}

StackQL MCP 서버가 등록되어 있고 엘리시테이션(elicitation)을 지원하는 MCP 클라이언트에서 다음 프롬프트를 사용해 보세요:

```
Show me everything I need to know to call the insert method  
on Google Compute networks -- required parameters,  
optional parameters, and the request body shape.
```

다음은 Claude Desktop의 예제입니다:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_describe_method_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_describe_method_web.mp4" type="video/mp4" />
</video>
