---
title: JSON_EACH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 JSON_EACH 테이블 반환 함수: JSON 배열 또는 객체를 요소나 멤버당 1행으로 펼쳐 중첩된 API 응답을 조인하고 필터링합니다."
image: "/img/stackql-featured-image.png"
---
각 배열 요소 또는 객체 멤버당 1행으로 구성된 테이블을 반환하는 테이블 반환 함수입니다.

참고 항목:  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[` JSON_ARRAY_LENGTH `]](/language-spec/functions/json/json_array_length) [[ 데이터 형식 ]](/language-spec/data-types)

* * * 

:::tip 

[**DESCRIBE**](/language-spec/describe) 문을 사용하면 `json_each` 함수와 함께 사용할 수 있는 `array` 데이터 형식을 찾을 수 있습니다.

:::

:::note 

`postgres` SQL 백엔드와 함께 stackql을 사용하는 경우 `json_each` 함수는 `json_array_elements_text` 함수로 구현됩니다.

:::

## 구문 {#syntax}

```sql
SELECT <alias>.<projectedField>, ... , json_each.value  
FROM <multipartIdentifier> <alias>, JSON_EACH(json_array_or_object, json_path);
```
## 인수 {#arguments}

__*json_array_or_object*__  
JSON 배열 또는 객체입니다.

__*json_path*__  
*json_array_or_object* 안에 포함된 배열 또는 객체에 대한 JSON 경로입니다. [jsonpath.com](https://jsonpath.com/)을 참조하세요.

> *json_path*가 *json_array_or_object*에 없으면 함수는 행을 반환하지 않습니다.

> *json_array_or_object*가 올바른 형식의 JSON이 아니면 `json_each()` 함수는 오류를 발생시킵니다.

## 반환 값 {#return-values}
각 배열 요소 또는 객체 멤버당 1행으로 구성된 테이블입니다.

* * *

## 예제 {#examples}

### 문자열 배열을 관련 필드와 조인된 테이블로 중첩 해제 {#unnest-an-array-of-strings-into-a-table-joined-with-associated-fields}

참고로 다음 문을 실행하면:  

```sql
describe google.cloudresourcemanager.projects_iam_policies;
```

다음이 반환됩니다...

```
|-----------|--------|                                                                                                                                                                                                     
|   name    |  type  |                                                                                                                                                                                                     
|-----------|--------|                                                                                                                                                                                                     
| condition | object |                                                                                                                                                                                                     
|-----------|--------|                                                                                                                                                                                                     
| members   | array  |                                                                                                                                                                                                     
|-----------|--------|                                                                                                                                                                                                     
| role      | string |                                                                                                                                                                                                     
|-----------|--------|  
```

다음 `stackql` 쿼리는 `json_each` 함수를 사용하여 `members` 배열을 `role` 필드 및 선택적으로 `condition` 필드와 조인된 테이블로 중첩 해제합니다.  

```sql
select 
iam.role, 
SPLIT_PART(json_each.value, ':', 2) as member 
from google.cloudresourcemanager.projects_iam_policies iam, json_each(members) 
where projectsId = 'stackql';
```

이 쿼리는 다음을 반환합니다...

```
|--------------------------------------|-------------------------------------------------------|
|                 role                 |                        member                         |
|--------------------------------------|-------------------------------------------------------|
| roles/cloudbuild.builds.builder      | 1234567890@cloudbuild.gserviceaccount.com             |
|--------------------------------------|-------------------------------------------------------|
| roles/editor                         | 1234567890-compute@developer.gserviceaccount.com      |
|--------------------------------------|-------------------------------------------------------|
| roles/editor                         | 1234567890@cloudservices.gserviceaccount.com          |
|--------------------------------------|-------------------------------------------------------|
| roles/editor                         | stackql@appspot.gserviceaccount.com                   |
|--------------------------------------|-------------------------------------------------------|
| ...                                  | ...                                                   |
|--------------------------------------|-------------------------------------------------------|
```

자세한 내용은 [https://www.sqlite.org/json1.html#jeach](https://www.sqlite.org/json1.html#jeach) 문서를 참조하세요.
