---
title: 세션 변수
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - 세션 변수
  - set
  - unset
  - identifier
description: "StackQL의 세션 변수: SET으로 형식이 있는 스칼라 값을 설정하고, 조건자와 식에서 $name으로 역참조하며, IDENTIFIER로 관계 이름과 열 이름에 사용합니다."
image: "/img/stackql-featured-image.png"
---

참고 항목:  
[[` SET `]](/language-spec/set) [[` UNSET `]](/language-spec/unset) [[ 프로바이더 세션 ]](/getting-started/provider-sessions) [[ 변수 사용 ]](/getting-started/variables)

세션 변수는 현재 세션이 보관하고 이후 문에서 참조하는 스칼라 값입니다. 세션 변수는 StackQL `v1.0` 이상에서 사용할 수 있으며, 대화형 셸, `exec` 스크립트, PostgreSQL 와이어 프로토콜 및 MCP 서버에서 작동합니다.

세션 변수는 [변수 사용](/getting-started/variables)에서 설명하는 템플릿 변수와 다릅니다. 템플릿 변수는 `--iqldata` 및 `--var` 플래그를 통해 `stackql exec` 명령에 제공되며, 스크립트 텍스트가 구문 분석되기 전에 해당 텍스트에 렌더링됩니다. 세션 변수는 구문 분석된 문 안에서 확인되고, 형식을 유지하며, 문과 문 사이에서 변경할 수 있습니다.

## 변수 설정 및 제거 {#setting-and-removing-variables}

[`SET`](/language-spec/set) 문은 문자열, 숫자, 부울 또는 `null` 값을 할당합니다. [`UNSET`](/language-spec/unset) 문은 변수를 제거하며, `SET name = DEFAULT` 문도 같은 동작을 합니다. 명시적인 `null` 값은 저장된 값이며 초기화가 아닙니다.

```sql
SET region = 'ap-southeast-2';
SET minimum_count = 3;
SET enabled = true;
SET optional_value = null;
UNSET region;
```

배열과 객체는 스칼라 값이 아닙니다. JSON 문서를 보관하려면 문자열로 저장하세요.

```sql
SET "providers.saved" = '{"provider":"aws","tenancy":"production"}';
```

## 변수 역참조 {#dereferencing-a-variable}

식이 올 수 있는 모든 위치에서 따옴표 없이 `$name` 형식을 사용하세요. 문자열은 SQL 리터럴로 이스케이프되며, 숫자, 부울 및 `null` 값은 형식을 유지합니다. 작은따옴표로 묶은 `'$name'` 값은 역참조가 아니라 리터럴 텍스트 `$name`입니다. 존재하지 않는 변수를 참조하면 프로바이더 요청이 이루어지기 전에 실패합니다.

확인된 값은 `CAST`, `CONVERT`, `||` 연산자를 사용한 연결, 함수 호출을 포함한 일반 SQL 식에서 사용할 수 있습니다.

```sql
SET some_id = 42;
SET suffix = 'East';

SELECT CAST($some_id AS TEXT) AS id_text,
       CONVERT($some_id, TEXT) || '-' || lower($suffix) AS label,
       md5(lower(CONVERT($some_id, TEXT) || '-' || $suffix)) AS digest,
       '$some_id' AS literal_text;
```

할당에는 스칼라 리터럴이나 직접 참조만 사용할 수 있습니다. 변환과 기타 식은 `SET` 문이 아니라 변수를 사용하는 곳에서 적용하세요.

## 조건자의 변수 {#variables-in-predicates}

`WHERE` 절의 변수는 문의 실행 계획이 수립되기 전에 확인되므로, 리터럴과 똑같이 프로바이더 API로 라우팅됩니다. 따라서 필수 매개 변수를 변수로 제공할 수 있습니다.

```sql
SET project = 'stackql-demo';
SET zone = 'australia-southeast1-a';

SELECT name, status
FROM google.compute.instances
WHERE project = $project AND zone = $zone;
```

형식이 있는 값은 해당 형식으로 비교됩니다. `SET enabled = false` 문과 함께 사용한 `WHERE active = $enabled` 절은 부울 값으로 필터링하며, 프로바이더 열이 문자열이고 변수가 숫자인 경우에는 `WHERE id = CAST($some_id AS TEXT)` 형식을 사용합니다.

## 객체 이름으로 사용하는 변수 {#variables-as-object-names}

변수는 값뿐 아니라 관계와 열의 이름도 지정할 수 있습니다. 이름은 구문 분석된 문 안에서 식별자로 변환되며 SQL 텍스트에 이어 붙여지지 않습니다. 따라서 문을 포함하거나, 비어 있거나, 문자열이 아닌 값은 프로바이더에 액세스하기 전에 거부됩니다.

| 위치 | 형식 |
|--|--|
| `FROM` 절 | `FROM $relation_name` 또는 구성 요소별로 `FROM $provider.$service.$resource` |
| 식의 열 | `IDENTIFIER($column_name)`([`IDENTIFIER`](/language-spec/functions/general/identifier) 참조) |
| 열 또는 테이블 별칭 | `AS "$alias_name"` |
| `ORDER BY` | `ORDER BY IDENTIFIER($column_name)` |
| `LIMIT` | `LIMIT $row_count` |
| DDL 열 이름, `INSERT` 열 목록, `UPDATE` 대상 | 큰따옴표로 묶은 `"$name"` 형식 |
<br/>

```sql
SET relation_name = 'aws.s3.buckets';
SET column_name = 'name';
SET output_name = 'bucket';
SET row_count = 10;

SELECT IDENTIFIER($column_name) AS "$output_name"
FROM $relation_name
WHERE region = 'ap-southeast-2'
ORDER BY IDENTIFIER($column_name)
LIMIT $row_count;
```

조인, CTE 및 하위 쿼리도 같은 방식으로 관계 변수를 확인합니다. 점으로 구분된 관계 값은 최대 4개의 구성 요소를 가집니다. 변수는 임의의 SQL 조각이 아닙니다. 키워드, 쉼표로 구분된 열 목록 또는 `WHERE` 절 전체는 대체할 수 없습니다.

## 범위 및 격리 {#scope-and-isolation}

변수는 변수를 설정한 세션에 속합니다. `stackql srv` 서버에 대한 별도의 연결은 각각 별도의 변수를 가지며, 별도의 `exec` 호출도 마찬가지입니다. 각 MCP 요청은 별도의 세션이므로, `SET` 문과 이를 사용하는 쿼리는 같은 문 배치로 보내세요. 새 세션은 변수 없이 시작됩니다.

범위는 세션뿐입니다. `SET SESSION` 및 `UNSET SESSION` 문도 허용되며 `SET` 및 `UNSET` 문과 정확히 같은 의미입니다. 이 키워드는 세션 설정과 전역 설정이 구분되는 데이터베이스에서와 같은 방식으로 읽혀야 하는 스크립트를 위해 존재합니다. 세션 간에 공유되는 상태가 없으므로 `GLOBAL` 키워드는 거부됩니다.

준비된 문은 준비될 때가 아니라 실행될 때 변수를 확인하며, 바인딩된 세션 값을 포함하는 실행 계획은 여러 실행 간에 재사용되지 않습니다.

[액세스 관리](/command-line-usage/access-management)가 활성화되어 있으면 변수를 읽는 데 `session/variables:read` 권한이 필요하고, 변수를 설정하거나 초기화하는 데 `session/config:write` 권한이 필요합니다. 변수를 객체 이름으로 확인해도 해당 객체에 대한 권한은 부여되지 않으며, 확인된 관계는 평소와 같이 실행 전에 권한 부여를 받습니다.
