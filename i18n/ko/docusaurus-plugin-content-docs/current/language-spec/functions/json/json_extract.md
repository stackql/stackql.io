---
title: JSON_EXTRACT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 JSON_EXTRACT 함수: 경로로 JSON 객체 또는 배열에서 하나 이상의 값을 추출하며, 중첩된 API 응답 필드를 평면화하는 주요 도구입니다."
image: "/img/stackql-featured-image.png"
---
올바른 형식의 JSON 객체 또는 배열에서 하나 이상의 값을 반환합니다.  

참고 항목:  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_ARRAY_LENGTH `]](/language-spec/functions/json/json_array_length) [[ 데이터 형식 ]](/language-spec/data-types)

* * * 

:::tip 

[**DESCRIBE**](/language-spec/describe) 문을 사용하면 StackQL JSON 함수와 함께 사용할 수 있는 `array` 또는 `object` 데이터 형식을 찾을 수 있습니다. [**JSON_TYPE**](/language-spec/functions/json/json_type) 함수를 사용하면 중첩된 객체 또는 배열 요소의 데이터 형식을 확인할 수 있습니다.

:::

:::note 

`postgres` SQL 백엔드와 함께 stackql을 사용하는 경우 `json_extract` 함수는 `json_extract_path_text` 함수로 구현됩니다.

:::


## 구문 {#syntax}

```sql
SELECT JSON_EXTRACT(json_array_or_object, json_path[, json_path1, .. json_pathN]) 
FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*json_array_or_object*__  
JSON 배열 또는 객체입니다.

__*json_path*__  
*json_array_or_object* 안에 포함된 배열 또는 객체에 대한 JSON 경로입니다. [jsonpath.com](https://jsonpath.com/)을 참조하세요.

> 단일 *json_path*를 지정하면 확인된 경로의 데이터 형식에 매핑되는 스칼라 결과가 반환됩니다.

> 여러 *json_path* 인수를 지정하면 결과는 각 경로로 확인된 객체로 구성된 배열이 됩니다.

> *json_path* 인수에 `'$'` 값을 지정하면 *json_array_or_object* 인수가 나타내는 열에 해당하는 전체 JSON 객체가 반환됩니다.

> 참고: [`json-viewer`](https://aarrange.com/json-viewer)와 같은 도구를 사용하여 JSON Path 식을 개발하고 테스트할 수 있습니다.

## 반환 값 {#return-values}
입력으로 지정한 *json_array_or_object* 및 *json_path*에 따라 문자열, 정수, 객체, 배열, 부울 또는 null입니다.

* * *

## 예제 {#examples}

### 각 Google Compute Engine 인스턴스와 연결된 디스크 객체 반환 {#return-the-disk-objects-associated-with-each-google-compute-engine-instance}

```sql
SELECT name, json_extract(disks, '$') as disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### 각 Google Compute Engine 인스턴스에 연결된 첫 번째 디스크의 디바이스 이름 및 크기 반환 {#return-the-device-name-and-size-of-the-first-disk-attached-to-each-google-compute-engine-instance}

```sql
SELECT name, json_extract(disks, '$[0].deviceName') as device_name,
json_extract(disks, '$[0].diskSizeGb') as disk_size
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### 각 Google Compute Engine 인스턴스에 연결된 첫 번째 NIC의 네트워크 계층 반환 {#return-the-network-tier-of-the-first-nic-attached-to-each-google-compute-engine-instance}

```sql
SELECT name, json_extract(networkInterfaces, '$[0].accessConfigs[0].networkTier') as network_tier
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### 각 Google Compute Engine 인스턴스와 연결된 첫 번째 서비스 계정의 범위 목록 반환 {#return-a-list-of-scopes-associated-with-the-first-service-account-associated-with-each-google-compute-engine-instance}

```sql
SELECT name, json_extract(serviceAccounts, '$[0].scopes') as scopes 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
/* returns a list for each service account */
```

자세한 내용은 [https://www.sqlite.org/json1.html#jex](https://www.sqlite.org/json1.html#jex) 문서를 참조하세요.
