---
title: Swift
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - incorporado
  - swift
  - macos
description: Inclua o binário notarizado do servidor MCP do StackQL em um aplicativo assinado para macOS
image: "/img/stackql-featured-image.png"
---

# Swift

Incorpore o servidor MCP do StackQL em um aplicativo para macOS. O binário assinado e notarizado pode ser incluído no seu `.app`, e o cliente é o SDK oficial do MCP para Swift.

:::info
O binário incorporado é baixado e verificado com sha256 na primeira execução, ou incluído no seu aplicativo por vendoring. Consulte o [contrato comum](/mcp/embedded#common-contract).
:::

* * *

## Instalação {#install}

Adicione o pacote ao seu `Package.swift`:

```swift
.package(url: "https://github.com/stackql/stackql-mcp-swift.git", from: "0.1.0")
```

Requer o macOS 13+ e o Swift 6.1 (Xcode 16.3+). A única dependência é o SDK oficial do MCP para Swift.

* * *

## Início rápido {#quick-start}

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

A API é composta por `StackQLServer.start(Options)`, `.listToolNames()`, `.call(_:_:)` e `.stop()`. `Options` contém `mode`, `auth`, `binaryOverride` e `allowDownload`. Para harnesses externos, `StackQLServer.resolveCommand(_:)` retorna o comando de inicialização resolvido.

* * *

## Modos {#modes}

O valor padrão de `Options.mode` é `.readOnly`. Defina-o como `.safe`, `.deleteSafe` ou `.fullAccess` para elevar o modo. A elevação é explícita - o servidor não habilita as ferramentas de mutação nem as de ciclo de vida, a menos que você as ative explicitamente.

* * *

## Vendoring / incorporação {#vendoring--embedding}

O binário `darwin-universal` é assinado com Developer ID e notarizado pela Apple, então pode ser incluído em um `.app` assinado para macOS sem invalidar a notarização do próprio aplicativo. Isso é exclusivo da biblioteca Swift - você distribui um único aplicativo autocontido e notarizado, sem download na primeira execução.

Ordem de resolução do binário:

1. `binaryOverride` explícito.
2. Recursos incluídos no `.app`.
3. Cache compartilhado em `~/.stackql/mcp-server-bin/`.
4. Download com verificação sha256 (quando `allowDownload` está definido).

* * *

## Aplicativo de demonstração {#demo-app}

**CloudLens** é uma sentinela da nuvem na barra de menus que incorpora o binário notarizado e executa um conjunto de verificações `read_only`.

* * *

## Link do repositório {#repo-link}

[github.com/stackql/stackql-mcp-swift](https://github.com/stackql/stackql-mcp-swift)
