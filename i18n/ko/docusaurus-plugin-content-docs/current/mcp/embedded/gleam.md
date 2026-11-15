---
title: Gleam
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - 임베디드
  - gleam
  - erlang
  - beam
description: StackQL MCP 서버를 Erlang/BEAM 대상의 Gleam 애플리케이션에 내장합니다
image: "/img/stackql-featured-image.png"
---

# Gleam

StackQL MCP 서버를 Erlang/BEAM 대상의 Gleam 애플리케이션에 내장합니다. 이 라이브러리는 OTP 슈퍼비전과 통합되며, 서명된 바이너리를 다운로드하고 검증한 다음 stdio를 통해 실행합니다.

:::info
임베디드 바이너리는 첫 실행 시 다운로드되어 sha256으로 검증되거나, 앱에 벤더링됩니다. [공통 계약](/mcp/embedded#common-contract)을 참조하세요.
:::

* * *

## 설치 {#install}

```
gleam add stackql_mcp
```

Erlang/BEAM 대상만 지원합니다(JavaScript 대상은 지원하지 않음). 서버는 `child_spec()` 함수를 통해 OTP 자식으로 노출되므로 자체 슈퍼비전 트리 안에 배치할 수 있습니다.

* * *

## 빠른 시작 {#quick-start}

```gleam
import gleam/io
import gleam/int
import gleam/list
import envoy
import stackql_mcp

pub fn main() {
  let config =
    stackql_mcp.Config(
      ..stackql_mcp.default_config(),
      auth: [stackql_mcp.auth_for("github", "null_auth")],
    )

  let assert Ok(server) =
    stackql_mcp.start(
      config: config,
      home: "/home/u",
      os: "linux",
      arch: "x86_64",
      getenv: envoy.get,
    )

  let assert Ok(tools) = stackql_mcp.list_tools(server)
  io.println(int.to_string(list.length(tools)) <> " tools available")

  stackql_mcp.stop(server)
}
```

API는 수명 주기용 `stackql_mcp.start()`, `stop()`, `child_spec()` 함수, 작업용 `list_tools()`, `call_tool()` 함수(슈퍼바이저 아래에서 사용하는 `supervised_list_tools()` 및 `supervised_call_tool()` 변형 포함), 구성용 `Config` 형식과 `default_config()`, `auth_for()` 함수로 구성됩니다.

* * *

## 모드 {#modes}

모드는 `Config.mode` 필드에서 설정하며 기본값은 `read_only`입니다. 승격하려면 `safe`, `delete_safe` 또는 `full_access` 값으로 설정하세요:

```gleam
stackql_mcp.Config(..stackql_mcp.default_config(), mode: stackql_mcp.safe)
```

승격은 명시적입니다. 옵트인하지 않으면 서버는 변경 작업 도구나 수명 주기 도구를 활성화하지 않습니다.

* * *

## 벤더링 / 내장 {#vendoring--embedding}

시작 시 라이브러리는 플랫폼용 StackQL 번들을 다운로드하고, 내장된 sha256 고정 값으로 검증한 다음, 라이브러리 제품군의 공유 캐시에 캐시합니다. 확인 우선순위:

1. 명시적 `Config.binary` 값.
2. `STACKQL_MCP_BIN` 환경 변수.
3. `~/.stackql/mcp-server-bin/<version>/<platform-key>/` 디렉터리의 공유 캐시.

오프라인 또는 에어 갭 배포의 경우 로컬 번들을 가리키도록 `STACKQL_MCP_BUNDLE` 환경 변수를 설정하세요.

* * *

## 데모 앱 {#demo-app}

**pipewatch**는 이 라이브러리용으로 계획된 데모입니다. 로드맵에 있으며 아직 구현되지 않았습니다.

* * *

## 리포지토리 링크 {#repo-link}

[github.com/stackql/stackql-mcp-gleam](https://github.com/stackql/stackql-mcp-gleam)
