---
title: LTRIM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 LTRIM 함수: 문자열의 왼쪽에서 공백 문자 또는 지정한 문자 집합을 제거합니다."
image: "/img/stackql-featured-image.png"
---
입력 문자열의 왼쪽에 나타나는 공백 문자 또는 임의의 문자를 제거하여 만든 문자열을 반환합니다.  

참고 항목:  
[[` SELECT `]](/language-spec/select) [[` RTRIM `]](/language-spec/functions/string/rtrim) [[` TRIM `]](/language-spec/functions/string/trim) 

* * * 

## 구문 {#syntax}

```sql
SELECT LTRIM(character_expression[, character ]) FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*character_expression*__  
리터럴 문자열 또는 문자열 열입니다.

__*character*__  
*character_expression*의 시작 부분에 있으면 제거할 선택적 문자입니다.

## 반환 값 {#return-values}
문자열을 반환합니다.

* * *

## 예제 {#examples}

### 문자열 열에서 선행 공백 문자 제거 {#remove-leading-whitespace-from-a-string-column}

```sql
SELECT ltrim(description) AS leading_whitespace_removed
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

자세한 내용은 [https://www.sqlite.org/lang_corefunc.html#ltrim](https://www.sqlite.org/lang_corefunc.html#ltrim) 문서를 참조하세요.
