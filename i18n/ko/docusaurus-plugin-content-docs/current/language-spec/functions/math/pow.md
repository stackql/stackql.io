---
title: POW
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: StackQL에서 SQL로 한 숫자를 다른 숫자만큼 거듭제곱한 값을 계산합니다.
image: "/img/stackql-featured-image.png"
---
*x*의 *y* 거듭제곱을 계산합니다.

참고 항목:  
[[`SELECT`]](/language-spec/select) 

* * * 

## 구문 {#syntax}

```sql
SELECT POW(x, y) FROM <multipartIdentifier>;
-- or using the equivalent
SELECT POWER(x, y) FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*x*__  
밑이 되는 수로, 숫자 식입니다.

__*y*__  
밑 *x*를 거듭제곱할 지수로, 역시 숫자 식입니다.

## 반환 값 {#return-values}
*x*의 *y* 거듭제곱 결과를 반환합니다.

* * *

## 예제 {#examples}

### x의 y 거듭제곱 계산 {#calculate-x-raised-to-the-power-y}


```sql
SELECT POW(2, 3) AS result;
```
