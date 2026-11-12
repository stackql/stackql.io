---
title: Swift
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - embarqué
  - swift
  - macos
description: Incluez le binaire notarisé du serveur MCP StackQL dans une application macOS signée
image: "/img/stackql-featured-image.png"
---

# Swift

Embarquez le serveur MCP StackQL dans une application macOS. Le binaire signé et notarisé peut être inclus dans votre `.app`, et le client est le SDK MCP officiel pour Swift.

:::info
Le binaire embarqué est téléchargé et vérifié par sha256 à la première exécution, ou vendored dans votre application. Consultez le [contrat commun](/mcp/embedded#common-contract).
:::

* * *

## Installation {#install}

Ajoutez le paquet à votre `Package.swift` :

```swift
.package(url: "https://github.com/stackql/stackql-mcp-swift.git", from: "0.1.0")
```

Nécessite macOS 13+ et Swift 6.1 (Xcode 16.3+). La seule dépendance est le SDK MCP officiel pour Swift.

* * *

## Démarrage rapide {#quick-start}

```swift
import StackQLMCP

var options = Options()
options.mode = .readOnly
options.auth = ["github": ["type": "null_auth"]]

let server = try await StackQLServer.start(options)
let tools = try await server.listToolNames()
print("\(tools.count) tools available")

let services = try await server.call(
    "list_services", ["provider": "github", "row_limit": 5])
print(services.text)

await server.stop()
```

L'API se compose de `StackQLServer.start(Options)`, `.listToolNames()`, `.call(_:_:)` et `.stop()`. `Options` contient `mode`, `auth`, `binaryOverride` et `allowDownload`. Pour les harnais externes, `StackQLServer.resolveCommand(_:)` renvoie la commande de lancement résolue.

* * *

## Modes {#modes}

`Options.mode` vaut `.readOnly` par défaut. Définissez-le sur `.safe`, `.deleteSafe` ou `.fullAccess` pour élever le niveau d'accès. L'élévation est explicite - le serveur n'active les outils de mutation ou de cycle de vie que si vous les activez explicitement.

* * *

## Vendoring / intégration {#vendoring--embedding}

Le binaire `darwin-universal` est signé avec un Developer ID et notarisé par Apple ; il peut donc être inclus dans une `.app` macOS signée tout en préservant la validité de la notarisation de l'application elle-même. C'est propre à la bibliothèque Swift - vous livrez une seule application autonome et notarisée, sans téléchargement à la première exécution.

Ordre de résolution du binaire :

1. `binaryOverride` explicite.
2. Ressources incluses dans la `.app`.
3. Cache partagé dans `~/.stackql/mcp-server-bin/`.
4. Téléchargement avec vérification sha256 (lorsque `allowDownload` est défini).

* * *

## Application de démonstration {#demo-app}

**CloudLens** est une sentinelle cloud pour la barre de menus qui embarque le binaire notarisé et exécute une suite de vérifications en mode `read_only`.

* * *

## Lien du dépôt {#repo-link}

[github.com/stackql/stackql-mcp-swift](https://github.com/stackql/stackql-mcp-swift)
