---
title: LAG
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 LAG 윈도우 함수: 파티션의 이전 행에 대해 식을 평가하며, 오프셋과 기본값을 선택적으로 지정할 수 있습니다."
image: "/img/stackql-featured-image.png"
---
파티션의 이전 행에 대해 식을 평가한 결과를 반환합니다.

`LAG()` 함수의 첫 번째 형식은 파티션의 바로 이전 행에 대해 식을 평가한 결과를 반환합니다. 이전 행이 없으면(현재 행이 첫 번째 행이기 때문에) `NULL`을 반환합니다.

offset 인수를 지정하는 경우 음수가 아닌 정수여야 합니다. 반환되는 값은 파티션 내에서 현재 행보다 *offset*개 행 앞에 있는 행에 대해 식을 평가한 결과입니다. offset이 0이면 현재 행에 대해 식을 평가합니다. 현재 행보다 *offset*개 행 앞에 행이 없으면 `NULL`을 반환합니다.

기본값도 지정한 경우, offset으로 식별되는 행이 없으면 `NULL` 대신 기본값을 반환합니다.

참고 항목:
[[` SELECT `]](/language-spec/select) [[` LEAD `]](/language-spec/functions/window/lead)

* * *

## 구문 {#syntax}

```sql
SELECT LAG(expr) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;

SELECT LAG(expr, offset) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;

SELECT LAG(expr, offset, default) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*expr*__
이전 행에 대해 평가할 식입니다.

__*offset*__
선택 사항입니다. 몇 행 앞의 행을 참조할지 지정하는 음수가 아닌 정수입니다. 기본값은 1입니다.

__*default*__
선택 사항입니다. offset 위치의 행이 없을 때 반환할 값입니다. 기본값은 `NULL`입니다.

__*PARTITION BY column*__
선택 사항입니다. 결과 집합을 파티션으로 나눕니다. `LAG` 함수는 각 파티션 내에서 개별적으로 적용됩니다.

__*ORDER BY column*__
행을 처리하는 순서를 지정합니다.

## 반환 값 {#return-values}

지정한 이전 행에 대해 평가한 식의 값을 반환합니다. 해당 행이 없으면 `NULL`(또는 기본값)을 반환합니다.

* * *

## 예제 {#examples}

### 각 릴리스와 이전 릴리스 비교 {#compare-each-release-to-the-previous-release}

```sql
-- Compare each release to the previous release
SELECT
    tag_name,
    name,
    published_at,
    LAG(tag_name, 1) OVER (ORDER BY published_at) as previous_release,
    LAG(published_at, 1) OVER (ORDER BY published_at) as previous_release_date,
    julianday(published_at) - julianday(LAG(published_at, 1) OVER (ORDER BY published_at)) as days_since_last_release
FROM github.repos.releases
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY published_at;
```

### 이슈 생성 간격 분석 {#analyze-gaps-between-issue-creation}

```sql
-- Analyze gaps between issue creation
SELECT
    number,
    title,
    created_at,
    LAG(created_at, 1) OVER (ORDER BY created_at) as prev_issue_date,
    julianday(created_at) - julianday(LAG(created_at, 1) OVER (ORDER BY created_at)) as days_since_prev_issue
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY created_at;
```

자세한 내용은 [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions) 문서를 참조하세요.
