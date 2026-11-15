---
title: NTH_VALUE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 NTH_VALUE 윈도우 함수: 윈도우 프레임의 N번째 행에 대해 평가한 식의 값입니다."
image: "/img/stackql-featured-image.png"
---
윈도우 프레임의 N번째 행에 대해 평가한 식의 값을 반환합니다.

이 기본 제공 윈도우 함수는 집계 윈도우 함수와 같은 방식으로 각 행의 윈도우 프레임을 계산합니다. 윈도우 프레임의 N번째 행에 대해 평가한 식의 값을 반환합니다. 윈도우 프레임 내의 행 번호는 `ORDER BY` 절이 있으면 그 절이 정의하는 순서에 따라, 없으면 임의의 순서로 1부터 매겨집니다. 파티션에 N번째 행이 없으면 `NULL`을 반환합니다.

참고 항목:
[[` SELECT `]](/language-spec/select) [[` FIRST_VALUE `]](/language-spec/functions/window/first_value) [[` LAST_VALUE `]](/language-spec/functions/window/last_value)

* * *

## 구문 {#syntax}

```sql
SELECT NTH_VALUE(expr, N) OVER (
    [PARTITION BY column]
    ORDER BY column
    [ROWS BETWEEN frame_start AND frame_end]
) FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*expr*__
윈도우 프레임의 N번째 행에 대해 평가할 식입니다.

__*N*__
가져올 행 번호를 지정하는 양의 정수입니다(1부터 시작).

__*PARTITION BY column*__
선택 사항입니다. 결과 집합을 파티션으로 나눕니다. `NTH_VALUE` 함수는 각 파티션 내에서 개별적으로 적용됩니다.

__*ORDER BY column*__
행을 처리하는 순서를 지정합니다.

__*ROWS BETWEEN frame_start AND frame_end*__
선택 사항입니다. 윈도우 프레임을 정의합니다.

## 반환 값 {#return-values}

윈도우 프레임의 N번째 행에 대해 평가한 식의 값을 반환합니다. N번째 행이 없으면 `NULL`을 반환합니다.

* * *

## 예제 {#examples}

### 기여 수 상위 3명의 기여자 조회 {#get-the-top-three-contributors}

```sql
-- Get the top 3 contributors by number of contributions
SELECT
    login,
    contributions,
    NTH_VALUE(login, 1) OVER (ORDER BY contributions DESC
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) as top_contributor,
    NTH_VALUE(login, 2) OVER (ORDER BY contributions DESC
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) as second_contributor,
    NTH_VALUE(login, 3) OVER (ORDER BY contributions DESC
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) as third_contributor
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

자세한 내용은 [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions) 문서를 참조하세요.
