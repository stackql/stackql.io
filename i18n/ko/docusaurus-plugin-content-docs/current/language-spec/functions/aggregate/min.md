---
title: MIN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 MIN 집계 함수: 열 또는 열 그룹에서 가장 작은 값을 반환합니다."
image: "/img/stackql-featured-image.png"
---
열 입력 또는 열 그룹을 기준으로 최솟값을 반환합니다.

참고 항목:  
[[` SELECT `]](/language-spec/select) 

* * * 

## 구문 {#syntax}

```sql
/* aggregate function */
SELECT MIN(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```
```sql
/* scalar (multi-argument) function */
SELECT MIN(scalarExpression1, scalarExpressionN, ...) FROM <multipartIdentifier>;
```

## 인수 {#arguments}

### 집계 함수 {#aggregate-function}

__*columnExpression*__  
연산자(`+`, `-`, ...)를 포함하는 열 또는 식입니다.

> `MIN` 함수는 열 데이터 형식을 기준으로 지정된 열의 가장 작은 값을 반환합니다. 예를 들어 정수 열에 대한 `MIN` 작업은 해당 열 또는 열 그룹에서 가장 작은 정수를 반환하는 반면, 문자열 열 또는 열 그룹에 대한 `MIN` 작업은 `BINARY` 데이터 정렬 함수로 문자열을 비교했을 때 처음으로 정렬되는 값을 반환합니다(바이트 단위로 비교하므로 대문자가 소문자보다 먼저 정렬됩니다).

__*groupByColumn*__  
요약 또는 집계 작업을 수행하는 데 사용되는 하나 이상의 열입니다. `GROUP BY` 절은 열 그룹당 1행을 반환합니다.

> `MIN` 함수는 `NULL` 값을 무시합니다.

> 그룹의 모든 값이 `NULL`이면 `MIN` 함수는 `NULL`을 반환합니다.

### 스칼라 함수 {#scalar-function}

__*scalarExpression*__  
가장 작은 값을 결정하여 반환할 대상인 열 또는 식의 목록(2개 이상)입니다.

> 인수 중 하나라도 `NULL`이면 스칼라 `MIN` 함수는 `NULL`을 반환합니다.

> 스칼라 또는 다중 인수 `MIN` 함수는 인수를 왼쪽에서 오른쪽으로 검색하여 데이터 정렬 함수를 정의하는 인수를 찾고, 그 데이터 정렬 함수를 모든 문자열 비교에 사용합니다.

> 스칼라 `MIN` 함수의 인수 중 어느 것도 데이터 정렬 함수를 정의하지 않으면 `BINARY` 데이터 정렬 함수가 사용됩니다.

> 인수가 하나만 제공되면 `MIN` 집계 함수가 호출됩니다.

## 반환 값 {#return-values}

입력 데이터 형식(또는 그룹)을 기준으로 최솟값을 반환합니다.

* * *

## 예제 {#examples}

### 열의 최솟값 반환 {#return-the-minimum-value-for-a-column}

```sql
SELECT name, location, min(timeCreated) 
FROM google.storage.buckets WHERE project = 'stackql';
```

### 열의 각 값에 대한 최솟값 반환 {#return-the-minimum-value-for-each-value-in-a-column}

```sql
SELECT name, location,
min(round(julianday('now')-julianday(timeCreated))) as age
FROM google.storage.buckets WHERE project = 'stackql'
GROUP BY location;
```

### 스칼라 `min` 함수를 사용하여 값 목록에서 최솟값 반환 {#return-the-minimum-value-from-a-list-of-values-using-the-scalar-min-function}

```sql
SELECT min(json_extract(disks, '$[0].diskSizeGb'),json_extract(disks, '$[1].diskSizeGb')) as smallest_disk
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### `MIN` 함수를 윈도우 함수로 사용하여 파티션 내 최솟값 조회 {#use-min-as-a-window-function-to-find-minimum-values-within-partitions}

```sql
-- Find the earliest issue in each state
SELECT
    number,
    title,
    state,
    created_at,
    MIN(created_at) OVER (PARTITION BY state) as earliest_in_state
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY state, created_at;
```

자세한 내용은 [https://www.sqlite.org/lang_aggfunc.html#min_agg](https://www.sqlite.org/lang_aggfunc.html#min_agg) 또는 [https://www.sqlite.org/lang_corefunc.html#min_scalar](https://www.sqlite.org/lang_corefunc.html#min_scalar) 페이지를 참조하세요
