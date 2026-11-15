---
title: RTRIM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 RTRIM 함수: 문자열의 오른쪽에서 공백 또는 지정한 문자 집합에 속하는 문자를 제거합니다."
image: "/img/stackql-featured-image.png"
---
입력 문자열의 오른쪽에서 공백 또는 지정한 문자 집합에 속하는 문자를 제거하여 만든 문자열을 반환합니다.  

참고 항목:  
[[` SELECT `]](/language-spec/select) [[` LTRIM `]](/language-spec/functions/string/ltrim) [[` TRIM `]](/language-spec/functions/string/trim) 

* * * 

## 구문 {#syntax}

```sql
SELECT RTRIM(character_expression[, character ]) FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*character_expression*__  
리터럴 문자열 또는 문자열 열입니다.

__*character*__  
제거할 문자로 이루어진 선택적 문자열입니다. 이 문자열에 포함된 모든 문자가 *character_expression*의 끝 부분에서 제거됩니다. 생략하면 공백이 제거되며 탭이나 줄 바꿈은 제거되지 않습니다.

## 반환 값 {#return-values}
문자열을 반환합니다.

* * *

## 예제 {#examples}

### 문자열 열에서 후행 공백 제거 {#remove-trailing-spaces-from-a-string-column}

```sql
SELECT rtrim(description) AS trailing_whitespace_removed
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

자세한 내용은 [https://www.sqlite.org/lang_corefunc.html#rtrim](https://www.sqlite.org/lang_corefunc.html#rtrim) 문서를 참조하세요.
