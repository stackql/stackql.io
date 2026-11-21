---
title: Claude Desktop에서 StackQL 사용
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - claude desktop
  - mcp
  - model context protocol
  - ai
  - infrastructure-as-code
  - 멀티 클라우드
description: Claude Desktop에서 StackQL을 사용하여 자연어로 클라우드 리소스 쿼리 및 프로비저닝
image: "/img/stackql-featured-image.png"
---

StackQL은 Model Context Protocol(MCP)를 통해 Claude Desktop과 통합되므로 자연어를 사용하여 여러 클라우드 제공업체의 클라우드 리소스를 쿼리하고 프로비저닝할 수 있습니다. 이 강력한 조합을 통해 내부적으로는 StackQL의 SQL 기반 인터페이스를 활용하면서 대화 방식으로 클라우드 인프라와 상호 작용할 수 있습니다.

설치 옵션은 3가지이며 여기서는 권장 순서대로 제시합니다: Anthropic Connector Directory에서 설치, 다운로드한 번들에서 설치, 로컬에 설치된 StackQL 바이너리를 수동으로 등록.

## Connector Directory에서 설치(권장) {#install-from-the-connector-directory-recommended}

Claude Desktop에서 StackQL MCP 서버를 사용하는 가장 쉬운 방법은 Anthropic Connector Directory에서 설치하는 것입니다. Claude Desktop에서 **Settings -> Connectors -> Add -> Browse Connectors**로 이동하여 **StackQL**을 검색하고 **Install**을 클릭합니다. 다운로드할 것이 없으며 StackQL을 별도로 설치할 필요도 없습니다.

![Anthropic Connector Directory의 StackQL](/img/stackql-anthropic-directory-listing.png)

[claude.ai/directory](https://claude.ai/directory/connectors/ant.dir.gh.stackql.stackql)에서 등록 정보를 직접 확인할 수도 있습니다.

이렇게 하면 사용 중인 플랫폼용 StackQL MCP 서버가 인프로세스로 설치됩니다. 등록 정보는 macOS 유니버설, Windows x64, Linux x64 및 arm64용 서명된 바이너리를 포함하는 통합 멀티 플랫폼 번들을 제공하며, Claude Desktop이 머신에 맞는 바이너리를 자동으로 선택합니다. 네이티브 Windows ARM64의 경우 [아래 다운로드](#install-from-a-downloaded-bundle)에서 별도의 `windows-arm64` 번들을 설치하세요. Windows 바이너리는 Authenticode로 서명되었고 macOS 바이너리는 Apple에서 공증되었습니다. 이는 독립 실행형 StackQL 릴리스와 동일한 바이너리입니다.

![StackQL 커넥터 설치](/img/stackql-anthropic-directory-install.png)

Connector Directory에서 설치한 커넥터는 Claude Desktop의 환경을 상속하므로 Claude Desktop을 시작하기 전에 운영 체제 수준에서 프로바이더의 환경 변수를 설정하세요. [프로바이더별 자격 증명](#provider-specific-credentials)을 참조하세요. Claude Desktop은 시작할 때 환경을 캡처합니다. 변수를 변경한 후에는 Claude Desktop을 완전히 종료하고(Windows에서는 트레이 아이콘 포함) 다시 시작하세요. 에이전트에게 [`reload_credentials`](/mcp/reload_credentials) 도구를 실행하도록 요청하면 프로바이더별 자격 증명 상태가 보고되며, 이는 서버가 볼 수 있는 것과 볼 수 없는 것을 확인하는 가장 빠른 방법입니다.

Claude Desktop은 MCP 엘리시테이션(elicitation) 기능을 알리므로 기본 서버 모드(`safe`)에서는 변경 작업마다 승인을 요청합니다. [서버 모드](/command-line-usage/mcp#server-modes)를 참조하세요.

## 다운로드한 번들에서 설치 {#install-from-a-downloaded-bundle}

특정 버전을 고정하거나, Connector Directory에 액세스하지 않고 설치하거나, 설치 전에 체크섬을 직접 검증하려면 이 설치 옵션을 사용하세요.

미리 빌드된 MCP Bundle(`.mcpb`)은 서명된 `stackql` 바이너리와 서버 구성을 포함하는, 클릭 한 번으로 설치할 수 있는 패키지입니다. StackQL을 별도로 설치할 필요가 없습니다.

최신 릴리스에서 사용 중인 플랫폼용 번들을 다운로드하세요:

| 플랫폼 | 번들 |
|---|---|
| macOS (유니버설) | [stackql-mcp-darwin-universal.mcpb](https://github.com/stackql/stackql/releases/latest/download/stackql-mcp-darwin-universal.mcpb) |
| Windows AMD64 (x64) | [stackql-mcp-windows-x64.mcpb](https://releases.stackql.io/stackql/latest/stackql-mcp-windows-x64.mcpb) |
| Windows ARM64 (네이티브) | [stackql-mcp-windows-arm64.mcpb](https://releases.stackql.io/stackql/latest/stackql-mcp-windows-arm64.mcpb) |
| Linux x64 | [stackql-mcp-linux-x64.mcpb](https://github.com/stackql/stackql/releases/latest/download/stackql-mcp-linux-x64.mcpb) |
| Linux arm64 | [stackql-mcp-linux-arm64.mcpb](https://github.com/stackql/stackql/releases/latest/download/stackql-mcp-linux-arm64.mcpb) |

<br/>

그런 다음 Claude Desktop에서 **Settings -> Extensions -> Install Extension**으로 이동하여 다운로드한 파일을 선택합니다. 번들에 포함된 바이너리는 Authenticode로 서명되었고(Windows) Apple에서 공증되었습니다(macOS). [릴리스 페이지](https://github.com/stackql/stackql/releases/latest)에는 각 번들과 함께 `.sha256` 체크섬 파일이 있습니다. 설치 전에 검증하는 방법은 다음과 같습니다:

```bash
# macOS / Linux
shasum -a 256 -c stackql-mcp-darwin-universal.mcpb.sha256
```

이 서버는 [공식 MCP 레지스트리](https://registry.modelcontextprotocol.io/v0/servers?search=stackql)에도 `io.github.stackql/stackql-mcp` 이름으로 등록되어 있으며, 이 레지스트리는 해당 체크섬을 고정합니다.

## 수동 구성 {#manual-configuration}

StackQL이 이미 설치되어 있으면 Claude Desktop의 구성 파일에 StackQL MCP 서버를 직접 등록할 수 있습니다.

### 사전 요구 사항 {#prerequisites}

Claude Desktop에서 StackQL을 설정하기 전에 다음이 준비되어 있는지 확인하세요:

1. **StackQL 설치**: [stackql.io](/installing-stackql)에서 StackQL을 다운로드하여 설치하거나 패키지 관리자를 사용하여 설치
2. **Claude Desktop**: [Anthropic 웹 사이트](https://claude.ai/desktop)에서 다운로드
3. **클라우드 제공업체 자격 증명**: 사용하려는 클라우드 제공업체의 API 키 또는 자격 증명

다음 명령을 실행하여 StackQL이 시스템 PATH에 있는지 확인하세요:

```bash
stackql --version
```

### MCP 서버 설정 {#setting-up-the-mcp-server}

Claude Desktop이 StackQL과 통신하도록 하려면 Claude Desktop 구성 파일에 MCP 서버를 구성하세요.

#### Windows {#windows}

다음 위치에 있는 구성 파일을 편집하세요:
```
%APPDATA%\Claude\claude_desktop_config.json
```

#### macOS {#macos}

다음 위치에 있는 구성 파일을 편집하세요:
```
~/Library/Application Support/Claude/claude_desktop_config.json
```

#### Linux {#linux}

다음 위치에 있는 구성 파일을 편집하세요:
```
~/.config/Claude/claude_desktop_config.json
```

### 구성 예제 {#configuration-example}

`claude_desktop_config.json` 파일에 다음 구성을 추가하세요:

```json
{
  "mcpServers": {
    "stackql": {
      "command": "stackql",
      "args": [
        "mcp",
        "--mcp.server.type=stdio"
      ],
      "env": {
        "GOOGLE_CREDENTIALS": "/path/to/google-credentials.json",
        "AWS_ACCESS_KEY_ID": "your-aws-access-key-id",
        "AWS_SECRET_ACCESS_KEY": "your-aws-secret-access-key",
        "AZURE_CLIENT_ID": "your-azure-client-id",
        "AZURE_CLIENT_SECRET": "your-azure-client-secret",
        "AZURE_TENANT_ID": "your-azure-tenant-id",
        "DATABRICKS_CLIENT_ID": "your-databricks-client-id",
        "DATABRICKS_CLIENT_SECRET": "your-databricks-client-secret",
        "OPENAI_API_KEY": "your-openai-api-key"
      }
    }
  }
}
```

:::tip
사용하려는 클라우드 제공업체의 환경 변수만 포함하면 됩니다. 사용하지 않는 프로바이더 자격 증명은 구성에서 제거하세요.
:::

:::warning
실제 자격 증명이 포함된 `claude_desktop_config.json` 파일을 버전 관리 시스템에 커밋하지 마세요. 안전한 자격 증명 관리 방식을 사용하세요.
:::

Claude Desktop은 MCP 엘리시테이션 기능을 알리므로 기본 서버 모드(`safe`)에서는 변경 작업마다 승인을 요청합니다. 인벤토리 전용 액세스에는 `mode: read_only` 설정으로, 승인 요청을 건너뛰려면 `mode: full_access` 설정으로 전환하세요. [서버 모드](/command-line-usage/mcp#server-modes)를 참조하세요.

### 자격 증명 파일 사용(`--env.file`) {#using-a-credentials-file---envfile}

`env` 블록 대신 사용할 수 있는 방법(Windows에서는 권장 방법)은 자격 증명을 dotenv 스타일 파일에 보관하고 [`--env.file`](/command-line-usage/mcp#credential-resourcing---envfile--reload_credentials) 플래그로 전달하는 것입니다:

```json
{
  "mcpServers": {
    "stackql": {
      "command": "stackql",
      "args": [
        "mcp",
        "--mcp.server.type=stdio",
        "--env.file=C:\\Users\\me\\stackql-credentials.env"
      ]
    }
  }
}
```

최소 구성의 자격 증명 파일은 다음과 같습니다:

```bash
# stackql-credentials.env
GOOGLE_CREDENTIALS=/path/to/google-credentials.json
AWS_ACCESS_KEY_ID=your-aws-access-key-id
AWS_SECRET_ACCESS_KEY=your-aws-secret-access-key
```

이 방법이 `env` 블록보다 나은 이유:

- `env` 블록의 변경 사항은 Claude Desktop을 완전히 다시 시작해야(트레이 아이콘 종료 포함) MCP 하위 프로세스에 반영됩니다. 또한 Windows에서 `setx` 명령은 레지스트리에 기록하므로 실행 중인 Desktop 프로세스에는 전달되지 않습니다.
- `--env.file` 플래그로 지정한 파일은 언제든지 생성하거나 교체할 수 있으며(서버가 시작될 때 존재하지 않아도 됩니다), 에이전트에게 [`reload_credentials`](/mcp/reload_credentials) 도구를 실행하도록 요청하면 다시 시작하지 않고도 변경 사항이 적용됩니다.

:::note

`--env.file` 플래그와 `reload_credentials` 도구는 StackQL `v0.10.542` 이후 릴리스에서 사용할 수 있습니다.

:::

### 프로바이더별 자격 증명 {#provider-specific-credentials}

클라우드 제공업체마다 필요한 인증 방법이 다릅니다. 원하는 프로바이더에 필요한 환경 변수는 프로바이더 문서([https://stackql.io/providers](/providers))를 참조하세요.


### Claude Desktop 다시 시작 {#restarting-claude-desktop}

구성 파일을 업데이트한 후 Claude Desktop을 다시 시작하여 새 MCP 서버 구성을 로드하세요. 새 대화를 시작하면 StackQL MCP 서버가 자동으로 시작됩니다.

## 할 수 있는 작업 {#what-you-can-do}

구성이 완료되면 Claude Desktop에서 자연어로 다음 작업을 수행할 수 있습니다:

### 클라우드 리소스 쿼리 {#query-cloud-resources}

Claude에게 클라우드 인프라에 대한 정보를 조회하도록 요청합니다:

- "us-central1 리전에 있는 내 GCP 컴퓨팅 인스턴스를 모두 보여 주세요"
- "내 AWS 계정의 S3 버킷을 모두 나열해 주세요"
- "내 구독에서 실행 중인 Azure 가상 머신은 무엇인가요?"
- "default 네임스페이스의 Kubernetes 파드를 모두 보여 주세요"

### 리소스 프로비저닝 {#provision-resources}

대화형 명령으로 클라우드 리소스를 생성하고 배포합니다:

- "us-west-2에 'my-data-bucket'이라는 S3 버킷을 만들어 주세요"
- "버전 관리가 활성화된 GCP Cloud Storage 버킷을 배포해 주세요"
- "us-east-1에서 작은 EC2 인스턴스를 시작해 주세요"
- "westus2에 Azure 리소스 그룹을 만들어 주세요"

### 분석 및 감사 {#analyze-and-audit}

보안 감사 및 규정 준수 검사를 수행합니다:

- "공개적으로 액세스할 수 있는 S3 버킷을 모두 보여 주세요"
- "90일 동안 로그인하지 않은 IAM 사용자를 나열해 주세요"
- "적절한 라벨이 없는 GCP 인스턴스를 모두 찾아 주세요"
- "백업이 활성화되지 않은 Azure VM을 식별해 주세요"

### 클라우드 간 작업 {#cross-cloud-operations}

여러 클라우드 제공업체의 리소스를 쿼리하고 비교합니다:

- "AWS, GCP, Azure의 모든 컴퓨팅 인스턴스를 보여 주세요"
- "AWS S3와 GCP Cloud Storage의 스토리지 비용을 비교해 주세요"
- "모든 클라우드 제공업체의 데이터베이스를 모두 나열해 주세요"

### 코드형 인프라 {#infrastructure-as-code}

인프라 배포 쿼리를 생성하고 실행합니다:

- "VPC, 서브넷, EC2 인스턴스를 포함하는 리소스 스택을 만들어 주세요"
- "노드 풀이 포함된 완전한 GKE 클러스터를 배포해 주세요"
- "AWS에서 멀티 리전 배포를 설정해 주세요"

### 리소스 수정 {#modify-resources}

기존 클라우드 리소스를 업데이트합니다:

- "태그가 없는 모든 EC2 인스턴스에 태그를 추가해 주세요"
- "S3 버킷 'my-bucket'의 암호화를 활성화해 주세요"
- "GCP 인스턴스 'web-server-1'의 머신 유형을 업데이트해 주세요"
- "내 Cloud Storage 버킷의 ACL을 비공개로 변경해 주세요"

### 리소스 삭제 {#delete-resources}

더 이상 필요하지 않은 클라우드 리소스를 제거합니다:

- "S3 버킷 'temp-bucket'을 삭제해 주세요"
- "중지된 EC2 인스턴스를 모두 제거해 주세요"
- "사용하지 않는 GCP 디스크를 정리해 주세요"

## 대화 예제 {#example-conversation}

다음은 StackQL을 사용하여 Claude Desktop과 상호 작용하는 방법의 예입니다:

**사용자:** "내 GCP 컴퓨팅 인스턴스를 모두 보여 주세요"

**Claude:** *StackQL 쿼리를 실행하고 인스턴스 이름, 영역, 머신 유형, 상태를 보여 주는 서식이 지정된 표로 결과를 반환합니다*

**사용자:** "us-central1 리전에 균일한 버킷 수준 액세스를 사용하는 'analytics-data-2025'라는 새 Cloud Storage 버킷을 만들어 주세요"

**Claude:** *StackQL INSERT 문을 실행하여 버킷을 생성하고 생성되었음을 확인합니다*

**사용자:** "이제 내 Cloud Storage 버킷을 모두 나열해 주세요"

**Claude:** *SELECT 쿼리를 실행하고 새로 생성된 버킷을 포함한 모든 버킷을 보여 줍니다*

## 팁 및 모범 사례 {#tips-and-best-practices}

1. **구체적인 요청**: 요청이 구체적일수록 더 나은 결과를 얻을 수 있습니다. 가능하면 리소스 이름, 리전, 특정 속성을 포함하세요.

2. **자연어 사용**: SQL 구문을 알 필요가 없습니다. Claude가 자연어 요청을 StackQL 쿼리로 변환합니다.

3. **실행 전 검토**: 파괴적인 작업(DELETE, UPDATE)의 경우 Claude는 일반적으로 진행하기 전에 실행할 내용을 보여 줍니다.

4. **다단계 작업**: 하나의 대화에서 여러 작업을 연결할 수 있습니다. Claude는 세션 전체에서 컨텍스트를 유지합니다.

5. **오류 처리**: 쿼리가 실패하면 Claude가 문제 해결을 돕고 오류 메시지를 바탕으로 수정 사항을 제안할 수 있습니다.

6. **기능 탐색**: 구성된 프로바이더에 맞는 제안을 받으려면 Claude에게 "StackQL로 무엇을 할 수 있나요?"라고 물어보세요.

## 문제 해결 {#troubleshooting}

### MCP 서버가 시작되지 않음 {#mcp-server-not-starting}

Claude Desktop이 StackQL MCP 서버에 연결할 수 없는 경우:

1. StackQL이 PATH에 있는지 확인
2. `claude_desktop_config.json` 파일이 유효한 JSON인지 확인
3. 구성의 파일 경로가 운영 체제에 맞는지 확인
4. Claude Desktop 로그에서 오류 메시지 검토

### 인증 오류 {#authentication-errors}

인증 오류가 발생하는 경우:

1. 클라우드 제공업체 자격 증명이 올바른지 확인
2. 지정된 경로에 자격 증명 파일이 있는지 확인
3. 구성에서 환경 변수의 형식이 올바른지 확인: 에이전트에게 [`reload_credentials`](/mcp/reload_credentials) 도구를 실행하도록 요청하면 프로바이더별 자격 증명 상태가 보고되며, 이는 어떤 변수가 누락되었는지 확인하는 가장 빠른 방법입니다
4. 자격 증명에 필요한 권한이 있는지 확인

### 권한 오류 {#permission-errors}

권한이 부족하여 작업이 실패하는 경우:

1. 서비스 계정 또는 IAM 사용자에게 필요한 권한이 있는지 확인
2. 프로바이더별 IAM 정책 확인
3. 클라우드 프로젝트에서 API 서비스가 활성화되어 있는지 확인

## 추가 정보 {#learn-more}

- [StackQL MCP 명령 참조](/command-line-usage/mcp)
- [StackQL Provider Registry](/providers)
- [언어 사양](/language-spec/select)
- [Model Context Protocol 문서](https://modelcontextprotocol.io)

## 보안 고려 사항 {#security-considerations}

:::caution
- 자격 증명을 안전하게 저장하고 버전 관리 시스템에 커밋하지 마세요
- 서비스 계정에는 최소 권한 액세스 원칙을 적용하세요
- 자격 증명과 API 키를 정기적으로 교체하세요
- MCP 서버 로그에서 비정상적인 활동을 모니터링하세요
- 클라우드 제공업체의 자격 증명 관리 도구(예: Secret Manager, Parameter Store) 사용을 고려하세요
:::
