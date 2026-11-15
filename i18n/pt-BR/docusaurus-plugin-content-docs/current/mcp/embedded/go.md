---
title: Go
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - incorporado
  - go
description: Incorpore o servidor MCP do StackQL em uma aplicação Go via go:embed ou sidecar
image: "/img/stackql-featured-image.png"
---

# Go

Incorpore o servidor MCP do StackQL em uma aplicação Go compilada, seja como um sidecar baixado, seja incluído no seu binário por vendoring com `go:embed`. O cliente é o SDK oficial do MCP para Go.

:::info
O binário incorporado é baixado e verificado com sha256 na primeira execução, ou incluído na sua aplicação por vendoring. Consulte o [contrato comum](/mcp/embedded#common-contract).
:::

* * *

## Instalação {#install}

```
go get github.com/stackql/stackql-mcp-go
```

* * *

## Início rápido {#quick-start}

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

A API é `StartServer(ctx, Options{Binary, Mode})`; o cliente retornado vem do SDK oficial do MCP para Go.

* * *

## Modos {#modes}

O servidor inicia no modo `read_only` por padrão. Defina `Options.Mode` para elevar o modo para `safe`, `delete_safe` ou `full_access`. A elevação é explícita - o servidor não habilita as ferramentas de mutação nem as de ciclo de vida, a menos que você as ative explicitamente.

* * *

## Vendoring / incorporação {#vendoring--embedding}

Faça o vendoring do binário com `go:embed` por meio de uma etapa de busca com `go:generate`, gerando um único executável autocontido:

```
//go:generate go run github.com/stackql/stackql-mcp-go/cmd/stackql-mcp-fetch -platform auto -package main
```

A etapa de busca baixa o binário assinado da plataforma de destino, o verifica com sha256 e o grava onde o `go:embed` pode incluí-lo no seu build. Em tempo de execução, `StackqlMCPBinary()` retorna o binário incorporado, então não há download na primeira execução.

* * *

## Aplicativo de demonstração {#demo-app}

**sandboxctl** é um concierge de infraestrutura: um servidor incorporado e um loop de agente do Claude em um único binário que planeja, verifica o custo e provisiona sandboxes efêmeros na nuvem, sujeitos a uma etapa de aprovação.

* * *

## Link do repositório {#repo-link}

[github.com/stackql/stackql-mcp-go](https://github.com/stackql/stackql-mcp-go)
