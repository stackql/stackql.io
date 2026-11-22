---
title: 프로바이더 세션 및 시크릿
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - 프로바이더 세션
  - 다중 계정
  - 자격 증명
  - 시크릿
  - 키체인
description: "StackQL의 프로바이더 세션: 자체 자격 증명, 테넌시, 매개 변수 기본값으로 프로바이더 별칭을 바인딩하고, 세션별로 자격 증명 필드를 재정의하며, 시크릿을 세션 메모리나 OS 시크릿 저장소에 보관합니다."
image: "/img/stackql-featured-image.png"
---

참고 항목:  
[[` SET `]](/language-spec/set) [[` UNSET `]](/language-spec/unset) [[ 세션 변수 ]](/getting-started/session-variables) [[ 전역 플래그 ]](/command-line-usage/global-flags)

세션은 자체 자격 증명, 테넌시 레이블, 매개 변수 기본값, 요청 동시성 제한을 갖는 별칭으로 프로바이더를 바인딩할 수 있습니다. 따라서 같은 프로바이더의 여러 계정이나 테넌트를 하나의 세션에서, 그리고 하나의 문에서 쿼리할 수 있습니다. 세션에 대해 자격 증명 필드를 재정의할 수도 있으며, 시크릿은 세션 메모리에 보관하거나 운영자가 옵트인한 경우 운영 체제의 시크릿 저장소에 보관할 수 있습니다. 이 모든 설정은 [`SET`](/language-spec/set) 문과 [`UNSET`](/language-spec/unset) 문으로 구성하며, StackQL `v1.0` 이상에서 사용할 수 있습니다.

여기에서 설명하는 모든 내용의 범위는 현재 세션입니다. `stackql srv` 서버에 대한 독립적인 연결은 바인딩이나 시크릿을 공유하지 않으며, 각 MCP 요청은 별도의 세션입니다. 따라서 MCP 서버를 사용할 때는 구성과 쿼리를 하나의 문 배치에서 수행하세요.

## 프로바이더 별칭 {#provider-aliases}

`SET "$.providers.<alias>"` 문은 별칭을 프로바이더에 바인딩합니다. 값은 문자열에 담긴 JSON 문서입니다.

| 키 | 필수 | 설명 |
|--|--|--|
| `provider` | 예 | 별칭이 가리키는 정식 프로바이더입니다(예: `aws`). |
| `tenancy` | 예 | 계정 이름이나 테넌트 이름 같은 그룹화 레이블입니다. StackQL은 이 값을 프로바이더에 대해 검증하지 않습니다. |
| `auth` | `auth` 또는 `auth_secret` 중 하나 | 별칭 형식의 인증 문서이며, 이 형식은 [`--auth`](/command-line-usage/global-flags) 객체에 있는 프로바이더 항목과 다릅니다. AWS의 경우 `keyIDenvvar` 및 `credentialsenvvar` 키가 아니라 `access_key_id_env_var` 및 `secret_access_key_env_var` 키를 사용하며, 별칭 형식이 알지 못하는 키는 `invalid session configuration field or type` 오류로 실패합니다. 별칭은 정식 프로바이더의 자격 증명을 빌려 쓰지 않습니다. |
| `auth_secret` | `auth` 또는 `auth_secret` 중 하나 | 인증 문서를 보관하는 [세션 시크릿](#session-secrets)의 이름입니다. |
| `params` | 아니요 | 선언된 프로바이더 매개 변수(선언된 서버 매개 변수 포함)의 기본값입니다. 쿼리의 조건자가 기본값을 재정의합니다. |
| `tuning` | 아니요 | 별칭의 요청에 대한 `max_concurrent` 및 `max_per_host` 제한입니다. 프로바이더와 테넌시를 공유하는 별칭은 이 제어를 공유하며, 동일한 튜닝을 선언해야 합니다. |
<br/>
다음 예제는 두 AWS 계정을 별도의 별칭으로 바인딩하고, 하나의 문에서 두 계정을 모두 쿼리합니다. 각 별칭은 서로 다른 이름의 환경 변수에서 키를 읽습니다.

```sql
SET "$.providers.production" = '{
  "provider": "aws",
  "tenancy": "production-account",
  "auth": {
    "type": "aws_signing_v4",
    "access_key_id_env_var": "PRODUCTION_AWS_ACCESS_KEY_ID",
    "secret_access_key_env_var": "PRODUCTION_AWS_SECRET_ACCESS_KEY"
  },
  "params": {"region": "ap-southeast-2"},
  "tuning": {"max_concurrent": 4, "max_per_host": 4}
}';

SET "$.providers.development" = '{
  "provider": "aws",
  "tenancy": "development-account",
  "auth": {
    "type": "aws_signing_v4",
    "access_key_id_env_var": "DEVELOPMENT_AWS_ACCESS_KEY_ID",
    "secret_access_key_env_var": "DEVELOPMENT_AWS_SECRET_ACCESS_KEY"
  },
  "params": {"region": "ap-southeast-2"},
  "tuning": {"max_concurrent": 2}
}';

SELECT name, 'production' AS account FROM production.s3.buckets
UNION ALL
SELECT name, 'development' AS account FROM development.s3.buckets;
```

환경 변수 참조는 StackQL 프로세스 환경에서 확인되며, 세션 로컬 환경은 없습니다. 임시 AWS 자격 증명에는 세션 토큰 필드 또는 환경 변수 참조도 필요합니다.

메타데이터 문과 데이터 문에서 별칭은 해당 프로바이더처럼 동작합니다. `SHOW`, `DESCRIBE`, `SELECT`, `INSERT`, `UPDATE`, `DELETE`, `EXEC` 문은 모두 프로바이더 이름 대신 별칭을 허용합니다. 별칭은 네이티브 기본 제공 관계나 미리 보기 IaC 관계에는 적용되지 않습니다. 서로 다른 별칭을 사용하는 `UNION ALL` 분기는 순차적으로 실행됩니다.

각 쿼리는 별칭 자격 증명의 분리된 스냅샷을 만듭니다. 바인딩을 변경하면 이후 쿼리에 영향을 주며, 이미 실행 중인 쿼리에는 영향을 주지 않습니다. `UNSET "$.providers.<alias>"` 문은 바인딩을 제거합니다.

## 자격 증명 필드 재정의 {#overriding-credential-fields}

`SET "$.auth.<provider>.<field>"` 문은 세션에 대해 프로바이더 인증 컨텍스트의 필드 하나를 변경합니다. 필드 이름은 해당 프로바이더 `--auth` 항목의 키입니다.

```sql
SET "$.auth.github.password_var" = 'GITHUB_TOKEN_READONLY';
SELECT name FROM github.repos.repos WHERE org = 'stackql';
UNSET "$.auth.github.password_var";
```

별칭과 마찬가지로 변경 사항은 `SET` 문 이후에 시작된 쿼리에 적용됩니다. 인증이 설정 해제된 프로바이더나 사용 가능한 `auth` 값이 없는 별칭에 대한 쿼리는 요청을 보내기 전에 실패합니다.

## 세션 시크릿 {#session-secrets}

기본적으로 모든 세션에는 메모리 전용 시크릿 저장소가 있으며, 별도로 구성할 필요가 없습니다. `SET "$.secrets.<name>"` 문은 리터럴 문자열(일반적으로 JSON 인증 문서)을 저장하고, 프로바이더 별칭은 `auth_secret` 키로 이를 참조합니다.

```sql
SET "$.secrets.production" = '{"type":"aws_signing_v4","access_key_id_env_var":"PRODUCTION_AWS_ACCESS_KEY_ID","secret_access_key_env_var":"PRODUCTION_AWS_SECRET_ACCESS_KEY"}';
SET "$.providers.production" = '{"provider":"aws","tenancy":"production-account","auth_secret":"production","params":{"region":"ap-southeast-2"}}';

SELECT name FROM production.s3.buckets;

UNSET "$.secrets.production";
```

시크릿을 교체하면 이후 쿼리에 영향을 주며, 시크릿을 삭제하면 이후의 사용이 실패합니다. 새 세션은 비어 있는 새 저장소를 받으며, 세션이 끝나면 저장소가 해제됩니다. `UNSET "$.secret_store"` 문은 현재 저장소 바인딩을 폐기하고 빈 세션 메모리로 다시 시작합니다.

일반적인 자격 증명 사용이나 변수 또는 구성을 저장하는 `SET` 문은 시크릿을 SQL 백엔드에 쓰지 않습니다. 중요한 데이터를 일반 변수나 테이블에 넣는 것은 호출자의 책임입니다.

## 영구 OS 시크릿 저장소 {#persistent-os-secret-stores}

영구 저장소는 기본적으로 비활성화되어 있으며, 운영자가 `--allow-persistent-secrets` 플래그로 StackQL을 시작해야만 활성화할 수 있습니다. SQL로는 이를 허용할 수 없습니다. 플래그가 설정된 경우 세션은 다음과 같이 OS 저장소를 선택합니다.

```sql
SET "$.secret_store" = '{"backend": "os", "namespace": "stackql/provider-auth"}';
```

저장소는 macOS Keychain, Linux Secret Service(작동하는 세션 서비스가 있는 `secret-tool` 도구) 또는 Windows Credential Manager입니다. `os` 백엔드만 허용됩니다. 플래그가 설정되어 있어도 플랫 파일 저장소, 파일 경로 및 모든 암시적 파일 대체는 거부되며, OS 기능을 사용할 수 없으면 파일로 대체되지 않고 실패합니다.

OS 저장소에 쓴 값은 SQL 세션이 끝난 후에도 유지되며, `UNSET "$.secrets.<name>"` 문으로 명시적으로 삭제합니다. 저장소 바인딩을 초기화해도 이미 영구 저장된 값은 삭제되지 않습니다. 저장소에 대한 액세스는 StackQL 프로세스의 운영 체제 ID에 의해 제어됩니다. 네임스페이스는 사용자별 액세스 제어가 아니므로, 이 옵트인을 다중 사용자 보관소로 취급하지 마세요.

## SQL의 시크릿 값 {#secret-values-in-sql}

운영자가 `--allow-secret-dereference` 플래그로 StackQL을 시작하지 않으면 시크릿 값은 SQL로 역참조되지 않습니다. 이 플래그를 사용하면 `"$secrets.production"`, `"$secrets.production.credentials"`, `"$auth.github.password"`, `"$providers.production.auth.credentials"` 같은 참조가 식 안에서 확인됩니다.

```sql
SELECT upper("$secrets.production.credentials") AS credential;

/*
-- the same statement in a session started without --allow-secret-dereference
SELECT upper("$secrets.production.credentials") AS credential;
-- FAILS with secret variable dereferencing is disabled
*/
```

주석 처리된 사본에서 보듯이, 플래그 없이 시작하면 같은 문은 아무것도 평가되기 전에 거부됩니다. 이 플래그는 명시적으로 역참조된 값이 SQL 스테이징, 결과, 로그 및 영구 저장된 쿼리 아티팩트에 들어갈 수 있음을 인정한다는 의미입니다. 일반적인 프로바이더 인증에는 이 플래그가 필요하지 않습니다.

## 권한 {#permissions}

[액세스 관리](/command-line-usage/access-management)가 활성화되어 있으면 변수와 시크릿을 읽는 데 `session/variables:read` 권한이 필요하고, `SET` 또는 `UNSET` 문에는 `session/config:write` 권한이 필요합니다. 확인된 별칭은 실행 전에 해당 정식 프로바이더로서 권한 부여를 받습니다. 변수별 및 시크릿별 소유권과 권한 부여는 구현되어 있지 않습니다.
