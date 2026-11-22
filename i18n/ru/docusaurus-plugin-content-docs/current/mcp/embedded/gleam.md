---
title: Gleam
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - встроенный
  - gleam
  - erlang
  - beam
description: Встройте MCP-сервер StackQL в приложение Gleam для целевой платформы Erlang/BEAM
image: "/img/stackql-featured-image.png"
---

# Gleam

Встройте MCP-сервер StackQL в приложение Gleam для целевой платформы Erlang/BEAM. Библиотека интегрируется с механизмом супервизоров OTP, скачивает и проверяет подписанный двоичный файл и запускает его через stdio.

:::info
Встроенный двоичный файл скачивается и проверяется по sha256 при первом запуске либо включается в ваше приложение через вендоринг. См. [Общий контракт](/mcp/embedded#common-contract).
:::

* * *

## Установка {#install}

```
gleam add stackql_mcp
```

Поддерживается только целевая платформа Erlang/BEAM (не JavaScript). Сервер предоставляется как дочерний процесс OTP через `child_spec()`, поэтому его можно разместить в вашем собственном дереве супервизоров.

* * *

## Быстрый старт {#quick-start}

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

API включает `stackql_mcp.start()`, `stop()` и `child_spec()` для управления жизненным циклом; `list_tools()` и `call_tool()` для операций (с вариантами `supervised_list_tools()` и `supervised_call_tool()` для использования под супервизором); а также `Config`, `default_config()` и `auth_for()` для конфигурации.

* * *

## Режимы {#modes}

Режим задается в поле `Config.mode`, по умолчанию используется `read_only`. Чтобы повысить уровень доступа, задайте значение `safe`, `delete_safe` или `full_access`:

```gleam
stackql_mcp.Config(..stackql_mcp.default_config(), mode: stackql_mcp.safe)
```

Повышение уровня доступа выполняется явно - сервер не включит инструменты изменения данных и жизненного цикла, пока вы сами их не включите.

* * *

## Вендоринг / встраивание {#vendoring--embedding}

При запуске библиотека скачивает пакет StackQL для текущей платформы, проверяет его по встроенным закрепленным хэшам sha256 и кэширует в общем кэше семейства библиотек. Порядок определения двоичного файла:

1. Явно заданное поле `Config.binary`.
2. Переменная среды `STACKQL_MCP_BIN`.
3. Общий кэш в каталоге `~/.stackql/mcp-server-bin/<version>/<platform-key>/`.

Для развертываний без доступа к сети или в физически изолированных средах укажите в переменной `STACKQL_MCP_BUNDLE` путь к локальному пакету.

* * *

## Демонстрационное приложение {#demo-app}

**pipewatch** - запланированное демонстрационное приложение для этой библиотеки. Оно включено в план развития и пока не реализовано.

* * *

## Ссылка на репозиторий {#repo-link}

[github.com/stackql/stackql-mcp-gleam](https://github.com/stackql/stackql-mcp-gleam)
