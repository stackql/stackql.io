---
title: SIN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: StackQL에서 SQL로 라디안 단위로 지정한 각도의 사인을 계산합니다.
image: "/img/stackql-featured-image.png"
---
라디안 단위로 지정한 각도의 사인을 계산합니다.

참고 항목:  
[[`SELECT`]](/language-spec/select)

* * * 

## 구문 {#syntax}

```sql
SELECT SIN(radian_value) FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*radian_value*__  
사인을 계산할 각도를 라디안 단위로 지정하는 숫자 식입니다.

## 반환 값 {#return-values}
지정한 각도의 사인을 반환합니다.

* * *

## 예제 {#examples}

### π/2 라디안의 사인 계산 {#calculate-the-sine-of-π2-radians}

```sql
SELECT SIN(PI() / 2) AS sine_value;
```

π/2 라디안의 사인은 1이므로 이 쿼리는 `1`을 반환합니다.

### π 라디안의 사인 계산 {#calculate-the-sine-of-π-radians}

```sql
SELECT SIN(PI()) AS sine_value;
```

이 쿼리는 π 라디안의 사인을 계산하며, π의 사인은 0이므로 결과는 약 `0`입니다.
