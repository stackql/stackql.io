---
title: UNSET
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - 세션 변수
  - unset
  - 프로바이더 세션
  - 시크릿
description: "StackQL의 UNSET: 세션 변수를 제거하거나, 현재 세션에서 SET으로 구성한 프로바이더 별칭, 자격 증명 필드, 시크릿을 지웁니다."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

세션 변수를 제거하거나, 현재 세션에서 [`SET`](/language-spec/set) 문으로 설정한 런타임 구성을 지웁니다. StackQL `v1.0` 이상에서 사용할 수 있습니다.

참고 항목:  
[[` SET `]](/language-spec/set) [[ 세션 변수 ]](/getting-started/session-variables) [[ 프로바이더 세션 ]](/getting-started/provider-sessions)

* * * 

## 구문 {#syntax}

*unsetStatement::=*

<RailroadDiagram
type="unset"
/>

&nbsp;  
&nbsp;  

```sql
UNSET [ SESSION ] <target> [, <target> ...];
```

__*target*__  
`SET` 문에서 허용하는 모든 대상입니다. 일반 변수 이름 또는 큰따옴표로 묶은 변수 이름이거나, 큰따옴표로 묶고 달러 기호 접두사를 붙인 구성 경로입니다.

__*SESSION*__  
선택 사항이며, 존재하는 유일한 범위입니다. 모든 변수와 구성 설정은 현재 세션에 속하므로 `UNSET SESSION name` 문과 `UNSET name` 문은 같은 문입니다. `UNSET GLOBAL` 문은 `configuration scope "global" is not supported; use SESSION` 오류와 함께 거부됩니다.

`UNSET name` 문은 `SET name = DEFAULT` 문과 같습니다. 한 목록에 있는 대상은 순서대로 제거되며, 설정되지 않은 이름을 제거해도 오류가 아닙니다. 설정이 해제된 변수를 역참조하면 프로바이더 요청이 이루어지기 전에 실패합니다.

| 대상 | 효과 |
|--|--|
| `name` | 세션 변수를 제거합니다. |
| `"$.auth.<provider>.<field>"` | 프로바이더의 세션 인증 컨텍스트에서 해당 필드를 삭제합니다. 재정의 전에 적용되던 값은 복원되지 않습니다. |
| `"$.providers.<alias>"` | 프로바이더 별칭 바인딩을 제거합니다. |
| `"$.secrets.<name>"` | 세션의 저장소에서 시크릿을 삭제합니다. 영구 OS 저장소인 경우 저장된 값이 삭제됩니다. |
| `"$.secret_store"` | 현재 저장소 바인딩을 버리고 빈 세션 메모리로 시작합니다. OS 저장소에 이미 저장된 값은 삭제되지 않습니다. |

* * *

## 예제 {#examples}

### 변수 제거 {#remove-a-variable}

변수가 세션에서 제거되므로, 주석 처리된 쿼리는 오래된 값이나 빈 값으로 실행되지 않고 `variable "$region" is not defined` 오류로 실패합니다. 이 이름을 다시 사용하려면 다시 설정하세요.

```sql
SET region = 'ap-southeast-2';
UNSET region;

/*
SELECT name FROM aws.s3.buckets WHERE region = $region;
-- FAILS with variable "$region" is not defined
*/
```

### 여러 대상 제거 {#remove-several-targets}

하나의 문으로 변수와 구성 경로를 함께 제거할 수 있으며, 대상은 순서대로 처리됩니다. 이 예제에서는 일반 변수, 이름에 점이 포함된 변수, 자격 증명 재정의를 한 목록에서 제거합니다.

```sql
UNSET region, "providers.saved", "$.auth.github.password_var";
```

### 세션 범위 명시 {#make-the-session-scope-explicit}

다음 두 문은 같은 작업을 수행합니다. `SESSION` 키워드는 존재하는 유일한 범위를 지정합니다. 명시적 형식은 세션 상태와 전역 상태를 구분하는 데이터베이스에서도 실행되는 스크립트에 적합합니다. 공유 상태가 존재하지 않으므로 주석 처리된 문은 실패합니다.

```sql
UNSET region;
UNSET SESSION region;

/*
UNSET GLOBAL region;
-- FAILS with configuration scope "global" is not supported; use SESSION
*/
```

### 자격 증명 재정의 해제 {#clear-a-credential-override}

앞에서 `SET "$.auth.github.password_var"` 문으로 설정한 필드가 GitHub 프로바이더의 세션 인증 컨텍스트에서 삭제되며, 해당 컨텍스트의 다른 필드는 변경되지 않습니다. 재정의 전에 적용되던 값은 복원되지 않으므로, 프로바이더에 해당 필드가 여전히 필요하면 다시 설정하세요.

```sql
UNSET "$.auth.github.password_var";
```

### 프로바이더 별칭과 해당 시크릿 제거 {#remove-a-provider-alias-and-its-secret}

`production` 별칭은 `SET "$.providers.production"` 문으로 바인딩되었으며, `auth_secret` 키를 통해 `$.secrets.production` 경로에 저장된 시크릿을 참조합니다. 하나의 문으로 둘 다 제거하면 바인딩이 삭제되어 `production.s3.buckets` 관계는 더 이상 유효하지 않으며, 세션 저장소에서 시크릿도 삭제됩니다. 다른 모든 곳과 마찬가지로 여기서도 `SESSION` 키워드는 선택 사항입니다.

```sql
UNSET SESSION "$.providers.production", "$.secrets.production";
```
