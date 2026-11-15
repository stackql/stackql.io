---
title: PERCENT_RANK
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 PERCENT_RANK 윈도우 함수: 파티션 내 현재 행의 상대 순위를 0.0에서 1.0 사이의 값으로 나타냅니다."
image: "/img/stackql-featured-image.png"
---
파티션 내 현재 행의 상대 순위를 나타내는 0.0에서 1.0 사이의 값을 반환합니다.

이름과 달리 이 함수는 `(rank - 1) / (partition-rows - 1)`과 같은 값을 반환합니다. 여기서 `rank`는 기본 제공 윈도우 함수 `RANK()`가 반환하는 값이고, `partition-rows`는 파티션의 전체 행 수입니다. 파티션에 행이 1개만 있으면 이 함수는 0.0을 반환합니다.

참고 항목:
[[` SELECT `]](/language-spec/select) [[` RANK `]](/language-spec/functions/window/rank) [[` CUME_DIST `]](/language-spec/functions/window/cume_dist)

* * *

## 구문 {#syntax}

```sql
SELECT PERCENT_RANK() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*PARTITION BY column*__
선택 사항입니다. 결과 집합을 파티션으로 나눕니다. `PERCENT_RANK` 함수는 각 파티션에 개별적으로 적용됩니다.

__*ORDER BY column*__
백분위 순위를 계산하는 순서를 지정합니다.

## 반환 값 {#return-values}

백분위 순위를 나타내는 0.0에서 1.0 사이의 부동 소수점 숫자를 반환합니다.

* * *

## 예제 {#examples}

### 기여자의 백분위 순위 계산 {#calculate-percentile-ranking-of-contributors}

```sql
-- Calculate percentile ranking of contributors
SELECT
    login,
    contributions,
    PERCENT_RANK() OVER (ORDER BY contributions) as percentile,
    CUME_DIST() OVER (ORDER BY contributions) as cumulative_dist
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY contributions DESC;
```

:::tip

`PERCENT_RANK` 값 0.0은 순위가 가장 낮은 행을 나타내고, 1.0은 순위가 가장 높은 행을 나타냅니다. 특정 값이 분포 내에서 어디에 위치하는지 확인하려면 이 함수를 사용하세요.

:::

자세한 내용은 [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions) 문서를 참조하세요.
