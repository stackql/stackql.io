---
title: SET
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - 세션 변수
  - set
  - 프로바이더 세션
  - 시크릿
description: "StackQL의 SET: 형식이 지정된 세션 변수를 할당하고, 달러 기호로 시작하는 대상으로 현재 세션의 프로바이더 별칭, 자격 증명 필드, 시크릿을 구성합니다."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

세션 변수를 할당하거나 현재 세션의 런타임 설정을 구성합니다.  StackQL `v1.0` 이상에서 사용할 수 있습니다.

참고 항목:  
[[` UNSET `]](/language-spec/unset) [[ 세션 변수 ]](/getting-started/session-variables) [[ 프로바이더 세션 ]](/getting-started/provider-sessions) [[` IDENTIFIER `]](/language-spec/functions/general/identifier)

* * * 

## 구문 {#syntax}

*setStatement::=*

<RailroadDiagram
type="set"
/>

*setExpression::=*

<RailroadDiagram
type="setExpression"
/>

&nbsp;  
&nbsp;  

```sql
SET [ SESSION ] <target> = { <literal> | DEFAULT | <reference> } [, <target> = { <literal> | DEFAULT | <reference> } ...];
```

__*target*__  
일반 식별자(`region`), 큰따옴표로 묶은 이름(`"providers.saved"`) 또는 큰따옴표로 묶고 달러 기호로 시작하는 구성 경로(`"$.auth.aws.password"`) 중 하나입니다.

__*literal*__  
문자열, 숫자, 부울 또는 `null` 값입니다.  문자열은 작은따옴표로 묶습니다.  배열과 객체는 스칼라 값이 아닙니다. JSON 문서는 문자열로 저장하세요.

__*DEFAULT*__  
대상을 재설정하며, [`UNSET`](/language-spec/unset) 문과 같습니다.  명시적인 `null` 값은 재설정이 아니라 저장되는 값입니다.

__*reference*__  
기존 변수를 가리키는, 큰따옴표로 묶은 `"$name"` 참조입니다(예: `SET "$.providers.production" = "$providers.saved";`).

__*SESSION*__  
선택 사항이며, StackQL에 있는 유일한 범위입니다.  `SET SESSION name = ...` 문과 `SET name = ...` 문은 같은 문입니다. 모든 할당은 현재 세션에만 속하며 그 밖의 어디에도 속하지 않습니다.  이 키워드는 `SET SESSION` 문과 `SET GLOBAL` 문이 서로 다른 데이터베이스에서와 같은 방식으로 스크립트를 읽을 수 있도록 하기 위해 존재합니다.  `SET GLOBAL` 문은 구문 분석은 되지만, 세션 간에 공유되는 상태가 없으므로 `configuration scope "global" is not supported; use SESSION` 오류와 함께 거부됩니다.

하나의 `SET` 목록에 있는 할당은 순서대로 적용되며 트랜잭션이 아닙니다. 목록의 두 번째 할당이 실패해도 첫 번째 할당은 그대로 유지됩니다.

## 대상 {#targets}

| 대상 | 효과 |
|--|--|
| `name` 또는 `"name"` | 스칼라 세션 변수를 저장하며, 이후 문에서 `$name`으로 역참조합니다.  점만으로는 구성이 선택되지 않습니다. `"providers.saved"` 대상은 `providers.saved`라는 이름의 변수입니다.  이전 `"variables.name"` 표기는 `name` 변수의 별칭입니다. |
| `"$.auth.<provider>.<field>"` | 이 세션에 대해 프로바이더 인증 컨텍스트의 필드 하나를 설정합니다.  필드는 해당 프로바이더에 대한 [`--auth`](/command-line-usage/global-flags) 객체의 키입니다(예: `credentialsenvvar`, `password`, `token_url`). |
| `"$.providers.<alias>"` | 고유한 자격 증명, 테넌시 레이블, 매개 변수 기본값, 튜닝 설정을 갖는 프로바이더 별칭을 바인딩합니다.  값은 문자열에 담긴 JSON 문서입니다. [프로바이더 세션](/getting-started/provider-sessions#provider-aliases)을 참조하세요. |
| `"$.secrets.<name>"` | 세션의 시크릿 저장소에 시크릿을 저장합니다.  값은 리터럴 문자열(일반적으로 JSON `auth` 문서)이며, 프로바이더 별칭에서 `auth_secret` 키로 참조합니다. |
| `"$.secret_store"` | 영구 OS 시크릿 저장소를 선택합니다(`{"backend":"os","namespace":"..."}`).  `--allow-persistent-secrets` 운영자 플래그가 필요합니다. |

모든 형태의 대상은 현재 세션에만 영향을 줍니다.  [`srv`](/command-line-usage/srv) 서버에 대한 독립된 연결은 서로의 할당을 볼 수 없으며, 각 MCP 요청은 별도의 세션입니다. 따라서 MCP 서버를 사용할 때는 하나의 문 배치에서 구성과 쿼리를 함께 수행하세요.

* * *

## 예제 {#examples}

### 세션 변수 설정 {#set-session-variables}

각 문은 지정한 이름으로 스칼라 값 하나를 저장하며, 값은 형식을 유지합니다. `region` 변수는 문자열, `minimum_count` 변수는 정수, `enabled` 변수는 부울입니다.  `null` 값도 값으로 저장되므로, 이후 `$optional_value` 변수는 정의되지 않았다는 이유로 실패하지 않고 SQL `NULL`로 해석됩니다.  마지막 문은 하나의 목록에서 변수 2개를 할당합니다.

```sql
SET region = 'ap-southeast-2';
SET minimum_count = 3;
SET enabled = true;
SET optional_value = null;
SET suffix = 'East', some_id = 42;
```

### 세션 범위 명시 {#make-the-session-scope-explicit}

처음 두 문은 동일합니다. `SESSION` 키워드는 존재하는 유일한 범위를 나타내며, 두 할당 모두 변수가 해제되거나 세션이 끝날 때까지 유지됩니다.  세션 설정과 전역 설정을 구분하는 데이터베이스에서와 같은 방식으로 스크립트가 읽히도록 하려면 이 키워드를 사용하세요.  주석 처리된 문은 비교를 위해 표시한 것입니다. 이 문은 아무것도 할당하지 않으며, 세션 간에 공유되는 상태가 없으므로 `configuration scope "global" is not supported; use SESSION` 오류와 함께 실패합니다.

```sql
SET region = 'ap-southeast-2';
SET SESSION region = 'ap-southeast-2';

/*
SET GLOBAL region = 'ap-southeast-2';
-- FAILS with configuration scope "global" is not supported; use SESSION
*/
```

### 쿼리에서 변수 사용 {#use-a-variable-in-a-query}

모든 식 위치에서 따옴표 없이 `$name` 형식으로 변수를 역참조합니다.  값은 형식을 유지합니다. 문자열은 SQL 리터럴로 이스케이프되고, 숫자, 부울, `null` 값은 그대로 전달됩니다.  `WHERE` 절의 변수는 요청의 실행 계획이 수립되기 전에 해석되므로, 리터럴과 마찬가지로 프로바이더에 푸시됩니다.

```sql
SET project = 'stackql-demo';
SET zone = 'australia-southeast1-a';

SELECT name, status FROM google.compute.instances
WHERE project = $project AND zone = $zone;
```

### 변수 재설정 {#reset-a-variable}

`DEFAULT` 키워드는 [`UNSET`](/language-spec/unset) 문과 똑같이 변수를 제거합니다. 따라서 이후의 `$region` 변수는 다시 설정될 때까지 `variable "$region" is not defined` 오류와 함께 실패하며, 주석 처리된 쿼리가 이 실패를 보여 줍니다.  이는 `null` 값을 할당하는 것과 다릅니다. 이 경우 변수는 `NULL` 값을 가진 채 그대로 남습니다.

```sql
SET region = 'ap-southeast-2';
SET region = DEFAULT;

/*
SELECT name FROM aws.s3.buckets WHERE region = $region;
-- FAILS with variable "$region" is not defined
*/
```

### 다른 변수의 값 복사 {#copy-a-value-from-another-variable}

오른쪽에는 기존 변수를 가리키는, 큰따옴표로 묶은 참조를 사용할 수 있으며, 이 경우 그 시점에 변수가 가진 값이 복사됩니다.  이 예제에서는 먼저 프로바이더 바인딩을 `providers.saved`라는 이름의 일반 변수로 저장한 다음(점이 있다고 해서 구성이 되지는 않습니다), `$.providers.production` 경로로 복사하여 활성화합니다.  이후 저장된 복사본을 제거해도 활성 바인딩에는 영향을 주지 않습니다.

```sql
SET "providers.saved" = '{"provider":"aws","tenancy":"production-account","auth":{"type":"aws_signing_v4","access_key_id_env_var":"PRODUCTION_AWS_ACCESS_KEY_ID","secret_access_key_env_var":"PRODUCTION_AWS_SECRET_ACCESS_KEY"}}';
SET "$.providers.production" = "$providers.saved";
UNSET "providers.saved";
```

### 이 세션의 자격 증명 필드 재정의 {#override-a-credential-field-for-this-session}

프로바이더의 인증 컨텍스트는 시작 시 [`--auth`](/command-line-usage/global-flags) 플래그를 통해 해당 프로바이더에 제공된 객체입니다.  `$.auth` 아래의 경로는 남은 세션 동안 이 객체의 필드 하나를 대체하며, 나머지 필드는 그대로 둡니다.  이 예제에서는 GitHub 프로바이더가 읽기 전용 토큰을 담은 다른 환경 변수를 가리키도록 합니다. 이 변수는 StackQL 프로세스 환경에서 읽으며, 이후 `github.*`에 대한 쿼리는 필드가 해제되거나 세션이 끝날 때까지 이 변수를 사용합니다.

```sql
SET "$.auth.github.credentialsenvvar" = 'GITHUB_TOKEN_READONLY';
```

### 프로바이더 별칭 바인딩 {#bind-a-provider-alias}

프로바이더 별칭은 독립적으로 구성된 프로바이더의 두 번째 인스턴스로, 쿼리에서 프로바이더 이름 대신 사용합니다.  값은 문자열에 담긴 JSON 문서입니다. `provider` 키는 기반 프로바이더의 이름을 지정하고, `tenancy` 키는 계정에 레이블을 지정하며, `auth` 키는 `--auth` 플래그와 같은 형태의 자격 증명 블록이고, `params` 키는 `region` 같은 요청 매개 변수의 기본값을 제공합니다.  그러면 `production.s3.buckets`에 대한 쿼리는 해당 자격 증명으로 실행되고, `aws.s3.buckets` 리소스는 시작 시 구성을 유지합니다.  전체 키 목록은 [프로바이더 세션](/getting-started/provider-sessions#provider-aliases)에 있습니다.

```sql
SET "$.providers.production" = '{
  "provider": "aws",
  "tenancy": "production-account",
  "auth": {
    "type": "aws_signing_v4",
    "access_key_id_env_var": "PRODUCTION_AWS_ACCESS_KEY_ID",
    "secret_access_key_env_var": "PRODUCTION_AWS_SECRET_ACCESS_KEY"
  },
  "params": {"region": "ap-southeast-2"}
}';

SELECT name FROM production.s3.buckets;
```
