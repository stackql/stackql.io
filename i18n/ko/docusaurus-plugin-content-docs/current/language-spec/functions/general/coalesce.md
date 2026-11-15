---
title: COALESCE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 COALESCE 함수: 2개 이상의 인수 중 NULL이 아닌 첫 번째 인수를 반환하며, 주로 누락된 값을 기본값으로 대체하는 데 사용합니다."
image: "/img/stackql-featured-image.png"
---
2개 이상의 인수 중 `NULL`이 아닌 첫 번째 인수를 반환합니다. `NULL` 값을 기본값으로 대체하는 데 사용할 수 있습니다.  

> `COALESCE` 함수는 Oracle의 `NVL` 함수 또는 MySQL의 `IFNULL` 함수와 같습니다.

참고 항목:  
[[` SELECT `]](/language-spec/select) [[` IFNULL `]](/language-spec/functions/general/ifnull) [[` NULLIF `]](/language-spec/functions/general/nullif)

* * * 

## 구문 {#syntax}

```sql
SELECT COALESCE(expression1, expression2 [, ..]) FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*expression*__  
`NULL` 또는 `NULL`이 아닌 값으로 평가되는 임의의 데이터 형식의 식 2개 이상입니다.

> 모든 *expression* 인수가 `NULL`이면 `COALESCE` 함수는 `NULL`을 반환합니다.

## 반환 값 {#return-values}

*expression* 인수 중 `NULL`이 아닌 첫 번째 값을 반환합니다.

* * *

## 예제 {#examples}

### `NULL` 값을 기본값으로 대체 {#replace-a-null-value-with-a-default-value}

```sql
SELECT name, coalesce(website, cors, 'not hosting shared resources') AS website_or_cors_config
FROM google.storage.buckets where project = 'stackql';
```

자세한 내용은 [https://www.sqlite.org/lang_corefunc.html#coalesce](https://www.sqlite.org/lang_corefunc.html#coalesce) 문서를 참조하세요.
