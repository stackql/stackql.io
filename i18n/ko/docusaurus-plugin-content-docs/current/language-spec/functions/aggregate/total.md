---
title: TOTAL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 TOTAL 집계 함수: 열 또는 그룹에서 NULL이 아닌 값의 합계를 항상 부동 소수점 숫자로 반환합니다."
image: "/img/stackql-featured-image.png"
---
열 또는 열 그룹에 있는 `NULL`이 아닌 모든 값의 합계를 반환합니다.

참고 항목:  
[[` SELECT `]](/language-spec/select) [[` SUM `]](/language-spec/functions/aggregate/sum)

* * * 

## 구문 {#syntax}

```sql
SELECT TOTAL(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## 인수 {#arguments}

__*columnExpression*__  
열 또는 식입니다.

> *columnExpression*의 모든 값이 `NULL`이면 `TOTAL` 함수는 `0.0`을 반환합니다.

__*groupByColumn*__  
요약 또는 집계 작업을 수행하는 데 사용되는 하나 이상의 열입니다. `GROUP BY` 절은 열 그룹당 1행을 반환합니다.

## 반환 값 {#return-values}

*columnExpression*의 합계를 나타내는 부동 소수점 값을 반환합니다.

* * *

## 예제 {#examples}

### 전체 리소스에 대한 열 식의 합계 반환 {#return-the-sum-of-a-column-expression-over-an-entire-resource}

```sql
SELECT total(json_array_length(disks)) as total_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### 다른 열 식으로 그룹화된 열 식의 합계 반환 {#return-the-sum-of-a-column-expression-grouped-by-another-column-expression}

```sql
SELECT tags, total(json_array_length(disks)) as total_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY json_extract(tags, '$.instanceType');
```

자세한 내용은 [https://www.sqlite.org/lang_aggfunc.html#sumunc](https://www.sqlite.org/lang_aggfunc.html#sumunc) 문서를 참조하세요.
