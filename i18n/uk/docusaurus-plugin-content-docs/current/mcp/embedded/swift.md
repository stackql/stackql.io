---
title: Swift
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - вбудований
  - swift
  - macos
description: Включіть нотаризований двійковий файл сервера MCP StackQL до підписаного застосунку macOS
image: "/img/stackql-featured-image.png"
---

# Swift

Вбудуйте сервер MCP StackQL у застосунок macOS. Підписаний нотаризований двійковий файл можна включити до вашого пакета `.app`, а клієнт - офіційний SDK MCP для Swift.

:::info
Вбудований двійковий файл завантажується й перевіряється за sha256 під час першого запуску або включається у ваш застосунок через вендоринг. Див. [Спільний контракт](/mcp/embedded#common-contract).
:::

* * *

## Встановлення {#install}

Додайте пакет до вашого файлу `Package.swift`:

```swift
.package(url: "https://github.com/stackql/stackql-mcp-swift.git", from: "0.1.0")
```

Потрібні macOS 13+ і Swift 6.1 (Xcode 16.3+). Єдина залежність - офіційний SDK MCP для Swift.

* * *

## Швидкий старт {#quick-start}

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

API складається з `StackQLServer.start(Options)`, `.listToolNames()`, `.call(_:_:)` і `.stop()`. Тип `Options` містить `mode`, `auth`, `binaryOverride` і `allowDownload`. Для зовнішніх harness `StackQLServer.resolveCommand(_:)` повертає визначену команду запуску.

* * *

## Режими {#modes}

За замовчуванням `Options.mode` має значення `.readOnly`. Щоб підвищити рівень доступу, встановіть значення `.safe`, `.deleteSafe` або `.fullAccess`. Підвищення рівня доступу завжди явне - сервер не вмикає інструменти змінення даних і життєвого циклу, якщо ви явно їх не ввімкнете.

* * *

## Вендоринг / вбудовування {#vendoring--embedding}

Двійковий файл `darwin-universal` підписано сертифікатом Developer ID і нотаризовано Apple, тож його можна включити до підписаного пакета macOS `.app`, і власна нотаризація застосунку залишиться чинною. Це можливо лише з бібліотекою Swift - ви постачаєте один автономний нотаризований застосунок без завантаження під час першого запуску.

Порядок визначення двійкового файлу:

1. Явно вказаний `binaryOverride`.
2. Ресурси, включені до пакета `.app`.
3. Спільний кеш у каталозі `~/.stackql/mcp-server-bin/`.
4. Завантаження з перевіркою sha256 (якщо встановлено `allowDownload`).

* * *

## Демонстраційний застосунок {#demo-app}

**CloudLens** - хмарний вартовий у рядку меню, який вбудовує нотаризований двійковий файл і виконує набір перевірок у режимі `read_only`.

* * *

## Посилання на репозиторій {#repo-link}

[github.com/stackql/stackql-mcp-swift](https://github.com/stackql/stackql-mcp-swift)
