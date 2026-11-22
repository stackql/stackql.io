---
title: srv
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "stackql srv 명령은 StackQL을 PostgreSQL 와이어 프로토콜 서버로 실행하므로 psql, BI 도구 및 언어 드라이버에서 SQL로 클라우드 및 SaaS API를 쿼리할 수 있습니다."
image: "/img/stackql-featured-image.png"
---

StackQL을 서비스로 시작하는 데 사용하는 명령입니다. 시작된 서비스에는 [Postgres 와이어 프로토콜](https://www.postgresql.org/docs/current/protocol.html) 클라이언트로 액세스할 수 있으며, 이 클라이언트는 mTLS로 인증할 수 있습니다. `srv` 명령은 PostgreSQL 와이어 프로토콜 서버와 함께 [Model Context Protocol (MCP)](/command-line-usage/mcp) 서버도 실행할 수 있으므로, 기존 데이터베이스 클라이언트와 AI 에이전트 모두에 이중 프로토콜 액세스를 제공합니다.

* * * 

### 구문 {#syntax}

`stackql srv [server_options] [flags]`

* * *

:::note

StackQL 서버는 Postgres 와이어 프로토콜을 사용하지만 Postgres 서버가 아니며, Postgres 서버가 필요하지도 않습니다. 와이어 프로토콜은 클라이언트가 서버와 통신하는 데 사용됩니다. 서버는 StackQL을 사용하여 원격 클라우드 및 SaaS 제공업체에 대해 쿼리 또는 DML 작업을 수행하고 결과를 클라이언트에 반환합니다.

:::

확장 쿼리 프로토콜(`psycopg` 3, `pgx`, JDBC에서 사용하는 `Parse` / `Bind` / `Execute`)로 전송된 매개 변수는 서버 측에서 문자열 리터럴로 바인딩됩니다. `v0.12.742`부터 문자열 리터럴, 따옴표로 묶은 식별자 또는 주석 안의 `$n` 자리 표시자는 그대로 유지되고, 매개 변수 값의 백슬래시는 이스케이프되며, `NULL`이라는 텍스트 값은 문자열 `NULL`로 유지됩니다(SQL `NULL`은 별도로 전달됩니다).

### 서버 옵션 {#server-options}

| 옵션 | 설명 | 기본값 |
|--|--|--|
|<span class="nowrap">`--pgsrv.address`</span>|서버가 수신 대기하는 주소입니다.|`0.0.0.0`|
|<span class="nowrap">`--pgsrv.port`</span>|서버가 수신 대기하는 TCP 포트입니다.|`5466`|
|<span class="nowrap">`--pgsrv.tls`</span>|JSON TLS 구성 객체입니다. 아래 [mTLS 예제](#examples)를 참조하세요.|없음|
|<span class="nowrap">`--pgsrv.debug.enable`</span>|PostgreSQL 클라이언트에 대한 쿼리 디버그 출력을 활성화합니다(예: `stackql srv --pgsrv.debug.enable=true`). 클라이언트가 디버그 정보를 받아야 하는 경우가 아니면 비활성화된 상태로 두세요.|`false`|
|<span class="nowrap">`--pgsrv.sundry`</span>|`--pgsrv.tls` 플래그가 설정되지 않은 경우에만 PostgreSQL 와이어 서버에 전달되는 고급 JSON 또는 YAML 매핑입니다.|`{}`|
|<span class="nowrap">`--pgsrv.loglevel`</span>|허용되지만 현재 엔진에서는 로거 수준을 설정하지 않습니다. 대신 `--loglevel` 플래그를 사용하세요.|`WARN`|

<br/>

> 추가 옵션은 [전역 플래그](/command-line-usage/global-flags)를 참조하세요.

### MCP 서버 옵션 {#mcp-server-options}

MCP 지원과 함께 `srv` 명령을 실행하는 경우 다음 추가 옵션을 사용할 수 있습니다:

| 옵션 | 설명 |
|--|--|
|<span class="nowrap">`--mcp.server.type`</span>|MCP 서버 유형: `http`(인메모리) 또는 `reverse_proxy`(TCP 기반)|
|<span class="nowrap">`--mcp.config`</span>|MCP 서버의 JSON 구성 객체([MCP 문서](/command-line-usage/mcp) 참조)|
|<span class="nowrap">`--mcp.log.format`</span>|MCP 감사 로그 인코딩: `jsonl`(기본값) 또는 `otel`(OTLP/JSON 로그 레코드). 구성 객체의 `server.audit.format` 설정을 재정의합니다.|
|<span class="nowrap">`--mcp.protocol.version`</span>|서버가 알리는 최신 MCP 프로토콜 리비전: `auto`(기본값), `2026-07-28`(세션 없는 방식 전용) 또는 `2025-11-25` 같은 이전 리비전. 구성 객체의 `server.protocol_version` 설정을 재정의합니다.|

<br/>

:::info[v0.12.742의 호환성이 손상되는 변경]

`v0.12.742`부터 MCP HTTP 전송 방식(`--mcp.server.type=http` 또는 `reverse_proxy`)은 구성 객체가 `server.auth_token_env_var` 키로 Bearer 토큰을 지정하거나 `"allow_unauthenticated": true` 설정으로 명시적으로 옵트아웃하지 않으면 시작되지 않습니다. [HTTP 클라이언트 인증](/command-line-usage/mcp#http-client-authentication)을 참조하세요.

:::

:::tip

배포 모드, 구성 옵션, AI 어시스턴트와의 통합을 포함하여 MCP 서버의 구성 및 사용에 대한 자세한 내용은 [MCP 명령 문서](/command-line-usage/mcp)를 참조하세요.

:::

:::info

서버를 시작하기 전에 프로바이더 인증에 필요한 환경 변수를 설정하세요. 자세한 내용은 [프로바이더 사용](/getting-started/using-a-provider)을 참조하세요.

:::

장기 실행 서버의 경우 [`--env.file`](/command-line-usage/global-flags) 플래그를 사용하면 셸로 변수를 내보내는 대신 시작 전에 디스크의 dotenv 스타일 파일에 자격 증명을 작성하거나 교체할 수 있습니다:

```bash
stackql srv --pgsrv.port 5444 --env.file /etc/stackql/credentials.env
```

`stackql srv` 명령의 경우 파일은 시작 시 한 번만 읽힙니다. 세션 도중에 다시 로드하려면 MCP [`reload_credentials`](/mcp/reload_credentials) 도구를 사용합니다.

### 플래그 {#flags}

| 플래그 | 설명 |
|--|--|
|`-h,--help`|도움말 정보 출력|
|`-v,--verbose`|상세 모드로 쿼리를 실행하며 추가 출력은 stdout으로 전송됩니다. 전역 `-f`(`--outfile`) 플래그를 설정한 경우<br/>이 추가 로깅 정보는 쿼리 결과와 함께 출력 파일에 기록됩니다.|

<br/>

> 추가 옵션은 [전역 플래그](/command-line-usage/global-flags)를 참조하세요.

* * *

### 예제 {#examples}

StackQL 서버 프로세스를 시작하고 `psql` 명령줄 클라이언트로 연결합니다.

먼저 [stackql/stackql](https://raw.githubusercontent.com/stackql/stackql/main/test/server/mtls/openssl.cnf)에서 `openssl.cnf` 구성 파일을 다운로드합니다.

다음으로 서버와 클라이언트가 상호 TLS 인증(mTLS)에 사용할 인증서 파일과 키 파일을 생성합니다.

```bash
openssl req -x509 -keyout ~/stackqlcreds/server_key.pem -out ~/stackqlcreds/server_cert.pem -config ./openssl.cnf -days 365
openssl req -x509 -keyout ~/stackqlcreds/client_key.pem -out ~/stackqlcreds/client_cert.pem -config ./openssl.cnf -days 365
chmod 400 ~/stackqlcreds/client_key.pem
```

이제 환경 변수를 설정합니다. 클라이언트와 서버가 같은 호스트에 있으면 이 환경 변수를 공유할 수 있습니다.

```bash
export PGPORT=5444
export PGSSLCERT=~/stackqlcreds/client_cert.pem
export PGSSLKEY=~/stackqlcreds/client_key.pem
export PGSSLROOTCERT=~/stackqlcreds/server_cert.pem
export PGSSLSRVKEY=~/stackqlcreds/server_key.pem
export CLIENT_CERT=$(base64 -w 0 ~/stackqlcreds/client_cert.pem)
export PGSSLMODE=allow
```

서버에 필요한 프로바이더의 자격 증명을 설정합니다. GitHub의 경우 프로바이더는 다음 두 변수에서 사용자 이름과 개인용 액세스 토큰을 읽습니다:

```bash
export STACKQL_GITHUB_USERNAME=youruser
export STACKQL_GITHUB_PASSWORD=ghp_youraccesstoken
```

이제 한 터미널에서 서버를 시작합니다.

```bash
stackql srv \
--pgsrv.address=0.0.0.0 \
--pgsrv.port=$PGPORT \
--pgsrv.tls='{ "keyFilePath": "'${PGSSLSRVKEY}'", "certFilePath": "'${PGSSLROOTCERT}'", "clientCAs": [ "'${CLIENT_CERT}'" ] }'
```

다른 터미널에서 `psql` 명령줄 클라이언트를 사용하여 서버에 연결합니다.

```bash
psql -h 127.0.0.1 -d stackql
```

이제 인증된 프로바이더에 대해 StackQL 쿼리를 실행합니다.

```
psql (12.11 (Ubuntu 12.11-0ubuntu0.20.04.1), server 0.0.0)
Type "help" for help.

stackql=> SHOW SERVICES IN github LIKE '%repos%';
      id      | name  |           title
--------------+-------+----------------------------
 repos:v0.3.3 | repos | GitHub v3 REST API - repos
(1 row)

stackql=>
```
