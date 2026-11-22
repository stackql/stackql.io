---
title: Swift
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - встроенный
  - swift
  - macos
description: Включите нотариально заверенный двоичный файл MCP-сервера StackQL в подписанное приложение macOS
image: "/img/stackql-featured-image.png"
---

# Swift

Встройте MCP-сервер StackQL в приложение macOS. Подписанный нотариально заверенный двоичный файл можно включить в ваш пакет `.app`, а клиентом служит официальный MCP SDK для Swift.

:::info
Встроенный двоичный файл скачивается и проверяется по sha256 при первом запуске либо включается в ваше приложение через вендоринг. См. [Общий контракт](/mcp/embedded#common-contract).
:::

* * *

## Установка {#install}

Добавьте пакет в файл `Package.swift`:

```swift
.package(url: "https://github.com/stackql/stackql-mcp-swift.git", from: "0.1.0")
```

Требуются macOS 13+ и Swift 6.1 (Xcode 16.3+). Единственная зависимость - официальный MCP SDK для Swift.

* * *

## Быстрый старт {#quick-start}

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

API включает `StackQLServer.start(Options)`, `.listToolNames()`, `.call(_:_:)` и `.stop()`. Тип `Options` содержит поля `mode`, `auth`, `binaryOverride` и `allowDownload`. Для внешних harness метод `StackQLServer.resolveCommand(_:)` возвращает итоговую команду запуска.

* * *

## Режимы {#modes}

Поле `Options.mode` по умолчанию имеет значение `.readOnly`. Чтобы повысить уровень доступа, задайте значение `.safe`, `.deleteSafe` или `.fullAccess`. Повышение уровня доступа выполняется явно - сервер не включит инструменты изменения данных и жизненного цикла, пока вы сами их не включите.

* * *

## Вендоринг / встраивание {#vendoring--embedding}

Двоичный файл `darwin-universal` подписан сертификатом Developer ID и нотариально заверен Apple, поэтому его можно включить в подписанный пакет `.app` для macOS, сохранив действительным нотариальное заверение самого приложения. Эта возможность есть только у библиотеки для Swift - вы поставляете одно автономное нотариально заверенное приложение без скачивания при первом запуске.

Порядок определения двоичного файла:

1. Явно заданное поле `binaryOverride`.
2. Ресурсы, включенные в пакет `.app`.
3. Общий кэш в каталоге `~/.stackql/mcp-server-bin/`.
4. Скачивание с проверкой sha256 (если задано поле `allowDownload`).

* * *

## Демонстрационное приложение {#demo-app}

**CloudLens** - облачный страж в строке меню, который встраивает нотариально заверенный двоичный файл и выполняет набор проверок в режиме `read_only`.

* * *

## Ссылка на репозиторий {#repo-link}

[github.com/stackql/stackql-mcp-swift](https://github.com/stackql/stackql-mcp-swift)
