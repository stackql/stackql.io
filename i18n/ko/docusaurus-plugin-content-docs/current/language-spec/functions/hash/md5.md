---
title: MD5
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - md5
  - 해시
  - 다이제스트
description: "StackQL의 MD5 함수: 문자열의 MD5 다이제스트를 소문자 16진수로 반환하며, 보안이 아닌 지문 생성과 변경 감지에 사용합니다."
image: "/img/stackql-featured-image.png"
---
문자열의 MD5 다이제스트를 소문자 16진수 32자로 반환합니다. StackQL `v1.0` 이상에서 사용할 수 있습니다.

참고 항목:  
[[` SELECT `]](/language-spec/select) [[ 연결 연산자 ]](/language-spec/functions/string/concatenation)

* * * 

## 구문 {#syntax}

```sql
SELECT MD5(expression) FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*expression*__  
문자열 또는 텍스트로 강제 변환되는 값입니다. `NULL` 인수는 `NULL`을 반환합니다.

## 반환 값 {#return-values}

32자의 소문자 16진수 문자열을 반환합니다.

> `MD5` 함수는 지문 생성과 변경 감지를 위해 제공됩니다. 비밀번호용 또는 보안용 기본 요소가 아닙니다. PostgreSQL 백엔드에서는 데이터베이스 자체의 `md5` 함수가 사용됩니다.

* * *

## 예제 {#examples}

### 파생 값의 지문 생성 {#fingerprint-a-derived-value}

변수는 형식을 가진 값으로 치환되고, 정수는 텍스트로 변환되어 접미사와 결합되며, 소문자로 변환된 결과에 대해 다이제스트가 계산됩니다. 같은 입력은 항상 같은 32자 16진수 문자열을 생성하므로 이 값을 안정적인 키로 사용할 수 있습니다.

```sql
SET some_id = 42;
SET suffix = 'East';

SELECT md5(lower(CONVERT($some_id, TEXT) || '-' || $suffix)) AS digest;
```

### 구성 변경 감지 {#detect-configuration-changes}

직렬화된 JSON 열을 해시하면 실행 간에 저장하고 비교할 짧은 값을 얻을 수 있습니다. 다이제스트가 마지막 실행과 다른 버킷은 IAM 구성이 변경된 것이며, 전체 문서를 저장하거나 비교할 필요가 없습니다.

```sql
SELECT name, md5(json_extract(iamConfiguration, '$')) AS iam_digest
FROM google.storage.buckets
WHERE project = 'stackql-demo';
```
