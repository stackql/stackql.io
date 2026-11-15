---
title: Go
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - integrado
  - go
description: Integre el servidor MCP de StackQL en una aplicación Go mediante go:embed o como sidecar
image: "/img/stackql-featured-image.png"
---

# Go

Integre el servidor MCP de StackQL en una aplicación Go compilada, ya sea como un sidecar descargado o incorporado a su binario mediante vendoring con `go:embed`. El cliente es el SDK oficial de MCP para Go.

:::info
El binario integrado se descarga y se verifica con sha256 en la primera ejecución, o se incorpora a su aplicación mediante vendoring. Consulte el [contrato común](/mcp/embedded#common-contract).
:::

* * *

## Instalación {#install}

```
go get github.com/stackql/stackql-mcp-go
```

* * *

## Inicio rápido {#quick-start}

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

La API es `StartServer(ctx, Options{Binary, Mode})`; el cliente devuelto procede del SDK oficial de MCP para Go.

* * *

## Modos {#modes}

El servidor se inicia en modo `read_only` de forma predeterminada. Establezca `Options.Mode` para elevar el modo a `safe`, `delete_safe` o `full_access`. La elevación de modo es explícita - el servidor no habilita las herramientas de mutación ni las de ciclo de vida a menos que usted las active explícitamente.

* * *

## Vendoring / integración {#vendoring--embedding}

Incorpore el binario mediante vendoring con `go:embed` a través de un paso de obtención con `go:generate`, lo que produce un único ejecutable autónomo:

```
//go:generate go run github.com/stackql/stackql-mcp-go/cmd/stackql-mcp-fetch -platform auto -package main
```

El paso de obtención descarga el binario firmado para la plataforma de destino, lo verifica con sha256 y después lo escribe donde `go:embed` puede incorporarlo a su build. En tiempo de ejecución, `StackqlMCPBinary()` devuelve el binario integrado, de modo que no hay descarga en la primera ejecución.

* * *

## Aplicación de demostración {#demo-app}

**sandboxctl** es un asistente de infraestructura: un servidor integrado más un bucle de agente de Claude en un solo binario que planifica, comprueba el costo y aprovisiona entornos sandbox efímeros en la nube tras un control de aprobación.

* * *

## Enlace al repositorio {#repo-link}

[github.com/stackql/stackql-mcp-go](https://github.com/stackql/stackql-mcp-go)
