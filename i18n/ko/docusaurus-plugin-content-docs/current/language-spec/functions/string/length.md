---
title: LENGTH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 LENGTH 함수: 문자열의 문자 수 또는 BLOB의 바이트 수."
image: "/img/stackql-featured-image.png"
---
문자열 입력 식에서 첫 번째 NUL 문자(`char(0)`) 앞까지의 문자 수 또는 BLOB 식의 바이트 수를 반환합니다.  

참고 항목:  
[[` SELECT `]](/language-spec/select) 

* * * 

:::info

JSON 배열의 길이를 구하려면 [**JSON_ARRAY_LENGTH**](/language-spec/functions/json/json_array_length) 함수를 사용하세요.

:::

## 구문 {#syntax}

```sql
SELECT LENGTH(expression) FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*expression*__  
문자열 또는 BLOB입니다.

> *expression*이 `NULL`이면 `NULL`을 반환합니다.

> *expression*이 숫자이면 *expression*의 문자열 표현 길이가 반환됩니다.

## 반환 값 {#return-values}
정수를 반환합니다.

* * *

## 예제 {#examples}

### 문자열 열의 길이 반환 {#return-the-length-of-a-string-column}

```sql
SELECT name, length(name) AS len
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

자세한 내용은 [https://www.sqlite.org/lang_corefunc.html#length](https://www.sqlite.org/lang_corefunc.html#length) 문서를 참조하세요.
