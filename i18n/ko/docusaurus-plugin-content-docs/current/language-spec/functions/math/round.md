---
title: ROUND
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 ROUND 함수: 숫자 인수를 지정한 소수 자릿수로 반올림하며, 기본값은 0입니다."
image: "/img/stackql-featured-image.png"
---
숫자 인수를 반올림합니다.  

참고 항목:  
[[` SELECT `]](/language-spec/select) 

* * * 

## 구문 {#syntax}

```sql
SELECT ROUND(numeric_expression [, precision]) FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*numeric_expression*__  
정확한 수치 또는 근사 수치 데이터 형식 범주의 식입니다.

__*precision*__  
*numeric_expression*을 반올림할 소수점 오른쪽 자릿수입니다.

> *precision* 인수를 생략하면 0으로 간주됩니다.

## 반환 값 {#return-values}
숫자 값을 반환합니다.

* * *

## 예제 {#examples}

### 부동 소수점 숫자를 소수 둘째 자리로 반올림 {#round-a-floating-point-number-to-two-decimal-places}

```sql
SELECT name, timeCreated,
round(julianday('now')-julianday(timeCreated), 2) as days_since
FROM google.storage.buckets WHERE project = 'stackql';
```
자세한 내용은 [https://www.sqlite.org/lang_corefunc.html#round](https://www.sqlite.org/lang_corefunc.html#round) 문서를 참조하세요.
