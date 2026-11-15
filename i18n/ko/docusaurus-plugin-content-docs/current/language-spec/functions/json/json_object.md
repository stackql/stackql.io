---
title: JSON_OBJECT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 JSON_OBJECT 함수: 키와 값 인수로 JSON 객체를 만들어 INSERT 및 UPDATE의 요청 본문 등을 구성합니다."
image: "/img/stackql-featured-image.png"
---
지정한 인수로 구성된 객체를 반환합니다.  

참고 항목:  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[ 데이터 형식 ]](/language-spec/data-types)

* * * 

:::tip 

[**DESCRIBE**](/language-spec/describe) 함수를 사용하면 StackQL JSON 함수와 함께 사용할 수 있는 `array` 또는 `object` 데이터 형식을 찾을 수 있습니다.

:::

:::note 

`postgres` SQL 백엔드와 함께 stackql을 사용하는 경우 `json_object` 함수는 `jsonb_build_object` 함수로 구현됩니다.

:::

## 구문 {#syntax}

```sql
SELECT JSON_OBJECT(key1, value1, key2, value2, ...) 
FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*key1, key2, ...*__  
JSON 객체의 레이블입니다.

__*value1, value2, ...*__  
JSON 객체의 레이블에 해당하는 값입니다.

## 반환 값 {#return-values}
올바른 형식의 JSON 객체를 나타내는 문자열입니다.

* * *

## 예제 {#examples}

### JSON 객체 또는 배열 안에 중첩된 필드의 데이터 형식 반환 {#return-the-datatype-of-a-field-nested-within-a-json-object-or-array}

```sql
SELECT JSON_GROUP_ARRAY(json_object('instance', selfLink)) as instance_links
FROM google.compute.instances 
WHERE project = 'stackql-k8s-the-hard-way-demo'
AND zone = 'australia-southeast1-a'
AND name like '%-dev-controller-%';
/* returns a data structure required for a targetPool in GCP */
```

자세한 내용은 [https://sqlite.org/json1.html#jobj](https://sqlite.org/json1.html#jobj) 문서를 참조하세요.
