---
title: .NET / C#
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - integrado
  - dotnet
  - csharp
description: Integre el servidor MCP de StackQL en una aplicación .NET como sidecar o como bundle incorporado mediante vendoring
image: "/img/stackql-featured-image.png"
---

# .NET / C#

Integre el servidor MCP de StackQL en una aplicación .NET, ya sea como un sidecar descargado o incorporado a su build mediante vendoring como ejecutable autónomo. El cliente es el SDK oficial de MCP para C#.

:::info
El binario integrado se descarga y se verifica con sha256 en la primera ejecución, o se incorpora a su aplicación mediante vendoring. Consulte el [contrato común](/mcp/embedded#common-contract).
:::

* * *

## Instalación {#install}

```
dotnet add package StackQL.Mcp
```

Un paquete opcional, `StackQL.Mcp.AgentFramework`, añade la integración con Agent Framework. Requiere .NET 8 o posterior.

* * *

## Inicio rápido {#quick-start}

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

La API es `StackqlMcp.CreateBuilder()` con `.WithMode()`, `.WithAuth()`, `.WithBinary()`, `.WithBundlePath()`, `.WithApproot()` y `.WithCommand()`, y después `.StartAsync()`, `.ListToolsAsync()` y `.CallToolAsync()`.

* * *

## Modos {#modes}

El builder usa `StackqlMode.ReadOnly` de forma predeterminada. Llame a `.WithMode()` con `StackqlMode.Safe`, `StackqlMode.DeleteSafe` o `StackqlMode.FullAccess` para elevar el modo. La elevación de modo es explícita - el servidor no habilita las herramientas de mutación ni las de ciclo de vida a menos que usted las active explícitamente.

* * *

## Vendoring / integración {#vendoring--embedding}

Dos formas de obtener el binario:

- **Sidecar (predeterminado)** - el bundle de la plataforma se descarga en la primera ejecución, se verifica con sha256, se almacena en caché en `~/.stackql/mcp-server-bin/<version>/<platform-key>/` y se inicia sobre stdio.
- **Vendored** - integre el bundle como recurso de build para producir un ejecutable autónomo:

```
dotnet build -p:StackqlVendorBundle=/path/to/stackql-mcp-windows-x64.mcpb
```

También puede reemplazar el binario o el bundle en tiempo de ejecución con las variables de entorno `STACKQL_MCP_BIN` o `STACKQL_MCP_BUNDLE`.

* * *

## Aplicación de demostración {#demo-app}

**driftwatch** es un Worker Service de .NET que integra el servidor de StackQL de solo lectura y, de forma programada, ejecuta un conjunto de comprobaciones de desviación en SQL (exposición pública, tags o licencias que faltan, desviación respecto a la línea de base) y publica los resultados en una Adaptive Card de Teams.

* * *

## Enlace al repositorio {#repo-link}

[github.com/stackql/stackql-mcp-dotnet](https://github.com/stackql/stackql-mcp-dotnet)
