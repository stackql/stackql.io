---
title: 액세스 관리
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - 액세스 관리
  - 인증
  - 권한 부여
  - jwt
  - mtls
  - Bearer 토큰
description: "StackQL의 옵트인 호출자 액세스 관리: JWT, Bearer 토큰, 상호 TLS 또는 로컬 OS ID로 호출자를 인증하고, 디렉터리의 보안 주체와 그룹에 매핑하고, 정책 규칙에 따라 문에 권한을 부여합니다."
image: "/img/stackql-featured-image.png"
---

호출자 액세스 관리는 StackQL 세션의 호출자를 인증하고 정책에 따라 각 문에 권한을 부여합니다. 이 기능은 프로바이더 자격 증명과는 별개이며, 프로바이더 자격 증명은 계속해서 `--auth` 플래그, 환경 변수 또는 [프로바이더 세션](/getting-started/provider-sessions)을 통해 제공됩니다. 기본적으로 비활성화되어 있으며, 이 기능을 활성화하지 않은 배포는 이전과 동일하게 동작합니다. StackQL `v1.0` 이상에서 사용할 수 있습니다.

참고 항목:  
[[ 전역 플래그 ]](/command-line-usage/global-flags) [[` mcp `]](/command-line-usage/mcp) [[` whoami `]](/language-spec/functions/session/whoami) [[` groups `]](/language-spec/functions/session/groups)

* * * 

## 액세스 관리 활성화 {#enabling-access-management}

액세스 관리는 JSON 구성 파일을 가리키는 `--access.config` 전역 플래그와 함께 StackQL을 시작하면 활성화됩니다. 다음 예제는 보호 모드에서 대화형 셸을 시작합니다. `exec` 및 `mcp` 명령도 같은 방식으로 활성화합니다.

```bash
stackql --access.config '{"path": "/etc/stackql/access.json"}' shell
```

`--access.config` 플래그는 단일 `path` 키가 있는 JSON 엔벨로프를 받습니다. 파일 경로만 지정해도 됩니다. 구성은 시작 시 한 번 로드되므로, 키나 정책을 교체하려면 통제된 방식으로 다시 시작하세요.

액세스 관리가 활성화되어 있는 동안에는 `exec`, `shell`, `mcp`, `version` 명령만 실행됩니다. `registry` 명령을 비롯한 다른 명령은 보호 경로를 우회하지 않고 실행을 거부합니다. `srv` 명령과 MCP `reverse_proxy` 서버 유형도 실행을 거부합니다. PostgreSQL 와이어 종속성이 아직 연결별 인증 증거를 노출하지 않기 때문입니다. SQL `AUTH` 및 `AUTH REVOKE` 문은 보호 모드에서 거부됩니다. 대신 호스트 구성을 통해 프로바이더 자격 증명을 프로비저닝하세요. 보호된 `exec` 명령이 실패하면 오류를 표시한 후 종료 상태 `1`을 반환합니다.

## 인증 모드 {#authentication-modes}

구성 파일은 `type` 키로 `jwt`(생략 시 기본값), `bearer`, `mtls`, `local_os` 중 하나를 선택합니다. 구성은 엄격한 JSON입니다. 알 수 없는 필드와 다른 모드에 속하는 필드는 거부됩니다. 모든 모드는 동일한 `rules` 키와 동일한 [보안 주체 디렉터리](#principal-directory)를 공유합니다.

### JWT {#jwt}

호출자는 서명된 토큰을 제시하며, 이 토큰은 구성에 고정된 키로 오프라인에서 검증됩니다. 최소 구성은 토큰에 포함되어야 하는 발급자와 대상을 지정하고, 공개 키 하나를 나열하고, `readers` 그룹에 읽기 액세스 권한을 부여합니다.

```json
{
  "type": "jwt",
  "issuer": "https://identity.example",
  "audience": "stackql",
  "keys": [
    {"kid": "primary", "algorithm": "EdDSA", "public_key_pem": "-----BEGIN PUBLIC KEY-----\n...\n-----END PUBLIC KEY-----\n"}
  ],
  "rules": [
    {"subject": "group:readers", "resource": "host/sql/read", "action": "execute"}
  ]
}
```

발급자, 대상 및 공개 키는 구성에 고정되며, OIDC 디스커버리는 없습니다. 허용되는 알고리즘은 `EdDSA`, `RS256`, `RS384`, `RS512`, `PS256`, `PS384`, `PS512`, `ES256`, `ES384`, `ES512`이며, RSA 키는 2048비트 이상이어야 합니다. 각 키는 구성된 알고리즘에 바인딩됩니다. 알 수 없는 키 ID, 잘못된 서명, 누락되었거나 지난 만료 시간, 미래의 `nbf` 또는 `iat`, 잘못된 발급자 또는 대상이 있는 토큰은 거부됩니다. 토큰에 포함된 그룹 클레임과 URL은 신뢰나 디렉터리 멤버십에 사용되지 않습니다.

### 정적 Bearer 토큰 {#static-bearer-token}

호출자는 불투명 토큰을 제시하고, 구성은 허용되는 각 토큰의 SHA-256 다이제스트와 해당 토큰이 식별하는 주체를 나열합니다. 이 방식은 토큰 발급자를 운영할 필요가 없는 소수의 고정된 서비스 ID 집합에 적합합니다.

```json
{
  "type": "bearer",
  "issuer": "local-tokens",
  "tokens": [{"subject": "alice", "sha256": "<64 hexadecimal characters: the SHA-256 of the token>"}],
  "rules": [{"subject": "group:readers", "resource": "host/sql/read", "action": "execute"}]
}
```

각 토큰의 SHA-256 다이제스트만 구성되며, 다이제스트는 고유해야 합니다. 구성된 다이제스트를 변경하려면 다시 시작해야 합니다.

### 상호 TLS {#mutual-tls}

호출자는 HTTPS MCP 전송 방식에서 제시하는 클라이언트 인증서로 식별됩니다. 구성에는 인증서를 발급해야 하는 CA가 포함되며, 어떤 주체 대체 이름에 ID가 들어 있는지 지정합니다.

```json
{
  "type": "mtls",
  "issuer": "client-certificates",
  "client_ca_pem": "-----BEGIN CERTIFICATE-----\n...\n-----END CERTIFICATE-----\n",
  "certificate_identity": "uri_san",
  "rules": [{"subject": "group:readers", "resource": "host/sql/read", "action": "execute"}]
}
```

클라이언트 인증서 체인은 `client_ca_pem` 값을 기준으로 검증되며, ID 주체는 구성된 종류(`uri_san` 또는 `dns_san`)의 SAN 정확히 하나에서 가져옵니다. Common Name으로 대체되지 않습니다. 상호 TLS는 HTTPS MCP 전송 방식에서만 사용할 수 있습니다.

### 로컬 OS ID {#local-os-identity}

자격 증명은 교환되지 않습니다. 주체는 StackQL 프로세스를 실행하는 운영 체제 ID입니다. 구성에는 `issuer` 레이블과 규칙만 있으면 됩니다.

```json
{
  "type": "local_os",
  "issuer": "this-host",
  "rules": [{"subject": "group:readers", "resource": "host/sql/read", "action": "execute"}]
}
```

ID 주체는 StackQL 프로세스의 유효 Unix UID 또는 Windows SID이며, passwd 항목이 없는 숫자 컨테이너 UID도 포함됩니다. 자격 증명은 제시되지 않으며 `STACKQL_CALLER_TOKEN` 환경 변수는 무시됩니다. 이 모드는 로컬 전용이며, HTTP 호출자에게는 거부됩니다. 운영 체제 그룹 멤버십은 액세스 권한을 부여하지 않으며, 디렉터리 멤버십만 액세스 권한을 부여합니다.

### 세션 기한 {#session-deadline}

기본적으로 쿼리에는 인증에서 파생된 기한이 없습니다. 새 문마다 자격 증명이 검증되고 디렉터리 멤버십이 새로 고쳐지므로, 만료된 자격 증명으로는 새 작업을 시작할 수 없습니다. 하지만 만료되어도 이미 권한이 부여된 문은 중단되지 않습니다. `max_session_seconds`(기본값 `300`, 최대 `86400`)에 도달하면 취소되도록 하려면 `"enforce_session_deadline": true`를 설정하세요. 이 시간은 JWT 또는 인증서 만료에 따라 단축됩니다. 이 설정은 토큰을 갱신하지 않습니다.

## 보안 주체 디렉터리 {#principal-directory}

ID는 보안 주체에, 보안 주체는 그룹에 매핑되며, 이 매핑은 구성된 SQLite 또는 PostgreSQL 백엔드의 테이블 4개에 저장됩니다.

| 테이블 | 내용 |
|--|--|
| `stackql_access_principals` | 보안 주체 ID 및 문자열 속성으로 이루어진 JSON 객체. `tenant` 속성은 미리 보기 IaC 상태의 네임스페이스를 구분합니다. |
| `stackql_access_identities` | 검증된 `(issuer, subject)` 쌍과 보안 주체 ID의 매핑. |
| `stackql_access_groups` | 그룹 ID. |
| `stackql_access_memberships` | 보안 주체와 그룹 간 바인딩. |

보호된 쿼리를 제공하기 전에 신뢰할 수 있는 데이터베이스 관리 연결을 통해 테이블, ID 및 멤버십을 프로비저닝하세요.

```sql
INSERT INTO stackql_access_principals (id, attributes) VALUES ('alice', '{}');
INSERT INTO stackql_access_groups (id) VALUES ('readers');
INSERT INTO stackql_access_identities (issuer, subject, principal_id)
VALUES ('https://identity.example', 'alice', 'alice');
INSERT INTO stackql_access_memberships (principal_id, group_id) VALUES ('alice', 'readers');
```

해당 모드의 실제 발급자와 주체를 사용하세요. 예를 들어 로컬 OS ID의 경우 `('this-host', '1000')`, 상호 TLS의 경우 `('client-certificates', 'spiffe://example/alice')`일 수 있습니다. 백엔드를 명시적으로 선택하세요.

```bash
stackql --access.config '{"path":"/etc/stackql/access.json"}' \
  --sqlBackend '{"dbEngine":"postgres_tcp","dsn":"postgres://runtime@localhost/stackql?sslmode=require","sqlDialect":"postgres"}' \
  exec 'SELECT whoami(), groups();'
```

디렉터리는 문마다 변경할 수 없는 스냅샷을 생성합니다. 해지는 관리 연결에서 `DELETE FROM stackql_access_memberships` 문을 실행하는 방식이며, 다음 문부터 새 스냅샷이 적용됩니다. 알 수 없는 ID, 스키마 부재, 잘못된 형식의 데이터, 디렉터리 중단은 모두 액세스 거부로 이어집니다. 일반적인 보호된 SQL로는 디렉터리 테이블을 참조할 수 없습니다. 멤버십 쓰기는 관리 연결에서만 수행하고, 데이터베이스 파일을 보호하고, 백엔드가 지원하는 경우 관리용과 런타임용 데이터베이스 권한을 분리하여 사용하세요.

## 정책 규칙 {#policy-rules}

각 규칙은 `group:<group id>` 형식의 `subject`에 하나의 `resource`에 대한 하나의 `action`을 부여합니다. 신뢰할 수 있는 프로바이더 문서의 컴파일된 메서드 및 작업 식별자를 사용하세요. 작업 와일드카드는 읽기뿐 아니라 변경 메서드도 허용합니다.

`readers` 그룹은 호스트의 안전한 읽기 SQL 기능을 암시적으로 보유합니다. 이 그룹에 속하지 않는 보안 주체가 읽으려면 `host/sql/read` 권한이 필요하고, 그 밖의 문에는 `host/sql/<statement kind>`에 대한 `execute` 권한이 필요합니다. 프로바이더 작업과 자격 증명 사용에는 항상 명시적 규칙이 필요하며, 컬렉션에 대한 권한 부여가 그 하위 메서드에 대한 권한을 부여하지는 않습니다. 추가 호스트 검사는 다음과 같습니다.

| 리소스 | 작업 | 범위 |
|--|--|--|
| `host/sql/read` | `execute` | 암시적 읽기 그룹 외부의 읽기 문 |
| `host/sql/exec` | `execute` | 중첩된 `EXEC` 문을 포함한 네이티브 `EXEC` 문 |
| `host/iac/run`, `host/iac/destroy` | `execute` | 미리 보기 IaC 관계(`SELECT` 구문을 사용하더라도 포함) |
| `host/function/<name>` | `execute` | 알려진 순수 함수로 구성된 작은 허용 목록에 없는 함수 |
| `host/backend` | `read` | 불투명한 저장 및 외부 백엔드 관계 |
| `host/credentials` | `reload` | MCP `reload_credentials` 도구 |
| `session/variables` | `read` | [세션 변수](/getting-started/session-variables) 및 시크릿 역참조 |
| `session/config` | `write` | `SET` 및 `UNSET` 문 |

저장된 쿼리의 확장은 실행 전에 검사되며, 보호된 경로에서는 계획 캐시 및 행 캐시의 재사용이 비활성화됩니다.

## 진입점 {#entry-points}

| 인증 | `exec`, `shell`, MCP stdio | HTTPS MCP |
|--|--|--|
| JWT | `STACKQL_CALLER_TOKEN` 환경 변수 | `Authorization: Bearer` 요청 헤더 |
| 정적 Bearer | `STACKQL_CALLER_TOKEN` 환경 변수 | `Authorization: Bearer` 요청 헤더 |
| 로컬 OS | 유효 프로세스 UID 또는 SID; 자격 증명 없음 | 거부됨 |
| 상호 TLS | 거부됨: 검증된 TLS 연결 없음 | 검증된 클라이언트 체인 및 구성된 SAN 1개 |

로컬 토큰 자격 증명은 SQL 리터럴이나 명령줄 인수가 아니라 프로세스 환경을 통해 제공하세요. HTTPS에서는 호출자 검증기가 [HTTP 클라이언트 인증](/command-line-usage/mcp#http-client-authentication)에서 설명하는 공유 `auth_token_env_var` Bearer 게이트를 대신합니다. 서버 TLS 인증서 및 키 구성은 필수이며, 자격 증명이 없는 요청은 서버의 환경 토큰으로 대체되지 않습니다.

## 호출자 확인 {#inspecting-the-caller}

인증된 세션에서 `whoami()` 함수는 보안 주체 ID를 반환하고 `groups()` 함수는 디렉터리 그룹을 JSON 배열로 반환합니다.

```sql
SELECT whoami() AS principal, groups() AS memberships;
```

```
|-----------|-------------|
| principal | memberships |
|-----------|-------------|
| alice     | ["readers"] |
|-----------|-------------|
```

두 함수 모두 `--access.config` 플래그로 시작한 세션이 필요합니다.

## 실습 {#walkthroughs}

StackQL 리포지토리에는 인증 모드별로 하나씩, [실행 가능한 실습 4개](https://github.com/stackql/stackql/blob/main/docs/examples/access-management/README.md)가 포함되어 있습니다. 각 실습은 데모 디렉터리를 프로비저닝하고 쿼리를 검증하는 Python 스크립트로 구동됩니다. 클라우드 계정이 필요하지 않으며 프로젝트의 테스트 스위트에서 실행됩니다.
