---
title: MOD
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: StackQL에서 SQL로 한 숫자를 다른 숫자로 나눈 나머지를 계산합니다.
image: "/img/stackql-featured-image.png"
---
*x*를 *y*로 나눈 나머지를 계산하여 모듈러스를 반환합니다. '%' 연산자를 사용하는 것과 비슷하지만 정수가 아닌 인수와도 호환됩니다.

참고 항목:  
[[`SELECT`]](/language-spec/select) 

* * * 

## 구문 {#syntax}

```sql
SELECT MOD(x, y) FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*x*__  
피제수로, 숫자 식입니다.

__*y*__  
제수로, 역시 숫자 식입니다.

## 반환 값 {#return-values}
*x*를 *y*로 나눈 나머지를 반환합니다.

* * *

## 예제 {#examples}

### 10을 3으로 나눈 모듈러스 계산 {#calculate-the-modulus-of-10-divided-by-3}

```sql
SELECT MOD(10, 3) AS remainder;
```
