---
title: JSON_ARRAY_LENGTH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 JSON_ARRAY_LENGTH 함수: JSON 값의 최상위 수준 또는 지정한 경로에 있는 JSON 배열의 요소 수를 반환합니다."
image: "/img/stackql-featured-image.png"
---
JSON 배열의 요소 수를 반환합니다.  

참고 항목:  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[ 데이터 형식 ]](/language-spec/data-types)

* * * 

:::tip 

[**DESCRIBE**](/language-spec/describe) 함수를 사용하여 StackQL JSON 함수와 함께 사용할 수 있는 `array` 또는 `object` 데이터 형식을 찾으세요. 중첩된 객체 또는 배열 요소의 데이터 형식을 확인하려면 [**JSON_TYPE**](/language-spec/functions/json/json_type) 함수를 사용하세요.

:::

## 구문 {#syntax}

```sql
SELECT JSON_ARRAY_LENGTH(json_array_or_object [, json_path]) FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*json_array_or_object*__  
JSON 배열 또는 객체입니다.

> *json_array_or_object*가 JSON 객체(배열이 아님)이고 *json_path*를 지정하지 않으면 `0`이 반환됩니다.

__*json_path*__  
객체 안에 포함된 배열에 대한 선택적 JSON 경로입니다. [jsonpath.com](https://jsonpath.com/)을 참조하세요.

> *json_path*를 지정하면 `json_array_length` 함수는 *json_array_or_object* 안에서 해당 경로를 찾아 배열의 길이를 반환하며, *json_path*가 객체(배열이 아님)이면 `0`을 반환합니다. *json_array_or_object* 안에서 *json_path*를 찾을 수 없으면 `NULL`이 반환됩니다.

> *json_array_or_object* 또는 *json_path*가 JSON 객체가 아니거나 올바른 형식이 아니면 오류가 발생합니다.

## 반환 값 {#return-values}
정수를 반환합니다.

* * *

## 예제 {#examples}

### Google Compute Engine 인스턴스에 연결된 디스크 수 반환 {#return-the-number-of-disks-attached-to-google-compute-engine-instances}

```sql
SELECT name, json_array_length(disks) as num_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Google Compute Engine 인스턴스에 연결된 네트워크 인터페이스 수 반환 {#return-the-number-of-network-interfaces-attached-to-google-compute-engine-instances}

```sql
SELECT name, json_array_length(networkInterfaces) as num_nics
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Google Storage 버킷과 연결된 ACL 수 반환 {#return-the-number-of-acls-associated-with-google-storage-buckets}

```sql
SELECT name, json_array_length(acl) as num_acls
FROM google.storage.buckets 
WHERE project = 'stackql';
```

### 서비스 계정과 연결된 범위 수 반환 {#return-the-number-of-scopes-associated-with-a-service-account}

```sql
SELECT name, json_array_length(serviceAccounts, '$[0].scopes') as num_scopes 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

자세한 내용은 [https://www.sqlite.org/json1.html#jarraylen](https://www.sqlite.org/json1.html#jarraylen) 문서를 참조하세요.
