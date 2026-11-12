---
title: Gleam
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - embarqué
  - gleam
  - erlang
  - beam
description: Embarquez le serveur MCP StackQL dans une application Gleam sur la cible Erlang/BEAM
image: "/img/stackql-featured-image.png"
---

# Gleam

Embarquez le serveur MCP StackQL dans une application Gleam sur la cible Erlang/BEAM. La bibliothèque s'intègre à la supervision OTP, télécharge et vérifie le binaire signé, puis le lance via stdio.

:::info
Le binaire embarqué est téléchargé et vérifié par sha256 à la première exécution, ou vendored dans votre application. Consultez le [contrat commun](/mcp/embedded#common-contract).
:::

* * *

## Installation {#install}

```
gleam add stackql_mcp
```

Cible uniquement Erlang/BEAM (pas la cible JavaScript). Le serveur est exposé comme un processus enfant OTP via `child_spec()`, de sorte qu'il peut prendre place dans votre propre arbre de supervision.

* * *

## Démarrage rapide {#quick-start}

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

L'API se compose de `stackql_mcp.start()`, `stop()` et `child_spec()` pour le cycle de vie ; de `list_tools()` et `call_tool()` pour les opérations (avec les variantes `supervised_list_tools()` et `supervised_call_tool()` à utiliser sous un superviseur) ; et de `Config`, `default_config()` et `auth_for()` pour la configuration.

* * *

## Modes {#modes}

Le mode se définit dans `Config.mode` et vaut `read_only` par défaut. Définissez-le sur `safe`, `delete_safe` ou `full_access` pour élever le niveau d'accès :

```gleam
stackql_mcp.Config(..stackql_mcp.default_config(), mode: stackql_mcp.safe)
```

L'élévation est explicite - le serveur n'active les outils de mutation ou de cycle de vie que si vous les activez explicitement.

* * *

## Vendoring / intégration {#vendoring--embedding}

Au démarrage, la bibliothèque télécharge le bundle StackQL de la plateforme, le vérifie par rapport aux empreintes sha256 épinglées qu'elle embarque, puis le met en cache dans le cache partagé de la famille de bibliothèques. Ordre de priorité de la résolution :

1. `Config.binary` explicite.
2. Variable d'environnement `STACKQL_MCP_BIN`.
3. Cache partagé dans `~/.stackql/mcp-server-bin/<version>/<platform-key>/`.

Pour les déploiements hors ligne ou isolés du réseau, définissez `STACKQL_MCP_BUNDLE` pour pointer vers un bundle local.

* * *

## Application de démonstration {#demo-app}

**pipewatch** est la démonstration prévue pour cette bibliothèque. Elle figure sur la feuille de route et n'est pas encore implémentée.

* * *

## Lien du dépôt {#repo-link}

[github.com/stackql/stackql-mcp-gleam](https://github.com/stackql/stackql-mcp-gleam)
