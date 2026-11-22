---
title: JSON_GROUP_OBJECT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: StackQL에서 SQL로 키-값 쌍을 JSON 객체로 집계합니다.
image: "/img/stackql-featured-image.png"
---
지정한 열을 기준으로 그룹화하여 키-값 쌍을 JSON 객체로 집계합니다. `JSON_GROUP_OBJECT()` 함수는 지정한 열에서 각 그룹의 모든 키-값 쌍을 수집하여 그룹마다 하나의 JSON 객체로 결합합니다.

참고 항목:  
[[`SELECT`]](/language-spec/select)

* * * 

## 구문 {#syntax}

```sql
SELECT JSON_GROUP_OBJECT(key_column, value_column) FROM <multipartIdentifier>
WHERE condition
GROUP BY grouping_column;
```

## 인수 {#arguments}

__*key_column*__  
JSON 객체의 키를 제공하는 열입니다.

__*value_column*__  
JSON 객체의 값을 제공하는 열입니다.

__*grouping_column*__  
`GROUP BY` 절에 지정하는 열입니다. `JSON_GROUP_OBJECT` 함수의 인수가 아니며, 이 함수는 정확히 2개의 인수를 받습니다. 그룹당 JSON 객체 1개가 반환됩니다.

## 반환 값 {#return-values}
지정한 열에서 집계된 모든 키-값 쌍을 포함하는 JSON 객체를 `grouping_column` 열에 따라 그룹별로 반환합니다.

* * *

## 예제 {#examples}

### 프로그래밍 언어별 리포지토리 크기 집계 {#aggregate-repository-sizes-by-programming-language}

```sql
-- Example: Group repository names with their corresponding sizes by programming language
SELECT
  language, 
  JSON_GROUP_OBJECT(name, size) AS language_index
FROM
  github.repos.repos
WHERE
  org = 'stackql'
  AND language IS NOT NULL
GROUP BY language;
```

이 쿼리는 프로그래밍 언어마다 JSON 객체를 반환합니다. 각 객체에는 리포지토리 이름과 해당 크기가 키-값 쌍으로 포함됩니다. 예상 출력의 예는 다음과 같습니다.

```plaintext
|-------------|-----------------------------------------------------------------------------------|
| language    | language_index                                                                    |
|-------------|-----------------------------------------------------------------------------------|
| C           | {"go-sqlite3":35667,"data-encryption-spike":50746}                                |
|-------------|-----------------------------------------------------------------------------------|
| C++         | {"expt-any-sdk":2399,"isqlang":9}                                                 |
|-------------|-----------------------------------------------------------------------------------|
| Dockerfile  | {"stackql-compliance-checks-devel":8}                                             |
|-------------|-----------------------------------------------------------------------------------|
| Go          | {"stackql-provider-registry":115711, ...}                                         |
|-------------|-----------------------------------------------------------------------------------|
```

이 출력은 조직 내 여러 프로그래밍 언어에 걸친 리포지토리 크기 분포를 시각화하는 데 유용하며, 언어별로 그룹화된 명확하고 체계적인 데이터를 제공합니다.
