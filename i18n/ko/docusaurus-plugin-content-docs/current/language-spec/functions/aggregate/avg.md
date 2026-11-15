---
title: AVG
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 AVG 집계 함수: 열 또는 그룹에 있는 NULL이 아닌 값의 평균을 반환합니다."
image: "/img/stackql-featured-image.png"
---
열 또는 열 그룹 내에서 `NULL`이 아닌 모든 값의 평균값을 반환합니다.

참고 항목:  
[[` SELECT `]](/language-spec/select)

* * * 

## 구문 {#syntax}

```sql
SELECT AVG(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## 인수 {#arguments}

__*columnExpression*__  
열 또는 식입니다.

> 숫자 값으로 표현할 수 없는 문자열 및 BLOB 값은 `0`으로 해석됩니다.

> *columnExpression*의 값 중 하나 이상은 `NULL`이 아니어야 합니다. 그렇지 않으면 `NULL`이 반환됩니다.

__*groupByColumn*__  
요약 또는 집계 작업을 수행하는 데 사용되는 하나 이상의 열입니다. `GROUP BY` 절은 열 그룹당 1행을 반환합니다.

## 반환 값 {#return-values}

*columnExpression*의 평균을 나타내는 부동 소수점 값을 반환합니다.

* * *

## 예제 {#examples}

### 전체 리소스에 대한 열 식의 평균 반환 {#return-the-average-of-a-column-expression-over-an-entire-resource}

```sql
SELECT avg(json_array_length(disks)) as avg_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### 다른 열 식으로 그룹화된 열 식의 평균 반환 {#return-the-average-of-a-column-expression-grouped-by-another-column-expression}

```sql
SELECT tags, avg(json_array_length(disks)) as avg_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY json_extract(tags, '$.instanceType');
```

### `AVG` 함수를 윈도우 함수로 사용하여 이동 평균 계산 {#use-avg-as-a-window-function-to-calculate-moving-averages}

```sql
-- Calculate moving average of commit activity
WITH weekly_totals AS (
    SELECT
        week,
        SUM(json_each.value) as commits_this_week
    FROM github.repos.stats_commit_activity, JSON_EACH(days)
    WHERE owner = 'stackql'
      AND repo = 'stackql'
    GROUP BY week
)
SELECT
    week,
    commits_this_week,
    ROUND(AVG(commits_this_week) OVER (ORDER BY week ROWS BETWEEN 3 PRECEDING AND CURRENT ROW), 1) as four_week_moving_avg
FROM weekly_totals
ORDER BY week;
```

자세한 내용은 [https://www.sqlite.org/lang_aggfunc.html#avg](https://www.sqlite.org/lang_aggfunc.html#avg) 문서를 참조하세요.
