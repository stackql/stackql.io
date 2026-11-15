---
title: INSTR
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 INSTR 함수: 한 문자열 안에서 다른 문자열이 처음 나타나는 위치(1부터 시작)이며, 찾지 못하면 0입니다."
image: "/img/stackql-featured-image.png"
---
다른 문자열 내에서 문자열이 처음 나타나는 문자 위치를 반환하며, 검색할 문자열을 찾지 못하면 0을 반환합니다.

참고 항목:  
[[` SELECT `]](/language-spec/select)

* * * 

## 구문 {#syntax}

```sql
SELECT INSTR(string, searchstring) FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*string*__  
검색 대상인 원본 문자열입니다.

__*searchstring*__  
검색할 부분 문자열입니다.

> *string*과 *searchstring*이 모두 BLOB이면 `instr(<string>, <searchstring>)` 함수는 *searchstring*이 처음 나타나기 전의 바이트 수에 1을 더한 값을 반환하고, *searchstring*이 *string* 내에 없으면 0을 반환합니다. 두 인수가 모두 `NULL`이 아니고 BLOB도 아니면 둘 다 문자열로 해석됩니다. 

## 반환 값 {#return-values}
정수를 반환합니다.

> *string* 또는 *searchstring* 값 중 하나가 `NULL`이면 결과는 `NULL`입니다.

* * *

## 예제 {#examples}

### 필드에서 문자열 검색 {#search-a-field-for-a-string}

```sql
SELECT INSTR(name, 'worker') as charpos
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
/*
returns 0 if the term 'worker' is not present in the 'name' column, otherwise returns the character position (starting from 1) of the first occurence of the term 'worker' in the 'name' column
*/
```

자세한 내용은 [https://www.sqlite.org/lang_corefunc.html#instr](https://www.sqlite.org/lang_corefunc.html#instr) 문서를 참조하세요.
