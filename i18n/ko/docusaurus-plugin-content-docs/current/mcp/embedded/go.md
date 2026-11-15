---
title: Go
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - 임베디드
  - go
description: go:embed 또는 사이드카로 Go 애플리케이션에 StackQL MCP 서버를 내장합니다
image: "/img/stackql-featured-image.png"
---

# Go

StackQL MCP 서버를 컴파일된 Go 애플리케이션에 내장합니다. 다운로드한 사이드카로 사용하거나 `go:embed` 지시문으로 바이너리에 벤더링할 수 있습니다. 클라이언트는 공식 Go MCP SDK입니다.

:::info
임베디드 바이너리는 첫 실행 시 다운로드되어 sha256으로 검증되거나, 앱에 벤더링됩니다. [공통 계약](/mcp/embedded#common-contract)을 참조하세요.
:::

* * *

## 설치 {#install}

```
go get github.com/stackql/stackql-mcp-go
```

* * *

## 빠른 시작 {#quick-start}

```go
import (
    "context"
    "github.com/modelcontextprotocol/go-sdk/mcp"
    stackqlmcp "github.com/stackql/stackql-mcp-go/embed"
)

func main() {
    ctx := context.Background()
    client, err := stackqlmcp.StartServer(ctx, stackqlmcp.Options{
        Binary: StackqlMCPBinary(),
    })
    if err != nil {
        // handle error
    }
    defer client.Close()

    res, err := client.Session.CallTool(ctx, &mcp.CallToolParams{
        Name: "run_select_query",
        Arguments: map[string]any{"sql": "SELECT name FROM google.compute.regions WHERE project = 'my-project'"},
    })
    _ = res
}
```

API는 `StartServer(ctx, Options{Binary, Mode})` 함수입니다. 반환되는 클라이언트는 공식 Go MCP SDK에서 제공됩니다.

* * *

## 모드 {#modes}

서버는 기본적으로 `read_only` 모드로 시작합니다. `safe`, `delete_safe` 또는 `full_access` 모드로 승격하려면 `Options.Mode` 필드를 설정하세요. 승격은 명시적입니다. 옵트인하지 않으면 서버는 변경 작업 도구나 수명 주기 도구를 활성화하지 않습니다.

* * *

## 벤더링 / 내장 {#vendoring--embedding}

`go:generate` 페치 단계를 통해 `go:embed` 지시문으로 바이너리를 벤더링하면 자체 포함된 단일 실행 파일이 만들어집니다:

```
//go:generate go run github.com/stackql/stackql-mcp-go/cmd/stackql-mcp-fetch -platform auto -package main
```

페치 단계는 대상 플랫폼용 서명된 바이너리를 다운로드하고 sha256으로 검증한 다음, `go:embed` 지시문이 빌드에 포함할 수 있는 위치에 기록합니다. 런타임에는 `StackqlMCPBinary()` 함수가 내장된 바이너리를 반환하므로 첫 실행 시 다운로드가 없습니다.

* * *

## 데모 앱 {#demo-app}

**sandboxctl**은 인프라 컨시어지입니다. 임베디드 서버와 Claude 에이전트 루프를 하나의 바이너리에 결합했으며, 승인 게이트를 거쳐 임시 클라우드 샌드박스의 계획, 비용 검사 및 프로비저닝을 수행합니다.

* * *

## 리포지토리 링크 {#repo-link}

[github.com/stackql/stackql-mcp-go](https://github.com/stackql/stackql-mcp-go)
