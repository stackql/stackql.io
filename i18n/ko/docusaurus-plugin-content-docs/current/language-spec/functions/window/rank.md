---
title: RANK
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 RANK 윈도우 함수: 파티션 내 현재 행의 간격 있는 순위로, 동순위 행은 같은 순위를 공유하고 이후 순위는 건너뜁니다."
image: "/img/stackql-featured-image.png"
---
현재 행의 순위를 간격을 두고 반환합니다. 이 값은 각 그룹의 첫 번째 피어의 `ROW_NUMBER` 값입니다. 정렬 열의 값이 같은 행은 같은 순위를 받으며, 다음 순위 값은 동순위 행의 수만큼 증가합니다(간격이 생김).

`ORDER BY` 절이 없으면 모든 행이 피어로 간주되며 이 함수는 항상 1을 반환합니다.

참고 항목:
[[` SELECT `]](/language-spec/select) [[` ROW_NUMBER `]](/language-spec/functions/window/row_number) [[` DENSE_RANK `]](/language-spec/functions/window/dense_rank)

* * *

## 구문 {#syntax}

```sql
SELECT RANK() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*PARTITION BY column*__
선택 사항입니다. 결과 집합을 파티션으로 나눕니다. `RANK` 함수는 각 파티션에 개별적으로 적용됩니다.

__*ORDER BY column*__
순위를 할당하는 순서를 지정합니다. `ORDER BY` 절이 없으면 모든 행이 피어로 간주되며 순위 1을 받습니다.

## 반환 값 {#return-values}

파티션 내 현재 행의 순위를 나타내는 정수를 반환하며, 동순위 값 뒤에는 간격이 생깁니다.

* * *

## 예제 {#examples}

### 기여 수에 따른 기여자 순위 지정(간격 있음) {#rank-contributors-by-contributions-with-gaps}

```sql
-- Rank contributors with gaps for ties
SELECT
    login,
    contributions,
    RANK() OVER (ORDER BY contributions DESC) as rank
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

### `RANK` 함수와 `DENSE_RANK`, `ROW_NUMBER` 함수 비교 {#compare-rank-with-dense_rank-and-row_number}

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

### `PARTITION BY` 절을 사용한 리포지토리별 기여자 순위 지정 {#rank-contributors-within-each-repository-using-partition-by}

```sql
-- Rank contributors within each repo
WITH repo_contributors AS (
    SELECT
        repo,
        login,
        contributions,
        RANK() OVER (PARTITION BY repo ORDER BY contributions DESC) as rank_in_repo
    FROM (
        SELECT 'stackql' as repo, login, contributions
        FROM github.repos.contributors
        WHERE owner = 'stackql' AND repo = 'stackql'
        UNION ALL
        SELECT 'stackql-provider-registry' as repo, login, contributions
        FROM github.repos.contributors
        WHERE owner = 'stackql' AND repo = 'stackql-provider-registry'
    ) t
)
SELECT
    repo,
    login,
    contributions,
    rank_in_repo
FROM repo_contributors
ORDER BY repo, rank_in_repo;
```

자세한 내용은 [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions) 문서를 참조하세요.
