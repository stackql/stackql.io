---
title: ROW_NUMBER
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 ROW_NUMBER 윈도우 함수: 윈도우의 ORDER BY 절이 정의하는 순서에 따라 파티션 내 행에 1부터 번호를 매깁니다."
image: "/img/stackql-featured-image.png"
---
현재 파티션 내 행의 번호를 반환합니다. 행 번호는 윈도우 정의의 `ORDER BY` 절이 있으면 그 절이 정의하는 순서에 따라, 없으면 임의의 순서로 1부터 매겨집니다.

참고 항목:
[[` SELECT `]](/language-spec/select) [[` RANK `]](/language-spec/functions/window/rank) [[` DENSE_RANK `]](/language-spec/functions/window/dense_rank)

* * *

## 구문 {#syntax}

```sql
SELECT ROW_NUMBER() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*PARTITION BY column*__
선택 사항입니다. 결과 집합을 파티션으로 나눕니다. `ROW_NUMBER` 함수는 각 파티션에 개별적으로 적용되며, 파티션마다 행 번호를 다시 매깁니다.

__*ORDER BY column*__
행 번호를 할당하는 순서를 지정합니다. 의미 있는 결과를 얻으려면 필요합니다.

## 반환 값 {#return-values}

파티션 내에서 1부터 시작하는 순차 행 번호를 나타내는 정수를 반환합니다.

* * *

## 예제 {#examples}

### 리포지토리 내 기여자 순위 지정 {#rank-contributors-within-a-repository}

```sql
-- Rank contributors within a repository
SELECT
    login,
    contributions,
    ROW_NUMBER() OVER (ORDER BY contributions DESC) as contribution_rank
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

### 시간에 따른 이슈 순서 추적 {#track-issue-sequence-over-time}

```sql
-- Track issue growth over time
SELECT
    number,
    title,
    state,
    created_at,
    ROW_NUMBER() OVER (ORDER BY created_at) as issue_sequence
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY created_at;
```

### `ROW_NUMBER` 함수와 `RANK`, `DENSE_RANK` 함수 비교 {#compare-row_number-with-rank-and-dense_rank}

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

자세한 내용은 [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions) 문서를 참조하세요.
