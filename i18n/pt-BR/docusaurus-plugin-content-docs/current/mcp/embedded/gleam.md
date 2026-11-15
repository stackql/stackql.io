---
title: Gleam
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - incorporado
  - gleam
  - erlang
  - beam
description: Incorpore o servidor MCP do StackQL em uma aplicação Gleam no destino Erlang/BEAM
image: "/img/stackql-featured-image.png"
---

# Gleam

Incorpore o servidor MCP do StackQL em uma aplicação Gleam no destino Erlang/BEAM. A biblioteca se integra à supervisão do OTP, baixa e verifica o binário assinado e o inicia por stdio.

:::info
O binário incorporado é baixado e verificado com sha256 na primeira execução, ou incluído na sua aplicação por vendoring. Consulte o [contrato comum](/mcp/embedded#common-contract).
:::

* * *

## Instalação {#install}

```
gleam add stackql_mcp
```

Tem como destino apenas o Erlang/BEAM (não o destino JavaScript). O servidor é exposto como um filho OTP por meio de `child_spec()`, para que possa ficar dentro da sua própria árvore de supervisão.

* * *

## Início rápido {#quick-start}

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

A API é composta por `stackql_mcp.start()`, `stop()` e `child_spec()` para o ciclo de vida; `list_tools()` e `call_tool()` para as operações (com as variantes `supervised_list_tools()` e `supervised_call_tool()` para uso sob um supervisor); e `Config`, `default_config()` e `auth_for()` para a configuração.

* * *

## Modos {#modes}

O modo é definido em `Config.mode` e o valor padrão é `read_only`. Defina-o como `safe`, `delete_safe` ou `full_access` para elevar o modo:

```gleam
stackql_mcp.Config(..stackql_mcp.default_config(), mode: stackql_mcp.safe)
```

A elevação é explícita - o servidor não habilita as ferramentas de mutação nem as de ciclo de vida, a menos que você as ative explicitamente.

* * *

## Vendoring / incorporação {#vendoring--embedding}

Na inicialização, a biblioteca baixa o bundle do StackQL da plataforma, o verifica com os hashes sha256 fixados no código e o armazena no cache compartilhado da família. Precedência de resolução:

1. `Config.binary` explícito.
2. Variável de ambiente `STACKQL_MCP_BIN`.
3. Cache compartilhado em `~/.stackql/mcp-server-bin/<version>/<platform-key>/`.

Para implantações offline ou em redes isoladas, defina `STACKQL_MCP_BUNDLE` com o caminho de um bundle local.

* * *

## Aplicativo de demonstração {#demo-app}

**pipewatch** é a demonstração planejada para esta biblioteca. Ela está no roadmap e ainda não foi implementada.

* * *

## Link do repositório {#repo-link}

[github.com/stackql/stackql-mcp-gleam](https://github.com/stackql/stackql-mcp-gleam)
