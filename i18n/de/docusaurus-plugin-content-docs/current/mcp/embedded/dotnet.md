---
title: .NET / C#
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - eingebettet
  - dotnet
  - csharp
description: Betten Sie den StackQL-MCP-Server als Sidecar oder per Vendoring eingebundenes Bundle in eine .NET-Anwendung ein
image: "/img/stackql-featured-image.png"
---

# .NET / C#

Betten Sie den StackQL-MCP-Server in eine .NET-Anwendung ein, entweder als heruntergeladenes Sidecar oder per Vendoring in Ihren Build eingebunden als eigenständige ausführbare Datei. Der Client ist das offizielle C# MCP SDK.

:::info
Die eingebettete Binärdatei wird beim ersten Start heruntergeladen und per sha256 verifiziert oder per Vendoring in Ihre App eingebunden. Siehe den [gemeinsamen Vertrag](/mcp/embedded#common-contract).
:::

* * *

## Installation {#install}

```
dotnet add package StackQL.Mcp
```

Ein optionales Paket `StackQL.Mcp.AgentFramework` ergänzt die Integration mit dem Agent Framework. Erfordert .NET 8 oder höher.

* * *

## Schnellstart {#quick-start}

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

Die API besteht aus `StackqlMcp.CreateBuilder()` mit `.WithMode()`, `.WithAuth()`, `.WithBinary()`, `.WithBundlePath()`, `.WithApproot()` und `.WithCommand()`, gefolgt von `.StartAsync()`, `.ListToolsAsync()` und `.CallToolAsync()`.

* * *

## Modi {#modes}

Der Builder verwendet standardmäßig `StackqlMode.ReadOnly`. Rufen Sie `.WithMode()` mit `StackqlMode.Safe`, `StackqlMode.DeleteSafe` oder `StackqlMode.FullAccess` auf, um zu eskalieren. Die Eskalation ist explizit - der Server aktiviert keine Mutations- oder Lifecycle-Tools, sofern Sie sich nicht ausdrücklich dafür entscheiden.

* * *

## Vendoring / Einbetten {#vendoring--embedding}

Es gibt zwei Wege, die Binärdatei zu beziehen:

- **Sidecar (Standard)** - das Plattform-Bundle wird beim ersten Start heruntergeladen, per sha256 verifiziert, unter `~/.stackql/mcp-server-bin/<version>/<platform-key>/` zwischengespeichert und über stdio gestartet.
- **Vendored** - betten Sie das Bundle als Build-Ressource ein, um eine eigenständige ausführbare Datei zu erzeugen:

```
dotnet build -p:StackqlVendorBundle=/path/to/stackql-mcp-windows-x64.mcpb
```

Sie können die Binärdatei oder das Bundle zur Laufzeit auch mit den Umgebungsvariablen `STACKQL_MCP_BIN` oder `STACKQL_MCP_BUNDLE` überschreiben.

* * *

## Demo-App {#demo-app}

**driftwatch** ist ein .NET Worker Service, der den schreibgeschützten StackQL-Server einbettet, nach Zeitplan eine Suite von SQL-Drift-Prüfungen ausführt (öffentliche Erreichbarkeit, fehlende Tags oder Lizenzen, Baseline-Drift) und die Ergebnisse als Teams Adaptive Card sendet.

* * *

## Repository-Link {#repo-link}

[github.com/stackql/stackql-mcp-dotnet](https://github.com/stackql/stackql-mcp-dotnet)
