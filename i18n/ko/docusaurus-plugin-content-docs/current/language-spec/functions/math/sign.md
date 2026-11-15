---
title: SIGN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 SIGN 함수: 숫자 인수가 음수, 0, 양수인지에 따라 -1, 0 또는 1을 반환합니다."
image: "/img/stackql-featured-image.png"
---
입력 값이 음수이면 -1, 0이면 0, 양수이면 1을 반환합니다.  

참고 항목:  
[[` SELECT `]](/language-spec/select) 

* * * 

## 구문 {#syntax}

```sql
SELECT SIGN(numeric_expression) FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*numeric_expression*__  
정확한 수치 또는 근사 수치 데이터 형식 범주의 식입니다.

> *numeric_expression*이 `NULL`이거나 손실 없이 숫자로 변환할 수 없는 `string` 또는 BLOB이면 함수는 `NULL`을 반환합니다.

## 반환 값 {#return-values}

`-1`, `0` 또는 `1`을 반환합니다.

* * *

## 예제 {#examples}

### 숫자의 부호 확인 {#determine-the-sign-of-a-number}

```sql
SELECT name, sign(strftime('%s', creationTimestamp)-strftime('%s','now')) as sign
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

자세한 내용은 [https://www.sqlite.org/lang_corefunc.html#sign](https://www.sqlite.org/lang_corefunc.html#sign) 문서를 참조하세요.
