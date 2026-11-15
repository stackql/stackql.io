---
title: TRUNC
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 TRUNC 함수: 0 방향으로 반올림하고 소수 부분을 버린 숫자의 정수 부분."
image: "/img/stackql-featured-image.png"
---

0 방향으로 반올림하여 소수 부분을 모두 제거한 숫자의 정수 부분을 반환합니다.

참고 항목:  
[[`SELECT`]](/language-spec/select) 

* * * 

## 구문 {#syntax}

```sql
SELECT TRUNC(numeric_expression) FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*numeric_expression*__  
정확한 수치 또는 근사 수치 데이터 형식 범주의 식입니다.

## 반환 값 {#return-values}
정수 부분으로 자른 숫자 값을 반환합니다.

* * *

## 예제 {#examples}

### Google Cloud Storage 버킷 생성 이후 경과 일수를 정수 일 단위로 절사 {#truncate-the-number-of-days-since-creation-of-google-cloud-storage-buckets-to-whole-days}

```sql
SELECT name, timeCreated,
TRUNC(julianday('now') - julianday(timeCreated)) as days_since
FROM google.storage.buckets WHERE project = 'stackql';
```
