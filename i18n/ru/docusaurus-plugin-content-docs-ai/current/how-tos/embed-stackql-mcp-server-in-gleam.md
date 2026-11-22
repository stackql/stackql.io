---
title: Как встроить MCP-сервер StackQL в приложение Gleam
description: Встройте MCP-сервер StackQL в приложение Gleam для целевой платформы Erlang/BEAM под супервизором OTP, чтобы сервис на BEAM выполнял запросы к облаку на SQL без зависимости от среды выполнения.
keywords: [stackql, mcp, gleam, erlang, beam, otp, дерево супервизоров, встроенный MCP-сервер, model context protocol, агентная инфраструктура, pipewatch]
proficiencyLevel: Intermediate
faq:
  - question: Как добавить встроенный MCP-сервер StackQL в приложение Gleam?
    answer: Выполните `gleam add stackql_mcp`, затем вызовите `stackql_mcp.start(...)` с Config, созданным на основе `stackql_mcp.default_config()`. Библиотека поддерживает только целевую платформу Erlang/BEAM (не JavaScript) и предоставляет сервер как дочерний процесс OTP через child_spec(). При запуске она скачивает подписанный двоичный файл в общий кэш в ~/.stackql/mcp-server-bin/ и проверяет его по sha256.
  - question: Интегрируется ли библиотека Gleam с механизмом супервизоров OTP?
    answer: Да. Сервер предоставляется как дочерний процесс OTP через child_spec(), поэтому его можно разместить в вашем собственном дереве супервизоров, а для вызова инструментов под супервизором есть варианты supervised_list_tools() и supervised_call_tool(). Библиотека поддерживает только среду выполнения Erlang/BEAM.
  - question: Как оставить встроенный MCP-сервер StackQL в режиме только для чтения в Gleam?
    answer: "Режим задается в поле Config.mode, по умолчанию используется режим read_only, который отклоняет INSERT, UPDATE, DELETE и EXEC. Чтобы повысить уровень доступа до safe, delete_safe или full_access, создайте Config с помощью `stackql_mcp.Config(..stackql_mcp.default_config(), mode: stackql_mcp.safe)` - повышение уровня доступа всегда выполняется явно."
  - question: Как библиотека Gleam определяет двоичный файл StackQL?
    answer: Сначала явно заданное поле Config.binary, затем переменная среды STACKQL_MCP_BIN, затем общий кэш в ~/.stackql/mcp-server-bin/. Для развертываний без доступа к сети или в физически изолированных средах укажите в STACKQL_MCP_BUNDLE путь к локальному пакету. Скачанный пакет проверяется по встроенным закрепленным хэшам sha256.
---

# Как встроить MCP-сервер StackQL в приложение Gleam

MCP-сервер StackQL может работать внутри приложения Gleam на целевой платформе Erlang/BEAM без отдельной установки и без зависимости от среды выполнения. Библиотека `stackql_mcp` запускает подписанный двоичный файл StackQL через stdio и интегрируется с механизмом супервизоров OTP, поэтому сервис на BEAM получает контролируемый самоописываемый SQL-интерфейс ко всем провайдерам облачных и SaaS-сервисов в реестре StackQL. Сервер предоставляется как дочерний процесс OTP через `child_spec()`, поэтому его можно разместить в вашем собственном дереве супервизоров.

## Шаги {#steps}

1. **Добавьте библиотеку:**

```
gleam add stackql_mcp
```

Поддерживается только целевая платформа Erlang/BEAM (не JavaScript).

2. **Запустите сервер и выведите список инструментов.** По умолчанию `Config.mode` имеет значение `read_only`:

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

Чтобы разместить сервер под супервизором, используйте `child_spec()`, а для вызовов под супервизором - `supervised_list_tools()` и `supervised_call_tool()`.

3. **Выберите режим.** По умолчанию `Config.mode` имеет значение `read_only`. Чтобы повысить уровень доступа до `safe`, `delete_safe` или `full_access`, создайте конфигурацию `stackql_mcp.Config(..stackql_mcp.default_config(), mode: stackql_mcp.safe)`. Повышение уровня доступа выполняется явно.

## Получение двоичного файла {#binary-sourcing}

При запуске библиотека скачивает пакет StackQL для текущей платформы, проверяет его по встроенным закрепленным хэшам sha256 и кэширует в общем кэше семейства библиотек. Порядок определения двоичного файла: явно заданное поле `Config.binary`, затем `STACKQL_MCP_BIN`, затем общий кэш в каталоге `~/.stackql/mcp-server-bin/`. Для развертываний без доступа к сети или в физически изолированных средах укажите в переменной `STACKQL_MCP_BUNDLE` путь к локальному пакету.

## Демонстрационное приложение {#demo-app}

**pipewatch** - запланированное демонстрационное приложение для этой библиотеки. Оно включено в план развития и пока не реализовано.

## Связанные концепции {#related-concepts}

- [Встроенный MCP: справочник по Gleam](/mcp/embedded/gleam) - установка, API и получение двоичного файла
- [Как использовать StackQL с ИИ-агентами](/ai/how-tos/use-stackql-with-ai-agents) - модель MCP и режимы безопасности
- [Архитектура MCP-сервера StackQL](/ai/architecture/stackql-mcp-architecture) - транспорты, режимы и внутреннее устройство аудита
- [Что такое агентная инфраструктура?](/ai/canonical-definitions/what-is-agentic-infrastructure) - подход, который это делает возможным
