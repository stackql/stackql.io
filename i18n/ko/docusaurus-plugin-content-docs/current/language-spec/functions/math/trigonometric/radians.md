---
title: RADIANS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: StackQL에서 SQL로 각도를 도에서 라디안 단위로 변환합니다.
image: "/img/stackql-featured-image.png"
---
지정한 각도를 도에서 라디안 단위로 변환합니다.

참고 항목:  
[[`SELECT`]](/language-spec/select)

* * * 

## 구문 {#syntax}

```sql
SELECT RADIANS(angle_in_degrees) FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*angle_in_degrees*__  
도 단위의 각도를 나타내는 숫자 식입니다.

## 반환 값 {#return-values}
라디안 단위로 환산한 각도를 반환합니다.

* * *

## 예제 {#examples}

### 180도를 라디안 단위로 변환 {#convert-180-degrees-to-radians}

```sql
SELECT RADIANS(180) AS radians_value;
```

180도를 라디안 단위로 변환하면 π 라디안이 되므로 이 쿼리는 π(약 `3.14159`)를 반환합니다.

### 90도를 라디안 단위로 변환 {#convert-90-degrees-to-radians}

```sql
SELECT RADIANS(90) AS radians_value;
```

이 쿼리는 90도를 라디안 단위로 변환하며, 90도는 180도의 절반이므로 결과는 π/2, 즉 약 `1.5708`입니다.
