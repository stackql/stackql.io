---
title: 연결 연산자
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 || 연결 연산자: 둘 이상의 문자열 값을 하나의 문자열로 연결합니다."
image: "/img/stackql-featured-image.png"
---
둘 이상의 문자열 값을 연결(결합)하여 생성된 문자열을 반환합니다.  

참고 항목:  
[[` SELECT `]](/language-spec/select) 

* * * 

## 구문 {#syntax}

```sql
SELECT character_expression1 || character_expression2 FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*character_expression*__  
리터럴 문자열 또는 문자열 열입니다.

## 반환 값 {#return-values}

문자열을 반환합니다.

:::note

StackQL `v1.0`부터 파서는 `||` 연산자를 연결 연산자로 처리하며, 이 연산자는 비교 연산자보다 우선순위가 높습니다. 따라서 `WHERE id = prefix || suffix` 절은 `id` 열을 연결된 문자열과 비교합니다.  이전 릴리스에서는 `||` 연산자를 부울 `OR` 연산자로 토큰화했습니다.  부울 논리합에는 `OR` 연산자를 사용하세요.  [세션 변수](/getting-started/session-variables)도 `$prefix || '-' || $suffix`와 같이 연결할 수 있습니다.

:::

* * *

## 예제 {#examples}

### 두 필드 연결 {#concatenate-two-fields-together}

```sql
SELECT name || ' (' || location || ')' as bucket_location
FROM google.storage.buckets 
WHERE project = 'stackql-demo';
```

자세한 내용은 [https://sqlite.org/lang_expr.html](https://sqlite.org/lang_expr.html) 문서를 참조하세요.
