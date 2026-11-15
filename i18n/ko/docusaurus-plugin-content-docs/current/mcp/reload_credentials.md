---
title: reload_credentials
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - reload_credentials
  - model context protocol
description: dotenv 파일(--env.file)에서 자격 증명을 다시 로드하고 프로바이더별 확인 상태를 보고하는 MCP 도구
image: "/img/stackql-featured-image.png"
---

[`--env.file`](/command-line-usage/global-flags) 플래그로 지정한 dotenv 형식 파일을 서버의 프로세스 환경으로 다시 로드한 다음, 각 프로바이더의 자격 증명 확인 상태를 보고합니다. 이를 통해 환경이 프로세스 시작 시점에 고정되는 문제가 해결됩니다. 세션 도중에 디스크에 기록되거나 교체된 자격 증명이 재시작 없이 실행 중인 서버에 반영됩니다.

env 파일은 프로세스 환경을 대체하지 않고 그 위에 겹쳐 적용됩니다. 자격 증명 확인은 항상 요청 시점의 실제 프로세스 환경을 읽습니다. 따라서 프로세스 시작 시 이미 있던 변수(셸 또는 클라이언트의 `env` 블록에서 설정된 변수)는 `--env.file` 플래그 사용 여부와 관계없이 계속 작동합니다. 다시 로드할 때는 파일에 있고 값이 비어 있지 않은 키만 기록되며, 설정이 해제되는 변수는 없습니다. 따라서 다시 로드가 실패하거나 일부만 완료되더라도 이전에 작동하던 자격 증명은 유지됩니다.

시크릿 값은 반환되지 않으며 로그나 감사 로그에도 기록되지 않습니다. 이 도구는 변수 이름과 상태만 보고합니다. `--env.file` 플래그가 구성되지 않은 경우 이 도구는 상태만 확인하는 기능으로 축소됩니다. 쿼리가 자격 증명 확인 단계에서 실패하면 MCP 오류에 `reload_credentials` 도구를 호출한 후 다시 시도하도록 에이전트에 안내하는 힌트가 포함되므로, 에이전트형 클라이언트가 스스로 복구할 수 있습니다.

:::note

StackQL `v0.10.542` 이후 릴리스에서 사용할 수 있습니다.

:::

참고 항목:
[[ MCP 개요 ]](/command-line-usage/mcp) [[ 자격 증명 로드 및 다시 로드 ]](/command-line-usage/mcp#credential-resourcing---envfile--reload_credentials) [[ `server_info` ]](/mcp/server_info) [[ Claude Desktop ]](/getting-started/claude-desktop)

* * *

## 입력 {#inputs}

| 인수 | 필수 | 설명 |
|--|--|--|
|`provider`|아니요|상태 보고서의 범위를 하나의 프로바이더로 제한합니다. 생략하면 모든 프로바이더가 보고됩니다.|

* * *

## 출력 {#output}

| 렌더러 | 형태 |
|--|--|
| 테이블 | env 파일 로드 결과 및 자격 증명 확인 상태가 포함된 프로바이더당 1행 |

<br/>

구조화된 페이로드에는 다음 필드가 포함됩니다.

| 필드 | 설명 |
|--|--|
|`env_file`|구성된 `--env.file` 경로입니다(구성되지 않은 경우 비어 있음).|
|`env_file_sourced`|이번 호출에서 파일을 찾아 로드한 경우 `true`입니다.|
|`sourced_vars`|파일에서 설정된 환경 변수의 이름입니다. 이름만 포함되며 값은 포함되지 않습니다.|
|`providers`|프로바이더당 1행입니다(아래 참조).|

<br/>

각 `providers` 행에는 다음 열이 있습니다.

| 열 | 설명 |
|--|--|
|`provider`|프로바이더 이름입니다.|
|`auth_type`|프로바이더에 구성된 인증 형식입니다.|
|`sourced_from`|자격 증명을 가져온 위치입니다(예: `env:OKTA_SECRET_KEY`, `file:/path/key.json`, `inline` 또는 `none`).|
|`status`|`ok`, `unresolved` 또는 `not_checked`입니다.|
|`detail`|`unresolved`인 경우의 오류 텍스트입니다.|

<br/>

자격 증명 바이트 경로 밖에서 인증하는 인증 형식(대화형 gcloud, azure CLI, oauth2 클라이언트 자격 증명, 역할 수임 체인, OCI 서명, null)은 `not_checked` 상태를 보고합니다.

* * *

## 게이트 제어 {#gating}

`read_only` 모드를 포함한 모든 서버 모드에서 허용됩니다. 이 도구는 서버 자체의 프로세스 환경 외에는 아무것도 변경하지 않습니다.

`reverse_proxy` 백엔드 모드에서는 지원되지 않습니다. 이 모드에서는 쿼리가 원격 stackql 서버 프로세스에서 실행되므로, 이 도구는 백엔드 서버에서 자격 증명을 다시 로드하도록 사용자에게 안내하는 오류를 반환합니다.

* * *

## 예제 {#example}

StackQL MCP 서버가 등록된(`--env.file` 플래그로 시작된) MCP 클라이언트에서 다음 프롬프트를 사용해 보세요:

```
My okta query failed with a credential error - I've just written the key
to the credentials file.  Reload credentials and retry the query.
```
