---
title: Swift
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - integrado
  - swift
  - macos
description: Incluya el binario notarizado del servidor MCP de StackQL dentro de una aplicación de macOS firmada
image: "/img/stackql-featured-image.png"
---

# Swift

Integre el servidor MCP de StackQL en una aplicación de macOS. El binario firmado y notarizado se puede incluir dentro de su `.app`, y el cliente es el SDK oficial de MCP para Swift.

:::info
El binario integrado se descarga y se verifica con sha256 en la primera ejecución, o se incorpora a su aplicación mediante vendoring. Consulte el [contrato común](/mcp/embedded#common-contract).
:::

* * *

## Instalación {#install}

Agregue el paquete a su `Package.swift`:

```swift
.package(url: "https://github.com/stackql/stackql-mcp-swift.git", from: "0.1.0")
```

Requiere macOS 13+ y Swift 6.1 (Xcode 16.3+). La única dependencia es el SDK oficial de MCP para Swift.

* * *

## Inicio rápido {#quick-start}

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

La API es `StackQLServer.start(Options)`, `.listToolNames()`, `.call(_:_:)` y `.stop()`. `Options` contiene `mode`, `auth`, `binaryOverride` y `allowDownload`. Para harnesses externos, `StackQLServer.resolveCommand(_:)` devuelve el comando de inicio resuelto.

* * *

## Modos {#modes}

`Options.mode` usa `.readOnly` de forma predeterminada. Establézcalo en `.safe`, `.deleteSafe` o `.fullAccess` para elevar el modo. La elevación de modo es explícita - el servidor no habilita las herramientas de mutación ni las de ciclo de vida a menos que usted las active explícitamente.

* * *

## Vendoring / integración {#vendoring--embedding}

El binario `darwin-universal` está firmado con Developer ID y notarizado por Apple, por lo que se puede incluir dentro de una `.app` de macOS firmada manteniendo válida la notarización de la propia aplicación. Esto es exclusivo de la biblioteca de Swift - usted distribuye una única aplicación autónoma y notarizada, sin descarga en la primera ejecución.

Orden de resolución del binario:

1. `binaryOverride` explícito.
2. Recursos incluidos en la `.app`.
3. Caché compartida en `~/.stackql/mcp-server-bin/`.
4. Descarga con verificación sha256 (cuando `allowDownload` está establecido).

* * *

## Aplicación de demostración {#demo-app}

**CloudLens** es un centinela de la nube para la barra de menús que integra el binario notarizado y ejecuta un conjunto de comprobaciones `read_only`.

* * *

## Enlace al repositorio {#repo-link}

[github.com/stackql/stackql-mcp-swift](https://github.com/stackql/stackql-mcp-swift)
