---
title: Как встроить MCP-сервер StackQL в приложение Swift для macOS
description: Включите нотариально заверенный MCP-сервер StackQL в подписанное приложение Swift для macOS, чтобы приложение в строке меню или настольное приложение выполняло SQL-запросы к облаку без потери нотариального заверения.
keywords: [stackql, mcp, swift, macos, swiftpm, нотариальное заверение, подпись кода, встроенный MCP-сервер, model context protocol, swift mcp sdk, cloudlens]
proficiencyLevel: Intermediate
faq:
  - question: Как добавить встроенный MCP-сервер StackQL в приложение Swift для macOS?
    answer: Добавьте пакет Swift из github.com/stackql/stackql-mcp-swift (начиная с версии 0.1.0), затем вызовите `StackQLServer.start(options)`, задав для Options.mode значение .readOnly. Клиентом служит официальный MCP SDK для Swift. Требуются macOS 13+ и Swift 6.1 (Xcode 16.3+).
  - question: Можно ли включить StackQL в подписанное и нотариально заверенное приложение macOS?
    answer: Да, и эта возможность есть только у библиотеки для Swift. Двоичный файл darwin-universal подписан сертификатом Developer ID и нотариально заверен Apple, поэтому его можно включить в ваш подписанный пакет .app, сохранив действительным нотариальное заверение самого приложения. Вы поставляете одно автономное нотариально заверенное приложение без скачивания при первом запуске.
  - question: В каком порядке библиотека для Swift определяет двоичный файл StackQL?
    answer: Сначала явно заданное поле binaryOverride, затем ресурсы, включенные в пакет .app, затем общий кэш в каталоге ~/.stackql/mcp-server-bin/, затем скачивание с проверкой sha256, если задано поле allowDownload. Если включить нотариально заверенный двоичный файл в ресурсы приложения, приоритет получает путь внутри пакета и ничего не скачивается.
  - question: Как ограничить встроенный MCP-сервер StackQL режимом только для чтения в Swift?
    answer: Задайте Options.mode = .readOnly; это значение используется по умолчанию. ReadOnly отклоняет INSERT, UPDATE, DELETE и EXEC. Повышайте уровень доступа до .safe, .deleteSafe или .fullAccess, только когда приложению нужно создавать ресурсы - повышение уровня доступа всегда выполняется явно.
---

# Как встроить MCP-сервер StackQL в приложение Swift для macOS

MCP-сервер StackQL может работать внутри нативного приложения macOS без отдельной установки и без зависимостей во время выполнения. Пакет `stackql-mcp-swift` запускает подписанный двоичный файл StackQL и работает с ним через stdio с помощью официального клиента MCP SDK для Swift. Поскольку двоичный файл `darwin-universal` подписан сертификатом Developer ID и нотариально заверен Apple, его можно включить в подписанный пакет `.app` и сохранить действительным нотариальное заверение приложения - на этом подходе построен **CloudLens**, облачный страж в строке меню.

## Шаги {#steps}

1. **Добавьте пакет** в файл `Package.swift`:

```swift
.package(url: "https://github.com/stackql/stackql-mcp-swift.git", from: "0.1.0")
```

Требуются macOS 13+ и Swift 6.1 (Xcode 16.3+). Единственная зависимость - официальный MCP SDK для Swift.

2. **Запустите сервер и вызовите инструмент.** Поле `Options.mode` по умолчанию имеет значение `.readOnly`:

```swift
import StackQLMCP

var options = Options()
options.mode = .readOnly
options.auth = ["github": ["type": "null_auth"]]

let server = try await StackQLServer.start(options)
let tools = try await server.listToolNames()
print("\(tools.count) tools available")

let services = try await server.call(
    "list_services", ["provider": "github", "row_limit": 5])
print(services.text)

await server.stop()
```

Для внешних harness метод `StackQLServer.resolveCommand(_:)` возвращает итоговую команду запуска.

3. **Выберите режим.** Поле `Options.mode` по умолчанию имеет значение `.readOnly`. Задавайте `.safe`, `.deleteSafe` или `.fullAccess`, только когда приложению нужно создавать ресурсы. Повышение уровня доступа выполняется явно.

## Включение нотариально заверенного двоичного файла в подписанное приложение {#bundle-the-notarised-binary-in-a-signed-app}

Двоичный файл `darwin-universal` подписан сертификатом Developer ID и нотариально заверен Apple, поэтому его можно включить в подписанный пакет `.app` для macOS, сохранив действительным нотариальное заверение самого приложения. Порядок определения двоичного файла:

1. Явно заданное поле `binaryOverride`.
2. Ресурсы, включенные в пакет `.app`.
3. Общий кэш в каталоге `~/.stackql/mcp-server-bin/`.
4. Скачивание с проверкой sha256 (если задано поле `allowDownload`).

Поместите нотариально заверенный двоичный файл в ресурсы приложения, и приоритет получит путь внутри пакета - приложение будет полностью автономным, без скачивания при первом запуске.

## Демонстрационное приложение {#demo-app}

**CloudLens** - облачный страж в строке меню, который встраивает нотариально заверенный двоичный файл и выполняет набор проверок в режиме `read_only`.

## Связанные концепции {#related-concepts}

- [Встроенный MCP: справочник по Swift](/mcp/embedded/swift) - установка, API и определение двоичного файла
- [Как использовать StackQL с ИИ-агентами](/ai/how-tos/use-stackql-with-ai-agents) - модель MCP и режимы безопасности
- [Архитектура MCP-сервера StackQL](/ai/architecture/stackql-mcp-architecture) - внутреннее устройство транспортов, режимов и аудита
- [Что такое агентная инфраструктура?](/ai/canonical-definitions/what-is-agentic-infrastructure) - подход, который это делает возможным
