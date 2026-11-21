---
title: SUM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 SUM 집계 함수: 열 또는 그룹에서 NULL이 아닌 값의 합계를 반환하며, 모든 입력이 정수이면 정수로 반환합니다."
image: "/img/stackql-featured-image.png"
---
열 또는 열 그룹에 있는 `NULL`이 아닌 모든 값의 합계를 반환합니다.

참고 항목:  
[[` SELECT `]](/language-spec/select) [[` TOTAL `]](/language-spec/functions/aggregate/total)

* * * 

:::tip

부동 소수점 숫자 또는 long 값의 합계를 구하려면 [**TOTAL**](/language-spec/functions/aggregate/total) 함수를 사용하세요.

:::

## 구문 {#syntax}

```sql
SELECT SUM(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## 인수 {#arguments}

__*columnExpression*__  
열 또는 식입니다.

> *columnExpression*의 모든 값이 `NULL`이면 `SUM` 함수는 `NULL`을 반환합니다.

> 계산 중 어느 시점에서든 정수 오버플로가 발생하면 `SUM` 함수는 "integer overflow" 예외를 발생시킵니다. [`TOTAL`](/language-spec/functions/aggregate/total) 함수는 정수 오버플로 예외를 발생시키지 않습니다.

__*groupByColumn*__  
요약 또는 집계 작업을 수행하는 데 사용되는 하나 이상의 열입니다. `GROUP BY` 절은 열 그룹당 1행을 반환합니다.

## 반환 값 {#return-values}

`NULL`이 아닌 모든 입력이 정수이면 *columnExpression*의 합계를 나타내는 정수를 반환합니다. `NULL`이 아닌 입력 중 하나라도 정수가 아니면 실제 합계의 근삿값일 수 있는 부동 소수점 값을 반환합니다.

* * *

## 예제 {#examples}

### 전체 리소스에 대한 열 식의 합계 반환 {#return-the-sum-of-a-column-expression-over-an-entire-resource}

```sql
SELECT sum(json_array_length(disks)) as sum_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### 다른 열 식으로 그룹화된 열 식의 합계 반환 {#return-the-sum-of-a-column-expression-grouped-by-another-column-expression}

```sql
SELECT status, sum(json_array_length(disks)) as sum_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY status;
```

### `SUM` 함수를 윈도우 함수로 사용하여 누계 계산 {#use-sum-as-a-window-function-to-calculate-running-totals}

```sql
-- Calculate running total and percentage of contributions
SELECT
    login,
    contributions,
    SUM(contributions) OVER (ORDER BY contributions DESC) as running_total,
    SUM(contributions) OVER () as total_contributions,
    ROUND(100.0 * contributions / SUM(contributions) OVER (), 2) as pct_of_total
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

자세한 내용은 [https://www.sqlite.org/lang_aggfunc.html#sumunc](https://www.sqlite.org/lang_aggfunc.html#sumunc) 문서를 참조하세요.
