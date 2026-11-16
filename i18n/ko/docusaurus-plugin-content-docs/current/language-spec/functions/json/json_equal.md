---
title: JSON_EQUAL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: SQL로 두 JSON 문자열이 동등한지 비교합니다.
image: "/img/stackql-featured-image.png"
---
두 JSON 문자열을 비교하여 동등하면 1을, 그렇지 않으면 0을 반환합니다.

참고 항목:  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[` JSON_ARRAY_LENGTH `]](/language-spec/functions/json/json_array_length) [[ 데이터 형식 ]](/language-spec/data-types)

* * * 

:::tip 

`json_equal` 함수는 StackQL의 사용자 지정 확장 함수입니다. JSON 객체와 배열의 비교를 지원하며, JSON 사양에 따라 키 순서가 다른 객체를 동등한 것으로 처리합니다. `json_equal` 함수는 StackQL `0.5.707` 이상에서 사용할 수 있으며, StackQL의 기본 임베디드 인메모리 SQL 백엔드에서만 지원됩니다.

:::

## 구문 {#syntax}

```sql
SELECT JSON_EQUAL(json1, json2);
```

## 인수 {#arguments}

__*json1*__  
객체 또는 배열을 나타내는 JSON 문자열입니다.

__*json2*__  
첫 번째 인수와 비교할 또 다른 JSON 문자열입니다.

> 두 인수 모두 유효한 JSON 문자열이어야 합니다.

## 반환 값 {#return-values}
정수 값입니다. JSON 문자열이 동등하면 1이고, 그렇지 않으면 0입니다.

* * *

## 예제 {#examples}

### JSON 객체 비교 {#compare-json-objects}

```sql
SELECT 
public_access_block_configuration as current_state,
'{"BlockPublicAcls": true, "BlockPublicPolicy": true, "IgnorePublicAcls": true, "RestrictPublicBuckets": true}' as desired_state,
JSON_EQUAL(public_access_block_configuration, '{"BlockPublicAcls": true, "BlockPublicPolicy": true, "IgnorePublicAcls": true, "RestrictPublicBuckets": true}') as comp
FROM awscc.s3.buckets 
WHERE region = 'ap-southeast-2' 
AND Identifier = 'my-bucket';
/* returns...
|--------------------------------------------------------------------------------------------------------|--------------------------------|------|
|                                             current_state                                              |         desired_state          | comp |
|--------------------------------------------------------------------------------------------------------|--------------------------------|------|
| {"RestrictPublicBuckets":true,"BlockPublicPolicy":true,"BlockPublicAcls":true,"IgnorePublicAcls":true} | {"BlockPublicAcls": true,      |    1 |
|                                                                                                        | "BlockPublicPolicy": true,     |      |
|                                                                                                        | "IgnorePublicAcls": true,      |      |
|                                                                                                        | "RestrictPublicBuckets": true} |      |
|--------------------------------------------------------------------------------------------------------|--------------------------------|------|
*/
```

### JSON 배열 비교 {#compare-json-arrays}

```sql
SELECT 
  json_equal('[1, 2, 3]', '[1, 2, 3]') as array_comp_1,
  json_equal('[1, 2, 3]', '[3, 2, 1]') as array_comp_2
; 
/* returns...
|--------------|--------------|
| array_comp_1 | array_comp_2 |
|--------------|--------------|
|            1 |            0 |
|--------------|--------------|
*/
```

자세한 내용은 [json_equal](https://github.com/stackql/sqlite-ext-functions/blob/main/docs/json_equal.md) 문서를 참조하세요.
