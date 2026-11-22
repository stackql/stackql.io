---
title: Как встроить MCP-сервер StackQL в приложение Go
description: Встройте MCP-сервер StackQL в скомпилированный двоичный файл Go с помощью go:embed или в режиме sidecar, чтобы агентное приложение Go выполняло запросы к облаку и создавало ресурсы на SQL без зависимости от среды выполнения.
keywords: [stackql, mcp, go, golang, go:embed, встроенный MCP-сервер, model context protocol, mcp sdk для go, агентная инфраструктура, sandboxctl]
proficiencyLevel: Intermediate
faq:
  - question: Как добавить встроенный MCP-сервер StackQL в приложение Go?
    answer: "Выполните `go get github.com/stackql/stackql-mcp-go`, затем вызовите `stackqlmcp.StartServer(ctx, stackqlmcp.Options{Binary: StackqlMCPBinary()})`. Возвращаемый клиент - это клиент официального MCP SDK для Go, поэтому инструменты вызываются с помощью `client.Session.CallTool`. При первом запуске подписанный двоичный файл скачивается в общий кэш в ~/.stackql/mcp-server-bin/ и проверяется по sha256 либо включается в ваш двоичный файл через вендоринг с помощью go:embed."
  - question: Можно ли поставлять один автономный двоичный файл Go со встроенным StackQL?
    answer: Да. Добавьте шаг получения go:generate (`//go:generate go run github.com/stackql/stackql-mcp-go/cmd/stackql-mcp-fetch -platform auto -package main`), который скачивает двоичный файл для платформы и проверяет его по sha256, после чего go:embed включает его в сборку. Во время выполнения функция StackqlMCPBinary() возвращает встроенный двоичный файл, поэтому скачивание при первом запуске и отдельная установка не нужны.
  - question: Как оставить встроенный MCP-сервер StackQL в режиме только для чтения в Go?
    answer: По умолчанию сервер запускается в режиме read_only, который отклоняет INSERT, UPDATE, DELETE и EXEC. Явно задавайте Options.Mode значение safe, delete_safe или full_access, только если приложению нужно создавать ресурсы. Повышение уровня доступа всегда выполняется явно - сервер не включит инструменты изменения данных или операций жизненного цикла, пока вы явно не включите их.
  - question: Какой MCP-клиент использует stackql-mcp-go?
    answer: Официальный MCP SDK для Go (github.com/modelcontextprotocol/go-sdk/mcp). Функция StartServer возвращает клиент, к Session которого вы обращаетесь напрямую, поэтому вы не привязываетесь к клиентскому API, специфичному для StackQL.
---

# Как встроить MCP-сервер StackQL в приложение Go

MCP-сервер StackQL может работать внутри скомпилированной программы Go без `npx`, без отдельной установки и без зависимости от среды выполнения. Библиотека `stackql-mcp-go` запускает подписанный двоичный файл StackQL через stdio и работает с ним через клиент официального MCP SDK для Go, поэтому ваше приложение Go получает контролируемый самоописываемый SQL-интерфейс ко всем провайдерам облачных и SaaS-сервисов в реестре StackQL. На этой схеме основан **sandboxctl** - инфраструктурный консьерж, работающий по запросу и поставляемый в виде одного двоичного файла.

## Шаги {#steps}

1. **Добавьте библиотеку:**

```
go get github.com/stackql/stackql-mcp-go
```

2. **Запустите встроенный сервер и вызовите инструмент.** По умолчанию сервер запускается в режиме `read_only`:

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

3. **Выберите режим.** По умолчанию поле `Options.Mode` имеет значение `read_only`. Задавайте значение `safe`, `delete_safe` или `full_access`, только если приложению нужно создавать ресурсы. Повышение уровня доступа выполняется явно.

## Поставка одного двоичного файла с помощью go:embed {#ship-a-single-binary-with-goembed}

Чтобы получить автономный исполняемый файл, добавьте шаг получения `go:generate`, который скачивает двоичный файл для платформы и проверяет его по sha256, а затем включите его в сборку с помощью `go:embed`:

```
//go:generate go run github.com/stackql/stackql-mcp-go/cmd/stackql-mcp-fetch -platform auto -package main
```

Во время выполнения функция `StackqlMCPBinary()` возвращает встроенный двоичный файл, поэтому скачивание при первом запуске не требуется. Без вендоринга двоичный файл при первом запуске скачивается в общий кэш в каталоге `~/.stackql/mcp-server-bin/` и проверяется.

## Демонстрационное приложение {#demo-app}

**sandboxctl** - инфраструктурный консьерж: встроенный MCP-сервер StackQL и цикл агента Claude в одном двоичном файле Go, который планирует, проверяет стоимость и создает временные облачные песочницы только после подтверждения.

## Связанные концепции {#related-concepts}

- [Встроенный MCP: справочник по Go](/mcp/embedded/go) - установка, API и режимы
- [Как использовать StackQL с ИИ-агентами](/ai/how-tos/use-stackql-with-ai-agents) - модель MCP и режимы безопасности
- [Архитектура MCP-сервера StackQL](/ai/architecture/stackql-mcp-architecture) - транспорты, режимы и внутреннее устройство аудита
- [Что такое агентная инфраструктура?](/ai/canonical-definitions/what-is-agentic-infrastructure) - подход, который это делает возможным
