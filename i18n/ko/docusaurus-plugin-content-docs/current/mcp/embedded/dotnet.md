---
title: .NET / C#
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - 임베디드
  - dotnet
  - csharp
description: StackQL MCP 서버를 사이드카 또는 벤더링된 번들로 .NET 애플리케이션에 내장합니다
image: "/img/stackql-featured-image.png"
---

# .NET / C#

StackQL MCP 서버를 .NET 애플리케이션에 내장합니다. 다운로드한 사이드카로 사용하거나, 빌드에 벤더링하여 자체 포함 실행 파일로 만들 수 있습니다. 클라이언트는 공식 C# MCP SDK입니다.

:::info
임베디드 바이너리는 첫 실행 시 다운로드되어 sha256으로 검증되거나, 앱에 벤더링됩니다. [공통 계약](/mcp/embedded#common-contract)을 참조하세요.
:::

* * *

## 설치 {#install}

```
dotnet add package StackQL.Mcp
```

선택적 `StackQL.Mcp.AgentFramework` 패키지는 Agent Framework 통합 기능을 추가합니다. .NET 8 이상이 필요합니다.

* * *

## 빠른 시작 {#quick-start}

```csharp
using StackQL.Mcp;

await using var server = await StackqlMcp.CreateBuilder()
    .WithMode(StackqlMode.ReadOnly)
    .WithAuth("github", "null_auth")
    .StartAsync();

var tools = await server.ListToolsAsync();
var result = await server.CallToolAsync("list_services",
    new() { ["provider"] = "github", ["row_limit"] = 5 });
```

API는 `StackqlMcp.CreateBuilder()` 메서드에 `.WithMode()`, `.WithAuth()`, `.WithBinary()`, `.WithBundlePath()`, `.WithApproot()`, `.WithCommand()` 메서드를 연결한 다음 `.StartAsync()`, `.ListToolsAsync()`, `.CallToolAsync()` 메서드를 호출하는 방식입니다.

* * *

## 모드 {#modes}

빌더의 기본값은 `StackqlMode.ReadOnly`입니다. 승격하려면 `StackqlMode.Safe`, `StackqlMode.DeleteSafe` 또는 `StackqlMode.FullAccess` 값으로 `.WithMode()` 메서드를 호출하세요. 승격은 명시적입니다. 옵트인하지 않으면 서버는 변경 작업 도구나 수명 주기 도구를 활성화하지 않습니다.

* * *

## 벤더링 / 내장 {#vendoring--embedding}

바이너리를 확보하는 방법은 2가지입니다:

- **사이드카(기본값)**: 플랫폼 번들을 첫 실행 시 다운로드하고, sha256으로 검증하고, `~/.stackql/mcp-server-bin/<version>/<platform-key>/` 디렉터리에 캐시한 다음, stdio를 통해 실행합니다.
- **벤더링**: 번들을 빌드 리소스로 내장하여 자체 포함 실행 파일을 만듭니다:

```
dotnet build -p:StackqlVendorBundle=/path/to/stackql-mcp-windows-x64.mcpb
```

`STACKQL_MCP_BIN` 또는 `STACKQL_MCP_BUNDLE` 환경 변수를 사용하여 런타임에 바이너리나 번들을 재정의할 수도 있습니다.

* * *

## 데모 앱 {#demo-app}

**driftwatch**는 읽기 전용 StackQL 서버를 내장한 .NET Worker Service입니다. 일정에 따라 일련의 SQL 드리프트 검사(공개 노출, 누락된 태그 또는 라이선스, 기준선 드리프트)를 실행하고 결과를 Teams Adaptive Card에 게시합니다.

* * *

## 리포지토리 링크 {#repo-link}

[github.com/stackql/stackql-mcp-dotnet](https://github.com/stackql/stackql-mcp-dotnet)
