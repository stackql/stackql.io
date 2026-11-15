---
title: mcp
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "AI 에이전트용 Model Context Protocol 서버로 StackQL을 실행하는 stackql mcp 명령: 배포 모드, 안전 모드, 자격 증명 다시 로드, 감사 로그."
image: "/img/stackql-featured-image.png"
---

StackQL을 Model Context Protocol(MCP) 서버로 시작하는 명령입니다. AI 에이전트와 어시스턴트가 StackQL의 쿼리 기능을 사용하여 클라우드 인프라와 상호 작용할 수 있습니다.

:::info[MCP란?]

[Model Context Protocol (MCP)](https://modelcontextprotocol.io/)는 AI 애플리케이션이 외부 데이터 원본 및 도구와 상호 작용하는 방식을 표준화하는 개방형 프로토콜입니다. StackQL을 MCP 서버로 실행하면 AI 에이전트(Claude, Cursor, Continue, 사용자 지정 에이전트 등)가 자연어를 사용하여 여러 프로바이더에 걸친 클라우드 인프라를 쿼리하고 관리할 수 있습니다.

:::

* * *

### 구문 {#syntax}

독립 실행형 MCP 서버에는 `stackql mcp [flags]` 명령을 사용합니다.

MCP 서버를 PostgreSQL 와이어 프로토콜 서버와 같은 프로세스에서 함께 실행하려면 `stackql srv [flags]` 명령을 사용합니다.

* * *

### 배포 모드 {#deployment-modes}

StackQL의 MCP 서버는 다양한 아키텍처 요구 사항에 맞게 3가지 구성으로 배포할 수 있습니다. 아래 예제는 `"allow_unauthenticated": true` 설정으로 인증되지 않은 서비스에 옵트인하며, 이는 로컬 개발에 적합합니다. 다른 프로세스나 호스트가 접근할 수 있는 서버에는 대신 Bearer 토큰을 구성하세요. [HTTP 클라이언트 인증](#http-client-authentication)을 참조하세요.

#### 1. 독립 실행형 MCP 서버 {#1-standalone-mcp-server}

지정한 포트에서 StackQL을 전용 MCP 서버로 실행합니다.

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true}}'
```

**사용 사례:** MCP 프로토콜 액세스만 필요하고 PostgreSQL 와이어 프로토콜 호환성은 필요하지 않은 경우입니다.

#### 2. MCP + PostgreSQL 서버(인메모리) {#2-mcp--postgresql-server-in-memory}

MCP 서버와 PostgreSQL 서버를 동시에 실행하며, 두 서버는 인메모리 방식으로 통신합니다.

```bash
stackql srv \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true}}' \
  --pgsrv.port 5665
```

**사용 사례:** AI 에이전트용 MCP 프로토콜 액세스와 기존 데이터베이스 클라이언트용 PostgreSQL 와이어 프로토콜이 모두 필요하고, 인메모리 통신으로 최대 성능을 얻으려는 경우입니다.

#### 3. MCP + PostgreSQL 서버(역방향 프록시) {#3-mcp--postgresql-server-reverse-proxy}

TCP 기반 통신으로 두 서버를 실행합니다. 분산 배포와 TLS 암호화를 지원합니다.

```bash
stackql srv \
  --mcp.server.type=reverse_proxy \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9004", "allow_unauthenticated": true}, "backend": {"dsn": "postgres://stackql:stackql@127.0.0.1:5446?default_query_exec_mode=simple_protocol"}}' \
  --pgsrv.port 5446
```

**TLS 암호화 사용:**

```bash
stackql srv \
  --mcp.server.type=reverse_proxy \
  --mcp.config '{"server": {"tls_cert_file": "/path/to/server_cert.pem", "tls_key_file": "/path/to/server_key.pem", "transport": "http", "address": "127.0.0.1:9004", "auth_token_env_var": "STACKQL_MCP_TOKEN"}, "backend": {"dsn": "postgres://stackql:stackql@127.0.0.1:5446?default_query_exec_mode=simple_protocol"}}' \
  --pgsrv.port 5446
```

TLS는 통신을 암호화하지만 클라이언트를 인증하지는 않으므로, 이 예제에서는 `STACKQL_MCP_TOKEN` 환경 변수에 저장된 Bearer 토큰도 요구합니다.

**사용 사례:** MCP 워크로드와 PostgreSQL 워크로드를 서로 다른 프로세스나 호스트로 분리해야 하거나, MCP 엔드포인트에 TLS 암호화가 필요한 경우입니다.

* * *

### 구성 옵션 {#configuration-options}

#### MCP 서버 유형 {#mcp-server-type}

| 유형 | 설명 |
|--|--|
|`http`|직접 HTTP 서버 모드입니다. MCP 요청은 StackQL 엔진이 프로세스 내에서 처리합니다.|
|`reverse_proxy`|역방향 프록시 모드입니다. MCP 요청은 구성된 DSN을 통해 백엔드 PostgreSQL 서버로 전달됩니다. `stackql srv` 명령과 함께 사용합니다.|
|`stdio`|표준 입력/출력 전송 방식입니다. 서버는 `stdin`에서 요청을 읽고 `stdout`에 응답을 씁니다. 편집기에 내장된 MCP 클라이언트(Claude Desktop, Cursor 등)에서 사용합니다.|

#### MCP 로그 형식 {#mcp-log-format}

`--mcp.log.format` 플래그는 감사 로그 인코딩을 선택합니다. 인코딩은 `jsonl`(기본값) 또는 `otel`(OpenTelemetry OTLP/JSON 로그 레코드) 중 하나입니다. 이 플래그는 구성 객체의 `server.audit.format` 설정을 재정의합니다. [로그 형식](#log-format)을 참조하세요.

#### MCP 프로토콜 버전 {#mcp-protocol-version}

`--mcp.protocol.version` 플래그는 서버가 알리는 최신 프로토콜 리비전을 설정합니다. 값은 `auto`(기본값, 지원되는 모든 리비전), `2026-07-28`(세션 없는 방식만) 또는 `2025-11-25` 같은 이전 리비전(해당 리비전과 그 이전 리비전)입니다. 이 플래그는 구성 객체의 `server.protocol_version` 설정을 재정의합니다. [리비전 고정](#pinning-the-revision)을 참조하세요.

#### MCP 구성 객체 {#mcp-configuration-object}

`--mcp.config` 플래그는 다음 구조의 JSON 객체를 받습니다.

##### 서버 구성 {#server-configuration}

| 필드 | 설명 | 필수 |
|--|--|--|
|`server.transport`|전송 프로토콜: `http` 또는 `stdio`.|예|
|`server.address`|MCP 서버를 바인딩할 주소와 포트입니다(예: `127.0.0.1:9912`). HTTP 전송 방식 전용입니다.|`http`인 경우|
|`server.tls_cert_file`|HTTPS용 TLS 인증서 파일의 경로입니다.|아니요|
|`server.tls_key_file`|HTTPS용 TLS 개인 키 파일의 경로입니다.|아니요|
|`server.auth_token_env_var`|사전 공유 Bearer 토큰을 저장한 환경 변수의 이름입니다. 설정하면 모든 HTTP 요청에 `Authorization: Bearer <token>` 헤더가 있어야 합니다. 변수가 설정되지 않았거나 비어 있으면 서버가 시작되지 않습니다. [HTTP 클라이언트 인증](#http-client-authentication)을 참조하세요.|`http`인 경우 이 필드 또는 `allow_unauthenticated` 필드 중 하나|
|`server.allow_unauthenticated`|클라이언트 인증 없이 HTTP 전송 방식을 제공하기 위한 명시적 옵트인입니다. 기본값은 `false`입니다.|`http`인 경우 이 필드 또는 `auth_token_env_var` 필드 중 하나|
|`server.mode`|변경 작업과 수명 주기 작업을 게이트 제어하는 안전 계약입니다. `read_only`, `safe`(기본값), `delete_safe`, `full_access` 중 하나입니다. [서버 모드](#server-modes)를 참조하세요.|아니요|
|`server.read_only`|`mode: read_only`의 **레거시** 부울 별칭입니다. 두 필드가 모두 설정되면 `mode` 필드가 우선합니다.|아니요|
|`server.stateless`|세션 없이(`Mcp-Session-Id` 없이) Streamable HTTP를 제공합니다. HTTP에서는 이 방식으로 프로토콜 리비전 `2026-07-28`을 제공합니다. 기본값은 `false`입니다. `stdio`에서는 무시됩니다. [프로토콜 리비전 지원](#protocol-revision-support)을 참조하세요.|아니요|
|`server.protocol_version`|알리는 최신 프로토콜 리비전: `auto`(기본값), `2026-07-28` 또는 `2025-11-25` 같은 이전 리비전. `2026-07-28` 리비전을 지정하면 Streamable HTTP에서 `stateless` 설정이 암시적으로 적용됩니다. [리비전 고정](#pinning-the-revision)을 참조하세요.|아니요|
|`server.audit`|감사 하위 시스템 구성입니다. `audit.format` 키(`jsonl` 또는 `otel`)를 포함합니다. [감사 로그](#audit-log)를 참조하세요.|아니요|

##### 백엔드 구성(역방향 프록시 모드 전용) {#backend-configuration-reverse-proxy-mode-only}

| 필드 | 설명 | 필수 |
|--|--|--|
|`backend.dsn`|백엔드 StackQL 서버의 PostgreSQL 연결 문자열입니다.|예(`reverse_proxy`인 경우)|

<br />

:::info

최적의 호환성을 위해 백엔드 DSN에 `default_query_exec_mode=simple_protocol` 매개 변수를 포함하는 것이 좋습니다.

:::

##### 허용 목록 {#allowlists}

| 필드 | 설명 |
|--|--|
|`enabled_tools`|서버가 게시할 수 있는 도구 이름의 배열입니다. 생략하거나 비어 있으면 모든 기본 제공 도구가 등록됩니다. 좁은 범위의 하위 집합(예: 읽기 전용 인벤토리 서버)을 노출할 때 사용합니다.|
|`enabled_prompts`|프롬프트 이름의 배열입니다. `enabled_tools` 필드와 같은 방식으로 동작하지만 프롬프트에 적용됩니다.|

<br/>

다음 예제는 `server_info` 도구와 `list_providers` 도구만 게시하는 서버입니다:

```json
{
  "server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true},
  "enabled_tools": ["server_info", "list_providers"]
}
```

* * *

### 프로토콜 리비전 지원 {#protocol-revision-support}

프로토콜 리비전 `2026-07-28`, `server.stateless` 필드, `otel` 감사 로그 형식은 StackQL `v0.11.660` 이상에서 사용할 수 있습니다. `server.protocol_version` 필드와 `--mcp.protocol.version` 플래그는 StackQL `v0.12.732` 이상에서 사용할 수 있습니다.

서버는 [Go MCP SDK](https://github.com/modelcontextprotocol/go-sdk)가 지원하는 Model Context Protocol의 모든 리비전을 지원하며 클라이언트별로 협상합니다. 따라서 여러 종류의 클라이언트가 섞여 있어도 하나의 서버로 작동합니다.

| 리비전 | 수명 주기 | 승인 요청(`safe` / `delete_safe`) |
|--|--|--|
|`2026-07-28`(알림 대상)|핸드셰이크 없음: `server/discover` 요청 후, 모든 요청이 `_meta`에 `io.modelcontextprotocol/protocolVersion` 키와 `io.modelcontextprotocol/clientCapabilities` 키를 포함합니다(HTTP에서는 `Mcp-Protocol-Version`, `Mcp-Method`, `Mcp-Name` 헤더도 포함).|`input_required` 결과이며, 클라이언트가 `inputResponses` 필드와 함께 다시 시도합니다(SEP-2322).|
|`2025-11-25`, `2025-06-18`|`initialize` / `notifications/initialized` 핸드셰이크.|서버가 시작하는 `elicitation/create` 요청.|
|`2025-03-26`, `2024-11-05`|위와 같음.|위와 같음.|

<br />

**stdio**는 하나의 프로세스에서 모든 리비전을 제공합니다. `2026-07-28` 클라이언트의 첫 요청은 핸드셰이크 없이 처리되고, 이전 클라이언트의 `initialize` 요청도 계속 작동합니다. Claude Desktop, npm / PyPI 런처, Docker 이미지에서는 따로 구성할 것이 없습니다.

**Streamable HTTP**는 기본적으로 클라이언트마다 세션을 두는 상태 저장 모델(`Mcp-Session-Id`)을 사용하며, SDK는 `2025-11-25`까지의 리비전에 이 모델을 제공합니다. `2026-07-28` 요청에는 JSON-RPC 오류 `-32022`(지원되지 않는 프로토콜 버전, `v0.12.718`까지의 릴리스에서는 일반 HTTP 400)로 응답하며, 이 오류의 `data.supported` 필드에는 핸드셰이크 리비전이 나열됩니다. `server/discover` 요청도 같은 리비전을 알립니다. 따라서 클라이언트는 낮은 리비전으로 협상하고, 기존 HTTP 통합은 세션과 승인 요청을 그대로 유지합니다. HTTP에서 `2026-07-28` 리비전을 네이티브로 제공하려면 `server.stateless` 필드를 설정합니다(또는 [아래](#pinning-the-revision)와 같이 리비전을 `2026-07-28`로 고정합니다):

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true, "stateless": true}}'
```

세션 없는 서버는 `Mcp-Session-Id` 헤더를 발급하지 않습니다. `GET` 요청과 `DELETE` 요청에는 405로 응답합니다(해제할 세션이 없음). `tools/list`, `prompts/list`, `resources/list` 결과는 연결과 관계없이 동일하게 유지하며, 승인 왕복은 `input_required` 결과를 통해 처리합니다. 이전 클라이언트의 `initialize` 요청은 여전히 받아들이고 읽기 요청도 처리합니다. 하지만 해당 클라이언트가 초기화 시 선언한 엘리시테이션(elicitation) 기능은 유지할 수 없습니다(각 요청이 임시 세션을 받기 때문입니다). 따라서 이전 클라이언트는 세션 없는 서버에서 승인이 필요한 쓰기를 승인할 수 없습니다. 현재 리비전을 사용하는 호스트에는 `stateless` 설정을, 이전 클라이언트가 아직 포함된 환경에는 기본 설정을 선택하세요.

서버는 호출 간 상태를 보유하지 않습니다. 모드, 감사, 프로바이더 자격 증명은 프로세스 수준 구성이므로 명시적 핸들 뒤로 옮겨야 할 것이 없습니다.

#### 리비전 고정 {#pinning-the-revision}

기본적으로 서버는 지원되는 모든 리비전을 알리고 클라이언트별로 협상합니다. `--mcp.protocol.version` 플래그 또는 구성 객체의 `server.protocol_version` 필드(플래그가 우선함)로 알릴 최신 리비전을 설정합니다:

```bash
# sessionless only; stateless Streamable HTTP is implied
stackql mcp \
  --mcp.server.type=http \
  --mcp.protocol.version=2026-07-28 \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true}}'

# handshake lifecycle only
stackql mcp --mcp.server.type=stdio --mcp.protocol.version=2025-11-25
```

| 값 | 알리는 리비전 | 효과 |
|--|--|--|
|`auto`(기본값) 또는 생략|지원되는 모든 리비전.|위와 같은 협상: 양쪽이 모두 지원하는 가장 높은 리비전.|
|`2026-07-28`|`2026-07-28`만.|세션 없는 방식만 사용하며, Streamable HTTP에서는 `stateless` 설정이 암시적으로 적용됩니다. 이전 클라이언트의 `initialize` 요청에는 `2025-11-25`로 응답합니다. 이는 해당 클라이언트가 응답을 새 수명 주기로 해석하지 않고 연결을 끊도록 하는 신호입니다.|
|`2025-11-25`(또는 그 이전의 모든 리비전)|해당 리비전과 그 이전의 모든 리비전.|핸드셰이크 수명 주기만 사용합니다. `2026-07-28` 요청은 JSON-RPC 오류 `-32022`를 받으며, `data.supported` 필드에 알리는 리비전이 나열되므로 클라이언트가 다시 협상할 수 있습니다. 이전 핸드셰이크 클라이언트는 자체 리비전을 유지합니다.|

<br />

그 밖의 값은 시작 시 구성 유효성 검사에 실패하며, 오류 메시지에 허용되는 값이 표시됩니다.

#### 전송 방식 제한 {#transport-limits}

Streamable HTTP 요청 본문은 4MiB로 제한되며(초과 시 HTTP 413), 1000단계보다 깊게 중첩된 JSON은 구문 분석 전에 거부되고, 단일 `stdio` 프레임은 8MiB로 제한됩니다. 이 제한은 구성할 수 없으며, 모두 서버가 받아들이는 어떤 SQL 문보다도 훨씬 큽니다.

* * *

### 자격 증명 로드 및 다시 로드(`--env.file` + `reload_credentials`) {#credential-resourcing---envfile--reload_credentials}

:::note

`--env.file` 플래그와 `reload_credentials` 도구는 StackQL `v0.10.542` 이후 릴리스에서 사용할 수 있습니다.

:::

프로세스 환경 블록은 생성 시점에 고정됩니다. 이로 인해 MCP 서버에 2가지 문제가 생깁니다:

- 자격 증명 환경 변수 없이 데스크톱 클라이언트(Claude Desktop, Cursor 등)가 생성한 MCP `stdio` 서버는 해당 변수를 볼 수 없습니다. 클라이언트가 자체 환경을 하위 프로세스에 전달하기 때문입니다.
- 교체된 단기 자격 증명이 장기 실행 서버 세션에 전달되지 않습니다.

`--env.file` 플래그는 변경 가능한 자격 증명 저장소 역할을 하는 dotenv 형식 파일을 지정하여 두 문제를 모두 해결합니다:

```bash
stackql mcp --mcp.server.type=stdio --env.file /path/to/stackql-credentials.env
```

#### 로드 동작 방식 {#sourcing-semantics}

파일은 모든 진입점(`exec`, `shell`, `srv`, `registry`, `mcp`)에서 시작 시 프로세스 환경으로 로드됩니다:

- 플래그가 비어 있거나 설정되지 않은 경우 아무 작업도 수행하지 않습니다. 자격 증명은 요청 시점에 프로세스 환경에서 지연 방식으로 확인되며, 이는 기존 동작과 정확히 같습니다.
- 지정된 경로에 파일이 없으면 오류 없이 무시됩니다(파일은 나중에 생성될 수 있음).
- 형식이 잘못되었거나 읽을 수 없는 파일은 시작 시 치명적 오류가 됩니다.
- 값이 비어 있지 않은 키만 설정합니다. 기존 프로세스 환경 변수는 **덮어씁니다**(파일이 단일 정보 출처입니다). 변수를 해제하는 일은 없습니다.
- 이 플래그는 명령줄 전용이며, `.stackqlrc` 파일에서 로드되지 않습니다.

파일 형식은 한 줄에 하나의 `KEY=VALUE` 쌍입니다. `#` 주석, 빈 줄, 선택적 `export ` 접두사, 값을 감싸는 선택적 작은따옴표 또는 큰따옴표, CRLF 줄 끝을 모두 허용합니다:

```bash
# stackql-credentials.env
export OKTA_SECRET_KEY="00abc...xyz"
STACKQL_GITHUB_USERNAME=myuser
STACKQL_GITHUB_PASSWORD='ghp_mytoken'
```

#### `reload_credentials` {#reload_credentials}

[`reload_credentials`](/mcp/reload_credentials) 도구는 세션 도중에 `--env.file` 파일을 프로세스 환경으로 다시 로드한 다음, 프로바이더별 자격 증명 확인 상태를 보고합니다. 시크릿 값은 반환하거나 로그에 기록하거나 감사하지 않으며, 이름과 상태만 다룹니다. `--env.file` 플래그가 구성되지 않은 경우 이 도구는 상태 확인 기능만 수행합니다.

쿼리가 자격 증명 확인에 실패하면 MCP 오류에 에이전트가 `reload_credentials` 도구를 호출하고 다시 시도하도록 안내하는 힌트가 포함됩니다. 따라서 에이전트형 클라이언트는 자가 복구됩니다. 자격 증명을 파일에 쓰고 에이전트에 다시 로드를 요청하면 실패한 쿼리가 재시도 시 성공합니다. 서버를 다시 시작할 필요가 없습니다.

`reload_credentials` 도구는 `reverse_proxy` 백엔드 모드에서 지원되지 않습니다. 쿼리가 원격 stackql 서버 프로세스에서 실행되므로, 이 도구는 백엔드 서버에서 다시 로드하도록 사용자에게 안내하는 오류를 반환합니다.

#### Windows 및 Claude Desktop {#windows-and-claude-desktop}

`setx` 명령은 실행 중인 프로세스가 아니라 레지스트리에 기록하며, Claude Desktop은 자체(오래된) 환경을 MCP 하위 프로세스에 전달합니다. 따라서 자격 증명을 변경하려면 일반적으로 Claude Desktop을 완전히 다시 시작해야 합니다. `--env.file` 플래그를 사용하면 자격 증명 파일을 언제든지 생성하거나 교체할 수 있으며, `reload_credentials` 도구가 다시 시작 없이 변경 사항을 반영합니다. 구성 예제는 [Claude Desktop에서 StackQL 사용](/getting-started/claude-desktop)을 참조하세요.

* * *

### HTTP 클라이언트 인증 {#http-client-authentication}

:::note

`server.auth_token_env_var` 필드와 `server.allow_unauthenticated` 필드는 StackQL `v0.12.742` 이상에서 사용할 수 있습니다.

:::

HTTP 전송 방식에는 클라이언트 인증이 필요합니다. `server.auth_token_env_var` 필드는 사전 공유 Bearer 토큰을 저장한 환경 변수의 이름을 지정합니다. 모든 요청은 `Authorization: Bearer <token>` 헤더를 포함해야 하며, 그렇지 않으면 `401`로 응답합니다. 토큰은 환경에서 읽으므로 `--mcp.config` 값이나 프로세스 인수에 나타나지 않습니다.

```bash
export STACKQL_MCP_TOKEN="$(openssl rand -hex 32)"

stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "auth_token_env_var": "STACKQL_MCP_TOKEN"}}'
```

:::info[서버 인증 필요]

토큰 없이 제공하려면 명시적으로 옵트인해야 합니다. `server` 객체에 `"allow_unauthenticated": true` 값을 설정하세요. 두 키가 모두 없으면 서버는 시작 시 두 키의 이름을 표시하는 메시지와 함께 종료됩니다. 토큰 변수가 설정되지 않았거나 비어 있는 경우에도 종료됩니다. `v0.12.742` 릴리스부터는 `http` 전송 방식을 사용할 때 `server` 객체에 `auth_token_env_var` 키 또는 `"allow_unauthenticated": true` 값을 추가하세요.

:::

TLS(`tls_cert_file` / `tls_key_file`)는 통신을 암호화하지만 클라이언트를 인증하지는 않습니다. 루프백 이외의 리스너에는 토큰과 함께 사용하세요. 교차 출처 브라우저 요청(일치하지 않는 `Origin` 헤더 또는 교차 사이트 `Sec-Fetch-Site` 헤더)은 인증 여부와 관계없이 `403`으로 거부됩니다. 번들로 제공되는 `stackql_mcp_client` 클라이언트는 `Authorization` 헤더를 보내지 않으므로, 이 클라이언트가 사용하는 서버는 인증되지 않은 서비스에 옵트인해야 합니다.

`v1.0`부터 [`--access.config`](/command-line-usage/access-management) 플래그로 서버를 시작하면 공유 토큰 게이트 대신 호출자별 인증이 적용됩니다. 각 HTTPS 요청은 구성된 JWT, 정적 Bearer 또는 상호 TLS(mTLS) 방식으로 확인되고, 정책 규칙에 따라 권한이 부여됩니다. 이 경우 서버 TLS는 필수입니다. HTTP를 통한 `local_os` 인증은 거부되며, 자격 증명이 없는 요청은 서버의 환경 토큰으로 대체되지 않습니다. 액세스 관리는 `reverse_proxy` 서버 유형에서 사용할 수 없습니다.

* * *

### 서버 모드 {#server-modes}

`server.mode` 필드는 4가지 안전 계약 중 하나를 선택합니다. 4가지 모두 `SELECT` 문과 메타데이터 읽기를 허용하며, 변경 작업과 수명 주기 작업을 처리하는 방식이 다릅니다.

| 모드 | `SELECT` / 메타데이터 | `INSERT` / `UPDATE` / `REPLACE` | `DELETE` | `EXEC`(수명 주기) |
|--|--|--|--|--|
|`read_only`|허용|거부|거부|거부|
|`safe`(기본값)|허용|승인 필요|승인 필요|승인 필요|
|`delete_safe`|허용|허용|승인 필요|승인 필요|
|`full_access`|허용|허용|허용|허용|

<br />

**거부**는 클라이언트에 즉시 오류를 반환합니다.

**승인 필요**는 협상된 [프로토콜 리비전](#protocol-revision-support)에 따라 MCP 엘리시테이션 흐름을 사용합니다:

- 클라이언트가 엘리시테이션 기능을 알린 경우(초기화 시, 또는 `2026-07-28`에서는 요청별 `_meta` 클라이언트 기능에서), 서버는 짧은 메시지(도구 이름, 쿼리 클래스, SQL)로 사용자에게 작업 승인을 요청합니다. `2026-07-28`에서는 승인 요청이 `stackql_approval` ID의 `elicitation/create` 입력 요청을 담은 `input_required` 결과로 반환되고, 클라이언트는 `inputResponses` 필드에 응답을 넣어 호출을 다시 시도합니다. 이전 리비전에서는 서버가 호출 도중에 `elicitation/create` 요청을 직접 보냅니다. 사용자는 수락, 거절 또는 취소할 수 있습니다.
- 클라이언트가 엘리시테이션 기능을 알리지 **않은** 경우, 도구는 부족한 기능을 설명하고 운영자에게 `full_access` 모드를 안내하는 메시지와 함께 거부됩니다.

승인은 클라이언트가 응답하므로, 사용자 모르게 에이전트가 작업하는 것은 막지만 신뢰할 수 없는 호출자는 막지 못합니다. 호출자를 신뢰할 수 없는 경우 HTTP 전송 방식을 [클라이언트 인증](#http-client-authentication)과 함께 사용하세요.

모드는 서버 전체에 적용됩니다. 도구별 재정의는 없습니다.

`v1.0`부터 [`run_select_query`](/mcp/run_select_query) 도구는 `full_access` 모드를 포함한 모든 모드에서 `INSERT`, `UPDATE`, `REPLACE`, `DELETE`, `EXEC` 문을 거부하며, 오류 메시지에 `run_mutation_query` 도구 또는 `run_lifecycle_operation` 도구가 표시됩니다. 또한 모든 도구는 `title` 필드와 4가지 MCP 동작 힌트(`readOnlyHint`, `destructiveHint`, `idempotentHint`, `openWorldHint`)를 명시적 부울 값으로 포함합니다. 이 힌트는 정책 게이트가 적용하는 것과 같은 분류에서 파생됩니다. `openWorldHint` 힌트는 레지스트리나 프로바이더 엔드포인트에 접근하는 `list_registry`, `pull_provider`, `run_select_query`, `run_mutation_query`, `run_lifecycle_operation` 도구에서는 `true`이고, 로컬 상태만 읽는 도구에서는 `false`입니다. 힌트는 클라이언트 UX를 위한 권고 사항이며, 적용은 정책 게이트가 담당합니다.

:::info[페이로드 게이트 제어]

`v0.12.742` 릴리스부터는 여러 문을 담은 페이로드가 모든 모드에서 가장 높은 권한의 문에 의해 게이트 제어됩니다. 따라서 `SELECT 1; DELETE ...` 페이로드는 `DELETE` 문으로 처리되며, 감사 레코드의 `query_class` 값은 `mutation_delete`입니다. `validate_select_query` 도구는 정확히 1개의 문만 받습니다.

:::

#### 기본 모드 동작 변경 {#default-mode-behaviour-change}

이전 릴리스에서는 기본값이 "적용 안 함"인 단일 `read_only: true/false` 플래그로만 안전을 적용했습니다. 현재 기본값(`mode: safe`)에서는 별도 설정 없이도 변경 작업에 사용자 승인이 필요합니다. 엘리시테이션을 지원하는 클라이언트(Claude Desktop, Cursor, Continue 등)를 사용하는 운영자에게는 변경 작업마다 승인 요청이 1번 표시됩니다. 자동화나 번들로 제공되는 `stackql_mcp_client` 클라이언트(엘리시테이션 기능을 알리지 않음)를 사용하는 경우, 변경 작업과 수명 주기 작업을 실행하려면 `mode: full_access`에 명시적으로 옵트인하세요.

레거시 `read_only: true` JSON / YAML 키는 이전 버전과의 호환성을 위해 여전히 허용되며, `mode: read_only`와 동일하게 처리됩니다.

#### 예제 {#examples}

```bash
# Read-only: SELECTs proceed; mutations and lifecycle refused immediately.
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true, "mode": "read_only"}}'

# Delete-safe: INSERT/UPDATE proceed; DELETE and EXEC need approval.
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true, "mode": "delete_safe"}}'

# Full access: everything proceeds without prompting.  Use only with trusted
# clients and operators.
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true, "mode": "full_access"}}'
```

* * *

### 감사 로그 {#audit-log}

모든 도구 호출은 구성된 감사 싱크에 레코드 1개를 기록합니다. 기본적으로 JSONL로 기록하며, OpenTelemetry 로그 레코드로 기록할 수도 있습니다([로그 형식](#log-format) 참조). 감사는 **기본적으로 켜져 있습니다**. 감사는 "에이전트가 무엇을 했는가"에 답하며 "에이전트가 무엇을 보았는가"에는 답하지 않습니다. `SELECT` 문의 결과 행은 어느 형식에서도 의도적으로 기록하지 않습니다.

#### 기록되는 항목 {#what-gets-recorded}

| 필드 | 설명 |
|--|--|
|`timestamp`|호출 시작 시점의 실제 시각(RFC3339).|
|`tool`|도구 이름(예: `run_select_query`).|
|`mode`|호출 시점에 적용된 서버 모드.|
|`decision`|`allow` / `refuse_immediate` / `needs_approval_accepted` / `needs_approval_declined` / `needs_approval_cancelled` / `needs_approval_unavailable`.|
|`query_class`|`select` / `mutation_create` / `mutation_delete` / `lifecycle` / `unknown`. 여러 문을 담은 페이로드의 경우 가장 높은 권한을 가진 문의 클래스.|
|`sql`|쿼리 도구(`run_select_query`, `run_mutation_query`, `run_lifecycle_operation`, `validate_select_query`)의 SQL 문자열.|
|`args`|메타데이터 도구(`list_*`, `describe_method`)의 경우 계층 구조 필드, 쿼리 도구의 경우 SQL + `row_limit`.|
|`duration_ms`|게이트 + 핸들러의 실제 소요 시간.|
|`error`|도구에서 오류가 발생했거나 도구가 거부된 경우의 오류 메시지.|

<br />

`SELECT` 문의 결과 행은 의도적으로 제외됩니다. 결과 행은 매우 클 수 있으며 민감한 데이터를 포함할 수 있습니다.

#### 파일 싱크 {#file-sink}

현재 제공되는 싱크는 `file` 싱크뿐입니다. 한 줄에 JSON 객체 1개를 기록하고, 각 레코드 후에 fsync를 수행하며, 크기 / 기간 / 백업 수 기준의 lumberjack 스타일 로테이션을 지원합니다.

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{
    "server": {
      "transport": "http",
      "address": "127.0.0.1:9912",
      "allow_unauthenticated": true,
      "audit": {
        "file": {
          "path": "/var/log/stackql-mcp.log",
          "max_size_mb": 100,
          "max_backups": 5,
          "max_age_days": 30
        }
      }
    }
  }'
```

위치를 지정하는 방법은 2가지입니다:

- `audit.file.path`: 전체 파일 경로(절대 경로 또는 cwd 기준 상대 경로).
- `audit.file.dir`: 디렉터리. 싱크가 그 안에서 `stackql_mcp_server_<RFC3339-utc-second>.log` 기본 이름을 선택합니다.

`mcp.config`에 둘 다 설정하지 않으면 MCP 서버는 `dir` 값을 cwd로 기본 설정하므로, 기존 운영자는 이전과 같은 동작을 보게 됩니다. 기반이 되는 범용 싱크 패키지 자체는 디렉터리를 암묵적으로 선택하지 않습니다. "로그가 어디에 기록되는가"는 항상 호출자가 결정합니다.

확인된 절대 경로는 시작 시 stderr에 `sink file: /path/to/file.log` 형식으로 기록됩니다.

#### 로그 형식 {#log-format}

감사 스트림에는 2가지 인코딩이 있으며, 둘 다 같은 파일 싱크에 기록됩니다:

```bash
stackql mcp --mcp.server.type=stdio --mcp.log.format=jsonl   # default, unchanged
stackql mcp --mcp.server.type=stdio --mcp.log.format=otel    # OTLP/JSON log records
```

이 플래그는 `mcp.config`의 `server.audit.format` 설정(`"audit": {"format": "otel"}`)을 재정의합니다. `jsonl` 형식은 위에서 설명한 레코드와 바이트 단위로 호환됩니다. `otel` 형식은 한 줄에 OTLP/JSON `LogsData` 객체 1개를 기록합니다. 이는 OpenTelemetry Collector의 `otlp_json_file` 리시버가 그대로 수집하는 형태이므로(otelcol-contrib 0.160.0에서 확인), 변환 프로세서 없이 모든 OTLP 파이프라인으로 스트림을 보낼 수 있습니다:

```yaml
receivers:
  otlp_json_file:
    include: [/var/log/stackql-mcp.log]
exporters:
  debug: {}
service:
  pipelines:
    logs:
      receivers: [otlp_json_file]
      exporters: [debug]
```

각 도구 호출은 로그 레코드 1개를 생성하며, 승인 게이트를 거친 호출은 엘리시테이션 결정에 대한 2번째 레코드를 생성합니다. 한 호출의 레코드는 `gen_ai.tool.call.id` 값과 트레이스 ID를 공유합니다. 호출자가 `params._meta`에 W3C `traceparent` 값을 제공하면 이를 따르고, 그렇지 않으면 MCP 세션마다 트레이스 ID를 1개 생성하여 에이전트 세션의 레코드를 서로 연관시킵니다.

내보내는 속성 집합은 버전이 지정된 인터페이스입니다. 계측 범위는 `github.com/stackql/stackql/pkg/mcp_server/audit`(버전 `1.0.0`)이며, `schemaUrl` 값은 안정적인 규칙을 `https://opentelemetry.io/schemas/1.44.0`으로 고정합니다. `gen_ai.*` 속성과 `mcp.*` 속성은 [GenAI](https://github.com/open-telemetry/semantic-conventions-genai/blob/main/docs/gen-ai/gen-ai-spans.md#execute-tool-span) 및 [MCP](https://github.com/open-telemetry/semantic-conventions-genai/blob/main/docs/gen-ai/mcp.md) 시맨틱 규칙을 따르며, 이 규칙은 Development 상태입니다. 속성 집합이 변경되면 범위 버전이 올라갑니다.

| 속성 | 레코드 | 값 |
|--|--|--|
|`service.name`, `service.version`|리소스|`stackql`, StackQL 버전|
|`gen_ai.operation.name`|도구 호출|`execute_tool`|
|`mcp.method.name`|둘 다|도구 호출 레코드에서는 `tools/call`, 결정 레코드에서는 `elicitation/create`|
|`gen_ai.tool.name`|둘 다|도구 이름|
|`gen_ai.tool.call.id`|둘 다|호출마다 생성되며 두 레코드가 공유|
|`mcp.protocol.version`|둘 다|협상된 리비전|
|`mcp.session.id`|둘 다|세션 ID(세션 없는 HTTP 요청에서는 없음)|
|`stackql.mode`|둘 다|서버 모드|
|`stackql.decision`|둘 다|위 표의 `decision` 값|
|`stackql.query`|도구 호출|쿼리 도구의 경우 원본 그대로의 SQL|
|`stackql.query_class`|도구 호출|`query_class` 값|
|`stackql.provider`|도구 호출|메타데이터 도구의 경우 프로바이더|
|`stackql.query.source`|도구 호출|제공된 경우 Query Library 항목 ID|
|`stackql.duration_ms`|도구 호출|실제 소요 시간|
|`stackql.rows_returned`|도구 호출|행을 반환하는 도구의 경우 행 수|
|`error.type`, `error.message`|도구 호출|실패 시: 거부 결정, 또는 게이트를 통과한 후에는 `tool_error`|

<br />

본문은 `execute_tool <tool>` / `elicitation <tool>`이고, 심각도는 `INFO`이며 호출이 실패하거나 거부된 경우 `ERROR`입니다. 비공개 처리 계약은 두 형식에서 동일합니다. 결과 값은 직렬화되지 않으며, 결과를 생성한 문만 기록됩니다.

#### 실패 모드 {#failure-modes}

싱크가 오류를 반환하면 응답 동작은 `audit.failure_mode` 설정에 따라 달라집니다.

| failure_mode | 효과 |
|--|--|
|`strict`(기본값)|기본 도구가 성공한 경우에도 도구 호출은 감사 오류를 클라이언트에 반환합니다. 의도된 동작입니다: 탐지되지 않은 `DELETE` 작업보다 모호한 클라이언트 응답이 낫습니다.|
|`strict_mutations`|`SELECT` / 메타데이터 읽기는 stderr에 메모를 남기고 진행하며, 변경 작업과 수명 주기 작업은 감사 오류를 표시합니다.|
|`best_effort`|항상 stderr에 로그를 기록하고 진행합니다.|

#### 순서 {#sequencing}

감사 기록은 도구가 실행된(또는 게이트에서 차단된) **후**, 그리고 응답이 클라이언트에 반환되기 **전**에 수행됩니다. strict 모드에서 성공한 DELETE 작업의 감사 기록이 실패하면 행은 삭제되었지만 클라이언트는 오류를 받습니다. 이는 의도된 설계로, 감사되지 않은 변경 작업이 없도록 하기 위한 것입니다.

#### 감사 비활성화 {#disabling-audit}

감사 기능이 없던 `v0.10.474` 이전 릴리스처럼 감사를 완전히 끄는 방법:

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true, "audit": {"disabled": true}}}'
```

* * *

### 사용 가능한 MCP 도구 {#available-mcp-tools}

MCP 서버로 실행할 때 StackQL은 구성된 `enabled_tools` 허용 목록에 따라 다음 도구를 노출합니다. 각 도구는 렌더링된 텍스트 뷰(LLM용)와 형식화된 구조적 페이로드(프로그래밍 방식 클라이언트용)를 모두 반환합니다. 기본 텍스트 렌더링은 균일한 여러 행 결과에는 Markdown 표이고, 희소하거나 단일 레코드인 결과에는 Markdown KV 블록입니다. 인수가 있는 도구는 선택적 `format` 인수(`markdown` 또는 `json`)로 렌더링을 재정의할 수 있으며, 지정하지 않으면 서버의 렌더링 설정이 사용됩니다. 아래 입력 열에는 작업별 인수가 나열되며, `?`는 선택적 입력을 나타냅니다.

도구 이름을 클릭하면 입력, 게이트 제어 동작, 예제 프롬프트를 포함한 전체 참조 페이지를 볼 수 있습니다.

| 도구 | 렌더러 | 설명 | 입력 |
|--|--|--|--|
|[`server_info`](/mcp/server_info)|KV|서버 ID 및 런타임: stackql 버전, 기반 SQL 엔진, 프로바이더 레지스트리 위치, 모드, 읽기 전용 플래그. 세션 시작 시 1번 호출하세요.|없음|
|[`list_providers`](/mcp/list_providers)|테이블|로컬 캐시로 이미 가져온 프로바이더(계층 구조의 최상위).|없음|
|[`list_services`](/mcp/list_services)|테이블|프로바이더 아래의 서비스.|`provider`|
|[`list_resources`](/mcp/list_resources)|테이블|`provider`.`service` 아래의 리소스.|`provider`, `service`|
|[`list_methods`](/mcp/list_methods)|테이블|리소스의 액세스 메서드(HTTP 작업)와 각 메서드에 매핑되는 SQL 동사 및 필수 매개 변수. 쿼리를 작성하기 전에 호출하세요. 필수 `WHERE` 매개 변수는 여기서 추론합니다.|`provider`, `service`, `resource`|
|[`describe_method`](/mcp/describe_method)|KV|하나의 메서드에 대한 전체 I/O 계약(항상 EXTENDED): 필수 입력과 선택적 입력, `SELECT` 문이 참조할 수 있는 출력 필드.|`provider`, `service`, `resource`, `method`|
|[`query_library_search`](/mcp/query_library_search)|테이블|자연어 의도로 엄선된 쿼리 템플릿을 찾습니다. 읽기 전용이며, 변경 작업 항목은 옵트인이 필요하고 `read_only` 모드에서는 제외됩니다.|`intent`, `provider?`, `service?`, `tags?`, `include_mutations?`, `limit?`|
|[`query_library_get`](/mcp/query_library_get)|KV|템플릿과 해당 매개 변수 선언을 조회합니다. `params` 인수가 비어 있지 않으면 실행하지 않고 값의 유효성을 검사한 후 렌더링된 SQL과 `next_tool` 값을 반환합니다.|`id`, `params?`|
|[`validate_select_query`](/mcp/validate_select_query)|KV|단일 `SELECT` 문을 실행하지 않고 구문 분석하고 실행 계획을 수립합니다. `{valid, errors}`를 반환합니다. `SELECT` 문만 지원하며 정확히 1개의 문이어야 합니다.|`sql`|
|[`run_select_query`](/mcp/run_select_query)|테이블|`SELECT` 문을 실행합니다. `{rows}`를 반환합니다. 읽기만 수행합니다.|`sql`, `row_limit?`|
|[`run_mutation_query`](/mcp/run_mutation_query)|KV|프로바이더에 대해 `INSERT`/`UPDATE`/`REPLACE`/`DELETE` 문을 실행합니다. **실제 부작용이 있습니다.** `{messages, timestamp}`를 반환합니다. 서버 [모드](#server-modes)에 의해 게이트 제어됩니다.|`sql`|
|[`run_lifecycle_operation`](/mcp/run_lifecycle_operation)|KV|stackql `EXEC` 수명 주기 작업을 실행합니다. `{messages, timestamp}`를 반환합니다. 서버 [모드](#server-modes)에 의해 게이트 제어됩니다.|`sql`|
|[`list_registry`](/mcp/list_registry)|테이블|구성된 레지스트리에서 사용할 수 있는 프로바이더(및 해당 버전). 이미 가져온 프로바이더만 나열하는 `list_providers` 도구와 다릅니다.|`provider?`|
|[`pull_provider`](/mcp/pull_provider)|KV|레지스트리에서 단일 프로바이더를 로컬 캐시에 설치합니다. 로컬 캐시 상태에만 영향을 주며, 클라우드 컨트롤 플레인이나 데이터 플레인에는 영향이 없습니다.|`provider`, `version?`|
|[`reload_credentials`](/mcp/reload_credentials)|테이블|`--env.file` dotenv 파일에서 자격 증명을 프로세스 환경으로 다시 로드하고 프로바이더별 확인 상태(`ok`, `unresolved`, `not_checked`)를 보고합니다. 시크릿 값은 반환하지 않습니다. 모든 모드에서 허용됩니다.|`provider?`|

SQL을 처음부터 작성하기 전에 `query_library_search` 도구를 먼저 확인하고, `query_library_get` 도구로 일치하는 템플릿을 검사하고 렌더링하세요. 쿼리 도구는 출처 표시만을 위해 라이브러리 항목 ID를 담은 선택적 `source` 인수도 받습니다. 실행은 여전히 일반 서버 모드와 승인 정책을 따릅니다.

:::note

`describe_resource` 도구는 StackQL `v0.12.732`에서 사용 중지되었습니다. 이 도구를 호출하던 MCP 클라이언트, 프롬프트 또는 에이전트 지침은 대신 [`list_methods`](/mcp/list_methods) 도구를 호출한 다음 `describe_method` 도구를 호출해야 합니다. 각 액세스 메서드는 고유한 형태를 반환하므로 리소스에는 단일 필드 목록이 없습니다. 열 이름은 쿼리가 라우팅되는 메서드에 대한 [`describe_method`](/mcp/describe_method) 도구에서 가져옵니다. SQL [`DESCRIBE`](/language-spec/describe) 문은 변경되지 않았습니다.

:::

### 사용 가능한 MCP 프롬프트 {#available-mcp-prompts}

정적 프롬프트 1개가 게시됩니다.

| 프롬프트 | 설명 |
|--|--|
|`write_safe_select`|stackql 리소스에 대해 안전한 `SELECT` 쿼리를 작성하기 위한 지침입니다. `SHOW METHODS IN <provider>.<service>.<resource>` 문을 사용하여 올바른 읽기 메서드와 필수 `WHERE` 매개 변수를 찾는 방법을 설명합니다.|

* * *

### 플래그 {#flags}

| 플래그 | 설명 |
|--|--|
|`--mcp.server.type`|MCP 서버 유형: `http`, `stdio` 또는 `reverse_proxy`(후자는 `stackql srv` 명령과 함께 사용).|
|`--mcp.config`|MCP 서버의 JSON 구성 객체입니다. YAML도 허용됩니다.|
|`--mcp.protocol.version`|알리는 최신 프로토콜 리비전: `auto`(기본값), `2026-07-28` 또는 `2025-11-25` 같은 이전 리비전. [리비전 고정](#pinning-the-revision)을 참조하세요.|
|`--env.file`|시작 시 로드되고 `reload_credentials` 도구가 요청 시 다시 로드하는 dotenv 형식 자격 증명 파일입니다. [자격 증명 로드 및 다시 로드](#credential-resourcing---envfile--reload_credentials)를 참조하세요.|
|`--pgsrv.port`|PostgreSQL 와이어 프로토콜 서버의 TCP 포트(`stackql srv` 명령과 함께 사용).|
|`-h`, `--help`|도움말 정보를 출력합니다.|
|`-v`, `--verbose`|추가 출력이 포함된 자세한 정보 표시 모드로 실행합니다.|

&nbsp;
&nbsp;
> 추가 옵션은 [전역 플래그](/command-line-usage/global-flags)를 참조하세요

:::info

MCP 서버를 시작하기 전에 프로바이더 인증에 필요한 환경 변수를 설정하거나, [`--env.file`](#credential-resourcing---envfile--reload_credentials) 플래그로 dotenv 형식 자격 증명 파일을 지정하세요. 자세한 내용은 [프로바이더 사용](/getting-started/using-a-provider)을 참조하세요.

:::

* * *

### 예제 {#examples-1}

#### 기본 독립 실행형 MCP 서버 {#basic-standalone-mcp-server}

프로바이더 인증과 클라이언트용 Bearer 토큰을 사용하여 독립 실행형 MCP 서버를 시작합니다:

```bash
export GOOGLE_CREDENTIALS=$(cat /path/to/google-credentials.json)
export STACKQL_MCP_TOKEN="$(openssl rand -hex 32)"

stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "auth_token_env_var": "STACKQL_MCP_TOKEN"}}' \
  --registry='{"url": "https://registry.stackql.io/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

#### PostgreSQL 서버와 함께 실행하는 MCP 서버(인메모리) {#mcp-server-with-postgresql-server-in-memory}

이중 프로토콜 액세스를 위해 MCP 서버와 PostgreSQL 서버를 모두 실행합니다:

```bash
export GOOGLE_CREDENTIALS=$(cat /path/to/google-credentials.json)

stackql srv \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true}}' \
  --pgsrv.port 5665 \
  --registry='{"url": "https://registry.stackql.io/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

#### 지정된 경로에 감사 로그를 기록하는 읽기 전용 인벤토리 서버 {#read-only-inventory-server-with-audit-to-a-known-path}

변경 작업을 절대 수행하지 않아야 하는 인벤토리 스캔 에이전트에 유용합니다:

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{
    "server": {
      "transport": "http",
      "address": "127.0.0.1:9912",
      "allow_unauthenticated": true,
      "mode": "read_only",
      "audit": {"file": {"path": "/var/log/stackql-mcp-audit.log"}}
    }
  }' \
  --registry='{"url": "https://registry.stackql.io/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

#### 전체 액세스 자동화 서버 {#full-access-automation-server}

변경 작업이 필요하고 엘리시테이션 요청에 응답할 수 없는 신뢰할 수 있는 자동화 파이프라인용입니다. 파이프라인은 `STACKQL_MCP_TOKEN` 환경 변수에 저장된 Bearer 토큰을 제시합니다:

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{
    "server": {
      "transport": "http",
      "address": "127.0.0.1:9912",
      "auth_token_env_var": "STACKQL_MCP_TOKEN",
      "mode": "full_access"
    }
  }' \
  --registry='{"url": "https://registry.stackql.io/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

#### TLS를 사용하는 보안 MCP 서버 {#secure-mcp-server-with-tls}

역방향 프록시 모드에서 TLS 암호화를 사용하는 MCP 서버를 시작합니다:

먼저 TLS 인증서를 생성합니다(개발용 자체 서명 인증서):

```bash
openssl req -x509 -newkey rsa:4096 -keyout server_key.pem -out server_cert.pem -days 365 -nodes
```

그런 다음 서버를 시작합니다:

```bash
stackql srv \
  --mcp.server.type=reverse_proxy \
  --mcp.config '{
    "server": {
      "tls_cert_file": "server_cert.pem",
      "tls_key_file": "server_key.pem",
      "transport": "http",
      "address": "127.0.0.1:9004",
      "auth_token_env_var": "STACKQL_MCP_TOKEN"
    },
    "backend": {
      "dsn": "postgres://stackql:stackql@127.0.0.1:5446?default_query_exec_mode=simple_protocol"
    }
  }' \
  --pgsrv.port 5446 \
  --registry='{"url": "https://registry.stackql.io/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

* * *

### MCP 서버 테스트 {#testing-your-mcp-server}

리포지토리에는 스크립팅 및 회귀 테스트용 개발 MCP 클라이언트(`stackql_mcp_client`)가 포함되어 있습니다. 다음 명령으로 빌드합니다:

```bash
python cicd/python/build.py --build-mcp-client
```

이 명령은 `./build/stackql_mcp_client` 파일을 생성합니다.

`stackql_mcp_client` 클라이언트는 `Authorization` 헤더를 보내지 않으므로, 대상 서버는 `"allow_unauthenticated": true` 값을 설정해야 합니다(위 예제와 같이). 토큰으로 보호되는 서버는 이 클라이언트에 `401`로 응답합니다. 또한 이 클라이언트는 MCP 엘리시테이션 기능을 알리지 **않습니다**. 비대화형 스크립팅용입니다. `safe` 또는 `delete_safe` 서버에서는 변경 작업과 수명 주기 작업을 호출할 때 "client does not support elicitation" 거부를 받습니다. 엘리시테이션 요청을 사용한 대화형 테스트에는 편집기에 내장된 MCP 클라이언트(Claude Desktop, Cursor, Continue)를 사용하세요.

```bash
# List all available tools
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912

# Server identity, mode, audit / registry context
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action server_info

# Re-source the --env.file credentials file and report per-provider
# credential status - a good first diagnostic when queries fail on auth
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action reload_credentials

# List providers already pulled into the local cache
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action list_providers

# List providers (and versions) available in the registry
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action list_registry

# Install a provider from the registry into the local cache
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action pull_provider \
  --exec.args '{"provider": "kafka"}'

# List services for a provider
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action list_services \
  --exec.args '{"provider": "google"}'

# List access methods for a resource (this is how an agent finds required
# WHERE parameters before writing a query)
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action list_methods \
  --exec.args '{"provider": "google", "service": "compute", "resource": "networks"}'

# Validate a SELECT without executing
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action validate_select_query \
  --exec.args '{"sql": "select name from google.compute.networks where project = '"'"'my-project'"'"';"}'

# Execute a SELECT
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action run_select_query \
  --exec.args '{"sql": "select name from google.compute.networks where project = '"'"'my-project'"'"';"}'

# Mutation - succeeds only against a full_access server, or against
# delete_safe (for INSERT/UPDATE) or safe with an elicitation-capable client.
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action run_mutation_query \
  --exec.args '{"sql": "delete from google.compute.firewalls where project = '"'"'my-project'"'"' and firewall = '"'"'old-rule'"'"';"}'
```

#### 응답 예제 {#example-responses}

`server_info`(기본 모드 서버 대상):

```json
{
  "version": "0.10.444",
  "commit": "abc1234",
  "build_date": "2026-05-16T10:47:33Z",
  "platform": "linux/amd64",
  "transport": "http",
  "sql_backend": "sqlite3",
  "provider_registry": "https://registry.stackql.io/providers",
  "mode": "safe",
  "is_read_only": false
}
```

`run_select_query`:

```json
{
  "rows": [
    {"name": "default"},
    {"name": "pathfinders-test-01"},
    {"name": "returning-test-03"}
  ]
}
```

`run_mutation_query`(`full_access` 서버 대상):

```json
{
  "messages": ["The operation was despatched successfully"],
  "timestamp": "2026-05-16T10:47:33+10:00 AEST"
}
```

같은 변경 작업에 대한 감사 로그 줄 예제:

```json
{"timestamp":"2026-05-16T00:47:33Z","tool":"run_mutation_query","mode":"full_access","decision":"allow","query_class":"mutation_delete","sql":"delete from google.compute.firewalls where project = 'my-project' and firewall = 'old-rule';","args":{"sql":"delete from google.compute.firewalls where project = 'my-project' and firewall = 'old-rule';","row_limit":0},"duration_ms":42}
```

* * *

### AI 어시스턴트와의 통합 {#integration-with-ai-assistants}

StackQL의 MCP 서버를 AI 어시스턴트와 통합하려면 어시스턴트 구성에 `stackql`을 MCP 서버로 등록하세요. 편집기에 내장된 대부분의 MCP 클라이언트는 `stdio` 전송 방식으로 서버를 실행합니다. 이러한 클라이언트에는 `--mcp.server.type=stdio` 플래그를 사용하세요. 그러면 어시스턴트가 프로세스를 직접 시작합니다. HTTP를 사용하는 독립 실행형 에이전트는 장기 실행 중인 `stackql mcp --mcp.server.type=http` 프로세스에 연결할 수 있습니다.

Anthropic Connector Directory 목록(권장 설치 방법), 다운로드 가능한 MCP Bundle(`.mcpb`), 수동 구성 등 Claude Desktop 관련 내용은 [Claude Desktop에서 StackQL 사용](/getting-started/claude-desktop)을 참조하세요.

:::note

구성 파일 위치는 운영 체제와 AI 어시스턴트에 따라 다릅니다. 올바른 경로와 형식은 AI 어시스턴트의 문서를 참조하세요.

:::

* * *

### 아키텍처 고려 사항 {#architecture-considerations}

배포 모드를 선택할 때 다음을 고려하세요:

1. **독립 실행형(`stackql mcp`)**: 가장 간단한 설정이며, 개발 환경과 `stdio` 전송 방식을 사용하는 편집기 내장 클라이언트에 적합합니다.
2. **인메모리 이중 서버(`mcp.server.type=http` 설정을 사용하는 `stackql srv`)**: 단일 프로세스가 MCP와 PostgreSQL 와이어 프로토콜을 모두 제공합니다. 최고의 성능을 제공하며 대부분의 프로덕션 배포에 적합합니다.
3. **역방향 프록시(`mcp.server.type=reverse_proxy` 설정을 사용하는 `stackql srv`)**:
   - 프로세스나 호스트 간 워크로드 분리 가능
   - 보안 MCP 엔드포인트를 위한 TLS 암호화 지원
   - MCP 인터페이스와 PostgreSQL 인터페이스의 독립적인 확장 가능
   - 엔터프라이즈 CA 통합을 위한 유연성 제공(참고: 엔터프라이즈 CA 지원은 실험적 기능입니다)

그리고 안전 측면에서는 별도로 다음을 고려하세요:

- 기본값인 `mode: safe`로 시작하세요. 엘리시테이션을 지원하는 클라이언트(Claude Desktop, Cursor, Continue)는 변경 작업 전에 사용자에게 확인을 요청합니다.
- 쓰기를 절대 수행하지 않아야 하는 인벤토리 또는 분석 에이전트에는 `mode: read_only`로 고정하세요.
- 생성/업데이트는 자유롭게 허용하되 파괴적 작업에는 사람이 개입하도록 하려면 `mode: delete_safe`로 고정하세요.
- `mode: full_access`는 신뢰할 수 있는 자동화 파이프라인에만 사용하고, 반드시 누군가가 검토하는 감사 로그와 함께 사용하세요.
- 다른 프로세스나 호스트가 접근할 수 있는 모든 HTTP 리스너에는 `server.auth_token_env_var` 필드를 설정하세요. `allow_unauthenticated` 설정은 로컬 개발용입니다. TLS만으로는 클라이언트를 인증하지 않습니다.

:::caution

엔터프라이즈 인증 기관(CA)과 함께 TLS를 사용하는 경우 추가 구성이 필요할 수 있습니다. 이 기능은 실험적이며 사용하는 CA 구현에 따라 조정이 필요할 수 있습니다.

:::

* * *

### 추가 자료 {#further-reading}

- 패키지 개발자용 참조는 stackql 리포지토리의 [`pkg/mcp_server` README](https://github.com/stackql/stackql/blob/main/pkg/mcp_server/README.md)입니다.
- 기여자용 기술 문서는 stackql 리포지토리의 [`docs/mcp.md`](https://github.com/stackql/stackql/blob/main/docs/mcp.md)에 있으며, 모든 도구에 대한 응답 예제를 포함합니다.
