---
title: Gleam
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - integrado
  - gleam
  - erlang
  - beam
description: Integre el servidor MCP de StackQL en una aplicación Gleam en el destino Erlang/BEAM
image: "/img/stackql-featured-image.png"
---

# Gleam

Integre el servidor MCP de StackQL en una aplicación Gleam en el destino Erlang/BEAM. La biblioteca se integra con la supervisión de OTP, descarga y verifica el binario firmado y lo inicia sobre stdio.

:::info
El binario integrado se descarga y se verifica con sha256 en la primera ejecución, o se incorpora a su aplicación mediante vendoring. Consulte el [contrato común](/mcp/embedded#common-contract).
:::

* * *

## Instalación {#install}

```
gleam add stackql_mcp
```

Está dirigida solo a Erlang/BEAM (no al destino JavaScript). El servidor se expone como un hijo de OTP mediante `child_spec()`, de modo que puede formar parte de su propio árbol de supervisión.

* * *

## Inicio rápido {#quick-start}

```gleam
import gleam/io
import gleam/int
import gleam/list
import envoy
import stackql_mcp

pub fn main() {
  let config =
    stackql_mcp.Config(
      ..stackql_mcp.default_config(),
      auth: [stackql_mcp.auth_for("github", "null_auth")],
    )

  let assert Ok(server) =
    stackql_mcp.start(
      config: config,
      home: "/home/u",
      os: "linux",
      arch: "x86_64",
      getenv: envoy.get,
    )

  let assert Ok(tools) = stackql_mcp.list_tools(server)
  io.println(int.to_string(list.length(tools)) <> " tools available")

  stackql_mcp.stop(server)
}
```

La API es `stackql_mcp.start()`, `stop()` y `child_spec()` para el ciclo de vida; `list_tools()` y `call_tool()` para las operaciones (con las variantes `supervised_list_tools()` y `supervised_call_tool()` para usarlas bajo un supervisor); y `Config`, `default_config()` y `auth_for()` para la configuración.

* * *

## Modos {#modes}

El modo se establece en `Config.mode` y su valor predeterminado es `read_only`. Establézcalo en `safe`, `delete_safe` o `full_access` para elevar el modo:

```gleam
stackql_mcp.Config(..stackql_mcp.default_config(), mode: stackql_mcp.safe)
```

La elevación de modo es explícita - el servidor no habilita las herramientas de mutación ni las de ciclo de vida a menos que usted las active explícitamente.

* * *

## Vendoring / integración {#vendoring--embedding}

Al iniciarse, la biblioteca descarga el bundle de StackQL de la plataforma, lo verifica con los hashes sha256 fijados que lleva incorporados y lo almacena en la caché compartida de la familia. Precedencia de resolución:

1. `Config.binary` explícito.
2. Variable de entorno `STACKQL_MCP_BIN`.
3. Caché compartida en `~/.stackql/mcp-server-bin/<version>/<platform-key>/`.

Para despliegues sin conexión o en entornos aislados, establezca `STACKQL_MCP_BUNDLE` para que apunte a un bundle local.

* * *

## Aplicación de demostración {#demo-app}

**pipewatch** es la demostración prevista para esta biblioteca. Figura en la hoja de ruta y aún no está implementada.

* * *

## Enlace al repositorio {#repo-link}

[github.com/stackql/stackql-mcp-gleam](https://github.com/stackql/stackql-mcp-gleam)
