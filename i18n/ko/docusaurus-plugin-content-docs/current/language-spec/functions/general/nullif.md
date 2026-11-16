---
title: NULLIF
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 NULLIF 함수: 두 인수가 같으면 NULL을 반환하고, 그렇지 않으면 첫 번째 인수를 반환합니다."
image: "/img/stackql-featured-image.png"
---
2개의 인수를 받아 두 인수가 같으면 `NULL` 값을 반환하고, 그렇지 않으면 첫 번째 인수를 반환합니다.  

> `NULLIF` 함수는 `CASE WHEN expression1 = expression2 THEN NULL ELSE expression1 END` 식과 같습니다.

> `NULLIF` 함수는 결과 집합에 `NULL`은 아니지만 집계 시 `NULL`로 처리하려는 특수 값이 있을 때 [`AVG`](/language-spec/functions/aggregate/avg), [`MAX`](/language-spec/functions/aggregate/max), [`MIN`](/language-spec/functions/aggregate/min), [`SUM`](/language-spec/functions/aggregate/sum), [`COUNT`](/language-spec/functions/aggregate/count) 등의 집계 함수와 함께 사용하면 유용합니다.

참고 항목:  
[[` SELECT `]](/language-spec/select) [[` COALESCE `]](/language-spec/functions/general/coalesce) [[` IFNULL `]](/language-spec/functions/general/ifnull)

* * * 

## 구문 {#syntax}

```sql
SELECT NULLIF(expression1, expression2) FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*expression*__  
`NULL` 또는 `NULL`이 아닌 값으로 평가되는 임의의 데이터 형식의 식 2개입니다.

> `NULLIF` 함수는 인수를 왼쪽에서 오른쪽으로 검색하여 데이터 정렬 함수를 정의하는 인수를 찾고, 모든 문자열 비교에 해당 데이터 정렬 함수를 사용합니다. 두 인수 모두 데이터 정렬 함수를 정의하지 않으면 `BINARY` 데이터 정렬이 사용됩니다.

## 반환 값 {#return-values}

두 *expression* 인수가 같은 값으로 평가되면 `NULL`을 반환하고, 그렇지 않으면 첫 번째 값을 반환합니다. 

* * *

## 예제 {#examples}

### 필드가 지정한 값과 같지 않은 레코드 수 계산 {#count-records-where-a-field-is-not-equal-to-a-given-value}

```sql
SELECT COUNT(NULLIF(name, 'controller-0'))
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
/*
equivalent to: 
SELECT COUNT(*) FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a' 
AND name != 'controller-0';
*/
```

자세한 내용은 [https://www.sqlite.org/lang_corefunc.html#nullif](https://www.sqlite.org/lang_corefunc.html#nullif) 문서를 참조하세요.
