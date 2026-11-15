---
title: JULIANDAY
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 JULIANDAY 함수: 기원전 4714년 11월 24일 정오부터 시간 값까지 경과한 일수를 소수로 나타내며 날짜 연산에 유용합니다."
image: "/img/stackql-featured-image.png"
---
율리우스일을 반환합니다. 율리우스일은 그리니치 기준 기원전 4714년 11월 24일 [(역산 그레고리력)](https://en.wikipedia.org/wiki/Proleptic_Gregorian_calendar) 정오 이후 경과한 일수입니다.  

참고 항목:  
[[` SELECT `]](/language-spec/select) [[ 날짜 한정자 ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## 구문 {#syntax}

```sql
SELECT JULIANDAY(datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*datetime_expression*__  
날짜/시간 값입니다.

__*modifier*__  
*datetime_expression*에 적용할 0개 이상의 한정자입니다. 한정자에 대한 자세한 내용은 [**날짜 한정자**](/language-spec/functions/datetime/date_modifiers)를 참조하세요.

## 반환 값 {#return-values}

*modifier*가 적용된 입력 *datetime_expression*의 [율리우스일](https://en.wikipedia.org/wiki/Julian_day)을 반환합니다. 이 값은 [역산 그레고리력](https://en.wikipedia.org/wiki/Proleptic_Gregorian_calendar) 기준 기원전 4714년 11월 24일 그리니치 정오부터 경과한 일수이며, 하루 중 시각에 해당하는 소수 부분을 포함합니다.

* * *

## 예제 {#examples}

### 두 날짜 사이의 일수 계산 {#calculate-the-days-between-two-dates}

```sql
SELECT name, timeCreated,
round(julianday('now')-julianday(timeCreated)) as days_since
FROM google.storage.buckets WHERE project = 'stackql';
```

:::tip 

부동 소수점 숫자를 가장 가까운 정수로 변환하려면 [**ROUND**](/language-spec/functions/math/round) 함수를 사용하세요.

:::

자세한 내용은 [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html) 문서를 참조하세요.
