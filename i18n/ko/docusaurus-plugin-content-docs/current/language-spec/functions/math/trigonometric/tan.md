---
title: TAN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: StackQL에서 SQL로 라디안 단위로 지정한 각도의 탄젠트를 계산합니다.
image: "/img/stackql-featured-image.png"
---
라디안 단위로 지정한 각도의 탄젠트를 계산합니다.

참고 항목:  
[[`SELECT`]](/language-spec/select)

* * * 

## 구문 {#syntax}

```sql
SELECT TAN(radian_value) FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*radian_value*__  
탄젠트를 계산할 각도를 라디안 단위로 지정하는 숫자 식입니다.

## 반환 값 {#return-values}
지정한 각도의 탄젠트를 반환합니다.

* * *

## 예제 {#examples}

### π/4 라디안의 탄젠트 계산 {#calculate-the-tangent-of-π4-radians}

```sql
SELECT TAN(PI() / 4) AS tangent_value;
```

π/4 라디안의 탄젠트는 1이므로 이 쿼리는 `1`을 반환합니다.

### π 라디안의 탄젠트 계산 {#calculate-the-tangent-of-π-radians}

```sql
SELECT TAN(PI()) AS tangent_value;
```

이 쿼리는 π 라디안의 탄젠트를 계산하며, π의 탄젠트는 0에 매우 가까우므로 결과는 약 `0`입니다.
