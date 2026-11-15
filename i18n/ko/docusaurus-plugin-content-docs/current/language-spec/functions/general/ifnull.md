---
title: IFNULL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 IFNULL 함수: 정확히 2개의 인수 중 NULL이 아닌 첫 번째 인수를 반환하며, 인수가 2개인 형식의 COALESCE입니다."
image: "/img/stackql-featured-image.png"
---
정확히 2개의 인수를 받아 `NULL`이 아닌 첫 번째 인수를 반환합니다. `NULL` 값을 기본값으로 대체하는 데 사용할 수 있습니다.  

> `IFNULL` 함수는 인수가 2개뿐인 [`COALESCE`](/language-spec/functions/general/coalesce) 함수와 같습니다. [`COALESCE`](/language-spec/functions/general/coalesce) 함수는 인수를 2개보다 많이 가질 수 있습니다.

참고 항목:  
[[` SELECT `]](/language-spec/select) [[` COALESCE `]](/language-spec/functions/general/coalesce) [[` NULLIF `]](/language-spec/functions/general/nullif)

* * * 

## 구문 {#syntax}

```sql
SELECT IFNULL(expression1, expression2) FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*expression*__  
`NULL` 또는 `NULL`이 아닌 값으로 평가되는 임의의 데이터 형식의 식 2개입니다.

> 두 *expression* 인수가 모두 `NULL`이면 `IFNULL` 함수는 `NULL`을 반환합니다.


## 반환 값 {#return-values}

두 *expression* 인수 중 `NULL`이 아닌 첫 번째 값을 반환합니다. 

* * *

## 예제 {#examples}

### `NULL` 값을 기본값으로 대체 {#replace-a-null-value-with-a-default-value}

```sql
SELECT name, ifnull(website, 'Not hosting a website') AS website 
FROM google.storage.buckets where project = 'stackql';
```

자세한 내용은 [https://www.sqlite.org/lang_corefunc.html#ifnull](https://www.sqlite.org/lang_corefunc.html#ifnull) 문서를 참조하세요.
