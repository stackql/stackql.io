---
title: IDENTIFIER
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - 세션 변수
  - 식별자
description: "StackQL의 IDENTIFIER 함수: 열 이름을 담은 세션 변수를 열 참조로 해석하여 프로젝션과 ORDER BY에서 변수로 열을 지정할 수 있게 합니다."
image: "/img/stackql-featured-image.png"
---
문자열 값(일반적으로 세션 변수)을 열 이름으로 해석합니다. 단독으로 쓴 `$variable` 변수가 스칼라 값으로 읽히는 위치에서 이 함수를 사용하세요. StackQL `v1.0` 이상에서 사용할 수 있습니다.

참고 항목:  
[[` SET `]](/language-spec/set) [[ 세션 변수 ]](/getting-started/session-variables)

* * * 

## 구문 {#syntax}

```sql
SELECT IDENTIFIER($variable) FROM <multipartIdentifier> ORDER BY IDENTIFIER($variable);
```

## 인수 {#arguments}

__*variable*__  
열 이름을 담은 세션 변수 또는 문자열 리터럴입니다. `t.name`과 같은 정규화된 이름도 허용됩니다. 인수는 정확히 1개만 허용되며 단일 문자열 값이어야 합니다. `'t.' || $column_name`과 같은 연결, 두 번째 인수 또는 숫자 값은 `IDENTIFIER requires one string value` 오류로 거부됩니다. 빈 값이나 문을 포함한 값은 `invalid identifier value` 오류로 거부되며, 정의되지 않은 변수는 정의되지 않음 오류로 실패합니다. 이 모든 검사는 프로바이더 요청이 이루어지기 전에 수행됩니다.

## 반환 값 {#return-values}

구문 분석된 문에서 이 함수는 지정한 열에 대한 참조로 대체되므로 해당 열의 값을 반환합니다. 이름은 식별자로 변환되며 SQL 텍스트에 이어 붙여지지 않습니다. 결과 열 참조는 선택 목록, `WHERE`, `GROUP BY`, `ORDER BY` 절, 함수 인수, CTE 및 하위 쿼리 내부 등 열을 사용할 수 있는 모든 위치에서 사용할 수 있습니다.

* * *

## 예제 {#examples}

### 변수에 지정된 열로 프로젝션 및 정렬 {#project-and-order-by-a-column-named-in-a-variable}

반환할 열과 정렬 기준 열은 모두 `column_name` 변수에서 가져오며, 출력 열의 이름은 `output_name` 변수의 값으로 바뀝니다. 선택 목록과 `ORDER BY` 절에는 `IDENTIFIER` 함수가 필요합니다. 이 위치에서 단독으로 쓴 `$column_name` 변수는 열이 아니라 문자열 `'name'`이 되기 때문입니다. 별칭에는 대신 큰따옴표 형식을 사용합니다. 변수가 해석되면 이 문은 `SELECT name AS bucket FROM aws.s3.buckets WHERE region = 'ap-southeast-2' ORDER BY name` 문과 같습니다.

```sql
SET column_name = 'name';
SET output_name = 'bucket';

SELECT IDENTIFIER($column_name) AS "$output_name"
FROM aws.s3.buckets
WHERE region = 'ap-southeast-2'
ORDER BY IDENTIFIER($column_name);
```

### 변수에 지정된 열로 필터링 {#filter-on-a-column-named-in-a-variable}

해석된 참조는 프로젝션뿐 아니라 조건자에서도 작동합니다. 이 예제에서는 필터링할 필드와 일치시킬 값을 모두 변수에서 가져옵니다. `IDENTIFIER($filter_column)` 식은 `status` 열이 되고, `$filter_value` 변수는 문자열 리터럴 `'RUNNING'`으로 전달됩니다. 첫 번째 형식은 열의 이름을 지정하고 두 번째 형식은 값을 제공하므로 두 형식은 의도적으로 다릅니다.

```sql
SET filter_column = 'status';
SET filter_value = 'RUNNING';

SELECT name, zone
FROM google.compute.instances
WHERE project = 'stackql-demo'
AND zone = 'australia-southeast1-a'
AND IDENTIFIER($filter_column) = $filter_value;
```

### 테이블 별칭으로 열 정규화 {#qualify-the-column-with-a-table-alias}

점이 포함된 값은 정규화된 열 참조로 해석됩니다. 테이블에는 `w` 별칭을 지정하며, 별칭 자체도 변수에서 가져옵니다. `IDENTIFIER($column_name)` 식은 `w.name` 열이 됩니다. 조인의 양쪽에 같은 열 이름이 있어 참조가 모호해지는 경우에 이 형식을 사용합니다.

```sql
SET table_alias = 'w';
SET column_name = 'w.name';

SELECT IDENTIFIER($column_name) AS bucket
FROM aws.s3.buckets AS "$table_alias"
WHERE w.region = 'ap-southeast-2';
```

### 거부되는 값 {#rejected-values}

값은 쿼리 텍스트가 아니라 이름이 되므로, 사용할 수 있는 이름이 아닌 값은 문의 실행 계획을 수립하기 전에 거부되며 프로바이더에 요청이 전달되지 않습니다. 아래의 처음 두 문은 `invalid identifier value` 오류로 실패하고, 세 번째 문은 `IDENTIFIER requires one string value` 오류로 실패합니다.

```sql
/*
SET column_name = '';
SELECT IDENTIFIER($column_name) FROM aws.s3.buckets;
-- FAILS with invalid identifier value

SET column_name = 'name; DROP TABLE imaginary';
SELECT IDENTIFIER($column_name) FROM aws.s3.buckets;
-- FAILS with invalid identifier value

SET column_name = 17;
SELECT IDENTIFIER($column_name) FROM aws.s3.buckets;
-- FAILS with IDENTIFIER requires one string value
*/
```

> `FROM` 절의 `$variable` 변수는 `IDENTIFIER` 함수 없이도 관계로 해석되며, 별칭, DDL 열 이름, `INSERT` 열 목록, `UPDATE` 대상에는 큰따옴표로 묶은 `"$variable"` 형식을 사용합니다.
