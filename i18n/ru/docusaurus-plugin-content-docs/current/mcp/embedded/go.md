---
title: Go
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - встроенный
  - go
description: Встройте MCP-сервер StackQL в приложение Go с помощью go:embed или в режиме sidecar
image: "/img/stackql-featured-image.png"
---

# Go

Встройте MCP-сервер StackQL в скомпилированное приложение Go: либо как скачиваемый sidecar, либо включив его в ваш двоичный файл через вендоринг с помощью `go:embed`. Клиент - официальный MCP SDK для Go.

:::info
Встроенный двоичный файл скачивается и проверяется по sha256 при первом запуске либо включается в ваше приложение через вендоринг. См. [Общий контракт](/mcp/embedded#common-contract).
:::

* * *

## Установка {#install}

```
go get github.com/stackql/stackql-mcp-go
```

* * *

## Быстрый старт {#quick-start}

В этом примере предполагается, что двоичный файл включен через вендоринг: функцию `StackqlMCPBinary()` генерирует шаг получения `go:generate`, описанный в разделе [Вендоринг / встраивание](#vendoring--embedding).

```go
import (
    "context"
    "github.com/modelcontextprotocol/go-sdk/mcp"
    stackqlmcp "github.com/stackql/stackql-mcp-go/embed"
)

func main() {
    ctx := context.Background()
    client, err := stackqlmcp.StartServer(ctx, stackqlmcp.Options{
        Binary: StackqlMCPBinary(),
    })
    if err != nil {
        // handle error
    }
    defer client.Close()

    res, err := client.Session.CallTool(ctx, &mcp.CallToolParams{
        Name: "run_select_query",
        Arguments: map[string]any{"sql": "SELECT name FROM google.compute.regions WHERE project = 'my-project'"},
    })
    _ = res
}
```

API - это функция `StartServer(ctx, Options{Binary, Mode})`; возвращаемый клиент предоставляется официальным MCP SDK для Go.

* * *

## Режимы {#modes}

По умолчанию сервер запускается в режиме `read_only`. Чтобы повысить уровень доступа до `safe`, `delete_safe` или `full_access`, задайте поле `Options.Mode`. Повышение уровня доступа выполняется явно - сервер не включит инструменты изменения данных и жизненного цикла, пока вы сами их не включите.

* * *

## Вендоринг / встраивание {#vendoring--embedding}

Включите двоичный файл через вендоринг с помощью `go:embed` и шага получения `go:generate`, чтобы собрать один автономный исполняемый файл:

```
//go:generate go run github.com/stackql/stackql-mcp-go/cmd/stackql-mcp-fetch -platform auto -package main
```

Шаг получения скачивает подписанный двоичный файл для целевой платформы, проверяет его по sha256 и записывает туда, откуда `go:embed` может включить его в вашу сборку. Во время выполнения функция `StackqlMCPBinary()` возвращает встроенный двоичный файл, поэтому скачивание при первом запуске не требуется.

* * *

## Демонстрационное приложение {#demo-app}

**sandboxctl** - инфраструктурный консьерж: встроенный сервер и цикл агента Claude в одном двоичном файле, который планирует, проверяет стоимость и создает временные облачные песочницы только после подтверждения.

* * *

## Ссылка на репозиторий {#repo-link}

[github.com/stackql/stackql-mcp-go](https://github.com/stackql/stackql-mcp-go)
