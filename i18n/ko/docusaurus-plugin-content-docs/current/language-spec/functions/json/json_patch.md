---
title: JSON_PATCH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: StackQL에서 SQL로 JSON 문서에 JSON MergePatch를 적용하여 JSON 객체를 수정하거나 병합합니다.
image: "/img/stackql-featured-image.png"
---
JSON 문서에 JSON MergePatch(RFC-7396)를 적용하여 지정한 작업에 따라 문서를 수정합니다. 이 함수는 JSON 객체 안의 요소를 추가, 수정 또는 삭제하는 데 사용되지만 JSON 배열은 원자 단위로 처리합니다.

참고 항목:  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract)

* * * 

## 구문 {#syntax}

```sql
SELECT JSON_PATCH(target_json, patch_json) FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*target_json*__  
패치할 JSON 문서입니다.

__*patch_json*__  
MergePatch 알고리즘에 따라 적용할 변경 내용을 설명하는 JSON 문서입니다.

## 반환 값 {#return-values}
패치가 적용된 후 수정된 JSON 문서를 반환합니다.

* * *

## 예제 {#examples}

### 간단한 MergePatch {#simple-mergepatch}

```sql
-- Example: Simple merge of new elements into the target JSON object
SELECT JSON_PATCH('{"a":1,"b":2}', '{"c":3,"d":4}') AS patched_json;
```

출력: `{"a":1,"b":2,"c":3,"d":4}`

### 배열 요소 교체 {#replace-element-in-array}

```sql
-- Example: Replacing an array with a single value in the JSON object
SELECT JSON_PATCH('{"a":[1,2],"b":2}', '{"a":9}') AS patched_json;
```

출력: `{"a":9,"b":2}`

### null을 사용한 요소 삭제 {#remove-element-using-null}

```sql
-- Example: Removing an element by setting it to null
SELECT JSON_PATCH('{"a":1,"b":2}', '{"a":9,"b":null,"c":8}') AS patched_json;
```

출력: `{"a":9,"c":8}`

### 중첩된 MergePatch {#nested-mergepatch}

```sql
-- Example: Nested merge patch updating sub-keys within a nested JSON object
SELECT JSON_PATCH('{"a":{"x":1,"y":2},"b":3}', '{"a":{"y":9},"c":8}') AS patched_json;
```

출력: `{"a":{"x":1,"y":9},"b":3,"c":8}`

이 기능을 사용하면 데이터베이스에 저장된 JSON을 유연하고 동적으로 업데이트할 수 있으며, 특히 데이터 구조를 조건부로 수정해야 하는 애플리케이션에 유용합니다.
