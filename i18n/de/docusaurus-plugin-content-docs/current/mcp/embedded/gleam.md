---
title: Gleam
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - eingebettet
  - gleam
  - erlang
  - beam
description: Betten Sie den StackQL-MCP-Server in eine Gleam-Anwendung auf dem Erlang/BEAM-Target ein
image: "/img/stackql-featured-image.png"
---

# Gleam

Betten Sie den StackQL-MCP-Server in eine Gleam-Anwendung auf dem Erlang/BEAM-Target ein. Die Bibliothek integriert sich in die OTP-Supervision, lädt die signierte Binärdatei herunter, verifiziert sie und startet sie über stdio.

:::info
Die eingebettete Binärdatei wird beim ersten Start heruntergeladen und per sha256 verifiziert oder per Vendoring in Ihre App eingebunden. Siehe den [gemeinsamen Vertrag](/mcp/embedded#common-contract).
:::

* * *

## Installation {#install}

```
gleam add stackql_mcp
```

Unterstützt nur das Erlang/BEAM-Target (nicht das JavaScript-Target). Der Server wird über `child_spec()` als OTP-Child bereitgestellt, sodass er in Ihrem eigenen Supervision-Tree laufen kann.

* * *

## Schnellstart {#quick-start}

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

Die API besteht aus `stackql_mcp.start()`, `stop()` und `child_spec()` für den Lebenszyklus, `list_tools()` und `call_tool()` für Operationen (mit den Varianten `supervised_list_tools()` und `supervised_call_tool()` für den Einsatz unter einem Supervisor) sowie `Config`, `default_config()` und `auth_for()` für die Konfiguration.

* * *

## Modi {#modes}

Der Modus wird über `Config.mode` gesetzt und ist standardmäßig `read_only`. Setzen Sie ihn auf `safe`, `delete_safe` oder `full_access`, um zu eskalieren:

```gleam
stackql_mcp.Config(..stackql_mcp.default_config(), mode: stackql_mcp.safe)
```

Die Eskalation ist explizit - der Server aktiviert keine Mutations- oder Lifecycle-Tools, sofern Sie sich nicht ausdrücklich dafür entscheiden.

* * *

## Vendoring / Einbetten {#vendoring--embedding}

Beim Start lädt die Bibliothek das StackQL-Bundle für die Plattform herunter, verifiziert es gegen eingebaute sha256-Pins und legt es im gemeinsamen Cache der Bibliotheksfamilie ab. Auflösungsreihenfolge:

1. Explizites `Config.binary`.
2. Umgebungsvariable `STACKQL_MCP_BIN`.
3. Gemeinsamer Cache unter `~/.stackql/mcp-server-bin/<version>/<platform-key>/`.

Für Offline- oder Air-Gapped-Deployments setzen Sie `STACKQL_MCP_BUNDLE` auf ein lokales Bundle.

* * *

## Demo-App {#demo-app}

**pipewatch** ist die geplante Demo für diese Bibliothek. Sie steht auf der Roadmap und ist noch nicht implementiert.

* * *

## Repository-Link {#repo-link}

[github.com/stackql/stackql-mcp-gleam](https://github.com/stackql/stackql-mcp-gleam)
