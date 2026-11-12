---
title: .NET / C#
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - embarqué
  - dotnet
  - csharp
description: Embarquez le serveur MCP StackQL dans une application .NET sous forme de sidecar ou de bundle vendored
image: "/img/stackql-featured-image.png"
---

# .NET / C#

Embarquez le serveur MCP StackQL dans une application .NET, soit sous forme de sidecar téléchargé, soit vendored dans votre build pour obtenir un exécutable autonome. Le client est le SDK MCP officiel pour C#.

:::info
Le binaire embarqué est téléchargé et vérifié par sha256 à la première exécution, ou vendored dans votre application. Consultez le [contrat commun](/mcp/embedded#common-contract).
:::

* * *

## Installation {#install}

```
dotnet add package StackQL.Mcp
```

Un paquet facultatif `StackQL.Mcp.AgentFramework` ajoute l'intégration avec Agent Framework. Nécessite .NET 8 ou une version ultérieure.

* * *

## Démarrage rapide {#quick-start}

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

L'API se compose de `StackqlMcp.CreateBuilder()` avec `.WithMode()`, `.WithAuth()`, `.WithBinary()`, `.WithBundlePath()`, `.WithApproot()` et `.WithCommand()`, puis de `.StartAsync()`, `.ListToolsAsync()` et `.CallToolAsync()`.

* * *

## Modes {#modes}

Le builder utilise `StackqlMode.ReadOnly` par défaut. Appelez `.WithMode()` avec `StackqlMode.Safe`, `StackqlMode.DeleteSafe` ou `StackqlMode.FullAccess` pour élever le niveau d'accès. L'élévation est explicite - le serveur n'active les outils de mutation ou de cycle de vie que si vous les activez explicitement.

* * *

## Vendoring / intégration {#vendoring--embedding}

Deux façons d'obtenir le binaire :

- **Sidecar (par défaut)** - le bundle de la plateforme est téléchargé à la première exécution, vérifié par sha256, mis en cache dans `~/.stackql/mcp-server-bin/<version>/<platform-key>/` et lancé via stdio.
- **Vendored** - embarquez le bundle comme ressource de build pour produire un exécutable autonome :

```
dotnet build -p:StackqlVendorBundle=/path/to/stackql-mcp-windows-x64.mcpb
```

Vous pouvez aussi remplacer le binaire ou le bundle à l'exécution avec les variables d'environnement `STACKQL_MCP_BIN` ou `STACKQL_MCP_BUNDLE`.

* * *

## Application de démonstration {#demo-app}

**driftwatch** est un Worker Service .NET qui embarque le serveur StackQL en lecture seule et qui, selon une planification, exécute une suite de vérifications de dérive en SQL (exposition publique, tags ou licences manquants, dérive par rapport à la référence) et publie les résultats dans une Adaptive Card Teams.

* * *

## Lien du dépôt {#repo-link}

[github.com/stackql/stackql-mcp-dotnet](https://github.com/stackql/stackql-mcp-dotnet)
