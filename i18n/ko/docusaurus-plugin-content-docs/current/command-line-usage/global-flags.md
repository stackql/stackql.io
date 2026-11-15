---
title: 전역 플래그
hide_title: false
hide_table_of_contents: true
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "모든 명령에서 사용할 수 있는 stackql 바이너리의 전역 플래그: 인증, 출력 형식, 레지스트리 위치, 로깅 및 SQL 백엔드 설정."
image: "/img/stackql-featured-image.png"
---

전역 플래그는 StackQL 애플리케이션의 런타임 프로그램 동작을 지정합니다. 이러한 플래그는 StackQL 애플리케이션을 시작할 때(대화형/셸 모드 또는 비대화형 exec 모드) 명령줄에서 설정하거나 StackQL 초기화([`.stackqlrc`](/getting-started/using-stackql#the-stackqlrc-initialization-file)) 파일에서 로드합니다. 플래그는 다음과 같습니다.  

| 플래그 | 데이터 형식 | 설명 | 기본값 |
| -- | -- | -- | -- |
| <span class="nowrap">`--access.config`</span> | string | 호출자 액세스 구성으로, JSON 엔벨로프 `{"path": "/path/to/access.json"}` 형식입니다(파일 경로만 지정해도 됩니다). [액세스 관리](/command-line-usage/access-management)를 활성화합니다. 호출자는 JWT, 정적 Bearer 토큰, 상호 TLS 또는 로컬 OS ID로 인증되고, 문은 정책 규칙에 따라 권한이 부여됩니다. 로컬 토큰 자격 증명은 `STACKQL_CALLER_TOKEN` 환경 변수에서 읽습니다. StackQL `v1.0` 이상에서 사용할 수 있습니다. | |
| <span class="nowrap">`--acid`</span> | string | ACID 구성을 나타내는 JSON / YAML 문자열 | `{}` |
| <span class="nowrap">`--allow-persistent-secrets`</span> | flag | 세션이 `SET "$.secret_store"` 문으로 영구 OS 시크릿 저장소(macOS Keychain, Linux Secret Service 또는 Windows Credential Manager)를 선택할 수 있도록 허용합니다. 플랫 파일 저장소는 이 플래그가 있어도 거부됩니다. [프로바이더 세션 및 시크릿](/getting-started/provider-sessions#persistent-os-secret-stores)을 참조하세요. StackQL `v1.0` 이상에서 사용할 수 있습니다. | `false` |
| <span class="nowrap">`--allow-secret-dereference`</span> | flag | SQL 식(`"$secrets.name"`, `"$auth.provider.field"`)에서 시크릿 및 자격 증명 값을 역참조할 수 있도록 허용합니다. 그 결과 이 값이 결과, 로그 및 영구 저장된 쿼리 아티팩트에 포함될 수 있습니다. 일반적인 프로바이더 인증에는 필요하지 않습니다. [SQL의 시크릿 값](/getting-started/provider-sessions#secret-values-in-sql)을 참조하세요. StackQL `v1.0` 이상에서 사용할 수 있습니다. | `false` |
| <span class="nowrap">`--apirequesttimeout`</span> | integer | API 요청 시간 제한(초), 0이면 시간 제한 없음 | `45` |
| <span class="nowrap">`--approot`</span> | string | 애플리케이션 구성 및 캐시 루트 경로 | `{cwd}/.stackql` |
| <span class="nowrap">`--approotfilemode`</span> | uint32 | 애플리케이션 구성 및 캐시 파일 모드 | `493` |
| <span class="nowrap">`--auth`</span> | string | json 형식의 `auth` 컨텍스트 키-값 |  |
| <span class="nowrap">`--cachekeycount`</span> | integer | 캐시 초기 키 개수 | `100` |
| <span class="nowrap">`--configfile`</span> | string | 구성 파일 전체 경로 | `{cwd}/.stackqlrc` |
| <span class="nowrap">`--cpuprofile`</span> | string | `cpuprofile` 파일, 비어 있으면 없음 | `null` |
| <span class="nowrap">`--dataflow.dependency.max`</span> | int | 지정된 쿼리의 최대 데이터 흐름 종속성 깊이 | `50` |
| <span class="nowrap">`--dataflow.components.max`</span> | int | 지정된 쿼리에서 데이터 흐름의 약하게 연결된 구성 요소 최대 개수 | `50` |
| <span class="nowrap">`--dbInternal`</span> | string | DBMS 유지 관리 쿼리 처리를 구성하는 JSON / YAML 문자열 | `{}` |
| <span class="nowrap">`-d`, `--delimiter`</span> | string | csv 출력의 구분 기호입니다. 단일 문자만 허용됩니다. csv 이외의 모든 출력에서는 무시됩니다 | `,` |
| <span class="nowrap">`--dryrun`</span> | flag | `dryrun` 플래그입니다. 전처리기만 실행하며 템플릿이 적용된 출력이 반환됩니다 | `false` |
| <span class="nowrap">`--env.file`</span> | string | 시작 시 프로세스 환경으로 로드되는 선택적 dotenv 스타일 파일입니다(모든 명령). MCP [`reload_credentials`](/mcp/reload_credentials) 도구는 요청 시 이 파일을 다시 로드합니다. 파일이 없어도 허용되고, 값이 비어 있는 키는 건너뛰며, 기존 변수는 덮어씁니다. `.stackqlrc` 파일에서는 로드되지 않습니다. StackQL `v0.10.542` 이후 릴리스에서 사용할 수 있습니다. | `null` |
| <span class="nowrap">`--execution.concurrency.limit`</span> | int | 쿼리 실행의 동시성 제한 | `1` |
| <span class="nowrap">`--export.alias`</span> | string | 내보내기 별칭 접두사(네임스페이스 또는 스키마) | |
| <span class="nowrap">`--gc`</span> | string | GC 구성을 나타내는 JSON / YAML 문자열 | `{}` |
| <span class="nowrap">`-H`, `--hideheaders`</span> | flag | 열 머리글을 비활성화합니다. `--output csv` 또는 `--output text`에서만 유효합니다 | `false` |
| <span class="nowrap">`--http.log.enabled`</span> | flag | 터미널에 http 요청 정보를 표시합니다 | `false` |
| <span class="nowrap">`--http.proxy.host`</span> | string | 프록시 호스트(프록시를 사용하지 않으려면 비워 둠) | `null` |
| <span class="nowrap">`--http.proxy.password`</span> | string | 프록시 암호 | `null` |
| <span class="nowrap">`--http.proxy.port`</span> | integer | 프록시 포트입니다. 0 이하의 숫자를 지정하면 해당 스킴의 기본 포트가 사용됩니다(예: `http -> 80`) | `-1` |
| <span class="nowrap">`--http.proxy.scheme`</span> | string | 프록시 스킴, `http` 또는 `https` | `http` |
| <span class="nowrap">`--http.proxy.user`</span> | string | 프록시 사용자 | `null` |
| <span class="nowrap">`--http.response.maxResults`</span> | integer | http 요청당 최대 결과 수입니다. 0 이하의 숫자를 지정하면 제한이 없습니다 | `-1` |
| <span class="nowrap">`--http.response.pageLimit`</span> | integer | 리소스당 반환되는 결과의 최대 페이지 수입니다. 0 이하의 숫자를 지정하면 제한이 없습니다 | `20` |
| <span class="nowrap">`--indirect.depth.max`</span> | integer | 간접 쿼리(뷰 및 하위 쿼리)의 최대 깊이 | `5` |
| <span class="nowrap">`-i`, `--infile`</span> | string | 쿼리를 읽어 오는 입력 파일(IQL 파일) | `{stdin}` |
| <span class="nowrap">`-q`, `--iqldata`</span> | string | 템플릿 기능에 사용하는 컨텍스트(데이터) 파일(`json` 또는 `jsonnet` 파일)| |
| <span class="nowrap">`--loglevel`</span> | string | 로그 수준이며, {`info`, `warn`, `debug`, `fatal`} 중 하나여야 합니다 | `fatal` |
| <span class="nowrap">`--mcp.config`</span> | string | [MCP 서버](/command-line-usage/mcp#mcp-configuration-object)의 JSON / YAML 구성 객체(`stackql mcp` 및 `stackql srv`) | `{}` |
| <span class="nowrap">`--mcp.log.format`</span> | string | MCP 감사 로그 인코딩으로, `jsonl` 또는 `otel`(OTLP/JSON 로그 레코드)입니다. `--mcp.config` 플래그의 `server.audit.format` 값을 재정의합니다. [로그 형식](/command-line-usage/mcp#log-format)을 참조하세요. StackQL `v0.11.660` 이상에서 사용할 수 있습니다. | `jsonl` |
| <span class="nowrap">`--mcp.protocol.version`</span> | string | 알릴 최신 MCP 프로토콜 리비전으로, `auto`, `2026-07-28`(세션 없는 방식 전용) 또는 `2025-11-25` 같은 이전 리비전입니다. `--mcp.config` 플래그의 `server.protocol_version` 값을 재정의합니다. [리비전 고정](/command-line-usage/mcp#pinning-the-revision)을 참조하세요. StackQL `v0.12.732` 이상에서 사용할 수 있습니다. | `auto` |
| <span class="nowrap">`--mcp.server.type`</span> | string | MCP 서버 유형이며, {`http`, `stdio`, `reverse_proxy`} 중 하나입니다. [MCP 서버 유형](/command-line-usage/mcp#mcp-server-type)을 참조하세요. | `http` |
| <span class="nowrap">`--metadatattl`</span> | integer | 캐시된 메타데이터 문서의 TTL(초) | `3600` |
| <span class="nowrap">`--namespaces`</span> | string | 캐싱, 뷰 등을 위한 네임스페이스를 나타내는 JSON / YAML 문자열 | `{}` |
| <span class="nowrap">`--offline`</span> | flag | 캐시된 데이터를 사용하여 오프라인으로 작업합니다 | `false` |
| <span class="nowrap">`-f`, `--outfile`</span> | string | 결과를 기록할 출력 파일 | `{stdout}` |
| <span class="nowrap">`-o`, `--output`</span> | string | 출력 형식이며, {`json`, `jsonl`, `table`, `csv`, `text`, `otel`} 중 하나여야 합니다(`ndjson`은 `jsonl`의 별칭으로 허용됩니다). `otel` 형식은 OpenTelemetry 로그 레코드를 내보냅니다. [출력 모드](/getting-started/output-modes#otel-output)를 참조하세요. 이 형식은 StackQL `v0.11.660` 이상에서 사용할 수 있습니다. `--otel.config` 플래그에 익스포터가 구성되어 있으면 `otel` 레코드가 해당 엔드포인트로도 푸시됩니다 | `table` |
| <span class="nowrap">`--otel.config`</span> | string | `otel` 출력 형식을 구성하는 JSON / YAML 문자열입니다. `exporter.endpoint`, `exporter.headers`, `exporter.timeout_ms`, `exporter.batch_size` 키는 레코드를 OTLP/HTTP 로그 엔드포인트로 푸시합니다. [OTLP 엔드포인트로 푸시](/getting-started/output-modes#pushing-to-an-otlp-endpoint)를 참조하세요. StackQL `v0.12.732` 이상에서 사용할 수 있습니다 | `{}` |
| <span class="nowrap">`--preview`</span> | string | 미리 보기 기능을 활성화하는 JSON 문자열입니다. `{"unstable": true}` 값은 문서 기반 `stackql_unstable_<provider>.<service>.<resource>` 관계를 노출합니다. 이 관계는 `omnisdk`가 프로바이더 문서에서 직접 해석하고 실행하며, 행은 생성되는 즉시 스트리밍됩니다. 나머지 키(`batchSize`, `flushInterval`, `endpoint`, `insecureSkipTLSVerify`)는 해당 백엔드를 조정합니다. 미리 보기 기능은 안정적이지 않습니다. 동작, 구문 및 출력이 릴리스 간에 예고 없이 변경되거나 중단될 수 있으므로 프로덕션 환경에서 의존하지 마세요. `v1.0`부터는 동일한 옵트인으로 `stackql_preview.iac.run` 및 `stackql_preview.iac.destroy` 관계가 노출됩니다. 이 관계는 `omnisdk`를 통해 선언된 리소스 집합을 적용하고 해체하며, 상태는 애플리케이션 루트 아래에 보관됩니다. [미리 보기 기능](https://github.com/stackql/stackql/blob/main/docs/preview.md)을 참조하세요 | `{}` |
| <span class="nowrap">`--querycachesize`</span> | integer | 쿼리 계획용 LRU 캐시의 크기(항목 수) | `10000` |
| <span class="nowrap">`--registry`</span> | string | 프로바이더 레지스트리 위치와 프로바이더 문서 서명 검증을 구성하는 JSON / YAML 문자열 | [`--registry`](#--registry) 참조 |
| <span class="nowrap">`--session`</span> | string | 세션 구성을 나타내는 JSON / YAML 문자열 | `{}` |
| <span class="nowrap">`--sqlBackend`</span> | string | SQL 백엔드 시스템 구성을 나타내는 JSON / YAML 문자열 | [`--sqlBackend`](#--sqlbackend) 참조 |
| <span class="nowrap">`--store.txn`</span> | string | Txn 저장소 구성을 나타내는 JSON / YAML 문자열 | `{}` |
| <span class="nowrap">`--tls.CABundle`</span> | string | CA 번들 경로이며, 지정하지 않으면 시스템 기본값이 사용됩니다 |  |
| <span class="nowrap">`--tls.allowInsecure`</span> | flag | 안전하지 않은 인증서 신뢰를 허용합니다(권장하지 않음) |  |
| <span class="nowrap">`--var`</span> | string | `jsonnet` 데이터 파일을 통해 StackQL 쿼리에 가져올 키/값 쌍으로 제공되는 외부 변수 |  |
| <span class="nowrap">`-v`, `--verbose`</span> | flag | 자세한 출력 | `false` |
| <span class="nowrap">`-h`, `--help`</span> | | stackql의 상황별 도움말 | |
| <span class="nowrap">`--version`</span> | | StackQL 프로그램의 버전을 표시합니다 | |

## `--registry` {#--registry}

`--registry` 플래그는 프로바이더 문서를 읽어 올 위치와 해당 문서의 서명을 검증할지 여부를 구성합니다. 값은 다음 키가 포함된 JSON 문자열입니다.

| 키 | 설명 | 기본값 |
| -- | -- | -- |
| `url` | 레지스트리 URL. 생략하면 공개 StackQL 프로바이더 레지스트리가 사용됩니다. | 공개 레지스트리 |
| `localDocRoot` | 레지스트리에서 다운로드하는 대신 읽어 오는 프로바이더 문서가 있는 로컬 디렉터리. [프로바이더를 개발](/developers/developing-stackql-providers#testing-a-provider-locally)할 때 사용됩니다. | |
| `srcPrefix` | 레지스트리 루트 아래에서 프로바이더 소스 문서가 있는 경로 접두사. | `src` |
| `verifyConfig.nopVerify` | `true` 값은 서명 검증을 건너뜁니다. `false`로 설정하면 문서 기반 미리 보기 관계를 포함하여 읽는 모든 프로바이더 및 서비스 문서에 포함된 ML-DSA-65(FIPS 204) 서명이 필요합니다. | `true` |
| `verifyConfig.publicKeys` | 프라이빗 레지스트리용으로 키 ID를 16진수로 인코딩된 ML-DSA-65 공개 키에 매핑하는 JSON 객체. 내장된 프로덕션 키 대신 사용되며, 명시적인 빈 객체는 오류입니다. | 내장 키 |

### 예제: 서명된 프로바이더 문서 요구 {#example-require-signed-provider-documents}

공개 레지스트리에서 읽는 모든 프로바이더 및 서비스 문서에 대해 서명 검증을 활성화하며, 내장된 프로덕션 키로 검사합니다.

```bash
stackql --registry '{"verifyConfig": {"nopVerify": false}}' shell
```

### 예제: 로컬 디렉터리에서 서명되지 않은 문서 읽기 {#example-read-unsigned-documents-from-a-local-directory}

레지스트리가 개발 중인 프로바이더 문서 디렉터리를 가리키도록 합니다. 해당 문서는 서명되지 않았으므로 검증은 비활성화된 상태로 유지됩니다.

```bash
stackql --registry '{"localDocRoot": "/path/to/provider-dev/openapi", "verifyConfig": {"nopVerify": true}}' shell
```

:::note

서명 검증은 StackQL `v1.0` 이상에서 사용할 수 있으며 기본적으로 비활성화되어 있습니다. 서명은 문서의 정확한 YAML 바이트를 대상으로 하므로, 서식이나 줄 바꿈 변경을 포함한 모든 편집은 서명을 무효화합니다. 레지스트리의 `pqsig sign-doc` 도구로 다시 서명하세요. 서명되지 않은 로컬 개발 문서는 `nopVerify: true` 설정을 유지해야 합니다. 이전의 인증서 옵션과 `.sig` 사이드카 파일은 더 이상 사용되지 않으며, 사이드카만으로 게시된 아카이브는 검증된 방식으로 사용하기 전에 다시 게시해야 합니다. 검증은 문서를 읽을 때 문서를 인증합니다. 아카이브 다운로드 및 추출은 대상이 아닙니다.

:::

## `--sqlBackend` {#--sqlbackend}

`--sqlBackend` 플래그는 StackQL의 외부 SQL 백엔드를 구성하여, 기본 임베디드 SQL 엔진 대신 외부 데이터베이스 엔진(예: PostgreSQL)을 사용할 수 있게 합니다. 이 기능은 쿼리 처리, 영구 스토리지 또는 기존 데이터베이스 인프라와의 통합에 외부 데이터베이스를 활용할 때 유용합니다.

값은 다음 키가 포함된 JSON 문자열입니다.

| 키 | 설명 | 예제 값 |
| -- | -- | -- |
| `dbEngine` | 데이터베이스 엔진 유형 | `postgres_tcp` |
| `sqlDialect` | 사용할 SQL 방언 | `postgres` |
| `dsn` | 데이터베이스의 데이터 원본 이름(연결 문자열) | 아래 예제 참조 |

### 예제: PostgreSQL 백엔드 사용 {#example-using-a-postgresql-backend}

```bash
./stackql \
  --sqlBackend='{"dbEngine": "postgres_tcp", "sqlDialect": "postgres", "dsn": "postgres://user:password@hostname:port/dbname?sslmode=require"}' \
  shell
```

### 예제: Databricks PostgreSQL 인터페이스 사용 {#example-using-databricks-postgresql-interface}

Databricks는 StackQL SQL 백엔드로 사용할 수 있는 PostgreSQL 호환 인터페이스를 제공합니다. DSN은 Databricks 자격 증명과 함께 표준 PostgreSQL 연결 문자열 형식을 사용합니다.

```bash
./stackql \
  --sqlBackend='{"dbEngine": "postgres_tcp", "sqlDialect": "postgres", "dsn": "postgres://user%40domain.com:your-access-token@your-databricks-host.cloud.databricks.com/databricks_postgres?sslmode=require"}' \
  shell
```

<br/>

:::note
사용자 이름이나 암호의 특수 문자(예: `@`)는 DSN에서 URL 인코딩해야 합니다(예: `@` 문자는 `%40`이 됩니다).
:::
