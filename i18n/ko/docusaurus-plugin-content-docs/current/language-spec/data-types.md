---
title: 데이터 형식
hide_title: false
hide_table_of_contents: true
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL 데이터 형식: 쿼리와 결과에서 리소스 속성과 매개 변수에 사용되는 형식 및 프로바이더 API 스키마와의 매핑입니다."
image: "/img/stackql-featured-image.png"
---

StackQL 데이터 형식은 클라우드 리소스 내에서 사용되는 입력 또는 출력 필드(속성 또는 매개 변수)의 값을 나타내는 데 사용됩니다. StackQL 데이터 형식은 아래 표에 나와 있습니다.

| StackQL 형식 | 설명 | 예제 |
|--|--|--|
| `string` | 작은따옴표로 묶인 유니코드 문자 시퀀스 | `'mybucket'` |
| `array` | 대괄호(`[ ]`)로 묶이고 쉼표로 구분되는, 순서가 있는 값 컬렉션 | `[0,1,2]` |
| `object` | `{ }` 사이에 삽입되고 쉼표로 구분되는, 0개 이상의 이름/값 쌍으로 이루어진 순서 없는 집합 | `{"name": "mydb"}` |
| `boolean` | `true` 또는 `false`, 부울 값은 따옴표로 묶지 않으며 문자열 값으로 처리됩니다 | `true` |
| `integer` | 양수, 음수 또는 0일 수 있는 정수 | `2` |
| `double` | 양수 또는 음수일 수 있는 배정밀도 부동 소수점 숫자 | `0.95` |
<br/>
<br/>
> StackQL 데이터 형식은 모든 주요 클라우드 제공업체의 API에 노출되는 데이터 형식에 매핑됩니다.

## 형식 변환 {#type-conversion}

`CAST(expression AS type)` 및 `CONVERT(expression, type)` 함수는 값을 다른 형식으로 변환합니다. MySQL 문법에서 상속된 변환 형식(`CHAR`, `DATE`, `DATETIME`, `DECIMAL`, `SIGNED`, `UNSIGNED`, `TIME`, `JSON`) 외에도 StackQL `v1.0` 이상은 `TEXT`, `INTEGER`, `INT`, `BIGINT`, `BOOLEAN` 및 `BOOL` 형식을 허용합니다. 변환은 SQL 백엔드에서 수행되므로 그 의미 체계는 SQLite 또는 PostgreSQL의 의미 체계를 따릅니다. 변환은 형식이 있는 [세션 변수](/getting-started/session-variables)를 문자열 열과 비교할 때 자주 사용됩니다.

```sql
SET some_id = 42;

SELECT CAST($some_id AS TEXT) AS id_text,
       CONVERT($some_id, TEXT) || '-east' AS label;
```

AWS와 같은 XML 프로바이더가 제공하는 부울 필드는 `true` 및 `false`로 보고됩니다. `v1.0` 이전의 StackQL 릴리스는 이를 `0` 및 `1`로 보고했습니다.
