---
title: .NET / C#
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - встроенный
  - dotnet
  - csharp
description: Встройте MCP-сервер StackQL в приложение .NET в виде sidecar или пакета, включенного через вендоринг
image: "/img/stackql-featured-image.png"
---

# .NET / C#

Встройте MCP-сервер StackQL в приложение .NET: либо как скачиваемый sidecar, либо через вендоринг в вашу сборку в виде автономного исполняемого файла. Клиент - официальный MCP SDK для C#.

:::info
Встроенный двоичный файл скачивается и проверяется по sha256 при первом запуске либо включается в ваше приложение через вендоринг. См. [Общий контракт](/mcp/embedded#common-contract).
:::

* * *

## Установка {#install}

```
dotnet add package StackQL.Mcp
```

Необязательный пакет `StackQL.Mcp.AgentFramework` добавляет интеграцию с Agent Framework. Требуется .NET 8 или более поздней версии.

* * *

## Быстрый старт {#quick-start}

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

API состоит из `StackqlMcp.CreateBuilder()` с `.WithMode()`, `.WithAuth()`, `.WithBinary()`, `.WithBundlePath()`, `.WithApproot()` и `.WithCommand()`, а затем `.StartAsync()`, `.ListToolsAsync()` и `.CallToolAsync()`.

* * *

## Режимы {#modes}

По умолчанию построитель использует `StackqlMode.ReadOnly`. Чтобы повысить уровень доступа, вызовите `.WithMode()` с `StackqlMode.Safe`, `StackqlMode.DeleteSafe` или `StackqlMode.FullAccess`. Повышение уровня доступа выполняется явно - сервер не включит инструменты изменения данных или операций жизненного цикла, пока вы явно не включите их.

* * *

## Вендоринг / встраивание {#vendoring--embedding}

Есть два способа получить двоичный файл:

- **Sidecar (по умолчанию)** - пакет для платформы скачивается при первом запуске, проверяется по sha256, кэшируется в `~/.stackql/mcp-server-bin/<version>/<platform-key>/` и запускается через stdio.
- **Вендоринг** - встройте пакет как ресурс сборки, чтобы получить автономный исполняемый файл:

```
dotnet build -p:StackqlVendorBundle=/path/to/stackql-mcp-windows-x64.mcpb
```

Двоичный файл или пакет также можно переопределить во время выполнения с помощью переменных среды `STACKQL_MCP_BIN` или `STACKQL_MCP_BUNDLE`.

* * *

## Демонстрационное приложение {#demo-app}

**driftwatch** - это приложение .NET Worker Service, которое встраивает сервер StackQL в режиме только для чтения, по расписанию выполняет набор SQL-проверок дрейфа (публичная доступность, отсутствующие теги или лицензии, дрейф относительно базового состояния) и публикует результаты в Teams в виде Adaptive Card.

* * *

## Ссылка на репозиторий {#repo-link}

[github.com/stackql/stackql-mcp-dotnet](https://github.com/stackql/stackql-mcp-dotnet)
