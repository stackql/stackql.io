---
title: NTILE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 NTILE 윈도우 함수: 파티션을 가능한 한 균등하게 N개 그룹으로 나누고 각 행의 그룹 번호를 1부터 N까지 매깁니다."
image: "/img/stackql-featured-image.png"
---
파티션을 가능한 한 균등하게 N개 그룹으로 나누고, `ORDER BY` 절이 정의하는 순서(없으면 임의의 순서)에 따라 각 그룹에 1에서 N 사이의 정수를 할당합니다. 필요한 경우 더 큰 그룹이 먼저 옵니다.

참고 항목:
[[` SELECT `]](/language-spec/select) [[` RANK `]](/language-spec/functions/window/rank)

* * *

## 구문 {#syntax}

```sql
SELECT NTILE(N) OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*N*__
파티션을 나눌 그룹(타일)의 수를 지정하는 양의 정수입니다.

__*PARTITION BY column*__
선택 사항입니다. 결과 집합을 파티션으로 나눕니다. `NTILE` 함수는 각 파티션에 개별적으로 적용됩니다.

__*ORDER BY column*__
행을 타일에 할당하는 순서를 지정합니다.

## 반환 값 {#return-values}

현재 행이 속한 타일(그룹)을 나타내는 1에서 N 사이의 정수를 반환합니다.

* * *

## 예제 {#examples}

### 기여 수준에 따라 기여자를 사분위로 그룹화 {#group-contributors-into-quartiles-by-contribution-level}

```sql
-- Group contributors into quartiles by contribution level
SELECT
    login,
    contributions,
    NTILE(4) OVER (ORDER BY contributions DESC) as contribution_quartile,
    CASE NTILE(4) OVER (ORDER BY contributions DESC)
        WHEN 1 THEN 'Top Contributors'
        WHEN 2 THEN 'Active Contributors'
        WHEN 3 THEN 'Moderate Contributors'
        WHEN 4 THEN 'Occasional Contributors'
    END as contributor_tier
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

:::tip

`NTILE` 함수를 사용하면 데이터에서 세그먼트나 등급을 만들 수 있습니다. 일반적인 사용 사례로는 데이터를 사분위수(N=4), 십분위수(N=10), 백분위수(N=100)로 나누는 것이 있습니다.

:::

자세한 내용은 [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions) 문서를 참조하세요.
