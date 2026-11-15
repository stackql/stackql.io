---
slug: /ai-agents
title: AI 에이전트
hide_title: false
keywords:
  - stackql
  - AI 에이전트
  - mcp
  - model context protocol
  - 에이전트형
description: MCP 서버, 도구, 임베디드 통합 및 SQL 쿼리를 통해 StackQL로 AI 에이전트를 클라우드 및 SaaS 서비스에 연결합니다.
image: "/img/stackql-featured-image.png"
---

StackQL은 사람이 클라우드 및 SaaS 서비스를 쿼리하고, 프로비저닝하고, 운영할 때 사용하는 것과 동일한 SQL 인터페이스를 AI 에이전트에 제공합니다. 에이전트는 프로바이더가 노출하는 항목을 탐색하고, 쿼리를 실행하기 전에 검증하고, `SELECT` 문으로 실시간 상태를 읽고, `INSERT`, `UPDATE`, `DELETE` 문으로 상태를 변경합니다. 상태 파일이나 클라우드별 SDK는 필요하지 않습니다.

## 에이전트 연결 방식 {#how-agents-connect}

- **stdio를 통한 MCP**는 Claude Desktop, Cursor, Continue 같은 편집기 및 데스크톱 클라이언트용입니다. 클라이언트가 `stackql mcp --mcp.server.type=stdio` 명령을 직접 실행합니다. [MCP 서버](/command-line-usage/mcp)를 참조하세요.
- **HTTP를 통한 MCP**는 장기 실행 중인 `stackql mcp` 또는 `stackql srv` 프로세스에 연결하는 독립 실행형 에이전트와 자동화용이며, Bearer 토큰 인증과 선택적 TLS를 지원합니다.
- **임베디드 MCP**는 컴파일된 애플리케이션용입니다. 서버는 Go, Rust, Swift, Kotlin, .NET, Gleam 애플리케이션의 프로세스 내에서 실행됩니다. [임베디드 MCP](/mcp/embedded)를 참조하세요.
- **PostgreSQL 와이어 프로토콜**은 [`stackql srv`](/command-line-usage/srv) 명령을 통해 제공되며, 이미 Postgres 프로토콜을 사용하는 에이전트 프레임워크, 노트북, BI 도구용입니다.

## Claude Desktop을 사용한 빠른 시작 {#quick-start-with-claude-desktop}

Anthropic Connector Directory(권장) 또는 다운로드 가능한 MCP Bundle에서 설치하거나, `claude_desktop_config.json` 파일에 서버를 직접 등록합니다.

```json
{
  "mcpServers": {
    "stackql": {
      "command": "stackql",
      "args": ["mcp", "--mcp.server.type=stdio", "--tls.allowInsecure"],
      "env": {
        "AWS_ACCESS_KEY_ID": "your-aws-access-key-id",
        "AWS_SECRET_ACCESS_KEY": "your-aws-secret-access-key"
      }
    }
  }
}
```

사용할 프로바이더의 자격 증명만 포함하세요. 번들 다운로드와 자격 증명 파일 사용을 포함한 전체 지침은 [Claude Desktop에서 StackQL 사용](/getting-started/claude-desktop)에 있습니다.

## 에이전트가 할 수 있는 작업 {#what-an-agent-can-do}

MCP 서버는 작고 형식이 지정된 도구 집합을 노출합니다. 각 도구는 모델용으로 렌더링된 뷰와 프로그래밍 방식 클라이언트용 구조화된 페이로드를 반환합니다.

- **탐색**: `list_providers`, `list_services`, `list_resources`, `list_methods`, `describe_method` 도구는 프로바이더 계층 구조를 순회하고 각 작업의 입력과 출력 필드를 반환합니다. 따라서 쿼리를 작성하기 전에 필요한 `WHERE` 매개 변수를 알 수 있습니다.
- **검증 및 읽기**: `validate_select_query` 도구는 `SELECT` 문을 실행하지 않고 구문 분석하고 실행 계획을 수립합니다. `run_select_query` 도구는 이 문을 실행합니다.
- **변경**: `run_mutation_query` 도구는 `INSERT`, `UPDATE`, `REPLACE`, `DELETE` 문을 실행하고, `run_lifecycle_operation` 도구는 `EXEC` 수명 주기 작업을 실행합니다. 두 도구 모두 아래의 서버 모드에 의해 게이트 제어됩니다.
- **유지 관리**: `server_info`, `list_registry`, `pull_provider`, `reload_credentials`.

도구별 입력, 게이트 제어 동작 및 예제 프롬프트를 포함한 전체 참조는 [MCP 도구](/mcp)에 있습니다.

## 가드레일 {#guardrails}

서버 모드는 세션 전체의 안전 계약을 설정합니다.

| 모드 | `SELECT` / 메타데이터 | `INSERT` / `UPDATE` / `REPLACE` | `DELETE` / `EXEC` |
|--|--|--|--|
| `read_only` | 허용 | 거부 | 거부 |
| `safe`(기본값) | 허용 | 승인 필요 | 승인 필요 |
| `delete_safe` | 허용 | 허용 | 승인 필요 |
| `full_access` | 허용 | 허용 | 허용 |

"승인 필요"는 MCP 엘리시테이션(elicitation)을 사용합니다. 이를 지원하는 클라이언트(Claude Desktop, Cursor, Continue)는 사용자에게 도구 이름, 쿼리 클래스, SQL을 표시하고, 어떤 작업이든 실행하기 전에 결정을 기다립니다. 이 기능을 알리지 않는 클라이언트는 거부됩니다. 모든 도구 호출은 감사 로그에 기록되고, HTTP 리스너에는 Bearer 토큰이 필요하며, 자격 증명은 환경 또는 dotenv 파일에서 로드되고 시크릿 값은 클라이언트에 반환되지 않습니다. 자세한 내용은 [서버 모드](/command-line-usage/mcp#server-modes), [HTTP 클라이언트 인증](/command-line-usage/mcp#http-client-authentication), [감사 로그](/command-line-usage/mcp#audit-log)를 참조하세요.

## 다음 단계 {#next-steps}

- [MCP 서버](/command-line-usage/mcp): 배포 모드, 구성, 서버 모드, 감사 로그 및 플래그.
- [MCP 도구](/mcp): 입력, 게이트 제어 및 예제 프롬프트를 포함한 전체 도구 참조.
- [임베디드 MCP](/mcp/embedded): 자체 Go, Rust, Swift, Kotlin, .NET 또는 Gleam 애플리케이션 내에서 서버 실행.
- [Query Library](pathname:///docs/query-library/): 일반적인 클라우드 운영 질문에 바로 실행할 수 있는 SQL(프로바이더별).
- [AI 참조](/ai): 에이전트와 에이전트를 만드는 사람을 위해 작성된 개념, 사용 방법, 비교 및 문제 해결 문서.
