---
title: SUBSTRING
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 SUBSTRING 함수(별칭 SUBSTR): 시작 위치와 길이를 기준으로 문자열 또는 이진 값의 일부를 반환합니다."
image: "/img/stackql-featured-image.png"
---
문자 식 또는 이진 식의 일부를 반환합니다.  `SUBSTR` 함수는 `SUBSTRING` 함수의 별칭입니다.

참고 항목:  
[[` SELECT `]](/language-spec/select) [[ 데이터 형식 ]](/language-spec/data-types)

* * * 

:::info

JSON 객체에서 필드를 추출하려면 [**JSON_EXTRACT**](/language-spec/functions/json/json_extract) 함수를 사용하세요.

:::

## 구문 {#syntax}

```sql
SELECT SUBSTRING(expression, start[, length]) FROM <multipartIdentifier>;
SELECT SUBSTR(expression, start[, length]) FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*expression*__  
ASCII 문자열 또는 BLOB입니다.

> *expression*이 문자열이면 문자 인덱스는 실제 UTF-8 문자를 가리킵니다. *expression*이 BLOB이면 인덱스는 바이트를 가리킵니다.

__*start*__  
반환할 입력 식의 하위 시퀀스에서 첫 번째 문자 또는 바이트를 나타내는 정수입니다.  *start*가 음수이면 부분 문자열의 첫 번째 문자를 왼쪽이 아닌 오른쪽부터 세어 찾습니다.

> *expression*의 맨 왼쪽 문자는 1번입니다.

__*length*__  
*start* 값 이후에 반환할 하위 시퀀스의 길이를 나타내는 선택적 정수 값입니다.  *length*를 생략하면 *expression*으로 지정한 문자열 또는 BLOB의 끝까지 모든 문자가 반환됩니다.

> *length*가 음수이면 *length*의 절댓값이 사용됩니다.

## 반환 값 {#return-values}

*expression* 형식에 따라 문자열 또는 BLOB 값을 반환합니다.

* * *

## 예제 {#examples}

### 열에서 처음 *n*개 문자 반환 {#return-the-first-n-characters-from-a-column}

```sql
SELECT SUBSTRING(name, 1, 5) as "first five chars" 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### 열에서 처음 *n*개 문자 제거 {#truncate-the-first-n-characters-from-a-column}

```sql
SELECT SUBSTR(name, 5) as "from char 5" 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```
### 열에서 마지막 *n*개 문자 반환 {#return-the-last-n-characters-from-a-column}

```sql
SELECT SUBSTRING(name, -5) as "last five chars" 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

자세한 내용은 [https://www.sqlite.org/lang_corefunc.html#substr](https://www.sqlite.org/lang_corefunc.html#substr) 문서를 참조하세요.
