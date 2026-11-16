---
title: JSON_TYPE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 JSON_TYPE 함수: JSON 값의 가장 바깥쪽 요소 또는 그 안의 경로가 가리키는 값의 형식(object, array, text, integer, real, true, false 또는 null)입니다."
image: "/img/stackql-featured-image.png"
---
JSON 값의 가장 바깥쪽 요소 또는 선택적 경로에 있는 요소의 데이터 형식을 반환합니다.  

참고 항목:  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[ 데이터 형식 ]](/language-spec/data-types)

* * * 

:::tip 

[**DESCRIBE**](/language-spec/describe) 문을 사용하면 StackQL JSON 함수와 함께 사용할 수 있는 `array` 또는 `object` 데이터 형식을 찾을 수 있습니다.

:::

:::note 

`postgres` SQL 백엔드와 함께 StackQL을 사용하는 경우 `json_typeof` 함수를 대신 사용하세요.

:::

## 구문 {#syntax}

```sql
SELECT JSON_TYPE(json_array_or_object, json_path) 
FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*json_array_or_object*__  
JSON 배열 또는 객체입니다.

__*json_path*__  
*json_array_or_object* 안에 포함된 배열 또는 객체에 대한 JSON 경로입니다. [jsonpath.com](https://jsonpath.com/)을 참조하세요.

> *json_path*가 *json_array_or_object*에 없으면 함수는 `NULL`을 반환합니다.

> 인수 중 하나라도 올바른 형식이 아니면 `json_type()` 함수는 오류를 발생시킵니다.

## 반환 값 {#return-values}
확인된 데이터 형식을 나타내는 문자열입니다.

> 데이터 형식에는 `null`, `true`, `false`, `integer`, `real`, `text`, `array` 또는 `object`가 있습니다.

* * *

## 예제 {#examples}

### JSON 객체 또는 배열 안에 중첩된 필드의 데이터 형식 반환 {#return-the-datatype-of-a-field-nested-within-a-json-object-or-array}

```sql
SELECT json_type(disks, '$[0].licenses[0]') as datatype
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
/* returns text */
```

자세한 내용은 [https://www.sqlite.org/json1.html#jtype](https://www.sqlite.org/json1.html#jtype) 문서를 참조하세요.
