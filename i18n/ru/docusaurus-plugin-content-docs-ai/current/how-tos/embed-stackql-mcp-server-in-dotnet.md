---
title: Как встроить MCP-сервер StackQL в приложение .NET
description: Встройте MCP-сервер StackQL в приложение .NET / C# в виде sidecar или пакета через вендоринг, чтобы рабочая служба или агент выполняли запросы к облаку и проверки дрейфа на SQL без зависимости от среды выполнения.
keywords: [stackql, mcp, dotnet, csharp, .net, nuget, встроенный MCP-сервер, model context protocol, mcp sdk для c#, modelcontextprotocol.core, рабочая служба, driftwatch]
proficiencyLevel: Intermediate
faq:
  - question: Как добавить встроенный MCP-сервер StackQL в приложение .NET?
    answer: Выполните `dotnet add package StackQL.Mcp`, затем вызовите `await StackqlMcp.CreateBuilder().WithMode(StackqlMode.ReadOnly).StartAsync()`. Клиент - официальный MCP SDK для C#. По умолчанию при первом запуске пакет для платформы скачивается, проверяется по sha256 и кэшируется в ~/.stackql/mcp-server-bin/. Требуется .NET 8 или более поздней версии.
  - question: Можно ли поставлять автономный исполняемый файл .NET со встроенным StackQL?
    answer: Да. Включите пакет через вендоринг как ресурс сборки с помощью `dotnet build -p:StackqlVendorBundle=/path/to/stackql-mcp-windows-x64.mcpb`, который встраивает двоичный файл в автономный исполняемый файл. Двоичный файл или пакет также можно переопределить во время выполнения с помощью переменных среды STACKQL_MCP_BIN или STACKQL_MCP_BUNDLE.
  - question: Какая версия .NET нужна библиотеке StackQL MCP?
    answer: .NET 8 или более поздняя. Поставляемые пакеты StackQL.Mcp и StackQL.Mcp.AgentFramework ориентированы на несколько целевых платформ (net8.0 и net9.0), а минимальную версию .NET 8 задает официальный MCP SDK для C#, ModelContextProtocol.Core.
  - question: Как оставить встроенный MCP-сервер StackQL в режиме только для чтения в .NET?
    answer: Вызовите .WithMode(StackqlMode.ReadOnly) у построителя; это значение по умолчанию. ReadOnly отклоняет INSERT, UPDATE, DELETE и EXEC. Повышайте уровень доступа до StackqlMode.Safe, StackqlMode.DeleteSafe или StackqlMode.FullAccess, только когда приложению нужно создавать ресурсы - повышение уровня доступа всегда выполняется явно.
---

# Как встроить MCP-сервер StackQL в приложение .NET

MCP-сервер StackQL может работать внутри приложения .NET без отдельной установки и без зависимости от среды выполнения. Пакет `StackQL.Mcp` запускает подписанный двоичный файл StackQL через stdio и работает с ним через клиент официального MCP SDK для C#, поэтому рабочая служба или агент получает контролируемый самоописываемый SQL-интерфейс ко всем провайдерам облачных и SaaS-сервисов в реестре StackQL. На этой схеме основан **driftwatch** - рабочая служба, которая выполняет проверки дрейфа по расписанию.

## Шаги {#steps}

1. **Добавьте пакет:**

```
dotnet add package StackQL.Mcp
```

Необязательный пакет `StackQL.Mcp.AgentFramework` добавляет интеграцию с Agent Framework. Требуется .NET 8 или более поздней версии (пакеты ориентированы на несколько целевых платформ: `net8.0` и `net9.0`).

2. **Запустите сервер и вызовите инструмент.** По умолчанию построитель использует `StackqlMode.ReadOnly`:

```csharp
using StackQL.Mcp;

await using var server = await StackqlMcp.CreateBuilder()
    .WithMode(StackqlMode.ReadOnly)
    .WithAuth("github", "null_auth")
    .StartAsync();

var tools = await server.ListToolsAsync();
var result = await server.CallToolAsync("list_services",
    new() { ["provider"] = "github", ["row_limit"] = 5 });
```

Построитель также предоставляет методы `.WithBinary()`, `.WithBundlePath()`, `.WithApproot()` и `.WithCommand()`.

3. **Выберите режим.** Метод `.WithMode()` по умолчанию использует `StackqlMode.ReadOnly`. Задавайте `StackqlMode.Safe`, `StackqlMode.DeleteSafe` или `StackqlMode.FullAccess`, только если приложению нужно создавать ресурсы. Повышение уровня доступа выполняется явно.

## Поставка автономного исполняемого файла {#ship-a-self-contained-executable}

Есть два способа получить двоичный файл:

- **Sidecar (по умолчанию)** - пакет для платформы скачивается при первом запуске, проверяется по sha256, кэшируется в `~/.stackql/mcp-server-bin/<version>/<platform-key>/` и запускается через stdio.
- **Вендоринг** - встройте пакет как ресурс сборки:

```
dotnet build -p:StackqlVendorBundle=/path/to/stackql-mcp-windows-x64.mcpb
```

Двоичный файл или пакет можно переопределить во время выполнения с помощью переменных среды `STACKQL_MCP_BIN` или `STACKQL_MCP_BUNDLE`.

## Демонстрационное приложение {#demo-app}

**driftwatch** - это приложение .NET Worker Service, которое встраивает сервер StackQL в режиме только для чтения, по расписанию выполняет набор SQL-проверок дрейфа (публичная доступность, отсутствующие теги или лицензии, дрейф относительно базового состояния) и публикует результаты в Teams в виде Adaptive Card.

## Связанные концепции {#related-concepts}

- [Встроенный MCP: справочник по .NET / C#](/mcp/embedded/dotnet) - установка, API и вендоринг
- [Как использовать StackQL с ИИ-агентами](/ai/how-tos/use-stackql-with-ai-agents) - модель MCP и режимы безопасности
- [Архитектура MCP-сервера StackQL](/ai/architecture/stackql-mcp-architecture) - транспорты, режимы и внутреннее устройство аудита
- [Что такое агентная инфраструктура?](/ai/canonical-definitions/what-is-agentic-infrastructure) - подход, который это делает возможным
