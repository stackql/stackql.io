---
title: GROUP_CONCAT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 GROUP_CONCAT 집계 함수: 열 또는 그룹의 NULL이 아닌 값을 선택적 구분 기호를 사용해 하나의 문자열로 연결합니다."
image: "/img/stackql-featured-image.png"
---
입력 열 또는 그룹의 `NULL`이 아닌 모든 값을 연결한 문자열을 반환합니다.

> 연결된 요소의 순서는 임의로 정해집니다.

참고 항목:  
[[` SELECT `]](/language-spec/select)

* * * 

## 구문 {#syntax}

```sql
SELECT GROUP_CONCAT(columnExpression [, separator]) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## 인수 {#arguments}

__*columnExpression*__  
열 또는 식입니다. __*__ 기호를 지정하면 리소스 또는 그룹의 모든 레코드 수를 셉니다.

> *columnExpression*의 값 중 하나 이상은 `NULL`이 아니어야 합니다. 그렇지 않으면 `NULL`이 반환됩니다.

__*separator*__  
연결된 결과에 사용되는 구분 기호입니다. *separator*를 지정하지 않으면 기본 구분 기호인 `,` 문자가 사용됩니다.

__*groupByColumn*__  
요약 또는 집계 작업을 수행하는 데 사용되는 하나 이상의 열입니다. `GROUP BY` 절은 열 그룹당 1행을 반환합니다.

## 반환 값 {#return-values}

*separator* 또는 `,` 문자로 구분된 값 목록을 문자열로 반환합니다.

* * *

## 예제 {#examples}

### 전체 리소스에 대한 열 값 목록 반환 {#return-a-list-of-values-for-a-column-over-an-entire-resource}

```sql
SELECT group_concat(name) as instance_list
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### 다른 열 식으로 그룹화된 열 식의 값 목록 반환 {#return-a-list-of-values-for-a-column-expression-grouped-by-another-column-expression}

```sql
SELECT tags, group_concat(name) as instance_list
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY json_extract(tags, '$.instanceType');
```

자세한 내용은 [https://www.sqlite.org/lang_aggfunc.html#group_concat](https://www.sqlite.org/lang_aggfunc.html#group_concat) 문서를 참조하세요.
