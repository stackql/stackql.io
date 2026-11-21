---
title: ABS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 ABS 함수: 숫자 인수의 절댓값."
image: "/img/stackql-featured-image.png"
---
숫자 인수의 절댓값을 반환합니다.  

참고 항목:  
[[` SELECT `]](/language-spec/select) 

* * * 

## 구문 {#syntax}

```sql
SELECT ABS(numeric_expression) FROM <multipartIdentifier>;
```

> `X`가 `NULL`이면 `abs(X)`는 `NULL`을 반환합니다. `X`가 숫자 값으로 변환할 수 없는 문자열 또는 blob이면 `abs(X)`는 `0.0`을 반환합니다. `X`가 정수 -9223372036854775808이면 이에 해당하는 양의 64비트 2의 보수 값이 없으므로 `abs(X)`는 정수 오버플로 오류를 발생시킵니다.

## 인수 {#arguments}

__*numeric_expression*__  
정확한 수치 또는 근사 수치 데이터 형식 범주의 식입니다.

## 반환 값 {#return-values}

음수가 아닌 숫자 값을 반환합니다.

* * *

## 예제 {#examples}

### 숫자의 절댓값 반환 {#return-the-absolute-value-of-a-number}

```sql
SELECT name, abs(strftime('%s', creationTimestamp)-strftime('%s','now')) as abs_value
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

자세한 내용은 [https://www.sqlite.org/lang_corefunc.html#abs](https://www.sqlite.org/lang_corefunc.html#abs) 문서를 참조하세요.
