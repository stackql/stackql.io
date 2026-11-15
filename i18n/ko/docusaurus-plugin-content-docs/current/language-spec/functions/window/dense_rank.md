---
title: DENSE_RANK
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 DENSE_RANK 윈도우 함수: 파티션 내 현재 행의 간격 없는 순위로, 동순위 행은 같은 순위를 공유하고 다음 순위가 바로 이어집니다."
image: "/img/stackql-featured-image.png"
---
현재 행의 순위를 간격 없이 반환합니다. 이 값은 파티션 내에서 현재 행이 속한 피어 그룹의 번호입니다. 정렬 열의 값이 같은 행은 같은 순위를 받으며, 다음 순위 값은 항상 1씩 증가합니다(간격 없음).

행 번호는 윈도우 정의의 `ORDER BY` 절이 정의하는 순서에 따라 1부터 매겨집니다. `ORDER BY` 절이 없으면 모든 행이 피어로 간주되며 이 함수는 항상 1을 반환합니다.

참고 항목:
[[` SELECT `]](/language-spec/select) [[` ROW_NUMBER `]](/language-spec/functions/window/row_number) [[` RANK `]](/language-spec/functions/window/rank)

* * *

## 구문 {#syntax}

```sql
SELECT DENSE_RANK() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*PARTITION BY column*__
선택 사항입니다. 결과 집합을 파티션으로 나눕니다. `DENSE_RANK` 함수는 각 파티션에 개별적으로 적용됩니다.

__*ORDER BY column*__
순위를 할당하는 순서를 지정합니다. `ORDER BY` 절이 없으면 모든 행이 피어로 간주되며 순위 1을 받습니다.

## 반환 값 {#return-values}

파티션 내 현재 행의 밀집 순위를 나타내는 정수를 반환하며, 동순위 값 뒤에 간격을 두지 않습니다.

* * *

## 예제 {#examples}

### 기여 수에 따른 기여자 순위 지정(간격 없음) {#rank-contributors-by-contributions-without-gaps}

```sql
-- Rank contributors without gaps for ties
SELECT
    DENSE_RANK() OVER (ORDER BY contributions DESC) as rank,
    login,
    contributions,
    html_url
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo IN ('stackql', 'stackql-deploy', 'stackql-provider-registry')
ORDER BY contributions DESC;
```

### `DENSE_RANK` 함수와 `RANK`, `ROW_NUMBER` 함수 비교 {#compare-dense_rank-with-rank-and-row_number}

```sql
-- Compare RANK vs DENSE_RANK vs ROW_NUMBER for contributors
SELECT
    login,
    contributions,
    RANK() OVER (ORDER BY contributions DESC) as rank,
    DENSE_RANK() OVER (ORDER BY contributions DESC) as dense_rank,
    ROW_NUMBER() OVER (ORDER BY contributions DESC) as row_num
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

### 여러 리포지토리에 걸친 집계 기여 수 순위 지정 {#rank-aggregated-contributions-across-multiple-repositories}

```sql
-- Rank total contributions across multiple repos
WITH all_contributors AS (
    SELECT login, contributions, html_url
    FROM github.repos.contributors
    WHERE owner = 'stackql' AND repo = 'stackql'
    UNION ALL
    SELECT login, contributions, html_url
    FROM github.repos.contributors
    WHERE owner = 'stackql' AND repo = 'stackql-deploy'
    UNION ALL
    SELECT login, contributions, html_url
    FROM github.repos.contributors
    WHERE owner = 'stackql' AND repo = 'stackql-provider-registry'
)
SELECT
    DENSE_RANK() OVER (ORDER BY SUM(contributions) DESC) as rank,
    login,
    SUM(contributions) as total_contributions,
    MAX(html_url) as html_url
FROM all_contributors
GROUP BY login
ORDER BY total_contributions DESC;
```

자세한 내용은 [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions) 문서를 참조하세요.
