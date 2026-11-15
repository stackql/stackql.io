---
title: SQRT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: StackQL에서 SQL로 숫자의 제곱근을 계산합니다.
image: "/img/stackql-featured-image.png"
---
음수가 아닌 숫자 식의 제곱근을 계산합니다.

참고 항목:  
[[`SELECT`]](/language-spec/select) 

* * * 

## 구문 {#syntax}

```sql
SELECT SQRT(numeric_expression) FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*numeric_expression*__  
제곱근을 계산할 음수가 아닌 숫자 식입니다.

## 반환 값 {#return-values}
지정한 음수가 아닌 숫자 값의 제곱근을 반환합니다.

* * *

## 예제 {#examples}

### 숫자의 제곱근 계산 {#calculate-the-square-root-of-a-number}

```sql
SELECT SQRT(9) AS square_root;
```
