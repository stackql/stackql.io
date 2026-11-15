---
title: COUNT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 COUNT 집계 함수: 리소스의 행 수 또는 그룹 내 열의 NULL이 아닌 값의 수를 반환합니다."
image: "/img/stackql-featured-image.png"
---
리소스의 전체 레코드 수 또는 그룹 내 열에서 `NULL`이 아닌 인스턴스의 수를 반환합니다.

참고 항목:  
[[` SELECT `]](/language-spec/select)

* * * 

## 구문 {#syntax}

```sql
SELECT COUNT( * | columnExpression ) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## 인수 {#arguments}

__*columnExpression*__ 또는 __*__  
열 또는 식입니다. __*__ 기호를 지정하면 리소스 또는 그룹의 모든 레코드 수를 셉니다.

> *columnExpression*의 값 중 하나 이상은 `NULL`이 아니어야 합니다. 그렇지 않으면 `NULL`이 반환됩니다.

__*groupByColumn*__  
요약 또는 집계 작업을 수행하는 데 사용되는 하나 이상의 열입니다. `GROUP BY` 절은 열 그룹당 1행을 반환합니다.

## 반환 값 {#return-values}

개수를 나타내는 정수를 반환합니다.

* * *

## 예제 {#examples}

### 전체 리소스의 행 수 반환 {#return-the-number-of-rows-in-an-entire-resource}

```sql
SELECT count(*) as num_instances
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### 열 식으로 그룹화된 행 수 반환 {#return-the-number-of-rows-grouped-by-a-column-expression}

```sql
SELECT tags, count(*) as num_instances
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY json_extract(tags, '$.instanceType');
```

### `COUNT` 함수를 윈도우 함수로 사용하여 누적 개수 계산 {#use-count-as-a-window-function-to-calculate-cumulative-counts}

```sql
-- Track cumulative issue counts over time
SELECT
    number,
    title,
    state,
    created_at,
    COUNT(*) OVER (ORDER BY created_at) as cumulative_issues,
    COUNT(*) OVER (PARTITION BY state ORDER BY created_at) as cumulative_by_state
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY created_at;
```

자세한 내용은 [https://www.sqlite.org/lang_aggfunc.html#count](https://www.sqlite.org/lang_aggfunc.html#count) 문서를 참조하세요.
