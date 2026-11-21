---
title: Gleam
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - вбудований
  - gleam
  - erlang
  - beam
description: Вбудуйте сервер MCP StackQL у застосунок Gleam на цільовій платформі Erlang/BEAM
image: "/img/stackql-featured-image.png"
---

# Gleam

Вбудуйте сервер MCP StackQL у застосунок Gleam на цільовій платформі Erlang/BEAM. Бібліотека інтегрується з наглядом OTP, завантажує й перевіряє підписаний двійковий файл і запускає його через stdio.

:::info
Вбудований двійковий файл завантажується й перевіряється за sha256 під час першого запуску або включається у ваш застосунок через вендоринг. Див. [Спільний контракт](/mcp/embedded#common-contract).
:::

* * *

## Встановлення {#install}

```
gleam add stackql_mcp
```

Бібліотека працює лише на цільовій платформі Erlang/BEAM (не на цільовій платформі JavaScript). Сервер доступний як дочірній процес OTP через `child_spec()`, тож його можна розмістити у вашому власному дереві нагляду.

* * *

## Швидкий старт {#quick-start}

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

API складається з `stackql_mcp.start()`, `stop()` і `child_spec()` для керування життєвим циклом; `list_tools()` і `call_tool()` для операцій (з варіантами `supervised_list_tools()` і `supervised_call_tool()` для використання під супервізором); а також `Config`, `default_config()` і `auth_for()` для конфігурації.

* * *

## Режими {#modes}

Режим визначає поле `Config.mode`, а значення за замовчуванням - `read_only`. Щоб підвищити рівень доступу, встановіть значення `safe`, `delete_safe` або `full_access`:

```gleam
stackql_mcp.Config(..stackql_mcp.default_config(), mode: stackql_mcp.safe)
```

Підвищення рівня доступу завжди явне - сервер не вмикає інструменти змінення даних і життєвого циклу, якщо ви явно їх не ввімкнете.

* * *

## Вендоринг / вбудовування {#vendoring--embedding}

Під час запуску бібліотека завантажує пакет StackQL для поточної платформи, перевіряє його за вбудованими в код закріпленими хешами sha256 і кешує у спільному кеші сімейства бібліотек. Пріоритет під час визначення двійкового файлу:

1. Явно вказане значення `Config.binary`.
2. Змінна середовища `STACKQL_MCP_BIN`.
3. Спільний кеш у каталозі `~/.stackql/mcp-server-bin/<version>/<platform-key>/`.

Для розгортань без доступу до мережі або у фізично ізольованих середовищах вкажіть у змінній `STACKQL_MCP_BUNDLE` шлях до локального пакета.

* * *

## Демонстраційний застосунок {#demo-app}

**pipewatch** - запланований демонстраційний застосунок для цієї бібліотеки. Його внесено до плану розвитку, але ще не реалізовано.

* * *

## Посилання на репозиторій {#repo-link}

[github.com/stackql/stackql-mcp-gleam](https://github.com/stackql/stackql-mcp-gleam)
