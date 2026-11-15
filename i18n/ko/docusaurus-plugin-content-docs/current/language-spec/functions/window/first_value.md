---
title: FIRST_VALUE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 FIRST_VALUE 윈도우 함수: 윈도우 프레임의 첫 번째 행에 대해 평가한 식의 값입니다."
image: "/img/stackql-featured-image.png"
---
윈도우 프레임의 첫 번째 행에 대해 평가한 식의 값을 반환합니다.

이 기본 제공 윈도우 함수는 집계 윈도우 함수와 같은 방식으로 각 행의 윈도우 프레임을 계산합니다. 각 행에 대해 윈도우 프레임의 첫 번째 행에 대해 평가한 식의 값을 반환합니다.

참고 항목:
[[` SELECT `]](/language-spec/select) [[` LAST_VALUE `]](/language-spec/functions/window/last_value) [[` NTH_VALUE `]](/language-spec/functions/window/nth_value)

* * *

## 구문 {#syntax}

```sql
SELECT FIRST_VALUE(expr) OVER (
    [PARTITION BY column]
    ORDER BY column
    [ROWS BETWEEN frame_start AND frame_end]
) FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*expr*__
윈도우 프레임의 첫 번째 행에 대해 평가할 식입니다.

__*PARTITION BY column*__
선택 사항입니다. 결과 집합을 파티션으로 나눕니다. `FIRST_VALUE` 함수는 각 파티션 내에서 개별적으로 적용됩니다.

__*ORDER BY column*__
행을 처리하는 순서를 지정합니다.

__*ROWS BETWEEN frame_start AND frame_end*__
선택 사항입니다. 윈도우 프레임을 정의합니다. 일반적인 옵션은 다음과 같습니다.
- `ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW`
- `ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING`

프레임 절이 없으면 프레임은 `RANGE BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW`입니다. 이 프레임은 현재 행이 아니라 `ORDER BY` 열 기준으로 현재 행과 동순위인 마지막 행에서 끝납니다.

## 반환 값 {#return-values}

윈도우 프레임의 첫 번째 행에 대해 평가한 식의 값을 반환합니다.

* * *

## 예제 {#examples}

### 참고용 첫 번째 릴리스 조회 {#find-the-first-release-for-context}

```sql
-- Find first and latest release for context
SELECT
    tag_name,
    name,
    published_at,
    FIRST_VALUE(tag_name) OVER (
        ORDER BY published_at
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING
    ) as first_release,
    LAST_VALUE(tag_name) OVER (
        ORDER BY published_at
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING
    ) as latest_release
FROM github.repos.releases
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

:::tip

기본적으로 윈도우 프레임은 파티션의 시작부터 현재 행까지입니다. 파티션의 모든 행을 포함하려면 `ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING` 절을 사용하세요.

:::

자세한 내용은 [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions) 문서를 참조하세요.
