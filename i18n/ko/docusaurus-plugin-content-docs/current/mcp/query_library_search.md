---
title: query_library_search
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - query_library_search
  - query library
  - model context protocol
description: 자연어 의도, 프로바이더, 서비스, 태그로 엄선된 StackQL 쿼리 템플릿을 검색하는 MCP 도구
image: "/img/stackql-featured-image.png"
---

[StackQL Query Library](pathname:///docs/query-library/)에서 자연어 의도와 일치하는 템플릿을 검색합니다. SQL을 처음부터 작성하기 전에 이 도구를 먼저 확인하세요. 그런 다음 결과의 `id` 값을 [`query_library_get`](/mcp/query_library_get) 도구에 전달하여 템플릿을 살펴보고 매개 변수의 유효성을 검사합니다.

이 도구는 라이브러리 콘텐츠만 조회합니다. SQL을 실행하거나 클라우드 API를 호출하지 않으며, 프로바이더 자격 증명도 필요하지 않습니다.

참고 항목:
[[ MCP 개요 ]](/command-line-usage/mcp) [[ `query_library_get` ]](/mcp/query_library_get) [[ `validate_select_query` ]](/mcp/validate_select_query)

* * *

## 입력 {#inputs}

| 인수 | 필수 | 설명 |
|--|--|--|
|`intent`|예|작업을 설명하는 자연어 텍스트입니다.|
|`provider`|아니요|프로바이더로 필터링합니다(예: `aws`).|
|`service`|아니요|서비스로 필터링합니다(예: `s3`).|
|`tags`|아니요|태그 배열입니다. 항목은 지정된 모든 태그와 일치해야 합니다.|
|`include_mutations`|아니요|변경 템플릿을 포함합니다. 기본값은 `false`입니다. `read_only` 모드에서는 무시되며, 이 모드에서는 변경 항목이 항상 제외됩니다.|
|`limit`|아니요|반환할 최대 결과 수입니다. 생략하거나 양수가 아니면 기본값은 `5`입니다. 최댓값은 `20`입니다.|
|`format`|아니요|텍스트 렌더링 형식(`markdown` 또는 `json`)입니다. 생략하면 서버의 렌더링 설정을 사용합니다(기본값은 `markdown`).|

* * *

## 출력 {#output}

기본 텍스트 보기는 `id`, `title`, `mutation`, `required_params`, `score` 열로 구성된 순위별 표이며, 그 뒤에 출처 인용과 결과가 없을 때의 안내가 이어집니다. 구조화된 페이로드에는 다음 필드가 포함됩니다.

| 필드 | 설명 |
|--|--|
|`hits`|순위가 매겨진 항목으로, `id`, `title`, 선택적 `description`, `providers`, `services`, `required_params` 필드와 `mutation`, `score` 필드를 포함합니다. 전체 매개 변수 선언은 이어서 `query_library_get` 도구로 확인하세요.|
|`miss`|결과가 없거나 최고 점수가 일치 임계값보다 낮으면 `true`입니다. 이 경우에도 점수가 낮은 가장 가까운 일치 항목이 반환될 수 있습니다.|
|`guidance`|결과가 없을 때 에이전트가 쿼리를 작성할 수 있도록 서버 지침, 탐색 도구, `validate_select_query` 도구를 안내합니다.|
|`source_tier`|콘텐츠 출처(`primary`, `fallback` 또는 `snapshot`)입니다.|
|`source_url`|인용에 사용할, 사용된 카탈로그의 URL 또는 내장 경로입니다.|
|`stale`|`true`이면 가져오기에 실패하여 캐시 또는 내장 스냅샷에서 콘텐츠가 제공된 것입니다.|

더 이상 사용되지 않는 항목은 제외됩니다. 서버는 카탈로그를 캐시하며, 호스팅된 라이브러리에서 GitHub 소스와 내장 스냅샷으로 대체할 수 있습니다.

* * *

## 게이트 제어 {#gating}

구성된 도구 허용 목록에 따라 모든 서버 모드에서 허용됩니다. 승인이나 프로바이더 자격 증명은 필요하지 않습니다. 변경 템플릿을 검색한다고 해서 해당 템플릿의 실행 권한이 부여되지는 않습니다.

* * *

## 예제 {#example}

도구 인수:

```json
{
  "intent": "List S3 bucket names and regions",
  "provider": "aws",
  "service": "s3",
  "limit": 5
}
```

`hits` 필드의 `id` 값(예: `aws/s3/buckets-list`)을 [`query_library_get`](/mcp/query_library_get) 도구에 사용하세요. `miss` 필드가 `true`이면 SQL을 작성하기 전에 가장 가까운 템플릿을 살펴보고 참고하거나 [`list_methods`](/mcp/list_methods) 및 [`describe_method`](/mcp/describe_method) 도구를 사용하세요.
