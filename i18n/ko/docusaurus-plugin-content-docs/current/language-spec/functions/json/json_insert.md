---
title: JSON_INSERT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: StackQL에서 SQL로 기존 데이터를 덮어쓰지 않고 JSON 문서에 새 값을 삽입합니다.
image: "/img/stackql-featured-image.png"
---
기존 값을 덮어쓰지 않고 JSON 문서의 지정한 경로에 새 값을 삽입합니다.

참고 항목:  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[` JSON_SET `]](/language-spec/functions/json/json_set) [[` JSON_REPLACE `]](/language-spec/functions/json/json_replace)

* * * 

## 구문 {#syntax}

```sql
SELECT JSON_INSERT(json_column, path, new_value) FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*json_column*__  
수정할 JSON 열 또는 식입니다.

__*path*__  
새 값을 삽입할 위치를 나타내는 JSON 경로 식입니다. 경로가 이미 있으면 해당 경로의 원래 값은 교체되지 않습니다.

__*new_value*__  
JSON 문서의 지정한 경로에 삽입할 값입니다.

## 반환 값 {#return-values}
지정한 경로가 아직 없으면 새 값이 삽입된 JSON 문서를 반환하고, 그렇지 않으면 JSON 문서를 변경 없이 반환합니다.

:::note 키가 있는 경우의 동작

| 함수                                                         | 이미 있는 경우 덮어쓰기 | 없는 경우 생성 |
|--------------------------------------------------------------|-------------------------|----------------|
| [`json_insert()`](/language-spec/functions/json/json_insert) | 아니요                  | 예             |
| [`json_replace()`](/language-spec/functions/json/json_replace) | 예                      | 아니요         |
| [`json_set()`](/language-spec/functions/json/json_set)       | 예                      | 예             |

:::

* * *

## 예제 {#examples}

### JSON 객체에 새 키-값 쌍 삽입 {#insert-a-new-key-value-pair-into-a-json-object}

```sql
-- Example: Insert a new 'region' key with the value 'Europe' into a JSON object
SELECT JSON_INSERT(json_data, '$.region', 'Europe') as updated_object;

/* expected output...
|-----------------------------------------------------------|
|                      updated_object                       |
|-----------------------------------------------------------|
| {"name":"ResourceOne","type":"Storage","region":"Europe"} |
|-----------------------------------------------------------|
*/
```

이 쿼리는 'region' 키가 아직 없는 경우 값이 'Europe'인 'region'이라는 새 키를 JSON 문서에 삽입합니다. 'region'이 이미 문서의 키이면 원래 문서가 변경 없이 반환됩니다.
