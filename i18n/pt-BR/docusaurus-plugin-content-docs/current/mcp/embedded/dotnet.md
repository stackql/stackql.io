---
title: .NET / C#
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - incorporado
  - dotnet
  - csharp
description: Incorpore o servidor MCP do StackQL em uma aplicação .NET como sidecar ou como bundle vendored
image: "/img/stackql-featured-image.png"
---

# .NET / C#

Incorpore o servidor MCP do StackQL em uma aplicação .NET, seja como um sidecar baixado, seja incluído no seu build por vendoring como um executável autocontido. O cliente é o SDK oficial do MCP para C#.

:::info
O binário incorporado é baixado e verificado com sha256 na primeira execução, ou incluído na sua aplicação por vendoring. Consulte o [contrato comum](/mcp/embedded#common-contract).
:::

* * *

## Instalação {#install}

```
dotnet add package StackQL.Mcp
```

O pacote opcional `StackQL.Mcp.AgentFramework` adiciona a integração com o Agent Framework. Requer o .NET 8 ou posterior.

* * *

## Início rápido {#quick-start}

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

A API é `StackqlMcp.CreateBuilder()` com `.WithMode()`, `.WithAuth()`, `.WithBinary()`, `.WithBundlePath()`, `.WithApproot()` e `.WithCommand()`, e depois `.StartAsync()`, `.ListToolsAsync()` e `.CallToolAsync()`.

* * *

## Modos {#modes}

O builder usa `StackqlMode.ReadOnly` por padrão. Chame `.WithMode()` com `StackqlMode.Safe`, `StackqlMode.DeleteSafe` ou `StackqlMode.FullAccess` para elevar o modo. A elevação é explícita - o servidor não habilita as ferramentas de mutação nem as de ciclo de vida, a menos que você as ative explicitamente.

* * *

## Vendoring / incorporação {#vendoring--embedding}

Duas formas de obter o binário:

- **Sidecar (padrão)** - o bundle da plataforma é baixado na primeira execução, verificado com sha256, armazenado em cache em `~/.stackql/mcp-server-bin/<version>/<platform-key>/` e iniciado por stdio.
- **Vendored** - incorpore o bundle como recurso de build para gerar um executável autocontido:

```
dotnet build -p:StackqlVendorBundle=/path/to/stackql-mcp-windows-x64.mcpb
```

Você também pode substituir o binário ou o bundle em tempo de execução com as variáveis de ambiente `STACKQL_MCP_BIN` ou `STACKQL_MCP_BUNDLE`.

* * *

## Aplicativo de demonstração {#demo-app}

**driftwatch** é um Worker Service do .NET que incorpora o servidor do StackQL em modo somente leitura e, de forma agendada, executa um conjunto de verificações de desvio em SQL (exposição pública, tags ou licenças ausentes, desvio em relação à linha de base) e publica os resultados em um Adaptive Card do Teams.

* * *

## Link do repositório {#repo-link}

[github.com/stackql/stackql-mcp-dotnet](https://github.com/stackql/stackql-mcp-dotnet)
