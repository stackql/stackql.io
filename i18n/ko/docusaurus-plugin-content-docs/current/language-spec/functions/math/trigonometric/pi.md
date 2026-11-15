---
title: PI
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: StackQL에서 SQL로 수학 상수 π(파이)를 조회합니다.
image: "/img/stackql-featured-image.png"
---
원의 둘레와 지름의 비인 수학 상수 π(파이)를 반환합니다.

참고 항목:  
[[`SELECT`]](/language-spec/select) 

* * * 

## 구문 {#syntax}

```sql
SELECT PI() AS pi_value;
```

## 인수 {#arguments}

이 함수에는 인수가 필요하지 않습니다.

## 반환 값 {#return-values}
약 3.14159인 상수 π를 반환합니다.

* * *

## 예제 {#examples}

### π 값 조회 {#retrieve-the-value-of-π}

```sql
SELECT PI() AS pi_value;
```

이 쿼리는 약 `3.14159`인 π 값을 출력합니다. 이 값은 원과 기타 기하 함수가 관련된 다양한 수학 계산에 유용합니다.
