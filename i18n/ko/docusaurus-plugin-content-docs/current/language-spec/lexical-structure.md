---
title: 어휘 구조
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 어휘 구조: 파서가 문, 식별자, 따옴표 처리, 리터럴, 주석, 공백 문자를 토큰으로 분리하는 방식입니다."
image: "/img/stackql-featured-image.png"
---

참고 항목:  
[[ StackQL 리소스 계층 구조 ]](/getting-started/resource-hierarchy) [[ 키워드 ]](/language-spec/keywords) [[ 데이터 형식 ]](/language-spec/data-types) [[ DESCRIBE ]](/language-spec/describe)


StackQL의 어휘 구조와 문법은 MySQL의 언어 구현(ANSI SQL-92 표준을 변형한 것)을 모델로 하며, 일부 확장 기능이 추가되어 있습니다.  StackQL 문은 클라우드 리소스를 쿼리하거나(`SELECT` 문법 사용), `INSERT`, `UPDATE`, `REPLACE`, `DELETE` 등의 표준 데이터 조작 언어(DML) 문을 사용하여 인프라를 생성, 수정 또는 삭제하는 데 사용됩니다.  또한 인스턴스 시작이나 AWS S3 버킷의 객체 가져오기 또는 넣기와 같은 DML 이외의 작업은 기본 제공 프로바이더 프로시저(DBMS의 저장 프로시저와 유사)로 구현됩니다.  기본 제공 프로바이더 프로시저는 `EXEC` 문으로 호출합니다.

StackQL의 SQL 도메인 특화 언어를 사용하면 선언적 인프라 프로그래밍과 절차적 인프라 프로그래밍을 모두 수행할 수 있습니다.

* * * 

## 구문 {#syntax}
StackQL 작업은 세미콜론(`;`)으로 끝나는 하나 이상의 문으로 구성됩니다.  문에는 키워드, 식별자, 매개 변수, 값이 포함됩니다. 다음은 GCP에서 10GB 디스크를 생성하는 StackQL 문의 예입니다.

```sql
INSERT INTO google.compute.disks (project, zone, name, sizeGb)
SELECT 'myProject', 'australia-southeast1-a', 'my10GbDisk', 10;
```

위 예제에는 키워드(_`INSERT`_, _`INTO`_, _`SELECT`_), 식별자(_google.compute.disks_), 매개 변수(_project_, _zone_, _name_, _sizeGb_), 값(_'myProject'_, _'australia-southeast1-a'_, _'my10GbDisk'_, _10_)이 포함되어 있습니다.

## 키워드 {#keywords}
키워드는 대소문자를 구분하지 않습니다. 즉, `INSERT`와 `insert`는 동일한 키워드로 처리됩니다. 다만 관례에 따라 대문자로 표기하는 경우가 많습니다. 전체 키워드 목록은 키워드 섹션에서 확인할 수 있습니다.

## 식별자 {#identifiers}
식별자는 클라우드 제공업체 리소스 계층 구조의 객체를 나타내는 기호입니다.  식별자는 여러 부분으로 구성됩니다. 식별자에 대한 자세한 내용은 StackQL 리소스 계층 구조 항목에서 확인할 수 있습니다.

## 매개 변수 및 값 {#parameters-and-values}
매개 변수는 리소스에서 지정합니다. 매개 변수는 특정 클라우드 제공업체 리소스에 대해 `DESCRIBE` 문을 사용하여 확인할 수 있습니다.  값은 매개 변수에 매핑되는, 형식이 지정된 스칼라 객체 또는 복합 객체입니다.  매개 변수와 값에 대한 자세한 내용은 `DESCRIBE` 항목과 데이터 형식 항목을 참조하세요.

## 변수 {#variables}
`$region`처럼 달러 기호로 시작하는 식별자는 [`SET`](/language-spec/set) 문으로 할당한 세션 변수를 역참조합니다. 이 식별자는 문의 실행 계획이 수립되기 전에 형식이 지정된 변수 값으로 대체됩니다.  `'$region'`처럼 작은따옴표로 묶으면 문자열 리터럴이 됩니다.  `FROM` 절에서 `$variable` 변수는 관계 이름으로 해석되고, 식 위치에서 `IDENTIFIER($variable)` 식은 열 이름으로 해석됩니다.  `"$.auth.aws.password"`처럼 큰따옴표로 묶고 달러 기호로 시작하는 경로는 `SET` 문과 `UNSET` 문에서 사용할 수 있는 런타임 구성 대상입니다.  세션 변수는 StackQL `v1.0` 이상에서 사용할 수 있습니다. [세션 변수](/getting-started/session-variables)를 참조하세요.

## 연산자 {#operators}
`||` 연산자는 문자열 연결 연산자이며 비교 연산자보다 결합 우선순위가 높습니다.  `OR` 연산자는 부울 논리합입니다.  `v1.0` 이전의 StackQL 릴리스에서는 `||` 연산자를 `OR` 연산자로 토큰화했습니다.

## 주석 {#comments}
StackQL은 StackQL 문 안에서 여러 줄 주석과 인라인 주석을 지원합니다. 다음은 주석의 예입니다.

```sql
/* This
Is a
Multiline
Comment
*/

--This is an inline comment
```
