---
title: JSON_REMOVE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: StackQL에서 SQL로 경로를 지정하여 JSON 문서에서 요소를 삭제합니다.
image: "/img/stackql-featured-image.png"
---
경로 식을 사용하여 JSON 문서에서 지정한 요소를 삭제합니다. 지정한 경로가 문서에 없으면 무시됩니다. 경로는 순차적으로 처리되므로 앞의 삭제가 이후 삭제 대상의 구조에 영향을 줄 수 있습니다.

참고 항목:  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract)

* * * 

## 구문 {#syntax}

```sql
SELECT JSON_REMOVE(json_document, path1, path2, ...) FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*json_document*__  
요소를 삭제할 JSON 문서입니다.

__*path1, path2, ...*__  
JSON 문서에서 삭제할 요소를 가리키는 하나 이상의 경로 식입니다.

## 반환 값 {#return-values}
지정한 요소가 삭제된 JSON 문서의 복사본을 반환합니다. 경로를 지정하지 않으면 불필요한 공백 문자를 제거하고 형식을 다시 지정한 입력 JSON을 반환합니다.

* * *

## 예제 {#examples}

### JSON 배열에서 요소 삭제 {#remove-an-element-from-a-json-array}

```sql
-- Example: Remove the third element from a JSON array
SELECT JSON_REMOVE('[0,1,2,3,4]', '$[2]') AS modified_json;
```

출력: `'[0,1,3,4]'`

### JSON 배열에서 여러 요소 삭제 {#remove-multiple-elements-from-a-json-array}

```sql
-- Example: Remove the first and third elements from a JSON array
SELECT JSON_REMOVE('[0,1,2,3,4]', '$[2]', '$[0]') AS modified_json;
```

출력: `'[1,3,4]'`

### 음수 인덱스를 사용한 마지막 요소와 첫 번째 요소 삭제 {#remove-last-element-using-negative-index}

```sql
-- Example: Remove the last and first elements from a JSON array
SELECT JSON_REMOVE('[0,1,2,3,4]', '$[#-1]', '$[0]') AS modified_json;
```

출력: `'[1,2,3]'`

### 존재하지 않는 경로 삭제 {#remove-non-existent-path}

```sql
-- Example: Attempt to remove a non-existent key from a JSON object
SELECT JSON_REMOVE('{"x":25,"y":42}', '$.z') AS modified_json;
```

출력: `'{"x":25,"y":42}'`

### JSON 객체에서 기존 키 삭제 {#remove-existing-key-from-json-object}

```sql
-- Example: Remove the 'y' key from a JSON object
SELECT JSON_REMOVE('{"x":25,"y":42}', '$.y') AS modified_json;
```

출력: `'{"x":25}'`

### 에지 케이스: 루트 경로를 사용한 삭제 {#edge-case-removal-with-root-path}

```sql
-- Example: Attempt to remove using the root path
SELECT JSON_REMOVE('{"x":25,"y":42}', '$') AS modified_json;
```

출력: `NULL`

이 예제는 `json_remove()` 함수를 사용하여 JSON 데이터를 동적으로 조작하는 방법을 보여 주며, 간단한 삭제부터 데이터 구조 수정 시의 에지 케이스 처리까지 다양한 시나리오를 다룹니다.
