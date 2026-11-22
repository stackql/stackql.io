---
title: .NET / C#
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - вбудований
  - dotnet
  - csharp
description: Вбудуйте сервер MCP StackQL у застосунок .NET як sidecar або як пакет, включений через вендоринг
image: "/img/stackql-featured-image.png"
---

# .NET / C#

Вбудуйте сервер MCP StackQL у застосунок .NET: або як завантажений sidecar, або через вендоринг у вашу збірку як автономний виконуваний файл. Клієнт - офіційний SDK MCP для C#.

:::info
Вбудований двійковий файл завантажується й перевіряється за sha256 під час першого запуску або включається у ваш застосунок через вендоринг. Див. [спільний контракт](/mcp/embedded#common-contract).
:::

* * *

## Встановлення {#install}

```
dotnet add package StackQL.Mcp
```

Необов'язковий пакет `StackQL.Mcp.AgentFramework` додає інтеграцію з Agent Framework. Потрібен .NET 8 або новіша версія.

* * *

## Швидкий старт {#quick-start}

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

API складається з `StackqlMcp.CreateBuilder()` з методами `.WithMode()`, `.WithAuth()`, `.WithBinary()`, `.WithBundlePath()`, `.WithApproot()` і `.WithCommand()`, а потім `.StartAsync()`, `.ListToolsAsync()` і `.CallToolAsync()`.

* * *

## Режими {#modes}

За замовчуванням будівельник використовує `StackqlMode.ReadOnly`. Щоб підвищити рівень доступу, викличте `.WithMode()` зі значенням `StackqlMode.Safe`, `StackqlMode.DeleteSafe` або `StackqlMode.FullAccess`. Підвищення рівня доступу відбувається лише явно - сервер не вмикає інструменти змінення даних і операцій життєвого циклу, якщо ви явно їх не ввімкнете.

* * *

## Вендоринг / вбудовування {#vendoring--embedding}

Отримати двійковий файл можна двома способами:

- **Sidecar (за замовчуванням)** - пакет для платформи завантажується під час першого запуску, перевіряється за sha256, кешується в `~/.stackql/mcp-server-bin/<version>/<platform-key>/` і запускається через stdio.
- **Вендоринг** - вбудуйте пакет як ресурс збірки, щоб отримати автономний виконуваний файл:

```
dotnet build -p:StackqlVendorBundle=/path/to/stackql-mcp-windows-x64.mcpb
```

Двійковий файл або пакет також можна перевизначити під час виконання за допомогою змінних середовища `STACKQL_MCP_BIN` або `STACKQL_MCP_BUNDLE`.

* * *

## Демонстраційний застосунок {#demo-app}

**driftwatch** - це .NET Worker Service, що вбудовує сервер StackQL у режимі лише для читання, за розкладом запускає набір перевірок дрейфу на SQL (публічний доступ, відсутні теги чи ліцензії, дрейф відносно базового стану) і надсилає результати в Adaptive Card у Teams.

* * *

## Посилання на репозиторій {#repo-link}

[github.com/stackql/stackql-mcp-dotnet](https://github.com/stackql/stackql-mcp-dotnet)
