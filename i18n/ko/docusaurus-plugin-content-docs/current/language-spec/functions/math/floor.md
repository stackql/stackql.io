---
title: FLOOR
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 FLOOR 함수: 숫자 인수를 가장 가까운 정수로 내림합니다."
image: "/img/stackql-featured-image.png"
---
숫자 인수를 가장 가까운 정수로 내림합니다.  

참고 항목:  
[[`SELECT`]](/language-spec/select) 

* * * 

## 구문 {#syntax}

```sql
SELECT FLOOR(numeric_expression) FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*numeric_expression*__  
정확한 수치 또는 근사 수치 데이터 형식 범주의 식입니다.

## 반환 값 {#return-values}
지정한 숫자 식보다 작거나 같은 가장 큰 정수를 반환합니다.

* * *

## 예제 {#examples}

### 부동 소수점 숫자를 가장 가까운 (더 작은) 정수로 내림 {#round-down-a-floating-point-number-to-the-closest-smaller-integer}

```sql
SELECT name, timeCreated,
floor(julianday('now')-julianday(timeCreated)) as days_since_ceiling
FROM google.storage.buckets WHERE project = 'stackql';
```
