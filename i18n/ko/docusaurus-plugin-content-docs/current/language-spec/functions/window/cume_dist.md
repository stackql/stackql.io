---
title: CUME_DIST
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 CUME_DIST 윈도우 함수: 파티션 내 현재 행의 누적 분포로, 행 번호를 파티션 행 수로 나눈 값입니다."
image: "/img/stackql-featured-image.png"
---
파티션 내 값의 누적 분포를 반환합니다. 이 값은 `row-number / partition-rows`로 계산됩니다. 여기서 `row-number`는 그룹의 마지막 피어에 대해 `ROW_NUMBER()` 함수가 반환하는 값이고, `partition-rows`는 파티션의 행 수입니다.

참고 항목:
[[` SELECT `]](/language-spec/select) [[` PERCENT_RANK `]](/language-spec/functions/window/percent_rank) [[` RANK `]](/language-spec/functions/window/rank)

* * *

## 구문 {#syntax}

```sql
SELECT CUME_DIST() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*PARTITION BY column*__
선택 사항입니다. 결과 집합을 파티션으로 나눕니다. `CUME_DIST` 함수는 각 파티션에 개별적으로 적용됩니다.

__*ORDER BY column*__
누적 분포를 계산하는 순서를 지정합니다.

## 반환 값 {#return-values}

누적 분포 값을 나타내는 0.0에서 1.0 사이의 부동 소수점 숫자를 반환합니다.

* * *

## 예제 {#examples}

### 기여자의 누적 분포 계산 {#calculate-cumulative-distribution-of-contributors}

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

`CUME_DIST` 함수는 값이 현재 행의 값보다 작거나 같은 행의 비율을 반환합니다. 값이 0.75이면 행의 75%가 현재 행의 값보다 작거나 같은 값을 가진다는 의미입니다.

:::

자세한 내용은 [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions) 문서를 참조하세요.
