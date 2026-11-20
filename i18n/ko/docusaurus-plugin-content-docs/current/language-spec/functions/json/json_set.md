---
title: JSON_SET
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: StackQL에서 SQL로 JSON 문서의 값을 업데이트하거나 삽입합니다.
image: "/img/stackql-featured-image.png"
---
JSON 문서의 지정한 경로에서 기존 값을 업데이트하거나 새 값을 삽입합니다.

참고 항목:  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[` JSON_INSERT `]](/language-spec/functions/json/json_insert) [[` JSON_REPLACE `]](/language-spec/functions/json/json_replace)

* * * 

## 구문 {#syntax}

```sql
SELECT JSON_SET(json_column, path, value_to_set) FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*json_column*__  
수정할 JSON 열 또는 식입니다.

__*path*__  
값을 설정할 위치를 나타내는 JSON 경로 식입니다. 경로가 있으면 값이 업데이트되고, 경로가 없으면 새 키-값 쌍이 추가됩니다.

__*value_to_set*__  
JSON 문서의 지정한 경로에 설정할 값입니다.

## 반환 값 {#return-values}
지정한 경로에 값이 설정된 JSON 문서를 반환합니다. 경로가 있으면 기존 값이 업데이트되고, 그렇지 않으면 새 키-값 쌍이 추가됩니다.

:::note 키가 있는 경우의 동작

| 함수                                                         | 이미 있는 경우 덮어쓰기 | 없는 경우 생성 |
|--------------------------------------------------------------|-------------------------|----------------|
| [`json_insert()`](/language-spec/functions/json/json_insert) | 아니요                  | 예             |
| [`json_replace()`](/language-spec/functions/json/json_replace) | 예                      | 아니요         |
| [`json_set()`](/language-spec/functions/json/json_set)       | 예                      | 예             |

:::

* * *

## 예제 {#examples}

### JSON 객체의 기존 값 업데이트 {#update-an-existing-value-in-a-json-object}

```sql
-- Example: Update the 'status' key's value to 'active' in a JSON object
SELECT JSON_SET('{"name":"ServiceOne","status":"pending","region":"US"}', '$.status', 'active') as updated_json;

-- Expected output:
-- |---------------------------------------------------------|
-- |                    updated_json                         |
-- |---------------------------------------------------------|
-- | {"name":"ServiceOne","status":"active","region":"US"}   |
-- |---------------------------------------------------------|
```

이 쿼리는 'status' 키가 있으면 JSON 문서에서 'status' 키의 값을 'active'로 업데이트합니다. 'status'가 문서에 없으면 값이 'active'인 키로 추가됩니다.

### JSON 객체에 새 키-값 쌍 추가 {#add-a-new-key-value-pair-to-a-json-object}

```sql
-- Example: Add a new key 'lastChecked' with a date value to a JSON object
SELECT JSON_SET('{"name":"ServiceOne","status":"pending"}', '$.lastChecked', '2024-04-29') as updated_json;

/* expected output...
|-----------------------------------------------------------------------|
|                          updated_json                                 |
|-----------------------------------------------------------------------|
| {"name":"ServiceOne","status":"pending","lastChecked":"2024-04-29"}   |
|-----------------------------------------------------------------------|
*/
```

이 쿼리는 'lastChecked' 키가 아직 없으면 값이 '2024-04-29'인 새 키 'lastChecked'를 JSON 문서에 추가합니다. 키가 이미 있으면 기존 값이 '2024-04-29'로 업데이트됩니다.
