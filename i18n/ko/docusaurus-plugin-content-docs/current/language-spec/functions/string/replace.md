---
title: REPLACE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 REPLACE 함수: 문자열 내에서 부분 문자열이 나타나는 모든 위치를 다른 문자열로 바꿉니다."
image: "/img/stackql-featured-image.png"
---
부분 문자열이 나타나는 모든 위치를 다른 문자열로 대체한 문자열의 복사본을 반환합니다.

참고 항목:  
[[` SELECT `]](/language-spec/select) 

* * * 

## 구문 {#syntax}

```sql
SELECT REPLACE(character_expression, pattern, replacement) FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*character_expression*__  
대체를 수행할 리터럴 문자열 또는 문자열 열입니다.

__*pattern*__  
원래 문자열에서 찾을 부분 문자열입니다.

> *pattern*이 빈 문자열이거나 *character_expression*에서 찾을 수 없으면 *character_expression*이 변경되지 않은 채 반환됩니다. 

__*replacement*__  
대체 문자열입니다.

> *replacement*가 처음부터 문자열이 아니면 처리 전에 UTF-8 문자열로 변환됩니다.

> 비교에는 `BINARY` 데이터 정렬 순서가 사용됩니다.

## 반환 값 {#return-values}
문자열을 반환합니다.

* * *

## 예제 {#examples}

### 열에서 문자열 패턴 대체 {#replace-a-string-pattern-in-a-column}

```sql
SELECT replace(name, 'prd', 'production') AS replacement_string
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

자세한 내용은 [https://www.sqlite.org/lang_corefunc.html#replace](https://www.sqlite.org/lang_corefunc.html#replace) 문서를 참조하세요.
