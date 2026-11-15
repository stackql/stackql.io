---
title: query_library_get
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - query_library_get
  - query library
  - model context protocol
description: StackQL 쿼리 템플릿을 조회하고 제공된 매개 변수를 검증하여 실행 없이 렌더링된 SQL을 반환하는 MCP 도구
image: "/img/stackql-featured-image.png"
---

[`query_library_search`](/mcp/query_library_search) 도구가 반환한 `id` 값으로 [Query Library](pathname:///docs/query-library/) 항목을 조회합니다.

`params` 인수를 생략하거나 비워 두면 원시 템플릿, 매개 변수 선언 및 메모를 반환합니다. 비어 있지 않은 `params` 객체를 전달하면 서버가 값을 검증하고 템플릿에 대입합니다. 렌더링이 성공한 경우에만 SQL을 반환합니다. 모델이 직접 값을 대입할 필요는 없습니다.

이 도구는 SQL을 실행하거나 클라우드 자격 증명을 확인하지 않습니다. 이 도구의 매개 변수 검증은 설치된 프로바이더를 대상으로 `SELECT` 문을 구문 분석하고 실행 계획을 수립하는 [`validate_select_query`](/mcp/validate_select_query) 도구와 다릅니다.

참고 항목:
[[ MCP 개요 ]](/command-line-usage/mcp) [[ `query_library_search` ]](/mcp/query_library_search) [[ `run_select_query` ]](/mcp/run_select_query) [[ `run_mutation_query` ]](/mcp/run_mutation_query) [[ `run_lifecycle_operation` ]](/mcp/run_lifecycle_operation)

* * *

## 입력 {#inputs}

| 인수 | 필수 | 설명 |
|--|--|--|
|`id`|예|검색 결과의 쿼리 ID(예: `aws/s3/buckets-list`)입니다.|
|`params`|아니요|선언된 매개 변수 이름을 값에 매핑하는 객체입니다. 렌더링 없이 템플릿을 검사하려면 생략하거나 `{}` 값을 전달하세요.|
|`format`|아니요|텍스트 렌더링 형식으로, `markdown` 또는 `json` 중 하나입니다. 생략하면 서버의 렌더링 설정(기본값 `markdown`)을 사용합니다.|

알 수 없는 매개 변수는 거부됩니다. 값이 제공되지 않았고 기본값도 없는 필수 매개 변수는 `missing_params` 필드에 보고됩니다. 제공된 값은 선언된 형식 및 제약 조건에 따라 검사되며, 확인되지 않은 자리 표시자는 오류가 됩니다.

* * *

## 출력 {#output}

기본 텍스트 보기는 식별 정보, 변경 작업 여부, 다음 도구, 유효성, 소스 링크와 함께 템플릿, 렌더링된 SQL 또는 오류(있는 경우)를 담은 KV 블록입니다. 전체 메타데이터를 보려면 구조화된 페이로드 또는 `format: "json"` 설정을 사용하세요:

| 필드 | 설명 |
|--|--|
|`id`, `title`, `description`|항목 식별 정보 및 요약입니다.|
|`mutation`, `verb`|항목이 리소스를 변경하는지 여부와, 선언된 경우 작업 유형(`select`, `mutation` 또는 `lifecycle`)입니다.|
|`next_tool`|항목의 실행 도구이며 `run_select_query`, `run_mutation_query` 또는 `run_lifecycle_operation` 중 하나입니다. 조회 시에는 이 도구를 호출하지 않습니다.|
|`params`|이름, 형식, 필수 여부 플래그와 기본값, 설명, 예제, 열거형 또는 패턴(있는 경우)을 포함한 매개 변수 선언입니다.|
|`template`, `notes`|원시 템플릿과 작성자 메모이며, `params` 인수를 생략하거나 비워 둔 경우에 반환됩니다.|
|`rendered`, `valid`|렌더링을 시도했는지와 성공했는지 여부입니다. 원시 템플릿을 검사할 때는 두 값 모두 `false`입니다.|
|`sql`|렌더링된 SQL이며, 렌더링이 성공한 후에만 포함됩니다.|
|`missing_params`, `unknown_params`, `errors`|렌더링이 실패한 경우의 검증 세부 정보입니다. 이 문제가 해결될 때까지 SQL을 실행하지 마세요.|
|`auth`, `permissions`|선언된 경우, 실행에 필요한 자격 증명 환경 변수와 프로바이더 고유의 권한입니다. 조회 자체에는 둘 다 필요하지 않습니다.|
|`cost`, `outputs`, `related`|선언된 경우, 팬아웃 및 비용 힌트, 결과 열 설명, 관련 항목 ID입니다.|
|`doc_url`|사람이 읽을 수 있는 라이브러리 페이지입니다.|
|`source_tier`, `source_url`, `stale`|콘텐츠 출처: `primary`, `fallback` 또는 `snapshot` 중 하나, 해당 URL 또는 내장 경로, 그리고 페치 실패 후 캐시나 스냅샷에서 콘텐츠를 제공했는지 여부입니다.|

알 수 없거나 형식이 잘못된 ID는 오류를 반환합니다. 유효한 ID를 찾으려면 `query_library_search` 도구를 사용하세요.

* * *

## 게이트 제어 {#gating}

구성된 도구 허용 목록에 따라 모든 서버 모드에서 허용됩니다. 변경 작업 또는 수명 주기 템플릿을 조회하거나 렌더링해도 클라우드 측에는 영향이 없으며 승인이 필요하지 않습니다. 반환된 SQL의 실행은 서버 [모드](/command-line-usage/mcp#server-modes)의 적용을 받는 별도의 호출입니다.

* * *

## 예제 {#example}

먼저 항목을 검사합니다:

```json
{
  "id": "aws/s3/buckets-list"
}
```

그런 다음 필수 라우팅 리전을 제공합니다:

```json
{
  "id": "aws/s3/buckets-list",
  "params": {
    "region": "us-east-1"
  }
}
```

`rendered` 필드와 `valid` 필드를 확인한 후 [`validate_select_query`](/mcp/validate_select_query) 도구로 반환된 `SELECT` 문을 검증합니다. `next_tool` 필드에 지정된 도구로 실행하며, 이때 반환된 `sql` 값을 전달하고 출처 표시를 위해 `source` 매개 변수를 라이브러리 항목의 `id` 값으로 설정합니다. 실행 시에는 프로바이더 자격 증명과 권한이 필요합니다.
