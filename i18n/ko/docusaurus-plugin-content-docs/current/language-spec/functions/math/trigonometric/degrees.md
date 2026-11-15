---
title: DEGREES
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: StackQL에서 SQL로 각도를 라디안에서 도 단위로 변환합니다.
image: "/img/stackql-featured-image.png"
---
지정한 각도를 라디안에서 도 단위로 변환합니다.

참고 항목:  
[[`SELECT`]](/language-spec/select)

* * * 

## 구문 {#syntax}

```sql
SELECT DEGREES(angle_in_radians) FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*angle_in_radians*__  
라디안 단위의 각도를 나타내는 숫자 식입니다.

## 반환 값 {#return-values}
도 단위로 환산한 각도를 반환합니다.

* * *

## 예제 {#examples}

### π 라디안을 도 단위로 변환 {#convert-π-radians-to-degrees}

```sql
SELECT DEGREES(3.14159) AS degrees_value;
```

π 라디안(약 `3.14159`)을 변환하면 약 180도가 되므로 이 쿼리는 약 `180`을 반환합니다.

### π/2 라디안을 도 단위로 변환 {#convert-π2-radians-to-degrees}

```sql
SELECT DEGREES(1.5708) AS degrees_value;
```

이 쿼리는 π/2 라디안(약 `1.5708`)을 도 단위로 변환하며, π/2 라디안은 90도 각도에 해당하므로 결과는 약 `90`입니다.
